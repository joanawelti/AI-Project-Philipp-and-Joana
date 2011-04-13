package dungeon.ai.BowStateMachine;

import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;

public class RestState extends State{

	public RestState(Creature creature) {
		super(creature);
		// TODO Auto-generated constructor stub
	}

	@Override
	boolean move(Game game) {
		if(isHeroInRoom(game) && isEnergySufficient(game)){
			fCreature.setBehaviour(new AttackState(fCreature));
		}
		return fCreature.moveToGoal(game);
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
