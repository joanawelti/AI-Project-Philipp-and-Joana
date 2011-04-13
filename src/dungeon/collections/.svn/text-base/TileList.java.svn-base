package dungeon.collections;

import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;

import org.w3c.dom.Node;

import dungeon.model.structure.Door;
import dungeon.model.structure.Finish;
import dungeon.model.structure.FlameTrap;
import dungeon.model.structure.Floor;
import dungeon.model.structure.Pit;
import dungeon.model.structure.Tile;
import dungeon.utils.Persistent;
import dungeon.utils.XMLHelper;

/**
 * A collection of Tile objects
 */
public class TileList extends Vector<Tile> implements Persistent
{
	// Required
	private static final long serialVersionUID = -3636215766705989267L;

	public void load(Node node)
	{
		HashMap<String, Class<? extends Tile>> classes = new HashMap<String, Class<? extends Tile>>();
		classes.put("Floor", Floor.class);
		classes.put("Door", Door.class);
		classes.put("Pit", Pit.class);
		classes.put("Finish", Finish.class);
		classes.put("FlameTrap", FlameTrap.class);
		
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
				
				Class<? extends Tile> c = classes.get(classname);
				if (c != null) {
					Tile obj = c.newInstance();
				
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
	
	public Tile find(UUID id)
	{
		for (Tile tile: this)
		{
			if (tile.getID().compareTo(id) == 0)
				return tile;
		}
		
		return null;
	}
}
