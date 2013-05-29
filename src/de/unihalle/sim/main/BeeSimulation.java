package de.unihalle.sim.main;

import java.awt.Point;

import org.mitre.sim.Simulation;

import de.unihalle.sim.entities.BeeHive;

public class BeeSimulation extends Simulation {

	private static final long serialVersionUID = 1L;

	private static final int SIMULATION_PACE = 100; // ms
	private static final double SIMULATION_TIME = 2.5; // s

	public static final int MIN_X_COORDINATE = -10;
	public static final int MAX_X_COORDINATE = 10;
	public static final int MIN_Y_COORDINATE = -10;
	public static final int MAX_Y_COORDINATE = 10;

	@Override
	public void initialize() {
		register(new BeeHive(new Point(5, 5), 5), "Mailand");
		register(new BeeHive(new Point(-5, -5), 5), "Rom");
		register(new BeeHive(new Point(5, -5), 5), "Neapel");
		register(new BeeHive(new Point(-5, 5), 5), "Turin");
		setTimeLast(SIMULATION_TIME);
		setPace(SIMULATION_PACE);
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
