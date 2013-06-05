package de.unihalle.sim.main;

import de.unihalle.sim.entities.PositionedEntity;

public class DummyEventListener implements EventListener {

	VisualisationCanvas bla = new VisualisationCanvas();

	@Override
	public void notify(PositionedEntity e) {

		bla.drawit();
		// BeeSimulation.getEnvironment().getBeesAt(Position.createFromCoordinates(0,
		// 0)).
	}

}
