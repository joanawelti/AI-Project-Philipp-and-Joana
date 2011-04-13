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
	
	private boolean doAction(Action action, Game game) {
		switch (action) {
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
	
	public static Action doAction(int index, Game game) {
		Action action = Action.getEnum(index);
		action.doAction(action, game);
		return action;
	}
	
	public static Action doRandomAction(Game game) {
		return doAction(new Random().nextInt(Action.getNumberOfActions()), game);
	}
	
	private boolean doAttack(Game game) {
		return false;
	}
	
	private boolean doEvade(Game game) {
		return false;
	}
	
	private boolean doGetHealthPotion(Game game) {
		return false;
	}
	
	private boolean doGetEnergyPotion(Game game) {
		return false;
	}
	
	/**
	 * @param index The ordinal number of the enum to return.
	 * @return the enum with ordinal number index.
	 */
	public static Action getEnum(int index) {
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
}
