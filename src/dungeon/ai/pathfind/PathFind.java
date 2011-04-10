package dungeon.ai.pathfind;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Vector;

import dungeon.App;
import dungeon.ai.Behaviour;
import dungeon.ai.CollisionDetection;
import dungeon.ai.pathfind.utilities.MappedPoint;
import dungeon.ai.pathfind.utilities.aStarNode;
import dungeon.ai.pathfind.utilities.bestFirstNode;
import dungeon.model.Game;
import dungeon.model.items.Item;
import dungeon.model.items.mobs.Creature;
import dungeon.model.items.mobs.Mob;
import dungeon.model.structure.Tile;
import dungeon.ui.MapPanel;

public abstract class PathFind {

	Creature fCreature = null;
	boolean[][] map = null;
	boolean[][] tilemap = null;
	boolean mapInitialised=false;
	int xArraySize = 25;
	int yArraySize = 25;
	double tileSize = 5;
	double halfTileSize = tileSize / 2;

	public PathFind(Creature creature) {
		fCreature = creature;

			tilemap = new boolean[xArraySize][yArraySize];
			map = new boolean[xArraySize][yArraySize];

	}

	 abstract public List<Point2D> findPath(Game game, Point2D goal);

	public void printMap(Game game) {

		System.out.println("map");
		// populate array by query
		for (int y = 0; y < yArraySize; y++) {
			for (int x = 0; x < xArraySize; x++) {
				int printnum = 0;
				if (map[x][y])
					printnum = 1;
				System.out.print(printnum);
			}
			System.out.println("");
		}
		System.out.println("\n");
		System.out.println("tilemap");
		// populate array by query
		for (int y = 0; y < yArraySize; y++) {
			for (int x = 0; x < xArraySize; x++) {
				int printnum = 0;
				if (tilemap[x][y])
					printnum = 1;
				System.out.print(printnum);
			}
			System.out.println("");
		}
		System.out.println("\n");
	}

	public void recalcMap(Game game) {

		if (!mapInitialised) {
			mapInitialised=true;


			// System.out.println("map");
			// populate array by query
			for (int y = 0; y < yArraySize; y++) {
				for (int x = 0; x < xArraySize; x++) {
					Point2D.Double location = new Point2D.Double(halfTileSize
							+ x * tileSize, halfTileSize + y * tileSize);
					Tile tile = App.getGame().getMap().getTileAt(location);
					if (tile != null) {
						tilemap[x][y] = tile.canOccupy(fCreature);
						// CollisionDetection.canOccupy(game,fCreature,location);

					} else {
						tilemap[x][y] = false;
					}
					// System.out.print(map[x][y]+" ");
				}
				// System.out.println("");
			}
			// System.out.println("\n");
			// set own location to true
			// MappedPoint ownLocation=getMappedPoint(fCreature.getLocation());
			// map[ownLocation.getX()][ownLocation.getY()] = true;
		copyarray();
		
		}

	}
	public void recalcMapWithoutMobs(Game game) {
		copyarray();
}
	public void recalcMapWithMobs(Game game) {
		// Make sure we're not trying to walk over another mob
		copyarray();
		Vector<Mob> mobs = new Vector<Mob>();
		mobs.addAll(game.getCreatures());

		if (game.getHero() != null)
			mobs.add(game.getHero());

		for (Mob mob : mobs) {
			if (fCreature == mob)
				continue;
			MappedPoint mobLocation = getMappedPoint(mob.getLocation());
			Rectangle2D item_rect = Item.getShape(
					getUnmappedPoint(mobLocation), 0.01);// fCreature.getSize()
			if (item_rect.intersects(mob.getShape()))
				map[mobLocation.getX()][mobLocation.getY()] = false;
		}

	}
	
	public void copyarray(){

				for (int y = 0; y < yArraySize; y++) {
				for (int x = 0; x < xArraySize; x++) {
				map[x][y]=tilemap[x][y];}}
	
	}
	
	/**
	 * Returns the element number of the node if it is in the list and -1 if it is not in the list
	 * @param it
	 * @param node
	 * @return
	 */
	
