package de.unihalle.sim.main;

import org.mitre.sim.Simulation;

import de.unihalle.sim.entities.BeeHive;
import de.unihalle.sim.entities.Flower;
import de.unihalle.sim.util.Position;
import de.unihalle.sim.util.TimeUtil;

public class BeeSimulation extends Simulation {

	private static final long serialVersionUID = 1L;

	private static final int SIMULATION_PACE = 100;
	private static final double SIMULATION_TIME = TimeUtil.minutes(60); // s

	private static Environment _environment = new Environment(-10, 10, -10, 10);

	@Override
	public void initialize() {
		setTimeLast(SIMULATION_TIME);
		setPace(SIMULATION_PACE);
		createFlowers();
		createHives();
	}

	private void createHives() {
		registerHive(Position.createFromCoordinates(5, 5), 0, "Rome");
		registerHive(Position.createFromCoordinates(-5, -5), 0, "Milan");
		registerHive(Position.createFromCoordinates(5, -5), 0, "Naples");
		registerHive(Position.createFromCoordinates(-5, 5), 0, "Turin");
		// register(new BeeHive(Position.createFromCoordinates(0, 0), 1), "Palermo");
		// register(new BeeHive(Position.createFromCoordinates(0, 0), 1), "Genoa");
		// register(new BeeHive(Position.createFromCoordinates(0, 0), 1), "Bologna");
		// register(new BeeHive(Position.createFromCoordinates(0, 0), 1), "Florence");
		// register(new BeeHive(Position.createFromCoordinates(0, 0), 1), "Bari");
		// register(new BeeHive(Position.createFromCoordinates(0, 0), 1), "Catania");
	}

	private void createFlowers() {
		for (int i = 1; i < 4; i++) {
			registerFlower("Flower" + i);
		}
	}

	@Override
	public void simulationComplete() {
		info("Simulation complete.");
	}

	public static Environment getEnvironment() {
		return _environment;
	}

	private void registerHive(Position pos, int capacity, String name) {
		BeeHive newHive = new BeeHive(pos, capacity);
		register(newHive, name);
		_environment.addHive(newHive);
	}

	private void registerFlower(String name) {
		Flower currentFlower = Flower.create();
		register(currentFlower, name);
		_environment.addFlower(currentFlower);
	}

	public static void main(String[] args) {
		Simulation mySimulation = new BeeSimulation();
		mySimulation.run();
	}

}
