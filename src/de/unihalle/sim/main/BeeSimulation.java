package de.unihalle.sim.main;

import java.util.List;

import org.mitre.sim.Simulation;

import com.google.common.collect.Lists;

import de.unihalle.sim.entities.BeeHive;
import de.unihalle.sim.entities.Flower;
import de.unihalle.sim.entities.PositionedEntity;
import de.unihalle.sim.util.Position;
import de.unihalle.sim.util.TimeUtil;

public class BeeSimulation extends Simulation {

	private static final long serialVersionUID = 1L;

	private static final int SIMULATION_PACE = 100;
	private static final double SIMULATION_TIME = TimeUtil.minutes(60); // s

	private static Environment _environment = new Environment(-10, 10, -10, 10);
	private static List<EventListener> _listeners = Lists.newArrayList();

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
		for (EventListener e : _listeners) {
			e.close();
		}
	}

	public static Environment getEnvironment() {
		return _environment;
	}

	public static void notifyListeners(PositionedEntity entity) {
		for (EventListener e : _listeners) {
			e.notify(entity);
		}
	}

	public static void addEventListener(EventListener e) {
		_listeners.add(e);
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

	public static void main(String[] args) throws Exception {
		BeeSimulation.addEventListener(new DummyEventListener());
		BeeSimulation.addEventListener(new ReportEventListener("report.csv"));
		Simulation mySimulation = new BeeSimulation();
		mySimulation.run();
	}

}
