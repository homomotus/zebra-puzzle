package org.kklenski;

import static org.kklenski.Zebra.AssociationType.NEXT_TO;
import static org.kklenski.Zebra.AssociationType.SAME;
import static org.kklenski.Zebra.AssociationType.TO_THE_LEFT_OF;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import junit.framework.Assert;

import org.junit.Test;

/**

Input:

5
SAME;nationality;English;color;Red
SAME;nationality;Spaniard;pet;Dog
SAME;drink;Coffee;color;Green
SAME;drink;Tea;nationality;Ukrainian
TO_THE_LEFT_OF;color;Ivory;color;Green
SAME;smoke;Old gold;pet;Snails
SAME;smoke;Kools;color;Yellow
SAME;drink;Milk;position;3
SAME;nationality;Norwegian;position;1
NEXT_TO;smoke;Chesterfields;pet;Fox
NEXT_TO;smoke;Kools;pet;Horse
SAME;smoke;Lucky strike;drink;Orange juice
SAME;smoke;Parliaments;nationality;Japanese
NEXT_TO;color;Blue;nationality;Norwegian
SAME;drink;Water
SAME;pet;Zebra

Example of the output file output.xml
<solutions>
  <solution>
    <house position="1" color="Yellow" nationality="Norwegian" drink="Water"        smoke="Kools"         pet="Fox"/>
    <house position="2" color="Blue"   nationality="Ukrainian" drink="Tea"          smoke="Chesterfields" pet="Horse"/>
    <house position="3" color="Red"    nationality="English"   drink="Milk"         smoke="Old gold"      pet="Snails"/>
    <house position="4" color="Ivory"  nationality="Spaniard"  drink="Orange juice" smoke="Lucky strike"  pet="Dog"/>
    <house position="5" color="Green"  nationality="Japanese"  drink="Coffee"       smoke="Parliaments"   pet="Zebra"/>
  </solution>
</solutions>

 * 
 * @author kklenski
 *
 */
public class Zebra {
	
	public static enum AssociationType {
		SAME,
		TO_THE_LEFT_OF,
		NEXT_TO
	}
	
	public static class HouseProperty {
		String name;
		String value;
		
		public HouseProperty(String name, String value) {
			this.name = name;
			this.value = value;
		}
		
		@Override
		public String toString() {
			return name+'='+value;
		}
		
	}
	
	
	public static class Association {
		AssociationType type;
		HouseProperty prop1;
		HouseProperty prop2;
		
		public Association(AssociationType type, HouseProperty prop1,
				HouseProperty prop2) {
			
			this.type = type;
			this.prop1 = prop1;
			this.prop2 = prop2;
		}
		
		@Override
		public String toString() {
			return new StringBuilder().append(type).append(';').append(prop1).append(';').append(prop2).toString();
		}
	}
	
	public static class House {
		
		private static final String PROP_POSITION = "position";
		
		Map<String, String> props = new HashMap<String, String>();
		
		public House() {
			super();
		}
		
		public House(int position) {
			this();
			props.put(PROP_POSITION, Integer.toString(position));
		}

		@Override
		protected House clone() {
			House house = new House();
			house.props.putAll(this.props);
			return house;
		}
		
		//FIXME unit test
		public boolean addProperty(HouseProperty property) {
			String value = props.get(property.name);
			if (value == null) {
				props.put(property.name, property.value);
				return true;
			} else {
				if (value.equals(property.value)) {
					return true;
				}
			}
			return false;
		}
	}
	
