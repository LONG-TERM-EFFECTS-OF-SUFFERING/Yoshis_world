package src.classes;

import java.awt.Image;
import javax.swing.ImageIcon;
import java.util.HashMap;
import java.util.Map;


public class ImageCollection {
	private Map <String, ImageIcon> images = new HashMap <String, ImageIcon>() {
		{
			put("available_tile", new ImageIcon("./src/assets/available_tile.png"));
			put("free_tile", new ImageIcon("./src/assets/free_tile.png"));
			put("machine", new ImageIcon("./src/assets/machine.png"));
			put("machine_tile", new ImageIcon("./src/assets/machine_tile.png"));
			put("player", new ImageIcon("./src/assets/player.png"));
			put("player_tile", new ImageIcon("./src/assets/player_tile.png"));
		}
	};


	public ImageCollection(int with, int height) {
		for (Map.Entry <String, ImageIcon> entry : images.entrySet()) {
			String key = entry.getKey();
			ImageIcon value = entry.getValue();

			Image scaled_icon = value.getImage();
			scaled_icon = scaled_icon.getScaledInstance(with, height, Image.SCALE_SMOOTH);

			images.put(key, new ImageIcon(scaled_icon));
		}
	}


	/**
	 * Returns the image icon associated with the given name.
	 *
	 * @param name The name of the image icon to retrieve.
	 * @return The image icon associated with the given name, or null if not found.
	 */
	public ImageIcon get_image_icon(String name) {
		return images.get(name);
	}
}
