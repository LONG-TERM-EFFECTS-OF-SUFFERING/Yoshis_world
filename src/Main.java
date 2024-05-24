package src;

import src.classes.Game;
import src.classes.Simulation;
import src.classes.Game.Difficulty;
import src.windows.MenuWindow;


public class Main {
	/**
	 * The main method to run the simulation and interact with the user.
	 *
	 * @param args The command-line arguments.
	 */
	public static void main(String[] args) {
		// MenuWindow menu_window = new MenuWindow();
		// menu_window.setVisible(true);

		Game game = new Game(Difficulty.NORMAL, 6, 5);

		Simulation simulation = new Simulation(game);
		simulation.run(100);
	}
}
