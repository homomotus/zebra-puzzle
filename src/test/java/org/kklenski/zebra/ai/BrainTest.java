package org.kklenski.zebra.ai;

import static org.kklenski.zebra.model.Rule.AssociationType.NEXT_TO;
import static org.kklenski.zebra.model.Rule.AssociationType.SAME;
import static org.kklenski.zebra.model.Rule.AssociationType.TO_THE_LEFT_OF;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.kklenski.zebra.model.House;
import org.kklenski.zebra.model.Puzzle;
import org.kklenski.zebra.model.Rule;
import org.kklenski.zebra.model.Rule.HouseProperty;

public abstract class BrainTest {
	
	private Brain brain;

	@Before
	public void setUp() throws Exception {
		brain = getBrain();
	}

	public abstract Brain getBrain();
	
	@Test
	public void testClassicPuzzle() {
		// FIXME unit test for no solution
		// FIXME unit test for multiple solutions
		List<Rule> rules = new ArrayList<Rule>();
		rules.add(new Rule(SAME, new HouseProperty("nationality", "English"), new HouseProperty("color", "Red")));
		rules.add(new Rule(SAME, new HouseProperty("nationality", "Spaniard"), new HouseProperty("pet", "Dog")));
		rules.add(new Rule(SAME, new HouseProperty("drink", "Coffee"), new HouseProperty("color", "Green")));
		rules.add(new Rule(SAME, new HouseProperty("drink", "Tea"), new HouseProperty("nationality", "Ukrainian")));
		rules.add(new Rule(TO_THE_LEFT_OF, new HouseProperty("color", "Ivory"), new HouseProperty("color", "Green")));
		rules.add(new Rule(SAME, new HouseProperty("smoke", "Old gold"), new HouseProperty("pet", "Snails")));
		rules.add(new Rule(SAME, new HouseProperty("smoke", "Kools"), new HouseProperty("color", "Yellow")));
		rules.add(new Rule(SAME, new HouseProperty("drink", "Milk"), new HouseProperty("position", "3")));
		rules.add(new Rule(SAME, new HouseProperty("nationality", "Norwegian"), new HouseProperty("position", "1")));
		rules.add(new Rule(NEXT_TO, new HouseProperty("smoke", "Chesterfields"), new HouseProperty("pet", "Fox")));
		rules.add(new Rule(NEXT_TO, new HouseProperty("smoke", "Kools"), new HouseProperty("pet", "Horse")));
		rules.add(new Rule(SAME, new HouseProperty("smoke",	"Lucky strike"), new HouseProperty("drink", "Orange juice")));
		rules.add(new Rule(SAME, new HouseProperty("smoke",	"Parliaments"), new HouseProperty("nationality", "Japanese")));
		rules.add(new Rule(NEXT_TO, new HouseProperty("color", "Blue"),	new HouseProperty("nationality", "Norwegian")));
		rules.add(new Rule(SAME, new HouseProperty("drink", "Water"), null));
		rules.add(new Rule(SAME, new HouseProperty("pet", "Zebra"), null));

		Collection<House[]> solutions = brain.solve(new Puzzle(rules, 5));
		
		Assert.assertEquals(1, solutions.size());
		House[] houses = solutions.iterator().next();
		Assert.assertTrue(houses[4].getProps().get("pet").equals("Zebra"));
		Assert.assertTrue(houses[0].getProps().get("drink").equals("Water"));
	}
}
