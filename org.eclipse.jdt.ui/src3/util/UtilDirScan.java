/*
 * @(#) DirScanProc.java
 * Copyright 2010 The Software Innovations Lab, Dept. of Computer Science,
 * Virginia Tech. All rights reserved.
 * 2202 Kraft Drive, Blacksburg, VA 24060
 */
package util;

import static java.lang.System.out;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Myoungkyu Song
 * @date Dec 27, 2010
 * @since JDK1.6
 */
public class UtilDirScan {
	private Collection<File>	fileCollection	= new ArrayList<File>();
	private String					filter;

	/**
	 * @METHOD
	 */
	public static ArrayList<File> getResult(String path, String filter) {
		ArrayList<File> result = new ArrayList<File>();
		UtilDirScan dirScanner = new UtilDirScan(path, filter);
		Iterator<File> itr = dirScanner.fileCollection.iterator();
		while (itr.hasNext()) {
			result.add(itr.next());
		}
		return result;
	}

	/**
	 * @METHOD
	 */
	public static List<String> getResultViaStr(String path, String filter) {
		ArrayList<String> result = new ArrayList<String>();
		UtilDirScan dirScanner = new UtilDirScan(path, filter);
		Iterator<File> itr = dirScanner.fileCollection.iterator();
		while (itr.hasNext()) {
			result.add(itr.next().getAbsolutePath());
		}
		return result;
	}

	/**
	 * @CONSTRUCTOR
	 */
	public UtilDirScan(String path, String filter) {
		this.filter = filter;
		addFilesRecursively(new File(path));
	}

	/**
	 * @METHOD
	 */
	void addFilesRecursively(File file) {
		final File[] children = file.listFiles();

		if (children != null) {
			for (File child : children) {
				String filename = child.getName();
				if (!filename.contains(".svn") && filename.contains(this.filter)) {
					fileCollection.add(child);
				}
				addFilesRecursively(child);
			}
		}
	}

	/**
	 * @param fileCollection
	 */
	void print() {
		int i = 0;
		Iterator<File> itr = this.fileCollection.iterator();
		while (itr.hasNext()) {
			File file = itr.next();
			String fileName = file.getName();
			if ((fileName.indexOf(filter)) != -1) {
				out.println(file.getAbsolutePath());
				i++;
			}
		}
		out.println("SIZE: " + i);
	}

	public Collection<File> getFileCollection() {
		return fileCollection;
	}

}
