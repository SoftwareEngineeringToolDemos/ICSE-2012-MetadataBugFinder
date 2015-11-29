/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.ui.refactoring.reorg;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import metadata.invariant.pbse.Comm;
import metadata.invariant.pbse.STR;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;

import org.eclipse.jdt.internal.corext.refactoring.rename.RenameTypeProcessor;
import org.eclipse.jdt.internal.corext.refactoring.tagging.ISimilarDeclarationUpdating;

import org.eclipse.jdt.internal.ui.refactoring.RefactoringMessages;
import org.eclipse.jdt.internal.ui.util.ExceptionHandler;
import org.eclipse.jdt.internal.ui.util.RowLayouter;

import relation.progelem.AnnotatedClassDecl;

import util.Open;
import util.UtilAST;
import util.UtilDirScan;
import util.UtilFile;
import util.UtilPrint;
import util.UtilStr;
import util.UtilXML;

import dsl.gen.ParameterHelper;

/**
 * Wizard page for renaming a type (with similarly named elements)
 * 
 * @since 3.2
 */
class RenameTypeWizardInputPage extends RenameInputWizardPage {

	private Button	fUpdateSimilarElements;
	private int		fSelectedStrategy;

	private Link	fUpdateSimilarElementsButton;

	public RenameTypeWizardInputPage(String description, String contextHelpId, boolean isLastUserPage, String initialValue) {
		super(description, contextHelpId, isLastUserPage, initialValue);
	}

