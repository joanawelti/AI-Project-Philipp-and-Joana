package dungeon.ai.ai_code;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import dungeon.App;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;
import dungeon.model.structure.Tile;
import dungeon.ui.MapPanel;
import dungeon.ui.MappedPoint;
import dungeon.ui.node.SearchNode;
import dungeon.ui.node.aStarNode;

public class PathFinding {
	int xArraySize = 20;
	int yArraySize = 20;
	
	Creature fCreature = null;
	boolean [][] map = null;
	
	public PathFinding(Creature creature) {
		fCreature = creature;
		
	}
	private void getMap(Game game){
		if(map != null){
			return;
		}
		xArraySize = 20;
		yArraySize = 20;
		map = new boolean[xArraySize][yArraySize];
		double tileSize = 5;
		
		//populate array by query
		for(int y = 0; y < map.length; y++){
			for(int x = 0; x < map.length; x++){
				Point2D.Double location = new Point2D.Double(2.5+x*tileSize, 2.5+y*tileSize);
				Tile tile = App.getGame().getMap().getTileAt(location);
				if(tile != null){
					map[x][y] = tile.canOccupy(fCreature);
				}else{
					map[x][y] = false;
				}
				System.out.print(map[x][y]+" ");
			}
			System.out.println("");
		}
		System.out.println("\n");
	}
	
	public List<Point2D> pathfindDepth(Game game, Point2D goal){
		getMap(game);
		//Search
		Stack<SearchNode> openList = new Stack<SearchNode>();
		ArrayList<SearchNode> closedList = new ArrayList<SearchNode>();
		
		//Map the point from the game model, to the search grid
		System.out.println("Start Search.......");
		MappedPoint mappedGoal = getMappedPoint(goal);
		SearchNode currentNode = new SearchNode(getMappedPoint(fCreature.getLocation()));
		openList.push(currentNode);
		while(!currentNode.equals(mappedGoal)){
			currentNode = openList.pop();
			System.out.println( currentNode + "popped of the stack");
			if(currentNode.equals(mappedGoal)){
				closedList.add(currentNode);
				break;
			}else{
				closedList.add(currentNode);
				System.out.println(currentNode + "closed");
				ArrayList<MappedPoint> neighbours = currentNode.getNeighbours(map, mappedGoal);
				for(MappedPoint point : neighbours){
					if(!(isPointInList(openList, point) || isPointInList(closedList, point))){
						SearchNode node = new SearchNode(point, currentNode);
						openList.add(node);
						System.out.println(node +" added to stack");
					}
				}
			}
			
		}
		
		List<Point2D> waypoints = new ArrayList<Point2D>();
		while(!currentNode.getParent().isRoot()){
			waypoints.add(getUnmappedPoint(currentNode));
			currentNode = currentNode.getParent();
			//System.out.println("X: " + currentNode.getX() + " Y: " + currentNode.getY());
		}
//		MapPanel.setPath(waypoints);
		return waypoints;
	}
	
	public List<Point2D> PathFindAStar(Game game, Point2D goal){
		System.out.println("called astar");
		getMap(game);
		
		ArrayList<aStarNode> closedList = new ArrayList<aStarNode>();
		PriorityQueue<aStarNode> openList = new PriorityQueue<aStarNode>();
		
		MappedPoint mappedGoal = getMappedPoint(goal);
		aStarNode currentNode = new aStarNode(getMappedPoint(fCreature.getLocation()));
		openList.add(currentNode);
		
		System.out.println("inside astar: ready to pathfind");
		while(!currentNode.equals(mappedGoal)){
			currentNode = openList.poll();// Retrieves and removes the head of this queue, or null  if this queue is empty.
			if(currentNode.equals(mappedGoal)){
				break;
			}
			closedList.add(currentNode);
			
			for(aStarNode node : currentNode.getNeighbours(map, mappedGoal)){
				boolean inClosed = isPointInList(closedList, node);
				boolean inOpen = isPointInList(openList, node);
				
				if(inOpen && 
						(currentNode.getGScore()+currentNode.getDistance(node)) < node.getGScore()){
					openList.remove(node);
					node.setParent(currentNode);
					node.setGScore(currentNode,node);
					openList.add(node);
				}
				else 
					if(!inOpen && !inClosed) {
						node.setParent(currentNode);
						node.setGScore(currentNode,node);
					openList.add(node);
				}
			}
		}
		System.out.println("ready to do waypoints");
		
		List<Point2D> waypoints = new ArrayList<Point2D>();

		for (;currentNode != null; currentNode = currentNode.getParent()) {
			Point2D point=getUnmappedPoint(currentNode);
			System.out.println(point+"X: " + currentNode.getX() + " Y: " + currentNode.getY());
			waypoints.add(point);
		}
//		MapPanel.setPath(waypoints);
		return waypoints;
	}
	
	private MappedPoint getMappedPoint(Point2D realPoint){
		int xPos = (int) (((realPoint.getX() - 2.5) / 5) + 0.5);
		int yPos = (int) (((realPoint.getY() - 2.5) / 5) + 0.5);
		return new MappedPoint(xPos, yPos);
	}
	
	private Point2D getUnmappedPoint(MappedPoint p){
		double x = p.getX()*5 + 2.5;
		double y = p.getY()*5 + 2.5;
//		System.out.println("X: " + x + " Y: " + y);
		return new Point2D.Double(x, y);
	}
	
	private Point2D getUnmappedPoint(aStarNode p){
		double x = p.getX()*5 + 2.5;
		double y = p.getY()*5 + 2.5;
//		System.out.println("X: " + x + " Y: " + y);
		return new Point2D.Double(x, y);
	}
	
	
	private boolean isPointInList(Iterable<? extends MappedPoint> it, MappedPoint point){
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
	private boolean isPointInList(Iterable<? extends aStarNode> it, aStarNode node){
		for(aStarNode n : it){
			if(node.equals(n)){
				return true;
			}
		}
		return false;
	}
}
