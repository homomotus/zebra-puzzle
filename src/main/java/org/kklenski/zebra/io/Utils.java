package org.kklenski.zebra.io;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.kklenski.zebra.model.House;

public class Utils {
	
	/**
	 * Prints <code>houses</code> array to {@link System#out}
	 * @param houses 
	 */
	public void print(House[] houses) {
		Set<String> propKeys = new HashSet<String>();
		for (House house : houses) {
			propKeys.addAll(house.getProps().keySet());
		}

		for (House house : houses) {
			for (String key : propKeys) {
				if (!house.getProps().containsKey(key)) {
					
					house.getProps().put(key, "<empty>");
				}
			}
		}

		for (int i = 0; i < houses.length; i++) {
			Map<String, String> props = new TreeMap<String, String>(
					houses[i].getProps());
			System.out.print(String.format("House %d:", i));
			for (String value : props.values()) {
				System.out.print(String.format("%20s", value));
			}
			System.out.println();
		}
	}
	
}
