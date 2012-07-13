package org.kklenski.zebra.ai;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Test;
import org.kklenski.zebra.model.House;
import org.kklenski.zebra.model.Rule;
import org.kklenski.zebra.model.Rule.AssociationType;
import org.kklenski.zebra.model.Rule.HouseProperty;

public class BruteForceBrainTest extends BrainTest {
	
	private BruteForceBrain brain = new BruteForceBrain();
	
	@Override
	protected Brain getBrain() {
		return brain;
	}

	@Test
	public void testApplySameSingleProperty() {
		//FIXME: Solution class would be better here with it's own constructor doing initialization
		House[] solution = initSolution(2);
		solution[0].getProps().put("drink", "Water");
		solution[1].getProps().put("pet", "Snake");
		
		Rule rule = new Rule(AssociationType.SAME, new HouseProperty("pet", "Zebra"));
		Collection<House[]> solutions = brain.apply(rule, solution, 0);
		Assert.assertEquals(1, solutions.size());
		House[] expected = simulateApply(solution, rule, 0, 0);
		House[] actual = solutions.iterator().next();
		Assert.assertTrue(equals(actual, expected));
		
		//house with conflicting property
		solutions = brain.apply(rule, solution, 1);
		Assert.assertEquals(0, solutions.size());
	}
	
	@Test
	public void testApplySameTwoProperties() {
		House[] solution = initSolution(7);
		solution[0].getProps().put("drink", "Water");
		solution[1].getProps().put("pet", "Snake");
		solution[1].getProps().put("nationality", "English");
		solution[2].getProps().put("pet", "Snake");
		solution[3].getProps().put("nationality", "English");
		solution[4].getProps().put("pet", "Zebra");
		solution[5].getProps().put("nationality", "Japanese");
		solution[6].getProps().put("pet", "Zebra");
		solution[6].getProps().put("nationality", "Japanese");
		
		Rule rule = new Rule(AssociationType.SAME, new HouseProperty("pet", "Zebra"), new HouseProperty("nationality", "Japanese"));
		
		//no confict
		Collection<House[]> solutions = brain.apply(rule, solution, 0);
		Assert.assertEquals(1, solutions.size());
		House[] expected = simulateApply(solution, rule, 0, 0);
		House[] actual = solutions.iterator().next();
		Assert.assertTrue(equals(actual, expected));

		//pet confict & nationality confict
		solutions = brain.apply(rule, solution, 1);
		Assert.assertEquals(0, solutions.size());
		
		//pet confict
		solutions = brain.apply(rule, solution, 2);
		Assert.assertEquals(0, solutions.size());

		//nationality conflict
		solutions = brain.apply(rule, solution, 3);
		Assert.assertEquals(0, solutions.size());
		
		//nationality match
		solutions = brain.apply(rule, solution, 4);
		Assert.assertEquals(1, solutions.size());
		expected = simulateApply(solution, rule, 4, 4);
		actual = solutions.iterator().next();
		Assert.assertTrue(equals(actual, expected));
		
		//pet match
		solutions = brain.apply(rule, solution, 5);
		Assert.assertEquals(1, solutions.size());
		expected = simulateApply(solution, rule, 5, 5);
		actual = solutions.iterator().next();
		Assert.assertTrue(equals(actual, expected));
		
		//pet match & nationality match
		solutions = brain.apply(rule, solution, 6);
		Assert.assertEquals(1, solutions.size());
		expected = simulateApply(solution, rule, 6, 6);
		actual = solutions.iterator().next();
		Assert.assertTrue(equals(actual, expected));
	}
	
	private House[] simulateApply(House[] solution, Rule rule, int prop1Index, int prop2Index) {
		House[] result = brain.clone(solution);
		result[prop1Index].getProps().put(rule.getProp1().getKey(), rule.getProp1().getValue());
		if (rule.getProp2() != null) {
			result[prop2Index].getProps().put(rule.getProp2().getKey(), rule.getProp2().getValue());
		}
		return result;
	}
	
	private House[] initSolution(int numHouses) {
		House[] solution = new House[numHouses];
		for (int i = 0; i < solution.length; i++) {
			solution[i] = new House(i);
		}
		return solution;
	}
	
	//FIXME: Solution class could provide equals
	private boolean equals(House[] expected, House[] actual) {
		if (expected.length != actual.length) {
			return false;
		}
		for (int i = 0; i < actual.length; i++) {
			if (!actual[i].getProps().equals(expected[i].getProps())) {
				return false;
			}
		}
		return true;
	}

	@Test
	public void testApplyNextTo() {
		Rule rule = new Rule(AssociationType.NEXT_TO, new HouseProperty("pet", "Zebra"), new HouseProperty("nationality", "Japanese"));
		House[] solution = initSolution(3);
		
		//1st house
		Collection<House[]> solutions = brain.apply(rule, solution, 0);
		Assert.assertEquals(1, solutions.size());
		
		House[] expected = simulateApply(solution, rule, 1, 0);
		House[] actual = solutions.iterator().next();
		Assert.assertTrue(equals(actual, expected));
		
		//2nd house
		solutions = brain.apply(rule, solution, 1);
		Assert.assertEquals(2, solutions.size());
		
		Iterator<House[]> iterator = solutions.iterator();
		expected = simulateApply(solution, rule, 2, 1);
		actual = iterator.next();
		Assert.assertTrue(equals(expected, actual));
		expected = simulateApply(solution, rule, 0, 1);
		actual = iterator.next();
		Assert.assertTrue(equals(expected, actual));
		
		//3rd house
		solutions = brain.apply(rule, solution, 2);
		Assert.assertEquals(1, solutions.size());
		
		expected = simulateApply(solution, rule, 1, 2);
		actual = solutions.iterator().next();
		Assert.assertTrue(equals(expected, actual));
	}
	
	@Test
	public void testApplyToTheLeft() {
		Rule rule = new Rule(AssociationType.TO_THE_LEFT_OF, new HouseProperty("pet", "Zebra"), new HouseProperty("nationality", "Japanese"));
		House[] solution = initSolution(2);
		
		//1st house
		Collection<House[]> solutions = brain.apply(rule, solution, 0);
		Assert.assertEquals(0, solutions.size());
		
		//2nd house
		solutions = brain.apply(rule, solution, 1);
		Assert.assertEquals(1, solutions.size());
		
		Iterator<House[]> iterator = solutions.iterator();
		House[] expected = simulateApply(solution, rule, 0, 1);
		House[] actual = iterator.next();
		Assert.assertTrue(equals(expected, actual));
	}
	
	@Test
	public void testAdd() {
		House house = new House(1);
		Assert.assertTrue(brain.addProperty(house, new HouseProperty("pet", "Zebra")));
		Assert.assertFalse(brain.addProperty(house, new HouseProperty("pet", "Cobra")));
		Assert.assertTrue(brain.addProperty(house, new HouseProperty("pet", "Zebra")));
	}

}
