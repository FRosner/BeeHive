package de.unihalle.sim.main;

import java.io.PrintStream;

import de.unihalle.sim.entities.PositionedEntity;

public class PrintStreamEventListener implements EventListener {

	PrintStream _out;

	public PrintStreamEventListener(PrintStream out) {
		_out = out;
	}

	@Override
	public void notify(PositionedEntity e) {
		_out.println(e.getTimeNow() + " " + e.getName() + " " + e.getPosition());
	}

	public void close() {
		_out.close();
	}

}
