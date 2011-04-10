package dungeon.ai;

import java.awt.geom.Point2D;
import java.util.List;

import dungeon.ai.pathfind.PathFind;
import dungeon.ai.pathfind.PathFindAStar;
import dungeon.ai.pathfind.PathFindDepth;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;
import dungeon.ui.MapPanel;

public abstract class BehaviourWithPathfindingAStar implements Behaviour {
	Point2D targetPosition = null;
	Point2D newTargetPosition = null;
	PathFind followAction = null;
	List<Point2D> wayPoints = null;
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

	private boolean followMovingTargetAttempt(Game game, int creatureindex) {

		if(creatureindex>-1)
			newTargetPosition  = game.getCreatures().elementAt(creatureindex)
					.getLocation();
			else
				newTargetPosition  = game.getHero().getLocation();

		if (targetPosition == null) {
			targetPosition =newTargetPosition;
		}

		// if he's moved too far, or we had no path anyway
		if (targetPosition.distance(newTargetPosition) > 4
				|| wayPoints == null) {
			wayPoints = followAction.findPath(game, newTargetPosition );
			targetPosition = newTargetPosition;
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
		if(creatureindex>-1)
			newTargetPosition  = game.getCreatures().elementAt(creatureindex)
					.getLocation();
			else
				newTargetPosition  = game.getHero().getLocation();

		if (targetPosition == null) {
			targetPosition =newTargetPosition;
		}

		if (targetPosition.distance(newTargetPosition) > 4
				|| wayPoints == null) {
			
			wayPoints = followAction.evadeHazard(game, newTargetPosition);
			targetPosition = newTargetPosition;
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
