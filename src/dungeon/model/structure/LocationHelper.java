package dungeon.model.structure;

import java.awt.geom.Point2D;

import dungeon.model.Game;
import dungeon.model.items.treasure.Potion;
import dungeon.model.items.treasure.Treasure;

public class LocationHelper {

	/**
	 * Returns the point in the center of the finish Tile if such a tile exists
	 * 
	 * @return point in center of Finish or null if no center
	 */
	public static Point2D getFinishCenter(Map map) {
		Point2D center = null;
		for (Tile t : map.getTiles()) {
			if (t instanceof Finish) {
				center = new Point2D.Double(t.getArea().getCenterX(), t.getArea().getCenterY());
			}
		} 
		return center;
	}
	
	/**
	 * Returns finish Tile if a Finish exists, otherwise returns null
	 * @return Finish tile or null
	 */
	public static Finish getFinish(Map map) {
		Finish finish = null;
		for (Tile t : map.getTiles()) {
			if (t instanceof Finish) {
				finish = (Finish) t;
			}
		} 
		return finish;
	}
	

	/**
	 * Finds location of closest health potion
	 * @param game Game where potion should be found
	 * @param position Potion closest to this position
	 * @return Potion closest Potion
	 */
	public static Point2D getClosestHealthPotion(Game game, Point2D position) {
		return getClosestPotion(game, position, Potion.POTION_HEALTH);
	}
	
	/**
	 * Finds location of closest energy potion
	 * @param game Game where potion should be found
	 * @param position Potion closest to this position
	 * @return Potion closest Potion
	 */
	public static Point2D getClosestEnergyPotion(Game game, Point2D position) {
		return getClosestPotion(game, position, Potion.POTION_ENERGY);		
	}
	
	/**
	 * Finds location of potion of a given type closest to a position
	 * @param game Game where potion should be found
	 * @param position Potion closest to this position
	 * @param type Type of potion
	 * @return Potion closest Potion
	 */
	private static Point2D getClosestPotion(Game game, Point2D position, int type) {
		double maxDistance = Double.MAX_VALUE;
		Point2D closestPotion = null;
		for (Treasure t : game.getTreasure()) {
			if (t instanceof Potion) {
				if (((Potion) t).getType() == type && position.distance(t.getLocation()) < maxDistance) {
					maxDistance = position.distance(t.getLocation());
					closestPotion = t.getLocation();
				}
			}
		}
		return closestPotion;
	}
	
}
