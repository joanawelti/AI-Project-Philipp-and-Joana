package dungeon.utils;

import org.w3c.dom.*;

import java.io.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;

/**
 * Utility class to handle XML persistence
 */
public class Persistence
{
	/**
	 * Returns the XML data for a Persistent object
	 */
	public static String getXML(Persistent object)
	{
		try
		{
			// Create an empty XML document
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			Document doc = factory.newDocumentBuilder().newDocument();
	
			// Convert the object into XML
			doc.appendChild(doc.createElement(object.getClass().getName()));
			object.save(doc.getDocumentElement());

			// Write out the document to a string
			StringWriter sw = new StringWriter();
			Transformer trans = TransformerFactory.newInstance().newTransformer();
			trans.transform(new DOMSource(doc), new StreamResult(sw));
			
			return sw.toString();
		}
		catch (Exception ex)
		{
			System.err.println(ex);
			return "";
		}
	}
	
	/**
	 * Load the specified object from the given XML file
	 */
	public static void loadFromXML(String filename, Persistent object) throws Exception
	{
		File f = new File(filename);
		
		// Parse in the XML document
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document doc = factory.newDocumentBuilder().parse(f);

		// Load the object from the document
		object.load(doc.getDocumentElement());
	}

	/**
	 * Save the specified object to the given XML file
	 */
	public static void saveToXML(String filename, Persistent object) throws Exception
	{
		File f = new File(filename);

		// Create an empty XML document
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document doc = factory.newDocumentBuilder().newDocument();

		// Convert the object into XML
		String str = object.getClass().getSimpleName();
		Element e = doc.createElement(str);
		doc.appendChild(e);
		object.save(e);

		// Write out the document to the file
		Transformer trans = TransformerFactory.newInstance().newTransformer();
		trans.transform(new DOMSource(doc), new StreamResult(f));
	}
}