package de.unihalle.sim.main;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Random;

import de.unihalle.sim.util.MovementUtil;
import de.unihalle.sim.util.TimeUtil;

public class InputData {

	private final String DEFAULT_PROPERTY_FILE = "default.properties";
	private final String CUSTOM_PROPERTY_FILE = "custom.properties";

	private Properties _properties;

	private Random _random = new Random();

	// environmental data
	private int _numberOfFlowersPerBee;
	private int _numberOfBeesPerHive;

	// hive data
	private double _eggSpawnRate;
	private double _initialInfectionPercentage;
	private double _workerBeePercentage;
	private double _collapseThreshold;

	// bee data
	private double _flyBackToWrongHiveChance;
	private double _infectionProbability;
	private double _movementSpeed;
	private double _initialTimeToLiveStandardDeviation;
	private double _initialTimeToLiveMean;
	private double _initialTimeToLiveDueToInfection;
	private double _incubationTime;
	private double _keepAliveTimer;
	private double _nectarStoreTime;
	private double _nectarCollectionTime;
	private int _beeMaxNectarCapacity;

	// flower data
	private int _flowerMaxNectarCapacity;
	private double _nectarRefreshRate;

	public InputData() {
		loadDefaultProperties();
		loadCustomProperties();
		applyProperties();
	}

	private void loadDefaultProperties() {
		System.out.println("Loading " + DEFAULT_PROPERTY_FILE);
		try {
			_properties = new Properties();
			_properties.load(new FileInputStream(DEFAULT_PROPERTY_FILE));
		} catch (Exception e) {
			System.err.println("Could not load default properties.");
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void loadCustomProperties() {
		File customPropertyFile = new File(CUSTOM_PROPERTY_FILE);
		if (customPropertyFile.isFile()) {
			System.out.println("Loading " + CUSTOM_PROPERTY_FILE);
			try {
				_properties.load(new FileInputStream(customPropertyFile));
			} catch (Exception e) {
				System.err.println("Loading custom properties failed.");
				e.printStackTrace();
			}
		}

	}

	private void applyProperties() {
		_numberOfFlowersPerBee = Integer.parseInt(_properties.getProperty("numberOfFlowersPerBee"));
		_numberOfBeesPerHive = Integer.parseInt(_properties.getProperty("numberOfBeesPerHive"));
		_eggSpawnRate = TimeUtil.seconds(Double.parseDouble(_properties.getProperty("eggSpawnRate.secs")));
		_initialInfectionPercentage = Double.parseDouble(_properties.getProperty("initialInfectionPercentage"));
		_workerBeePercentage = Double.parseDouble(_properties.getProperty("workerBeePercentage"));
		_collapseThreshold = Double.parseDouble(_properties.getProperty("collapseThreshold"));
		_flyBackToWrongHiveChance = Double.parseDouble(_properties.getProperty("flyBackToWrongHiveChance"));
		_infectionProbability = Double.parseDouble(_properties.getProperty("infectionProbability"));
		_movementSpeed = MovementUtil.kilometersPerHour(Double.parseDouble(_properties
				.getProperty("movementSpeed.kmph")));
		_initialTimeToLiveStandardDeviation = TimeUtil.days(Double.parseDouble(_properties
				.getProperty("initialTimeToLiveStandardDeviation.days")));
		_initialTimeToLiveMean = TimeUtil.days(Double
				.parseDouble(_properties.getProperty("initialTimeToLiveMean.days")));
		_initialTimeToLiveDueToInfection = TimeUtil.days(Double.parseDouble(_properties
				.getProperty("initialTimeToLiveDueToInfection.days")));
		_incubationTime = TimeUtil.days(Double.parseDouble(_properties.getProperty("incubationTime.days")));
		_keepAliveTimer = TimeUtil.minutes(Double.parseDouble(_properties.getProperty("keepAliveTimer.mins")));
		_nectarStoreTime = TimeUtil.hours(Double.parseDouble(_properties.getProperty("nectarStoreTime.hours")));
		_nectarCollectionTime = TimeUtil.minutes(Double.parseDouble(_properties
				.getProperty("nectarCollectionTime.mins")));
		_beeMaxNectarCapacity = Integer.parseInt(_properties.getProperty("beeMaxNectarCapacity"));
		_flowerMaxNectarCapacity = Integer.parseInt(_properties.getProperty("flowerMaxNectarCapacity"));
		_nectarRefreshRate = TimeUtil.days(Double.parseDouble(_properties.getProperty("nectarRefreshRate.days")))
				/ _flowerMaxNectarCapacity;
	}

	private double sampleGaussian(double mean, double standardDeviation) {
		return _random.nextGaussian() * standardDeviation + mean;
	}

	public int getNumberOfFlowersPerBee() {
		return _numberOfFlowersPerBee;
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
		return sampleGaussian(_initialTimeToLiveMean, _initialTimeToLiveStandardDeviation);
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

	public double getCollapseThreshold() {
		return _collapseThreshold;
	}

}
