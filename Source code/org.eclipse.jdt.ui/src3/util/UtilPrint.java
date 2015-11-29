/*
 * @(#)XMain.java 05/17/2010
 * Copyright 2010 The Software Innovations Lab, Dept. of Computer Science,
 * Virginia Tech. All rights reserved.
 * 2202 Kraft Drive, Blacksburg, VA 24060
 */
package util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Myoungkyu Song
 * @version 05/17/2010
 * @since JDK1.6 {@link} https://clarify.cs.vt.edu/svn/X10/trunk/code/pbseConverter
 */
public class UtilPrint {

	/**
	 * @param tableView
	 */
	public static void print2Array(String[][] tableView)
	{
		int max_len = 0;

		for (int i = 0; i < tableView[0].length; i++) {
			int len = tableView[0][i].length();

			if (max_len < len)
				max_len = len;
		}

		for (int i = 0; i < tableView[0].length; i++) {
			String x = tableView[0][i];
			String y = tableView[1][i];

			String SP = "  ";
			if (i > 8)
				SP = " ";

			System.out.print("" + (i + 1) + SP + x + " ");
			System.out.print(toSpace(max_len - x.length() + 1));
			System.out.println(y);
		}
	}

	/**
	 * @param x
	 * @return
	 */
	public static String toSpace(int x)
	{
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < x; i++) {
			sb.append("_");
		}
		return sb.toString();
	}

	/**
	 * @param lt
	 * @param arr
	 * @param rt
	 */
	public static void printArray(String lt, String[] arr, String rt)
	{
		if (arr.length < 1)
			System.out.println("[EMPTY]");
		else if (arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				if (i < 9)
					System.out.println(" " + (i + 1) + lt + arr[i] + rt);
				else
					System.out.println("" + (i + 1) + lt + arr[i] + rt);
			}
		}
		else {
			for (int i = 0; i < arr.length; i++) {
				System.out.println(lt + arr[i] + rt);
			}
		}
	}

	/**
	 * @param lt
	 * @param arr
	 * @param rt
	 */
	public static void printArray(String lt, Object[] arr, String rt)
	{
		if (arr.length > 1) {
			for (int i = 0; i < arr.length; i++) {
				System.out.println(lt + arr[i] + rt);
			}
		}
		else {
			for (int i = 0; i < arr.length; i++) {
				System.out.println(lt + arr[i] + rt);
			}
		}
	}

	/**
	 * @param map
	 */
	public static void printMap2(Map<String, String> map)
	{
		printMap((HashMap<String, String>) map);
	}

	/**
	 * @param map
	 */
	public static void printMap(HashMap<String, String> map)
	{
		if (map == null || map.size() == 0) {
			System.out.println("**NO ITEM !!");
			return;
		}

		int max = 0;
		Iterator<String> itr;
		itr = map.keySet().iterator();
		while (itr.hasNext()) {
			int x = itr.next().length();
			if (x > max)
				max = x;
		}
		max += 0;
		itr = map.keySet().iterator();
		int i = 0;
		while (itr.hasNext()) {
			String aKey = itr.next();
			String aVal = map.get(aKey);
			int len = aKey.length();
			int gap = max - len;
			System.out.print("   " + ((i++) + 1) + ": " + aKey);

			for (int j = 0; j < gap; j++)
				System.out.print(" ");
			System.out.println(", " + aVal + "");
		}
	}

	/** @METHOD */
	public static void printMap(String keyStr, String valStr, HashMap<String, String> map)
	{
		if (map == null || map.size() == 0) {
			System.out.println("**NO ITEM !!");
			return;
		}

		int max = 0;
		Iterator<String> itr;
		itr = map.keySet().iterator();
		while (itr.hasNext()) {
			int x = itr.next().length();
			if (x > max)
				max = x;
		}
		max += 0;
		itr = map.keySet().iterator();
		int i = 0;

		System.out.println("------------------------------------------------------------------------------------");
		int keylen = keyStr.length();
		System.out.print(keyStr);
		for (int j = 0; j < max - keylen + 5; j++)
			System.out.print(" ");
		System.out.println(valStr);
		System.out.println("------------------------------------------------------------------------------------");

		while (itr.hasNext()) {
			String aKey = itr.next();
			String aVal = map.get(aKey);
			int len = aKey.length();
			int gap = max - len;
			System.out.print(" " + ((i++) + 1) + ": " + aKey);

			for (int j = 0; j < gap; j++)
				System.out.print(" ");
			System.out.println(", " + aVal + "");
		}
	}

	/** @METHOD */
	public static void printMapGeneral(Map<String, ?> map)
	{
		if (map == null || map.size() == 0) {
			System.out.println("**NO ITEM !!");
			return;
		}

		int max = 0;
		Iterator<String> itr;
		itr = map.keySet().iterator();
		while (itr.hasNext()) {
			int x = itr.next().length();
			if (x > max)
				max = x;
		}
		max += 0;
		itr = map.keySet().iterator();
		int i = 0;
		while (itr.hasNext()) {
			String aKey = itr.next();
			Object aVal = map.get(aKey);
			int len = aKey.length();
			int gap = max - len;
			System.out.print("   " + ((i++) + 1) + ": " + aKey);

			for (int j = 0; j < gap; j++)
				System.out.print(" ");
			System.out.println(", " + aVal + "");
		}
	}

	/** @METHOD */
	public static void printMapGeneral(Map<String, ?> map, String suffix)
	{
		if (map == null || map.size() == 0) {
			System.out.println("**NO ITEM !!");
			return;
		}

		int max = 0;
		Iterator<String> itr;
		itr = map.keySet().iterator();
		while (itr.hasNext()) {
			int x = itr.next().length();
			if (x > max)
				max = x;
		}
		max += 0;
		itr = map.keySet().iterator();
		int i = 0;
		while (itr.hasNext()) {
			String aKey = itr.next();
			Object aVal = map.get(aKey);
			int len = aKey.length();
			int gap = max - len;
			System.out.print("   " + ((i++) + 1) + ": " + aKey);

			for (int j = 0; j < gap; j++)
				System.out.print(" ");
			System.out.println(", " + aVal + suffix);
		}
	}

	/**
	 * @param map
	 */
	public static void printVarTable(HashMap<String, ?> map)
	{
		int max = 0;
		Iterator<String> itr;

		/**
		 * @ Calculate gap for printing.
		 */
		itr = map.keySet().iterator();
		while (itr.hasNext()) {
			int x = itr.next().length();
			if (x > max)
				max = x;
		}
		max += 3;

		itr = map.keySet().iterator();

		/**
		 * @ Print the map.
		 */
		while (itr.hasNext()) {
			String aKey = itr.next();
			String aVal = map.get(aKey).toString();

			int len = aKey.length();
			int gap = max - len;

			System.out.print("  - " + aKey);

			for (int j = 0; j < gap; j++)
				System.out.print(" ");

			System.out.println(": " + aVal);
		}
	}

	/**
	 * @param list
	 */
	public static void printArrayList(ArrayList<String> list)
	{
		if (list == null || list.isEmpty()) {
			System.out.println("\tNO ITEM !!");
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			System.out.println("  " + (i + 1) + ": " + list.get(i) + "");
		}
	}

	/** @METHOD */
	public static void printArrayList(List<?> list)
	{
		if (list.isEmpty())
			System.out.println("\tNO ITEM !!");

		for (int i = 0; i < list.size(); i++) {
			System.out.println("  " + (i + 1) + ": " + list.get(i) + "");
		}
	}

	/** @METHOD */
	public static void printListPair(List<String> list)
	{
		if (list.isEmpty())
			System.out.println("\tNO ITEM !!");

		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i) + " ");
			if ((i + 1) % 2 == 0)
				System.out.println();
		}
	}

	/**
	 * @param list
	 */
	public static void printArrayList2(ArrayList<String> list)
	{
		if (list.isEmpty())
			System.out.println("\tNO ITEM !!");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}

	/**
	 * @param list
	 */
	public static void printListFlat(ArrayList<String> list)
	{
		if (list == null || list.isEmpty()) {
			System.out.println("\tNO ITEM !!");
			return;
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < list.size(); i++) {
			if (i == 0)
				sb.append("{");

			sb.append(list.get(i));

			if (i != list.size() - 1)
				sb.append(",");
			if (i == list.size() - 1)
				sb.append("}");
		}
		System.out.println(sb.toString());
	}

	/**
	 * @param list
	 */
	public static void print_al(ArrayList<String> list)
	{
		for (int i = 0; i < list.size(); i++) {
			System.out.println("\t" + i + ": [" + list.get(i) + "]");
		}
	}

	/**
	 * @param rtag
	 * @param list
	 * @param ltag
	 */
	public static void printArrayList(String rtag, ArrayList<String> list, String ltag)
	{
		for (int i = 0; i < list.size(); i++) {
			System.out.println(rtag + list.get(i) + ltag);
		}
	}

	/**
	 * @param rtag
	 * @param list
	 * @param ltag
	 * @param isTrim
	 */
	public static void printArrayList(String rtag, ArrayList<String> list, String ltag, boolean isTrim)
	{
		for (int i = 0; i < list.size(); i++) {
			if (isTrim)
				System.out.println(rtag + list.get(i).trim() + ltag);
			else
				System.out.println(rtag + list.get(i) + ltag);
		}
	}

	/**
	 * @param list
	 */
	public static void printArrayListInt(ArrayList<Integer> list)
	{
		for (int i = 0; i < list.size(); i++) {
			System.out.println("\t" + i + ": [" + list.get(i) + "]");
		}
	}

	/**
	 * @param list
	 */
	public static void printFileArrayList(ArrayList<File> list)
	{
		for (int i = 0; i < list.size(); i++) {
			System.out.println("\t[" + list.get(i).getAbsolutePath() + "]");
		}
	}

	/** @METHOD */
	public static void printFileArrayList(List<File> list)
	{
		for (int i = 0; i < list.size(); i++) {
			System.out.println("\t[" + list.get(i).getAbsolutePath() + "]");
		}
	}

	/**
	 * @param lt
	 * @param list
	 * @param rt
	 */
	public static void printFileArrayList(String lt, ArrayList<File> list, String rt)
	{
		for (int i = 0; i < list.size(); i++) {
			System.out.println(lt + list.get(i).getAbsolutePath() + rt);
		}
	}

	/**
	 * @param lt
	 * @param map
	 * @param rt
	 * @param maxparm
	 * @return
	 */
	public static int printHashMap(String lt, HashMap<String, String> map, String rt, int maxparm)
	{
		Set<String> keySetOfPaths = map.keySet();
		Iterator<String> itrKeySetOfPaths = keySetOfPaths.iterator();

		int maxlen = maxparm;

		while (itrKeySetOfPaths.hasNext()) {
			String aKey = itrKeySetOfPaths.next();
			if (maxlen < aKey.length())
				maxlen = aKey.length();
		}

		keySetOfPaths = map.keySet();
		itrKeySetOfPaths = keySetOfPaths.iterator();

		while (itrKeySetOfPaths.hasNext()) {
			String aKey = itrKeySetOfPaths.next();
			String aVal = map.get(aKey);

			int gap = maxlen - aKey.length();

			System.out.print(lt + aKey);

			for (int i = 0; i < gap; i++)
				System.out.print(" ");

			System.out.println(" , " + aVal + rt);
		}

		return maxlen;
	}

	/**
	 * @param map
	 */
	public static void printHashMap(HashMap<String, String> map)
	{
		Set<String> keySetOfPaths = map.keySet();
		Iterator<String> itrKeySetOfPaths = keySetOfPaths.iterator();

		int maxlen = 0;

		while (itrKeySetOfPaths.hasNext()) {
			String aKey = itrKeySetOfPaths.next();
			if (maxlen < aKey.length())
				maxlen = aKey.length();
		}

		keySetOfPaths = map.keySet();
		itrKeySetOfPaths = keySetOfPaths.iterator();

		while (itrKeySetOfPaths.hasNext()) {
			String aKey = itrKeySetOfPaths.next();
			String aVal = map.get(aKey);

			int gap = maxlen - aKey.length();

			System.out.print("-> " + aKey);

			for (int i = 0; i < gap; i++)
				System.out.print(" ");

			System.out.println(" , " + aVal);
		}
	}

	/**
	 * @param lt
	 * @param map
	 * @param rt
	 * @return
	 */
	public static int printHashMap(String lt, HashMap<String, String> map, String rt)
	{
		Set<String> keySetOfPaths = map.keySet();
		Iterator<String> itrKeySetOfPaths = keySetOfPaths.iterator();

		int maxlen = 0;

		while (itrKeySetOfPaths.hasNext()) {
			String aKey = itrKeySetOfPaths.next();
			if (maxlen < aKey.length())
				maxlen = aKey.length();
		}

		keySetOfPaths = map.keySet();
		itrKeySetOfPaths = keySetOfPaths.iterator();

		while (itrKeySetOfPaths.hasNext()) {
			String aKey = itrKeySetOfPaths.next();
			String aVal = map.get(aKey);

			int gap = maxlen - aKey.length();

			System.out.print(lt + aKey);

			for (int i = 0; i < gap; i++)
				System.out.print(" ");

			System.out.println(" , " + aVal + rt);
		}

		return maxlen;
	}

	/**
	 * @param sortedMap
	 */
	public static void printMap(TreeMap<String, String> sortedMap)
	{
		int max = 0;
		Iterator<String> itr;

		/**
		 * @ Calculate gap for printing.
		 */
		itr = sortedMap.keySet().iterator();
		while (itr.hasNext()) {
			int x = itr.next().length();
			if (x > max)
				max = x;
		}
		max += 3;

		/**
		 * @ Sort the map.
		 */
		itr = sortedMap.keySet().iterator();

		/**
		 * @ Print the map.
		 */
		while (itr.hasNext()) {
			String aKey = itr.next();
			String aVal = sortedMap.get(aKey);

			int len = aKey.length();
			int gap = max - len;

			System.out.print("	" + aKey);

			for (int j = 0; j < gap; j++)
				System.out.print(" ");

			System.out.println(", " + aVal);
		}
	}

	/** @METHOD */
	public static void displayResult(boolean result, String filename)
	{
		Open.lineNumber = UtilFile.fileCompare(Open.beforeFileContents, Open.afterFileContents);

		System.out.println("------------------------------------------");
		if (result) {
			System.out.println("[DBG] OK !!!");
		}
		else {
			System.out.println("[DBG] Violate !!!");
			System.out.println("[DBG] File:" + UtilStr.getShorfileName(filename));
			System.out.println("[DBG] Changed (line number: " + (Open.lineNumber + 1) + "): ");
			String oldLine = Open.beforeFileContents.get(Open.lineNumber);
			String newLine = Open.afterFileContents.get(Open.lineNumber);
			System.out.println("[DBG] < " + oldLine);
			System.out.println("[DBG] ---");
			System.out.println("[DBG] >" + newLine);
		}
		System.out.println("------------------------------------------");
	}

	public static void displayResult(boolean result, String filename, String src, String dst)
	{
		Open.lineNumber = UtilFile.fileCompare(Open.beforeFileContents, Open.afterFileContents);

		System.out.println("------------------------------------------");
		if (result) {
			System.out.println("[DBG] OK !!!");
		}
		else {
			System.out.println("[DBG] Violate !!!");
			System.out.println("[DBG] File:" + UtilStr.getShorfileName(filename));
			if (Open.xmlTagValues != null) {
				System.out.println("[DBG] XML file: " + UtilStr.getShorfileName(Open.xmlFilePath));
				for (int i = 0; i < Open.xmlTagValues.size(); i++) {
					String elem = Open.xmlTagValues.get(i);
					System.out.println("[DBG] XML Tag Value: " + elem);
				}
				System.out.println("------------------------------------------");
			}
			System.out.println("[DBG] Changed (line number: " + (Open.lineNumber + 1) + "): " + //
					src + " -> " + dst);
			String oldLine = Open.beforeFileContents.get(Open.lineNumber);
			String newLine = Open.afterFileContents.get(Open.lineNumber);
			System.out.println("[DBG] < " + oldLine);
			System.out.println("[DBG] ---");
			System.out.println("[DBG] >" + newLine);
		}
		System.out.println("------------------------------------------");
	}

	/**
	 * @param category
	 */
	public static void pl(int category)
	{
		if (category == 0)
			System.out.println("------------------------------------------");
		if (category == 1)
			System.out.println("==========================================");
	}
}
