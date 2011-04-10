package dungeon.ai;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;

import dungeon.App;
import dungeon.ai.actions.ActionAttack;
import dungeon.ai.actions.ActionDoor;
import dungeon.ai.actions.ActionPickUp;
import dungeon.ai.pathfind.PathFind;
import dungeon.ai.pathfind.PathfindAStar;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;
import dungeon.ui.MapPanel;

public class FollowHeroBehaviour implements Behaviour {
	/**
	 * Constructor
	 * 
	 * @param creature The creature
	 */
	public FollowHeroBehaviour(Creature creature)
	{
		fCreature = creature;
		followAction = new PathfindAStar(creature);
	}
	
	Creature fCreature = null;
	Random fRandom = new Random();
	PathFind followAction = null; 
	List<Point2D> wayPoints = null;
	Point2D heroPosition = null;
	int pointer = 0;
	
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
	boolean move(Game game) {
		if (heroPosition == null) {
			heroPosition = game.getHero().getLocation();
		}

		if (heroPosition.distance(game.getHero().getLocation()) > 4
				|| wayPoints == null) {
			
			wayPoints = pathfind(game, game.getHero().getLocation());
			heroPosition = game.getHero().getLocation();
			fCreature.setGoal(null, game);
			
		} 
		
		if (fCreature.getGoal() == null && wayPoints.size() > 0) {
			
			Point2D goal = findBestWayPoint(game);
			wayPoints.remove(wayPoints.size()-1);

			fCreature.setGoal(goal, game);
//			App.log("Goal: " + fCreature.getLocation());
		}
		
		MapPanel.setPath(wayPoints);
		return fCreature.moveToGoal(game);

	}

	private Point2D findBestWayPoint(Game game) {
		int index = 0;
		Point2D goal = wayPoints.get(wayPoints.size()-1);

		return goal;
	}
	
	private List<Point2D> pathfind(Game game, Point2D goal){
		
			return followAction.findPath(game, goal);
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
