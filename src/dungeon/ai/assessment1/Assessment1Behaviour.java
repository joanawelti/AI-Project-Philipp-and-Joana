package dungeon.ai.assessment1;

import dungeon.ai.BehaviourWithPathfindingAStar;
import dungeon.ai.actions.ActionAttack;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;
import dungeon.model.structure.LocationHelper;


public class Assessment1Behaviour extends BehaviourWithPathfindingAStar {	
	/** Constants */ 
	private final int MAXTICKS = 20;	
	
	private final static int PENALTY_STUCK = -1;
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
	
	/** health points of fCreature last tick */
	private double oldHealth;
	
//	/** the Q-table to use */
//	private Qtable learner = new Qtable();

	
	public Assessment1Behaviour(Creature creature) {
		super(creature);
		fCreature = creature;
		oldHealth = fCreature.getCurrentHealth();
	}
	
	/**
	 * Update Q-table and take according actions. It is only updated if the state of fCreature changed.
	 * @return true if update occurred; false else.
	 * @return true if fCreature did something; false else.
	 */
	public boolean onTick(Game game) {		
		if (newState == null) {
			// set initial state
			oldState = new State(game, fCreature);
			// initialise QTable
			Qtable.initializeValues(game);
		}		
		// get new state
		newState = new State(game, fCreature);		
		
		// check if state changed
		if( oldState.hasNotChanged(newState) ){
			ticks += 1;
			if (ticks > MAXTICKS) {
				// get next action
				newAction = Qtable.getGreedyAction(newState, game);
				//newAction = Qtable.getRandomAction();
				
				// and give penalty because creature was stuck
				Qtable.updateTable(PENALTY_STUCK, oldState, newState, oldAction);
				ticks = 0;
			}						
		} else {						
			// Update Q-table
			Qtable.updateTable(oldAction.getReward(), oldState, newState, oldAction);
			
			// get appropriate action
			newAction = Qtable.getGreedyAction(newState, game);
			
			oldState = newState;
			ticks = 0;
		}	
		
		// if action changed
		if (newAction != oldAction) {
			// and recalculate paths
			targetPosition = null;
			wayPoints = null;
		}
		
		// do your action
		boolean moved = false;
		// first check if the creature can attack something,
		switch( ActionAttack.performActionStatus(fCreature, game)) {
		case 0:
			moved = doAction(newAction,game);
			break;
		case 1:
			// ogre gets reward for having hit an enemy
			Qtable.updateTable(REWARD_HIT_ENEMY, oldState, newState, oldAction);
			moved = true;
			break;
		case 2:
			// ogre gets reward for having killed an enemy
			Qtable.updateTable(REWARD_KILL_ENEMY, oldState, newState, oldAction);
			moved = true;
			break;
		}
		
		// check for other rewards
		if (gotHitByEnemy()) {
			Qtable.updateTable(PENALTY_HIT_ENEMY, oldState, newState, oldAction);
		}
		
		// save old action 
		oldAction = newAction;
		
//		// print Qtable
//		System.out.println( Qtable.out() + "\n " + 
//				            oldState.toString() + "\n " + 
//				            newState.toString());
		
		return moved;
	}
	
	public boolean doAction(Action action, Game game) {	
		switch (action) {
			case ATTACK: 
				return doAttack(game);
			case EVADE: 
				return doEvade(game);
//			case GET_HEALTH_POTION:
//				return doGetHealthPotion(game);
//			case GET_ENERGY_POTION:
//				return doGetEnergyPotion(game);
			default:
				return false;
		}
	}
	
	private boolean doAttack(Game game) {
		return followMovingTarget(game, 1);
	}
	
	private boolean doEvade(Game game) {
		return evadeMovingTarget(game, 1);
	}
	
	/**
	 * Move to exit of dungeon if there is one
	 */
	private boolean doGoToExit(Game game) {
		return followTarget(game, LocationHelper.getFinishCenter(game.getMap()));
	}
	
	/**
	 * Go to closest health potion if there are any
	 **/
	private boolean doGetHealthPotion(Game game) {
		// find nearest health potion
		return followTarget(game, LocationHelper.getClosestHealthPotion(game, fCreature.getLocation()));
	}
	
	/**
	 * Go to closest energy potion if there are any
	 */
	private boolean doGetEnergyPotion(Game game) {
		// find nearest energy potion
		return followTarget(game, LocationHelper.getClosestEnergyPotion(game, fCreature.getLocation()));
	}

	/**
	 * @return Returns true if the health points decreased since last tick
	 */
	private boolean gotHitByEnemy() {
		boolean flag = (fCreature.getCurrentHealth() < this.oldHealth); 
		oldHealth = fCreature.getCurrentHealth();
		return flag;
	}
	

	public boolean deathTick(Game game) {
		Qtable.updateTable(PENALTY_DIED, oldState, newState, oldAction);
		return false;
	}

	/**
	 * called when game over
	 */
	public boolean gameOverTick(Game game) {
		if (fCreature.getCurrentHealth() > 0) {
			Qtable.updateTable(REWARD_REACH_EXIT, oldState, newState, oldAction);
		} else {
			Qtable.updateTable(PENALTY_DIED, oldState, newState, oldAction);
		}
		return false;
	}
}
