package dungeon.ai.assessment1;

import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;
import dungeon.ai.assessment1.Action;

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
	private final int MAXTICKS = 20;
	
	private final static int PENALTY_STUCK = -3;
	
	/** The Q-table: it stores for each pair of action and state the 'value of goodness' of taking 
	 * this action at this state */
	private static double[][] qtable;
	
	/** old state */ 
	private State oldState;
	
	/** old action */
	private Action oldAction;
	
	/** ticks to avoid getting stuck */
	private int ticks = 0; 	
	
	/**
	 * Initialise Q-table
	 */
	private static void init() {
		qtable = new double[State.getMaxIndex()][Action.getNumberOfActions()];		
		for(int i=0;i<qtable.length;i++) {
			for(int j=0;j<qtable[0].length;j++) {
				qtable[i][j] = 0.0;
			}
		}
	}
	/**
	 * Update Q-table and take according actions. It is only updated if the state of fCreature changed.
	 * @return true if update occurred; false else.
	 * @return true if fCreature did something; false else.
	 */
	public boolean tick(Game game, Creature fCreature, double reward) {		
		// get new state
		State newState = new State(game, fCreature);
		Action newAction;
		
		if( oldState.hasNotChanged(newState) ){
			ticks += 1;
			if (ticks > MAXTICKS) {
				// just go to next action
				newAction = doGreedyAction(newState, game);
				
				// and give penalty because creature was stuck
				updateTable(PENALTY_STUCK, oldState, newState, oldAction);
				oldState = newState;
				oldAction = newAction;
				ticks = 0;
				return true;				
			} else {
				return false;
			}						
		} else {			
			//do appropriate action
			newAction = doGreedyAction(newState, game);		
			
			// change Q-table
			updateTable(reward, oldState, newState, oldAction);
			
			oldState = newState;
			oldAction = newAction; 
			ticks = 0;
			
			return true;
		}		
	}
	
	/**
	 * Update Q-table according to parameters given.
	 * @param reward The reward to give.
	 * @param oldState The previous state.
	 * @param newState The current state.
	 * @param oldAction The previous action.
	 */
	private void updateTable (double reward, State oldState, State newState, Action oldAction) {
		qtable[oldState.getIndex()][oldAction.ordinal()] += 
			LEARNING_RATE * (reward + DISCOUNT_RATE * getBestActionValue(newState) - qtable[oldState.getIndex()][oldAction.ordinal()]);
	}
	
	/**
	 * Do the best action to take for the given state.
	 * @return Returns the best action to take for the given state.
	 */
	private static Action doBestAction(State state, Game game) {
		double max = Double.MIN_NORMAL;
		int actionIndex = -1;
		for(int i=0;i<qtable[state.getIndex()].length;i++) {
			if (qtable[state.getIndex()][i] > max) {
				max = qtable[state.getIndex()][i];
				actionIndex = i;
			}
		}
		return Action.doAction(actionIndex, game);
	}
	
	/**
	 * Do a random action
	 * @return Returns a random action to take
	 */
	private static Action doRandomAction(Game game) {
		return Action.doRandomAction(game);
	}
	
	/**
	 * Does to GREEDINESS % the best, otherwise a random action
	 * @param state The current state we are in
	 * @return Returns the taken action.
	 */
	private Action doGreedyAction(State state, Game game) {
		java.util.Random generator = new java.util.Random();
		if( generator.nextDouble() < GREEDINESS ) {
			return doBestAction(state, game);
		} else {
			return doRandomAction(game);
		}
	}
	
	/**
	 * @param state The current state.
	 * @return The value of the best action for the current state.
	 */
	private static double getBestActionValue(State state) {
		double max = Double.MIN_NORMAL;
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
}

