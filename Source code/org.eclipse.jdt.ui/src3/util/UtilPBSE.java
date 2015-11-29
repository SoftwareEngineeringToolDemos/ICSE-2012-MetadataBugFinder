/*
 * @(#) UtilPBSE.java
 * Copyright 2010 The Software Innovations Lab, Dept. of Computer Science,
 * Virginia Tech. All rights reserved.
 * 2202 Kraft Drive, Blacksburg, VA 24060
 */
package util;

import java.util.ArrayList;

import metadata.invariant.pbse.programconstructs.FieldDecl;
import metadata.invariant.pbse.programconstructs.FullFieldDecl;
import metadata.invariant.pbse.visitor.Visitor;

/**
 * @author Myoungkyu Song
 * @date Oct 27, 2010
 * @since JDK1.6
 */
public class UtilPBSE {
	/**
	 * @METHOD
	 */
	public static String getWhereStat(Visitor visitor, String mostFreStr) {
		ArrayList<String> modifiers = new ArrayList<String>();

		// Only within the most frequent transformation, 'where' statement is allowed.
		for (FullFieldDecl fullFieldDecl : visitor.getFullFieldDeclList()) {
			String trax = fullFieldDecl.getTransforms();

			if (trax != null && trax.equals(mostFreStr)) {
				FieldDecl fieldDecl = fullFieldDecl.getFieldDecl();

				// String fullfieldname = fieldDecl.getModifier() + " ";
				// fullfieldname += fieldDecl.getType() + " ";
				// fullfieldname += fieldDecl.getName();

				String mod = fieldDecl.getModifier();
				if (!modifiers.contains(mod)) {
					modifiers.add(mod);
				}
			}
		}

		String modifier = "";

		// best case: there is only one same modifier in all field.
		if (modifiers.size() == 1) {
			modifier = modifiers.get(0) + " *";
		}
		// there is muliple modifiers.
		else {

			for (int i = 0; i < modifiers.size(); i++) {
				String mod = modifiers.get(i) + " *";
				modifier += mod;

				if (i != modifiers.size() - 1) {
					modifier += " || ";
				}
			}
		}
		return modifier;
	}
}
