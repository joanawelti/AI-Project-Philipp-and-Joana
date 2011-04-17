package dungeon.ai;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.rmi.server.UID;
import java.util.List;
import java.util.UUID;

import dungeon.ai.pathfind.PathFind;
import dungeon.ai.pathfind.PathFindAStar;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;
import dungeon.model.structure.Tile;
import dungeon.ui.MapPanel;

public abstract class BehaviourWithPathfindingAStar implements Behaviour {
	protected Point2D targetPosition = null;
	PathFind followAction = null;
	protected List<Point2D> wayPoints = null;
	Creature fCreature = null;

	public BehaviourWithPathfindingAStar(Creature creature){
	fCreature = creature;
	followAction = new PathFindAStar(creature);}
	
	public boolean followMovingTarget(Game game, int creatureindex) {
		boolean successfulmove = followMovingTargetAttempt(game, creatureindex);
		if (!successfulmove) {
			// we are stuck
			// need to pathfind with mobs
			wayPoints = null;
			followAction.recalcMapWithMobs(game);
			successfulmove = followMovingTargetAttempt(game, creatureindex);
			followAction.recalcMapWithoutMobs(game);
		}
		return successfulmove;
	}
	
	public boolean followTarget(Game game, Point2D position) {
		boolean successfulmove = followTargetAttempt(game, position);
		if (!successfulmove) {
			// we are stuck
			// need to pathfind with mobs
			wayPoints = null;
			followAction.recalcMapWithMobs(game);
			successfulmove = followTargetAttempt(game, position);
			followAction.recalcMapWithoutMobs(game);
		}
		return successfulmove;
	}

	private boolean followMovingTargetAttempt(Game game, int creatureindex) {
		Point2D position = null;
		if(creatureindex>-1)
			position  = game.getCreatures().elementAt(creatureindex)
					.getLocation();
			else
				position  = game.getHero().getLocation();
		
		return followTargetAttempt(game, position);

		
	}
	
	private boolean followTargetAttempt(Game game, Point2D position) {
		if (position == null) {
			return false;
		}

		if (targetPosition == null) {
			targetPosition = position;
		}

		// if he's moved too far, or we had no path anyway
		if (targetPosition.distance(position) > 4
				|| wayPoints == null) {
			wayPoints = followAction.findPath(game, position );
			targetPosition = position;
			fCreature.setGoal(null, game);
		}

		// put this in to get over problem of null
		if (wayPoints == null)
			return false;

		if (fCreature.getGoal() == null && wayPoints.size() > 0) {
			Point2D goal = followAction.findBestWayPoint(game, wayPoints);
			fCreature.setGoal(goal, game);
		}

		// if (fCreature == game.getCreatures().elementAt(3))
		MapPanel.setPath(wayPoints);
		return fCreature.moveToGoal(game);
	}
	
	public boolean evadeMovingTarget(Game game, int creatureindex) {
		Point2D position = null;
		if(creatureindex>-1)
			position  = game.getCreatures().elementAt(creatureindex)
					.getLocation();
			else
				position  = game.getHero().getLocation();
		return evadeTargetAttempt(game, position);
	}
	
	/**
	 * 
	 * @param game
	 * @param creatureID
	 * @param currentTile
	 * @return
	 */
	public boolean evadeAllTargetsInRoom(Game game, UUID creatureID, Tile currentTile) {
		Point2D position = null;
		double x = 0;
		double y = 0;
		double x_alt = 0;
		double y_alt = 0;
		int count = 0;
		int alt_count = 0;
		// get all positions of creatures in currentTile
		for (Creature creature : game.getCreatures()) {
			if (creature.getID() != creatureID) {
				if (game.getMap().getTileAt(creature.getLocation()) == currentTile) {
					x += creature.getLocation().getX();
					y += creature.getLocation().getY();
					count += 1;
				} else {
					// run away from average of all other creatures
					x_alt += creature.getLocation().getX();
					y_alt += creature.getLocation().getY();
					alt_count += 1;
				}
			}	
		}
		if (count > 0) {
			position = new Point2D.Double(x/count, y/count);
		} else if (alt_count > 0) {
			position = new Point2D.Double(x_alt/alt_count, y_alt/alt_count);
		}
		
		return evadeTargetAttempt(game, position);
	}
	
	public boolean evadeTargetAttempt(Game game, Point2D position) {
		if (position == null) {
			return false;
		}
		
		if (targetPosition == null) {
			targetPosition = position;
		}

		if (targetPosition.distance(position) > 4
				|| wayPoints == null) {
			
			wayPoints = followAction.evadeHazard(game, position);
			targetPosition = position;
			fCreature.setGoal(null, game);
		} 
		
		if(wayPoints == null){
			return fCreature.moveToGoal(null);
		}
		
		
		if (fCreature.getGoal() == null && wayPoints.size() > 0) {
			Point2D goal = followAction.findBestWayPoint(game,wayPoints);
			fCreature.setGoal(goal, game);
//		App.log("Goal: " + fCreature.getLocation());
		}
		
		MapPanel.setPath(wayPoints);
		return fCreature.moveToGoal(game);
		
	}
}

