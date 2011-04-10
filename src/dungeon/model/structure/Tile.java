package dungeon.model.structure;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.UUID;

import org.w3c.dom.Node;

import dungeon.model.Game;
import dungeon.model.items.Item;
import dungeon.model.items.mobs.Mob;
import dungeon.utils.Persistent;
import dungeon.utils.XMLHelper;

/**
 * A tile is an area on the map, such as a room or a door
 */
public abstract class Tile implements Persistent
{
	/**
	 * Constructor
	 */
	public Tile()
	{
	}
	
	/**
	 * Constructor
	 * 
	 * @param x The tile's x co-ordinate
	 * @param y The tile's y co-ordinate
	 * @param width The tile's width
	 * @param height The tile's height
	 */
	public Tile(double x, double y, double width, double height)
	{
		fArea = new Rectangle2D.Double(x, y, width, height);
	}
	
	/**
	 * Constructor
	 * 
	 * @param area The map area the tile will occupy
	 */
	public Tile(Rectangle2D area)
	{
		fArea = area;
	}
	
	/**
	 * Draw the tile on the screen
	 * 
	 * @param g The graphics object
	 * @param rect The area to draw in
	 */
	public abstract void draw(Graphics2D g, Rectangle2D rect);

	/**
	 * @return Returns the tile's unique ID
	 */
	public UUID getID()
	{
		return fID;
	}
	protected UUID fID = UUID.randomUUID();
	
	/**
	 * @return Returns the tile's map area
	 */
	public Rectangle2D getArea()
	{
		return fArea;
	}
	/**
	 * Sets the map area occupied by the tile
	 * @param area The area of the map which the tile should occupy
	 */
	public void setArea(Rectangle2D area)
	{
		fArea = area;
	}
	protected Rectangle2D fArea = new Rectangle2D.Double(0, 0, 0, 0);
	
	/**
	 * This method is called on every game tick
	 * 
	 * @param game The current game state
	 */
	public void onTick(Game game)
	{
		// Do nothing by default
	}
	
	/**
	 * This method is called whenever a mob is on this tile
	 * <BR>
	 * This could be overridden to represent poison gas, etc
	 * 
	 * @param mob The mob to affect
	 */
	public void affect(Mob mob)
	{
		// Do nothing by default
	}
	
	/**
	 * @return Returns true if the tile blocks line-of-sight; false otherwise
	 */
	public boolean blocksLineOfSight()
	{
		// By default, tiles do not block line-of-sight
		return false;
	}
	
	/**
	 * Determines whether the given item can occupy this tile
	 * <BR>
	 * This could be overridden to represent rooms where certain mobs cannot enter, etc
	 * 
	 * @param item The item
	 * @return Returns true if the item can occupy the tile; false otherwise
	 */
	public boolean canOccupy(Item item)
	{
		// By default, items can occupy tiles
		return true;
	}
	
	/** check of this tile touches the other tile
	 * @param otherTile
	 * @return Returns true if the tiles touch, false otherwise
	 */
	public boolean touches (Tile otherTile) {
		// check for overlap on left, right
		if (match(getArea().getMaxX(), otherTile.getArea().getMinX()) ||
				match(getArea().getMinX(), otherTile.getArea().getMaxX()))	
			return checkOverlap(getArea().getMinY(), getArea().getMaxY(),
					otherTile.getArea().getMinY(), otherTile.getArea().getMaxY());
		// check for overlap on top, bottom
		if (match(getArea().getMaxY(), otherTile.getArea().getMinY()) ||
				match(getArea().getMinY(), otherTile.getArea().getMaxY()))	
			return checkOverlap(getArea().getMinX(), getArea().getMaxX(),
					otherTile.getArea().getMinX(), otherTile.getArea().getMaxX());
		// no side overlap
		return false;
	}
	
	/** Find the centre point where this tile touches the other time
	 * @param otherTile
	 * @return return touching point if the tiles touch, otherwise null
	 */
	public Point2D getTouchPoint (Tile otherTile)
	{
		if (!touches(otherTile))
			return null;
		Rectangle2D intersects = getArea().createIntersection(otherTile.getArea());
		return new Point2D.Double(intersects.getCenterX(), intersects.getCenterY());
	}

	/** check if two numbers are equal within a small tolerance (0.1)
	 * @param d1
	 * @param d2
	 * @return Returns true if the numbers differ by less than 0.1, false otherwise
	 */
	private boolean match(double d1, double d2)
	{
		return (Math.abs(d1-d2) < 0.1);
	}

	/** check if edges with below coords overlap
	 * @param edge1Min
	 * @param edge1Max
	 * @param edge2Min
	 * @param edge2Max
	 * @return Returns true if the edges overlap, false otherwise
	 */
	private boolean checkOverlap(double edge1Min, double edge1Max, double edge2Min, double edge2Max)
	{
		return (edge1Max > edge2Min) & (edge2Max > edge1Min);
	}
	
	public void load(Node node)
	{
		double x = XMLHelper.getDblValue(node, "X");
		double y = XMLHelper.getDblValue(node, "Y");
		double width = XMLHelper.getDblValue(node, "Width");
		double height = XMLHelper.getDblValue(node, "Height");
		
		fArea = new Rectangle2D.Double(x, y, width, height);
	}

	public void save(Node node)
	{
		XMLHelper.setDblValue(node, "X", fArea.getX());
		XMLHelper.setDblValue(node, "Y", fArea.getY());
		XMLHelper.setDblValue(node, "Width", fArea.getWidth());
		XMLHelper.setDblValue(node, "Height", fArea.getHeight());
	}
}
