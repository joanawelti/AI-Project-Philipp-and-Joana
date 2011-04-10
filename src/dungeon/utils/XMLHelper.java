package dungeon.utils;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Vector;

/**
 * Class containing utility methods for XML persistence
 */
public class XMLHelper
{
	/**
	 * Load a Persistent object from the given XML node
	 */
	public static <T extends Persistent> void loadObject(Node parent, String name, T obj)
	{
		// Get the named child node
		Node child = findChild(parent, name);
		if ((child != null) && (obj != null))
		{
			// Load the object
			obj.load(child);
		}
	}

	/**
	 * Save a Persistent object to the given XML node
	 */
	public static <T extends Persistent> void saveObject(Node parent, String name, T obj)
	{
		// Create a new child node
		Node child = createChild(parent, name);
		if ((child != null) && (obj != null))
		{
			// Save the object
			obj.save(child);
		}
	}
	
	/**
	 * Save a vector of Persistent objects to the given XML node
	 */
	public static <T extends Persistent> void saveVector(Node parent, String name, Vector<T> vec)
	{
		// Create a new child node
		Node base = createChild(parent, name);

		// Iterate through the items in the vector
		for (int a = 0; a != vec.size(); ++a)
		{
			// Save this item
			T obj = vec.get(a);
			String classname = obj.getClass().getSimpleName();
			saveObject(base, classname, obj);
		}
	}

	/**
	 * Creates and returns a new XML node with the specified name, as a child
	 * of the given parent
	 * Returns null if the child could not be created
	 */
	public static Node createChild(Node parent, String name)
	{
		// Create the node
		Node child = parent.getOwnerDocument().createElement(name);
		if (child != null)
		{
			// Add it to the document
			parent.appendChild(child);
		}

		return child;
	}

	/**
	 * Returns the child of the given node which has the specified name
	 * If no such node exists, returns null
	 */
	public static Node findChild(Node parent, String name)
	{
		// Iterate through the collection of children
		NodeList nodes = parent.getChildNodes();
		for (int a = 0; a != nodes.getLength(); ++a)
		{
			Node node = nodes.item(a);
			if (node.getNodeName().equals(name))
			{
				// This is the one
				return node;
			}
		}

		// No such node
		return null;
	}

	/**
	 * Returns whether the named attribute exists for the specified node
	 */
	public static boolean attributeExists(Node node, String name)
	{
		if (node == null)
			return false;
		
		return (get_named_attribute(node, name) != null);
	}

	/**
	 * Returns the string value of the named attribute for the given node
	 * If no such attribute exists, returns an empty string
	 */
	public static String getStrValue(Node node, String name)
	{
		// Look for the attribute
		Node att = get_named_attribute(node, name);
		if (att != null)
		{
			// Return the value
			return att.getNodeValue();
		}
		else
		{
			// No such attribute
			return "";
		}
	}

	/**
	 * Returns the integer value of the named attribute for the given node
	 * If no such attribute exists, returns 0
	 */
	public static int getIntValue(Node node, String name)
	{
		// Look for the attribute
		Node att = get_named_attribute(node, name);
		if (att != null)
		{
			// Return the value
			return Integer.valueOf(att.getNodeValue()).intValue();
		}
		else
		{
			// No such attribute
			return 0;
		}
	}

	/**
	 * Returns the boolean value of the named attribute for the given node
	 * If no such attribute exists, returns false
	 */
	public static boolean getBoolValue(Node node, String name)
	{
		// Look for the attribute
		Node att = get_named_attribute(node, name);
		if (att != null)
		{
			// Return the value
			return Boolean.valueOf(att.getNodeValue()).booleanValue();
		}
		else
		{
			// No such attribute
			return false;
		}
	}

	/**
	 * Returns the double value of the named attribute for the given node
	 * If no such attribute exists, returns 0.0
	 */
	public static double getDblValue(Node node, String name)
	{
		// Look for the attribute
		Node att = get_named_attribute(node, name);
		if (att != null)
		{
			// Return the value
			return Double.valueOf(att.getNodeValue()).doubleValue();
		}
		else
		{
			// No such attribute
			return 0.0;
		}
	}

	/**
	 * Sets an attribute on the specified node, with the given name and value
	 */
	public static void setStrValue(Node node, String name, String value)
	{
		Attr att = node.getOwnerDocument().createAttribute(name);
		node.getAttributes().setNamedItem(att);
		att.setValue(value);
	}

	/**
	 * Sets an attribute on the specified node, with the given name and value
	 */
	public static void setIntValue(Node node, String name, int value)
	{
		setStrValue(node, name, Integer.toString(value));
	}

	/**
	 * Sets an attribute on the specified node, with the given name and value
	 */
	public static void setBoolValue(Node node, String name, boolean value)
	{
		setStrValue(node, name, Boolean.toString(value));
	}

	/**
	 * Sets an attribute on the specified node, with the given name and value
	 */
	public static void setDblValue(Node node, String name, double value)
	{
		setStrValue(node, name, Double.toString(value));
	}
        
    /**
     * Returns the given attribute of a node, if it exists
     * Otherwise, returns null
     */
	static Node get_named_attribute(Node node, String att_name)
	{
		if (node == null)
			return null;
		
		// Does the node have attributes?
		NamedNodeMap attributes = node.getAttributes();
		if (attributes != null)
		{
			// Does the node have this attribute?
			return attributes.getNamedItem(att_name);
		}

		// No attributes
		return null;
	}
}