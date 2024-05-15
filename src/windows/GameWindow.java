package src.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import src.classes.Coordinate;
import src.classes.Game;
import src.classes.ImageCollection;
import src.classes.Minimax;
import src.classes.Game.Difficulty;
import src.classes.GameState.Player;
import src.classes.GameState;


public class GameWindow extends JFrame implements ActionListener {
	private Difficulty difficulty;
	private Game game;
	private GameState game_state;
	private ImageCollection image_collection;
	private int columns;
	private int rows;
	private int window_height;
	private int window_width;
	private JButton menu_button;
	private JButton new_game_button;
	private JDialog information_dialog;
	private JLabel machine_tiles_counter;
	private JLabel player_tiles_counter;
	private JPanel grid_panel;
	private List <Coordinate> available_player_tiles;
	private Minimax minimax;


	public GameWindow(Difficulty difficulty, int rows, int columns) {
		this.difficulty = difficulty;
		this.rows = rows;
		this.columns = columns;

		game = new Game(difficulty, rows, columns);
		game_state = game.build_initial_game_state();
		available_player_tiles = game.get_available_tiles(Player.GREEN, game_state);
		minimax = new Minimax(game);

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

		JPanel main_panel = new JPanel();
		main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));

		grid_panel = new JPanel(new GridLayout(rows, columns));
		main_panel.add(grid_panel);

		JPanel labels_panel = new JPanel();
		labels_panel.setLayout(new GridLayout(1, 2));
		labels_panel.setMaximumSize(new Dimension(window_width, window_height));

		player_tiles_counter = new JLabel("Player tiles: ");
		machine_tiles_counter = new JLabel("Machine tiles: ");

		labels_panel.add(player_tiles_counter);
		labels_panel.add(machine_tiles_counter);

		JPanel buttons_panel = new JPanel();
		buttons_panel.setLayout(new GridLayout(1, 2));
		buttons_panel.setMaximumSize(new Dimension(window_width, window_height));

		menu_button = new JButton("Menu");
		new_game_button = new JButton("New game");

		menu_button.addActionListener(this);
		new_game_button.addActionListener(this);

		buttons_panel.add(menu_button);
		buttons_panel.add(new_game_button);

		main_panel.add(labels_panel);
		main_panel.add(buttons_panel);

		for (int i = 0; i < rows * columns; i++) {
			JLabel label = new JLabel();
			label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			label.addMouseListener(new JlabelClickListener());

			grid_panel.add(label);
		}

		add(main_panel);
		setLocationRelativeTo(null);

		update_grid();
	}

	/**
	 * Updates the grid panel with the current state of the game.
	 * This method sets the appropriate icons for each tile on the grid panel based
	 * on the game state.
	 */
	private void update_grid() {
		Coordinate player = game_state.get_player(Player.GREEN);
		Coordinate machine = game_state.get_player(Player.RED);

		List <Coordinate> player_tiles = game_state.get_tiles(Player.GREEN);
		List <Coordinate> machine_tiles = game_state.get_tiles(Player.RED);
		List <Coordinate> free_tiles = game_state.get_tiles(null);

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				JLabel label = (JLabel) grid_panel.getComponent(i * columns + j);
				Coordinate coordinate = new Coordinate(j, i);
				ImageIcon icon;

				if (available_player_tiles.indexOf(coordinate) != -1)
					icon = image_collection.get_image_icon("available_tile");
				else if (coordinate.equals(player))
					icon = image_collection.get_image_icon("player");
				else if (coordinate.equals(machine))
					icon = image_collection.get_image_icon("machine");
				else if (free_tiles.indexOf(coordinate) != -1)
					icon = image_collection.get_image_icon("free_tile");
				else if (machine_tiles.indexOf(coordinate) != -1)
					icon = image_collection.get_image_icon("machine_tile");
				else if (player_tiles.indexOf(coordinate) != -1)
					icon = image_collection.get_image_icon("player_tile");
				else
					throw new IllegalArgumentException("Invalid coordinate");

				label.setIcon(icon);
			}
		}
	}

	/**
	 * Plays a turn in the game by selecting a tile and updating the grid.
	 *
	 * @param tile the coordinate of the tile to be played
	 */
	public void play(Coordinate player_tile) {
		game_state = game.play(Player.GREEN, player_tile, game_state);

		Coordinate machine_tile;

		do {
			machine_tile = minimax.run(game_state);

			if (machine_tile != null)
				game_state = game.play(Player.RED, machine_tile, game_state);

			available_player_tiles = game.get_available_tiles(Player.GREEN, game_state);
		} while (available_player_tiles.size() == 0 && machine_tile != null);

		update_grid();

		if (game.is_game_finished(game_state))
			display_game_information();
	}

	private void display_game_information() {
		information_dialog = new JDialog(this, "Game information", true);
		information_dialog.setSize((int) (window_width * 0.5), (int) (window_height * 0.3));
		information_dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		information_dialog.setResizable(false);

		JPanel information_panel = new JPanel();
		information_panel.setLayout(new BoxLayout(information_panel, BoxLayout.Y_AXIS));

		Difficulty difficulty = game.get_difficulty();
		String string_difficulty;

		if (difficulty == Difficulty.NORMAL)
			string_difficulty = "normal";
		else if (difficulty == Difficulty.MEDIUM)
			string_difficulty = "medium";
		else
			string_difficulty = "hard";

		JLabel difficulty_label = new JLabel("Difficulty: " + string_difficulty);
		difficulty_label.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel player_tiles_label = new JLabel("Player tiles: " + game_state.get_tiles(Player.GREEN).size());
		player_tiles_label.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel machine_tiles_label = new JLabel("Machine tiles: " + game_state.get_tiles(Player.RED).size());
		machine_tiles_label.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel winner_label = new JLabel("Winner: " + game.get_winner());
		winner_label.setAlignmentX(Component.CENTER_ALIGNMENT);

		information_panel.add(Box.createVerticalGlue());
		information_panel.add(difficulty_label);
		information_panel.add(player_tiles_label);
		information_panel.add(machine_tiles_label);
		information_panel.add(winner_label);
		information_panel.add(Box.createVerticalGlue());

		information_dialog.add(information_panel, BorderLayout.CENTER);

		information_dialog.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String button_name = e.getActionCommand();

		switch (button_name) {
			case "Menu" -> {
				dispose();
				MenuWindow menu_window = new MenuWindow();
				menu_window.setVisible(true);
			}
			case "New game" -> {
				dispose();
				GameWindow game_window = new GameWindow(difficulty, rows, columns);
				game_window.setVisible(true);
			}
		}
	}

	private class JlabelClickListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			JLabel label = (JLabel) e.getSource();

			System.out.println(grid_panel.getComponentZOrder(label));
			int label_index = grid_panel.getComponentZOrder(label);
			int row = label_index % columns;
			int column = label_index / columns;

			Coordinate tile = new Coordinate(row, column);

			System.out.println("Player: " + game_state.get_player(Player.GREEN));
			System.out.println("Machine: " + game_state.get_player(Player.RED));
			System.out.println("Pressed tile: " + tile);

			System.out.println("Available tiles");
			for (Coordinate coordinate : available_player_tiles)
				System.out.println(coordinate);

			if (available_player_tiles.indexOf(tile) != -1)
				play(tile);
		}
	}
}
