package dungeon.ai.assessment1;

import java.util.Random;
/**
 * 
 * @author Philipp
 *
 */
public enum Action {
	ATTACK, EVADE, GET_HEALTH_POTION, GET_ENERGY_POTION, GO_TO_EXIT;
	
	/**
	 * @return Returns a random action.
	 */
	public static Action getRandomAction() {
		return getAction(new Random().nextInt(Action.getNumberOfActions()));
	}
	
	/**
	 * @return Returns the default action to be taken.
	 */
	public static Action getDefaultAction() {
		return Action.GO_TO_EXIT;
	}
	
	/**
	 * @param index The ordinal number of the Action to return.
	 * @return the Action with ordinal number index.
	 */
	public static Action getAction(int index) {
		for ( Action e : Action.values() ) {
			if (e.ordinal() == index) {
				return e;
			}
		}
		return null;
	}	
	
	/**
	 * @return Number of different Actions. 
	 */
	public static int getNumberOfActions() {
		return Action.values().length;
	}
	
	/**
	 * Return the reward for the taken action
	 * macht das sinn??
	 * TODO
	 */
	public double getReward() {
		switch (this) {
		case ATTACK: 
			return 0;
		case EVADE: 
			return 0;
		case GET_HEALTH_POTION:
			return 0;
		case GET_ENERGY_POTION:
			return 0;
		default:
			return 0;
		}
	}
}
