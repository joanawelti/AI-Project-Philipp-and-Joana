package dungeon.ai.pathfind.utilities;



public class SearchNode extends MappedPoint{
	private SearchNode parent = null;

	public SearchNode(MappedPoint node, SearchNode parent) {
		super(node.getX(), node.getY());
		setDistance(node.getDistance());
		this.parent = parent;
	}
	
	public SearchNode(int x, int y){
		super(x, y);
		parent = this;
	}
	
	public SearchNode(MappedPoint point){
		this(point.getX(), point.getY());
	}

	public SearchNode getParent() {
		return parent;
	}

	public void setParent(SearchNode parent) {
		this.parent = parent;
	}
	
	public boolean isRoot(){
		return this == parent;
	}
	

	
}
