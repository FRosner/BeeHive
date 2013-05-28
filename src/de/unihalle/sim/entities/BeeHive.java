package de.unihalle.sim.entities;

import java.util.Set;

import org.mitre.sim.DefaultEntity;

import com.google.common.collect.Sets;

public class BeeHive extends DefaultEntity {

	private static final int EGG_SPAWN_RATE = 10; // sec

	private int _capacity;
	private int _beeCounter;
	private Set<Bee> _bees;

	public BeeHive(int capacity) {
		super();
		_capacity = capacity;
	}

	public void initialize() {
		info("I am alive!");
		_bees = Sets.newHashSet();
		fillHive();
	}

	private void fillHive() {
		for (int i = 0; i < _capacity; i++) {
			register(new Bee(), getName() + "." + "Bee" + i);
		}
		_beeCounter = _capacity;
	}

}
