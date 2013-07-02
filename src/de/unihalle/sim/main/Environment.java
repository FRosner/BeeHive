package de.unihalle.sim.main;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;

import com.google.common.collect.Lists;

import de.unihalle.sim.entities.Bee;
import de.unihalle.sim.entities.BeeHive;
import de.unihalle.sim.entities.Flower;
import de.unihalle.sim.entities.PositionedEntity;
import de.unihalle.sim.util.Position;

public class Environment implements Cloneable {

	private List<Flower> _flowers = Lists.newArrayList();
	private List<BeeHive> _hives = Lists.newArrayList();
	private List<Bee> _bees = Lists.newArrayList();

	private int _minX = -10;
	private int _maxX = 10;
	private int _minY = -10;
	private int _maxY = 10;

	private Random _random = new Random();
	private NormalDistribution _normalDistribution = new NormalDistribution(0, 10);

	public Environment() {
	}

	public Environment(int minX, int maxX, int minY, int maxY) {
		_minX = minX;
		_maxX = maxX;
		_minY = minY;
		_maxY = maxY;
	}

	public int getMaxX() {
		return _maxX;
	}

	public int getMinX() {
		return _minX;
	}

	public int getMaxY() {
		return _maxY;
	}

	public int getMinY() {
		return _minY;
	}

	public boolean addFlower(Flower f) {
		return _flowers.add(f);
	}

	public boolean addHive(BeeHive h) {
		return _hives.add(h);
	}

	public boolean addBee(Bee b) {
		return _bees.add(b);
	}

	public boolean removeBee(Bee b) {
		return _bees.remove(b);
	}

	public List<Bee> getBees() {
		return _bees;
	}

	public List<BeeHive> getBeeHives() {
		return _hives;
	}

	public List<Flower> getFlowers() {
		return _flowers;
	}

	public int getNumberOfBees() {
		return _bees.size();
	}

	public int getNumberOfFlowers() {
		return _flowers.size();
	}

	public int getNumberOfHives() {
		return _hives.size();
	}

	public int getNumberOfCollapsedHives() {
		int numberOfCollapsedHives = 0;
		for (BeeHive hive : _hives) {
			if (hive.isCollapsed()) {
				numberOfCollapsedHives++;
			}
		}
		return numberOfCollapsedHives;
	}

	public Position getRandomValidPosition() {
		return Position.createRandomPositionWithin(_minX, _maxX, _minY, _maxY);
	}

	public int getNumberOfInfectedBees() {
		int numberOfInfectedBees = 0;
		for (Bee bee : _bees) {
			if (bee.isInfected()) {
				numberOfInfectedBees++;
			}
		}
		return numberOfInfectedBees;
	}

	/**
	 * Return a randomly selected flower. Each flower has the same probability.
	 * 
	 * @return randomly selected flower
	 */
	public Flower getRandomFlower() {
		if (_flowers.size() <= 0) {
			System.err.println("No flowers created but tried to select one.");
			System.exit(1);
		}
		Collections.shuffle(_flowers);
		return _flowers.get(0);
	}

	/**
	 * Returns a randomly selected flower. Flowers closer to the specified position have higher probability.
	 * 
	 * @param position
	 * @return randomly selected flower, probably close to position
	 */
	public Flower getRandomFlowerWithNectarCloseTo(Position position) {
		if (_flowers.size() <= 0) {
			System.err.println("No flowers created but tried to select one.");
			System.exit(1);
		}
		Flower randomFlower = null;
		double minRandomValue = Double.POSITIVE_INFINITY;
		double currentRandomValue;
		for (Flower f : _flowers) {
			if (f.getNectarAmount() == 0) {
				continue;
			}
			currentRandomValue = f.getPosition().distance(position) * _random.nextDouble();
			if (Double.compare(currentRandomValue, minRandomValue) < 0) {
				randomFlower = f;
				minRandomValue = currentRandomValue;
			}
		}
		// no flowers with nectar available
		if (randomFlower == null) {
			return getRandomFlower();
		}
		return randomFlower;
	}

	/**
	 * Returns a list of all bees that are currently at the specified position.
	 * 
	 * @param position
	 * @return list of all bees at the specified position
	 */
	public List<Bee> getBeesAt(Position position) {
		List<Bee> beesAt = Lists.newArrayList();
		for (Bee bee : _bees) {
			if (bee.getPosition().equals(position) && !bee.isMoving()) {
				beesAt.add(bee);
			}
		}
		return beesAt;
	}

	/**
	 * Returns a list of all bees that are currently at the same position as the specified entity.
	 * 
	 * @param position
	 * @return list of all bees at the position of the specified entity
	 */
	public List<Bee> getBeesAt(PositionedEntity entity) {
		return getBeesAt(entity.getPosition());
	}

	/**
	 * Returns a random hive from all hives in the environment but the one specified. If no other hives are available,
	 * <tt>null</tt> is returned.
	 * 
	 * @param hive
	 *            to be excluded from selection
	 * @return random hive
	 */
	public BeeHive getRandomBeeHiveButNot(BeeHive hive) {
		if (_hives.size() <= 0) {
			System.err.println("No hives created but tried to select one.");
			System.exit(1);
		}
		List<BeeHive> tempHives = Lists.newArrayList(_hives);
		tempHives.remove(hive);
		if (tempHives.size() == 0) {
			return null;
		}
		Collections.shuffle(tempHives);
		return tempHives.get(0);
	}

	/**
	 * Returns a random hive from all hives in the environment. Hives closer to the specified position have a higher
	 * probability to be selected.
	 * 
	 * @param pos
	 *            that the random hive will be probable close to
	 * @return random hive
	 */
	public BeeHive getRandomBeeHiveCloseToPosition(Position pos) {
		List<Double> odds = Lists.newArrayList();
		double oddsSum = 0;
		for (BeeHive h : _hives) {
			double odd = _normalDistribution.density(h.getPosition().distance(pos)) / _normalDistribution.density(0);
			oddsSum += odd;
			odds.add(odd);
		}
		double cumulativeProbability = 0;
		double random = _random.nextDouble();
		for (int i = 0; i < odds.size(); i++) {
			double probability = (odds.get(i) / oddsSum);
			cumulativeProbability += probability;
			if (random < cumulativeProbability) {
				return _hives.get(i);
			}
		}
		System.err.println("Cumulative probabilies seem not to sum to 1.");
		System.exit(1);
		return null;
	}

	/**
	 * Infects a randomly selected subset of bees for each hive.
	 * 
	 * @param infectionPercentage
	 *            for each hive
	 */
	public void applyInitialInfectionToHive(BeeHive hive, double infectionPercentage) {
		int numberOfInfectedBeesPerHive;
		List<Bee> bees = getBeesAt(hive);
		Collections.shuffle(bees);
		numberOfInfectedBeesPerHive = (int) (bees.size() * infectionPercentage);
		for (int i = 0; i < numberOfInfectedBeesPerHive; i++) {
			bees.get(i).becomeInfected();
		}
	}

	public void refreshFlowers() {
		for (Flower f : _flowers) {
			f.refreshNectar();
		}
	}

	/**
	 * Clones the environment and all of its fields. However the random number generator is <i>not</i> cloned so the
	 * environments will not produce the same results when used in a simulation.
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Environment clone() {
		Environment clonedEnvironment = new Environment(_minX, _maxX, _minY, _maxY);
		clonedEnvironment._bees = Lists.newArrayList(_bees);
		clonedEnvironment._flowers = Lists.newArrayList(_flowers);
		clonedEnvironment._hives = Lists.newArrayList(_hives);
		return clonedEnvironment;
	}

}
