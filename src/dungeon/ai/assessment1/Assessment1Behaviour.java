package dungeon.ai.assessment1;

import dungeon.ai.BehaviourWithPathfindingAStar;
import dungeon.ai.actions.ActionAttack;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;

public class Assessment1Behaviour extends BehaviourWithPathfindingAStar {	
	/** Constants */ 
	private final int MAXTICKS = 20;	
	
	private final static int PENALTY_STUCK = -3;
	private final static int PENALTY_DIED = -10;
	private final static int PENALTY_HIT_ENEMY = -3;
	
	private final static int REWARD_REACH_EXIT = 10;
	private final static int REWARD_HIT_ENEMY = 3;
	private final static int REWARD_KILL_ENEMY = 5;

	/** old and new state */ 
	private State oldState = null;
	private State newState = null;
	
	/** old and new action */
	private Action oldAction = Action.getDefaultAction();
	private Action newAction = Action.getDefaultAction();
	
	/** ticks to avoid getting stuck */
	private int ticks = 0; 	
	
	/** creature to do AI for */
	private Creature fCreature;
	
	/** the Q-table to use */
	private Qtable learner = new Qtable();

	
	public Assessment1Behaviour(Game game, Creature creature) {
		super(creature);
		fCreature = creature;
				
		// set initial state
		oldState = new State(game, creature);
	}
	
	/**
	 * Update Q-table and take according actions. It is only updated if the state of fCreature changed.
	 * @return true if update occurred; false else.
	 * @return true if fCreature did something; false else.
	 */
	public boolean onTick(Game game) {						
		// get new state
		newState = new State(game, fCreature);		
		
		// check if state changed
		if( oldState.hasNotChanged(newState) ){
			ticks += 1;
			if (ticks > MAXTICKS) {
				// get next action
				newAction = learner.getGreedyAction(newState, game);
				
				// and give penalty because creature was stuck
				learner.updateTable(PENALTY_STUCK, oldState, newState, oldAction);
			}						
		} else {						
			// Update Q-table
			learner.updateTable(oldAction.getReward(), oldState, newState, oldAction);
			
			// get appropriate action
			newAction = learner.getGreedyAction(newState, game);
			
			oldState = newState;
			ticks = 0;
		}	
		
		// if action changed
		if (newAction != oldAction) {
			// save old action 
			oldAction = newAction;
			// and recalculate paths
			targetPosition = null;
			wayPoints = null;
		}
		
		// do your action
		boolean moved = false;
		// first check if the creature can attack something,
		if (ActionAttack.performAction(fCreature, game)) {
			// ogre gets reward for having hit the hero
			learner.updateTable(REWARD_HIT_ENEMY, oldState, newState, oldAction);
			moved = true;
		} else {
			moved = newAction.doAction(game);
		}
//			
//		switch (oldAction) {
//			case 0:
//				moved = attack(game);
//				break;
//			case 1:
//				moved = evade(game);
//				break;
//			}		
		return moved;
	}

	/**
	 * OLD
	 * @param game
	 * @return
	 */
	private boolean attack(Game game) {
		int creatureindex = 1;
		return followMovingTarget(game, creatureindex);
	}

	/**
	 * OLD
	 * @param game
	 * @return
	 */
	private boolean evade(Game game) {
		int creatureindex = 1;
		return evadeMovingTarget(game, creatureindex);
	}

	/**
	 * called when ... ???
	 */
	public boolean deathTick(Game game) {
		learner.updateTable(PENALTY_DIED, oldState, newState, oldAction);
		return false;
	}

	/**
	 * called when game over
	 */
	public boolean gameOverTick(Game game) {
		if (fCreature.getCurrentHealth() > 0) {
			learner.updateTable(REWARD_REACH_EXIT, oldState, newState, oldAction);
		} else {
			learner.updateTable(PENALTY_DIED, oldState, newState, oldAction);
		}
		return false;
	}
}
