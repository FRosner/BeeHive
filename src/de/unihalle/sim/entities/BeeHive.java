package de.unihalle.sim.entities;

import java.util.List;

import de.unihalle.sim.main.BeeSimulation;
import de.unihalle.sim.util.Position;

public class BeeHive extends PositionedEntity {

	// http://en.wikipedia.org/wiki/List_of_cities_in_Italy

	private static final double EGG_SPAWN_RATE = BeeSimulation.getInputData().getEggSpawnRate();
	private static final double INITIAL_INFECTION_PERCENTAGE = BeeSimulation.getInputData()
			.getInitialInfectionPercentage();
	private static final double WORKER_BEE_PERCENTAGE = BeeSimulation.getInputData().getWorkerBeePercentage();

	private int _populationCapacity;
	private int _currentPopulation = 0;
	private int _currentWorkerBeePopulation = 0;
	private int _beeCounter = 0;
	private int _storedNectar = 0;
	private boolean _infected = false;

	public static class BeeHiveFactory {

		BeeSimulation _simulation;

		private BeeHiveFactory(BeeSimulation simulation) {
			_simulation = simulation;
		}

		/**
		 * Create a new <tt>BeeHive</tt> at the specified <tt>Position</tt> position.
		 * 
		 * @param position
		 *            the <tt>BeeHive</tt> will spawn at
		 * @return a new <tt>BeeHive</tt> instance at the specified position
		 */
		public BeeHive createHiveAtPosition(Position position, int capacity) {
			return new BeeHive(position, capacity, _simulation);
		}

	}

	public static BeeHiveFactory createFactory(BeeSimulation simulation) {
		return new BeeHiveFactory(simulation);
	}

	private BeeHive(Position position, int capacity, BeeSimulation simulation) {
		_populationCapacity = capacity;
		_position = position;
		_simulation = simulation;
	}

	@Event
	public void spawnBee() {
		if (hasTooMuchInfectedBees()) {
			infoWithPosition("I am collapsed!");
			_infected = true;
			return;
		}
		if (_populationCapacity > _currentPopulation) {
			boolean newBeeIsWorker = _currentWorkerBeePopulation < _currentPopulation * WORKER_BEE_PERCENTAGE;
			Bee newBee = _simulation.beeFactory().createBee(this, newBeeIsWorker);
			register(newBee, getName() + "." + (newBeeIsWorker ? "WorkerBee" : "HiveBee") + _beeCounter);
			_simulation.environment().addBee(newBee);
			if (newBeeIsWorker) {
				_currentWorkerBeePopulation++;
			}
			_beeCounter++;
			_currentPopulation++;
		}
		schedule("spawnBee", EGG_SPAWN_RATE);
	}

	private boolean hasTooMuchInfectedBees() {
		List<Bee> bees = _simulation.environment().getBeesAt(this);
		int numberOfInfectedBees = 0;
		for (Bee bee : bees) {
			if (bee.isInfected()) {
				numberOfInfectedBees++;
			}
		}
		return ((double) numberOfInfectedBees / (double) bees.size()) > 0.75;
	}

	@Override
	public void initialize() {
		infoWithPosition("I am alive!");
		fillHive();
		schedule("spawnBee", EGG_SPAWN_RATE);
	}

	private void fillHive() {
		for (int i = 0; i < _populationCapacity; i++) {
			boolean newBeeIsWorker = _currentWorkerBeePopulation < _currentPopulation * WORKER_BEE_PERCENTAGE;
			Bee newBee = _simulation.beeFactory().createBee(this, newBeeIsWorker);
			register(newBee, getName() + "." + (newBeeIsWorker ? "WorkerBee" : "HiveBee") + _beeCounter);
			_simulation.environment().addBee(newBee);
			if (newBeeIsWorker) {
				_currentWorkerBeePopulation++;
			}
			_beeCounter++;
			_currentPopulation++;
			newBee.setRandomTimeToLive();
		}
		_simulation.environment().applyInitialInfectionToHive(this, INITIAL_INFECTION_PERCENTAGE);
	}

	public void reportDead(Bee deadBee) {
		if (_currentPopulation <= 0 || !_simulation.environment().removeBee(deadBee)) {
			// Exception is caught by Tortuga framework
			// throw new RuntimeException("A bee of an empty hive wanted to die. This is impossible.");
			System.err.println("A bee of an empty hive wanted to die. This is impossible.");
			System.exit(1);
		}
		if (deadBee.isWorker()) {
			_currentWorkerBeePopulation--;
		}
		_currentPopulation--;
	}

	public void storeNectar(int amount) {
		_storedNectar += amount;
	}

	public int getStoredNectar() {
		return _storedNectar;
	}

	public boolean isCollapsed() {
		return _infected;
	}

}
