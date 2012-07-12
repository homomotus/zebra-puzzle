package org.kklenski.zebra.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kklenski.zebra.model.House;
import org.kklenski.zebra.model.Puzzle;
import org.kklenski.zebra.model.Rule;
import org.kklenski.zebra.model.Rule.HouseProperty;

/**
 * <p>This {@link Brain} implementation finds all the possible {@link Puzzle} solutions by checking all possible combinations.
 * It is achieved by fitting all the house properties 
 * </p>
 * <p>Algorithm starts with initial solution that has the requested number of empty houses (see {@link Puzzle#getNumHouses()}).<br> 
 * {@link Puzzle#getRules()} are analyzed one by one. It is a common situation that house properties defined in the rule can be applied to solution in different ways - 
 * in this case each of the options will lead to a separate solution.</p>
 *   
 * @author kklenski
 *
 */
public class BruteForceBrain implements Brain {
	
	//FIXME unit test
	@Override
	public Collection<House[]> solve(Puzzle puzzle) {
		List<House[]> solutions = new ArrayList<House[]>();
		House[] initialSolution = new House[puzzle.getNumHouses()];
		for (int i = 0; i < initialSolution.length; i++) {
			initialSolution[i] = new House(i + 1);
		}

		solutions.add(initialSolution);

		for (Rule rule : puzzle.getRules()) {
			List<House[]> solutionsToAdd = new ArrayList<House[]>();
			System.out.println("Solutions: " + solutions.size()
					+ " Adding association: " + rule);
			for (House[] solution : solutions) {

				Collection<House[]> newSolutions = apply(rule, solution);
				if (newSolutions != null) {
					solutionsToAdd.addAll(newSolutions);
				}

			}

			solutions = solutionsToAdd;
		}
		
		return solutions;
	}
	
	//FIXME: unit test
	protected Collection<House[]> apply(Rule rule, House[] solution) {
		List<House[]> newBoards = new ArrayList<House[]>();

		for (int i = 0; i < solution.length; i++) {
			House[] newBoard;
			switch (rule.getType()) {
			case SAME:
				newBoard = clone(solution);
				if (addProperty(newBoard[i], rule.getProp1())
						&& (rule.getProp2() == null || addProperty(newBoard[i], rule.getProp2()))) {
					newBoards.add(newBoard);
				}
				break;
			case NEXT_TO:
				if (i < solution.length - 1) { //right house
					newBoard = clone(solution);
					if (addProperty(newBoard[i + 1], rule.getProp1())
							&& addProperty(newBoard[i], rule.getProp2())) {
						newBoards.add(newBoard);
					}
				}
				//continue to left house processing
			case TO_THE_LEFT_OF:
				if (i > 0) { // left house
					newBoard = clone(solution);
					if (addProperty(newBoard[i - 1], rule.getProp1())
							&& addProperty(newBoard[i], rule.getProp2())) {
						newBoards.add(newBoard);
					}
				}
				break;
			default:
				break;
			}
		}
		return newBoards;
	}
	
	// FIXME unit test
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
	
	private House[] clone(House[] board) {
		House[] result = new House[board.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = board[i].clone();
		}
		return result;
	}
	
}