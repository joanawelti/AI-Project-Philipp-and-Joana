package dungeon.ai.BowStateMachine;

import java.awt.geom.Point2D;
import java.util.UUID;

import dungeon.ai.Behaviour;
import dungeon.ai.actions.ActionAttack;
import dungeon.ai.actions.ActionDoor;
import dungeon.ai.actions.ActionPickUp;
import dungeon.collections.AttackList;
import dungeon.model.Game;
import dungeon.model.combat.Attack;
import dungeon.model.items.mobs.Creature;

public abstract class State implements Behaviour {
	
	/**
	 * Constructor
	 * 
	 * @param creature The creature
	 */
	public State(Creature creature)
	{
		fCreature = creature;
	}
	
	Creature fCreature = null;
	
	
	/* (non-Javadoc)
	 * @see dungeon.ai.Behaviour#onTick(dungeon.model.Game)
	 */
	final public boolean onTick(Game game)
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
	
	abstract boolean move(Game game);
	
	protected boolean isHeroInReach(Game game){
		Point2D heroPos = game.getHero().getLocation();
		Point2D orkPos = fCreature.getLocation();
		double orkReach = fCreature.getReach();
		double distance = heroPos.distance(orkPos);
		return orkReach >= distance;
	}
	
	protected boolean isHeroInRoom(Game game){
		UUID heroRoom = game.getMap().getTileAt(game.getHero().getLocation()).getID();
		UUID orkRoom = game.getMap().getTileAt(fCreature.getLocation()).getID();
		return orkRoom.equals(heroRoom);
	}
	
	protected Attack getMaxRangeAttack(Game game){
		AttackList attacks = fCreature.getAttacks();
		Attack maxRange = null;
		
		for(Attack attack : attacks){
			if(maxRange==null || attack.getMaxRange()>maxRange.getMaxRange()){
				maxRange = attack;
			}
		}
		return maxRange;
	}
	
	protected boolean isEnergySufficient(Game game){
		int cost = getMaxRangeAttack(game).getEnergyCost();
		int energy = fCreature.getCurrentEnergy();
		return energy >= cost;
	}
}
