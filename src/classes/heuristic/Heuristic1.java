package src.classes.heuristic;

import src.classes.Coordinate;
import src.classes.Game;
import src.classes.GameState;
import src.classes.GameState.Player;

import java.util.List;


public class Heuristic1 extends Heuristic {
	public Heuristic1(Player maximized_player, Game game) {
		super(game, maximized_player);
	}


	/**
	 * Calculates the score based on the difference between the number of available
	 * moves for the maximizing player and the number of available moves for the
	 * opponent player.
	 *
	 *
	 * @param game_state A game state.
	 * @return the difference between the number of available moves
	 *         for the maximizing player and the number of
	 *         available moves for the opponent player.
	 */
	private int available_moves_score(GameState game_state) {
		Player opponent = maximized_player == Player.GREEN ? Player.RED : Player.GREEN;

		return game.get_available_tiles(game_state.get_player(maximized_player), game_state).size() -
					game.get_available_tiles(game_state.get_player(opponent), game_state).size();
	}

	/**
	 * Calculates the score based on the number of future moves from each available
	 * tile in the game state.
	 *
	 * @param game_state a game state
	 * @return the total score based on the number of future moves from each
	 *         available tile
	 */
	private int calculate_future_moves_score_from_each_available_tile(GameState game_state) {
		List <Coordinate> available_tiles = game.get_available_tiles(game_state.get_player(maximized_player), game_state);
		int score = 0;

		for (Coordinate move : available_tiles) {
			List <Coordinate> future_available_tiles = game.get_available_tiles(move, game_state);
			score += future_available_tiles.size();
		}

		return score;
	}

	/**
	 * Calculates the score for the given game state based on available moves and future moves.
	 *
	 * @param game_state a game state.
	 * @return the score for the game state.
	 */
	public float get_score(GameState game_state) {
		return available_moves_score(game_state) +
				calculate_future_moves_score_from_each_available_tile(game_state);
	}
}
