package org.kklenski.zebra.io;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.kklenski.zebra.model.House;

public class Utils {
	
	/**
	 * Prints solution to {@link System#out}
	 * @param solution 
	 */
	public static void print(House[] solution) {
		Set<String> propKeys = new HashSet<String>();
		for (House house : solution) {
			propKeys.addAll(house.getProps().keySet());
		}

		for (House house : solution) {
			for (String key : propKeys) {
				if (!house.getProps().containsKey(key)) {
					
					house.getProps().put(key, "<empty>");
				}
			}
		}

		for (int i = 0; i < solution.length; i++) {
			Map<String, String> props = new TreeMap<String, String>(
					solution[i].getProps());
			System.out.print(String.format("House %d:", i));
			for (String value : props.values()) {
				System.out.print(String.format("%20s", value));
			}
			System.out.println();
		}
	}
	
	/**
	 * Safely closes input stream.
	 * @param in
	 */
	public static void safeClose(InputStream in) {
		try {
			if (in != null) {
				in.close();
			}
		} catch (Exception ignore) {
		}
	}
	
	/**
	 * Safely closes input stream.
	 * @param in
	 */
	public static void safeClose(OutputStream out) {
		try {
			if (out != null) {
				out.close();
			}
		} catch (Exception ignore) {
		}
	}
	
}
