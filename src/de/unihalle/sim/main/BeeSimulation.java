package de.unihalle.sim.main;

import java.io.FileNotFoundException;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
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

	private static final int SIMULATION_PACE = 100;
	private static final double SIMULATION_TIME = TimeUtil.months(12);

	private static Environment _environment = new Environment(-500, 500, -500, 500);
	private static List<EventListener> _listeners = Lists.newArrayList();
	private static InputData _inputData = new InputData();
	private static BeeSimulation _simulation;

	private int _hiveGroupNumbers;
	private int _hiveGroupSize;

	public BeeSimulation(int n, int s, boolean g, boolean r) {
		_hiveGroupNumbers = n;
		_hiveGroupSize = s;

		if (g == true)
			BeeSimulation.addEventListener(new VisualisationEventListener());
		if (r == true)
			try {
				BeeSimulation.addEventListener(new ReportEventListener("report.csv"));
			} catch (FileNotFoundException e) {
				System.err.println("Something went wrong with the \"ReportEventListener\".");
			}
	}

	@Override
	public void initialize() {
		setTimeLast(SIMULATION_TIME);
		setPace(SIMULATION_PACE);
		createFlowers();
		createHives();
	}

	private void createHives() {
		int groupNumbers = _hiveGroupNumbers;
		int groupFixed = 0;
		int groupSize = _hiveGroupSize;
		int numbersOfGroupsSet = 0;

		numbersOfGroupsSet = (int) Math.ceil(Math.sqrt(groupNumbers));

		int dimensionX = Math.abs(_environment.getMaxX()) + Math.abs(_environment.getMinX());
		int dimensionY = Math.abs(_environment.getMaxY()) + Math.abs(_environment.getMinY());

		int pixel_x = Math.round(dimensionX / numbersOfGroupsSet);
		int pixel_y = Math.round(dimensionY / numbersOfGroupsSet);

		for (int y = pixel_y; y <= dimensionY; y += pixel_y) {
			for (int x = pixel_x; x <= dimensionX; x += pixel_x) {

				if (groupFixed < groupNumbers) {

					int groupDimension;
					int groupCount = 0;
					if ((Math.sqrt(groupSize) % 1) == 0)
						groupDimension = (int) Math.sqrt(groupSize);
					else {
						groupDimension = (int) Math.sqrt(groupSize);
						groupDimension++;
					}

					for (int xg = 0; xg < groupDimension; xg++)
						for (int yg = 0; yg < groupDimension; yg++) {
							if (groupCount < groupSize) {
								registerHive(Position.createFromCoordinates((x - dimensionX / 2) - pixel_x / 2 + xg,
										(y - dimensionY / 2) - pixel_y / 2 + yg), _inputData.getNumberOfBeesPerHive(),
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
		register(currentFlower, name);
		_environment.addFlower(currentFlower);
	}

	public static void main(String[] args) throws Exception {

		Options options = new Options();
		options.addOption("h", "help", false, "prints information about passing arguments");
		options.addOption("n", "number", true, "number of groups of hives");
		options.addOption("s", "size", true, "size of each group, number of hives in group");
		options.addOption("c", "configuration", true, "determines which alignment of groups should be used");
		options.addOption("g", "gui", true, "says if the gui will be displayed during the simulation [yes|no]");
		options.addOption("r", "report", true, "says if a report will be generated after a simuluation [yes|no]");

		CommandLineParser commandLineParser = new BasicParser();
		CommandLine commandLine = commandLineParser.parse(options, args);

		if (commandLine.hasOption("h")) {
			HelpFormatter helpFormatter = new HelpFormatter();
			helpFormatter.printHelp("BeeSimulation <command> [<arg>]", options);
		}
		if (commandLine.hasOption("n")) {
			System.out.println(commandLine.getOptionValue("n"));
		}

		if (commandLine.hasOption("s")) {
			System.out.println(commandLine.getOptionValue("s"));
		}
		if (commandLine.hasOption("c")) {
			System.out.println(commandLine.getOptionValue("c"));
		}
		if (commandLine.hasOption("g")) {
			System.out.println(commandLine.getOptionValue("g"));
		}
		if (commandLine.hasOption("r")) {
			System.out.println(commandLine.getOptionValue("r"));
		}

		boolean showGui;
		boolean generateReport;
		if (commandLine.getOptionValue("g").equals("yes"))
			showGui = true;
		else
			showGui = false;
		if (commandLine.getOptionValue("r").equals("yes"))
			generateReport = true;
		else
			generateReport = false;

		_simulation = new BeeSimulation(Integer.parseInt(commandLine.getOptionValue("n")), Integer.parseInt(commandLine
				.getOptionValue("s")), showGui, generateReport);
		_simulation.run();
	}

}
