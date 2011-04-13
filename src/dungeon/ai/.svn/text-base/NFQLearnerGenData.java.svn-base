package dungeon.ai;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import dungeon.ai.actions.ActionAttack;
import dungeon.ai.ai_code.NFQ_learner;
import dungeon.ai.ai_code.OgreState;
import dungeon.ai.ai_code.Transition;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;

public class NFQLearnerGenData extends BehaviourWithPathfindingAStar {

	int creatureindex = 0;
	int oldAction = 0;
	float distIterStepX = 1f;
	float distIterStepY = 2.5f;
	float energyIterStep = 10f;
	static boolean startCheck=true;
	// Random fRandom = new Random();

	OgreState oldState = null;
	OgreState newState = null;
	int tick = 0;

	public NFQLearnerGenData(Creature creature) {
		super(creature);
	}

	public boolean onTick(Game game) {

		switch (NFQ_learner.runningstate) {
		case 0:
			// gather the data
			if (fileSize100()&&startCheck) {
				NFQ_learner.getInstance().transitionList = readFile();
				NFQ_learner.runningstate = 1;
			} else{
				startCheck=false;
				generateTransitions(game);
			}
			break;
		case 1:
			// do the training
			NFQ_learner.getInstance().NFQwithGraphics();
			NFQ_learner.runningstate = 2;
			break;
		case 2:
			// now ready to act using what we've learned
			int newAction= NFQ_learner.getInstance().getBestAction(
					getRealTimeState(game));
			if(oldAction !=newAction){
				oldAction =newAction;
				targetPosition = null;
				wayPoints = null;
			}
			
		}
		
//		if(NFQ_learner.dataIterYpos>2.4&& NFQ_learner.dataIterYpos<2.6)
//				System.out.println("pos"+fCreature.getLocation());
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
		if (tick == 10)
			tick = 0;

		return moved;
	}

	private void generateTransitions(Game game) {
		//we only record every 10 ticks, when tick gets above 10 it's reset to zero
		// if it's the first time then get an action and record state
		if (tick == 0) {
			newState = getRealTimeState(game);
			if (oldState != null) {
				// in this case we are ready to record a transition
				Transition t = new Transition(oldState, oldAction, newState);
				NFQ_learner.getInstance().addTransitionNoTrain(t);
			}
			// this case covers both the very start
			// and every time tick is back to zero and we record a
			// transition

			// generate data ranging across distance, energy
			// outer loop for distance, going across the bottom, from right to left
			if (NFQ_learner.dataIterXpos > 2.55) {
				if (NFQ_learner.dataIterEnergy < 0) {
					NFQ_learner.dataIterXpos -= distIterStepX;
					NFQ_learner.dataIterEnergy = 100;
				}
				game.getCreatures().elementAt(1).placeAt(
						new Point2D.Double(27.5, 27.5), game);
				game.getCreatures().elementAt(1).setCurrentHealth(100);
				fCreature.setCurrentEnergy((int) NFQ_learner.dataIterEnergy);
				fCreature.placeAt(new Point2D.Double(NFQ_learner.dataIterXpos,
						NFQ_learner.dataIterYpos), game);
				newState = getRealTimeState(game);

				if (NFQ_learner.dataIterAttack) {
					oldAction = 1;
					targetPosition = null;
					wayPoints = null;
					NFQ_learner.dataIterAttack = false;
				} else {
					oldAction = 0;
					targetPosition = null;
					wayPoints = null;
					NFQ_learner.dataIterAttack = true;
					//inner loop is for energy, decreasing
					if (NFQ_learner.dataIterEnergy > -1) {

						NFQ_learner.dataIterEnergy -= energyIterStep;
					}
				}

			} else
			// outer loop for distance, going up the left side
			if (NFQ_learner.dataIterYpos > 2) {
				if (NFQ_learner.dataIterEnergy < 0) {
					NFQ_learner.dataIterYpos -= distIterStepY;
					NFQ_learner.dataIterEnergy = 100;
				}
				game.getCreatures().elementAt(1).placeAt(
						new Point2D.Double(27.5, 27.5), game);
				game.getCreatures().elementAt(1).setCurrentHealth(100);
				fCreature.setCurrentEnergy((int) NFQ_learner.dataIterEnergy);
				fCreature.placeAt(new Point2D.Double(NFQ_learner.dataIterXpos,
						NFQ_learner.dataIterYpos), game);
				newState = getRealTimeState(game);

				if (NFQ_learner.dataIterAttack) {
					oldAction = 1;
					targetPosition = null;
					wayPoints = null;
					NFQ_learner.dataIterAttack = false;
				} else {
					oldAction = 0;
					targetPosition = null;
					wayPoints = null;
					NFQ_learner.dataIterAttack = true;
					//inner loop is for energy, decreasing
					if (NFQ_learner.dataIterEnergy > -1) {
						NFQ_learner.dataIterEnergy -= energyIterStep;
					}
				}

			} else {
				System.out.println("finished getting data, ready to train");
				NFQ_learner.runningstate = 1;
			}
			oldState = newState;
			// oldState.print();
		}
	}

