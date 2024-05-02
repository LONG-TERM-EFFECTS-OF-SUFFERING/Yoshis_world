package src.windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import src.classes.Game;
import src.classes.Game.Turn;
import src.classes.ImageCollection;


public class GameWindow extends JFrame implements ActionListener {
	private ImageCollection image_collection;
	private Game game;
	private int window_height;
	private int window_width;
	private int rows;
	private int columns;
	private JPanel grid_panel;
	private JLabel player_tiles_counter;
	private JLabel machine_tiles_counter;
	private JButton menu_button;
	private JButton new_game_button;


	public GameWindow(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;

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

		buttons_panel.add(new_game_button);
		buttons_panel.add(menu_button);

		main_panel.add(labels_panel);
		main_panel.add(buttons_panel);

		for (int i = 0; i < rows * columns; i++) {
			JLabel label = new JLabel();
			label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			label.setSize(grid_label_width, grid_label_height);
			label.addMouseListener(new JlabelClickListener());

			grid_panel.add(label);
		}

		add(main_panel);
		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String button_name = e.getActionCommand();

		switch (button_name) {
			case "Menu" -> {
				dispose();
				MenuWindow menuWindow = new MenuWindow();
				menuWindow.setVisible(true);
			}
			case "New game" -> {
				dispose();
				GameWindow gameWindow = new GameWindow(rows, columns);
				gameWindow.setVisible(true);
			}
		}
	}

	private class JlabelClickListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			JLabel label = (JLabel) e.getSource();

			int label_index = grid_panel.getComponentZOrder(label);
			int row = label_index / columns;
			int column = label_index % columns;

			System.out.println("Clicked tile (" + row + "," + column + ")");
		}
	}
}
