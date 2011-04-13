package dungeon.ai;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.UUID;

import dungeon.ai.actions.ActionAttack;
import dungeon.ai.actions.ActionDoor;
import dungeon.ai.actions.ActionPickUp;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;
import dungeon.model.items.treasure.Treasure;
import dungeon.model.structure.Tile;

/**
 * Class providing default behaviour for Creature mobs
 */
public class NewBehaviour implements Behaviour
{
	/**
	 * Constructor
	 * 
	 * @param creature The creature
	 */
	public NewBehaviour(Creature creature)
	{
		fCreature = creature;
	}
	
	Creature fCreature = null;
	Random fRandom = new Random();
	Point2D nextStep = null;
	HashMap<UUID, Boolean> visitedTiles = new HashMap<UUID, Boolean>();
	Stack<Point2D> backTrack = new Stack<Point2D>();
	
	
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
	boolean move(Game game)
	{
		// creature has not moved
		boolean moved = false;
		
		// does the creature have a goal?  If not, set one
		if (fCreature.getGoal() == null)
		{
			Rectangle2D bounds = null; // where to look for new goal
			
			// if KEEP_TO_ROOMS is set, look for a goal point in the same tile (room)
			if (DefaultBehaviour.KEEP_TO_ROOMS)
			{
				Tile tile = game.getMap().getTileAt(fCreature.getLocation());
				bounds = tile.getArea();
			}
			// otherwise, look for a goal point anywhere on map
			else
			{
				bounds = game.getMap().getBounds(0);
			}
			Point2D goal_pt = null;
			
			// add all bearable and reachable treasure in the room to a list
			if(nextStep == null){
				if(game.getTreasure().size() > 0){
					List<Treasure> treasureInRoom = getTreasureInSameRoom(game);
					//If no treasure is left go to next room
					if(treasureInRoom.isEmpty()){
						goal_pt = changeRoom(game);
					}else{
						goal_pt = treasureInRoom.get(0).getLocation();
					}
				}

				// choose random point if there is no treasure left
				if(goal_pt == null){
					double x = bounds.getX() + (bounds.getWidth() * fRandom.nextDouble());
					double y = bounds.getY() + (bounds.getHeight() * fRandom.nextDouble());
					goal_pt = new Point2D.Double(x, y);

				}
			}else{
				goal_pt = nextStep;
				nextStep = null;
			}
			
			// check that this point is within a room. not occupied by another creature, etc
			if (CollisionDetection.canOccupy(game, fCreature, goal_pt))
				// all conditions passed, set a new goal
				fCreature.setGoal(goal_pt, game);
		}
		
		// if the creature has a goal (perhaps set above), move towards it
		if (fCreature.getGoal() != null)
		{			
			moved = fCreature.moveToGoal(game);
		}
	
		// if the creature hasn't moved towards a goal, make it move randomly
		if (!moved)
		{
			double theta = fRandom.nextDouble() * Math.PI * 2;
			moved = fCreature.move(theta, game);
		}
		
		return moved;
	}
	
	private boolean isTreasureBearable(Treasure treasure){
		double capacity = fCreature.getStrength() - fCreature.getEncumbrance();
		return capacity >= treasure.getWeight();
	}
	
	private boolean isTresureInSameRoom(Treasure treasure, Game game){
		Tile npcTile = game.getMap().getTileAt(fCreature.getLocation());
		Tile treasuretile = game.getMap().getTileAt(treasure.getLocation());
		return npcTile.equals(treasuretile);
	}
	
//	private boolean isTrasureInRoom
	
	private List<Treasure> getTreasureInSameRoom(Game game){
		List<Treasure> treasureList = new ArrayList<Treasure>();
		for(Treasure treasure : game.getTreasure()){
			if(isTresureInSameRoom(treasure, game) && isTreasureBearable(treasure)){
				treasureList.add(treasure);
			}
		}
		return treasureList;
	}
	
	private Point2D changeRoom(Game game){
		Tile npcTile = game.getMap().getTileAt(fCreature.getLocation());
		visitedTiles.put(npcTile.getID(), true);
		
		for(Tile tile : game.getMap().getTiles()){
			if(tile.touches(npcTile) && (visitedTiles.get(tile.getID()) == null || !visitedTiles.get(tile.getID()))){
				//go to first connected room
				nextStep = new Point2D.Double(tile.getArea().getCenterX(), tile.getArea().getCenterY());
				backTrack.push(tile.getTouchPoint(npcTile));
				return tile.getTouchPoint(npcTile);
			}
		}
		if(backTrack.isEmpty()){
			return null;
		}
		return backTrack.pop();
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
