package de.unihalle.sim.main;

import java.io.FileNotFoundException;
import java.util.List;

import org.mitre.sim.Simulation;

import com.google.common.collect.Lists;

import de.unihalle.sim.entities.Bee;
import de.unihalle.sim.entities.Bee.BeeFactory;
import de.unihalle.sim.entities.BeeHive;
import de.unihalle.sim.entities.BeeHive.BeeHiveFactory;
import de.unihalle.sim.entities.Flower;
import de.unihalle.sim.entities.Flower.FlowerFactory;
import de.unihalle.sim.entities.Meadow;
import de.unihalle.sim.entities.PositionedEntity;
import de.unihalle.sim.util.Position;
import de.unihalle.sim.util.TimeUtil;

public class BeeSimulation extends Simulation {

	private static final long serialVersionUID = 1L;

	private static final int SIMULATION_PACE = 0;
	private static final double SIMULATION_TIME = TimeUtil.months(12);

	private Environment _environment;
	private List<EventListener> _listeners = Lists.newArrayList();
	private static InputData _inputData = new InputData();

	private BeeFactory _beeFactory;
	private BeeHiveFactory _hiveFactory;
	private FlowerFactory _flowerFactory;
	private int _hiveGroups;
	private int _hivesPerGroup;

	public BeeSimulation(int hiveGroups, int hivesPerGroup, Environment environment) {
		_hiveGroups = hiveGroups;
		_hivesPerGroup = hivesPerGroup;
		_environment = environment;
		_beeFactory = Bee.createFactory(this);
		_hiveFactory = BeeHive.createFactory(this);
		_flowerFactory = Flower.createFactory(this);
	}

	@Override
	public void initialize() {
		setTimeLast(SIMULATION_TIME);
		setPace(SIMULATION_PACE);
		createFlowers();
		createHives();
	}

	private void createHiveGroups(int groupDimension, int groupCount, int groupSize, int x, int y, int dimensionX,
			int dimensionY, int pixelX, int pixelY, int groupFixed, int fullLineSwitch) {
		for (int yg = 0; yg < groupDimension; yg++) {
			for (int xg = 0; xg < groupDimension; xg++) {
				if (groupCount < groupSize) {

					int lastPositionedHiveX;
					int lastPositionedHiveY;

					if (fullLineSwitch == 1) {
						lastPositionedHiveX = (x - dimensionX / 2) - pixelX / 2 + xg - groupDimension / 2;
						lastPositionedHiveY = (y - dimensionY / 2) - pixelY / 2 + yg - groupDimension / 2;
					} else {
						lastPositionedHiveX = (x - dimensionX / 2) + xg - groupDimension / 2;
						lastPositionedHiveY = (y - dimensionY / 2) + yg - pixelY / 2 - groupDimension / 2;
					}

					registerHive(Position.createFromCoordinates(lastPositionedHiveX, lastPositionedHiveY),
							_inputData.getNumberOfBeesPerHive(), "Hive" + groupFixed + "_" + groupCount);

					groupCount++;
				}
			}
		}
	}

