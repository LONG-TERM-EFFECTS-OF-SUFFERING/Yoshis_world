package src;

import src.classes.Coordinate;
import src.classes.Game;
import src.classes.Simulation;
import src.classes.Game.Difficulty;
import src.classes.GameState.Player;
import src.windows.MenuWindow;


public class Main {
	/**
	 * The main method to run the simulation and interact with the user.
	 *
	 * @param args The command-line arguments.
	 */
	public static void main(String[] args) {
		MenuWindow menu_window = new MenuWindow();
		menu_window.setVisible(true);

		Coordinate human_coordinate = new Coordinate(3, 3);
		Coordinate machine_coordinate = new Coordinate(4, 4);
		Game game = new Game(Difficulty.NORMAL, 8, 8);
		Player machine = Player.GREEN;

		Simulation simulation = new Simulation(human_coordinate, machine_coordinate, game, machine);
		simulation.run(100);
	}
}
