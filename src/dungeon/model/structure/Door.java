package dungeon.model.structure;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.w3c.dom.Node;

import dungeon.model.items.Item;
import dungeon.utils.Persistent;
import dungeon.utils.XMLHelper;

/**
 * A door tile
 * <BR>
 * Doors can be open, closed or locked
 * <BR>
 * <B>Note</B>: By default, locked doors are opened by any key; this can be overridden
 */
public class Door extends Tile implements Persistent
{
	public static final int OPEN = 0;
	public static final int CLOSED = 1;
	public static final int LOCKED = 2;
	
	// This constant is used whenever we need to indicate that a door does not need a specific key
	public static final int ANY_KEY = -1;
	
	/**
	 * Constructor
	 */
	public Door()
	{
	}
	
	/**
	 * Constructor
	 * 
	 * @param x The door's x co-ordinate
	 * @param y The door's y co-ordinate
	 * @param width The door's width
	 * @param height The door's height
	 * @param state OPEN, CLOSED or LOCKED
	 */
	public Door(double x, double y, double width, double height, int state)
	{
		super(x, y, width, height);
		
		fState = state;
	}
	
	/**
	 * Constructor
	 * 
	 * @param x The door's x co-ordinate
	 * @param y The door's y co-ordinate
	 * @param width The door's width
	 * @param height The door's height
	 * @param state OPEN, CLOSED or LOCKED
	 * @param key_id The ID of the required key, or ANY_KEY
	 */
	public Door(double x, double y, double width, double height, int state, int key_id)
	{
		super(x, y, width, height);
		
		fState = state;
		fKeyID = key_id;
	}
	
	/**
	 * Constructor
	 * 
	 * @param area The map area the door will occupy
	 * @param state OPEN, CLOSED or LOCKED
	 */
	public Door(Rectangle2D area, int state)
	{
		super(area);
		
		fState = state;
	}
	
	/**
	 * Constructor
	 * 
	 * @param area The map area the door will occupy
	 * @param state OPEN, CLOSED or LOCKED
	 * @param key_id The ID of the required key, or ANY_KEY
	 */
	public Door(Rectangle2D area, int state, int key_id)
	{
		super(area);
		
		fState = state;
		fKeyID = key_id;
	}
	
	/**
	 * @return Returns the ID of the key required to open the door
	 */
	public int getKeyID()
	{
		return fKeyID;
	}
	/**
	 * Sets the ID of the key required to open the door
	 * @param key_id The ID of the required key, or ANY_KEY
	 */
	public void setKeyID(int key_id)
	{
		fKeyID = key_id;
	}
	private int fKeyID = Door.ANY_KEY;
	
	/**
	 * @return Returns the door state
	 */
	public int getState()
	{
		return fState;
	}
	/**
	 * Sets the door state
	 * 
	 * @param state OPEN, CLOSED or LOCKED
	 */
	public void setState(int state)
	{
		fState = state;
	}
	private int fState = CLOSED;
	
	public boolean blocksLineOfSight()
	{
		return (fState != Door.OPEN);
	}
	
	public boolean canOccupy(Item item)
	{
		return (fState == Door.OPEN);
	}
	
	public void draw(Graphics2D g, Rectangle2D rect)
	{
		switch (fState)
		{
		case OPEN:
			g.setColor(Color.WHITE);
			g.fill(rect);
			g.setColor(Color.LIGHT_GRAY);
			g.draw(rect);
			break;
		case CLOSED:
			g.setColor(Color.GRAY);
			g.fill(rect);
			break;
		case LOCKED:
			g.setColor(Color.GRAY);
			g.fill(rect);
			Point2D top_left = new Point2D.Double(rect.getX(), rect.getY());
			Point2D top_right = new Point2D.Double(rect.getX() + rect.getWidth(), rect.getY());
			Point2D bottom_left = new Point2D.Double(rect.getX(), rect.getY() + rect.getHeight());
			Point2D bottom_right = new Point2D.Double(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight());
			Line2D d1 = new Line2D.Double(top_left, bottom_right);
			Line2D d2 = new Line2D.Double(bottom_left, top_right);
			g.setColor(Color.WHITE);
			g.draw(d1);
			g.draw(d2);
			break;
		}
	}

	public void load(Node node)
	{
		super.load(node);
		
		fKeyID = XMLHelper.getIntValue(node, "KeyID");
		fState = XMLHelper.getIntValue(node, "State");
	}

	public void save(Node node)
	{
		super.save(node);
		
		XMLHelper.setIntValue(node, "KeyID", fKeyID);
		XMLHelper.setIntValue(node, "State", fState);
	}
}
