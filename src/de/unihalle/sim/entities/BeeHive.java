package de.unihalle.sim.entities;

import java.awt.Point;

public class BeeHive extends PositionedEntity {

	private static final int EGG_SPAWN_RATE = 10; // sec

	private int _capacity;
	private int _beeCounter;

	public BeeHive(Point position, int capacity) {
		super();
		_capacity = capacity;
		_position = position;
	}

	@Override
	public void initialize() {
		info("I am alive!");
		fillHive();
	}

	private void fillHive() {
		for (int i = 0; i < _capacity; i++) {
			register(Bee.create(this), getName() + "." + "Bee" + i);
		}
		_beeCounter = _capacity;
	}

}
