package de.unihalle.sim.entities;

import java.util.List;
import java.util.Random;

import de.unihalle.sim.main.BeeSimulation;
import de.unihalle.sim.util.MovementUtil;
import de.unihalle.sim.util.Position;
import de.unihalle.sim.util.TimeUtil;

public class Bee extends PositionedEntity {

	private static final int MAX_NECTAR_CAPACITY = BeeSimulation.getInputData().getBeeMaxNectarCapacity();
	private static final double NECTAR_COLLECTION_TIME = BeeSimulation.getInputData().getNectarCollectionTime();
	private static final double MOVEMENT_SPEED = BeeSimulation.getInputData().getMovementSpeed();
	private static final double STORE_TIME = BeeSimulation.getInputData().getNectarStoreTime();
	private static final double KEEP_ALIVE_TIMER = BeeSimulation.getInputData().getKeepAliveTimer();
	private static final double INCUBATION_TIME = BeeSimulation.getInputData().getIncubationTime();
	private static final double INFECTION_PROBABILITY = BeeSimulation.getInputData().getInfectionProbability();
	private static final double FLY_BACK_TO_WRONG_HIVE_CHANCE = BeeSimulation.getInputData()
			.getFlyBackToWrongHiveChance();

	private double _initialTimeToLive = BeeSimulation.getInputData().getInitialTimeToLive();
	private double _initialTimeToLiveDueToInfection = BeeSimulation.getInputData().getInitialTimeToLiveDueToInfection();
	private double _timeToLive = _initialTimeToLive;
	private double _timeToLiveDueToInfection = _initialTimeToLive;
	private boolean _infected = false;
	private boolean _incubated = false;
	private int _capacity = MAX_NECTAR_CAPACITY;
	private Random _random;
	private BeeHive _home;
	private boolean _isWorker;

	private Bee(Position position, BeeHive home, boolean isWorker) {
		_position = position;
		_home = home;
		_isWorker = isWorker;
		_random = new Random();
	}

	/**
	 * Create a new <tt>Bee</tt> instance belonging to specified <tt>BeeHive</tt> home. The bee will spawn at the
	 * location of its home.
	 * 
	 * @param home
	 *            hive the <tt>Bee</tt> belongs to
	 * @return a new <tt>Bee</tt> instance linked to and located at the specified <tt>BeeHive</tt> instance
	 */
	public static Bee create(BeeHive home, boolean isWorker) {
		return new Bee(home.getPosition(), home, isWorker);
	}

	/**
	 * Create a new <tt>Bee</tt> instance belonging to specified <tt>BeeHive</tt> home at the specified
	 * <tt>Position</tt> position.
	 * 
	 * @param home
	 *            hive the <tt>Bee</tt> belongs to
	 * @param position
	 *            the <tt>Bee</tt> will spawn at
	 * @return a new <tt>Bee</tt> instance linked to the specified home and located at the specified position
	 */
	public static Bee createAtPosition(Position position, BeeHive home, boolean isWorker) {
		return new Bee(position, home, isWorker);
	}

	@Event
	public void collectNectarAtFlower(Flower flower) {
		applyInfectionActions();
		_capacity -= flower.harvestMaxNectar(_capacity);
		infoWithPosition("Collecting nectar (" + (MAX_NECTAR_CAPACITY - _capacity) + " / " + MAX_NECTAR_CAPACITY + ").");
		if (_capacity == 0) {
			scheduleIfNotDead("flyBack", NECTAR_COLLECTION_TIME);
		} else {
			scheduleIfNotDead("flyToFlower", NECTAR_COLLECTION_TIME, BeeSimulation.getEnvironment()
					.getRandomFlowerWithNectarCloseTo(_position));
		}
	}

	@Event
	public void flyBack() {
		BeeHive destination = tryToFindHome();
		double distance = _position.distance(destination.getPosition());
		double movementTime = MovementUtil.calculateMovementTime(distance, MOVEMENT_SPEED);
		moveTo(destination.getPosition(), movementTime);
		if (isAtHomeAt(destination)) {
			scheduleIfNotDead("storeNectar", movementTime);
		} else {
			scheduleIfNotDead("arriveAtWrongHive", movementTime, destination);
		}
		infoWithPosition("Flying back to the hive.");
	}

	public String getHomeName() {
		return _home.getName();
	}

