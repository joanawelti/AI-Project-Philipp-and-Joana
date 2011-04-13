package dungeon.ai;

import dungeon.model.Game;

/**
 * Interface which controls mob behaviour
 * <BR>
 * <BR>
 * Derived classes are <B>HeroBehaviour</B> (for Hero mobs) and
 * <B>DefaultBehaviour</B> (for Creature mobs)
 */
public interface Behaviour
{
	/**
	 * This method is called on every game tick to prompt the mob to act.
	 * 
	 * @param game The current game object
	 * @return Returns true if the mob has acted; false otherwise.
	 */
	public boolean onTick(Game game);
	/**
	 * This method is called (only for the winning team) on gameOver 
	 * to give the winner a chance to update learning tables.
	 * 
	 * @param game The current game object
	 * @return Returns true if the mob has acted; false otherwise.
	 */
	public boolean gameOverTick(Game game);
	/**
	 * Bummer.
	 * You just died:(
	 * Any final words or last thoughts?
	 * 
	 * @param game The current game object
	 * @return Returns true if the mob has acted; false otherwise.
	 */
	public boolean deathTick(Game game);
}
