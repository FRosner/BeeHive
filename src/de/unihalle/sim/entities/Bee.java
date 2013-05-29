package de.unihalle.sim.entities;

import de.unihalle.sim.util.Event;
import de.unihalle.sim.util.Position;
import de.unihalle.sim.util.TimeUtil;

public class Bee extends PositionedEntity {

	private static int INITIAL_TIME_TO_LIVE = TimeUtil.seconds(30); // s
	private int _timeToLive = INITIAL_TIME_TO_LIVE;

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
		infoWithPosition("Flying back.");
		scheduleIfNotDead("collectNectar", TimeUtil.seconds(2));
	}

	@Event
	public void die() {
		infoWithPosition("I am dead.");
		_home.reportDead();
		_home.reportDead();
	}

	@Override
	public void initialize() {
		infoWithPosition("I am alive!");
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

	private void scheduleIfNotDead(String event, int time) {
		if (willBeAliveIn(time)) {
			_timeToLive -= time;
			schedule(event, time);
		} else {
			schedule("die", _timeToLive);
		}
	}

}