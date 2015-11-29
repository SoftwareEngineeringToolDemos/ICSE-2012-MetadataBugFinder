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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import org.eclipse.core.runtime.CoreException;

import org.eclipse.jface.dialogs.Dialog;

import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;

import org.eclipse.jdt.core.IMethod;

import org.eclipse.jdt.internal.corext.refactoring.rename.RenameFieldProcessor;
import org.eclipse.jdt.internal.corext.util.Messages;

import org.eclipse.jdt.internal.ui.IJavaHelpContextIds;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.refactoring.RefactoringMessages;
import org.eclipse.jdt.internal.ui.viewsupport.BasicElementLabels;

// *------- customized import package
import relation.annotation.ctrl.CtrlClassType;
import relation.progelem.AnnotatedClassDecl;
import dsl.gen.ParameterHelper;
import metadata.invariant.pbse.*;
import util.*;

// import pbse.ChangeFieldName;
// import pbse.ChangeSourceCode;
// import pbse.StartMain;

public class RenameFieldWizard extends RenameRefactoringWizard {

	public RenameFieldWizard(Refactoring refactoring) {
		super(refactoring, RefactoringMessages.RenameFieldWizard_defaultPageTitle, RefactoringMessages.RenameFieldWizard_inputPage_description, JavaPluginImages.DESC_WIZBAN_REFACTOR_FIELD, IJavaHelpContextIds.RENAME_FIELD_WIZARD_PAGE);
	}

	protected RenameInputWizardPage createInputPage(String message, String initialSetting)
	{
		return new RenameFieldInputWizardPage(message, IJavaHelpContextIds.RENAME_FIELD_WIZARD_PAGE, initialSetting) {
			protected RefactoringStatus validateTextField(String text)
			{
				RefactoringStatus result = validateNewName(text);
				updateGetterSetterLabels();
				return result;
			}
		};
	}

	private static class RenameFieldInputWizardPage extends RenameInputWizardPage implements CtrlClassType {

		private Button	fRenameGetter;
		private Button	fRenameSetter;
		private String	fGetterRenamingErrorMessage;
		private String	fSetterRenamingErrorMessage;

		public RenameFieldInputWizardPage(String message, String contextHelpId, String initialValue) {
			super(message, contextHelpId, true, initialValue);
		}

