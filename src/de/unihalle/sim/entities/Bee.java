package de.unihalle.sim.entities;

import org.mitre.sim.DefaultEntity;

public class Bee extends DefaultEntity {

	public void collectNectar() {
		info("Collecting nectar.");
		schedule("flyBack", 3.0);
	}

	public void flyBack() {
		info("Flying back.");
	}

	public void initialize() {
		info("I am alive!");
		schedule("collectNectar", 2.0);
	}

}
