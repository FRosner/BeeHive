package de.unihalle.sim.entities;

import de.unihalle.sim.main.BeeSimulation;

public class Meadow extends DefaultEntity {

	public Meadow(BeeSimulation simulation) {
		_simulation = simulation;
	}

	private static final double NECTAR_REFRESH_RATE = BeeSimulation.inputData().getNectarRefreshRate();

	@Event
	public void refreshNectar() {
		_simulation.environment().refreshFlowers();
		schedule("refreshNectar", NECTAR_REFRESH_RATE);
	}

	@Override
	public void initialize() {
		schedule("refreshNectar", NECTAR_REFRESH_RATE);
	}

}
