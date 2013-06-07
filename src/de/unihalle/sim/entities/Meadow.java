package de.unihalle.sim.entities;

import org.mitre.sim.DefaultEntity;

import de.unihalle.sim.main.BeeSimulation;

public class Meadow extends DefaultEntity {

	private static final double NECTAR_REFRESH_RATE = BeeSimulation.getInputData().getNectarRefreshRate();

	@Event
	public void refreshNectar() {
		BeeSimulation.getEnvironment().refreshFlowers();
		schedule("refreshNectar", NECTAR_REFRESH_RATE);
	}

	@Override
	public void initialize() {
		schedule("refreshNectar", NECTAR_REFRESH_RATE);
	}

}
