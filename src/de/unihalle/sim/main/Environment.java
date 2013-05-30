package de.unihalle.sim.main;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import de.unihalle.sim.entities.Bee;
import de.unihalle.sim.entities.BeeHive;
import de.unihalle.sim.entities.Flower;
import de.unihalle.sim.util.Position;

public class Environment {

	private List<Flower> _flowers = Lists.newArrayList();
	private List<BeeHive> _hives = Lists.newArrayList();
	private List<Bee> _bees = Lists.newArrayList();

	private int _minX = -10;
	private int _maxX = 10;
	private int _minY = -10;
	private int _maxY = 10;

	public Environment() {
	}

	public Environment(int minX, int maxX, int minY, int maxY) {
		_minX = minX;
		_maxX = maxX;
		_minY = minY;
		_maxY = maxY;
	}

	public boolean addFlower(Flower f) {
		return _flowers.add(f);
	}

	public boolean addHive(BeeHive h) {
		return _hives.add(h);
	}

	public boolean addBee(Bee b) {
		return _bees.add(b);
	}

	public boolean removeBee(Bee b) {
		return _bees.remove(b);
	}

	public Position getRandomValidPosition() {
		return Position.createRandomPositionWithin(_minX, _maxX, _minY, _maxY);
	}

	public Flower getRandomFlower() {
		if (_flowers.size() <= 0) {
			System.err.println("No flowers created but tried to select one.");
			System.exit(1);
		}
		Collections.shuffle(_flowers);
		return _flowers.get(0);
	}

}
