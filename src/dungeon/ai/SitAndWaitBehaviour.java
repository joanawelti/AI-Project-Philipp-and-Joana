package dungeon.ai;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import dungeon.ai.actions.ActionAttack;
import dungeon.ai.actions.ActionDoor;
import dungeon.ai.actions.ActionPickUp;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;
import dungeon.model.structure.Tile;

/**
 * Class providing default behaviour for Creature mobs
 */
public class SitAndWaitBehaviour implements Behaviour
{
	/**
	 * Constructor
	 * 
	 * @param creature The creature
	 */
	public SitAndWaitBehaviour(Creature creature)
	{
		fCreature = creature;
	}
	
	Creature fCreature = null;
	Random fRandom = new Random();
	
	// This specifies whether the creature will stay in the room it starts in
	static final boolean KEEP_TO_ROOMS = false;
	
	/* (non-Javadoc)
	 * @see dungeon.ai.Behaviour#onTick(dungeon.model.Game)
	 */
	public boolean onTick(Game game)
	{
		// default routine to move the character on a clock tick.
		if (fCreature.getCurrentHealth() > 0) {
		
		// first check if the creature can attack something,
		// pick up something, or open a door
		if (ActionAttack.performAction(fCreature, game))
			return true;
		
		if (ActionPickUp.performAction(fCreature, game))
			return true;
	
		if (ActionDoor.performAction(fCreature, game))
			return true;
		
	
		
		return false;
		
		} else // dead
		{
			// negative reward for learner

			return false;

		}
	}
	
	/**
	 * Make the creature move, dictated by its goal
	 * <BR>
	 * <UL>
	 * <LI>If the creature has no goal, it will set one</LI>
	 * <LI>The creature will then attempt to walk towards its goal</LI>
	 * <LI>If it cannot, it will attempt to take a step in a random direction</LI>
	 * </UL>
	 * @param game The current game state
	 * @return Returns true if the creature moved; false otherwise
	 */
	boolean move(Game game)
	{
		// creature has not moved
		boolean moved = false;
		
		// does the creature have a goal?  If not, set one
		if (fCreature.getGoal() == null)
		{
			Rectangle2D bounds = null; // where to look for new goal
			
			// if KEEP_TO_ROOMS is set, look for a goal point in the same tile (room)
			if (SitAndWaitBehaviour.KEEP_TO_ROOMS)
			{
				Tile tile = game.getMap().getTileAt(fCreature.getLocation());
				bounds = tile.getArea();
			}
			// otherwise, look for a goal point anywhere on map
			else
			{
				bounds = game.getMap().getBounds(0);
			}
			
			// pick random goal point within bounds
			double x = bounds.getX() + (bounds.getWidth() * fRandom.nextDouble());
			double y = bounds.getY() + (bounds.getHeight() * fRandom.nextDouble());
			Point2D goal_pt = new Point2D.Double(x, y);
			
			// check that this point is within a room. not occupied by another creature, etc
			if (CollisionDetection.canOccupy(game, fCreature, goal_pt))
				// all conditions passed, set a new goal
				fCreature.setGoal(goal_pt, game);
		}
		
		// if the creature has a goal (perhaps set above), move towards it
		if (fCreature.getGoal() != null)
		{			
			moved = fCreature.moveToGoal(game);
		}
	
		// if the creature hasn't moved towards a goal, make it move randomly
		if (!moved)
		{
			double theta = fRandom.nextDouble() * Math.PI * 2;
			moved = fCreature.move(theta, game);
		}
		
		return moved;
	}

	public boolean deathTick(Game game) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean gameOverTick(Game game) {
		// TODO Auto-generated method stub
		return false;
	}
}
