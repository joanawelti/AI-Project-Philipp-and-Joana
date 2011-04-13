package dungeon.ai;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import javax.swing.JOptionPane;

import dungeon.App;
import dungeon.ai.actions.ActionAttack;
import dungeon.ai.actions.ActionDoor;
import dungeon.ai.actions.ActionPickUp;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;
import dungeon.model.items.mobs.Hero;
import dungeon.model.structure.Tile;

/**
 * Class providing initial behaviour for riddle creature
 * Very simple. Does not move or pick up treasure
 * 3 states
 * REST: do nothing (hero not next to creature)
 * RIDDLE: ask riddle
 * ATTACK: attack hero if possible
 * IGNORE: ignore hero
 */
public class RiddleBehaviour implements Behaviour
{
	// riddle and answer
	private static final String RIDDLE_TEXT = "What creature goes on four legs in the morning , two legs at mid-day, and three legs in the evening?";
	private static final String RIDDLE_ANSWER = "man";

	// state of the creature
	enum State  {REST, RIDDLE, ATTACK, IGNORE};
	State state = State.REST;
	
	Creature fCreature = null;

	/**
	 * Constructor
	 * 
	 * @param creature The creature
	 */
	public RiddleBehaviour(Creature creature)
	{
		fCreature = creature;
		state = State.REST;
	}
	

	
	
	/* (non-Javadoc)
	 * @see dungeon.ai.Behaviour#onTick(dungeon.model.Game)
	 */
	public boolean onTick(Game game)
	{
		// my location
		Point2D myLocation = fCreature.getLocation(); 

		//get hero, do nothing if no hero
		Hero hero = game.getHero();
		if (hero == null)
			return false;
		
		// hero location
		Point2D heroLocation = hero.getLocation(); 
		
		// distance between us (edges, not centre), see if in range
		double distance = myLocation.distance(heroLocation) - fCreature.getSize() - hero.getSize();
		boolean inRange = (distance < fCreature.getReach()+1);

		// state transition
		switch (state) {
		case REST: // if in range, move to RIDDLE, otherwise stay
			if (inRange)
				state = State.RIDDLE;
			break;
			
		case RIDDLE:  // ask riddle, move to attack/passive as appropriate
			if (answeredRiddle())
				state = State.IGNORE;
			else
				state = State.ATTACK;
			break;
			
		case ATTACK: case IGNORE:  // move to REST if out of range and I'm not injured, else leave as is
			if (!inRange && (fCreature.getCurrentHealth() >= fCreature.getMaxHealth()))
				state = State.REST;
			break;
		}
		
		// state actions
		switch (state) {
		case REST: case RIDDLE: case IGNORE: // do nothing
			return true;

		case ATTACK: // attack
			if (ActionAttack.performAction(fCreature, game))
				return true;
			return false;  // do nothing
		}
		
		return false;
	}




	/** ask riddle, return true if answer is correct
	 * @return
	 */
	private boolean answeredRiddle() {
		String answer = JOptionPane.showInputDialog(null, RIDDLE_TEXT);
		App.log("Riddle: " + RIDDLE_TEXT);
		if (answer != null)
			App.log("Answer: " + answer);
		if (answer != null && answer.equals(RIDDLE_ANSWER)) {
			JOptionPane.showMessageDialog(null, "You answered the riddle correctly");
			App.log( "You answered the riddle correctly");
			return true;
		}
		else {
			JOptionPane.showMessageDialog(null, "You answered the riddle incorrectly");
			App.log( "You answered the riddle incorrectly");
			return false;
		}

		
	}





	public boolean deathTick(Game game) {
		// TODO Auto-generated method stub
		return false;
	}





	public boolean gameOverTick(Game game) {
		// TODO Auto-generated method stub
		return false;
	}
		

}
