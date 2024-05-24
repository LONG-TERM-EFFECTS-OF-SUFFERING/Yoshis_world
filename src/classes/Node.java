package src.classes;

import src.classes.GameState.Player;

public class Node {
	private GameState game_state;
	private Node parent;
	private int depth;
	private float utility;
	private Type type;


	public Node(GameState game_state, Node parent, int depth, Type type) {
		this.game_state = game_state;
		this.parent = parent;
		this.depth = depth;
		this.type = type;

		if (type == Type.MAX)
			utility = Integer.MIN_VALUE;
		else
			utility = Integer.MAX_VALUE;
	}


	/**
	 * Returns the game state associated with this node.
	 *
	 * @return the game state associated with this node.
	 */
	public GameState get_game_state() {
		return game_state;
	}

	/**
	 * Returns the parent node of this node.
	 *
	 * @return the parent node of this node.
	 */
	public Node get_parent() {
		return parent;
	}

	/**
	 * Returns the depth of this node in the game tree.
	 *
	 * @return the depth of this node.
	 */
	public int get_depth() {
		return depth;
	}

	/**
	 * Returns the utility value of this node.
	 *
	 * @return the utility value of this node.
	 */
	public float get_utility() {
		return utility;
	}

	/**
	 * Returns the type of node (MIN or MAX).
	 *
	 * @return the type of node.
	 */
	public Type get_type() {
		return type;
	}

	/**
	 * Sets the utility value of this node.
	 *
	 * @param utility the utility value to set.
	 */
	public void set_utility(float utility) {
		this.utility = utility;
	}

	/**
	 * Returns a string representation of the Node object.
	 * The string includes the values of the human player, machine player, depth, utility, and type.
	 *
	 * @return a string representation of the Node object.
	 */
	@Override
	public String toString() {
		return "Node{" +
				"\n\thuman = " + game_state.get_player(Player.RED) +
				"\n\tmachine = " + game_state.get_player(Player.GREEN) +
				"\n\tdepth = " + depth +
				"\n\tutility = " + utility +
				"\n\ttype = " + type +
				"\n}";
	}


	static public enum Type {
		MIN,
		MAX
	}
}
