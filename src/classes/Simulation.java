package src.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import src.classes.GameState.Player;


public class Simulation {
	private Coordinate human_coordinate;
	private Coordinate machine_coordinate;
	private Game game;
	private GameState game_state;
	private Minimax minimax;
	private Player human;
	private Player machine;


	public Simulation(Coordinate human_coordinate, Coordinate machine_coordinate,
						Game game, Player machine) {
		this.human_coordinate = human_coordinate;
		this.machine_coordinate = machine_coordinate;
		this.game = game;
		this.machine = machine;
		human = machine == Player.GREEN ? Player.RED : Player.GREEN;

		minimax = new Minimax(game, machine);

		build_game_state();
	}


	/**
	 * Builds the initial game state.
	 *
	 * This method sets up the coordinates for the human and machine players,
	 * and determines the free tiles on the board.
	 */
	private void build_game_state() {
		List <Coordinate> free_tiles = new ArrayList <>();
		List <Coordinate> green_yoshi_tiles = new ArrayList <>();
		List <Coordinate> red_yoshi_tiles = new ArrayList <>();

		green_yoshi_tiles.add(machine_coordinate);
		red_yoshi_tiles.add(human_coordinate);

		int rows = game.get_rows();
		int columns = game.get_columns();

		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++) {
				Coordinate coordinate = new Coordinate(j, i);

				if (!coordinate.equals(machine_coordinate) &&
						!coordinate.equals(human_coordinate))
					free_tiles.add(coordinate);
			}

		game_state = new GameState(machine_coordinate, human_coordinate, free_tiles, red_yoshi_tiles, green_yoshi_tiles);
	}

	/**
	 * Runs a single game simulation.
	 *
	 * @return The winner of the game.
	 */
	private Player run_game() {
		GameState game_state_copy = game_state.copy();

		List <Coordinate> available_human_tiles;
		Coordinate tile;

		while (!game.is_game_finished(game_state_copy)) {
			available_human_tiles = game.get_available_tiles(human, game_state_copy);

			if (available_human_tiles.size() != 0) {
				tile = available_human_tiles.get(new Random().nextInt(available_human_tiles.size()));

				game_state_copy = game.play(human, tile, game_state_copy);
			}

			tile = minimax.run(game_state_copy);
			if (tile != null)
				game_state_copy = game.play(machine, tile, game_state_copy);
		}

		return game.get_winner();
	}

	/**
	 * Runs the simulation for a specified number of games.
	 *
	 * @param iterations The number of iterations to run.
	 */
	public void run(int iterations) {
		int human_wins = 0;
		int machine_wins = 0;
		int ties = 0;

		for (int i = 0; i < iterations; i++) {
			Player winner = run_game();

			if (winner == human)
				human_wins++;
			else if (winner == machine)
				machine_wins++;
			else
				ties++;
		}

		System.out.println("Simulation results");
		System.out.println("Human wins: " + human_wins);
		System.out.println("Machine wins: " + machine_wins);
		System.out.println("Ties: " + ties);
	}
}
