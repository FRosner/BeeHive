package de.unihalle.sim.entities;

import java.awt.Point;

import de.unihalle.sim.util.TimeUtil;

public class Bee extends PositionedEntity {

	private static int INITIAL_TIME_TO_LIVE = TimeUtil.seconds(30); // s
	private int _timeToLive = INITIAL_TIME_TO_LIVE;

	private BeeHive _home;

	private Bee(Point position, BeeHive home) {
		_position = position;
		_home = home;
	}

	public static Bee create(BeeHive home) {
		return new Bee(home.getPosition(), home);
	}

	public static Bee createAtPosition(Point position, BeeHive home) {
		return new Bee(position, home);
	}

	public void collectNectar() {
		info("Collecting nectar.");
		int arrivalTime = TimeUtil.seconds(2);
		if (willBeAliveIn(arrivalTime)) {
			_timeToLive -= arrivalTime;
			schedule("flyBack", arrivalTime);
		} else {
			schedule("die", _timeToLive);
		}
	}

	public void flyBack() {
		info("Flying back.");
		int arrivalTime = TimeUtil.seconds(2);
		if (willBeAliveIn(arrivalTime)) {
			_timeToLive -= arrivalTime;
			schedule("collectNectar", arrivalTime);
		} else {
			schedule("die", _timeToLive);
		}
	}

	public void die() {
		info("I am dead.");
	}

	@Override
	public void initialize() {
		info("I am alive!");
		int arrivalTime = TimeUtil.seconds(2);
		if (willBeAliveIn(arrivalTime)) {
			_timeToLive -= arrivalTime;
			schedule("collectNectar", arrivalTime);
		}

	}

	public boolean isAtHome(BeeHive home) {
		return _home.equals(home);
	}

	private boolean willBeAliveIn(int seconds) {
		return (_timeToLive > seconds);
	}

}