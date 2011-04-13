package dungeon.ai;

import dungeon.ai.actions.ActionAttack;
import dungeon.ai.ai_code.CircularNetwork;
import dungeon.ai.ai_code.Experience;
import dungeon.ai.ai_code.OgreState;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;
import dungeon.model.items.mobs.Movement;

public class CircularChaseGenNN extends BehaviourWithPathfindingAStar {

	int creatureindex = 0;
	int oldAction = 2;
	static boolean startCheck = true;
	OgreState newState = null;

	public CircularChaseGenNN(Creature creature) {
		super(creature);
	}

	public boolean onTick(Game game) {
		Movement m = game.getHero().getMovement();
		int newAction = 2;//starting with "rest" action
		switch (CircularNetwork.runningstate) {
		case 0:
			// gather the data
			if (m.Train) {
				CircularNetwork.runningstate = 1;
			} else {
				startCheck = false;
				if (m.Attack)
					newAction = 0;
				if (m.RunAway)
					newAction = 1;
				if ((newAction != 2)) {
					// record a transition
					newState = getRealTimeState(game);
					Experience t = new Experience(newState, newAction);
					CircularNetwork.getInstance().addExperienceNoTrain(t);
				}
			}
			break;
		case 1:
			// do the training
			CircularNetwork.getInstance().trainwithGraphicsNN();
			CircularNetwork.runningstate = 2;
		case 2:
			// now ready to act using what we've learned
			if (m.Play) {
				CircularNetwork.runningstate = 0;
			} else {
				if (CircularNetwork.getInstance().queryNetwork(
						getRealTimeState(game), 0)[0] > 0.5)
					newAction = 1;
				else
					newAction = 0;
				System.out.println("action:" + newAction);
			}
		}

		if (oldAction != newAction) {
			oldAction = newAction;
			targetPosition = null;
			wayPoints = null;
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
			case 2:
				moved = rest();
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

	private boolean rest() {
		return false;
	}

	public boolean deathTick(Game game) {
		return false;
	}

	public boolean gameOverTick(Game game) {
		return false;
	}

	// this gets the current state and is used for the
	// transition experiences
	public OgreState getRealTimeState(Game game) {
		double CreatureEnergy = (double) fCreature.getCurrentEnergy()
				/ (double) fCreature.getMaxEnergy();
		double distance = (double) game.getCreatures().get(1).getLocation()
				.distance(fCreature.getLocation()) / 36.0;// 36 is to normalise
															// between 0 and 1
		return new OgreState(CreatureEnergy,distance);
	}

}
