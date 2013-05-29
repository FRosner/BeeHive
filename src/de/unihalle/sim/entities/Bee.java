package de.unihalle.sim.entities;

import de.unihalle.sim.main.BeeSimulation;
import de.unihalle.sim.util.Event;
import de.unihalle.sim.util.MovementUtil;
import de.unihalle.sim.util.Position;
import de.unihalle.sim.util.TimeUtil;

public class Bee extends PositionedEntity {

	private static final double MOVEMENT_SPEED = MovementUtil.meterPerSecond(1); // m/sec
	private static double INITIAL_TIME_TO_LIVE = TimeUtil.seconds(30); // s
	private static int MAX_CAPACITY = 3;
	private double _timeToLive = INITIAL_TIME_TO_LIVE;
	private int _capacity = MAX_CAPACITY;

	private BeeHive _home;

	private Bee(Position position, BeeHive home) {
		_position = position;
		_home = home;
	}

	public static Bee create(BeeHive home) {
		return new Bee(home.getPosition(), home);
	}

	public static Bee createAtPosition(Position position, BeeHive home) {
		return new Bee(position, home);
	}

	@Event
	public void collectNectarAtFlower(Flower flower) {
		infoWithPosition("Collecting nectar.");
		_capacity -= flower.harvestMaxNectar(_capacity);
		if (_capacity == 0) {
			scheduleIfNotDead("flyBack", TimeUtil.seconds(2));
		} else {
			scheduleIfNotDead("flyToFlower", TimeUtil.seconds(2), BeeSimulation.getRandomFlower());
		}
	}

	@Event
	public void flyBack() {
		infoWithPosition("Flying back to the hive.");
		double distance = _position.distance(_home.getPosition());
		double movementTime = MovementUtil.calculateMovementTime(distance, MOVEMENT_SPEED);
		scheduleIfNotDead("storeNectar", movementTime);
		moveTo(_home.getPosition());
	}

	@Event
	public void storeNectar() {
		infoWithPosition("Home, sweet home.");
		_capacity = MAX_CAPACITY;
		scheduleIfNotDead("flyToFlower", TimeUtil.seconds(2), BeeSimulation.getRandomFlower());
	}

	@Event
	public void flyToFlower(Flower destination) {
		infoWithPosition("Flying to flower.");
		double movementTime = MovementUtil.calculateMovementTime(_position.distance(destination.getPosition()),
				MOVEMENT_SPEED);
		scheduleIfNotDead("collectNectarAtFlower", movementTime, destination);
		moveTo(destination.getPosition());
	}

	@Event
	public void die() {
		infoWithPosition("I am dead.");
		_home.reportDead();
	}

	@Override
	public void initialize() {
		infoWithPosition("I am alive!");
		scheduleIfNotDead("flyToFlower", TimeUtil.seconds(2), BeeSimulation.getRandomFlower());
	}

	private boolean willBeAliveIn(double seconds) {
		return (_timeToLive > seconds);
	}

	private void scheduleIfNotDead(String event, double time, Object... arguments) {
		if (willBeAliveIn(time)) {
			_timeToLive -= time;
			schedule(event, time, arguments);
		} else {
			schedule("die", _timeToLive, arguments);
		}
	}

	private void moveTo(Position pos) {
		_position.x = pos.x;
		_position.y = pos.y;
	}

	public boolean isAtHome(BeeHive home) {
		return _home.equals(home);
	}

}