	protected boolean isPointInList(Iterable<? extends MappedPoint> it, MappedPoint point){
		for(MappedPoint p : it){
			if(point.equals(p)){
				return true;
			}
		}
		return false;
	}
	/**
	 * Returns the element number of the node if it is in the list and -1 if it is not in the list
	 * @param it
	 * @param node
	 * @return
	 */
	public boolean isPointInList(Iterable<? extends aStarNode> it, aStarNode node){
		for(aStarNode n : it){
			if(node.equals(n)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isPointInList(Iterable<? extends bestFirstNode> it, bestFirstNode node){
		for(bestFirstNode n : it){
			if(node.equals(n)){
				return true;
			}
		}
		return false;
	}
	
	
	protected MappedPoint getMappedPoint(Point2D realPoint){
		int xPos = (int) (Math.round((realPoint.getX() - halfTileSize) / tileSize) );
		int yPos = (int) (Math.round((realPoint.getY() - halfTileSize) / tileSize) );
		return new MappedPoint(xPos, yPos);
	}
	
	protected Point2D getUnmappedPoint(MappedPoint p){
		double x = p.getX()*tileSize + halfTileSize;
		double y = p.getY()*tileSize + halfTileSize;
		return new Point2D.Double(x, y);
	}
	
	protected Point2D getUnmappedPoint(aStarNode p){
		double x = p.getX()*tileSize + halfTileSize;
		double y = p.getY()*tileSize + halfTileSize;
		return new Point2D.Double(x, y);
	}
	
	protected Point2D getUnmappedPoint(bestFirstNode p){
		double x = p.getX()*tileSize + halfTileSize;
		double y = p.getY()*tileSize + halfTileSize;
		return new Point2D.Double(x, y);
	}
	
	protected Point2D getHidingPlace(){
		return null;
	}
	
	public List<Point2D> evadeHazard(Game game, Point2D hazard2D){
		if(map==null){
			recalcMap(game);
		}
		MappedPoint hazard = getMappedPoint(hazard2D);
		MappedPoint safePlace = new MappedPoint(0, 0);
		for(int y = -1; y < 2; y++){
			for(int x = -1; x<2; x++){
				try{
				map[x + hazard.getX()][y + hazard.getY()] = false;
				}catch(ArrayIndexOutOfBoundsException e){
					//nothing to do
				}catch(NullPointerException e){
					
				}
			}
		}
		
		MappedPoint temp;
		for(int y= 0; y < map[0].length; y++){
			for(int x= 0; x < map.length; x++){
				if (map[x][y]) {
					temp = new MappedPoint(x, y);
					if (temp.getDistance(hazard) > safePlace
							.getDistance(hazard)) {
						safePlace = temp;
					}
				}
			}
		}
		List<Point2D> wayPoints = findPath(game, getUnmappedPoint(safePlace));
//		map = null;
		mapInitialised=false;

		recalcMap(game);
		return wayPoints;
		
	}
	
	public Point2D findBestWayPoint(Game game, 	List<Point2D> wayPoints) {
		Point2D goal1 = wayPoints.get(wayPoints.size()-1);
		wayPoints.remove(wayPoints.size()-1);
		if(wayPoints.size()==0)return goal1;
		Point2D goal2 = wayPoints.get(wayPoints.size()-1);
		//if we're already at goal 1 then skip it
		if(fCreature.getLocation().distance(goal1)<1.5*fCreature.getSpeed())return goal2;
		double dx = goal1.getX() - fCreature.getLocation().getX();
		double dy = goal1.getY() - fCreature.getLocation().getY();
		double theta1 = Math.atan2(dy,dx );
		if(theta1<0)theta1+=2*Math.PI;
		dx = goal2.getX() - fCreature.getLocation().getX();
		dy = goal2.getY() - fCreature.getLocation().getY();
		double theta2 = Math.atan2(dy, dx);
		if(theta2<0)theta2+=2*Math.PI;
//		System.out.println(theta1+" - "+theta2);
		double angleDif=Math.abs(theta1-theta2);
		if (angleDif>2.0*Math.PI/3.0&&angleDif<4.0*Math.PI/3.0){
			wayPoints.remove(wayPoints.size()-1);
			return goal2;			
		}
		else
		return goal1;
	}
	

}
