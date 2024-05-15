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
			put("green_yoshi", new ImageIcon("./src/assets/green_yoshi.png"));
			put("green_yoshi_tile", new ImageIcon("./src/assets/green_yoshi_tile.png"));
			put("red_yoshi", new ImageIcon("./src/assets/red_yoshi.png"));
			put("red_yoshi_tile", new ImageIcon("./src/assets/red_yoshi_tile.png"));
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
	 * @return the image icon associated with the given name, or null if not found.
	 */
	public ImageIcon get_image_icon(String name) {
		return images.get(name);
	}
}
