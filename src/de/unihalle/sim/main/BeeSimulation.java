package de.unihalle.sim.main;

import org.mitre.sim.Simulation;

import de.unihalle.sim.entities.BeeHive;
import de.unihalle.sim.entities.Flower;
import de.unihalle.sim.util.Position;
import de.unihalle.sim.util.TimeUtil;

public class BeeSimulation extends Simulation {

	private static final long serialVersionUID = 1L;

	private static final int SIMULATION_PACE = 1000; // ms
	private static final double SIMULATION_TIME = TimeUtil.minutes(2); // s

	public static final int MIN_X_COORDINATE = -10;
	public static final int MAX_X_COORDINATE = 10;
	public static final int MIN_Y_COORDINATE = -10;
	public static final int MAX_Y_COORDINATE = 10;

	@Override
	public void initialize() {
		setTimeLast(SIMULATION_TIME);
		setPace(SIMULATION_PACE);
		createFlowers();
		createHives();
	}

	private void createHives() {
		register(new BeeHive(Position.createFromCoordinates(5, 5), 1), "Milan");
		// register(new BeeHive(new Point(-5, -5), 5), "Rome");
		// register(new BeeHive(new Point(5, -5), 5), "Naples");
		// register(new BeeHive(new Point(-5, 5), 5), "Turin");
	}

	private void createFlowers() {
		for (int i = 1; i < 4; i++) {
			register(Flower.create(), "Flower" + i);
		}
	}

	@Override
	public void simulationComplete() {
		info("Simulation complete.");
	}

	public static void main(String[] args) {
		Simulation mySimulation = new BeeSimulation();
		mySimulation.run();
	}

}
