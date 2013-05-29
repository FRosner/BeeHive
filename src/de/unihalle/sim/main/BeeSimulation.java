package de.unihalle.sim.main;

import java.util.Collections;
import java.util.List;

import org.mitre.sim.Simulation;

import com.google.common.collect.Lists;

import de.unihalle.sim.entities.BeeHive;
import de.unihalle.sim.entities.Flower;
import de.unihalle.sim.util.Position;
import de.unihalle.sim.util.TimeUtil;

public class BeeSimulation extends Simulation {

	private static final long serialVersionUID = 1L;

	private static final int SIMULATION_PACE = 100; // ms
	private static final double SIMULATION_TIME = TimeUtil.minutes(60); // s

	public static final int MIN_X_COORDINATE = -10;
	public static final int MAX_X_COORDINATE = 10;
	public static final int MIN_Y_COORDINATE = -10;
	public static final int MAX_Y_COORDINATE = 10;

	private static List<Flower> _flowers;

	@Override
	public void initialize() {
		setTimeLast(SIMULATION_TIME);
		setPace(SIMULATION_PACE);
		createFlowers();
		createHives();
	}

	private void createHives() {
		register(new BeeHive(Position.createFromCoordinates(5, 5), 1), "Milan");
		// register(new BeeHive(Position.createFromCoordinates(0, 0), 1), "Rome");
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
		_flowers = Lists.newArrayList();
		for (int i = 1; i < 4; i++) {
			Flower currentFlower = Flower.create();
			register(currentFlower, "Flower" + i);
			_flowers.add(currentFlower);
		}
	}

	@Override
	public void simulationComplete() {
		info("Simulation complete.");
	}

	public static Flower getRandomFlower() {
		if (_flowers.size() <= 0) {
			System.err.println("No flowers created but tried to select one.");
			System.exit(1);
		}
		Collections.shuffle(_flowers);
		return _flowers.get(0);
	}

	public static void main(String[] args) {
		Simulation mySimulation = new BeeSimulation();
		mySimulation.run();
	}

}
