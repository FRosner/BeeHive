package de.unihalle.sim.main;

import de.unihalle.sim.entities.PositionedEntity;

public class VisualisationEventListener implements EventListener {

	VisualisationCanvas canvasObject = new VisualisationCanvas();
	Environment tempVisualisationEnvironment = BeeSimulation.getEnvironment().clone();
	double dOldTimestamp = 0;

	@Override
	public void notify(PositionedEntity e) {

		if (e.getTimeNow() > dOldTimestamp)
			canvasObject.drawit(tempVisualisationEnvironment);
		tempVisualisationEnvironment = BeeSimulation.getEnvironment().clone();
		dOldTimestamp = e.getTimeNow();
	}

	@Override
	public void close() {
		canvasObject.drawit(tempVisualisationEnvironment);
	}

}
