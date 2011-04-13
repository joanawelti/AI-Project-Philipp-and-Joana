package dungeon.ai;

import java.util.Random;

import dungeon.ai.actions.ActionAttack;
import dungeon.ai.ai_code.NFQ_learner;
import dungeon.ai.ai_code.OgreState;
import dungeon.ai.ai_code.Transition;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;

public class NFQLearner extends BehaviourWithPathfindingAStar {

	int creatureindex = 0;
	int oldAction = 0;
//	Random fRandom = new Random();
	
	OgreState oldState = null;
	OgreState newState = null;
	int tick = 0;

	public NFQLearner(Creature creature) {
		super(creature);
	}

	public boolean onTick(Game game) {

		// if it's the first time then get an action and record state
		if (tick == 0) {
			newState= getRealTimeState(game);
			if (oldState != null) {
				// in this case we are ready to record a transition
				Transition t = new Transition(oldState, oldAction,newState);
				NFQ_learner.getInstance().addTransition(t);
			}
			// this case covers both the very start
			// and every time tick is back to zero and we record a
			// transition
			oldAction = NFQ_learner.getInstance().selectAction(newState);
			oldState =newState;
		}

		boolean moved = false;
		// first check if the creature can attack something,
		if (ActionAttack.performAction(fCreature, game))
			moved = true;
		else
			switch (oldAction) {
			case 0:
				moved = attack(game);
				break;

			case 1:
				moved = evade(game);
				break;
			}

		tick++;
		if (tick == 5)
			tick = 0;

		return moved;
	}



	private boolean attack(Game game) {
		creatureindex = 1;
		return followMovingTarget(game,creatureindex);
	}

	private boolean evade(Game game) {
		creatureindex = 1;
		return evadeMovingTarget(game,creatureindex);
	}


	
	private boolean rest() {
		return false;
	}


	public boolean deathTick(Game game) {
		Transition t = new Transition(oldState, oldAction ,
				getRealTimeState(game));
		NFQ_learner.getInstance().addTransition(t);

		return false;
	}

	public boolean gameOverTick(Game game) {
		Transition t = new Transition(oldState, oldAction ,
				getRealTimeState(game));
		NFQ_learner.getInstance().addTransition(t);

		return false;
	}
	
	//this gets the current state (all four variables) and is used for the transition experiences
	public OgreState getRealTimeState(Game game) {
		
		double tempCreatureEnergy = (double)fCreature.getCurrentEnergy()/(double)fCreature.getMaxEnergy();
//		System.out.println("energy"+fCreature.getCurrentEnergy()+" "+fCreature.getMaxEnergy()+" "+tempCreatureEnergy);
		double tempCreatureHealth = (double)fCreature.getCurrentHealth()/(double)fCreature.getMaxHealth();
		double enemyHealth = (double)game.getCreatures().get(1).getCurrentHealth()/(double)game.getCreatures().get(0).getMaxHealth();
		double distance = (double)game.getCreatures().get(1).getLocation().distance(fCreature.getLocation())/64.0;
		
		return new OgreState(tempCreatureHealth, tempCreatureEnergy, enemyHealth, distance);
	}
	


}
