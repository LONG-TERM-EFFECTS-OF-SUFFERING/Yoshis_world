package src.classes.heuristic;

import src.classes.Coordinate;
import src.classes.Game;
import src.classes.GameState;
import src.classes.GameState.Player;

import java.util.List;

public class Heuristic2 extends Heuristic {
	public Heuristic2(Player maximized_player, Game game) {
		super(game, maximized_player);
	}


	/**
	 * Counts the number of tiles painted by the specified player in the game state.
	 *
	 * @param game_state the current game state
	 * @param player     the player whose painted tiles are to be counted
	 * @return the number of tiles painted by the player
	 */
	private int count_player_painted_tiles(GameState game_state, Player player) {
		List <Coordinate> tiles = game_state.get_tiles(player);
		return tiles.size();
	}

	/**
	 * Calculates the score based on the number of painted tiles for the current
	 * player.
	 * The score is calculated by subtracting the number of painted tiles of the
	 * opposing player
	 * from the number of painted tiles of the maximizing player.
	 *
	 * @param game_state the current game state
	 * @return the calculated score
	 */
	private int calculate_painted_tiles_score(GameState game_state) {
		int maximizingPlayerTiles = count_player_painted_tiles(game_state, maximized_player);
		int opposingPlayerTiles = count_player_painted_tiles(game_state,
				maximized_player == Player.GREEN ? Player.RED : Player.GREEN);

		int score = maximizingPlayerTiles - opposingPlayerTiles;

		return score;
	}

	/**
	 * Calculates the score based on the difference between the number of available
	 * moves for the maximizing player
	 * and the number of available moves for the opponent player.
	 *
	 * @param game_state The current game state.
	 * @return The score representing the difference between the number of available
	 *         moves for the maximizing player
	 *         and the number of available moves for the opponent player.
	 */
	private int calculate_future_moves_score(GameState game_state) {
		Player opponent = maximized_player == Player.GREEN ? Player.RED : Player.GREEN;
		List <Coordinate> availableMoves = game.get_available_tiles(game_state.get_player(maximized_player), game_state);
		List <Coordinate> opponentAvailableMoves = game.get_available_tiles(game_state.get_player(opponent), game_state);
		return availableMoves.size() - opponentAvailableMoves.size();
	}

	/**
	 * Calculates the score based on the section of the player's position on the game board.
	 * The score is determined as follows:
	 * - If the player is in a corner, the score is -15.
	 * - If the player is on a border, the score is -10.
	 * - If the player is in the interior near the border, the score is -5.
	 * - If the player is in the center, the score is 0.
	 *
	 * @param game_state the current state of the game
	 * @return the score based on the player's position section
	 */
	private int calculate_position_section_score(GameState game_state) {
		// Check if the game board is 8x8
		if (game.get_columns() != 8 || game.get_rows() != 8) {
			return 0; // This method is only applicable to an 8x8 board
		}

		Coordinate playerPosition = game_state.get_player(maximized_player);
		int x = playerPosition.get_x();
		int y = playerPosition.get_y();

		// Determine the section of the player's position
		if ((x == 0 && y == 0) || (x == 0 && y == 7) || (x == 7 && y == 0) || (x == 7 && y == 7)) {
			// The player is in a corner
			return -15;
		} else if ((x == 0 || x == 7) && (1 <= y && y <= 6) || (y == 0 || y == 7) && (1 <= x && x <= 6)) {
			// The player is on a border
			return -10;
		} else if ((x == 1 || x == 6) && (1 <= y && y <= 6) || (y == 1 || y == 6) && (1 <= x && x <= 6)) {
			// The player is in the interior near the border
			return -5;
		} else {
			// The player is in the center
			return 0;
		}
	}

	/**
	 * Calculates the score based on the number of future moves from each available tile in the game state.
	 *
	 * @param game_state the current game state
	 * @return the total score based on the number of future moves from each available tile
	 */
	private int calculate_future_moves_score_from_each_available_tile(GameState game_state) {
		List <Coordinate> availableMoves = game.get_available_tiles(game_state.get_player(maximized_player), game_state);
		int totalFutureMoves = 0;

		for (Coordinate move : availableMoves) {
			List <Coordinate> futureMoves = game.get_available_tiles(move, game_state);
			totalFutureMoves += futureMoves.size();
		}

		return totalFutureMoves;
	}

	public float get_score(GameState game_state) {
		// float painted_tiles_score =
		// heuristic.calculate_painted_tiles_score(game_state);

		float future_moves_score = calculate_future_moves_score(game_state);

		// float position_section_score =
		// heuristic.calculate_position_section_score(game_state);

		float future_moves_score_from_each_available_tile_score = calculate_future_moves_score_from_each_available_tile(
				game_state);

		return 1; //future_moves_score + future_moves_score_from_each_available_tile_score;
	}
}
