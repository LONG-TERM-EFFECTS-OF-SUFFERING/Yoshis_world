package src.classes;

import java.util.ArrayList;
import java.util.List;

import src.classes.Game.Difficulty;
import src.classes.GameState.Player;
import src.classes.Node.Type;

public class Minimax {
	private List<Node> list = new ArrayList<>();
	private Game game;
	private int max_depth;
	private Node root;
	private Player maximized_player;
	private Heuristic heuristic;

	public Minimax(Game game, Player maximizing_player) {
		this.game = game;
		this.maximized_player = maximizing_player;
		this.heuristic = new Heuristic(maximizing_player, game);

		Difficulty difficulty = game.get_difficulty();

		if (difficulty == Difficulty.NORMAL)
			max_depth = 2;
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
				player = maximized_player;
				type = Type.MIN;
			} else if (node.get_type() == Type.MIN) {
				player = maximized_player == Player.GREEN ? Player.RED : Player.GREEN;
				type = Type.MAX;
			} else
				throw new IllegalArgumentException("Error: invalid player");

			if (game.is_game_finished(game_state) || depth == max_depth) { // is_leaf_node(node)
				float heuristic = apply_heuristic(game_state);
				node.set_utility(heuristic);
			} else {
				List<Coordinate> available_tiles = game.get_available_tiles(player, game_state);

				if (available_tiles.size() != 0)
					for (Coordinate tile : available_tiles) {
						GameState new_game_state = game.play(player, tile, node.get_game_state().copy());
						list.add(new Node(new_game_state, node, node.get_depth() + 1, type));
					}
				else
					list.add(new Node(game_state, node, node.get_depth() + 1, type)); // In this case it is not necessary
				// make a copy of the game state
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
		// float painted_tiles_score = heuristic.calculate_painted_tiles_score(game_state);

		float future_moves_score = heuristic.calculate_future_moves_score(game_state);

		// float position_section_score = heuristic.calculate_position_section_score(game_state);

		float future_moves_score_from_each_available_tile_score = heuristic
				.calculate_future_moves_score_from_each_available_tile(game_state);

		return future_moves_score  + future_moves_score_from_each_available_tile_score;
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

		for (int i = 0; i < list.size(); i++)
			expand_node(list.get(i));

		list = list.reversed();

		Coordinate best_move = null;

		for (int i = 0; i < list.size() - 1; i++) {
			Node node = list.get(i);
			float node_utility = node.get_utility();

			Node parent = node.get_parent();

			float parent_utility = parent.get_utility();

			if (parent.get_type() == Type.MAX) {
				if (node_utility > parent_utility) {
					parent.set_utility(node_utility);

					if (parent == root)
						best_move = node.get_game_state().get_player(maximized_player); // The root is a
					// MAX node
				}
			} else if (node_utility < parent_utility)
				parent.set_utility(node_utility);
		}

		list.clear(); // Clear the list for future simulations

		return best_move;
	}
}
