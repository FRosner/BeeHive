package de.unihalle.sim.entities;

import java.awt.Point;

import org.mitre.sim.DefaultEntity;

public class Bee extends DefaultEntity {

	private Point _position;
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

	public Point getPosition() {
		return new Point(_position);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Bee) {
			Bee bee = (Bee) o;
			return bee.getName().equals(getName()) && bee.getPosition().equals(getPosition());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (getName() + getPosition()).hashCode();

	}
}
