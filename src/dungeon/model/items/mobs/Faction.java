package dungeon.model.items.mobs;

import java.awt.Color;
import java.lang.reflect.Field;

import org.w3c.dom.Node;

import dungeon.ai.Behaviour;
import dungeon.model.Game;
import dungeon.utils.Persistent;
import dungeon.utils.XMLHelper;

/**
 * Class to hold information about a mob faction.
 */
public class Faction implements Persistent
{
	/**
	 * Constructor
	 */
	public Faction()
	{
	}
	
	/**
	 * Constructor
	 * @param name The faction name
	 */
	public Faction(String name)
	{
		fName = name;
	}
	
	/**
	 * @return Returns the name of the faction
	 */
	public String getName()
	{
		return fName;
	}
	/**
	 * Sets the name of the faction
	 * @param name The name
	 */
	public void setName(String name)
	{
		fName = name;
	}
	private String fName = "";
	
	/**
	 * @return Returns the colour of the faction
	 */
	public Color getColour()
	{
		try
		{
			String colour_name = fName.toLowerCase();
			Field f = Color.class.getField(colour_name);
			if (f != null)
			{
				Color c = (Color)f.get(null);
				if (c != null)
					return c;
			}
		}
		catch (Exception ex)
		{
		}
		
		return Color.RED;
	}
	
	/**
	 * @return Returns the faction behaviour
	 */
	public Behaviour getBehaviour()
	{
		return fBehaviour;
	}
	/**
	 * Sets the faction behavour
	 * @param b The behaviour
	 */
	public void setBehaviour(Behaviour b)
	{
		fBehaviour = b;
	}
	private Behaviour fBehaviour = null;
	
	public void onTick(Game game)
	{
		if (fBehaviour != null)
			fBehaviour.onTick(game);
	}

	public void load(Node node)
	{
		fName = XMLHelper.getStrValue(node, "Name");
		
		if (XMLHelper.attributeExists(node, "Behaviour"))
		{
			String class_name = XMLHelper.getStrValue(node, "Behaviour");

			try
			{
				Class<?> c = Class.forName(class_name);
				
				if (Class.forName("dungeon.ai.Behaviour").isAssignableFrom(c))
				{
					Behaviour b = (Behaviour)c.getConstructor(Class.forName("dungeon.model.items.mobs.Faction")).newInstance(this);
					if (b != null)
						fBehaviour = b;
				}
				else
				{
					System.err.println("Specified behaviour " + class_name + " is not a subclass of dungeon.ai.Behaviour");
				}
			}
			catch (Exception ex)
			{
				System.err.println(ex);
			}
		}
	}

	public void save(Node node)
	{
		XMLHelper.setStrValue(node, "Name", fName);
		
		if (getBehaviour() != null)
		{
			String class_name = getBehaviour().getClass().getName();
			XMLHelper.setStrValue(node,"Behaviour", class_name);
		}
	}
}
