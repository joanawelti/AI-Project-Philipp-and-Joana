package dungeon.ai.actions;

import java.util.Vector;

import dungeon.model.Game;
import dungeon.model.items.mobs.Mob;
import dungeon.model.items.treasure.Key;
import dungeon.model.structure.Door;

/**
 * Helper class to unlock / open doors
 */
public class ActionDoor
{
	/**
	 * Make the given mob unlock or open a door if possible
	 * 
	 * @param mob The mob
	 * @param game The current game object
	 * @return Returns true if the mob acted; false otherwise
	 */
	public static boolean performAction(Mob mob, Game game)
	{
		Vector<Door> nearby_doors = mob.nearbyDoors(game);
		
		// Am I beside a locked door, for which I have a key?
		for (Door door: nearby_doors)
		{
			if (door.getState() == Door.LOCKED)
			{
				// Do I have a key for this door?
				Key key = mob.findKey(door.getKeyID());
				if (key == null)
					continue;
				
				// Unlock the door
				mob.unlockDoor(door, key);
				
				return true;
			}
		}
		
		// Am I beside a closed door?
		for (Door door: nearby_doors)
		{
			if (door.getState() == Door.CLOSED)
			{
				// Open the door
				mob.openDoor(door);
				
				return true;
			}
		}
		
		return false;
	}
}
