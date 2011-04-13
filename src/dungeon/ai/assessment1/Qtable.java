package dungeon.ai.assessment1;

import dungeon.model.Game;

/**
 * Generic class for a q-table used by reinforcement learning.
 * 
 * @author Philipp
 *
 */

public class Qtable {

	/**
	 * Constructor for Qtable.
	 * Automaticcally creates qtable with correct dimensions.
	 */
	public Qtable() {
		super();
		init();
	}
	
	// constants
	private final static double GREEDINESS = 0.9;
	private final static double LEARNING_RATE = 1.0;
	private final static double DISCOUNT_RATE = 0.9;

	
	/** The Q-table: it stores for each pair of action and state the 'value of goodness' of taking 
	 * this action at this state */
	private double[][] qtable;
		
	/**
	 * Initialise Q-table
	 */
	private void init() {
		qtable = new double[State.getMaxIndex()][Action.getNumberOfActions()];		
		for(int i=0;i<qtable.length;i++) {
			for(int j=0;j<qtable[0].length;j++) {
				qtable[i][j] = 0.0;
			}
		}
	}

	/**
	 * Update Q-table according to parameters given.
	 * @param reward The reward to give.
	 * @param oldState The previous state.
	 * @param newState The current state.
	 * @param oldAction The previous action.
	 */
	public void updateTable (double reward, State oldState, State newState, Action oldAction) {
		qtable[oldState.getIndex()][oldAction.ordinal()] += 
			LEARNING_RATE * (reward + DISCOUNT_RATE * getBestActionValue(newState) - qtable[oldState.getIndex()][oldAction.ordinal()]);
	}
	
	/**
	 * Gets the best action to take for the given state.
	 * @return Returns the best action to take for the given state.
	 */
	private Action getBestAction(State state) {
		double max = Double.MIN_NORMAL;
		int actionIndex = -1;
		for(int i=0;i<qtable[state.getIndex()].length;i++) {
			if (qtable[state.getIndex()][i] > max) {
				max = qtable[state.getIndex()][i];
				actionIndex = i;
			}
		}
		return Action.getAction(actionIndex);
	}
	
	/**
	 * Gets a random action
	 * @return Returns a random action to take
	 */
	private Action getRandomAction() {
		return Action.getRandomAction();
	}
	
	/**
	 * Does to GREEDINESS % the best, otherwise a random action
	 * @param state The current state we are in
	 * @return Returns the taken action.
	 */
	public Action getGreedyAction(State state, Game game) {
		java.util.Random generator = new java.util.Random();
		if( generator.nextDouble() < GREEDINESS ) {
			return getBestAction(state);
		} else {
			return getRandomAction();
		}
	}
	
	/**
	 * @param state The current state.
	 * @return The value of the best action for the current state.
	 */
	private double getBestActionValue(State state) {
		double max = Double.MIN_NORMAL;
		for(int i=0;i<qtable[state.getIndex()].length;i++) {
			if (qtable[state.getIndex()][i] > max) {
				max = qtable[state.getIndex()][i];
			}
		}
		return max;
	}
	
	private double getValue(State state, Action action) {
		return qtable[state.getIndex()][action.ordinal()];
	}
}

