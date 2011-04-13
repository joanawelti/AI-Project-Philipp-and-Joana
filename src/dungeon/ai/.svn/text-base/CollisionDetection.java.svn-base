package dungeon.ai;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import dungeon.model.Game;
import dungeon.model.items.Item;
import dungeon.model.items.mobs.Mob;
import dungeon.model.structure.Tile;

/**
 * Helper class to determine whether an Item can occupy a given location on the map
 */
public class CollisionDetection
{
	/**
	 * Determines whether the given item can occupy the given point
	 * 
	 * @param game The current game object
	 * @param item The item
	 * @param location The location to check
	 * @return Returns true if the item can occupy the location; false otherwise
	 */
	public static boolean canOccupy(Game game, Item item, Point2D location)
	{
		Vector<Point2D> points = new Vector<Point2D>();
		
		int point_count = (int)(item.getSize() * 8);
		double da = 2 * Math.PI / point_count;
		
		for (int n = 0; n != point_count; ++n)
		{
			double theta = da * n;
			
			double x = item.getSize() * Math.sin(theta);
			double y = item.getSize() * Math.cos(theta);
			
			Point2D pt = new Point2D.Double(location.getX() + x, location.getY() + y);
			points.add(pt);
		}
		
		// Make sure each of these points is on a tile
		for (Point2D pt: points)
		{
			Tile tile = game.getMap().getTileAt(pt);
			if (tile == null)
				return false;
			
			if (!tile.canOccupy(item))
				return false;
		}

		if (item instanceof Mob)
		{
			// Make sure we're not trying to walk over another mob
			Vector<Mob> mobs = new Vector<Mob>();
			mobs.addAll(game.getCreatures());
			
			if (game.getHero() != null)
				mobs.add(game.getHero());
			
			Rectangle2D item_rect = Item.getShape(location, item.getSize());

			for (Mob mob: mobs)
			{
				if (item == mob)
					continue;
				
				if (item_rect.intersects(mob.getShape()))
					return false;
			}
		}
		
		return true;
	}
}
