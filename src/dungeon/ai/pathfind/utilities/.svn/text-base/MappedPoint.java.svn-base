package dungeon.ai.pathfind.utilities;

import java.util.ArrayList;
import java.util.Collections;

public class MappedPoint implements Comparable<MappedPoint>{
	int x = 0;
	int y = 0;
	double distance = 0.0;
	
	public MappedPoint(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public MappedPoint(int x, int y, MappedPoint goal){
		this(x, y);
		double xx = (x - goal.getX());
		double yy = (y - goal.getY());
		distance = Math.sqrt(Math.pow(xx, 2)+Math.pow(yy, 2));
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
	
	public ArrayList<MappedPoint> getNeighbours(boolean[][] map, MappedPoint goal){
		int xArraySize = map.length;
		int yArraySize = map[0].length;
		ArrayList<MappedPoint> list = new ArrayList<MappedPoint>();
		//upper left
//		if(x > 0 && y > 0){
//			list.add(new MappedPoint(x-1, y-1));
//		}
//		upper middle
		if(y > 0){
			list.add(new MappedPoint(x, y-1, goal));
		}
		//upper right
//		if(y > 0 && x < xArraySize - 1){
//			list.add(new MappedPoint(x+1, y-1));
//		}
		//left
		if(x > 0){
			list.add(new MappedPoint(x-1, y, goal));
		}
		//right
		if(x < xArraySize - 1){
			list.add(new MappedPoint(x+1, y, goal));
		}
		//down left
//		if(y < yArraySize - 1 && x > 0){
//			list.add(new MappedPoint(x-1, y+1));
//		}
		//down middle
		if(y < yArraySize -1){
			list.add(new MappedPoint(x, y+1, goal));
		}
		//down right
//		if(y < yArraySize - 1 && x < xArraySize -1 ){
//			list.add(new MappedPoint(x+1, y+1));
//		}
		
		for(int i = 0; i < list.size(); i++){
			if(!map[list.get(i).getX()][list.get(i).getY()]){
				list.remove(i);
				i--;
			}
		}
		Collections.sort(list);
		return list;
	}
	
	public boolean equals(MappedPoint point){
		return (x == point.getX() && y == point.getY());
	}

	public int compareTo(MappedPoint o) {

		if(distance > o.getDistance()){
			return -1;
		}
		if(distance < o.getDistance()){
			return 1;
		}
		return 0;
	}
	
	public String toString(){
		return "(" + getX() + "," + getY() + "distance:" + distance +")";
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public double getDistance(MappedPoint point){
		
		double x = Math.abs(this.getX() - point.getX());
		double y = Math.abs(this.getY() - point.getY());
		
		return Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));		
	}
}
