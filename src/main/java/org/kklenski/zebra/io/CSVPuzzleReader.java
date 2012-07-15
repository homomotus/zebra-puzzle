package org.kklenski.zebra.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.kklenski.zebra.model.Puzzle;
import org.kklenski.zebra.model.Rule;
import org.kklenski.zebra.model.Rule.AssociationType;
import org.kklenski.zebra.model.Rule.HouseProperty;

/**
 * <p>
 * Reads {@link Puzzle} definitions CSV-style input. 
 * Input data charset and delimiter can be specified using {@link CSVPuzzleReader#CSVPuzzleReader(Charset, String)}  
 * </p>
 * 
 * Below is an example of CSV representation for the classic zebra puzzle definition: 
 * <code>
 * <pre>
 * 		5
 * 		SAME;nationality;English;color;Red
 * 		SAME;nationality;Spaniard;pet;Dog
 * 		SAME;drink;Coffee;color;Green
 * 		SAME;drink;Tea;nationality;Ukrainian
 * 		TO_THE_LEFT_OF;color;Ivory;color;Green
 * 		SAME;smoke;Old gold;pet;Snails
 * 		SAME;smoke;Kools;color;Yellow
 * 		SAME;drink;Milk;position;3
 * 		SAME;nationality;Norwegian;position;1
 * 		NEXT_TO;smoke;Chesterfields;pet;Fox
 * 		NEXT_TO;smoke;Kools;pet;Horse
 * 		SAME;smoke;Lucky strike;drink;Orange juice
 * 		SAME;smoke;Parliaments;nationality;Japanese
 * 		NEXT_TO;color;Blue;nationality;Norwegian
 * 		SAME;drink;Water
 * 		SAME;pet;Zebra
 * </pre>
 * </code>
 * 
 * @author kklenski
 * 
 */
public class CSVPuzzleReader implements PuzzleReader {
	
	static final Charset DEFAULT_CHARSET = Charset.defaultCharset();
	static final char DEFAULT_DELIMITER = ';';
	Charset charset;
	char delimiter;

	public CSVPuzzleReader() {
		this(DEFAULT_CHARSET, DEFAULT_DELIMITER);
	}
	
	public CSVPuzzleReader(Charset charset, char delimiter) {
		this.charset = charset;
		this.delimiter = delimiter;
	}

	@Override
	public Puzzle read(InputStream in) throws IOException, PuzzleFormatException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
		
		int numHouses;
		String numHousesStr = null;
		try {
			numHousesStr = reader.readLine();
			if (numHousesStr == null) {
				throw new PuzzleFormatException("Empty stream, no puzzle definition data found");
			}
			numHouses = Integer.parseInt(numHousesStr);
		} catch (NumberFormatException e) {
			throw new PuzzleFormatException(String.format("House quantity expected on the first line but '%s' found", numHousesStr));
		}

		List<Rule> rules = new ArrayList<Rule>();
		String line;
		int lineNum = 0;
		while ((line = reader.readLine()) != null) {
			lineNum++;
			
			String[] tokens = line.split(Character.toString(delimiter));
			
			AssociationType type;
			try {
				type = AssociationType.valueOf(tokens[0]);	
			} catch (IllegalArgumentException e) {
				throw new PuzzleFormatException(String.format("Unsupported association type encountered: '%s' on line: %d", tokens[0], lineNum));
			}
			
			HouseProperty prop1;
			HouseProperty prop2 = null;
			switch (tokens.length) {
			case 5:
				prop2 = new HouseProperty(tokens[3], tokens[4]); 
			case 3:
				prop1 = new HouseProperty(tokens[1], tokens[2]);
				break;
			default:
				throw new PuzzleFormatException(String.format("Incorrect number of delimited values: %d on line: %d", tokens.length, lineNum));
			}
			rules.add(new Rule(type, prop1, prop2));
		}
		return new Puzzle(rules, numHouses);	
	}

}
