package de.unihalle.sim.entities;

import de.unihalle.sim.util.Event;
import de.unihalle.sim.util.MovementUtil;
import de.unihalle.sim.util.Position;
import de.unihalle.sim.util.TimeUtil;

public class Bee extends PositionedEntity {

	private static double INITIAL_TIME_TO_LIVE = TimeUtil.seconds(30); // s
	private double _timeToLive = INITIAL_TIME_TO_LIVE;
	private static final double MOVEMENT_SPEED = MovementUtil.meterPerSecond(1); // m/sec

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
	public void collectNectar() {
		infoWithPosition("Collecting nectar.");
		scheduleIfNotDead("flyBack", TimeUtil.seconds(2));
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
		infoWithPosition("Home sweet home.");
		scheduleIfNotDead("flyToFlower", TimeUtil.seconds(2));
	}

	@Event
	public void flyToFlower() {
		infoWithPosition("Flying to flower.");
		double distance = _position.distance(Position.create());
		double movementTime = MovementUtil.calculateMovementTime(distance, MOVEMENT_SPEED);
		scheduleIfNotDead("collectNectar", movementTime);
		moveTo(Position.create());
	}

	@Event
	public void die() {
		infoWithPosition("I am dead.");
		_home.reportDead();
	}

	@Override
	public void initialize() {
		infoWithPosition("I am alive!");
		scheduleIfNotDead("flyToFlower", TimeUtil.seconds(2));
	}

	private boolean willBeAliveIn(double seconds) {
		return (_timeToLive > seconds);
	}

	private void scheduleIfNotDead(String event, double time) {
		if (willBeAliveIn(time)) {
			_timeToLive -= time;
			schedule(event, time);
		} else {
			schedule("die", _timeToLive);
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