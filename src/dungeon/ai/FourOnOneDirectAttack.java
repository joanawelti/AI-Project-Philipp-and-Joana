package dungeon.ai;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;

import dungeon.ai.actions.ActionAttack;
import dungeon.ai.actions.ActionDoor;
import dungeon.ai.actions.ActionPickUp;
import dungeon.ai.pathfind.PathFind;
import dungeon.ai.pathfind.PathFindAStar;
import dungeon.ai.ai_code.TeamState;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;
import dungeon.ui.MapPanel;

public class FourOnOneDirectAttack implements Behaviour {

	Creature fCreature = null;
	Random fRandom = new Random();
	PathFind followAction = null;
	List<Point2D> wayPoints = null;
	Point2D targetPosition = null;
	int counter = 0;
	int creatureindex = 0;


	
	public FourOnOneDirectAttack(Creature creature) {
		fCreature = creature;
		followAction = new PathFindAStar(creature);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dungeon.ai.Behaviour#onTick(dungeon.model.Game)
	 */
	public boolean onTick(Game game) {

			// default routine to move the character on a clock tick.

			// first check if the creature can attack something,
			// pick up something, or open a door
			if (ActionAttack.performAction(fCreature, game))
				return true;

//			if (ActionPickUp.performAction(fCreature, game))
//				return true;

			if (ActionDoor.performAction(fCreature, game))
				return true;


			// if the creature can't do any of the above, figure out how it will
			// move
			if (move(game))
				return true;

			return false;
	}

	private boolean move(Game game) {
		boolean successfulmove = true;


			successfulmove = attack(game);

		if (!successfulmove) {
			// we are stuck
			// need to pathfind with mobs
			wayPoints = null;
			followAction.recalcMapWithMobs(game);

				successfulmove = attack(game);

			followAction.recalcMapWithoutMobs(game);
		}
		return true;
	}

	private boolean attack(Game game) {
		// hardcoded to go for the first creature
		creatureindex = 0;

		if (targetPosition == null) {
			targetPosition = game.getCreatures().elementAt(creatureindex)
					.getLocation();
		}

		// if he's moved too far, or we had no path anyway
		if (targetPosition.distance(game.getCreatures()
				.elementAt(creatureindex).getLocation()) > 4
				|| wayPoints == null) {
			wayPoints = followAction.findPath(game, game.getCreatures()
					.elementAt(creatureindex).getLocation());
			targetPosition = game.getCreatures().elementAt(creatureindex)
					.getLocation();
			fCreature.setGoal(null, game);
		}

		// put this in to get over problem of null
		if (wayPoints == null)
			return false;

		if (fCreature.getGoal() == null && wayPoints.size() > 0) {
			Point2D goal = followAction.findBestWayPoint(game, wayPoints);
			fCreature.setGoal(goal, game);
		}

		if (fCreature == game.getCreatures().elementAt(0))
			MapPanel.setPath(wayPoints);
		return fCreature.moveToGoal(game);// false means we are stuck, can't get
											// to goal
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
