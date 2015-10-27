/*
 * @(#) Open.java
 * Copyright 2010 The Software Innovations Lab, Dept. of Computer Science,
 * Virginia Tech. All rights reserved.
 * 2202 Kraft Drive, Blacksburg, VA 24060
 */
package util;

import java.util.List;

/**
 * @author Myoungkyu Song
 * @date Jan 7, 2012
 * @since JDK1.6
 */
public class Open {
	public static String			refactoredFileName;
	public static int				lineNumber;
	public static List<String>	beforeFileContents;
	public static List<String>	afterFileContents;

	public static List<String>	xmlTagValues;
	public static String			xmlFilePath;

	public static void setRefactFileName(String pRefactFileName)
	{
		refactoredFileName = pRefactFileName;
	}
}
