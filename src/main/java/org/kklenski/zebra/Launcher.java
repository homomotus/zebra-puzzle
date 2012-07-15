package org.kklenski.zebra;


import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.kklenski.zebra.ai.Brain;
import org.kklenski.zebra.ai.BruteForceBrain;
import org.kklenski.zebra.io.CSVPuzzleReader;
import org.kklenski.zebra.io.PuzzleReader;
import org.kklenski.zebra.io.PuzzleReader.PuzzleFormatException;
import org.kklenski.zebra.io.SolutionWriter;
import org.kklenski.zebra.io.XMLSolutionWriter;
import org.kklenski.zebra.model.House;
import org.kklenski.zebra.model.Puzzle;



/**
 * Command-line launcher for solving zebra puzzles.
 * 
 * @author kklenski
 * 
 */
public class Launcher {
	
	public static void main(String[] args) {
		PuzzleReader reader = new CSVPuzzleReader();
		InputStream in = System.in;
		Puzzle puzzle;
		try {
			puzzle = reader.read(in);
		} catch (IOException e) {
			System.out.println("Puzzle loading error: "+e.getMessage());
			return;
		} catch (PuzzleFormatException e) {
			System.out.println("Please check puzzle format: "+e.getMessage());
			return;
		}
		Brain brain = new BruteForceBrain();
		Collection<House[]> solutions = brain.solve(puzzle);
		SolutionWriter writer = new XMLSolutionWriter();
		writer.write(solutions, System.out);
	}
	
}
