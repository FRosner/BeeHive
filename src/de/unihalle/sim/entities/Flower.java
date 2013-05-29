package de.unihalle.sim.entities;

import java.util.Random;

import de.unihalle.sim.main.BeeSimulation;
import de.unihalle.sim.util.Event;
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
		Random random = new Random();
		int x = random.nextInt(BeeSimulation.MAX_X_COORDINATE - BeeSimulation.MIN_X_COORDINATE)
				+ BeeSimulation.MIN_X_COORDINATE;
		int y = random.nextInt(BeeSimulation.MAX_Y_COORDINATE - BeeSimulation.MIN_Y_COORDINATE)
				+ BeeSimulation.MIN_Y_COORDINATE;
		return createAtPosition(Position.createFromCoordinates(x, y));
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

	public int getNectarAmount() {
		return _nectar;
	}

}