	private boolean attack(Game game) {

		// System.out.println("attacking");
		creatureindex = 1;
		return followMovingTarget(game, creatureindex);
	}

	private boolean evade(Game game) {
		//cheat: give him a tiny bit of energy to avoid getting stuck in evade/attack jitter
//		fCreature.setCurrentEnergy(fCreature.getCurrentEnergy()+1);
		
		// System.out.println("evading");
		creatureindex = 1;
		// if(fCreature.getLocation().getY()-2.5<0.01)
		// System.out.println("pos"+fCreature.getLocation());

		return evadeMovingTarget(game, creatureindex);
	}

	private boolean rest() {
		return false;
	}

	public boolean deathTick(Game game) {
		// System.out.println("learner death tick");
		if (NFQ_learner.runningstate == 0) {
			Transition t = new Transition(oldState, oldAction,
					getRealTimeState(game));
			NFQ_learner.getInstance().addTransitionNoTrain(t);
		}
		return false;
	}

	public boolean gameOverTick(Game game) {
		// did I win?
		if (NFQ_learner.runningstate == 0) {
			if (fCreature.getCurrentHealth() > 0) {
				// System.out.println("learner gameover tick");
				Transition t = new Transition(oldState, oldAction,
						getRealTimeState(game));
				NFQ_learner.getInstance().addTransitionNoTrain(t);
			}
		}
		return false;
	}

	// this gets the current state (all four variables) and is used for the
	// transition experiences
	public OgreState getRealTimeState(Game game) {

		double tempCreatureEnergy = (double) fCreature.getCurrentEnergy()
				/ (double) fCreature.getMaxEnergy();
		// System.out.println("energy"+fCreature.getCurrentEnergy()+" "+fCreature.getMaxEnergy()+" "+tempCreatureEnergy);
		double tempCreatureHealth = (double) fCreature.getCurrentHealth()
				/ (double) fCreature.getMaxHealth();
		double enemyHealth = 1;// for simplicity I skipped his health being a variable, so it's just fixed at 1
//		(double) game.getCreatures().get(1)
//				.getCurrentHealth()
//				/ (double) game.getCreatures().get(0).getMaxHealth();
//		if (enemyHealth < .99)
//			enemyHealth = .99;
		double distance = (double) game.getCreatures().get(1).getLocation()
				.distance(fCreature.getLocation()) / 36.0;//36 is to normalise between 0 and 1

		return new OgreState(tempCreatureHealth, tempCreatureEnergy,
				enemyHealth, distance);
	}

	
	
	
	
	


    
    private ArrayList<Transition> readFile() {
    try {
        ArrayList<Transition> transFinal = new ArrayList<Transition>();
        FileReader in = new FileReader("DunOut.txt");
        BufferedReader reader = new BufferedReader(in);
        while (reader.ready()) {
            String values = reader.readLine();
            if (!values.contains("New")) {
                String valuesArray[] = values.split(" ");

               Transition transIn = new Transition(
                		new OgreState(Double.valueOf(valuesArray[0]), Double.valueOf(valuesArray[1]), Double.valueOf(valuesArray[2]), Double.valueOf(valuesArray[3]))
                		, Integer.parseInt(valuesArray[4]),
                		new OgreState(Double.valueOf(valuesArray[5]), Double.valueOf(valuesArray[6]), Double.valueOf(valuesArray[7]), Double.valueOf(valuesArray[8]))
                		);
                System.out.println(transIn.toString());
                transFinal.add(transIn);
            }
        }
        return transFinal;
    } catch (Exception e) {
        System.out.println(e.toString());
        return null;
    }
}

private boolean fileSize100() {
    int i = 0;
    try {
        FileReader in = new FileReader("DunOut.txt");
        BufferedReader reader = new BufferedReader(in);
        while (reader.ready()) {
            if(!reader.readLine().contains("New"))
                    ++i;
        }
        if (i >= 100) {
            return true;
        }
        return false;
    } catch (Exception e) {
        return false;
    }
}

}
