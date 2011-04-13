package dungeon.ai;

import java.awt.geom.Point2D;

import dungeon.App;
import dungeon.ai.actions.ActionAttack;
import dungeon.ai.ai_code.Q_learner;
import dungeon.ai.ai_code.TeamState;
import dungeon.ai.ai_code.TeamState.FactionAction;
import dungeon.ai.ai_code.TeamState.FactionState;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;

public class FourOnOneMusterThenAttackQLearner extends BehaviourWithPathfindingAStar {

	int creatureindex = 0;
	int newAction = 0;
	Point2D[] oldLocation=new Point2D[] {new Point2D.Double(),new Point2D.Double(),new Point2D.Double(),new Point2D.Double()};
	int howLongStuck=0;
	FactionState localState = FactionState.farAway;
	FactionAction localAction = null;
	boolean firstTick=true;

	public FourOnOneMusterThenAttackQLearner(Creature creature) {
		super(creature);
	}

	public boolean onTick(Game game) {
		// if first time
		firstTickInitialise(game);

		// first check if the creature can attack something,
		// pick up something, or open a door
		if (ActionAttack.performAction(fCreature, game))
			return true;

		// find the state we are in
		findState(game);
		
		// test if we are stuck in a state and going nowhere
		checkIfStuckInState(game);

		stateChange(game);

		if (move(game))
			return true;

		return false;
	}

	private void stateChange(Game game) {
		// if the state has changed then do an update
		if (localState != TeamState.GlobalState || howLongStuck > 10) {
			// if I'm the leader I update the table and pick a new action
			if (game.getCreatures().indexOf(fCreature) == 1) {
				Q_learner.updateTable(localState, TeamState.GlobalState,
						localAction, 0);
				// select an action
				pickAction();
				App.log("currentState = " + TeamState.GlobalState);
				App.log("currentAction = " + TeamState.GlobalAction);
			} 
			localState = TeamState.GlobalState;
			howLongStuck = 0;
		}

		//if the action has changed
		if (localAction != TeamState.GlobalAction) {
			localAction = TeamState.GlobalAction;
			targetPosition = null;
			wayPoints = null;
		}
	}

	private void checkIfStuckInState(Game game) {
		boolean potentiallyStuck = true;
		for (int i = 1; i < game.getCreatures().size(); i++)
			if (game.getCreatures().elementAt(i).getLocation().distance(oldLocation[i - 1])>0.01) {
				potentiallyStuck = false;
				oldLocation[i - 1] = game.getCreatures().elementAt(i).getLocation();
			}
		if (potentiallyStuck)
			howLongStuck++;
		else
			howLongStuck = 0;
	}

	private void firstTickInitialise(Game game) {
		// if I am the leader, I initialise the Q-table
		if (game.getCreatures().indexOf(fCreature) == 1) {
			if (Q_learner.Q_table == null) {
				Q_learner.initTable(2, 2);
			}
			if (firstTick)// and pick a starting action (on every new game)
			{
				//set the global state
				TeamState.GlobalState=FactionState.farAway;
				pickAction();
				App.log("currentState = " + TeamState.GlobalState);
				App.log("currentAction = " + TeamState.GlobalAction);
			}
		}
		firstTick = false;
		if (localAction == null)
			localAction = TeamState.GlobalAction;
	}

	private void pickAction() {
		if(TeamState.GlobalState==FactionState.close)
			App.log("");
		newAction = Q_learner.selectAction(TeamState.GlobalState);
		TeamState.GlobalAction=FactionAction.class.getEnumConstants()[newAction];
	}

	private boolean move(Game game) {
		// System.out.println("Orc "+game.getCreatures().indexOf(fCreature)+" is "+currentAction);
		if (localAction == FactionAction.muster)
			return muster(game);
		if (localAction == FactionAction.attack)
			return attack(game);
		return true;
	}

	private boolean attack(Game game) {
		// hardcoded to go for the first creature
		creatureindex = 0;
		return followMovingTarget(game,creatureindex);
	}

	private boolean muster(Game game) {
		// if I'm the team leader, they come to me
		if (game.getCreatures().indexOf(fCreature) == 1) {
			return rest();
		} else
		// how far am I from the leader?
		if (fCreature.getLocation().distance(
				game.getCreatures().elementAt(1).getLocation()) < 5)
			return rest();
		else
			creatureindex = 1;

		return followMovingTarget(game,creatureindex);
	}



	private boolean rest() {
		return false;
	}

	void findState(Game game) {
		// don't change this state if there is only one/two orcs left
		if (game.getCreatures().size() > 3) {
			
		// find the largest distance to another orc 
		// cycle through creatures and pick one on my side furthest from the leader
		double Orcdist = 0, newdist = 100;
		for (creatureindex = 0; creatureindex < game.getCreatures().size(); creatureindex++) {
			Creature c = game.getCreatures().elementAt(creatureindex);
			if (//(c.getCurrentHealth() > 0)&& 
					(c.getFaction().equalsIgnoreCase(fCreature.getFaction()))
					&& creatureindex!=1) {
				// we have found a potential candidate, now check out his
				// distance
				newdist = game.getCreatures().elementAt(1).getLocation().distance(c.getLocation());
				if (newdist > Orcdist) {
					Orcdist = newdist;
				}
			}
		}

			if (TeamState.GlobalState == TeamState.FactionState.farAway)
				if (Orcdist < 20)
					TeamState.GlobalState = TeamState.FactionState.close;
			if (TeamState.GlobalState == TeamState.FactionState.close)
				if (Orcdist > 50)
					TeamState.GlobalState = TeamState.FactionState.farAway;
		}
	}

	public boolean deathTick(Game game) {
		// Am I the last man standing on my team?
		// If so then this is not good news because I just died
		if(game.getCreatures().size()==2){
			findState(game);
			Q_learner.updateTable(localState, TeamState.GlobalState,
					localAction, -10);}
		return false;
	}

	public boolean gameOverTick(Game game) {
		//This means I won the game
		//big reward
		//just do it for the leader
		if(game.getCreatures().indexOf(fCreature) == 0){
		findState(game);
		Q_learner.updateTable(localState, TeamState.GlobalState,
				localAction, 10);}
		return false;
	}
}
