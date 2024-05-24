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
import src.classes.heuristic.Heuristic1;
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
	private List <Coordinate> available_human_tiles;
	private Minimax minimax;
	private Player human;
	private Player machine;


	public GameWindow(Difficulty difficulty, int rows, int columns, Player human) {
		this.difficulty = difficulty;
		this.rows = rows;
		this.columns = columns;
		this.human = human;
		machine = human == Player.GREEN ? Player.RED : Player.GREEN;

		game = new Game(difficulty, rows, columns);
		minimax = new Minimax(new Heuristic1(machine, game), game, machine);
		game_state = game.build_initial_game_state();
		game_state = game.play(machine, minimax.run(game_state), game_state); // The first move is made by the
																				// machine
		available_human_tiles = game.get_available_tiles(game_state.get_player(human), game_state);

		Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
		int screen_width = (int) screen_size.getWidth();
		int screen_height = (int) screen_size.getHeight();

		window_width = (int) (screen_width * 0.3);
		window_height = (int) (screen_height * 0.5);

		int grid_label_width = (int) (window_width * 0.1);
		int grid_label_height = (int) (window_height * 0.075);

		setTitle("Yoshi's world - Game");
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

		player_tiles_counter = new JLabel("Player tiles: 1");
		machine_tiles_counter = new JLabel("Machine tiles: 2");

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
		Coordinate human_coordinate = game_state.get_player(human);
		Coordinate machine_coordinate = game_state.get_player(machine);

		List <Coordinate> player_tiles = game_state.get_tiles(human);
		List <Coordinate> machine_tiles = game_state.get_tiles(machine);
		List <Coordinate> free_tiles = game_state.get_tiles(null);

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				JLabel label = (JLabel) grid_panel.getComponent(i * columns + j);
				Coordinate coordinate = new Coordinate(j, i);
				ImageIcon icon;

				if (available_human_tiles.indexOf(coordinate) != -1)
					icon = image_collection.get_image_icon("available_tile");
				else if (free_tiles.indexOf(coordinate) != -1)
					icon = image_collection.get_image_icon("free_tile");
				else if (coordinate.equals(machine_coordinate))
					icon = image_collection.get_image_icon("green_yoshi");
				else if (machine_tiles.indexOf(coordinate) != -1)
					icon = image_collection.get_image_icon("green_yoshi_tile");
				else if (coordinate.equals(human_coordinate))
					icon = image_collection.get_image_icon("red_yoshi");
				else if (player_tiles.indexOf(coordinate) != -1)
					icon = image_collection.get_image_icon("red_yoshi_tile");
				else
					throw new IllegalArgumentException("Invalid coordinate");

				label.setIcon(icon);
			}
		}
	}

	/**
	 * Executes a move for the human player and then for the machine.
	 *
	 * This method updates the game state based on the human player's move and then
	 * determines the machine's move using the Minimax algorithm. It updates the
	 * available tiles for the human player, updates the game grid, and refreshes
	 * the player and machine tile counters. If the game is finished after the
	 * moves,
	 * it displays the game information.
	 *
	 * @param human_tile The coordinate of the tile to which the human player moves.
	 */
	public void play(Coordinate human_tile) {
		game_state = game.play(human, human_tile, game_state);

		Coordinate machine_tile = minimax.run(game_state);

		available_human_tiles = game.get_available_tiles(game_state.get_player(human), game_state); // Update after the
																				// player move

		if (available_human_tiles.size() == 0)
			while (machine_tile != null) {
				game_state = game.play(machine, machine_tile, game_state);
				machine_tile = minimax.run(game_state);
			}
		else if (machine_tile != null)
			game_state = game.play(machine, machine_tile, game_state);

		available_human_tiles = game.get_available_tiles(game_state.get_player(human), game_state); // Update after the
																				// machine move

		update_grid();

		player_tiles_counter.setText("Player tiles: " + game_state.get_tiles(human).size());
		machine_tiles_counter.setText("Machine tiles: " + game_state.get_tiles(machine).size());

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

		JLabel player_tiles_label = new JLabel("Player tiles: " + game_state.get_tiles(human).size());
		player_tiles_label.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel machine_tiles_label = new JLabel("Machine tiles: " + game_state.get_tiles(machine).size());
		machine_tiles_label.setAlignmentX(Component.CENTER_ALIGNMENT);

		Player winner = game.get_winner(game_state);
		String string_winner;

		if (winner == human)
			string_winner = "human";
		else if (winner == machine)
			string_winner = "machine";
		else
			string_winner = "tie";

		JLabel winner_label = new JLabel("Winner: " + string_winner);
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
				GameWindow game_window = new GameWindow(difficulty, rows, columns, human);
				game_window.setVisible(true);
			}
		}
	}

	private class JlabelClickListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			JLabel label = (JLabel) e.getSource();

			int label_index = grid_panel.getComponentZOrder(label);
			int row = label_index % columns;
			int column = label_index / columns;

			Coordinate tile = new Coordinate(row, column);

			System.out.println(/* -------------------------------------------------------------------------- */);
			System.out.println("Player: " + game_state.get_player(human));
			System.out.println("Machine: " + game_state.get_player(machine));
			System.out.println("Pressed tile: " + tile);

			System.out.println("Available human tiles");
			for (Coordinate coordinate : available_human_tiles)
				System.out.println(coordinate);

			List <Coordinate> available_machine_tiles = game.get_available_tiles(game_state.get_player(machine), game_state);
			System.out.println("Available machine tiles");
			for (Coordinate coordinate : available_machine_tiles)
				System.out.println(coordinate);

			if (available_human_tiles.indexOf(tile) != -1)
				play(tile);
		}
	}
}
