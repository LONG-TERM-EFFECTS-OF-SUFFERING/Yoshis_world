package src.classes;

import src.classes.GameState.Player;

import java.util.List;

public class Heuristic {
	private Player maximizing_player;
	private Game game;

	public Heuristic(Player maximizing_player, Game game) {
		this.maximizing_player = maximizing_player;
		this.game = game;
	}

	/**
	 * Counts the number of tiles painted by the specified player in the game state.
	 *
	 * @param game_state the current game state
	 * @param player     the player whose painted tiles are to be counted
	 * @return the number of tiles painted by the player
	 */
	private int count_player_painted_tiles(GameState game_state, Player player) {
		List<Coordinate> tiles = game_state.get_tiles(player);
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
	public int calculate_painted_tiles_score(GameState game_state) {
		int maximizingPlayerTiles = count_player_painted_tiles(game_state, maximizing_player);
		int opposingPlayerTiles = count_player_painted_tiles(game_state,
				maximizing_player == Player.GREEN ? Player.RED : Player.GREEN);

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
	public int calculate_future_moves_score(GameState game_state) {
		Player opponent = maximizing_player == Player.GREEN ? Player.RED : Player.GREEN;
		List<Coordinate> availableMoves = game.get_available_tiles(maximizing_player, game_state);
		List<Coordinate> opponentAvailableMoves = game.get_available_tiles(opponent, game_state);
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
	public int calculate_position_section_score(GameState game_state) {
		// Check if the game board is 8x8
		if (game.get_columns() != 8 || game.get_rows() != 8) {
			return 0; // This method is only applicable to an 8x8 board
		}

		Coordinate playerPosition = game_state.get_player(maximizing_player);
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
	public int calculate_future_moves_score_from_each_available_tile(GameState game_state) {
		List<Coordinate> availableMoves = game.get_available_tiles(maximizing_player, game_state);
		int totalFutureMoves = 0;

		for (Coordinate move : availableMoves) {
			List<Coordinate> futureMoves = game.get_available_tiles_from_coordinate(move, game_state);
			totalFutureMoves += futureMoves.size();
		}

		return totalFutureMoves;
	}

}