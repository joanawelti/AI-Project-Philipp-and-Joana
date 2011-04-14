package dungeon.ai;

import java.awt.geom.Point2D;
import java.util.List;

import dungeon.ai.pathfind.PathFind;
import dungeon.ai.pathfind.PathFindAStar;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;
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
		boolean successfulmove = followTarget(game, position);
		if (!successfulmove) {
			// we are stuck
			// need to pathfind with mobs
			wayPoints = null;
			followAction.recalcMapWithMobs(game);
			successfulmove = followTarget(game, position);
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
	
	public boolean evadeTargetAttempt(Game game, Point2D position) {
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

