package dungeon.ui.node;

import java.util.ArrayList;
import java.util.List;

import dungeon.ui.MappedPoint;

public class aStarNode implements Comparable<aStarNode> {
	int x = 0;
	int y = 0;
	private aStarNode parent = null;
	private int hScore = 0;
	private int gScore = 0;

	public aStarNode(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public aStarNode(int x, int y, aStarNode parent) {
		this(x, y);
		setParent(parent);
	}

	public aStarNode(MappedPoint point) {
		this(point.getX(), point.getY());
	}

	public aStarNode(MappedPoint point, aStarNode parent) {
		this(point);
		setParent(parent);
	}

	public boolean isRoot() {
		return parent == null;
	}

	public aStarNode getParent() {
		return parent;
	}

	public void setParent(aStarNode parent) {
		this.parent = parent;
		if (parent.getX() == getX() || parent.getY() == getY()) {
			hScore = 10;
		} else {
			hScore = 14;
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

	public boolean equals(MappedPoint point) {
		return x == point.getX() && y == point.getY();
	}

	public boolean equals(aStarNode node) {
		return x == node.getX() && y == node.getY();
	}

	public int compareTo(aStarNode o) {
		if (this.getFScore() < o.getFScore()) {
			return -1;
		}
		if (this.getFScore() > o.getFScore()) {
			return 1;
		}
		return 0;
	}

	public int getHScore() {
		return hScore;
	}

	public void setHScore(int hScore) {
		this.hScore = hScore;
	}

	public int getGScore() {
		return gScore;
	}

	public void setGScore(int gScore) {
		this.gScore = gScore;
	}

	public int getFScore() {
		return hScore + gScore;
	}

	public int getDistance(aStarNode node) {
		if (this.getX() == node.getX() || this.getY() == node.getY()) {
			return 10;
		} else {
			return 14;
		}
	}

	public List<aStarNode> getNeighbours(boolean[][] map, MappedPoint goal){
		int xArraySize = map.length;
		int yArraySize = map[0].length;
		ArrayList<aStarNode> list = new ArrayList<aStarNode>();
//		upper left
		if(x > 0 && y > 0 && map[x][y-1]&& map[x-1][y]){
			if(map[x][y])
				list.add(new aStarNode(x-1, y-1, this));
		}
//		upper middle
		if(y > 0){
			if(map[x][y])
			list.add(new aStarNode(x, y-1, this));
		}
		//upper right
		if(y > 0 && x < xArraySize - 1 && map[x][y-1] && map[x+1][y]){
			if(map[x][y])
			list.add(new aStarNode(x+1, y-1, this));
		}
		//left
		if(x > 0){
			if(map[x][y])
			list.add(new aStarNode(x-1, y, this));
		}
		//right
		if(x < xArraySize - 1){
			if(map[x][y])
			list.add(new aStarNode(x+1, y, this));
		}
		//down left
		if(y < yArraySize - 1 && x > 0 && map[x-1][y] && map[x][y+1]){
			if(map[x][y])
			list.add(new aStarNode(x-1, y+1, this));
		}
		//down middle
		if(y < yArraySize -1){
			if(map[x][y])
			list.add(new aStarNode(x, y+1, this));
		}
		//down right
		if(y < yArraySize - 1 && x < xArraySize -1 && map[x+1][y] && map[x][y+1]){
			if(map[x][y])
			list.add(new aStarNode(x+1, y+1, this));
		}
		
		for(int i = 0; i < list.size(); i++){
			list.get(i).setHScore(goal, list.get(i));
			list.get(i).setGScore(this.parent, list.get(i));
		}
		
		return list;
	}

	private void setHScore(MappedPoint goal, aStarNode node){
		 int xDistance = 10*Math.abs(goal.getX() - node.getX());
		 int yDistance = 10*Math.abs(goal.getY() - node.getY());
		 node.setHScore(xDistance + yDistance);
		 return;
	}
	
	public void setGScore(aStarNode parent, aStarNode node){
		if (parent==null)node.setGScore(0);else
		 node.setGScore(parent.getGScore() + node.getDistance(parent));
		 return;
	}
}
