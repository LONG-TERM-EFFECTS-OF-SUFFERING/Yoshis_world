package src.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import src.classes.GameState.Player;

public class Game {
	private Difficulty difficulty;
	private int columns;
	private int rows;


	public Game(Difficulty difficulty, int rows, int columns) {
		this.difficulty = difficulty;
		this.rows = rows;
		this.columns = columns;
	}


	/**
	 * Builds the initial game state.
	 *
	 * This method initializes the game state by randomly placing the green Yoshi
	 * and the red Yoshi on different tiles within the game board. It ensures
	 * that the two Yoshis do not start on the same tile. It also initializes the
	 * list of free tiles and the lists of tiles occupied by each Yoshi.
	 *
	 * @return the initial game state with randomly placed Yoshis and the list of
	 *         free tiles.
	 */
	public GameState build_initial_game_state() {
		Random random = new Random();

		Coordinate green_yoshi = new Coordinate(random.nextInt(columns), random.nextInt(rows));
		Coordinate red_yoshi = new Coordinate(random.nextInt(columns), random.nextInt(rows));

		while (green_yoshi.equals(red_yoshi))
			red_yoshi = new Coordinate(random.nextInt(columns), random.nextInt(rows));

		List <Coordinate> free_tiles = new ArrayList <>();
		List <Coordinate> green_yoshi_tiles = new ArrayList <>();
		List <Coordinate> red_yoshi_tiles = new ArrayList <>();

		green_yoshi_tiles.add(green_yoshi);
		red_yoshi_tiles.add(red_yoshi);

		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++) {
				Coordinate coordinate = new Coordinate(j, i);

				if (!coordinate.equals(green_yoshi) &&
						!coordinate.equals(red_yoshi))
					free_tiles.add(coordinate);
			}

		return new GameState(green_yoshi, red_yoshi, free_tiles, red_yoshi_tiles, green_yoshi_tiles);
	}

	/**
	 * Returns the difficulty of the game.
	 *
	 * @return the difficulty of the game.
	 */
	public Difficulty get_difficulty() {
		return difficulty;
	}

	/**
	 * Returns the number of columns in the game.
	 *
	 * @return the number of columns.
	 */
	public int get_columns() {
		return columns;
	}

	/**
	 * Returns the number of rows in the game.
	 *
	 * @return the number of rows.
	 */
	public int get_rows() {
		return rows;
	}

	/**
	 * Determines the winner in a game state.
	 * The player who occupies more tiles is declared the winner.
	 *
	 * @param game_state A game state.
	 * @return The player who has occupied more tiles, or null if there is a tie.
	 */
	public Player get_winner(GameState game_state) {
		Player winner = null;
		int green_yoshi_tiles = game_state.get_tiles(Player.GREEN).size();
		int red_yoshi_tiles = game_state.get_tiles(Player.RED).size();

		if (green_yoshi_tiles > red_yoshi_tiles)
			winner = Player.GREEN;
		else if (red_yoshi_tiles > green_yoshi_tiles)
			winner = Player.RED;

		return winner;
	}

	/**
	 * Checks if a given coordinate is a valid coordinate to move in a given
	 * game state.
	 * A coordinate is considered valid if it is within the board boundaries
	 * and not occupied by any player.
	 *
	 * @param coordinate The coordinate to check.
	 * @param game_state A game state.
	 * @return true if the coordinate is valid to move in, false otherwise.
	 */
	private boolean is_valid_cordinate_to_move_in(Coordinate coordinate, GameState game_state) {
		int x = coordinate.get_x();
		int y = coordinate.get_y();

		boolean is_in_board = 0 <= y && y < rows && 0 <= x && x < columns;

		return is_in_board &&
				game_state.get_tiles(Player.GREEN).indexOf(coordinate) == -1 &&
				game_state.get_tiles(Player.RED).indexOf(coordinate) == -1;
	}


	/**
	 * Plays a game by moving the given player to the specified tile in a copy
	 * of the given game state.
	 *
	 * @param player     the player object representing the player in the game
	 * @param tile       the coordinate of the tile to move the player to.
	 * @param game_state A game state.
	 * @return the game state in which the move was made.
	 */
	public GameState play(Player player, Coordinate tile, GameState game_state) {
		GameState game_state_copy = game_state.copy();

		game_state_copy.move_to_tile(player, tile);

		return game_state_copy;
	}

	/**
	 * Returns a list of available tiles from a given coordinate in the game state.
	 *
	 * @param coordinate The coordinate from which to find available tiles.
	 * @param game_state The current game state.
	 * @return A list of coordinates representing the available tiles.
	 */
	public List <Coordinate> get_available_tiles(Coordinate coordinate, GameState game_state) {
		List <Coordinate> coordinates = new ArrayList<>();

		int x = coordinate.get_x();
		int y = coordinate.get_y();

		/*
		 * (x - 1,y - 2) -> |((x - 1) - x)| + |(y - 2) - y)| = 3
		 * (x + 1,y - 2) -> |((x + 1) - x)| + |(y - 2) - y)| = 3
		 * (x + 2,y - 1) -> |((x + 2) - x)| + |(y - 1) - y)| = 3
		 * (x + 2,y + 1) -> |((x + 2) - x)| + |(y + 1) - y)| = 3
		 * (x + 1,y + 2) -> |((x + 1) - x)| + |(y + 2) - y)| = 3
		 * (x - 1,y + 2) -> |((x - 1) - x)| + |(y + 2) - y)| = 3
		 * (x - 2,y + 1) -> |((x - 2) - x)| + |(y + 1) - y)| = 3
		 * (x - 2,y - 1) -> |((x - 2) - x)| + |(y - 1) - y)| = 3
		 */
		List <Coordinate> free_tiles = game_state.get_tiles(null);
		for (int i = x - 2; i <= x + 2; i++) {
			for (int j = y - 2; j <= y + 2; j++) {
				if (Math.abs(i - x) + Math.abs(j - y) == 3) {
					Coordinate possible_tile = new Coordinate(i, j);

					if (is_valid_cordinate_to_move_in(possible_tile, game_state) &&
							free_tiles.indexOf(possible_tile) != -1)
						coordinates.add(possible_tile);
				}
			}
		}

		return coordinates;
	}

	/**
	 * Checks if the game is finished in the given game sate by determining if
	 * both players have no available tiles left.
	 *
	 * @param game_state A game state.
	 * @return true if the game is finished, false otherwise.
	 */
	public boolean is_game_finished(GameState game_state) {
		return get_available_tiles(game_state.get_player(Player.GREEN), game_state).size() == 0 &&
				get_available_tiles(game_state.get_player(Player.RED), game_state).size() == 0;
	}



	/**
	 * Represents the difficulty levels of the game.
	 */
	static public enum Difficulty {
		NORMAL,
		MEDIUM,
		HARD
	}
}
