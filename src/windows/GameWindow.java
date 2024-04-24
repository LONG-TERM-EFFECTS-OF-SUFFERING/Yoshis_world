package src.windows;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import src.classes.Game;
import src.classes.Game.Turn;
import src.classes.ImageCollection;


public class GameWindow extends JFrame {
	private ImageCollection image_collection;
	private Game game;
	private int window_height;
	private int window_width;

	public GameWindow(int rows, int columns) {
		game = new Game(rows, columns);
		game.get_available_coordinates(Turn.PLAYER);

		Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
		int screen_width = (int) screen_size.getWidth();
		int screen_height = (int) screen_size.getHeight();

		window_width = (int) (screen_width * 0.3);
		window_height = (int) (screen_height * 0.5);

		int grid_label_width = (int) (window_width * 0.1);
		int grid_label_height = (int) (window_height * 0.075);

		setTitle("Yoshi's world - Gane");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(window_width, window_height);
		setResizable(false);
		setIconImage((new ImageIcon("./src/assets/icon.png").getImage()));

		image_collection = new ImageCollection(grid_label_width, grid_label_height);
	}
}
