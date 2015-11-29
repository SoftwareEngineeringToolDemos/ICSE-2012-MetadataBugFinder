/*
 * @(#)UtilFile.java 05/17/2010
 * Copyright 2010 The Software Innovations Lab, Dept. of Computer Science,
 * Virginia Tech. All rights reserved.
 * 2202 Kraft Drive, Blacksburg, VA 24060
 */
package util;

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Myoungkyu Song
 * @version May 2009
 * @since JDK1.6
 */
public class UtilFile {

	private static PrintStream	fout	= null;

	/** @METHOD */
	public static List<String> convertStrPath(List<File> filelist)
	{
		List<String> filepathlist = new ArrayList<String>();
		for (File file : filelist) {
			filepathlist.add(file.getAbsolutePath());
		}
		return filepathlist;
	}

	/**
	 * @METHOD
	 */
	public static void printStreamBegin(String filename)
	{
		try {
			fout = new PrintStream(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void printStreamClose()
	{
		fout.close();
	}

	public static void println(String t)
	{
		fout.println(t);
		System.out.println("[DBG] " + t);
	}

	/**
	 * @METHOD
	 */
	public static boolean contains(String parm1, File file)
	{
		boolean flag = false;
		String line = null;
		try {
			BufferedReader input = new BufferedReader(new FileReader(file));
			while ((line = input.readLine()) != null) {

				if (file.getPath().contains(".svn")) {
					flag = false;
					break;
				}
				if (line.trim().contains(parm1)) {
					flag = true;
					break;
				}
			}
			input.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return flag;
	}

	/**
	 * @param file
	 * object
	 * @return file contents
	 */
	public static String fileRead(File f)
	{
		StringBuilder sb = new StringBuilder();
		String line = null;

		try {
			BufferedReader input = new BufferedReader(new FileReader(f));

			while ((line = input.readLine()) != null) {
				sb.append(line);
				sb.append(System.getProperty("line.separator"));
			}

			input.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (sb.toString());
	}

	public static StringBuilder fileReadBuf(File f)
	{
		StringBuilder sb = new StringBuilder();
		String line = null;

		try {
			BufferedReader input = new BufferedReader(new FileReader(f));

			while ((line = input.readLine()) != null) {
				sb.append(line);
				sb.append(System.getProperty("line.separator"));
			}

			input.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return (sb);
	}

	/**
	 * @param file
	 * path string
	 * @return file contents' list object
	 */
	public static ArrayList<String> fileRead2List(String filepath)
	{
		ArrayList<String> list = new ArrayList<String>();
		String line = null;

		try {
			File f = new File(filepath);
			BufferedReader input = new BufferedReader(new FileReader(f));

			while ((line = input.readLine()) != null) {
				list.add(line);
			}

			input.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return list;
	}

	/**
	 * @param file
	 * path string
	 * @param text
	 * to save
	 * @return file object
	 */
	public static File fileWriteTempo(String path, String text)
	{
		File file = null;

		try {
			file = new File(path);

			// Delete temp file when program exits.
			file.deleteOnExit();

			Writer output = new BufferedWriter(new FileWriter(file));

			output.write(text);
			output.flush();
			output.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return file;
	}

	/** @METHOD */
	public static void saveTemporally(String dir, List<File> javaAnnotatedSrcList)
	{
		for (int j = 0; j < javaAnnotatedSrcList.size(); j++) {
			File javafile = javaAnnotatedSrcList.get(j);
			saveTemporally(dir, javafile);
		}
	}

	/** @METHOD */
	public static void saveTemporally(String dir, File javafile)
	{
		String filebody = UtilFile.fileRead(javafile);
		String destfile = dir + javafile.getName();
		fileWrite(destfile, filebody);
	}

	/**
	 * @param file
	 * name string
	 * @param saved
	 * text
	 */
	public static void fileWrite(String filename, String text)
	{
		try {
			File file = new File(filename);

			Writer output = new BufferedWriter(new FileWriter(file));

			output.write(text);
			output.flush();
			output.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @param file
	 * name string
	 * @param text
	 * to save
	 */
	public static void fileWrite2(String filename, String text)
	{
		File file = null;

		try {
			file = new File(filename);

			Writer output = new BufferedWriter(new FileWriter(file));

			output.write(text);
			output.flush();
			output.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @param file
	 * name to save
	 * @param contents
	 * to save
	 */
	public static void fileWrite(String filename, List<String> fileContents)
	{
		StringBuilder sb = new StringBuilder();
		String newline = System.getProperty("line.separator");
		for (int i = 0; i < fileContents.size(); i++) {
			String line = fileContents.get(i);
			sb.append(line + newline);
		}
		String text = sb.toString();
		File file = null;
		try {
			file = new File(filename);
			Writer output = new BufferedWriter(new FileWriter(file));
			output.write(text);
			output.flush();
			output.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @param file
	 * name string
	 * @return true if the file exist, or false
	 */
	public static boolean fileExists(String fname)
	{
		return (new File(fname).exists());
	}

	/**
	 * @param file
	 * path string
	 */
	public static void fileDelete(String filepath)
	{
		File f = new File(filepath);

		if (!f.exists())
			throw new IllegalArgumentException("UtilFile.fileDelete: no such file or directory: " + filepath);

		if (f.isDirectory()) {
			String[] files = f.list();
			if (files.length > 0)
				throw new IllegalArgumentException("UtilFile.fileDelete: directory not empty: " + filepath);
		}

		if (!f.canWrite())
			System.out.println("UtilFile.fileDelete- canWrite failed: " + filepath);

		boolean success = false;

		try {
			success = f.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!success)
			System.out.println("UtilFile.fileDelete- failed: " + filepath);
	}

	/**
	 * @param source
	 * path to copy
	 * @param destination
	 * path to copy
	 */
	public static void fileCopy(String src, String dst)
	{
		try {
			File inputFile = new File(src);
			File outputFile = new File(dst);

			FileReader in = new FileReader(inputFile);
			FileWriter out = new FileWriter(outputFile);
			int c;

			while ((c = in.read()) != -1)
				out.write(c);

			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param source
	 * path to copy
	 * @param destination
	 * path to copy
	 */
	public static void fileBinCopy(String src, String dst)
	{
		try {
			File sourceFile = new File(src); // source
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile), 4096);
			File targetFile = new File(dst); // destination
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetFile), 4096);
			int theChar;
			while ((theChar = bis.read()) != -1) {
				bos.write(theChar);
			}
			bos.close();
			bis.close();
			System.out.println("copy done!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @param directory
	 * path
	 */
	public static void createSingleDir(String path)
	{
		try {
			(new File(path)).mkdir();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param directory
	 * path
	 */
	public static void createMultiDir(String path)
	{
		try {
			(new File(path)).mkdirs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param full
	 * path string
	 * @return short file name
	 */
	public static String getFileNameFromPath(String path)
	{
		int pos1 = path.lastIndexOf("\\");

		String fileName = path.substring(pos1 + 1);

		return fileName;
	}

	/**
	 * @param filename
	 * @param string
	 * to be removed
	 */
	public static void removeStringFromFile(String filename, String toRemove)
	{
		ArrayList<String> fileContents = fileRead2List(filename);
		for (int i = 0; i < fileContents.size(); i++) {
			String elem = fileContents.get(i);
			elem = elem.replace(toRemove, "");
			fileContents.set(i, elem);
		}
		fileWrite(filename, fileContents);
	}

	/**
	 * @METHOD
	 */
	public static void deleteAllFiles(String dir, String pattern)
	{
		List<File> filelist = UtilDirScan.getResult(dir, pattern);
		for (int i = 0; i < filelist.size(); i++) {
			UtilFile.fileDelete(filelist.get(i).getAbsolutePath());
		}
	}

	/**
	 * @METHOD
	 */
	public static int fileCompare(List<String> beforeFileContents, List<String> afterFileContents)
	{
		int linenumber = -1;
		for (int i = 0; i < afterFileContents.size(); i++) {
			String elem1 = afterFileContents.get(i);
			String elem2 = beforeFileContents.get(i);

			if (elem1.equals(elem2))
				continue;
			else {
				linenumber = i;
				break;
			}
		}
		return linenumber;
	}
}
