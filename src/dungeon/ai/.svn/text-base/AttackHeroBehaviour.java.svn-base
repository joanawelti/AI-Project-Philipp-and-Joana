package dungeon.ai;

import java.awt.geom.Point2D;
import java.util.Random;
import java.util.UUID;

import dungeon.ai.actions.ActionAttack;
import dungeon.ai.actions.ActionDoor;
import dungeon.ai.actions.ActionPickUp;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;

/**
 * Class providing default behaviour for Creature mobs
 */
public class AttackHeroBehaviour implements Behaviour
{
	/**
	 * Constructor
	 * 
	 * @param creature The creature
	 */
	public AttackHeroBehaviour(Creature creature)
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
	 * <LI>Creature will attack a hero in the same room.</LI>
	 * <LI>If the hero is too far away the creature will move towards him </LI>
	 * </UL>
	 * @param game The current game state
	 * @return Returns true if the creature moved; false otherwise
	 */
	boolean move(Game game)
	{
		if(isHeroInReach(game)){
			//perform attack
			fCreature.setGoal(null, game);
		}else{
			if(isHeroInRoom(game)){
				fCreature.setGoal(game.getHero().getLocation(), game);
			}else{
				fCreature.setGoal(null, game);
			}
		}
		return fCreature.moveToGoal(game);
	}
	
	private boolean isHeroInReach(Game game){
		Point2D heroPos = game.getHero().getLocation();
		Point2D orkPos = fCreature.getLocation();
		double orkReach = fCreature.getReach();
		double distance = heroPos.distance(orkPos);
		return orkReach >= distance;
	}
	
	private boolean isHeroInRoom(Game game){
		UUID heroRoom = game.getMap().getTileAt(game.getHero().getLocation()).getID();
		UUID orkRoom = game.getMap().getTileAt(fCreature.getLocation()).getID();
		return orkRoom.equals(heroRoom);
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