	@Event
	public void arriveAtWrongHive(BeeHive destination) {
		infoWithPosition("Oops that's not home: " + destination.getName() + ".");
		applyInfectionActions();
		scheduleIfNotDead("flyBack", TimeUtil.minutes(1));
	}

	@Event
	public void storeNectar() {
		applyInfectionActions();
		if (isIncubated()) {
			// TODO check whether banned bees unload their nectar before getting
			// banned
			infoWithPosition("I am now banned from my hive due to infection.");
			scheduleIfNotDead("die", TimeUtil.seconds(0));
			return;
		}
		_home.storeNectar(MAX_NECTAR_CAPACITY - _capacity);
		infoWithPosition("Storing nectar (" + _home.getStoredNectar() + ").");
		_capacity = MAX_NECTAR_CAPACITY;
		scheduleIfNotDead("flyToFlower", STORE_TIME, BeeSimulation.getEnvironment().getRandomFlowerWithNectarCloseTo(
				_position));
	}

	@Event
	public void flyToFlower(Flower destination) {
		double movementTime = MovementUtil.calculateMovementTime(_position.distance(destination.getPosition()),
				MOVEMENT_SPEED);
		moveTo(destination.getPosition(), movementTime);
		scheduleIfNotDead("collectNectarAtFlower", movementTime, destination);
		infoWithPosition("Flying to flower.");
	}

	@Event
	public void die() {
		infoWithPosition("I am dead.");
		_home.reportDead(this);
	}

	@Event
	public void incubation() {
		infoWithPosition("Oh no! Now the other bees can see that I am sick.");
		_incubated = true;
	}

	@Event
	public void keepAlive() {
		scheduleIfNotDead("keepAlive", KEEP_ALIVE_TIMER);
	}

	public void becomeInfected() {
		if (!_infected) {
			infoWithPosition("I am infected. Incubation in " + INCUBATION_TIME + " seconds.");
			_infected = true;
			_timeToLiveDueToInfection = _initialTimeToLiveDueToInfection;
			scheduleParallelEventIfNotDead("incubation", INCUBATION_TIME);
		}
	}

	@Override
	public void initialize() {
		infoWithPosition("I am alive!");
		if (_isWorker) {
			scheduleIfNotDead("flyToFlower", TimeUtil.seconds(1), BeeSimulation.getEnvironment()
					.getRandomFlowerWithNectarCloseTo(_position));
		} else {
			scheduleIfNotDead("keepAlive", TimeUtil.seconds(1));
		}
	}

	private void applyInfectionActions() {
		List<Bee> beesAtPosition = BeeSimulation.getEnvironment().getBeesAt(this);
		for (Bee bee : beesAtPosition) {
			if (isInfected()) {
				if (_random.nextDouble() <= INFECTION_PROBABILITY) {
					// infect the other bee
					bee.becomeInfected();
				}
			} else if (bee.isInfected()) {
				if (_random.nextDouble() <= INFECTION_PROBABILITY) {
					// become infected by the other bee
					becomeInfected();
				}
			}
		}
	}

	private boolean willBeAliveIn(double seconds) {
		return (_timeToLiveDueToInfection > seconds && _timeToLive > seconds);
	}

	private void scheduleIfNotDead(String event, double time, Object... arguments) {
		if (willBeAliveIn(time)) {
			_timeToLive -= time;
			_timeToLiveDueToInfection -= time;
			schedule(event, time, arguments);
		} else {
			schedule("die", Math.min(_timeToLive, _timeToLiveDueToInfection));
		}
	}

	private void scheduleParallelEventIfNotDead(String event, double time, Object... arguments) {
		if (willBeAliveIn(time)) {
			schedule(event, time, arguments);
		} else {
			schedule("die", Math.min(_timeToLive, _timeToLiveDueToInfection));
		}
	}

	private BeeHive tryToFindHome() {
		BeeHive destination = _home;
		if (_random.nextDouble() <= FLY_BACK_TO_WRONG_HIVE_CHANCE) {
			destination = BeeSimulation.getEnvironment().getRandomBeeHiveCloseToPositionButNot(_home,
					_home.getPosition());
		}
		if (destination == null) {
			destination = _home;
		}
		return destination;
	}

	public boolean isIncubated() {
		return _incubated;
	}

	public boolean isInfected() {
		return _infected;
	}

	public boolean isWorker() {
		return _isWorker;
	}

	public boolean isAtHomeAt(BeeHive home) {
		return _home.equals(home);
	}

}