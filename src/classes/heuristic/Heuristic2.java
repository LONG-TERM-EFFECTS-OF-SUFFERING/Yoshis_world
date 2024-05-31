package src.classes.heuristic;

import src.classes.Game;
import src.classes.GameState;
import src.classes.GameState.Player;


public class Heuristic2 extends Heuristic {
	public Heuristic2(Player maximized_player, Game game) {
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
	 * Calculates the score based on the number of painted tiles for the current
	 * game state.
	 *
	 * @param game_state The current game state.
	 * @return the difference of the number of tiles owned by the minimized player
	 *         and the number of tiles owned by the maximized player.
	 */
	private float pinted_tiles_score(GameState game_state) {
		return game_state.get_tiles(maximized_player).size() -
				game_state.get_tiles(minimized_player).size();
	}

	/**
	 * Calculates the score for the given game state based on available moves and
	 * painted tiles.
	 *
	 * @param game_state a game state.
	 * @return the score for the game state.
	 */
	public float get_score(GameState game_state) {
		return available_moves_score(game_state) + pinted_tiles_score(game_state);
	}
}
