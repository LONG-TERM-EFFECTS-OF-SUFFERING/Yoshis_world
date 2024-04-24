package src.windows;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class MenuWindow extends JFrame implements ActionListener {
	private JButton start_button;
	private JComboBox <String> difficulty_combo_box;


	public MenuWindow() {
		Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
		int screen_width = (int) screen_size.getWidth();
		int screen_height = (int) screen_size.getHeight();
		int width = (int) (screen_width * 0.2);
		int height = (int) (screen_height * 0.3);

		setTitle("Yoshi's world - Menu");
		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setIconImage(new ImageIcon("./src/assets/icon.png").getImage());

		JPanel main_panel = new JPanel();
		main_panel.setLayout(new GridLayout(2, 2));

		JLabel title_label = new JLabel("Select the difficulty:");
		main_panel.add(title_label);

		String[] difficulties = { "Normal", "Medium", "Hard" };
		difficulty_combo_box = new JComboBox <>(difficulties);
		main_panel.add(difficulty_combo_box);

		start_button = new JButton("Start game");
		start_button.addActionListener(this);
		main_panel.add(start_button);

		add(main_panel);
		setLocationRelativeTo(null);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		String button_name = e.getActionCommand();

		switch (button_name) {
			case "Start game" -> {
				String selected_difficulty = (String) difficulty_combo_box.getSelectedItem();

				switch (selected_difficulty) {
					case "Normal" -> System.out.println("Normal");
					case "Medium" -> System.out.println("Medium");
					case "Hard" -> System.out.println("Hard");
					default -> throw new IllegalArgumentException("Invalid difficulty selected");
				}

				dispose();
				GameWindow game_window = new GameWindow(10, 10);
				game_window.setVisible(true);
			}
		}
	}
}
