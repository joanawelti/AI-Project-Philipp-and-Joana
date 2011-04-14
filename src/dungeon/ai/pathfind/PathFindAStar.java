package dungeon.ai.pathfind;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import dungeon.ai.pathfind.utilities.MappedPoint;
import dungeon.ai.pathfind.utilities.aStarNode;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;

public class PathFindAStar extends PathFind{

	public PathFindAStar(Creature creature) {
		super(creature);
		// TODO Auto-generated constructor stub
	}
	
	public List<Point2D> findPath(Game game, Point2D goal){
		recalcMap(game);
		
		ArrayList<aStarNode> closedList = new ArrayList<aStarNode>();
		PriorityQueue<aStarNode> openList = new PriorityQueue<aStarNode>();
		
		MappedPoint mappedGoal = getMappedPoint(goal);
		aStarNode currentNode = new aStarNode(getMappedPoint(fCreature.getLocation()));
		openList.add(currentNode);
		
		while(!currentNode.equals(mappedGoal)){
//			System.out.println("Closed List size : " + closedList.size());
			currentNode = openList.poll();
			if(currentNode==null){
				return null;
			}
			if(currentNode.equals(mappedGoal)){
				break;
			}
			closedList.add(currentNode);
			
			for(aStarNode node : currentNode.getNeighbours(map, mappedGoal)){
				boolean inClosed = isPointInList(closedList, node);
				boolean inOpen = isPointInList(openList, node);
				
				if(inOpen && 
						(currentNode.getgScore()+currentNode.getDistance(node)) < node.getgScore()){
					openList.remove(node);
					node.setParent(currentNode);
					node.setgScore(currentNode,node);
					openList.add(node);
				}
				else 
					if(!inOpen && !inClosed) {
						node.setParent(currentNode);
						node.setgScore(currentNode,node);
					openList.add(node);
				}
			}
		}
		
		List<Point2D> waypoints = new ArrayList<Point2D>();
		
		while(currentNode != null){
			waypoints.add(getUnmappedPoint(currentNode));
			currentNode = currentNode.getParent();
		}


//		MapPanel.setPath(waypoints);
//		if(waypoints.size()>0 && !LineOfSight.exists(fCreature.getLocation(), waypoints.get(waypoints.size()-1), game.getMap())){
//			System.out.println("No line of sight");
//		}
		return waypoints;
	}
	

}
