package org.kklenski.zebra.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kklenski.zebra.model.House;
import org.kklenski.zebra.model.Puzzle;
import org.kklenski.zebra.model.Rule;
import org.kklenski.zebra.model.Rule.HouseProperty;

/**
 * <p>
 * This {@link Brain} implementation finds all the possible {@link Puzzle}
 * solutions by checking all possible combinations. It is achieved by fitting
 * all the house properties
 * </p>
 * <p>
 * Algorithm starts with initial solution that has the requested number of empty
 * houses (see {@link Puzzle#getNumHouses()}).<br>
 * {@link Puzzle#getRules()} are applied to candidate solutions one by one. It
 * is a common situation that house properties defined in the rule can be
 * applied to solution in a number of ways - in this case each of the options
 * will lead to a separate solution.
 * </p>
 * 
 * @author kklenski
 * 
 */
public class BruteForceBrain implements Brain {
	
	@Override
	public Collection<House[]> solve(Puzzle puzzle) {
		List<House[]> solutions = new ArrayList<House[]>();
		House[] initialSolution = new House[puzzle.getNumHouses()];
		for (int i = 0; i < initialSolution.length; i++) {
			initialSolution[i] = new House(i + 1);
		}

		solutions.add(initialSolution);

		for (Rule rule : puzzle.getRules()) {
			List<House[]> newSolutions = new ArrayList<House[]>();
			//System.out.println("Solutions: " + solutions.size()	+ " Adding association: " + rule);
			for (House[] solution : solutions) {
				for (int i = 0; i < solution.length; i++) {
					newSolutions.addAll((apply(rule, solution, i)));
				}
			}

			solutions = newSolutions;
		}
		
		return solutions;
	}
	
	/**
	 * Try to apply <code>rule</code> to a <code>solution</code>. If the application leads to new
	 * solution(s) they will be returned. In case rule does not fit the
	 * solution at the specified <code>houseIndex</code>, empty collection will be returned.
	 * 
	 * @param rule
	 * @param solution
	 * @param houseIndex
	 * @return new solutions
	 */
	protected Collection<House[]> apply(Rule rule, House[] solution, int houseIndex) {
		House[] newSolution;
		List<House[]> result = new ArrayList<House[]>();
		switch (rule.getType()) {
		case SAME:
			newSolution = clone(solution);
			if (addProperty(newSolution[houseIndex], rule.getProp1())
					&& (rule.getProp2() == null || addProperty(newSolution[houseIndex], rule.getProp2()))) {
				result.add(newSolution);
			}
			break;
		case NEXT_TO:
			//right house
			if (houseIndex < solution.length - 1) { 
				newSolution = clone(solution);
				if (addProperty(newSolution[houseIndex + 1], rule.getProp1())
						&& addProperty(newSolution[houseIndex], rule.getProp2())) {
					result.add(newSolution);
				}
			}
			//fall through to left house processing
		case TO_THE_LEFT_OF:
			//left house
			if (houseIndex > 0) { 
				newSolution = clone(solution);
				if (addProperty(newSolution[houseIndex - 1], rule.getProp1())
						&& addProperty(newSolution[houseIndex], rule.getProp2())) {
					result.add(newSolution);
				}
			}
			break;
		}
		return result;
	}
	
	/**
	 * Try to add property to a house. 
	 * 
	 * @param house
	 * @param property
	 * @return <code>true</code> only if property was successfully added or property of this type having the same value already exists
	 */
	protected boolean addProperty(House house, HouseProperty property) {
		String value = house.getProps().get(property.getKey());
		if (value == null) {
			house.getProps().put(property.getKey(), property.getValue());
			return true;
		} else {
			if (value.equals(property.getValue())) {
				return true;
			}
		}
		return false;
	}
	
	House[] clone(House[] solution) {
		House[] result = new House[solution.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = solution[i].clone();
		}
		return result;
	}
	
}