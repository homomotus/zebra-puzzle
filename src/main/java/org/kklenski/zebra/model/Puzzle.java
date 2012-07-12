package org.kklenski.zebra.model;

import java.util.Collection;

/**
 * Zebra puzzle formalized problem statement.
 * 
 * @author kklenski
 *
 */
public class Puzzle {
	private Collection<Rule> rules;
	private int numHouses;
	
	public Puzzle(Collection<Rule> rules, int numHouses) {
		super();
		this.rules = rules;
		this.numHouses = numHouses;
	}
	
	public Collection<Rule> getRules() {
		return rules;
	}
	
	public int getNumHouses() {
		return numHouses;
	}
	
}