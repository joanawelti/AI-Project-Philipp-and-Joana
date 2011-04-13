package dungeon.ai.pathfind;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import dungeon.ai.pathfind.utilities.MappedPoint;
import dungeon.ai.pathfind.utilities.SearchNode;
import dungeon.model.Game;
import dungeon.model.items.mobs.Creature;
import dungeon.ui.MapPanel;

public class PathFindDepth extends PathFind{

	public PathFindDepth(Creature creature) {
		super(creature);
		// TODO Auto-generated constructor stub
	}
	
	public List<Point2D> findPath(Game game, Point2D goal){
		recalcMap(game);
		//Search
		Stack<SearchNode> openList = new Stack<SearchNode>();
		ArrayList<SearchNode> closedList = new ArrayList<SearchNode>();
		
		//Map the point from the game model, to the search grid
//		System.out.println("Start Search.......");
		MappedPoint mappedGoal = getMappedPoint(goal);
		SearchNode currentNode = new SearchNode(getMappedPoint(fCreature.getLocation()));
		openList.push(currentNode);
		while(!currentNode.equals(mappedGoal)){
			currentNode = openList.pop();
//			System.out.println( currentNode + "popped of the stack");
			if(currentNode.equals(mappedGoal)){
				closedList.add(currentNode);
				break;
			}else{
				closedList.add(currentNode);
//				System.out.println(currentNode + "closed");
				ArrayList<MappedPoint> neighbours = currentNode.getNeighbours(map, mappedGoal);
				for(MappedPoint point : neighbours){
					if(!(isPointInList(openList, point)  || isPointInList(closedList, point))){
						SearchNode node = new SearchNode(point, currentNode);
						openList.add(node);
//						System.out.println(node +" added to stack");
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
		MapPanel.setPath(waypoints);
		return waypoints;
	}

}
