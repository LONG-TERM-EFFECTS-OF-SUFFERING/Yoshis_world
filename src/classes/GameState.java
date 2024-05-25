package src.classes;

import java.util.ArrayList;
import java.util.List;


public class GameState {
	private Coordinate green_yoshi = null;
	private Coordinate red_yoshi = null;
	private List <Coordinate> free_tiles = new ArrayList <>();
	private List <Coordinate> green_yoshi_tiles = new ArrayList <>();
	private List <Coordinate> red_yoshi_tiles = new ArrayList <>();


	public GameState(Coordinate green_yoshi, Coordinate red_yoshi, List <Coordinate> free_tiles,
						 List <Coordinate> red_yoshi_tiles, List <Coordinate> green_yoshi_tiles) {
		this.green_yoshi = green_yoshi;
		this.red_yoshi = red_yoshi;
		this.free_tiles = free_tiles;
		this.red_yoshi_tiles = red_yoshi_tiles;
		this.green_yoshi_tiles = green_yoshi_tiles;
	}


	/**
	 * Returns the coordinate associated with the specified player.
	 *
	 * @param player The player for which to retrieve his coordinate.
	 */
	public Coordinate get_player(Player player) {
		if (player == Player.GREEN)
			return green_yoshi;
		else if (player == Player.RED)
			return red_yoshi;
		else
			throw new IllegalArgumentException("Error: invalid player");
	}

	/**
	 * Returns the list of tiles associated with the specified player.
	 *
	 * @param player The player for which to retrieve the tiles.
	 * @return the list of tiles associated with the player.
	 */
	public List <Coordinate> get_tiles(Player player) {
		if (player == Player.GREEN)
			return green_yoshi_tiles;
		else if (player == Player.RED)
			return red_yoshi_tiles;
		else
			return free_tiles;
	}

	/**
	 * Adds a tile to the corresponding player's collection of tiles.
	 *
	 * @param player the player to whom the tile belongs (GREEN, RED or null if
	 *               the tile is free).
	 * @param tile the coordinate of the tile to be added.
	 */
	public void add_tile(Player player, Coordinate tile) {
		if (player == Player.GREEN)
			green_yoshi_tiles.add(tile);
		else if (player == Player.RED)
			red_yoshi_tiles.add(tile);
		else
			free_tiles.add(tile);
	}

	/**
	 * Moves the specified player to the given tile.
	 *
	 * @param player The player to move (GREEN or RED).
	 * @param tile the tile to move the player to.
	 */
	public void move_to_tile(Player player, Coordinate tile) {
		if (player == Player.GREEN && green_yoshi_tiles.indexOf(tile) == -1) {
			green_yoshi_tiles.add(tile);
			green_yoshi = tile;
			free_tiles.remove(tile);
		} else if (player == Player.RED && red_yoshi_tiles.indexOf(tile) == -1) {
			red_yoshi_tiles.add(tile);
			red_yoshi = tile;
			free_tiles.remove(tile);
		}
	}

	/**
	 * Creates a deep copy of the GameState object.
	 *
	 * @return a new GameState object that is a copy of the current object.
	 */
	public GameState copy() {
		List <Coordinate> free_tiles_copy = new ArrayList <>(free_tiles);
		List <Coordinate> green_yoshi_tiles_copy = new ArrayList <>(green_yoshi_tiles);
		List <Coordinate> red_yoshi_tiles_copy = new ArrayList <>(red_yoshi_tiles);
		Coordinate green_yoshi_copy = (green_yoshi != null) ? new Coordinate(green_yoshi.get_x(), green_yoshi.get_y()) : null;
		Coordinate red_yoshi_copy = (red_yoshi != null) ? new Coordinate(red_yoshi.get_x(), red_yoshi.get_y()) : null;

		return new GameState(green_yoshi_copy, red_yoshi_copy, free_tiles_copy, red_yoshi_tiles_copy, green_yoshi_tiles_copy);
	}

	/**
	 * Represents the players in the game.
	 */
	static public enum Player {
		RED,
		GREEN
	}
}
