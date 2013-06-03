package de.unihalle.sim.main;

import de.unihalle.sim.entities.PositionedEntity;
import de.unihalle.sim.util.Position;

public interface EventListener {

	public void notifyPosition(PositionedEntity e);

	public void notifyMovement(PositionedEntity e, Position nextPosition);

}
