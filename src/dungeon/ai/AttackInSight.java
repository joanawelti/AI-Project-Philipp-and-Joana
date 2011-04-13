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
public class AttackInSight extends BehaviourWithPathfindingAStar
{
	/**
	 * Constructor
	 * 
	 * @param creature The creature
	 */
	public AttackInSight(Creature creature)
	{
		super(creature);
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
		
		// first check if the creature can attack something,
		// pick up something, or open a door
		if (ActionAttack.performAction(fCreature, game))
			return true;
		
		if (ActionPickUp.performAction(fCreature, game))
			return true;
	
		if (ActionDoor.performAction(fCreature, game))
			return true;
		
		// if the creature can't do any of the above, figure out how it will move
		if (move(game))
			return true;
		
		return false;
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
		
		// if the enemy is in view go for him
		if(LineOfSight.exists(fCreature.getLocation(), game.getCreatures().elementAt(0).getLocation(), game.getMap()))
//				fCreature.setGoal(game.getCreatures().elementAt(0).getLocation(), game);
			// System.out.println("attacking");
			return followMovingTarget(game, 0);
		else fCreature.setGoal(null,game);
		
		// if the creature has a goal (perhaps set above), move towards it
		if (fCreature.getGoal() != null)
		{			
			moved = fCreature.moveToGoal(game);
		}
	
		// if the creature hasn't moved towards a goal, make it do nothing
		if (!moved)
		{
//			double theta = fRandom.nextDouble() * Math.PI * 2;
//			moved = fCreature.move(theta, game);
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
