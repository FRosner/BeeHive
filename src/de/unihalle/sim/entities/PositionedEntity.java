package de.unihalle.sim.entities;

import org.mitre.sim.DefaultEntity;

import de.unihalle.sim.main.BeeSimulation;
import de.unihalle.sim.util.Position;

public abstract class PositionedEntity extends DefaultEntity {

	protected Position _position;
	protected Position _destination;

	/**
	 * Returns a defensive copy of the entity position.
	 * 
	 * @return entity position
	 */
	public Position getPosition() {
		return Position.createFromPosition(_position);
	}

	/**
	 * Returns a defensive copy of the entity destination. Returns <tt>null</tt> if the entity has no destination.
	 * 
	 * @return entity destination
	 */
	public Position getDestination() {
		if (_destination == null) {
			return null;
		} else {
			return Position.createFromPosition(_destination);
		}
	}

	@Event
	public void arriveAt(Position pos) {
		_position.x = pos.x;
		_position.y = pos.y;
		_destination = null;
	}

	/**
	 * Schedule movement to the specified position in the specified time. This method has to be called <b>before</b> the
	 * entity notifies (infoWithPosition) the simulation of its movement. It will also schedule the arrival to change
	 * the coordinates and reset the destination to <tt>null</tt>. Scheduling this movement will have no impact on the
	 * time to live or any other property of the entity.
	 * 
	 * @param position
	 *            to move to
	 * @param movementTime
	 *            that is consumed for moving
	 */
	protected void moveTo(Position position, double movementTime) {
		_destination = position;
		schedule("arriveAt", movementTime, position);
	}

	/**
	 * Returns whether the entity is currently moving somewhere. This is done by checking if a destination is set.
	 * 
	 * @return whether the entity is currently moving somewhere
	 */
	public boolean isMoving() {
		return _destination != null;
	}

	/**
	 * The entity notifies the simulation with the specified message and its position.
	 * 
	 * @param message
	 */
	public void infoWithPosition(String message) {
		BeeSimulation.notifyListeners(this);
		info(getPosition() + " " + getDestination() + " " + message);
	}

	@Override
	public boolean equals(Object o) {
		if (PositionedEntity.class.isAssignableFrom(o.getClass())) {
			PositionedEntity bee = (PositionedEntity) o;
			return bee.getName().equals(getName()) && bee.getPosition().equals(getPosition());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (getName() + getPosition()).hashCode();
	}

}
