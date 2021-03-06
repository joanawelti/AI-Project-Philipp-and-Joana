package dungeon.ai.pathfind;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import dungeon.ai.LineOfSight;
import dungeon.ai.pathfind.utilities.MappedPoint;
import dungeon.ai.pathfind.utilities.aStarNode;
import dungeon.ai.pathfind.utilities.bestFirstNode;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;
import dungeon.ui.MapPanel;

public class PathFindHillClimb extends PathFind{

	public PathFindHillClimb(Creature creature) {
		super(creature);
		// TODO Auto-generated constructor stub
	}
	
	public List<Point2D> findPath(Game game, Point2D goal){
		recalcMap(game);
		
		ArrayList<bestFirstNode > closedList = new ArrayList<bestFirstNode >();
		PriorityQueue<bestFirstNode > openList = new PriorityQueue<bestFirstNode >();
		
		MappedPoint mappedGoal = getMappedPoint(goal);
		bestFirstNode currentNode = new bestFirstNode (getMappedPoint(fCreature.getLocation()));
		openList.add(currentNode);
		
		while(!currentNode.equals(mappedGoal)){
//			System.out.println("Closed List size : " + closedList.size());
			if(openList.isEmpty())break;
			currentNode = openList.poll();
			while(!openList.isEmpty()){
				closedList.add(openList.remove());//cobble search for naive hillclimb
			}
			if(currentNode==null){
				break;
			}
			if(currentNode.equals(mappedGoal)){
				break;
			}
			closedList.add(currentNode);
			
			for(bestFirstNode node : currentNode.getNeighbours(map, mappedGoal)){
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
		System.out.println(waypoints.size());


//		MapPanel.setPath(waypoints);
//		if(waypoints.size()>0 && !LineOfSight.exists(fCreature.getLocation(), waypoints.get(waypoints.size()-1), game.getMap())){
//			System.out.println("No line of sight");
//		}
		return waypoints;
	}
	

}
