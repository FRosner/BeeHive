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
		BeeHive newHive;
		newHive = new BeeHive(Position.createFromCoordinates(5, 5), 1);
		register(newHive, "Rome");
		_environment.addHive(newHive);
		// newHive = new BeeHive(Position.createFromCoordinates(5, 5), 1);
		// register(newHive, "Milan");
		// _hives.add(newHive);
		// register(new BeeHive(Position.createFromCoordinates(0, 0), 1), "Naples");
		// register(new BeeHive(Position.createFromCoordinates(0, 0), 1), "Turin");
		// register(new BeeHive(Position.createFromCoordinates(0, 0), 1), "Palermo");
		// register(new BeeHive(Position.createFromCoordinates(0, 0), 1), "Genoa");
		// register(new BeeHive(Position.createFromCoordinates(0, 0), 1), "Bologna");
		// register(new BeeHive(Position.createFromCoordinates(0, 0), 1), "Florence");
		// register(new BeeHive(Position.createFromCoordinates(0, 0), 1), "Bari");
		// register(new BeeHive(Position.createFromCoordinates(0, 0), 1), "Catania");
	}

	private void createFlowers() {
		for (int i = 1; i < 4; i++) {
			Flower currentFlower = Flower.create();
			register(currentFlower, "Flower" + i);
			_environment.addFlower(currentFlower);
		}
		for (int i = 1; i < 100; i++)
			System.err.println(_environment.getRandomFlowerCloseTo(Position.create()).getPosition());
		System.exit(1);
	}

	@Override
	public void simulationComplete() {
		info("Simulation complete.");
	}

	public static Environment getEnvironment() {
		return _environment;
	}

	public static void main(String[] args) {
		Simulation mySimulation = new BeeSimulation();
		mySimulation.run();
	}

}
