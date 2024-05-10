package src.classes;


public class Coordinate {
	private int x;
	private int y;


	/**
	 * Constructs a Coordinate object with the specified x and y coordinates.
	 *
	 * @param x The x coordinate of the point.
	 * @param y The y coordinate of the point.
	 */
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}


	/**
	 * Returns the x coordinate of the point.
	 *
	 * @return the x coordinate of the point.
	 */
	public int get_x() {
		return x;
	}

	/**
	 * Returns the y coordinate of the point.
	 *
	 * @return the y coordinate of the point.
	 */
	public int get_y() {
		return y;
	}

	/**
	 * Sets the x coordinate of the point.
	 *
	 * @param x The new x coordinate of the point.
	 */
	public void set_x(int x) {
		this.x = x;
	}

	/**
	 * Sets the y coordinate of the point.
	 *
	 * @param y The new y coordinate of the point.
	 */
	public void set_y(int y) {
		this.y = y;
	}

	/**
	 * Checks if this Coordinate object is equal to another object.
	 * Two Coordinate objects are considered equal if they have the same x and y coordinates.
	 *
	 * @param obj The object to compare with.
	 * @return true if the objects are equal, false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || getClass() != obj.getClass())
			return false;

		Coordinate other = (Coordinate) obj;
		return x == other.get_x() && y == other.get_y();
	}

	/**
	 * Returns a string representation of the Coordinate object.
	 * The string representation is in the format "(x, y)".
	 *
	 * @return a string representation of the Coordinate object.
	 */
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
