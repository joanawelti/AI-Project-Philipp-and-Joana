package dungeon.ai.assessment1;

import java.util.Random;

import dungeon.model.Game;
/**
 * 
 * @author Philipp
 *
 */
enum Action {
	ATTACK, EVADE, GET_HEALTH_POTION, GET_ENERGY_POTION;
	
	public boolean doAction(Game game) {
		switch (this) {
			case ATTACK: 
				return doAttack(game);
			case EVADE: 
				return doEvade(game);
			case GET_HEALTH_POTION:
				return doGetHealthPotion(game);
			case GET_ENERGY_POTION:
				return doGetEnergyPotion(game);
			default:
				return false;
		}
	}
	
	public static Action getRandomAction() {
		return getAction(new Random().nextInt(Action.getNumberOfActions()));
	}
	
	public static Action getDefaultAction() {
		return Action.ATTACK;
	}
	
	private boolean doAttack(Game game) {
		game.
		return followMovingTarget(game, 1);
	}
	
	private boolean doEvade(Game game) {
		return evadeMovingTarget(game, 1);
	}
	
	private boolean doGetHealthPotion(Game game) {
		return false;
	}
	
	private boolean doGetEnergyPotion(Game game) {
		return false;
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
