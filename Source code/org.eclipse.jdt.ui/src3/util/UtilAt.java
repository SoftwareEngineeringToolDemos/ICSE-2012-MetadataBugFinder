/*
 * @(#) UtilAt.java
 * Copyright 2010 The Software Innovations Lab, Dept. of Computer Science,
 * Virginia Tech. All rights reserved.
 * 2202 Kraft Drive, Blacksburg, VA 24060
 */
package util;

import java.util.ArrayList;

import metadata.invariant.pbse.STR;
import metadata.invariant.pbse.programconstructs.FieldDecl;
import metadata.invariant.pbse.programconstructs.FullFieldDecl;
import metadata.invariant.pbse.visitor.Visitor;

/**
 * @author Myoungkyu Song
 * @date Oct 26, 2010
 * @since JDK1.6
 */
public class UtilAt {

	/** @METHOD */
	public static boolean hasNameAttr(String line) {
		int pos1 = line.indexOf("(") + 1;
		int pos2 = line.indexOf("=", pos1);

		if (pos1 == 0 || pos2 == -1)
			return false;

		String str1 = line.substring(pos1, pos2);
		return str1.equals("name") ? true : false;
	}

	/** @METHOD */
	public static boolean hasOnlyValue(String val) {
		int pos1 = val.indexOf("\"");
		int pos2 = val.indexOf("=");
		return (pos1 == -1 && pos2 == -1) ? true : false;
	}

	/** @METHOD */
	public static String getAttrValue(String val) {
		return val;
	}

	/** @METHOD */
	public static String getNameAttrValue(String line) {
		int pos1 = line.indexOf("(") + 1;
		int pos2 = line.indexOf("=", pos1);
		int pos3 = line.indexOf("\"", pos2) + 1;
		int pos4 = line.indexOf("\"", pos3);

		if (pos1 == 0 || pos2 == -1 || pos3 == 0 || pos4 == -1)
			return null;

		String nameAttributeValue = line.substring(pos3, pos4);
		return nameAttributeValue;
	}

	/**
	 * @METHOD
	 */
	public static String getAttrValue(String src, String val) {
		String[] vals = src.split(",");

		for (int i = 0; i < vals.length; i++) {
			String elem = vals[i];

			if (elem.trim().contains(val)) {
				String[] pair = elem.split("=");
				String righthand = pair[1].trim().replace("\"", "");
				return righthand;
			}
		}
		return null;
	}

	/**
	 * @METHOD
	 */
	public static String getOtherClassName(String collectionType) {
		int pos1 = collectionType.indexOf("<");
		int pos2 = collectionType.indexOf(">");
		return collectionType.substring(pos1 + 1, pos2);
	}

	/**
	 * @METHOD
	 */
	public static FieldDecl getFieldWithId(ArrayList<FullFieldDecl> fullFieldDeclList) {
		FieldDecl theFieldDecl = null;

		for (int i = 0; i < fullFieldDeclList.size(); i++) {
			FullFieldDecl fullFieldDecl = fullFieldDeclList.get(i);

			if (fullFieldDecl.getAtId() != null) {
				theFieldDecl = fullFieldDecl.getFieldDecl();
				break;
			}
		}
		return theFieldDecl;
	}

	/**
	 * @METHOD
	 */
	public static FieldDecl getFieldWithId(String fieldtype) {
		FieldDecl theFieldDecl = null;

		String elemType = UtilStr.getStrBetween(fieldtype, "<", ">");
		String otherJavaSrc = STR.file_test + "\\" + elemType + STR.file_java;

		Visitor visitor = UtilAST.startVisit(otherJavaSrc);
		ArrayList<FullFieldDecl> fullFieldDeclList = visitor.getFullFieldDeclList();

		theFieldDecl = getFieldWithId(fullFieldDeclList);
		return theFieldDecl;
	}
}
