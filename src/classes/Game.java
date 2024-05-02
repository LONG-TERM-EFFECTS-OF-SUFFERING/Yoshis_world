package src.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Game {
	private Coordinate player = null;
	private Coordinate machine = null;
	private int columns;
	private int rows;
	private List <Coordinate> player_tiles = new ArrayList <>();
	private List <Coordinate> machine_tiles = new ArrayList <>();
	private List <Coordinate> free_tiles = new ArrayList<>();


	public Game(int rows, int columns) {
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

		System.out.println("Player " + player);
		System.out.println("Machine " + machine);
	}

	/**
	 * Returns the number of rows in the game.
	 *
	 * @return the number of rows
	 */
	public int get_rows() {
		return rows;
	}

	/**
	 * Returns the number of columns in the game.
	 *
	 * @return the number of columns
	 */
	public int get_columns() {
		return columns;
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
	public List <Coordinate> get_available_coordinates(Turn turn) {
		List <Coordinate> coordinates = new ArrayList <>();
		Coordinate coordinate = null;

		if (turn == Turn.PLAYER)
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
					Coordinate possible_coordinate = new Coordinate(i, j);

					if (is_valid_cordinate_to_move_in(possible_coordinate))
						coordinates.add(possible_coordinate);
				}
			}
		}

		for (Coordinate coordinate_ : coordinates)
			System.out.println(coordinate_);

		return coordinates;
	}

	/**
	 * Represents the turn in the game.
	 */
	static public enum Turn {
		PLAYER,
		MACHINE
	}
}
