package de.unihalle.sim.main;

import de.unihalle.sim.entities.PositionedEntity;

public class VisualisationEventListener implements EventListener {

	VisualisationCanvas _canvas = new VisualisationCanvas(BeeSimulation.getEnvironment().clone());
	Environment _tempVisualisationEnvironment = BeeSimulation.getEnvironment().clone();
	double _oldTimestamp = 0;

	@Override
	public void notify(PositionedEntity e) {

		if (e.getTimeNow() > _oldTimestamp)
			_canvas.drawit(_tempVisualisationEnvironment);
		_tempVisualisationEnvironment = BeeSimulation.getEnvironment().clone();
		_oldTimestamp = e.getTimeNow();
	}

	@Override
	public void close() {
		_canvas.drawit(_tempVisualisationEnvironment);
	}

}
