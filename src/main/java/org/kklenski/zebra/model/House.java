package org.kklenski.zebra.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a house that can have arbitrary properties.
 * 
 * @author kklenski
 *
 */
public class House {
	
	//FIXME add this constraint to doc, better remove this from model object
	private static final String PROP_POSITION = "position";

	private Map<String, String> props = new HashMap<String, String>();

	private House() {
		super();
	}

	public House(int position) {
		this();
		props.put(PROP_POSITION, Integer.toString(position));
	}

	@Override
	public House clone() {
		House house = new House();
		house.props.putAll(this.props);
		return house;
	}

	public Map<String, String> getProps() {
		return props;
	}
	
	@Override
	public String toString() {
		return props.toString();
	}
}