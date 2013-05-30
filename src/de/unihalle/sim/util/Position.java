package de.unihalle.sim.util;

import java.awt.Point;
import java.util.Random;

public class Position extends Point {

	private static final long serialVersionUID = 1L;

	private Position(int x, int y) {
		super(x, y);
	}

	/**
	 * Creates a position at (0,0).
	 * 
	 * @return position at (0,0)
	 */
	public static Position create() {
		return new Position(0, 0);
	}

	/**
	 * Creates a position at (x,y).
	 * 
	 * @param x
	 * @param y
	 * @return position at (x,y)
	 */
	public static Position createFromCoordinates(int x, int y) {
		return new Position(x, y);
	}

	/**
	 * Creates a position at the same position as pos.
	 * 
	 * @param pos
	 * @return position with same coordinates as pos
	 */
	public static Position createFromPosition(Position pos) {
		return new Position(pos.x, pos.y);
	}

	/**
	 * Creates a random position within the specified x and y range.
	 * 
	 * @param minX
	 * @param maxX
	 * @param minY
	 * @param maxY
	 * @return random position within ((minX,maxX),(minY,maxY))
	 */
	public static Position createRandomPositionWithin(int minX, int maxX, int minY, int maxY) {
		Random random = new Random();
		int x = random.nextInt(maxX - minX) + minX;
		int y = random.nextInt(maxY - minY) + minY;
		return createFromCoordinates(x, y);
	}

	@Override
	public String toString() {
		return "[" + this.x + ";" + this.y + "]";
	}

}