		public void createControl(Composite parent)
		{
			super.createControl(parent);
			Composite parentComposite = (Composite) getControl();

			Composite composite = new Composite(parentComposite, SWT.NONE);
			final GridLayout layout = new GridLayout();
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			composite.setLayout(layout);
			composite.setLayoutData(new GridData(GridData.FILL_BOTH));

			Label separator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
			separator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			getGetterSetterRenamingEnablement();

			fRenameGetter = new Button(composite, SWT.CHECK);
			boolean getterEnablement = fGetterRenamingErrorMessage == null;
			fRenameGetter.setEnabled(getterEnablement);
			boolean getterSelection =
					getterEnablement &&
							getBooleanSetting(RenameRefactoringWizard.FIELD_RENAME_GETTER, getRenameFieldProcessor().getRenameGetter());
			fRenameGetter.setSelection(getterSelection);
			getRenameFieldProcessor().setRenameGetter(getterSelection);
			fRenameGetter.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			fRenameGetter.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e)
				{
					getRenameFieldProcessor().setRenameGetter(fRenameGetter.getSelection());
					updateLeaveDelegateCheckbox(getRenameFieldProcessor().getDelegateCount());
				}
			});

			fRenameSetter = new Button(composite, SWT.CHECK);
			boolean setterEnablement = fSetterRenamingErrorMessage == null;
			fRenameSetter.setEnabled(setterEnablement);
			boolean setterSelection =
					setterEnablement &&
							getBooleanSetting(RenameRefactoringWizard.FIELD_RENAME_SETTER, getRenameFieldProcessor().getRenameSetter());
			fRenameSetter.setSelection(setterSelection);
			getRenameFieldProcessor().setRenameSetter(setterSelection);
			fRenameSetter.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			fRenameSetter.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e)
				{
					getRenameFieldProcessor().setRenameSetter(fRenameSetter.getSelection());
					updateLeaveDelegateCheckbox(getRenameFieldProcessor().getDelegateCount());
				}
			});

			updateGetterSetterLabels();
			updateLeaveDelegateCheckbox(getRenameFieldProcessor().getDelegateCount());
			Dialog.applyDialogFont(composite);
		}

		/**
		 * @METHOD
		 * @author mksong
		 * @description renaming a field name.
		 */
		public void dispose()
		{
			Open.afterFileContents = //
				UtilFile.fileRead2List(ParameterHelper.getGlobalParm("target") + //
						Open.refactoredFileName);
			
			// * Infer metadata-invariatns.
			if (Comm.getGlobalParm("inference").equalsIgnoreCase("true"))
				inferMetadataInvariants();

			if (Comm.getGlobalParm("eclipse-violation-check").equalsIgnoreCase("true"))
				checkMetadataInvariants();

			// * existing code
			if (saveSettings()) {
				if (fRenameGetter.isEnabled())
					saveBooleanSetting(RenameRefactoringWizard.FIELD_RENAME_GETTER, fRenameGetter);
				if (fRenameSetter.isEnabled())
					saveBooleanSetting(RenameRefactoringWizard.FIELD_RENAME_SETTER, fRenameSetter);
			}
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
			// ChangeSourceCode change = ChangeFieldName.getInstance();
			// change.setBeforeStr(oldStr);
			// change.setAfterStr(newStr);
			// System.out.println("[DBG]" + change);
			String oldFieldName = getInitialValue();
			String newFieldName = getText();
			// System.out.println("[DBG] change: " + oldFieldName + " -> " + newFieldName);

			// * check metadata invariants!
			ParameterHelper ph = new ParameterHelper();
			String metadata = ParameterHelper.getParm("metadata");
			String pattern = ph.getPattern();
			// System.out.println("[DBG]" + pattern);
			// System.out.println("[DBG]" + metadata);

			if (metadata.equals(STR.parm_annotation)) {
				String refactoredFileName = "";
				refactoredFileName += ParameterHelper.getGlobalParm("target");
				refactoredFileName += Open.refactoredFileName;

				List<String> fieldlist = UtilAST.getFieldList(refactoredFileName, false);
				// UtilPrint.printArrayList(fieldlist);
				// >>>
				String theAnnotation = getAnnotation(fieldlist, newFieldName);
				// >>>
				if (theAnnotation == null)
					theAnnotation = getAnnotation(refactoredFileName, Comm.getParm("relation"));

				if (pattern.startsWith(STR.pbse_ptrn_match)) {
					boolean result = checkMatchAssert(newFieldName, theAnnotation);
					// displayResult(result);
					displayResult(result, Open.refactoredFileName, oldFieldName, newFieldName);
				}
				else if (pattern.startsWith(STR.pbse_ptrn_contains)) {
					boolean result = checkContainsAssertExists(newFieldName, theAnnotation);
					// displayResult(result);
					displayResult(result, Open.refactoredFileName, oldFieldName, newFieldName);
				}
			}
		}

		/** @METHOD */
		void displayResult(boolean result)
		{
			System.out.println("------------------------------------------");
			if (result) {
				System.out.println("[DBG] OK !!!");
			}
			else {
				System.out.println("[DBG] Violate !!!");
			}
			System.out.println("------------------------------------------");
		}

		void displayResult(boolean result, String filename, String src, String dst)
		{
			UtilPrint.displayResult(result, filename, src, dst);
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
		private String getAnnotation(List<String> fieldlist, String fieldname)
		{
			String elem = "";
			for (int i = 0; i < fieldlist.size(); i++) {
				elem = fieldlist.get(i);
				if (elem.contains(fieldname))
					break;
			}
			String attr = UtilStr.getStrBetween(elem, "(", ")");
			if (attr == null)
				return null;
			else if (attr.contains("=")) {
				attr = attr.split("=")[1].trim();
			}
			attr = attr.replace("\"", "");
			return attr;
		}

		private String getAnnotation(String refactoredFileName, String relation)
		{
			String relations[] = relation.split(",");
			int i = 0;
			for (i = 0; i < relations.length; i++) {
				if (relations[i].contains("@"))
					break;
			}
			AnnotatedClassDecl clazzDecl = UtilAST.getClassList(refactoredFileName, this);
			String annotation = clazzDecl.getNormalAnnotation(relations[i].trim());
			String attrValues[] = UtilStr.getValues(annotation, relations[i - 1].trim());

			if (attrValues != null && attrValues.length != 0) {
				annotation = "#";
				for (int j = 0; j < attrValues.length; j++) {
					annotation += attrValues[j];
					if (j != (attrValues.length - 1))
						annotation += ",";
				}
			}
			return annotation;
		}

		@Override
		public AnnotatedClassDecl cbfGetAnnotatedClassList(List<AnnotatedClassDecl> clazzlist)
		{
			AnnotatedClassDecl result = clazzlist.get(0);
			return result;
		}

		/** @METHOD */
		public boolean checkMatchAssert(String newFieldName, String theAnnotation)
		{
			// ParameterHelper ph = new ParameterHelper();
			// String pattern = ph.getPattern();
			// pattern = MATCH($ANNOTATION_ATTRIBUTE.NAME-OR-EMPTY, $FIELD_NAME)
			if (newFieldName.equalsIgnoreCase(theAnnotation))
				return true;
			if (theAnnotation.endsWith(newFieldName))
				return true;
			return false;
		}

		/**
		 * @METHOD
		 */
		public boolean checkContainsAssertExists(String newFieldName, String theAnnotation)
		{
			// pattern:CONTAINS($METADATA.ATTRVAL, $FIELD)
			// System.out.println("[DBG]" + theAnnotation);
			if (theAnnotation.startsWith("#")) {
				theAnnotation = theAnnotation.substring(1);
				String attrValues[] = theAnnotation.split(",");

				for (int i = 0; i < attrValues.length; i++) {
					String elem = attrValues[i].trim();
					if (elem.equals(newFieldName))
						return true;
				}
			}
			return false;
		}

		/**
		 * @METHOD
		 */
		private void getGetterSetterRenamingEnablement()
		{
			BusyIndicator.showWhile(getShell().getDisplay(), new Runnable() {
				public void run()
				{
					checkGetterRenamingEnablement();
					checkSetterRenamingEnablement();
				}
			});
		}

		protected void updateGetterSetterLabels()
		{
			fRenameGetter.setText(getRenameGetterLabel());
			fRenameSetter.setText(getRenameSetterLabel());
		}

		private String getRenameGetterLabel()
		{
			String defaultLabel = RefactoringMessages.RenameFieldInputWizardPage_rename_getter;
			if (fGetterRenamingErrorMessage != null)
				return constructDisabledGetterRenamingLabel(defaultLabel);
			try {
				IMethod getter = getRenameFieldProcessor().getGetter();
				if (getter == null || !getter.exists())
					return defaultLabel;
				String oldGetterName = BasicElementLabels.getJavaElementName(getter.getElementName());
				String newGetterName = BasicElementLabels.getJavaElementName(createNewGetterName());
				return Messages.format(RefactoringMessages.RenameFieldInputWizardPage_rename_getter_to, new String[] {
						oldGetterName, newGetterName });
			} catch (CoreException e) {
				JavaPlugin.log(e);
				return defaultLabel;
			}
		}

		private String getRenameSetterLabel()
		{
			String defaultLabel = RefactoringMessages.RenameFieldInputWizardPage_rename_setter;
			if (fSetterRenamingErrorMessage != null)
				return constructDisabledSetterRenamingLabel(defaultLabel);
			try {
				IMethod setter = getRenameFieldProcessor().getSetter();
				if (setter == null || !setter.exists())
					return defaultLabel;
				String oldSetterName = BasicElementLabels.getJavaElementName(setter.getElementName());
				String newSetterName = BasicElementLabels.getJavaElementName(createNewSetterName());
				return Messages.format(RefactoringMessages.RenameFieldInputWizardPage_rename_setter_to, new String[] {
						oldSetterName, newSetterName });
			} catch (CoreException e) {
				JavaPlugin.log(e);
				return defaultLabel;
			}
		}

		private String constructDisabledSetterRenamingLabel(String defaultLabel)
		{
			if (fSetterRenamingErrorMessage.equals("")) //$NON-NLS-1$
				return defaultLabel;
			String[] keys = { defaultLabel, fSetterRenamingErrorMessage };
			return Messages.format(RefactoringMessages.RenameFieldInputWizardPage_setter_label, keys);
		}

		private String constructDisabledGetterRenamingLabel(String defaultLabel)
		{
			if (fGetterRenamingErrorMessage.equals("")) //$NON-NLS-1$
				return defaultLabel;
			String[] keys = { defaultLabel, fGetterRenamingErrorMessage };
			return Messages.format(RefactoringMessages.RenameFieldInputWizardPage_getter_label, keys);
		}

		private String createNewGetterName() throws CoreException
		{
			return getRenameFieldProcessor().getNewGetterName();
		}

		private String createNewSetterName() throws CoreException
		{
			return getRenameFieldProcessor().getNewSetterName();
		}

		private String checkGetterRenamingEnablement()
		{
			if (fGetterRenamingErrorMessage != null)
				return fGetterRenamingErrorMessage;
			try {
				fGetterRenamingErrorMessage = getRenameFieldProcessor().canEnableGetterRenaming();
				return fGetterRenamingErrorMessage;
			} catch (CoreException e) {
				JavaPlugin.log(e);
				return ""; //$NON-NLS-1$
			}
		}

		private String checkSetterRenamingEnablement()
		{
			if (fSetterRenamingErrorMessage != null)
				return fSetterRenamingErrorMessage;
			try {
				fSetterRenamingErrorMessage = getRenameFieldProcessor().canEnableSetterRenaming();
				return fSetterRenamingErrorMessage;
			} catch (CoreException e) {
				JavaPlugin.log(e);
				return ""; //$NON-NLS-1$
			}
		}

		private RenameFieldProcessor getRenameFieldProcessor()
		{
			return (RenameFieldProcessor) ((RenameRefactoring) getRefactoring()).getProcessor();
		}
	}
}
