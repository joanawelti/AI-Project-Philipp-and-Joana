package dungeon.ai.assessment1;


import dungeon.ai.BehaviourWithPathfindingAStar;
import dungeon.ai.actions.ActionAttack;
import dungeon.ai.ai_code.OgreState;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;

public class Temp extends BehaviourWithPathfindingAStar {

	private int ticks = 0; // ticks to avoid getting stuck
	private final int MAXTICKS = 20;

	private int creatureindex = 0;
	private int oldAction = 0; // last action taken
	static boolean startCheck = true;
	private OgreState newState = null; // current state of the Ogre 
	private OgreState oldState = null; // state the Ogre was in before newState

	private Creature fCreature; 

	public Temp(Creature creature) {
		super(creature);
		fCreature = creature;
	}

	public boolean onTick(Game game) {
		// save last state
		if (newState != null) {
			oldState = newState.cloneWithSameEnergyDistanceAction();
		}
		int newAction = -1;

		ReinforcementLearner.initializeValues(game, fCreature);

		// has state changed?
		newState = getRealTimeState(game);
		if (ReinforcementLearner.hasStateChanged(newState, oldState)) {
			newAction = ReinforcementLearner.getNextAction(newState);
			newState.setOgreAction(newAction);
			ReinforcementLearner.updateState(oldState, newState, 0);
			// only reset ticks if state has actually changed
			ticks = 0; 
		} else {
			ticks += 1;
			if (ticks > MAXTICKS) {
				// set reward to -1 since we've been stuck in the same state for 20 ticks
				newAction = ReinforcementLearner.getNextAction(newState);
				newState.setOgreAction(newAction);
				ReinforcementLearner.updateState(oldState, newState, -1);
			}
		}

		if (newAction != -1 && oldAction != newAction) {
			// new action, recalculate path
			oldAction = newAction;
			targetPosition = null;
			wayPoints = null;
		}

		boolean moved = false;

		// first check if the creature can attack something,
		if (ActionAttack.performAction(fCreature, game)) {
			// ogre gets reward for having hit the hero
			ReinforcementLearner.setReward(oldState, newState);
			moved = true;
		} else
			switch (oldAction) {
			case 0:
				moved = attack(game);
				break;
			case 1:
				moved = evade(game);
				break;
			}
		return moved;
	}

	private boolean attack(Game game) {
		creatureindex = 1;
		return followMovingTarget(game, creatureindex);
	}

	private boolean evade(Game game) {
		creatureindex = 1;
		return evadeMovingTarget(game, creatureindex);
	}

	public boolean deathTick(Game game) {
		ReinforcementLearner.setPenalty(oldState, newState);
		return false;
	}

	public boolean gameOverTick(Game game) {
		if (fCreature.getCurrentHealth() > 0) {
			ReinforcementLearner.setReward(oldState, newState);
		} else {
			ReinforcementLearner.setPenalty(oldState, newState);
		}
		return false;
	}

	// this gets the current state and is used for the
	// transition experiences
	public OgreState getRealTimeState(Game game) {
		double CreatureEnergy = (double) fCreature.getCurrentEnergy();
		double distance = (double) game.getCreatures().get(1).getLocation()
		.distance(fCreature.getLocation());
		return new OgreState(CreatureEnergy,distance);
	}

}
