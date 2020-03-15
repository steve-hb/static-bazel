package de.stvehb.bazel.java.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class XmlUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(XmlUtil.class.getSimpleName());

	private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
	private static final DocumentBuilder DOCUMENT_BUILDER;

	static {
		DOCUMENT_BUILDER_FACTORY.setIgnoringElementContentWhitespace(true);

		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			LOGGER.error("Document builder creation failed", ex);
			System.exit(-1);
		}
		DOCUMENT_BUILDER = documentBuilder;
	}

	/**
	 * Parses the xml file with given <i>path</i>.
	 *
	 * @return a document or null if the file couldn't be parsed
	 */
	public static Document parseDoc(Path path) {
		try {
			return DOCUMENT_BUILDER.parse(path.toFile());
		} catch (SAXException | IOException ex) {
			LOGGER.error("Couldn't parse file: {}", path, ex);
		}

		return null;
	}

	/**
	 * Gets the child element of the given <i>parent</i> node with the given <i>name</i>.
	 *
	 * @return the child element or null if it couldn't be found
	 */
	public static Node getElement(Node parent, String name) {
		return getChildren(parent).stream().filter(c -> c.getNodeName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}

	/**
	 * Gets all child elements of the given <i>parent</i> node.
	 *
	 * @return a list of nodes
	 */
	public static List<Node> getChildren(Node parent) {
		List<Node> children = new ArrayList<>();
		for (int i = 0; i < parent.getChildNodes().getLength(); i++) {
			Node child = parent.getChildNodes().item(i);
			if (child.getParentNode() == parent && !child.getNodeName().equals("#text")) children.add(child);
		}

		return children;
	}

}
