package de.unihalle.sim.main;

import de.unihalle.sim.entities.PositionedEntity;

public class VisualisationEventListener implements EventListener {

	BeeSimulation _simulation;
	VisualisationCanvas _canvas;
	Environment _tempVisualisationEnvironment;
	double _oldTimestamp = 0;

	public VisualisationEventListener(BeeSimulation simulation) {
		_simulation = simulation;
		_canvas = new VisualisationCanvas(_simulation.environment());
		_tempVisualisationEnvironment = _simulation.environment().clone();
	}

	@Override
	public void notify(PositionedEntity e) {

		if (e.getTimeNow() > _oldTimestamp) {
			_canvas.drawit(_tempVisualisationEnvironment);
		}
		_tempVisualisationEnvironment = _simulation.environment().clone();
		_oldTimestamp = e.getTimeNow();
	}

	@Override
	public void close() {
		_canvas.drawit(_tempVisualisationEnvironment);
	}

}
