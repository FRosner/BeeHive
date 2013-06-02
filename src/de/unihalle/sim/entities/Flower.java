package de.unihalle.sim.entities;

import de.unihalle.sim.main.BeeSimulation;
import de.unihalle.sim.util.Position;
import de.unihalle.sim.util.TimeUtil;

public class Flower extends PositionedEntity {

	private static final int MAX_NECTAR_CAPACITY = 5;
	private static final double NECTAR_REFRESH_RATE = TimeUtil.seconds(10);

	private int _nectar;

	private Flower(Position position, int nectar) {
		_position = position;
		_nectar = nectar;
	}

	/**
	 * Create a new <tt>Flower</tt> instance at a random position. The position will be in range of the min and max
	 * coordinates specified in the <tt>BeeSimulation</tt>.
	 * 
	 * @return a new <tt>Flower</tt> instance at random position
	 */
	public static Flower create() {
		return createAtPosition(BeeSimulation.getEnvironment().getRandomValidPosition());
	}

	/**
	 * Create a new <tt>Flower</tt> instance at the specified position. The position should be in range of the min and
	 * max coordinates specified in the <tt>BeeSimulation</tt>. However this will not be checked.
	 * 
	 * @param position
	 *            of the new flower
	 * @return a new <tt>Flower</tt> instance at the specified position
	 */
	public static Flower createAtPosition(Position position) {
		return new Flower(position, MAX_NECTAR_CAPACITY);
	}

	@Event
	public void refreshNectar() {
		if (_nectar < MAX_NECTAR_CAPACITY) {
			_nectar++;
			info("Refreshing nectar (" + getNectarAmount() + " / " + MAX_NECTAR_CAPACITY + ").");
		}
		schedule("refreshNectar", NECTAR_REFRESH_RATE);
	}

	@Override
	public void initialize() {
		infoWithPosition("I am alive!");
		schedule("refreshNectar", NECTAR_REFRESH_RATE);
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

}
