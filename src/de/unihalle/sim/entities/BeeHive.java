package de.unihalle.sim.entities;

import de.unihalle.sim.util.Position;
import de.unihalle.sim.util.TimeUtil;

public class BeeHive extends PositionedEntity {

	// http://en.wikipedia.org/wiki/List_of_cities_in_Italy

	private static final double EGG_SPAWN_RATE = TimeUtil.seconds(10); // sec

	private int _populationCapacity;
	private int _currentPopulation;
	private int _beeCounter;
	private int _storedNectar;

	public BeeHive(Position position, int capacity) {
		super();
		_populationCapacity = capacity;
		_position = position;
	}

	@Event
	public void spawnBee() {
		if (_populationCapacity > _currentPopulation) {
			_beeCounter++;
			_currentPopulation++;
			register(Bee.create(this), getName() + "." + "Bee" + _beeCounter);
		}
		schedule("spawnBee", EGG_SPAWN_RATE);
	}

	@Override
	public void initialize() {
		infoWithPosition("I am alive!");
		fillHive();
		schedule("spawnBee", EGG_SPAWN_RATE);
	}

	private void fillHive() {
		for (int i = 0; i < _populationCapacity; i++) {
			register(Bee.create(this), getName() + "." + "Bee" + i);
		}
		_beeCounter = _populationCapacity;
		_currentPopulation = _populationCapacity;
	}

	public void reportDead() {
		if (_currentPopulation <= 0) {
			// Exception is caught by Tortuga framework
			// throw new RuntimeException("A bee of an empty hive wanted to die. This is impossible.");
			System.err.println("A bee of an empty hive wanted to die. This is impossible.");
			System.exit(1);
		}
		_currentPopulation--;
	}

	public void storeNectar(int amount) {
		_storedNectar += amount;
	}

	public int getStoredNectar() {
		return _storedNectar;
	}

}
