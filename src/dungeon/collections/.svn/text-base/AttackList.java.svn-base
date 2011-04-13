package dungeon.collections;

import java.util.HashMap;
import java.util.Vector;

import org.w3c.dom.Node;

import dungeon.model.combat.Attack;
import dungeon.model.combat.Dagger;
import dungeon.model.combat.Longbow;
import dungeon.model.combat.Smash;
import dungeon.model.combat.Sword;
import dungeon.utils.Persistent;
import dungeon.utils.XMLHelper;

/**
 * A collection of Attack objects
 */
public class AttackList extends Vector<Attack> implements Persistent
{
	// Required
	private static final long serialVersionUID = 1227442551015841389L;

	public void load(Node node)
	{
		HashMap<String, Class<? extends Attack>> classes = new HashMap<String, Class<? extends Attack>>();
		classes.put("Smash", Smash.class);
		classes.put("Sword", Sword.class);
		classes.put("Longbow", Longbow.class);
		classes.put("Dagger", Dagger.class);
		
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
				
				Class<? extends Attack> c = classes.get(classname);
				if (c != null) {
					Attack obj = c.newInstance();
				
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
}
