package dungeon.ai.assessment1;

import java.awt.geom.Point2D;

import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;

/**
 * @author Philipp, Joana
 *
 */
public class State { 

	/* Each possible state is assigned a unique number between (including) 0 and getMaxIndex().
	 * This index is stored in this variable. */
	private int index;
	
	/* state variables for reinforcement learning */
	private double health;	// 1st dimension
	private double energy; // 2nd dimension
	private double distanceToEnemy; // 3rd dimension
	private double distanceToGoal; // 4th dimension
	
	/* interval sizes (difference between lowest and highest value) */
	private int energyInterval;
	private int healthInterval;
	private int distanceToEnemyInterval;
	private int distanceToGoalInterval;
	
	/* indices */
	private int healthIndex;
	private int energyIndex;
	private int distanceToEnemyIndex;
	private int distanceToGoalIndex;
	
	/* constant to define in how many section an interval is divided */
	public static final int ENERGY_SECTION_CNT = 3;
	public static final int HEALTH_SECTION_CNT = 3;
	public static final int DISTANCE_ENEMY_SECTION_CNT = 5;
	public static final int DISTANCE_GOAL_SECTION_CNT = 5;
	
	/**
	 * Creates a new instance of this class, i.e. a state for the provided creature.
	 * @param game The game world that the creatures is in.
	 * @param fCreature The creature to get the state for.
	 */
	public State(Game game, Creature fCreature) {
		super();		
		// get state variable values
		setHealth( (double) fCreature.getCurrentHealth() );
		setEnergy( (double) fCreature.getCurrentEnergy() );
		setDistanceToEnemy(getMinimumDistanceToEnemy(game, fCreature) );
		Point2D finish = dungeon.model.structure.LocationHelper.getFinishCenter(game.getMap());
		setDistanceToGoal(finish.distance(fCreature.getLocation()));		
		
		// get interval lengths
		energyInterval = fCreature.getMaxEnergy() / ENERGY_SECTION_CNT;
		healthInterval = fCreature.getMaxHealth() / HEALTH_SECTION_CNT;
		distanceToEnemyInterval = (int) Math.sqrt(
				Math.pow(game.getMap().getBounds(0).getMaxX() - game.getMap().getBounds(0).getMinX(), 2) + 
				Math.pow(game.getMap().getBounds(0).getMaxY() - game.getMap().getBounds(0).getMinY(), 2) ) / DISTANCE_ENEMY_SECTION_CNT;
		distanceToGoalInterval = distanceToEnemyInterval;
		
		// calculate indices
		setIndex();
	}

	/**
	 * @return Returns the index of this state.
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * All possible indices of an instance of State are in the interval [0,getMaxIndex()) (including 0, excluding getMaxIndex()). 
	 * @return Returns the number of possible different indices. 
	 */
	public static int getMaxIndex() {
		return ENERGY_SECTION_CNT * HEALTH_SECTION_CNT * DISTANCE_ENEMY_SECTION_CNT * DISTANCE_GOAL_SECTION_CNT;
	}
	
	/**
	 * Sets the index of this state depending on the current values of the state variables.
	 */
	private void setIndex() {
		healthIndex = getIndex( getHealth(), healthInterval,HEALTH_SECTION_CNT);
		energyIndex = getIndex( getEnergy(), energyInterval,ENERGY_SECTION_CNT);
		distanceToEnemyIndex = getIndex( getDistanceToEnemy(), distanceToEnemyInterval,DISTANCE_ENEMY_SECTION_CNT);
		distanceToGoalIndex = getIndex( getDistanceToGoal(), distanceToGoalInterval,DISTANCE_GOAL_SECTION_CNT);
		
//		this.index = 
//		(energyIndex) + // energy dimension 
//		(distanceIndex*ENERGY_SECTION_CNT); //distance dimension	
		
//		this.index = 
//			healthIndex + // health dimension 
//			(energyIndex*HEALTH_SECTION_CNT) + // energy dimension 
//			(distanceIndex*ENERGY_SECTION_CNT*HEALTH_SECTION_CNT); //distance dimension		
		
		this.index = 
		healthIndex + // health dimension 
		(energyIndex*HEALTH_SECTION_CNT) + // energy dimension 
		(distanceToEnemyIndex*ENERGY_SECTION_CNT*HEALTH_SECTION_CNT) + //distance to enemy dimension
		(distanceToGoalIndex*DISTANCE_ENEMY_SECTION_CNT*ENERGY_SECTION_CNT*HEALTH_SECTION_CNT); //distance to goal dimension
	}
	
	/**
	 * @param state The state to check against.
	 * @return Returns true if both states are the same, i.e. they map to the same index, i.e.
	 * this methods returns true iff state.getIndex() == this.getIndex().
	 */
	public boolean hasNotChanged(State state) {
		return state.getIndex() == this.getIndex();
	}
	
	/**
	 * @param game The game object.
	 * @param fCreature The creature to which the relative distances are calculated.
	 * @return The distance to the closest creature to fCreature.
	 */
	private double getMinimumDistanceToEnemy(Game game, Creature fCreature) {
		double minDist = Double.MAX_VALUE;
		for( Creature c : game.getCreatures() ) {
			if(!c.equals(fCreature)) {
				double dist = c.getLocation().distance(fCreature.getLocation());
				if (dist < minDist) {
					minDist = dist;
				}
			}
		}
		return minDist;
	}

	/**
	* Gets the index `value` belongs to
	* @param value that needs to be found an interval for
	* @param intervalsize size of the intervals
	* @param intervals number of intervals
	* @return interval 0 <= interval <= max energy / INTERVALNR
	*/
	private static int getIndex(double value, double intervalsize, int maxIndex) {
//		int i=0;
//		while(value > (i+1)*intervalsize) {
//			i++;
//		}
//		return (i>maxIndex?maxIndex:i);
//		
		// quadratic interval length
		double fitFactor = intervalsize/maxIndex;
		int i=0;
		while(value >Math.pow(i+1,2)*fitFactor) {
			i++;
		}
		//System.out.println(Math.pow(i+1,2)*fitFactor);
		return (i>maxIndex-1?maxIndex-1:i);
	}	

	public double getHealth() {
		return health;
	}

	public void setHealth(double health) {
		this.health = health;
	}

	public double getEnergy() {
		return energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}

	public double getDistanceToEnemy() {
		return distanceToEnemy;
	}

	public void setDistanceToEnemy(double distanceToEnemy) {
		this.distanceToEnemy = distanceToEnemy;
	}
	
	@Override
	public String toString() {
		String out = "";
		out += "energy = " + getEnergy() + " ~ " + energyIndex + "\n" +
		       "health = " + getHealth() + " ~ " + healthIndex + "\n" +
		       "distance to goal = " + getDistanceToGoal() + " ~ " + distanceToGoalIndex +
			   "distance to enemy = " + getDistanceToEnemy() + " ~ " + distanceToEnemyIndex;
		return out;
	}

	public double getDistanceToGoal() {
		return distanceToGoal;
	}

	public void setDistanceToGoal(double distanceToGoal) {
		this.distanceToGoal = distanceToGoal;
	}	
}

