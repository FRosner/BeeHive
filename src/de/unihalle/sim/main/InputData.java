package de.unihalle.sim.main;

import de.unihalle.sim.util.MovementUtil;
import de.unihalle.sim.util.TimeUtil;

public class InputData {

	// environmental data
	private int _numberOfFlowers = 8;
	private int _numberOfBeesPerHive = 2;

	// hive data
	private double _eggSpawnRate = TimeUtil.seconds(43.2);
	private double _initialInfectionPercentage = 0.01;
	private double _workerBeePercentage = 0.55;

	// bee data
	private double _flyBackToWrongHiveChance = 0.30;
	private double _infectionProbability = 0.1;
	private double _movementSpeed = MovementUtil.kilometersPerHour(5);
	private double _initialTimeToLive = TimeUtil.days(45);
	private double _initialTimeToLiveDueToInfection = TimeUtil.days(4);
	private double _incubationTime = TimeUtil.days(2);
	private double _keepAliveTimer = TimeUtil.minutes(1);
	private double _nectarStoreTime = TimeUtil.hours(5);
	private double _nectarCollectionTime = TimeUtil.minutes(30);
	private int _beeMaxNectarCapacity = 40;

	// flower data
	private int _flowerMaxNectarCapacity = 16;
	private double _nectarRefreshRate = TimeUtil.days(1) / _flowerMaxNectarCapacity;

	public int getNumberOfFlowers() {
		return _numberOfFlowers;
	}

	public int getNumberOfBeesPerHive() {
		return _numberOfBeesPerHive;
	}

	public double getEggSpawnRate() {
		return _eggSpawnRate;
	}

	public double getWorkerBeePercentage() {
		return _workerBeePercentage;
	}

	public double getInitialInfectionPercentage() {
		return _initialInfectionPercentage;
	}

	public double getFlyBackToWrongHiveChance() {
		return _flyBackToWrongHiveChance;
	}

	public double getInfectionProbability() {
		return _infectionProbability;
	}

	public double getMovementSpeed() {
		return _movementSpeed;
	}

	public double getInitialTimeToLive() {
		return _initialTimeToLive;
	}

	public double getInitialTimeToLiveDueToInfection() {
		return _initialTimeToLiveDueToInfection;
	}

	public double getIncubationTime() {
		return _incubationTime;
	}

	public double getKeepAliveTimer() {
		return _keepAliveTimer;
	}

	public double getNectarStoreTime() {
		return _nectarStoreTime;
	}

	public double getNectarCollectionTime() {
		return _nectarCollectionTime;
	}

	public int getBeeMaxNectarCapacity() {
		return _beeMaxNectarCapacity;
	}

	public int getFlowerMaxNectarCapacity() {
		return _flowerMaxNectarCapacity;
	}

	public double getNectarRefreshRate() {
		return _nectarRefreshRate;
	}

}
