package dungeon.collections;

import java.util.Vector;

import org.w3c.dom.Node;

import dungeon.model.items.mobs.Faction;
import dungeon.utils.Persistent;
import dungeon.utils.XMLHelper;

/**
 * A collection of Faction objects
 */
public class FactionList extends Vector<Faction> implements Persistent
{
	// Required
	private static final long serialVersionUID = 285534892749864246L;

	public void load(Node node)
	{
		clear();
		
		// Find the base node
		Node base = XMLHelper.findChild(node, "Vector");
		if (base == null)
			return;

		int count = base.getChildNodes().getLength();
		for (int a = 0; a != count; ++a)
		{
			// Load this item
			try
			{
				Node first_node = base.getChildNodes().item(0);
				String classname = first_node.getNodeName();
				if (classname.equalsIgnoreCase("faction")) {
				
					Faction faction = new Faction();				
					XMLHelper.loadObject(base, "Faction", faction);
					add(faction);
				}
			}
			catch (Exception ex)
			{
				System.err.println(ex);
			}
			
			Node child = base.getChildNodes().item(0);
			base.removeChild(child);
		}
	}

	public void save(Node node)
	{
		XMLHelper.saveVector(node, "Vector", this);
	}
	
	public Faction find(String name)
	{
		for (Faction f: this)
		{
			if (f.getName().equals(name))
				return f;
		}
		
		return null;
	}
}
