/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.corext.refactoring.changes;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.core.resources.IResource;

import org.eclipse.ltk.core.refactoring.Change;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;

import org.eclipse.jdt.internal.corext.refactoring.AbstractJavaElementRenameChange;
import org.eclipse.jdt.internal.corext.refactoring.RefactoringCoreMessages;
import org.eclipse.jdt.internal.corext.util.Messages;

import org.eclipse.jdt.internal.ui.viewsupport.BasicElementLabels;

// import pbse.ChangeClassName;
// import pbse.ChangeSourceCode;
// import pbse.gen.visitor.CodeGeneratingVisitor;
// import util.UtilStr;

public final class RenameCompilationUnitChange extends AbstractJavaElementRenameChange {

	public RenameCompilationUnitChange(ICompilationUnit unit, String newName) {
		this(unit.getResource().getFullPath(), unit.getElementName(), newName, IResource.NULL_STAMP);
		Assert.isTrue(!unit.isReadOnly(), "compilation unit must not be read-only"); //$NON-NLS-1$
	}

	private RenameCompilationUnitChange(IPath resourcePath, String oldName, String newName, long stampToRestore) {
		super(resourcePath, oldName, newName, stampToRestore);

		setValidationMethod(VALIDATE_NOT_READ_ONLY | SAVE_IF_DIRTY);
	}

	protected IPath createNewPath()
	{
		final IPath path = getResourcePath();
		if (path.getFileExtension() != null)
			return path.removeFileExtension().removeLastSegments(1).append(getNewName());
		else
			return path.removeLastSegments(1).append(getNewName());
	}

	protected Change createUndoChange(long stampToRestore) throws JavaModelException
	{
		return new RenameCompilationUnitChange(createNewPath(), getNewName(), getOldName(), stampToRestore);
	}

	/**
	 * @METHOD
	 * @author mksong
	 * @description renaming a class name
	 */
	protected void doRename(IProgressMonitor pm) throws CoreException
	{
		// ChangeSourceCode change = ChangeClassName.getInstance();
		// String oldStr = getOldName();
		// String newStr = getNewName();
		// System.out.println("[DBG] change: " + oldStr + " -> " + newStr);
		// change.setBeforeStr(oldStr.replace(".java", ""));
		// change.setAfterStr(newStr.replace(".java", ""));
		//
		// System.out.println("[DBG]" + change);
		// System.out.println("[DBG]" + System.getProperty("user.dir"));
		// System.out.println("[DBG]" + System.getProperty("working.dir"));
		// System.out.println("[DBG]" + getResourcePath());
		// change.checkMetadataInvariants();

		ICompilationUnit cu = (ICompilationUnit) getModifiedElement();
		if (cu != null)
			cu.rename(getNewName(), false, pm);
	}

	public String getName()
	{
		String[] keys =
				new String[] { BasicElementLabels.getJavaElementName(getOldName()),
						BasicElementLabels.getJavaElementName(getNewName()) };
		return Messages.format(RefactoringCoreMessages.RenameCompilationUnitChange_name, keys);
	}
}
