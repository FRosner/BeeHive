package de.unihalle.sim.entities;

import de.unihalle.sim.main.BeeSimulation;
import de.unihalle.sim.util.Position;

public class Flower extends PositionedEntity {

	private static final int MAX_NECTAR_CAPACITY = BeeSimulation.getInputData().getFlowerMaxNectarCapacity();
	private static final double KEEP_ALIVE_TIMER = BeeSimulation.getInputData().getKeepAliveTimer();

	private int _nectar;

	public static class FlowerFactory {

		BeeSimulation _simulation;

		private FlowerFactory(BeeSimulation simulation) {
			_simulation = simulation;
		}

		/**
		 * Create a new <tt>Flower</tt> instance at a random position. The position will be in range of the min and max
		 * coordinates specified in the <tt>BeeSimulation</tt>.
		 * 
		 * @return a new <tt>Flower</tt> instance at random position
		 */
		public Flower createFlower() {
			return createFlowerAtPosition(BeeSimulation.getEnvironment().getRandomValidPosition());
		}

		/**
		 * Create a new <tt>Flower</tt> instance at the specified position. The position should be in range of the min
		 * and max coordinates specified in the <tt>BeeSimulation</tt>. However this will not be checked.
		 * 
		 * @param position
		 *            of the new flower
		 * @return a new <tt>Flower</tt> instance at the specified position
		 */
		public Flower createFlowerAtPosition(Position position) {
			return new Flower(position, MAX_NECTAR_CAPACITY);
		}

	}

	public static FlowerFactory createFactory(BeeSimulation simulation) {
		return new FlowerFactory(simulation);
	}

	private Flower(Position position, int nectar) {
		_position = position;
		_nectar = nectar;
	}

	public void refreshNectar() {
		if (_nectar < MAX_NECTAR_CAPACITY) {
			_nectar++;
		}
	}

	@Override
	public void initialize() {
		infoWithPosition("I am alive!");
		schedule("keepAlive", KEEP_ALIVE_TIMER);
	}

	@Event
	public void keepAlive() {
		schedule("keepAlive", KEEP_ALIVE_TIMER);
	}

	/**
	 * Harvests and returns the maximum available amount of nectar from a flower. This is also limited by the capacity
	 * of the harvester.
	 * 
	 * @param remainingBeeCapacity
	 *            remaining capacity of the harvester
	 * @return amount of harvested nectar
	 */
	public int harvestMaxNectar(int remainingBeeCapacity) {
		int nectarTaken = 0;
		if (remainingBeeCapacity <= _nectar) {
			nectarTaken = remainingBeeCapacity;
		} else {
			nectarTaken = _nectar;
		}
		_nectar -= nectarTaken;
		return nectarTaken;
	}

	/**
	 * Returns the available nectar of the flower.
	 * 
	 * @return available nectar
	 */
	public int getNectarAmount() {
		return _nectar;
	}

	public int getMaxNectarAmount() {
		return MAX_NECTAR_CAPACITY;
	}

}
