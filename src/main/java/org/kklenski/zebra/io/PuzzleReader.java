package org.kklenski.zebra.io;

import java.io.IOException;
import java.io.InputStream;

import org.kklenski.zebra.model.Puzzle;

/**
 * Reads {@link Puzzle} definition from input stream. The format of the data in
 * the stream is not restricted and can be different for various
 * implementations.
 * 
 * @author kklenski
 * 
 */
public interface PuzzleReader {
	
	@SuppressWarnings("serial")
	public class PuzzleFormatException extends Exception {

		public PuzzleFormatException(String message) {
			super(message);
		}
	}
	
	/**
	 * Reads puzzle definition from stream.
	 * 
	 * @param in
	 *            Input stream containing puzzle definition
	 * @return puzzle definition model object
	 * @throws IOException
	 * @throws PuzzleFormatException
	 *             in case incorrect definition format is encountered
	 */
	Puzzle read(InputStream in) throws IOException, PuzzleFormatException;
}