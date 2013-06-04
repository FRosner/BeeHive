package de.unihalle.sim.entities;

import de.unihalle.sim.main.BeeSimulation;
import de.unihalle.sim.util.Position;
import de.unihalle.sim.util.TimeUtil;

public class BeeHive extends PositionedEntity {

	// http://en.wikipedia.org/wiki/List_of_cities_in_Italy

	private static final double EGG_SPAWN_RATE = TimeUtil.seconds(10); // sec
	private static final double INITIAL_INFECTION_PERCENTAGE = 0.1;

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
			Bee newBee = Bee.create(this);
			register(newBee, getName() + "." + "Bee" + _beeCounter);
			BeeSimulation.getEnvironment().addBee(newBee);
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
			Bee newBee = Bee.create(this);
			register(newBee, getName() + "." + "Bee" + i);
			BeeSimulation.getEnvironment().addBee(newBee);
		}
		BeeSimulation.getEnvironment().applyInitialInfectionToHive(this, INITIAL_INFECTION_PERCENTAGE);
		_beeCounter = _populationCapacity;
		_currentPopulation = _populationCapacity;
	}

	public void reportDead(Bee deadBee) {
		if (_currentPopulation <= 0) {
			// Exception is caught by Tortuga framework
			// throw new RuntimeException("A bee of an empty hive wanted to die. This is impossible.");
			System.err.println("A bee of an empty hive wanted to die. This is impossible.");
			System.exit(1);
		}
		BeeSimulation.getEnvironment().removeBee(deadBee);
		_currentPopulation--;
	}

	public void storeNectar(int amount) {
		_storedNectar += amount;
	}

	public int getStoredNectar() {
		return _storedNectar;
	}

}
