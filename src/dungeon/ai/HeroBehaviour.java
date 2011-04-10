package dungeon.ai;

import dungeon.ai.actions.ActionAttack;
import dungeon.ai.actions.ActionDoor;
import dungeon.ai.actions.ActionPickUp;
import dungeon.model.Game;
import dungeon.model.items.mobs.Hero;
import dungeon.model.items.mobs.Movement;

/**
 * Class providing default behaviour for the Hero
 */
public class HeroBehaviour implements Behaviour
{
	/**
	 * Constructor
	 * 
	 * @param hero The hero
	 */
	public HeroBehaviour(Hero hero)
	{
		fHero = hero;
		
		fAutomaticPickUp = true;
		fAutomaticDoors = true;
	}
	
	Hero fHero = null;
	
	// These flags are so we can quickly change the default hero behaviour
	boolean fAutomaticPickUp = true;
	boolean fAutomaticDoors = true;
		
	public boolean onTick(Game game)
	{
		// Handle keyboard movement
		Hero hero = (Hero)fHero;
		if (move(game, hero.getMovement()))
			return true;
		
		if (ActionAttack.performAction(fHero, game))
			return true;
		
		if (fAutomaticPickUp)
		{
			if (ActionPickUp.performAction(fHero, game))
				return true;
		}
		
		if (fAutomaticDoors)
		{
			if (ActionDoor.performAction(fHero, game))
				return true;
		}
		
		if (move(game))
			return true;
		
		return false;
	}
	
	/**
	 * Make the hero walk in accordance with the user's keyboard input
	 * 
	 * @param game The current game state
	 * @param movement The movement request
	 * @return Returns true if the hero moved; false otherwise
	 */
	boolean move(Game game, Movement movement)
	{
		double theta = movement.getAngle();
		return fHero.move(theta, game);
	}
	
	/**
	 * Make the hero walk in a straight line towards its goal
	 * 
	 * @param game The current game state
	 * @return Returns true if the hero moved; false otherwise
	 */
	boolean move(Game game)
	{
		if (fHero.getGoal() != null)
		{
			// Try to move towards the goal

			double dx = fHero.getGoal().getX() - fHero.getLocation().getX();
			double dy = fHero.getGoal().getY() - fHero.getLocation().getY();
			double theta = Math.atan2(dx, dy);
			
			boolean moved = fHero.move(theta, game);
			if (moved)
			{
				// Are we there yet?
				double distance = fHero.getLocation().distance(fHero.getGoal());
				if (distance < fHero.getSpeed())
					fHero.setGoal(null, null);
			}
			
			return moved;
		}
		
		return false;
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
