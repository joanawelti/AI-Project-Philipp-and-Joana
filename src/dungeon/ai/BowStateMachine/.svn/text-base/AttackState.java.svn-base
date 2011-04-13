package dungeon.ai.BowStateMachine;

import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;

public class AttackState extends State{

	public AttackState(Creature creature) {
		super(creature);
		// TODO Auto-generated constructor stub
	}

	@Override
	boolean move(Game game) {
		fCreature.setGoal(null, game);
		if(isEnergySufficient(game)){
			if(isHeroInRoom(game)){
				if(isHeroInReach(game)){
					//just attack
				}else{
					//walk towards hero
					fCreature.setGoal(game.getHero().getLocation(), game);
				}
			}else{
				//if hero is not in the room rest
				fCreature.setBehaviour(new RestState(fCreature));
			}
		}else{
			//if energy is not sufficient for long range retreat
			fCreature.setBehaviour(new Retreat(fCreature));
			
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
