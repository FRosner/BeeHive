package de.unihalle.sim.entities;

import java.awt.Point;

public class Bee extends PositionedEntity {

	private BeeHive _home;

	private Bee(Point position, BeeHive home) {
		_position = position;
		_home = home;
	}

	public static Bee create(BeeHive home) {
		return new Bee(home.getPosition(), home);
	}

	public static Bee createAtPosition(Point position, BeeHive home) {
		return new Bee(position, home);
	}

	public void collectNectar() {
		info("Collecting nectar.");
		schedule("flyBack", 3.0);
	}

	public void flyBack() {
		info("Flying back.");
	}

	@Override
	public void initialize() {
		info("I am alive!");
		schedule("collectNectar", 2.0);
	}

	public boolean isAtHome(BeeHive home) {
		return _home.equals(home);
	}

}