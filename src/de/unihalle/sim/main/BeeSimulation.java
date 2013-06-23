package de.unihalle.sim.main;

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
	private boolean _showGui;
	private boolean _generateReport;

	public BeeSimulation(int n, int s, boolean g, boolean r) {
		_hiveGroupNumbers = n;
		_hiveGroupSize = s;
		_showGui = g;
		_generateReport = r;
	}

	@Override
	public void initialize() {
		setTimeLast(SIMULATION_TIME);
		setPace(SIMULATION_PACE);
		createFlowers();
		createHives();
	}

	private void createHives() {
		int anzahlGruppen = _hiveGroupNumbers;
		int gruppenGesetzt = 0;
		int gruppengroesse = _hiveGroupSize;
		double platzierungenProSplateUndZeile = Math.sqrt(anzahlGruppen);
		int platzierungsanzahl = 0;

		if ((platzierungenProSplateUndZeile % 1) == 0) {
			platzierungsanzahl = (int) platzierungenProSplateUndZeile;
		} else {
			platzierungsanzahl = (int) platzierungenProSplateUndZeile;
			platzierungsanzahl++;
		}

		int dimensionX = Math.abs(_environment.getMaxX()) + Math.abs(_environment.getMinX());
		int dimensionY = Math.abs(_environment.getMaxY()) + Math.abs(_environment.getMinY());

		int pixel_x = Math.round(dimensionX / platzierungsanzahl);
		int pixel_y = Math.round(dimensionY / platzierungsanzahl);

		for (int y = pixel_y; y <= dimensionY; y += pixel_y) {
			for (int x = pixel_x; x <= dimensionX; x += pixel_x) {

				if (gruppenGesetzt < anzahlGruppen) {

					int gruppenDimension;
					int gruppenDurchlaufen = 0;
					if ((Math.sqrt(gruppengroesse) % 1) == 0)
						gruppenDimension = (int) Math.sqrt(gruppengroesse);
					else {
						gruppenDimension = (int) Math.sqrt(gruppengroesse);
						gruppenDimension++;
					}

					for (int xg = 0; xg < gruppenDimension; xg++)
						for (int yg = 0; yg < gruppenDimension; yg++) {
							if (gruppenDurchlaufen < gruppengroesse) {
								registerHive(Position.createFromCoordinates((x - dimensionX / 2) - pixel_x / 2 + xg,
										(y - dimensionY / 2) - pixel_y / 2 + yg), _inputData.getNumberOfBeesPerHive(),
										"Hive" + gruppenGesetzt + "_" + gruppenDurchlaufen);

								gruppenDurchlaufen++;
							}
						}
				}
				gruppenGesetzt++;
			}
		}

		// registerHive(Position.createFromCoordinates(5, 5),
		// _inputData.getNumberOfBeesPerHive(), "Rome");
		// registerHive(Position.createFromCoordinates(5, 5), 4, "Rome");
		// registerHive(Position.createFromCoordinates(-5, -5), 4, "Milan");
		// registerHive(Position.createFromCoordinates(5, -5), 4, "Naples");
		// registerHive(Position.createFromCoordinates(-5, 5), 4, "Turin");
		// register(new BeeHive(Position.createFromCoordinates(0, 0), 1),
		// "Palermo");
		// register(new BeeHive(Position.createFromCoordinates(0, 0), 1),
		// "Genoa");
		// register(new BeeHive(Position.createFromCoordinates(0, 0), 1),
		// "Bologna");
		// register(new BeeHive(Position.createFromCoordinates(0, 0), 1),
		// "Florence");
		// register(new BeeHive(Position.createFromCoordinates(0, 0), 1),
		// "Bari");
		// register(new BeeHive(Position.createFromCoordinates(0, 0), 1),
		// "Catania");
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
		BeeSimulation.addEventListener(new ReportEventListener("report.csv"));
		BeeSimulation.addEventListener(new VisualisationEventListener());

		Options options = new Options();
		options.addOption("h", "help", false, "prints information about passing arguments");
		options.addOption("n", "number", true, "number of groups of hives");
		options.addOption("s", "size", true, "size of each group, number of hives in group");
		options.addOption("c", "configuration", true, "determines which alignment of groups should be used");
		options.addOption("g", "gui", true, "says if the gui will be displayed during the simulation [yes|no]");
		options.addOption("r", "report", true, "says if a report will be generated after a simuluation [yes|no]");

		CommandLineParser commandLineParser = new BasicParser();
		CommandLine _commandLine = commandLineParser.parse(options, args);

		if (_commandLine.hasOption("h")) {
			HelpFormatter helpFormatter = new HelpFormatter();
			helpFormatter.printHelp("BeeSimulation <command> [<arg>]", options);
		}
		if (_commandLine.hasOption("n")) {
			System.out.println(_commandLine.getOptionValue("n"));
		}

		if (_commandLine.hasOption("s")) {
			System.out.println(_commandLine.getOptionValue("s"));
		}
		if (_commandLine.hasOption("c")) {
			System.out.println(_commandLine.getOptionValue("c"));
		}
		if (_commandLine.hasOption("g")) {
			System.out.println(_commandLine.getOptionValue("g"));
		}
		if (_commandLine.hasOption("r")) {
			System.out.println(_commandLine.getOptionValue("r"));
		}

		boolean showGui;
		boolean generateReport;
		if (_commandLine.getOptionValue("g").equals("yes"))
			showGui = true;
		else
			showGui = false;
		if (_commandLine.getOptionValue("r").equals("yes"))
			generateReport = true;
		else
			generateReport = false;

		_simulation = new BeeSimulation(Integer.parseInt(_commandLine.getOptionValue("n")),
				Integer.parseInt(_commandLine.getOptionValue("s")), showGui, generateReport);
		_simulation.run();
	}

}
