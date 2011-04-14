package dungeon.model.structure;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;

import org.w3c.dom.Node;

import dungeon.collections.TileList;
import dungeon.utils.Persistent;
import dungeon.utils.XMLHelper;

/**
 * Class containing the list of tiles which make up the dungeon
 */
public class Map implements Persistent
{
	/**
	 * @return Returns the list of dungeon tiles
	 */
	public TileList getTiles()
	{
		return fTiles;
	}
	private TileList fTiles = new TileList();
	
	
	
	/**
	 * Returns the bounds for the entire dungeon, including an optional border
	 * 
	 * @param border_size The size of the border to add
	 * @return Returns the dungeon's bounding rectangle
	 */
	public Rectangle2D getBounds(int border_size)
	{
		Rectangle2D rect = null;
		
		for (Tile t: fTiles)
		{
			if (rect == null)
			{
				Rectangle2D tile = t.getArea();
				rect = new Rectangle2D.Double(tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight());
			}
			else
			{
				rect.add(t.getArea());
			}
		}
		
		return new Rectangle2D.Double(rect.getX() - border_size, rect.getY() - border_size, rect.getWidth() + (border_size * 2), rect.getHeight() + (border_size * 2));
	}
	
	/**
	 * Finds the map tile at a certain point
	 * 
	 * @param point The point to check
	 * @return Returns the uppermost tile at the given point, or null if there is no tile there
	 */
	public Tile getTileAt(Point2D point)
	{
		if (point == null)
			return null;
		
		Tile top_tile = null;
		
		for (Tile tile: fTiles)
		{
			if (tile.getArea().contains(point))
				top_tile = tile;
		}
		
		return top_tile;
	}

	public void load(Node node)
	{
		XMLHelper.loadObject(node, "Tiles", fTiles);
	}

	public void save(Node node)
	{
		XMLHelper.saveObject(node, "Tiles", fTiles);
	}
}
