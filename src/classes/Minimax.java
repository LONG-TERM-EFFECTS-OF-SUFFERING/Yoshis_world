package src.classes;

import java.util.ArrayList;
import java.util.List;

import src.classes.Game.Difficulty;
import src.classes.GameState.Player;
import src.classes.Node.Type;


public class Minimax {
	private ArrayList <Node> list = new ArrayList<>();
	private Game game;
	private int max_depth;
	private Node root;


	public Minimax(Game game) {
		this.game = game;
		Difficulty difficulty = game.get_difficulty();

		if (difficulty == Difficulty.NORMAL)
			max_depth = 1;
		else if (difficulty == Difficulty.MEDIUM)
			max_depth = 4;
		else
			max_depth = 6;
	}

	/**
	 * Expands a given node in the search tree.
	 *
	 * @param node the node to expand.
	 */
	private void expand_node(Node node) {
		int depth = node.get_depth();

		if (depth <= max_depth) {
			GameState game_state = node.get_game_state();
			Player player;
			Type type;

			if (node.get_type() == Type.MAX) {
				player = Player.RED;
				type = Type.MIN;
			} else if (node.get_type() == Type.MIN) {
				player = Player.GREEN;
				type = Type.MAX;
			} else
				throw new IllegalArgumentException("Error: invalid player");

			if (game.is_game_finished(game_state) || depth == max_depth) { // is_leaf_node(node)
				float heuristic = apply_heuristic(game_state);
				node.set_utility(heuristic);
			} else {
				List <Coordinate> available_tiles = game.get_available_tiles(player, game_state);
				for (Coordinate tile : available_tiles) {
					GameState new_game_state = game.play(player, tile,node.get_game_state().copy());
					list.add(new Node(new_game_state, node, node.get_depth() + 1 , type));
				}
			}
		}
	}

	/**
	 * Applies a heuristic function to evaluate the given game state.
	 *
	 * @param game_state The game state to evaluate.
	 * @return the heuristic value for the given game state.
	 */
	private float apply_heuristic(GameState game_state) {
		return 1;
	}

	/**
	 * Executes the Minimax algorithm to determine the best move.
	 *
	 * @param game_state The current game state.
	 * @return the best move determined by the Minimax algorithm.
	 */
	public Coordinate run(GameState game_state) {
		root = new Node(game_state, null, 0, Type.MAX);

		list.add(root);

		for (int i = 0; i < list.size(); i++) {
			expand_node(list.get(i));
		}

		list.reversed();

		Coordinate best_move = null;

		for (int i = 0; i < list.size(); i++) {
			Node node = list.get(i);
			float node_utility = node.get_utility();

			Node parent = node.get_parent();

			if (parent != null) {
				float parent_utility = parent.get_utility();

				if (parent.get_type() == Type.MAX)
					if (node_utility > parent_utility) {
						parent.set_utility(node_utility);
						if (parent == root)
							best_move = node.get_game_state().get_player(Player.RED); // The sons
							// of the root will always (if it is possible) move the RED player (machine)
					}
				else
					if (node_utility < parent_utility) {
						parent.set_utility(node_utility);
						if (parent == root)
							best_move = node.get_game_state().get_player(Player.RED);
					}
			}
		}

		return best_move;
	}
}
