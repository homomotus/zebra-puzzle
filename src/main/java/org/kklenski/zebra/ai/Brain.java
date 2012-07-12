package org.kklenski.zebra.ai;

import java.util.Collection;

import org.kklenski.zebra.model.House;
import org.kklenski.zebra.model.Puzzle;

/**
 * Provides the ability to solve zebra puzzles, see {@link #solve(Puzzle)} for more details.
 * 
 * @author kklenski
 *
 */
public interface Brain {
	
	/**
	 * Finds solutions to the provided zebra <code>puzzle</code>.
	 * @param puzzle 
	 * @return found solutions
	 */
	Collection<House[]> solve(Puzzle puzzle);
}