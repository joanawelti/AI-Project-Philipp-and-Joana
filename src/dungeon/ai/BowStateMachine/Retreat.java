package dungeon.ai.BowStateMachine;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;

public class Retreat extends State{
	Point2D restingPoint = null;
	public Retreat(Creature creature) {
		super(creature);
	}

	@Override
	boolean move(Game game) {
		if(restingPoint == null){
			restingPoint = getRestingPoint(game);
			fCreature.setGoal(getRestingPoint(game), game);
		}
		
		boolean moved = fCreature.moveToGoal(game);
		if(!moved){
			fCreature.setBehaviour(new RestState(fCreature));
		}
		return moved;
	}
	
	private Point2D getRestingPoint(Game game){
		Rectangle2D area = game.getMap().getTileAt(fCreature.getLocation()).getArea();
		Point2D center = new Point2D.Double(area.getCenterX(), area.getCenterY());
		return center;
	}

	public boolean deathTick(Game game) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean gameOverTick(Game game) {
		// TODO Auto-generated method stub
		return false;
	}
	
//	private Point2D getRandomPoint(Game game){
//
//	}

}
