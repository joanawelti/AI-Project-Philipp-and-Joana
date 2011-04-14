package dungeon.ai.assessment1;

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
	
	/* interval sizes (difference between lowest and highest value) */
	private int energyInterval;
	private int healthInterval;
	private int distanceInterval;
	
	/* constant to define in how many section an interval is divided */
	public static final int ENERGY_SECTION_CNT = 5;
	public static final int HEALTH_SECTION_CNT = 5;
	public static final int LENGTH_SECTION_CNT = 5;	
	
	/**
	 * Creates a new instance of this class, i.e. a state for the provided creature.
	 * @param game The game world that the creatures is in.
	 * @param fCreature The creature to get the state for.
	 */
	public State(Game game, Creature fCreature) {
		super();		
		setHealth( (double) fCreature.getCurrentHealth() );
		setEnergy( (double) fCreature.getCurrentEnergy() );
		setDistanceToEnemy(getMinimumDistanceToEnemy(game, fCreature) );
		energyInterval = fCreature.getMaxEnergy() / ENERGY_SECTION_CNT;
		healthInterval = fCreature.getMaxHealth() / HEALTH_SECTION_CNT;
		distanceInterval = (int) Math.sqrt(
				Math.pow(game.getMap().getBounds(0).getMaxX() - game.getMap().getBounds(0).getMinX(), 2) + 
				Math.pow(game.getMap().getBounds(0).getMaxY() - game.getMap().getBounds(0).getMinY(), 2) ) / LENGTH_SECTION_CNT;
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
		//return ENERGY_SECTION_CNT * HEALTH_SECTION_CNT * LENGTH_SECTION_CNT;
		return ENERGY_SECTION_CNT * LENGTH_SECTION_CNT;
	}
	
	/**
	 * Sets the index of this state depending on the current values of the state variables.
	 */
	private void setIndex() {
		int healthIndex = getIndex( getHealth(), healthInterval, HEALTH_SECTION_CNT);
		int energyIndex = getIndex( getEnergy(), energyInterval, ENERGY_SECTION_CNT);
		int distanceIndex = getIndex( getDistanceToEnemy(), distanceInterval, LENGTH_SECTION_CNT); 
			
//		this.index = 
//			healthIndex + // health dimension 
//			(energyIndex*HEALTH_SECTION_CNT) + // energy dimension 
//			(distanceIndex*ENERGY_SECTION_CNT*HEALTH_SECTION_CNT); //distance dimension

		this.index = 
		(energyIndex) + // energy dimension 
		(distanceIndex*ENERGY_SECTION_CNT); //distance dimension			
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
	private static int getIndex(double value, double intervalsize, int intervals) {
		int index = 0;
		for (int i = 1; i < intervals; i++) {
			if (value > i*intervalsize && value <= (i+1)*intervalsize) {
				index = i;
				break;
			}
		}
		return index;
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
		out += "energy = " + getEnergy() + " ~ " + getIndex( getEnergy(), energyInterval, ENERGY_SECTION_CNT) + "\n" +
		       "distance = " + getDistanceToEnemy() + " ~ " + getIndex( getDistanceToEnemy(), distanceInterval, LENGTH_SECTION_CNT);
		return out;
	}	
}

