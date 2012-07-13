package org.kklenski.zebra.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.kklenski.zebra.io.PuzzleReader.PuzzleFormatException;
import org.kklenski.zebra.model.Puzzle;
import org.kklenski.zebra.model.Rule;
import org.kklenski.zebra.model.Rule.AssociationType;

import static org.kklenski.zebra.io.CSVPuzzleReader.*;

public class CSVPuzzleReaderTest {

	private static String EOL = System.getProperty("line.separator");
	private CSVPuzzleReader reader;

	@Before
	public void setUp() throws Exception {
		reader = new CSVPuzzleReader();
	}

	@Test
	public void testRead() throws IOException, PuzzleFormatException {
		testRead(DEFAULT_CHARSET, DEFAULT_DELIMITER);
	}
	
	private void testRead(Charset charset, char delim) throws IOException, PuzzleFormatException {
		StringBuilder data = new StringBuilder();
		int numHouses = 5;
		data.append(numHouses).append(EOL);
		data.append("SAME").append(delim).append("pet").append(delim).append("Zebra").append(delim).append("nationality").append(delim).append("Japanese").append(EOL);
		data.append("NEXT_TO").append(delim).append("drink").append(delim).append("Vodka").append(delim).append("nationality").append(delim).append("Japanese").append(EOL);
		data.append("SAME").append(delim).append("drink").append(delim).append("Water");
		InputStream in = new ByteArrayInputStream(data.toString().getBytes(charset));
		
		try {
			reader.charset = charset;
			reader.delimiter = delim;
			Puzzle puzzle = reader.read(in);
			
			Assert.assertEquals(numHouses, puzzle.getNumHouses());
			Assert.assertEquals(3, puzzle.getRules().size());
			
			Rule rule = puzzle.getRules().iterator().next();
			Assert.assertEquals(AssociationType.SAME, rule.getType());
			Assert.assertEquals("pet", rule.getProp1().getKey());
			Assert.assertEquals("Zebra", rule.getProp1().getValue());
			Assert.assertEquals("nationality", rule.getProp2().getKey());
			Assert.assertEquals("Japanese", rule.getProp2().getValue());
		} finally {
			safeClose(in);
		}
	}

	@Test(expected=PuzzleFormatException.class)
	public void testReadEmpty() throws IOException, PuzzleFormatException {
		StringBuilder data = new StringBuilder();
		InputStream in = new ByteArrayInputStream(data.toString().getBytes());
		
		try {
			reader.read(in);
		} finally {
			safeClose(in);
		}
	}
	
	@Test(expected=PuzzleFormatException.class)
	public void testReadWrongNumberOfFields() throws IOException, PuzzleFormatException {
		StringBuilder data = new StringBuilder();
		int numHouses = 5;
		data.append(numHouses).append(EOL);
		data.append("SAME;pet").append(EOL);
		InputStream in = new ByteArrayInputStream(data.toString().getBytes());

		try {
			reader.read(in);
		} finally {
			safeClose(in);
		}
	}
	
	@Test(expected=PuzzleFormatException.class)
	public void testReadUnknownAssociation() throws IOException, PuzzleFormatException {
		StringBuilder data = new StringBuilder();
		int numHouses = 5;
		data.append(numHouses).append(EOL);
		data.append("UNKNOWN_ASSOCIATION_TYPE;pet;Zebra;nationality;Japanese").append(EOL);
		InputStream in = new ByteArrayInputStream(data.toString().getBytes());

		try {
			reader.read(in);
		} finally {
			safeClose(in);
		}
	}
	
	@Test
	public void testReadDifferentCharset() throws IOException, PuzzleFormatException {
		testRead(Charset.forName("UTF-8"), DEFAULT_DELIMITER);
		testRead(Charset.forName("US-ASCII"), DEFAULT_DELIMITER);
		testRead(Charset.forName("ISO-8859-1"), DEFAULT_DELIMITER);
		testRead(Charset.forName("UTF-16LE"), DEFAULT_DELIMITER);
		testRead(Charset.forName("UTF-16BE"), DEFAULT_DELIMITER);
	}
	
	@Test
	public void testReadDifferentDelimiter() throws IOException, PuzzleFormatException {
		testRead(DEFAULT_CHARSET, ',');
	}
	
	private void safeClose(InputStream in) {
		try {
			in.close();
		} catch (Exception ignore) {
		}
	}
	
}
