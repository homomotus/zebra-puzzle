package org.kklenski.zebra.io;

import java.io.OutputStream;
import java.util.Collection;

import org.kklenski.zebra.model.House;

/**
 * Serializes solutions into target {@link OutputStream}. Output format is not
 * restricted and is determined by the implementations.
 * 
 * @author kklenski
 * 
 */
public interface SolutionWriter {

	/**
	 * Writes {@link Collection} of solutions into specified <code>out</code>
	 * stream.
	 * 
	 * @param solutions
	 * @param out
	 *            Stream to write the solutions
	 */
	void write(Collection<House[]> solutions, OutputStream out);

}