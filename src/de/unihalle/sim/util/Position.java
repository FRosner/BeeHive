package de.unihalle.sim.util;

import java.awt.Point;

public class Position extends Point {

	private static final long serialVersionUID = 1L;

	private Position(int x, int y) {
		super(x, y);
	}

	public static Position create() {
		return new Position(0, 0);
	}

	public static Position createFromCoordinates(int x, int y) {
		return new Position(x, y);
	}

	public static Position createFromPosition(Position pos) {
		return new Position(pos.x, pos.y);
	}

	@Override
	public String toString() {
		return "[" + this.x + ";" + this.y + "]";
	}

}
