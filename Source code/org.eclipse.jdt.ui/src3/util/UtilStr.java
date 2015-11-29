/*
 * @(#) UtilStr.java
 * Copyright 2010 The Software Innovations Lab, Dept. of Computer Science,
 * Virginia Tech. All rights reserved.
 * 2202 Kraft Drive, Blacksburg, VA 24060
 */
package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import metadata.invariant.pbse.STR;

/**
 * @author John Edstrom & Myoungkyu Song
 * @date Oct 28, 2010
 * @since JDK1.6
 */
public class UtilStr {

	/** @METHOD */
	public static String toStringList(List<?> list) {
		if (list.isEmpty())
			return ("\tNO ITEM !!");

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i) + "\n");
		}
		return sb.toString();
	}

	/** @METHOD */
	public static boolean isNull(String obj) {
		if (obj == null)
			return true;
		if (obj.equals("null"))
			return true;
		return false;
	}

	/** @METHOD */
	public static String split(String str, String delimeter) {
		List<String> subStrList = new ArrayList<String>();
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			if (Character.isUpperCase(str.charAt(i))) {
				subStrList.add(buf.toString());
				buf.setLength(0);
			}
			buf.append(str.charAt(i));
		}
		subStrList.add(buf.toString());

		buf = new StringBuilder();
		if (subStrList.isEmpty() == false) {
			for (int i = 0; i < subStrList.size(); i++) {
				String elem = subStrList.get(i);
				buf.append(elem);
				if (i != subStrList.size() - 1)
					buf.append(delimeter);
			}
		}
		return buf.toString();
	}

	/** @METHOD */
	public static int countSubString(String str, String substr) {
		int cnt = 0;
		int lastindex = str.length();
		while (lastindex > -2) {
			lastindex = str.lastIndexOf(substr, lastindex) - 1;
			if (lastindex > -2)
				cnt++;
		}
		return cnt;
	}

	/** @METHOD */
	public static String combine(List<String> strlist, String delimiter) {
		StringBuilder buf = new StringBuilder();
		if (strlist.isEmpty() == false) {
			for (int i = 0; i < strlist.size(); i++) {
				String elem = strlist.get(i);
				buf.append(elem);
				if (i != strlist.size() - 1)
					buf.append(delimiter);
			}
		}
		return buf.toString();
	}

	/** @METHOD */
	public static String[] getValues(String longline, String key) {
		int bgn = -1, end = -1;
		int start = longline.indexOf(key);
		boolean equalmark = false, braceopen = false;
		if (start < 0)
			return new String[] {};

		for (int i = start; i < longline.length(); i++) {
			char c = longline.charAt(i);

			if (c == '=') {
				equalmark = true;
			}
			else if (equalmark && c == '{') {
				bgn = i + 1;
				braceopen = true;
			}
			else if (braceopen && c == '}') {
				end = i;
				break;
			}
		}
		String value = longline.substring(bgn, end);
		String values[] = value.split(",");
		for (int i = 0; i < values.length; i++)
			values[i] = values[i].trim().replace("\"", "");
		return values;
	}

	/** @METHOD */
	public static int getSize(String line, char ch) {
		int cnt = 0;
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (c == ch) {
				cnt++;
			}
		}
		return cnt;
	}

	/** @METHOD */
	public static String removeComment(String methodDef) {
		int pos1 = startComment(methodDef);
		if (pos1 == -1)
			return methodDef;
		else {
			int pos2 = -1;
			for (int i = pos1; i < methodDef.length() - 1; i++) {
				char c1 = methodDef.charAt(i);
				char c2 = methodDef.charAt(i + 1);
				if (c1 == '*' && c2 == '/') {
					pos2 = i;
					break;
				}
			}
			String result = methodDef.substring(pos2 + 2);
			if (result.startsWith("\n"))
				result = result.substring(1);
			return result;
		}
	}

	/** @METHOD */
	static int startComment(String line) {
		for (int i = 0; i < line.length() - 1; i++) {
			if (line.charAt(i) == '/' && line.charAt(i + 1) == '*')
				return (i + 1);
		}
		return -1;
	}

	/** @METHOD */
	public static String[] trim(String[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = array[i].trim();
		}
		return array;
	}

	// public static String arrayToStr(String[] strArray) {
	// String str = "";
	// for (int i = 0; i < strArray.length; i++) {
	// str += strArray[i];
	// if (i != strArray.length - 1)
	// str += ", ";
	// }
	// return str;
	// }

	/** @METHOD */
	public static String arrayToStr(String[] array) {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			buf.append(array[i]);
			if (i != array.length - 1)
				buf.append(",");

		}
		return buf.toString();
	}

	/** @METHOD */
	public static String findLongestSubStr(String target, String src) {
		ArrayList<String> substrList = new ArrayList<String>();
		for (int i = 0; i < src.length(); i++) {
			for (int j = 0; j < target.length(); j++) {
				if (src.charAt(i) == target.charAt(j)) {
					substrList.add(substr(src.substring(i), target.substring(j)));
				}
			}
		}
		return findElemMaxLength(substrList);
	}

	/** @METHOD */
	public static String findElemMaxLength(List<String> strList) {
		int maxlen = strList.get(0).length();
		int maxlenIndex = 0;
		for (int i = 1; i < strList.size(); i++) {
			String elem = strList.get(i);
			if (maxlen < elem.length()) {
				maxlen = elem.length();
				maxlenIndex = i;
			}
		}
		return strList.get(maxlenIndex);
	}

	/** @METHOD */
	public static String substr(String substr1, String substr2) {
		String substr = "";
		int size = Math.min(substr1.length(), substr2.length());

		for (int i = 0; i < size; i++) {
			if (substr1.charAt(i) == substr2.charAt(i))
				substr += substr1.charAt(i);
			else
				break;
		}
		return substr;
	}

	/**
	 * @METHOD
	 */
	public static String findMostSimilarStartWithSubStr(ArrayList<String> list) {

		ArrayList<String> tmplist = new ArrayList<String>();
		// *
		for (int i = 0; i < list.size(); i++) {
			String elem1 = list.get(i);

			for (int j = 0; j < list.size(); j++) {
				if (i >= j)
					continue;

				String elem2 = list.get(j);
				String result1 = UtilStr.getMostSimilarStartWithSubStr(elem1, elem2);
				tmplist.add(result1);
			}
		}

		int index1 = 0, tmplen = tmplist.get(0).length();
		// * Find the lest length string in the listbuf.
		for (int i = 0; i < tmplist.size(); i++) {
			int elem = tmplist.get(i).length();

			if (elem < tmplen) {
				index1 = i;
				tmplen = elem;
			}
		}
		return tmplist.get(index1);
	}

	/**
	 * @METHOD
	 */
	public static String getShortClassName(String longClassName) {
		String result = longClassName.trim();

		int pos1 = result.lastIndexOf(".");
		result = result.substring(pos1 + 1);

		pos1 = result.trim().lastIndexOf("$");
		result = result.substring(pos1 + 1);

		pos1 = result.indexOf("<");
		if (pos1 != -1)
			result = result.substring(0, pos1);

		result = result.replace(STR.file_java, "");
		return result;
	}

	/**
	 * @METHOD
	 */
	public static String getShorfileName(String longfileName) {
		String sep = System.getProperty("file.separator");
		String result = "";
		result = longfileName.substring(longfileName.lastIndexOf(sep) + 1);
		return result;
	}

	/**
	 * @METHOD
	 */
	public static void displayRelationShipBetweenTwoWord(ArrayList<String> display4List, String[] columns, String delim) {

		String[] tmpTokens = display4List.get(0).split(delim);
		int maxCol1 = tmpTokens[0].length(), maxCol2 = tmpTokens[1].length();
		for (int i = 1; i < display4List.size(); i++) {
			String[] tokens = display4List.get(i).split(delim);
			int col1 = tokens[0].length();
			int col2 = tokens[1].length();

			if (col1 > maxCol1)
				maxCol1 = col1;
			if (col2 > maxCol2)
				maxCol2 = col2;
		}

		StringBuilder sbuf1 = new StringBuilder();

		sbuf1.append(columns[0]);
		int gap = maxCol1 - columns[0].length() + 2;
		UtilStr.addGap(sbuf1, gap);

		sbuf1.append("| " + columns[1]);
		gap = maxCol2 - columns[1].length() + 1;
		UtilStr.addGap(sbuf1, gap);

		sbuf1.append("| " + columns[2]);

		UtilLog.w("------------------------------------------------------------------------------------");
		UtilLog.w(" " + sbuf1);
		UtilLog.w("------------------------------------------------------------------------------------");
		displayRelationShipBetweenTwoWord(display4List, delim);
		UtilLog.w("------------------------------------------------------------------------------------");
	}

	/**
	 * @METHOD
	 */
	public static void displayRelationShipBetweenTwoWord(ArrayList<String> display4List, String[] columns) {
		final String __COMMA = ",";
		String[] tmpTokens = display4List.get(0).split(",");
		int maxCol1 = tmpTokens[0].length(), maxCol2 = tmpTokens[1].length();
		for (int i = 1; i < display4List.size(); i++) {
			String[] tokens = display4List.get(i).split(",");
			int col1 = tokens[0].length();
			int col2 = tokens[1].length();

			if (col1 > maxCol1)
				maxCol1 = col1;
			if (col2 > maxCol2)
				maxCol2 = col2;
		}

		StringBuilder sbuf1 = new StringBuilder();

		sbuf1.append(columns[0]);
		int gap = maxCol1 - columns[0].length() + 2;
		UtilStr.addGap(sbuf1, gap);

		sbuf1.append("| " + columns[1]);
		gap = maxCol2 - columns[1].length() + 1;
		UtilStr.addGap(sbuf1, gap);

		sbuf1.append("| " + columns[2]);

		UtilLog.w("------------------------------------------------------------------------------------");
		UtilLog.w(" " + sbuf1);
		UtilLog.w("------------------------------------------------------------------------------------");
		displayRelationShipBetweenTwoWord(display4List, __COMMA);
		UtilLog.w("------------------------------------------------------------------------------------");
	}

	/**
	 * @METHOD
	 */
	public static void displayRelationShipBetweenTwoWord(ArrayList<String> display4List, String delim) {
		String[] tmpTokens = display4List.get(0).split(delim);
		int maxCol1 = tmpTokens[0].length(), maxCol2 = tmpTokens[1].length();
		for (int i = 1; i < display4List.size(); i++) {
			String[] tokens = display4List.get(i).split(delim);
			int col1 = tokens[0].length();
			int col2 = tokens[1].length();

			if (col1 > maxCol1)
				maxCol1 = col1;
			if (col2 > maxCol2)
				maxCol2 = col2;
		}

		StringBuilder sbuf2 = new StringBuilder();
		for (int i = 0; i < display4List.size(); i++) {

			String[] tokens = display4List.get(i).split(delim);

			String elem1 = tokens[0];
			String elem2 = tokens[1];
			String elem3 = tokens[2];
			sbuf2.setLength(0);

			sbuf2.append(elem1);
			int gap = maxCol1 - elem1.length() + 2;
			UtilStr.addGap(sbuf2, gap);

			sbuf2.append("| " + elem2);
			gap = maxCol2 - elem2.length() + 1;
			UtilStr.addGap(sbuf2, gap);

			sbuf2.append("| " + elem3);

			UtilLog.w(" " + sbuf2);
		}
	}

	/**
	 * @METHOD
	 */
	public static void addGap(StringBuilder sbuf, int gap) {
		for (int j = 0; j < gap; j++) {
			sbuf.append(" ");
		}
	}

	/**
	 * @METHOD
	 */
	public static String getMostSimilarStartWithSubStr(String str1, String str2) {
		StringBuilder sbuf1 = new StringBuilder();

		// * Choose the one with the less length.
		int size = (str1.length() > str2.length()) ? str2.length() : str1.length();

		for (int i = 0; i < size; i++) {
			if (str1.charAt(i) == str2.charAt(i)) {
				sbuf1.append(str1.charAt(i));
			}
			else {
				break;
			}
		}
		if (sbuf1.length() < size) {
			sbuf1.append("*");
		}
		return sbuf1.toString();
	}

	/**
	 * @METHOD
	 */
	public static boolean comparePath(String path1, String path2) {
		String newPath1 = path1.replace("\\", "/");
		String newPath2 = path2.replace("\\", "/");
		return newPath1.endsWith(newPath2);
	}

	/** @METHOD */
	public static boolean compare(String str, List<String> list) {
		String x = str.replace(" ", "");
		x = x.replace("[", "").replace("]", "");
		String y = list.toString().replace(" ", "");
		y = y.replace("[", "").replace("]", "");
		return x.equals(y);
	}

	public static boolean contains(String src, String trg) {
		for (int i = 0; i < src.length(); i++) {
			char c = src.charAt(i);

			if (c == trg.charAt(0)) {
				String cur = src.substring(i, i + trg.length());
				if (cur.equals(trg))
					return true;
			}
		}
		return false;
	}

	/** @METHOD */
	public static boolean contains(String[] array, String key) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].trim().equals(key.trim()))
				return true;
		}
		return false;
	}

	/** @METHOD */
	public static boolean contains(String[] largeArray, String[] smallArray) {
		if ((largeArray.length < smallArray.length) ||
				(largeArray.length == 0 && smallArray.length == 0))
			return false;

		for (String attrValue : smallArray) {

			if (UtilStr.contains(largeArray, attrValue) == false)
				return false;
		}
		return true;
	}

	/** @METHOD */
	public static boolean match(String[] theArrayA, String[] theArrayB) {
		if (theArrayA.length != theArrayB.length)
			return false;

		for (String elementOfArrayB : theArrayB) {

			if (UtilStr.contains(theArrayA, elementOfArrayB) == false)
				return false;
		}
		return true;
	}

	/**
	 * @METHOD
	 */
	public static void ExplodeString(String methodName, ArrayList<String> tokens) {
		StringBuilder running = new StringBuilder("");
		for (int i = 0; i < methodName.length(); i++) {
			char c = methodName.charAt(i);
			if (Character.isUpperCase(c)) {
				if (i > 0 && Character.isLowerCase(methodName.charAt(i - 1))) {
					if (running.length() > 0)
						tokens.add(running.toString());
					running = new StringBuilder("");
				}
				else if (methodName.length() > (i + 1) && Character.isLowerCase(methodName.charAt(i + 1))) {
					if (running.length() > 0)
						tokens.add(running.toString());
					running = new StringBuilder("");
				}
			}
			else if (isDelimitingCharacter(c)) {
				if (running.length() > 0)
					tokens.add(running.toString());
				running = new StringBuilder("");
				continue;
			}
			running.append(c);
		}
		if (running.length() > 0)
			tokens.add(running.toString());
	}

	/**
	 * @METHOD
	 */
	public static ArrayList<String> ExplodeString(String name) {
		ArrayList<String> ans = new ArrayList<String>();
		ExplodeString(name, ans);
		return ans;
	}

	/**
	 * @METHOD
	 */
	public static boolean isDelimitingCharacter(char c) {
		return c == '_' || c == '*';
	}

	/**
	 * @METHOD
	 */
	public static String getStrBetween(String src, String left, String right) {
		int pos1 = src.indexOf(left) + left.length();
		int pos2 = src.indexOf(right);
		if (pos1 == -1 || pos2 == -1)
			return null;
		return src.substring(pos1, pos2);
	}

	/** @METHOD */
	public static boolean endsWithIgnoreWhiteSpace(String decl, String token) {
		int index = decl.length() - 1;
		Stack<Character> stack = new Stack<Character>();
		for (int i = 0; i < token.length(); index--) {
			if (decl.charAt(index) == ' ' || decl.charAt(index) == '\t')
				continue;
			char c = decl.charAt(index);
			stack.push(c);
			i++;
		}

		int stack_sz = stack.size();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < stack_sz; i++) {
			Character elem = stack.pop();
			sb.append(elem.toString());
		}
		if (token.equals(sb.toString()))
			return true;
		return false;
	}

	/** @METHOD */
	public static boolean hasAttributeStr(String elem) {
		Integer index[] = { -1, -1 };

		for (int i = 0; i < elem.length(); i++) {
			char ch = elem.charAt(i);

			if (ch == '(')
				index[0] = i;
			if (ch == ')')
				index[1] = i;
			if (index[0] != -1 && index[1] != -1) {
				if (hasAttributeStr(index, elem) == false)
					return false;
				for (int j = 0; j < index.length; j++)
					index[j] = -1;
			}
		}
		return true;
	}

	/** @METHOD */
	private static boolean hasAttributeStr(Integer[] index, String elem) {
		String substr = elem.substring(index[0], index[1]);

		int encnter = 0;
		for (int i = 0; i < substr.length(); i++) {
			char ch = substr.charAt(i);
			if (ch == '"') {
				encnter++;
			}
		}
		if (encnter != 0 && encnter % 2 == 0) {
			return true;
		}
		return false;
	}

	/** @METHOD */
	public static String upFirstChar(String key) {
		String theFirstChar = String.valueOf(key.charAt(0)).toUpperCase();
		String result = theFirstChar + key.substring(1);
		return result;
	}

	/** @METHOD */
	public static String lwFirstChar(String key) {
		String theFirstChar = String.valueOf(key.charAt(0)).toLowerCase();
		String result = theFirstChar + key.substring(1);
		return result;
	}

	// /** @METHOD */
	// public static void compare(String transformed, String original) {
	// for (int i = 0; i < transformed.length(); i++) {
	// char transCh = transformed.charAt(i);
	// for (int j = 0; j < original.length(); j++) {
	// char orgCh = original.charAt(j);
	// if (transCh == orgCh) {
	// String result = substr(transformed.substring(i), original.substring(j));
	// System.out.println("[DBG]" + result);
	// }
	// }
	// }
	// }
	//
	// /** @METHOD */
	// private static String substr(String subs1, String subs2) {
	// String result = "";
	// int size = Math.min(subs1.length(), subs2.length());
	// for (int i = 0; i < size; i++) {
	// //if (subs1)
	// }
	// return null;
	// }
}
