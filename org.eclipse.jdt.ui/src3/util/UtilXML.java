/*
 * @(#) UtilXML.java
 * Copyright 2010 The Software Innovations Lab, Dept. of Computer Science,
 * Virginia Tech. All rights reserved.
 * 2202 Kraft Drive, Blacksburg, VA 24060
 */
package util;

import java.util.List;

import relation.xml.XMLReader;

/**
 * @author Myoungkyu Song
 * @date Jun 16, 2011
 * @since JDK1.6
 */
public class UtilXML {
	/** @METHOD */
	public static List<String> readXMLTags(String xmlFilepath, List<String> relationParmList) {
		XMLReader readXMLInst = new XMLReader(xmlFilepath, false);
		readXMLInst.parse(relationParmList);
		List<String> values = readXMLInst.getValues();
		// System.out.print("[DBG] VALUE FROM XML: ");
		if (values.isEmpty()) {
			// System.out.println("[DBG]\tNO DATA");
			return null;
		}
		// else {
		// System.out.println();
		// }
		// UtilPrint.printListPair(values);
		return values;
	}
}
