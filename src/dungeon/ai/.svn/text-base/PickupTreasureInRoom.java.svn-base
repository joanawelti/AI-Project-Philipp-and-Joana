/*P1Beahviour - solution for CS3516 pract 1
 * Parts 2 and 3 - full solution
 * Part 4 - searches neighbouring room when current room finished,
 *   does not backtrack
 * 
 */
package dungeon.ai;

import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;
import java.util.Random;

import dungeon.App;
import dungeon.ai.actions.ActionPickUp;
import dungeon.ai.pathfind.PathFind;
import dungeon.ai.pathfind.PathFindAStar;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;
import dungeon.model.items.treasure.Treasure;
import dungeon.model.structure.Tile;
import dungeon.ui.MapPanel;

public class PickupTreasureInRoom extends BehaviourWithPathfindingAStar {

	// the creature (Orc) the behaviour is for
	Creature fCreature = null;
	
	// goal tile, whether cleared
	Tile goalTile;
	boolean tileCleared;
	
	// random number generator for random movement
	Random fRandom = new Random();
	
	// class variables for part 4
	Queue<Point2D> goalQueue;  // queue of goals, to be executed in sequence
	
	// Pathfinder
	PathFind pathfind;
	
	/** Constructor
	 * @param creature - creature behaviour is for
	 */
	public PickupTreasureInRoom(Creature creature)
	{
		super(creature);
		fCreature = creature;
		goalQueue = new ArrayDeque<Point2D>();
		goalTile = null;
		tileCleared = true;
		pathfind = new PathFindAStar(creature);
	}

	public boolean onTick(Game game) {
//		setGoalTile(game.getMap().getTileAt(new Point2D.Double(80,80)),game);

		// if goal tile is null, set it to creature's current tile
		if (goalTile == null) {
			goalTile = game.getMap().getTileAt(fCreature.getLocation());
			tileCleared = false;
		}
		
		// pick up treasure if possible
		if (ActionPickUp.performAction(fCreature, game)) {
			return true;
		}

		// else move
		// no doors, no one to attack
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

		// case 1: no current goal, goal queue is not empty;
		// get next goal from queue
		if (fCreature.getGoal() == null && !goalQueue.isEmpty()) {
			fCreature.setGoal(goalQueue.poll(), game);
			App.log(fCreature + " is going to goal at " + fCreature.getGoal());
		}
	
		// case 2: no current goal, goal queue is empty
		if (fCreature.getGoal() == null)
		{
			if (!game.getTreasure().isEmpty()) {  // if there are treasures left
				boolean treasureFound = false;
				Tile myTile = game.getMap().getTileAt(fCreature.getLocation()); // tile I am in
				// look for treasures which I can carry, which are in my tile
				for (Treasure t: game.getTreasure())
					if (fCreature.getEncumbrance() + t.getWeight() <= fCreature.getStrength() &&
							myTile == game.getMap().getTileAt(t.getLocation())) {
						// found suitable treasure; make it my goal
						fCreature.setGoal(t.getLocation(), game);
						App.log(fCreature + " is going to treasure at " + fCreature.getGoal());
						treasureFound = true;
						break;
					}
				if (treasureFound == false) { // no treasure in my tile
					tileCleared = true;
/*					roomsCleared.add(myTile); // room has been cleared of treasure
					// look for adjacent tile which has not yet been cleared
					for (Tile otherTile: game.getMap().getTiles()) {
						if (!roomsCleared.contains(otherTile) && myTile.touches(otherTile)) {
							// found suitable candidate.  In order to move into it, first move to
							// centre of current tile, then move to point where tiles touch,
							// then move to centre of otherTile
							goalQueue.offer(new Point2D.Double(myTile.getArea().getCenterX(), myTile.getArea().getCenterY()));
							goalQueue.offer(myTile.getTouchPoint(otherTile));
							goalQueue.offer(new Point2D.Double(otherTile.getArea().getCenterX(), otherTile.getArea().getCenterY()));
						}
					}
*/				}
			}
		}

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
	

	/**
	 * @return the tilesCleared
	 */
	public boolean isTileCleared() {
		return tileCleared;
	}

	/**
	 * @param goalTile the goalTile to set
	 */
	public void setGoalTile(Tile goalTile, Game game) {
		// if goal tile is same as current one, do nothing
		if (goalTile == this.goalTile)
			return;
		
		// set goalTile
		this.goalTile = goalTile;
		
		// if null, return
		if (goalTile == null)
			return;
		
		// mark tile as not cleared
		tileCleared = false;
		
		// if orc not already in tile, use pathfinder to make a path to centre of goalTile
		Tile myTile = game.getMap().getTileAt(fCreature.getLocation()); // tile I am in
		if (myTile != goalTile) {
			Point2D goalPoint = new Point2D.Double(goalTile.getArea().getCenterX(), goalTile.getArea().getCenterY());
			goalQueue.clear();
			wayPoints=pathfind.findPath(game, goalPoint);
			MapPanel.setPath(wayPoints);
			Collections.reverse(wayPoints);
			goalQueue.addAll(wayPoints);

		}
	}

	/**
	 * @return the goalTile
	 */
	public Tile getGoalTile() {
		return goalTile;
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