	//FIXME unit test for no solution
	//FIXME unit test for multiple solutions
	@Test
	public void testSolve() {
		List<Association> rules = new ArrayList<Association>();
		rules.add(new Association(SAME, new HouseProperty("nationality", "English"), new HouseProperty("color", "Red")));
		rules.add(new Association(SAME, new HouseProperty("nationality", "Spaniard"), new HouseProperty("pet", "Dog")));
		rules.add(new Association(SAME, new HouseProperty("drink", "Coffee"), new HouseProperty("color", "Green")));
		rules.add(new Association(SAME, new HouseProperty("drink", "Tea"), new HouseProperty("nationality", "Ukrainian")));
		rules.add(new Association(TO_THE_LEFT_OF, new HouseProperty("color", "Ivory"), new HouseProperty("color", "Green")));
		rules.add(new Association(SAME, new HouseProperty("smoke", "Old gold"), new HouseProperty("pet", "Snails")));
		rules.add(new Association(SAME, new HouseProperty("smoke", "Kools"), new HouseProperty("color", "Yellow")));
		rules.add(new Association(SAME, new HouseProperty("drink", "Milk"), new HouseProperty("position", "3")));
		rules.add(new Association(SAME, new HouseProperty("nationality", "Norwegian"), new HouseProperty("position", "1")));
		rules.add(new Association(NEXT_TO, new HouseProperty("smoke", "Chesterfields"), new HouseProperty("pet", "Fox")));
		rules.add(new Association(NEXT_TO, new HouseProperty("smoke", "Kools"), new HouseProperty("pet", "Horse")));
		rules.add(new Association(SAME, new HouseProperty("smoke", "Lucky strike"), new HouseProperty("drink", "Orange juice")));
		rules.add(new Association(SAME, new HouseProperty("smoke", "Parliaments"), new HouseProperty("nationality", "Japanese")));
		rules.add(new Association(NEXT_TO, new HouseProperty("color", "Blue"), new HouseProperty("nationality", "Norwegian")));
		rules.add(new Association(SAME, new HouseProperty("drink", "Water"), null));
		rules.add(new Association(SAME, new HouseProperty("pet", "Zebra"), null));
		
		House[] houses = solve(rules, 5);
		
		if (houses != null) {
			print(houses);
		} else {
			System.out.println("no solution");
		}
		
		Assert.assertTrue(houses[4].props.get("pet").equals("Zebra"));
		Assert.assertTrue(houses[0].props.get("drink").equals("Water"));
	}

	private static void print(House[] houses) {
		Set<String> propKeys = new HashSet<String>();
		for (House house : houses) {
			propKeys.addAll(house.props.keySet());
		}
		
		for (House house : houses) {
			for (String key : propKeys) {
				if (!house.props.containsKey(key)) {
					house.props.put(key, "<empty>");
				}
			}
		}
		
		for (int i = 0; i < houses.length; i++) {
			Map<String, String> props = new TreeMap<String, String>(houses[i].props);
			System.out.print(String.format("House %d:", i));
			for (String value : props.values()) {
				 System.out.print(String.format("%20s", value));
			}
			System.out.println();
		}
	}

	//FIXME: >1 house check
	private static House[] solve(List<Association> rules, int numHouses) {
		List<House[]> boards = new ArrayList<House[]>();
		House[] initialBoard = new House[numHouses];
		for (int i = 0; i < initialBoard.length; i++) {
			initialBoard[i] = new House(i+1);
		}
		
		boards.add(initialBoard);
		
		for (Association association : rules) {
			List<House[]> boardsToAdd = new ArrayList<House[]>();
			System.out.println("Boards: "+boards.size()+" Adding association: "+association);
			for (House[] board : boards) {
				
				Collection<House[]> newBoards = add(association, board);
				if (newBoards != null) {
					boardsToAdd.addAll(newBoards);
				}
				
			}

			boards = boardsToAdd;
			
			//print(boards.get(0));
		}
		return boards.size() > 0 ? boards.get(0) : null;
	}
	
	//FIXME: unit test
	private static Collection<House[]> add(Association association,
			House[] board) {
		
		List<House[]> newBoards = new ArrayList<House[]>();
		
		for (int i = 0; i < board.length; i++) {
			House[] newBoard;
			switch (association.type) {
			case SAME:
				newBoard = clone(board);
				if (newBoard[i].addProperty(association.prop1) && (association.prop2 == null || newBoard[i].addProperty(association.prop2))) {
					newBoards.add(newBoard);
				}
				break;
			case NEXT_TO:
				//FIXME: we could use LinkedList here;
				//right house
				if (i < board.length-1) { 
					newBoard = clone(board);
					if (newBoard[i+1].addProperty(association.prop1) && newBoard[i].addProperty(association.prop2)) {
						newBoards.add(newBoard);
					}
				}
			case TO_THE_LEFT_OF:
				if (i > 0) { //left house
					newBoard = clone(board);
					if (newBoard[i-1].addProperty(association.prop1) && newBoard[i].addProperty(association.prop2)) {
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

	//FIXME: move to Board class
	private static House[] clone(House[] board) {
		House[] result = new House[board.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = board[i].clone();
		}
		return result;
	}
	
}
