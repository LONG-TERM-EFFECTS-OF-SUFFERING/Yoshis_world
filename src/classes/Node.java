package src.classes;

public class Node {
	private Type type;
	private int depth;
	private int utility;


	public Node(Type type, int depth, int utility) {
		this.type = type;
		this.depth = depth;
		this.utility = utility;
	}


	static private enum Type {
		MIN,
		MAX
	}
}
