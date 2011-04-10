package dungeon.ai.pathfind.utilities;

import java.util.ArrayList;
import java.util.List;


public class bestFirstNode implements Comparable<bestFirstNode> {
	int x = 0;
	int y = 0;
	private bestFirstNode parent = null;
	private int hScore = 0;
	private int gScore = 0;

	public bestFirstNode(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public bestFirstNode(int x, int y, bestFirstNode parent) {
		this(x, y);
		setParent(parent);
	}

	public bestFirstNode(MappedPoint point) {
		this(point.getX(), point.getY());
	}

	public bestFirstNode(MappedPoint point, bestFirstNode parent) {
		this(point);
		setParent(parent);
	}

	public boolean isRoot() {
		return parent == null;
	}

	public bestFirstNode getParent() {
		return parent;
	}

	public void setParent(bestFirstNode parent) {
		this.parent = parent;
		gScore = parent.getgScore();
		if (parent.getX() == getX() || parent.getY() == getY()) {
			gScore += 10;
		} else {
			gScore += 14;
		}
		
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public boolean equals(Object o){
		if(o instanceof MappedPoint){
			return equals((MappedPoint)o);
		}	
		if(o instanceof bestFirstNode){
			return equals((bestFirstNode)o);
		}
		return super.equals(o);
	}
	
	public boolean equals(MappedPoint point) {
		return x == point.getX() && y == point.getY();
	}

	public boolean equals(bestFirstNode node) {
		return x == node.getX() && y == node.getY();
	}

	public int compareTo(bestFirstNode o) {
		if (this.getFScore() < o.getFScore()) {
			return -1;
		}
		if (this.getFScore() > o.getFScore()) {
			return 1;
		}
		return 0;
	}

	public int gethScore() {
		return hScore;
	}

	public void setHScore(int hScore) {
		this.hScore = hScore;
	}

	public int getgScore() {
		return gScore;
	}

	public void setgScore(int gScore) {
		this.gScore = gScore;
	}

	public void setgScore(bestFirstNode parent, bestFirstNode node){
		if (parent==null)node.setgScore(0);else
		 node.setgScore(parent.getgScore() + node.getDistance(parent));
		 return;
	}
	
	public int getFScore() {
		return hScore;//cobble to make it best first , i.e. no + gScore;
	}

	public int getDistance(bestFirstNode node) {
		if (this.getX() == node.getX() || this.getY() == node.getY()) {
			return 10;
		} else {
			return 14;
		}
	}

	public List<bestFirstNode> getNeighbours(boolean[][] map, MappedPoint goal){
		int xArraySize = map.length;
		int yArraySize = map[0].length;
		ArrayList<bestFirstNode> list = new ArrayList<bestFirstNode>();
//		upper left
		if(x > 0 && y > 0 && map[x][y-1]&& map[x-1][y]){
			list.add(new bestFirstNode(x-1, y-1, this));
		}
//		upper middle
		if(y > 0){
			list.add(new bestFirstNode(x, y-1, this));
		}
		//upper right
		if(y > 0 && x < xArraySize - 1 && map[x][y-1] && map[x+1][y]){
			list.add(new bestFirstNode(x+1, y-1, this));
		}
		//left
		if(x > 0){
			list.add(new bestFirstNode(x-1, y, this));
		}
		//right
		if(x < xArraySize - 1){
			list.add(new bestFirstNode(x+1, y, this));
		}
		//down left
		if(y < yArraySize - 1 && x > 0 && map[x-1][y] && map[x][y+1]){
			list.add(new bestFirstNode(x-1, y+1, this));
		}
		//down middle
		if(y < yArraySize -1){
			list.add(new bestFirstNode(x, y+1, this));
		}
		//down right
		if(y < yArraySize - 1 && x < xArraySize -1 && map[x+1][y] && map[x][y+1]){
			list.add(new bestFirstNode(x+1, y+1, this));
		}
		
		for(int i = 0; i < list.size(); i++){
			list.get(i).setHScore(goal);
			if(!map[list.get(i).getX()][list.get(i).getY()]){
				list.remove(i);
				i--;
			}
		}
		
		return list;
	}

	private void setHScore(MappedPoint goal){
		 int xDistance = Math.abs(goal.getX() - this.getX());
		 int yDistance = Math.abs(goal.getY() - this.getY());
		 this.setHScore(xDistance + yDistance);
		 return;
	}
}
