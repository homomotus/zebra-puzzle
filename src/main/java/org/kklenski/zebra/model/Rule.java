package org.kklenski.zebra.model;

import java.util.AbstractMap.SimpleEntry;

/**
 * Defines an association between two house properties.<br>
 * A set of rules is used to describe the puzzle. 
 * 
 * @see AssociationType
 * 
 * @author kklenski
 *
 */
public class Rule {
	
	/**
	 * Describes association type between two house properties
	 */
	public static enum AssociationType {
		/**
		 * Properties belong to the same house
		 */
		SAME,
		/**
		 * First property belongs to the house that is situated to the left of the house with the second property
		 */
		TO_THE_LEFT_OF,
		/**
		 * First property belongs to the house that is located from either side of the house with the second property
		 */
		NEXT_TO
	}
	
	@SuppressWarnings("serial")
	public static class HouseProperty extends SimpleEntry<String, String> {

		public HouseProperty(String name, String value) {
			super(name, value);
		}
		
	}
	
	private AssociationType type;
	private HouseProperty prop1;
	private HouseProperty prop2;
	
	
	public Rule(AssociationType type, HouseProperty prop1, HouseProperty prop2) {
		this.type = type;
		this.prop1 = prop1;
		this.prop2 = prop2;
	}
	
	public AssociationType getType() {
		return type;
	}
	
	public HouseProperty getProp1() {
		return prop1;
	}
	
	public HouseProperty getProp2() {
		return prop2;
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append(type).append(';').append(prop1).append(';').append(prop2).toString();
	}
	
}