package dungeon.ai;

import java.awt.geom.Point2D;

import dungeon.model.structure.Map;
import dungeon.model.structure.Tile;

/**
 * Helper class to determine whether two points have line-of-sight to each other
 */
public class LineOfSight
{
	/**
	 * Determines whether line-of-sight exists between two points
	 * 
	 * @param pt_a The first point
	 * @param pt_b The second point
	 * @param map The current map
	 * @return Returns true if line-of-sight exists; false otherwise
	 */
	public static boolean exists(Point2D pt_a, Point2D pt_b, Map map)
	{
		double distance = pt_a.distance(pt_b);
		
		int steps = (int)(distance * 5);
		
		double dx = (pt_b.getX() - pt_a.getX()) / steps;
		double dy = (pt_b.getY() - pt_a.getY()) / steps;
		
		for (int n = 1; n != steps; ++n)
		{
			double x = (n * dx) + pt_a.getX();
			double y = (n * dy) + pt_a.getY();
			
			Point2D pt = new Point2D.Double(x, y);
			
			Tile tile = map.getTileAt(pt);
			if (tile == null)
				return false;
			
			// Check if tile blocks LOS
			if (tile.blocksLineOfSight())
				return false;
			
			// We don't currently check whether we're looking through mobs or treasure
		}
		
		return true;
	}
}
