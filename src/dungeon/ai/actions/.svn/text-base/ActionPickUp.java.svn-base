package dungeon.ai.actions;

import java.util.Vector;

import dungeon.model.Game;
import dungeon.model.items.mobs.Mob;
import dungeon.model.items.treasure.Treasure;

/**
 * Helper class for picking up treasure
 */
public class ActionPickUp
{
	/**
	 * Make the given mob pick up nearby treasure if possible
	 * 
	 * @param mob The mob
	 * @param game The current game object
	 * @return Returns true if the mob picked up treasure; false otherwise
	 */
	public static boolean performAction(Mob mob, Game game)
	{
		// Am I beside something I want to pick up?
		Vector<Treasure> nearby_items = mob.nearbyTreasure(game);
		for (Treasure treasure: nearby_items)
		{
			// Can I carry it?
			double weight = treasure.getWeight() + mob.getEncumbrance();
			if (weight > mob.getStrength())
				continue;
			
			// Pick it up
			return mob.pickUp(game, treasure);
		}
		
		return false;
	}
}
