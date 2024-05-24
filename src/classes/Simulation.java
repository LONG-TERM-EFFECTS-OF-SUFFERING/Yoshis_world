package src.classes;

import src.classes.GameState.Player;


public class Simulation {
	private Coordinate green_yoshi_coordinate;
	private Coordinate red_yoshi_coordinate;
	private Game game;
	private GameState game_state;
	private Minimax green_yoshi_minimax;
	private Minimax red_yoshi_minimax;


	public Simulation(Coordinate green_yoshi_coordinate, Coordinate red_yoshi_coordinate,
						Game game) {
		this.green_yoshi_coordinate = green_yoshi_coordinate;
		this.red_yoshi_coordinate = red_yoshi_coordinate;
		this.game = game;

		Heuristic heuristic = new Heuristic1(Player.GREEN, game);
		green_yoshi_minimax = new Minimax(heuristic, game, Player.GREEN);

		heuristic = new Heuristic2(Player.RED, game);
		red_yoshi_minimax = new Minimax(heuristic, game, Player.RED);
	}

	/**
	 * Runs a single game simulation.
	 *
	 * @return The winner of the game.
	 */
	private Player run_game() {
		GameState game_state_copy = game.build_initial_game_state();
		Coordinate tile;

		while (!game.is_game_finished(game_state_copy)) {
			tile = red_yoshi_minimax.run(game_state_copy);

			if (tile != null)
				game_state_copy = game.play(Player.RED, tile, game_state_copy);

			tile = green_yoshi_minimax.run(game_state_copy);

			if (tile != null)
				game_state_copy = game.play(Player.GREEN, tile, game_state_copy);
		}

		return game.get_winner();
	}

	/**
	 * Runs the simulation for a specified number of games.
	 *
	 * @param iterations The number of iterations to run.
	 */
	public void run(int iterations) {
		int green_yoshi_wins = 0;
		int red_yoshi_wins = 0;
		int ties = 0;

		for (int i = 0; i < iterations; i++) {
			Player winner = run_game();

			if (winner == Player.GREEN)
				green_yoshi_wins++;
			else if (winner == Player.RED)
				red_yoshi_wins++;
			else
				ties++;
		}

		System.out.println("Simulation results");
		System.out.println("Green Yoshi wins: " + green_yoshi_wins);
		System.out.println("Red Yoshi wins: " + red_yoshi_wins);
		System.out.println("Ties: " + ties);
	}
}