	private void createHives() {
		int groupNumbers = _hiveGroups;
		int groupFixed = 0;
		int groupSize = _hivesPerGroup;
		int numbersOfGroupsSet = (int) Math.ceil(Math.sqrt(groupNumbers));

		int dimensionX = Math.abs(_environment.getMaxX()) + Math.abs(_environment.getMinX());
		int dimensionY = Math.abs(_environment.getMaxY()) + Math.abs(_environment.getMinY());

		int hivesPrintedInLastLine = ((int) Math.pow(numbersOfGroupsSet, 2)) - groupNumbers;
		int additionalVerticalLinesToPrint = (int) Math.ceil(hivesPrintedInLastLine / numbersOfGroupsSet);
		hivesPrintedInLastLine = numbersOfGroupsSet - (hivesPrintedInLastLine % numbersOfGroupsSet);

		int stepsInXdirection = Math.round(dimensionX / numbersOfGroupsSet);
		int stepsInYdirection;
		if (additionalVerticalLinesToPrint > 0) {

			stepsInYdirection = Math.round(dimensionY / (numbersOfGroupsSet - additionalVerticalLinesToPrint));
		} else {
			stepsInYdirection = Math.round(dimensionY / (numbersOfGroupsSet));
		}

		int actualExtraHiveToPrint = 1;
		boolean alreadyPrintedaHive = false;
		int extraHiveX = 0;
		int extraHiveY = 0;

		int groupDimension = (int) Math.ceil(Math.sqrt(groupSize));
		int groupCount = 0;
		int lineCount = 0;

		for (int y = stepsInYdirection; y <= stepsInYdirection * numbersOfGroupsSet; y += stepsInYdirection) {
			for (int x = stepsInXdirection; x <= stepsInXdirection * numbersOfGroupsSet; x += stepsInXdirection) {

				if (groupFixed < groupNumbers - hivesPrintedInLastLine && lineCount < numbersOfGroupsSet) {

					createHiveGroups(groupDimension, groupCount, groupSize, x, y, dimensionX, dimensionY,
							stepsInXdirection, stepsInYdirection, groupFixed, 1);
					groupFixed++;
					lineCount++;
				} else if (groupFixed >= groupNumbers - hivesPrintedInLastLine && lineCount < numbersOfGroupsSet) {
					lineCount++;
					if (actualExtraHiveToPrint <= hivesPrintedInLastLine) {

						if (hivesPrintedInLastLine == numbersOfGroupsSet) {
							if (!alreadyPrintedaHive) {
								extraHiveX = 0 + (dimensionX / (hivesPrintedInLastLine));
								extraHiveY = y;
								alreadyPrintedaHive = true;
							}

							createHiveGroups(groupDimension, groupCount, groupSize, extraHiveX, y, dimensionX,
									dimensionY, stepsInXdirection, stepsInYdirection, groupFixed, 1);
							groupFixed++;

							extraHiveX += (dimensionX / (hivesPrintedInLastLine));
							extraHiveY += (dimensionY / hivesPrintedInLastLine);
						} else {
							if (!alreadyPrintedaHive) {
								extraHiveX = 0 + (dimensionX / (hivesPrintedInLastLine + 1));
								extraHiveY = y;
								alreadyPrintedaHive = true;
							}

							createHiveGroups(groupDimension, groupCount, groupSize, extraHiveX, extraHiveY, dimensionX,
									dimensionY, stepsInXdirection, stepsInYdirection, groupFixed, 0);
							groupFixed++;

							extraHiveX += (dimensionX / (hivesPrintedInLastLine + 1));
						}

						actualExtraHiveToPrint++;
					}
				}
			}
			lineCount = 0;
		}
	}

	private void createFlowers() {
		for (int i = 0; i < _inputData.getNumberOfFlowersPerBee() * _hiveGroups * _hivesPerGroup
				* _inputData.getNumberOfBeesPerHive(); i++) {
			registerFlower("Flower" + i);
		}
		register(new Meadow(this), "Meadow");
	}

	@Override
	public void simulationComplete() {
		info("Simulation complete.");
		for (EventListener e : _listeners) {
			e.close();
		}
	}

	// do not call this method getEnvironment as tortuga will crash
	public Environment environment() {
		return _environment;
	}

	// do not call this method getInputData as tortuga will crash
	public static InputData inputData() {
		return _inputData;
	}

	public void stopSimulation() {
		simulationComplete();
		System.exit(0);
	}

	public void notifyListeners(PositionedEntity entity) {
		for (EventListener e : _listeners) {
			e.notify(entity);
		}
	}

	public void addEventListener(EventListener e) {
		_listeners.add(e);
	}

	private void registerHive(Position pos, int capacity, String name) {
		BeeHive newHive = _hiveFactory.createHiveAtPosition(pos, capacity);
		register(newHive, name);
		_environment.addHive(newHive);
	}

	private void registerFlower(String name) {
		Flower currentFlower = _flowerFactory.createFlower();
		_environment.addFlower(currentFlower);
	}

	public static void main(String[] args) throws Exception {

		BeeCommandLineParser arguments = BeeCommandLineParser.parse(args);

		// average available surface: 29ha
		// 1ha = 10000 square meters -> 29ha = 290000 square meters ->
		// sqrt(290000) approximately 540m -> Env(-270, 270, -270, 270)
		BeeSimulation simulation = new BeeSimulation(arguments.getNumberOfGroups(), arguments.getGroupSize(),
				(arguments.showGui()) ? new Environment(-30, 30, -30, 30) : new Environment(-270, 270, -270, 270));

		if (arguments.showGui()) {
			simulation.addEventListener(new VisualisationEventListener(simulation));
		}

		if (arguments.generateReport()) {
			try {
				simulation.addEventListener(new ReportEventListener("report.csv", simulation));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		if (arguments.showControls()) {
			simulation.setVisible(true);
		} else {
			simulation.run();
		}
	}

	// do not call this method getBeeFactory as tortuga will crash
	public BeeFactory beeFactory() {
		return _beeFactory;
	}

}
