package dungeon.model.items;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.UUID;

import org.w3c.dom.Node;

import dungeon.ai.CollisionDetection;
import dungeon.model.Game;
import dungeon.utils.Persistent;
import dungeon.utils.XMLHelper;

/**
 * An item in the dungeon
 * <BR>
 * Derived classes are:
 * <UL>
 * <LI><B>Mob</B> (for mobile items such as creatures)</LI>
 * <LI><B>Treasure</B> (for collectable treasure items)</LI>
 * </UL>
 */
public abstract class Item implements Persistent
{
	/**
	 * @return Returns the name of the item
	 */
	public abstract String getName();
	
	/**
	 * @return Returns the size (radius) of the item
	 */
	public abstract double getSize();
	
	/**
	 * Draws the item to the screen
	 * 
	 * @param g The graphics object
	 * @param rect The area the item occupies
	 * @param highlight Whether the item should be highlighted
	 */
	public abstract void draw(Graphics2D g, Rectangle2D rect, boolean highlight);
	
	/**
	 * This method is called on every game tick
	 * <BR>
	 * Treasure items usually ignore this, but mobs use it to interact with the world
	 * 
	 * @param game The current game state
	 */
	public abstract void onTick(Game game);
	public abstract void gameOverTick(Game game);
	public abstract void deathTick(Game game);
	
	/**
	 * @return Returns the item's unique ID
	 */
	public UUID getID()
	{
		return fID;
	}
	protected UUID fID = UUID.randomUUID();
	
	/**
	 * @return Returns the item's location on the map
	 */
	public Point2D getLocation()
	{
		return fLocation;
	}
	protected Point2D fLocation = new Point2D.Double(0, 0);
	
	/**
	 * @return Returns the area occupied by the item
	 */
	public Rectangle2D getShape()
	{
		return Item.getShape(fLocation, getSize());
	}
	
	/**
	 * Helper method to determine the area occupied by an item
	 * 
	 * @param location The location of the item
	 * @param radius The radius of the item
	 * @return Returns the area occupied by the item
	 */
	public static Rectangle2D getShape(Point2D location, double radius)
	{
		double x = location.getX();
		double y = location.getY();
		
		return new Rectangle2D.Double(x - radius, y - radius, radius * 2, radius * 2);
	}
	
	/**
	 * Attempt to place the item at a given location on the map
	 * <BR>
	 * <B>Note</B>: This method checks for collisions
	 * 
	 * @param location The map location
	 * @param game The game state
	 * @return Returns true if the item was placed on the map; false otherwise
	 */
	public boolean placeAt(Point2D location, Game game)
	{
		if (CollisionDetection.canOccupy(game, this, location))
		{
			fLocation = location;
			return true;
		}
		
		return false;
	}

	public String toString()
	{
		return getName();
	}

	public void load(Node node)
	{
		double x = XMLHelper.getDblValue(node, "X");
		double y = XMLHelper.getDblValue(node, "Y");
		
		fLocation = new Point2D.Double(x, y);
	}

	public void save(Node node)
	{
		XMLHelper.setDblValue(node, "X", fLocation.getX());
		XMLHelper.setDblValue(node, "Y", fLocation.getY());
	}
}
