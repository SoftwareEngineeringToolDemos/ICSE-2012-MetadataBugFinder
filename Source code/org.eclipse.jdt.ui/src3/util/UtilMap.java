/*
 * @(#) UtilMap.java
 * Copyright 2010 The Software Innovations Lab, Dept. of Computer Science,
 * Virginia Tech. All rights reserved.
 * 2202 Kraft Drive, Blacksburg, VA 24060
 */
package util;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Myoungkyu Song
 * @date Jan 3, 2011
 * @since JDK1.6
 */
public class UtilMap {
	/**
	 * @METHOD
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		// * - the list-up container to sort.
		// *
		List<Entry<K, V>> list = new LinkedList<Entry<K, V>>(map.entrySet());

		Comparator<Entry<K, V>> newComparator = new Comparator<Entry<K, V>>() {
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		};
		
		Collections.sort(list, newComparator);

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (int i = (list.size() - 1); i > -1; i--) {
			Entry<K, V> entry = list.get(i);
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}
