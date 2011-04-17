package dungeon.ai.assessment1;

import java.util.HashMap;

import dungeon.configuration.ReinforcementLearnerConfigurations;
import dungeon.model.Game;

/**
 * Generic class for a q-table used by reinforcement learning.
 * 
 * @author Philipp, Joana
 *
 */

public class Qtable {
	
	private static double greediness;
	private static double learning_rate;
	private static double discount_rate;
	
	private static double init_value;
	
	private static boolean initialized = false;

	// mappings from parameters to parameter keys as used in the configuration 
	private static int initKey  = 0;
	private static int learning_rateKey = 1;
	private static int discount_rateKey = 2;
	private static int greedinessKey = 3;
		
	/** The Q-table: it stores for each pair of action and state the 'value of goodness' of taking 
	 * this action at this state */
	private static double[][] qtable;
	
	/**
	 * Set up method for QTable
	 * Sets Q-Learning parameters according to game's parameters and initializes QTable
	 * 
	 * @param game Game for which QTable is used
	 */
	public static void initializeValues(Game game) {
		if (!initialized) {
			HashMap<Integer, Double> configs;
			// get configured values if set
			if (game.hasConfigurations()) {
				configs = game.getConfigurations().getParameters();
			} else {
				configs = new ReinforcementLearnerConfigurations().getDefaultValues();
			}	
			greediness = configs.get(greedinessKey);
			learning_rate = configs.get(learning_rateKey);
			discount_rate = configs.get(discount_rateKey);
			init_value = configs.get(initKey);
				
			init();
			initialized = true;
		}
	}
	
	
	/**
	 * Initialise Q-table
	 */
	private static void init() {
		qtable = new double[State.getMaxIndex()][Action.getNumberOfActions()];
		System.out.println("state max index = " + State.getMaxIndex() + " -- Action max index = " + Action.getNumberOfActions());
		for(int i=0;i<qtable.length;i++) {
			for(int j=0;j<qtable[0].length;j++) {
				qtable[i][j] = init_value;
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
	public static void updateTable (double reward, State oldState, State newState, Action oldAction) {
		qtable[oldState.getIndex()][oldAction.ordinal()] += 
			learning_rate * (reward + discount_rate * getBestActionValue(newState) - qtable[oldState.getIndex()][oldAction.ordinal()]);
	}
	
	/**
	 * Gets the best action to take for the given state.
	 * @return Returns the best action to take for the given state.
	 */
	private static Action getBestAction(State state) {
		double max = qtable[state.getIndex()][0];
		boolean allSame = true;
		int actionIndex = 0;
		for(int i=1;i<qtable[state.getIndex()].length;i++) {
			if (qtable[state.getIndex()][i] > max) {
				allSame = false;
				max = qtable[state.getIndex()][i];
				actionIndex = i;			
			}
		}
		if(allSame) {
			return Action.getRandomAction();
		} else {
			return Action.getAction(actionIndex);
		}
	}
	
	
	/**
	 * Gets a random action
	 * @return Returns a random action to take
	 */
	public static Action getRandomAction() {
		return Action.getRandomAction();
	}
	
	/**
	 * Return to greediness % the best, otherwise a random action
	 * @param state The current state we are in
	 * @return Returns the taken action.
	 */
	public static Action getGreedyAction(State state, Game game) {
		java.util.Random generator = new java.util.Random();
		if( generator.nextDouble() < greediness ) {
			return getBestAction(state);
		} else {
			return getRandomAction();
		}
	}
	
	/**
	 * @param state The current state.
	 * @return The value of the best action for the current state.
	 */
	private static double getBestActionValue(State state) {
		double max = Double.NEGATIVE_INFINITY;
		for(int i=0;i<qtable[state.getIndex()].length;i++) {
			if (qtable[state.getIndex()][i] > max) {
				max = qtable[state.getIndex()][i];
			}
		}
		return max;
	}
	
	private static double getValue(State state, Action action) {
		return qtable[state.getIndex()][action.ordinal()];
	}
	
	public static String out() {
		String out = "";
		int i,j;
		for(i=0;i<qtable.length;i++) {
			for(j=0;j<qtable[0].length-1;j++) {
				out += qtable[i][j] + " ; ";				
			}
			out += qtable[i][j];
			out += "\n";
		}
		return out;
	}
}

