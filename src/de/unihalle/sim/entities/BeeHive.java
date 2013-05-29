package de.unihalle.sim.entities;

import java.awt.Point;
import java.util.Set;

import org.mitre.sim.DefaultEntity;

import com.google.common.collect.Sets;

public class BeeHive extends DefaultEntity {

	private static final int EGG_SPAWN_RATE = 10; // sec

	private int _capacity;
	private int _beeCounter;
	private Point _position;
	private Set<Bee> _bees;

	public BeeHive(Point position, int capacity) {
		super();
		_capacity = capacity;
		_position = position;
	}

	@Override
	public void initialize() {
		info("I am alive!");
		_bees = Sets.newHashSet();
		fillHive();
	}

	public Point getPosition() {
		return new Point(_position);
	}

	private void fillHive() {
		for (int i = 0; i < _capacity; i++) {
			register(Bee.create(this), getName() + "." + "Bee" + i);
		}
		_beeCounter = _capacity;
	}

}