	protected void addAdditionalOptions(Composite composite, RowLayouter layouter)
	{

		if (getSimilarElementUpdating() == null ||
				!getSimilarElementUpdating().canEnableSimilarDeclarationUpdating())
			return;

		try {
			fSelectedStrategy = getRefactoringSettings().getInt(RenameRefactoringWizard.TYPE_SIMILAR_MATCH_STRATEGY);
		} catch (NumberFormatException e) {
			fSelectedStrategy = getSimilarElementUpdating().getMatchStrategy();
		}

		getSimilarElementUpdating().setMatchStrategy(fSelectedStrategy);

		Composite c = new Composite(composite, SWT.NULL);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		c.setLayout(layout);

		fUpdateSimilarElements = new Button(c, SWT.CHECK);
		fUpdateSimilarElements.setText(RefactoringMessages.RenameTypeWizardInputPage_update_similar_elements);

		final boolean updateSimilarElements =
				getBooleanSetting(RenameRefactoringWizard.TYPE_UPDATE_SIMILAR_ELEMENTS, getSimilarElementUpdating().getUpdateSimilarDeclarations());
		fUpdateSimilarElements.setSelection(updateSimilarElements);
		getSimilarElementUpdating().setUpdateSimilarDeclarations(updateSimilarElements);
		fUpdateSimilarElements.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fUpdateSimilarElements.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e)
			{
				getSimilarElementUpdating().setUpdateSimilarDeclarations(fUpdateSimilarElements.getSelection());
				fUpdateSimilarElementsButton.setEnabled(fUpdateSimilarElements.getSelection());
			}
		});

		fUpdateSimilarElementsButton = new Link(c, SWT.NONE);
		GridData d = new GridData();
		d.grabExcessHorizontalSpace = true;
		d.horizontalAlignment = SWT.RIGHT;
		fUpdateSimilarElementsButton.setText(RefactoringMessages.RenameTypeWizardInputPage_update_similar_elements_configure);
		fUpdateSimilarElementsButton.setEnabled(updateSimilarElements);
		fUpdateSimilarElementsButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e)
			{
				RenameTypeWizardSimilarElementsOptionsDialog dialog =
						new RenameTypeWizardSimilarElementsOptionsDialog(getShell(), fSelectedStrategy);
				if (dialog.open() == Window.OK) {
					fSelectedStrategy = dialog.getSelectedStrategy();
					getSimilarElementUpdating().setMatchStrategy(fSelectedStrategy);
				}
			}
		});
		fUpdateSimilarElementsButton.setLayoutData(d);

		GridData forC = new GridData();
		forC.grabExcessHorizontalSpace = true;
		forC.horizontalAlignment = SWT.FILL;
		forC.horizontalSpan = 2;
		c.setLayoutData(forC);

		final Label separator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		layouter.perform(separator);
	}

	public void dispose()
	{
		if (saveSettings())
			if (fUpdateSimilarElements != null && !fUpdateSimilarElements.isDisposed() &&
					fUpdateSimilarElements.isEnabled()) {
				saveBooleanSetting(RenameRefactoringWizard.TYPE_UPDATE_SIMILAR_ELEMENTS, fUpdateSimilarElements);
				getRefactoringSettings().put(RenameRefactoringWizard.TYPE_SIMILAR_MATCH_STRATEGY, fSelectedStrategy);
			}

		// * ------- mksong, smk, customize code

		String oldName = getInitialValue();
		String newName = getText();

		String filepath = ParameterHelper.getGlobalParm("target") + Open.refactoredFileName;
		if (!UtilFile.fileExists(filepath)) {
			filepath = filepath.replace(oldName, newName);
			Open.setRefactFileName(Open.refactoredFileName.replace(oldName, newName));
		}

		Open.afterFileContents = UtilFile.fileRead2List(filepath);

		// System.out.println("[DBG] RenameTypeWizardInputPage: " + oldName + " -> " + newName);
		if (Comm.getGlobalParm("inference").equalsIgnoreCase("true"))
			inferMetadataInvariants();

		if (Comm.getGlobalParm("eclipse-violation-check").equalsIgnoreCase("true"))
			checkMetadataInvariants();

		// * ------- mksong, smk, customize code

		super.dispose();
	}

	/**
	 * @METHOD
	 * @author mksong
	 */
	private void inferMetadataInvariants()
	{
		// StartMain m = new StartMain();
		// m.inferInvariantProc();
		dsl.Main startMain = new dsl.Main();
		startMain.generateMIL();
	}

	/**
	 * @METHOD
	 * @author mksong
	 */
	private void checkMetadataInvariants()
	{
		String oldTypeName = getInitialValue();
		String newTypeName = getText();
		// System.out.println("[DBG] change: " + oldTypeName + " -> " + newTypeName);

		// * check metadata invariants!
		ParameterHelper ph = new ParameterHelper();
		String metadata = Comm.getParm("metadata");
		String pattern = ph.getPattern();
		String relation = Comm.getParm("relation");

		// System.out.println("[DBG]" + pattern);
		// System.out.println("[DBG]" + metadata);
		// System.out.println("[DBG]" + relation);

		String refactoredFileName = Comm.getGlobalParm("target") + Open.refactoredFileName;
		// System.out.println("[DBG] filename: " + refactoredFileName);

		if (metadata.equals(STR.parm_annotation)) {
			if (pattern.startsWith(STR.pbse_ptrn_endswith)) {
				boolean result = checkEndsWithAssert(refactoredFileName, newTypeName);
				displayResult(result, oldTypeName, newTypeName, Open.refactoredFileName);
			}
		}
		else if (metadata.equalsIgnoreCase(STR.parm_xml)) {
			boolean result = false;

			if (pattern.split("\\+").length > 1)
				result = checkMultiXMLAssert(refactoredFileName, newTypeName);
			else
				result = checkSingleXMLAssert(refactoredFileName, newTypeName);

			displayResult(result, oldTypeName, newTypeName, Open.refactoredFileName);
		}
	}

	/** @METHOD */
	String transform(String src, String pattern)
	{
		String[] patternArray = pattern.split("\\+");
		String result = src;

		for (int i = 0; i < patternArray.length; i++) {
			String elem = patternArray[i].trim();
			String value = UtilStr.getStrBetween(elem, "(", ")");

			if (elem.startsWith(STR.pbse_ptrn_prefix)) {
				result = value + src;
			}
			else if (elem.startsWith(STR.pbse_ptrn_uppercase)) {
				result = result.toUpperCase();
			}
			else if (elem.startsWith(STR.pbse_ptrn_lowfirstchar)) {
				result = UtilStr.lwFirstChar(src);
			}
		}
		return result;
	}

	/** @METHOD */
	boolean checkEndsWithAssert(String refactoredFileName, String newTypeName)
	{
		ParameterHelper ph = new ParameterHelper();
		String pattern = ph.getPattern();

		String pairs = UtilStr.getStrBetween(pattern, "(", ")");
		String type = pairs.split(",")[0].trim();
		String annotation = pairs.split(",")[1].trim();

		if (type.equalsIgnoreCase(STR.var_classname) && //
				annotation.equalsIgnoreCase("$ANNOTATIONNAME")) {

			List<AnnotatedClassDecl> clazzList = UtilAST.getClassList(refactoredFileName);
			AnnotatedClassDecl elem = null;
			for (int i = 0; i < clazzList.size(); i++) {
				elem = clazzList.get(i);
				if (elem.name.endsWith(newTypeName))
					break;
			}
			annotation = elem.markerAnnotationList.isEmpty() ? //
					elem.normalAnnotationList.get(0) : //
					elem.markerAnnotationList.get(0);

			if (newTypeName.contains(annotation.replace("@", "")))
				return true;
		}
		return false;
	}

	/** @METHOD */
	boolean checkMultiXMLAssert(String refactoredFileName, String newTypeName)
	{
		// pattern:PREFIX(JBPM_) + UPPERCASE($CLASSNAME)
		ParameterHelper ph = new ParameterHelper();
		String pattern = ph.getPattern();
		String relation = Comm.getParm("relation");

		String transformedResult = transform(newTypeName, pattern);
		// System.out.println("[DBG] transformedResult:" + transformedResult);

		String xmlFilePath = refactoredFileName.replace(STR.file_java, Comm.getParm("xml-suffix"));

		if (!UtilFile.fileExists(xmlFilePath)) {
			int dirIdx = xmlFilePath.lastIndexOf("/");
			String searchXMLPath = xmlFilePath.substring(0, dirIdx);
			List<String> xmlList = UtilDirScan.getResultViaStr(searchXMLPath, "." + STR.parm_xml);
			// System.out.println("[DBG]" + xmlList.get(0));
			xmlFilePath = xmlList.get(0);
		}

		List<String> parmList = new ArrayList<String>();
		String[] paramArray = relation.split(",");
		for (int i = 0; i < paramArray.length; i++) {
			parmList.add(paramArray[i].trim());
		}

		List<String> xmlTagValues = UtilXML.readXMLTags(xmlFilePath, parmList);
		Open.xmlTagValues = xmlTagValues;
		Open.xmlFilePath = xmlFilePath;

		int correctCount = 0;
		for (int i = 0; i < xmlTagValues.size(); i++) {
			String elem = xmlTagValues.get(i);
			if (elem.endsWith(newTypeName)) {
				correctCount++;
			}
			else if (elem.equals(transformedResult)) {
				correctCount++;
			}
		}
		return (correctCount == xmlTagValues.size());
	}

	/** @METHOD */
	boolean checkSingleXMLAssert(String refactoredFileName, String newTypeName)
	{
		// LOWFIRSTCHAR($CLASSNAME)

		ParameterHelper ph = new ParameterHelper();
		String pattern = ph.getPattern();
		String relation = Comm.getParm("relation");

		String transformedResult = transform(newTypeName, pattern);
		// System.out.println("[DBG] transformedResult:" + transformedResult);

		String xmlFilePath = refactoredFileName.replace(STR.file_java, Comm.getParm("xml-suffix"));

		if (!UtilFile.fileExists(xmlFilePath)) {
			int dirIdx = xmlFilePath.lastIndexOf("/");
			String searchXMLPath = xmlFilePath.substring(0, dirIdx);
			List<String> xmlList = UtilDirScan.getResultViaStr(searchXMLPath, "." + STR.parm_xml);
			// System.out.println("[DBG]" + xmlList.get(0));
			xmlFilePath = xmlList.get(0);
		}

		List<String> parmList = new ArrayList<String>();
		String[] paramArray = relation.split(",");
		for (int i = 0; i < paramArray.length; i++) {
			parmList.add(paramArray[i].trim());
		}

		List<String> xmlTagValues = UtilXML.readXMLTags(xmlFilePath, parmList);
		Open.xmlTagValues = xmlTagValues;
		Open.xmlFilePath = xmlFilePath;

		int correctCount = 0;
		for (int i = 0; i < xmlTagValues.size(); i++) {
			String elem = xmlTagValues.get(i);
			if (elem.endsWith(newTypeName)) {
				correctCount++;
			}
			else if (elem.equals(transformedResult)) {
				correctCount++;
			}
			if (correctCount > 1) {
				break;
			}
		}

		if (correctCount > 1)
			return true;

		String oldTypeName = getInitialValue();
		String transformedoldTypeName = transform(oldTypeName, pattern);
		List<String> xmlTagValuesList = new ArrayList<String>();

		for (int i = 0; i < Open.xmlTagValues.size(); i++) {
			String elem = Open.xmlTagValues.get(i);

			if (elem.trim().equals(transformedoldTypeName)) {
				xmlTagValuesList.add(elem);
				xmlTagValuesList.add(Open.xmlTagValues.get(i + 1));
				i++;
			}
		}
		Open.xmlTagValues = xmlTagValuesList;

		return false;
	}

	/** @METHOD */
	void displayResult(boolean result, String src, String dst, String filename)
	{
		UtilPrint.displayResult(result, filename, src, dst);
		//
		// System.out.println("------------------------------------------");
		// if (result) {
		// System.out.println("[DBG] OK !!!");
		// }
		// else {
		// System.out.println("[DBG] Violate !!!");
		// System.out.println("[DBG] File:" + UtilStr.getShorfileName(filename));
		// System.out.println("[DBG] Changed: " + src + " -> " + dst);
		// }
		// System.out.println("------------------------------------------");
	}

	/** @METHOD */
	void displayResult(boolean result)
	{
		System.out.println("------------------------------------------");
		if (result) {
			System.out.println("[DBG] OK !!!");
		}
		else {
			System.out.println("[DBG] Violate Metadata Invariants !!!");
		}
		System.out.println("------------------------------------------");
	}

	/*
	 * Override - we don't want to initialize the next page (may needlessly
	 * trigger change creation if similar elements page is skipped, which is not
	 * indicated by fIsLastUserInputPage in parent).
	 */
	public boolean canFlipToNextPage()
	{
		return isPageComplete();
	}

	private ISimilarDeclarationUpdating getSimilarElementUpdating()
	{
		return (ISimilarDeclarationUpdating) getRefactoring().getAdapter(ISimilarDeclarationUpdating.class);
	}

	protected boolean performFinish()
	{
		boolean returner = super.performFinish();
		// check if we got deferred to the error page
		if (!returner && getContainer().getCurrentPage() != null)
			getContainer().getCurrentPage().setPreviousPage(this);
		return returner;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ltk.ui.refactoring.UserInputWizardPage#getNextPage()
	 */
	public IWizardPage getNextPage()
	{
		RenameTypeWizard wizard = (RenameTypeWizard) getWizard();
		IWizardPage nextPage;

		if (wizard.isRenameType()) {
			final RenameTypeProcessor renameTypeProcessor = wizard.getRenameTypeProcessor();
			try {
				getContainer().run(true, true, new IRunnableWithProgress() {

					public void run(IProgressMonitor pm) throws InterruptedException
					{
						try {
							renameTypeProcessor.initializeReferences(pm);
						} catch (OperationCanceledException e) {
							throw new InterruptedException();
						} catch (CoreException e) {
							ExceptionHandler.handle(e, RefactoringMessages.RenameTypeWizard_defaultPageTitle, RefactoringMessages.RenameTypeWizard_unexpected_exception);
						} finally {
							pm.done();
						}
					}
				});
			} catch (InvocationTargetException e) {
				ExceptionHandler.handle(e, getShell(), RefactoringMessages.RenameTypeWizard_defaultPageTitle, RefactoringMessages.RenameTypeWizard_unexpected_exception);
			} catch (InterruptedException e) {
				// user canceled
				return this;
			}

			if (renameTypeProcessor.hasSimilarElementsToRename()) {
				nextPage = super.getNextPage();
			}
			else {
				nextPage = computeSuccessorPage();
			}

		}
		else
			nextPage = computeSuccessorPage();

		nextPage.setPreviousPage(this);
		return nextPage;
	}
}
