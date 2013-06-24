package de.unihalle.sim.main;

import java.io.FileNotFoundException;
import java.util.List;

import org.mitre.sim.Simulation;

import com.google.common.collect.Lists;

import de.unihalle.sim.entities.BeeHive;
import de.unihalle.sim.entities.Flower;
import de.unihalle.sim.entities.Meadow;
import de.unihalle.sim.entities.PositionedEntity;
import de.unihalle.sim.util.Position;
import de.unihalle.sim.util.TimeUtil;

public class BeeSimulation extends Simulation {

	private static final long serialVersionUID = 1L;

	private static final int SIMULATION_PACE = 0;
	private static final double SIMULATION_TIME = TimeUtil.months(12);

	private static Environment _environment = new Environment(-500, 500, -500, 500);
	private static List<EventListener> _listeners = Lists.newArrayList();
	private static InputData _inputData = new InputData();
	private static BeeSimulation _simulation;

	private int _hiveGroups;
	private int _hivesPerGroup;

	public BeeSimulation(int hiveGroups, int hivesPerGroup) {
		_hiveGroups = hiveGroups;
		_hivesPerGroup = hivesPerGroup;
	}

	@Override
	public void initialize() {
		setTimeLast(SIMULATION_TIME);
		setPace(SIMULATION_PACE);
		createFlowers();
		createHives();
	}

	private void createHives() {
		int groupNumbers = _hiveGroups;
		int groupFixed = 0;
		int groupSize = _hivesPerGroup;
		int numbersOfGroupsSet = (int) Math.ceil(Math.sqrt(groupNumbers));

		int dimensionX = Math.abs(_environment.getMaxX()) + Math.abs(_environment.getMinX());
		int dimensionY = Math.abs(_environment.getMaxY()) + Math.abs(_environment.getMinY());

		int pixelX = Math.round(dimensionX / numbersOfGroupsSet);
		int pixelY = Math.round(dimensionY / numbersOfGroupsSet);

		for (int y = pixelY; y <= dimensionY; y += pixelY) {
			for (int x = pixelX; x <= dimensionX; x += pixelX) {

				if (groupFixed < groupNumbers) {

					int groupDimension;
					int groupCount = 0;
					if ((Math.sqrt(groupSize) % 1) == 0) {
						groupDimension = (int) Math.sqrt(groupSize);
					} else {
						groupDimension = (int) Math.sqrt(groupSize);
						groupDimension++;
					}

					for (int xg = 0; xg < groupDimension; xg++)
						for (int yg = 0; yg < groupDimension; yg++) {
							if (groupCount < groupSize) {
								registerHive(Position.createFromCoordinates((x - dimensionX / 2) - pixelX / 2 + xg,
										(y - dimensionY / 2) - pixelY / 2 + yg), _inputData.getNumberOfBeesPerHive(),
										"Hive" + groupFixed + "_" + groupCount);
								groupCount++;
							}
						}
				}
				groupFixed++;
			}
		}
	}

	private void createFlowers() {
		for (int i = 0; i < _inputData.getNumberOfFlowers(); i++) {
			registerFlower("Flower" + i);
		}
		register(new Meadow(), "Meadow");
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

	public static InputData getInputData() {
		return _inputData;
	}

	public static BeeSimulation getSimulation() {
		return _simulation;
	}

	public void stopSimulation() {
		simulationComplete();
		System.exit(0);
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
		_environment.addFlower(currentFlower);
	}

	public static void main(String[] args) throws Exception {

		BeeCommandLineParser arguments = BeeCommandLineParser.parse(args);

		_simulation = new BeeSimulation(arguments.getNumberOfGroups(), arguments.getGroupSize());

		if (arguments.showGui()) {
			BeeSimulation.addEventListener(new VisualisationEventListener());
		}

		if (arguments.generateReport()) {
			try {
				BeeSimulation.addEventListener(new ReportEventListener("report.csv"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		if (arguments.showControls()) {
			_simulation.setVisible(true);
		} else {
			_simulation.run();
		}
	}

}
