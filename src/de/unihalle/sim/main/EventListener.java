package de.unihalle.sim.main;

import de.unihalle.sim.entities.PositionedEntity;

public interface EventListener {

	public void notify(PositionedEntity e);

	public void close();

}
