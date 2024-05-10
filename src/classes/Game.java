package src.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Game {
	private Difficulty difficulty;
	private Player winner = null;
	private Coordinate player = null;
	private Coordinate machine = null;
	private int columns;
	private int rows;
	private List <Coordinate> player_tiles = new ArrayList <>();
	private List <Coordinate> machine_tiles = new ArrayList <>();
	private List <Coordinate> free_tiles = new ArrayList<>();


	public Game(Difficulty difficulty, int rows, int columns) {
		this.difficulty = difficulty;
		this.rows = rows;
		this.columns = columns;

		Random random = new Random();

		player = new Coordinate(random.nextInt(columns), random.nextInt(rows));
		machine = new Coordinate(random.nextInt(columns), random.nextInt(rows));

		while(player.equals(machine))
			machine = new Coordinate(random.nextInt(columns), random.nextInt(rows));

		player_tiles.add(player);
		machine_tiles.add(machine);

		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++) {
				Coordinate coordinate = new Coordinate(j, i);

				if (!coordinate.equals(player) &&
					!coordinate.equals(machine))
					free_tiles.add(coordinate);
			}

		play(null);
	}

	/**
	 * Returns the winner of the game.
	 *
	 * @return the winner of the game.
	 */
	public Player get_winner() {
		return winner;
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
	 * Returns the number of columns in the game.
	 *
	 * @return the number of columns.
	 */
	public int get_columns() {
		return columns;
	}

	/**
	 * Returns the player's coordinetes in the game.
	 *
	 * @return the player's coordinetes.
	 */
	public Coordinate get_player() {
		return player;
	}

	/**
	 * Returns the machine's coordinetes in the game.
	 *
	 * @return the machine's coordinetes.
	 */
	public Coordinate get_machine() {
		return machine;
	}

	/**
	 * Returns the list of player tiles.
	 *
	 * @return the list of player tiles.
	 */
	public List <Coordinate> get_player_tiles() {
		return player_tiles;
	}

	/**
	 * Returns the list of machine tiles.
	 *
	 * @return the list of machine tiles.
	 */
	public List <Coordinate> get_machine_tiles() {
		return machine_tiles;
	}

	/**
	 * Returns the list of free tiles.
	 *
	 * @return the list of free tiles.
	 */
	public List <Coordinate> get_free_tiles() {
		return free_tiles;
	}

	/**
	 * Checks if the given coordinate is a valid coordinate to move in.
	 *
	 * @param coordinate The coordinate to check
	 * @return true if the coordinate is valid to move in, false otherwise
	 */
	private boolean is_valid_cordinate_to_move_in(Coordinate coordinate) {
		int x = coordinate.get_x();
		int y = coordinate.get_y();

		boolean is_in_board = 0 <= y && y < rows && 0 <= x && x < columns;

		return is_in_board &&
				player_tiles.indexOf(coordinate) == -1 &&
				machine_tiles.indexOf(coordinate) == -1;
	}

	/**
	 * Returns a list of available coordinates for a given turn.
	 *
	 * @param turn The turn (PLAYER or MACHINE) for which to find available coordinates.
	 * @return A list of Coordinate objects representing the available coordinates.
	 */
	public List <Coordinate> get_available_tiles(Player turn) {
		List <Coordinate> coordinates = new ArrayList <>();
		Coordinate coordinate = null;

		if (turn == Player.HUMAN)
			coordinate = player;
		else
			coordinate = machine;

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
		for (int i = x - 2; i <= x + 2; i++) {
			for (int j = y - 2; j <= y + 2; j++) {
				if (Math.abs(i - x) + Math.abs(j - y) == 3) {
					Coordinate possible_tile = new Coordinate(i, j);

					if (is_valid_cordinate_to_move_in(possible_tile) &&
						free_tiles.indexOf(possible_tile) != -1)
						coordinates.add(possible_tile);
				}
			}
		}

		return coordinates;
	}


	/**
	 * Plays a move in the game by placing a tile on the board.
	 * If the given tile is null, the method selects a random available tile for the machine player and places it on the board.
	 * If the given tile is not null, the method places the tile on the board for the human player.
	 *
	 * @param tile The tile to be placed on the board. If null, a random available tile is selected for the machine player.
	 * @return true if the move was successfully played, false otherwise.
	 */
	public boolean play(Coordinate tile) {
		if (tile == null) {
			List <Coordinate> available_machine_tiles = get_available_tiles(Player.MACHINE);

			if (available_machine_tiles.size() != 0) {
				tile = available_machine_tiles.get(new Random().nextInt(available_machine_tiles.size()));

				machine_tiles.add(tile);
				free_tiles.remove(tile);

				machine = tile;

				return true;
			} else
				return false;

		} else {
			player_tiles.add(tile);
			free_tiles.remove(tile);

			player = tile;

			return true;
		}
	}

	/**
	 * Checks if the game has finished.
	 * The game is considered finished when there are no available tiles for both the machine player and the human player.
	 * The winner is determined by comparing the number of tiles each player has.
	 *
	 * @return true if the game is finished, false otherwise
	 */
	public boolean is_game_finished() {
		if (get_available_tiles(Player.MACHINE).size() == 0 &&
			get_available_tiles(Player.HUMAN).size() == 0) {
			winner = player_tiles.size() > machine_tiles.size() ? Player.HUMAN : Player.MACHINE;

			return true;
		} else
			return false;
	}

	/**
	 * Represents the difficulty levels of the game.
	 */
	static public enum Difficulty {
		NORMAL,
		MEDIUM,
		HARD
	}

	/**
	 * Represents the players in the game.
	 */
	static public enum Player {
		HUMAN,
		MACHINE
	}
}
