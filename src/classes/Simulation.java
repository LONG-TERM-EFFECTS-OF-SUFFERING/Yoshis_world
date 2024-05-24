package src.classes;

import src.classes.GameState.Player;
import src.classes.heuristic.Heuristic;
import src.classes.heuristic.Heuristic1;
import src.classes.heuristic.Heuristic2;


public class Simulation {
	private Game game;
	private Minimax green_yoshi_minimax;
	private Minimax red_yoshi_minimax;


	public Simulation(Game game) {
		this.game = game;

		Heuristic heuristic_1 = new Heuristic1(Player.GREEN, game);
		green_yoshi_minimax = new Minimax(heuristic_1, game, Player.GREEN);

		Heuristic heuristic_2 = new Heuristic2(Player.RED, game);
		red_yoshi_minimax = new Minimax(heuristic_2, game, Player.RED);
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

		System.out.println("Red " + game_state_copy.get_tiles(Player.RED).size());
		System.out.println("Green " + game_state_copy.get_tiles(Player.RED).size());
		System.out.println("Free " + game_state_copy.get_tiles(null).size());
		System.out.println("");

		return game.get_winner(game_state_copy);
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

			if (winner == Player.GREEN) {
				System.out.println("Green Yoshi");
				green_yoshi_wins++;
			} else if (winner == Player.RED) {
				System.out.println("Red Yoshi");
				red_yoshi_wins++;
			} else {
				System.out.println("Tie");
				ties++;
			}
		}

		System.out.println("Simulation results");
		System.out.println("Green Yoshi wins: " + green_yoshi_wins);
		System.out.println("Red Yoshi wins: " + red_yoshi_wins);
		System.out.println("Ties: " + ties);
	}
}
