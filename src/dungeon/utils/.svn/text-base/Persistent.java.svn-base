package dungeon.utils;

import org.w3c.dom.Node;

/**
 * Interface to allow for XML persistence
 * <BR>
 * Implemented by classes which are to be serialised to XML
 */
public interface Persistent
{
	/**
	 * Set the object's fields from the given XML node
	 */
	public void load(Node node);

	/**
	 * Save the object's state to the given XML node
	 */
	public void save(Node node);
}