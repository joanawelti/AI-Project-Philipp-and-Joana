package dungeon.collections;

import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;

import org.w3c.dom.Node;

import dungeon.model.items.treasure.Gold;
import dungeon.model.items.treasure.Key;
import dungeon.model.items.treasure.Potion;
import dungeon.model.items.treasure.Treasure;
import dungeon.utils.Persistent;
import dungeon.utils.XMLHelper;

/**
 * A collection of Treasure objects
 */
public class TreasureList extends Vector<Treasure> implements Persistent
{
	// Required
	private static final long serialVersionUID = 285534892749864246L;

	public void load(Node node)
	{
		HashMap<String, Class<? extends Treasure>> classes = new HashMap<String, Class<? extends Treasure>>();
		classes.put("Gold", Gold.class);
		classes.put("Key", Key.class);
		classes.put("Potion", Potion.class);
		
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
				
				Class<? extends Treasure> c = classes.get(classname);
				if (c != null) {
					Treasure obj = c.newInstance();
				
					XMLHelper.loadObject(base, classname, obj);
					add(obj);
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
	
	public Treasure find(UUID id)
	{
		for (Treasure treasure: this)
		{
			if (treasure.getID().compareTo(id) == 0)
				return treasure;
		}
		
		return null;
	}
}
