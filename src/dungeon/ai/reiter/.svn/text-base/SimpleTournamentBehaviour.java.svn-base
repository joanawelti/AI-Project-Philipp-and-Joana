/* SimpleTournamentBehaviour - simple behaviour class for tournament
 * Based on solution to practical 1.
 * If there is an enemy in the room, the orc will attack it,
 * Otherwise it will move to a neighbouring room
 * 
 * Ehud Reiter Mar-2010
 */
package dungeon.ai.reiter;

import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import dungeon.App;
import dungeon.ai.Behaviour;
import dungeon.ai.actions.ActionAttack;
import dungeon.ai.actions.ActionDoor;
import dungeon.ai.actions.ActionPickUp;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;
import dungeon.model.items.treasure.Potion;
import dungeon.model.items.treasure.Treasure;
import dungeon.model.structure.Tile;

public class SimpleTournamentBehaviour implements Behaviour {

	// the creature (Orc) the behaviour is for
	Creature fCreature = null;
	
	// class variables for part 4
	List<Tile> roomsVisited;   // rooms which have been visited in this round
	Queue<Point2D> goalQueue;  // queue of goals, to be executed in sequence
	
	// random number generator for random movement
	Random fRandom = new Random();
	
	/** Constructor
	 * @param creature - creature behaviour is for
	 */
	public SimpleTournamentBehaviour(Creature creature)
	{
		fCreature = creature;
		roomsVisited = new ArrayList<Tile>();;
		goalQueue = new ArrayDeque<Point2D>();
	}

	public boolean onTick(Game game) {

		
		// first check if the creature can attack something,
		// pick up something, or open a door
		if (ActionAttack.performAction(fCreature, game))
			return true;
		
		if (ActionPickUp.performAction(fCreature, game))
			return true;
	
		if (ActionDoor.performAction(fCreature, game))
			return true;
		
		// take health potion if this helps
		Potion potion = getHealthPotion();
		if (potion != null && fCreature.getCurrentHealth() < fCreature.getMaxHealth()) {
			fCreature.consume(potion);
			return true;
		}

		// else move
		if (move(game))
			return true;
		
		return false;
	}

	/**
	 * Make the creature move
	 * @param game The current game state
	 * @return Returns true if the creature moved; false otherwise
	 */
	boolean move(Game game)
	{
		// flag saying whether creature has moved
		boolean moved = false;
		
		// case 1: enemy in same room as me; if so, attack
		// if several enemies, go for first one in creature list
		Creature enemy = findEnemy(game);
		if (enemy != null) {
			// clear goal queue until enemy is dead
			goalQueue.clear();
			// set goal: move towards enemy
			fCreature.setGoal(enemy.getLocation(), game);
		}

		// case 2: no current goal, goal queue is not empty;
		// get next goal from queue
		if (fCreature.getGoal() == null && !goalQueue.isEmpty()) {
			fCreature.setGoal(goalQueue.poll(), game);
//			App.log(fCreature + " is going to goal at " + fCreature.getGoal());
		}
	
		// case 3: no current goal, goal queue is empty; move to next room
		if (fCreature.getGoal() == null)
		{
			Tile myTile = game.getMap().getTileAt(fCreature.getLocation()); 
			roomsVisited.add(myTile); // room has been cleared of treasure
			// look for adjacent tile which has not yet been visited in this round
			for (Tile otherTile: game.getMap().getTiles()) {
				if (!roomsVisited.contains(otherTile) && myTile.touches(otherTile)) {
					// found suitable candidate.  In order to move into it, first move to
					// centre of current tile, then move to point where tiles touch,
					// then move to centre of otherTile
					goalQueue.offer(new Point2D.Double(myTile.getArea().getCenterX(), myTile.getArea().getCenterY()));
					goalQueue.offer(myTile.getTouchPoint(otherTile));
					goalQueue.offer(new Point2D.Double(otherTile.getArea().getCenterX(), otherTile.getArea().getCenterY()));
					break; // exit for loop
				}
			}
		}
		
		// case 4: still no goal set, need to start new round; clear rooms visited
		if (fCreature.getGoal() == null && goalQueue.isEmpty())
			roomsVisited.clear();
		

		// goal exists - move to it
		if (fCreature.getGoal() != null)
		{
			moved = fCreature.moveToGoal(game);
		}
		
		// if the creature hasn't moved towards a goal, make it move randomly
		// helps resolve jams
		if (!moved)
		{
			double theta = fRandom.nextDouble() * Math.PI * 2;
			moved = fCreature.move(theta, game);
		}


		return moved;
	}

	/** find an enemy in the same room as me.
	 * Return null if no enemy
	 * 
	 * @param game
	 * @return enemy creature, or null
	 */
	private Creature findEnemy(Game game) {
		Tile myTile = game.getMap().getTileAt(fCreature.getLocation()); 
		for (Creature c: game.getCreatures()) {
			if (c.getCurrentHealth() > 0 &&
					!c.getFaction().equals(fCreature.getFaction()) &&
					myTile == game.getMap().getTileAt(c.getLocation()))
				return c;
		}
		
		// no creature found that meets criteria
		return null;
	}

	
	/** get first health potion in inventory
	 * return null if no health potion
	 * @return
	 */
	private Potion getHealthPotion() {
		for (Treasure treasure: fCreature.getInventory())
			if (treasure instanceof Potion) {
				Potion potion = (Potion)treasure;
				if (potion.getType() == Potion.POTION_HEALTH)
					return potion;
			}
		return null;
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
