package org.kklenski.zebra.io;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.kklenski.zebra.model.House;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Serializes solutions into XML format.
 * 
 * @author kklenski
 * 
 */
public class XMLSolutionWriter implements SolutionWriter {

	@Override
	public void write(Collection<House[]> solutions, OutputStream out) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}
		Document document = documentBuilder.newDocument();
		Element rootElement = document.createElement("solutions");
		document.appendChild(rootElement);
		for (House[] houses : solutions) {
			Element elSolution = document.createElement("solution");
			rootElement.appendChild(elSolution);
			for (House house : houses) {
				Element elHouse = document.createElement("house");
				elSolution.appendChild(elHouse);
				for (Entry<String, String> property : house.getProps()
						.entrySet()) {
					elHouse.setAttribute(property.getKey(), property.getValue());
				}
			}
		}

		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException(e);
		}
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(out);
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}
	}

}