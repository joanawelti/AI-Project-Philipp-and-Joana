package dungeon.model.items.treasure;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import org.w3c.dom.Node;

import dungeon.model.structure.Door;
import dungeon.utils.Persistent;
import dungeon.utils.XMLHelper;

/**
 * A key to a door in the dungeon
 * <BR>
 * Keys can be generic, or can be tied to a specific door
 */
public class Key extends Treasure implements Persistent
{
	/**
	 * Constructor (generic key)
	 */
	public Key()
	{
	}
	
	/**
	 * Constructor (for a specific door)
	 * @param door_id The ID of the door
	 */
	public Key(int door_id)
	{
		fDoorID = door_id;
	}
	
	public String getName()
	{
		return "Key";
	}

	public double getSize()
	{
		return 0.8;
	}

	public double getWeight()
	{
		return 0.1;
	}

	public int getWorth()
	{
		return 0;
	}

	/**
	 * @return Returns the ID of the door (or Door.ANY_KEY for a generic key)
	 */
	public int getDoorID()
	{
		return fDoorID;
	}
	/**
	 * Sets the door that the key can open
	 * @param door_id The ID of the door (or (Door.ANY_KEY for a generic key)
	 */
	public void setDoorID(int door_id)
	{
		fDoorID = door_id;
	}
	private int fDoorID = Door.ANY_KEY;

	public void draw(Graphics2D g, Rectangle2D rect, boolean highlight)
	{
		// Mechanism
		double mech_width = rect.getWidth() * 0.25;
		double mech_height = rect.getHeight() * 0.2;
		double mech_x = rect.getX() + rect.getWidth() - mech_width;
		double mech_y = rect.getY() + (rect.getHeight() / 2);
		Rectangle2D mech = new Rectangle2D.Double(mech_x, mech_y, mech_width, mech_height);
		
		g.setColor(Color.GRAY);
		g.fill(mech);
		
		g.setColor(Color.DARK_GRAY);
		g.draw(mech);
		
		// Bar
		double bar_height = rect.getHeight() / 8;
		double bar_y = rect.getY() + ((rect.getHeight() - bar_height) / 2);
		Rectangle2D bar = new Rectangle2D.Double(rect.getX(), bar_y, rect.getWidth(), bar_height);
		
		g.setColor(Color.GRAY);
		g.fill(bar);
		
		g.setColor(Color.DARK_GRAY);
		g.draw(bar);
		
		// Handle
		double handle_size = rect.getHeight() * 0.4;
		double handle_y = rect.getY() + ((rect.getHeight() - handle_size) / 2);
		Ellipse2D handle = new Ellipse2D.Double(rect.getX(), handle_y, handle_size, handle_size);
		
		g.setColor(Color.GRAY);
		g.fill(handle);
		
		g.setColor(Color.DARK_GRAY);
		g.draw(handle);
	}

	public void load(Node node)
	{
		super.load(node);
		fDoorID = XMLHelper.getIntValue(node, "DoorID");
	}

	public void save(Node node)
	{
		super.save(node);
		XMLHelper.setIntValue(node, "DoorID", fDoorID);
	}
}
