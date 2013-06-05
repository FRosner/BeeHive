package de.unihalle.sim.main;

import java.util.List;

import com.google.common.collect.Lists;

import de.unihalle.sim.entities.PositionedEntity;

public class ReportEventListener implements EventListener {

	private static class SimulationState {

		private double _time;
		private int _numberOfHives;
		private int _numberOfFlowers;
		private int _numberOfBees;

		private SimulationState(double time) {
			_time = time;
		}

		private static class Builder {

			private SimulationState _state;

			private Builder(double time) {
				_state = new SimulationState(time);
			}

			public static Builder time(double time) {
				return new Builder(time);
			}

			public Builder numberOfHives(int numberOfHives) {
				_state.setNumberOfHives(numberOfHives);
				return this;
			}

			public Builder numberOfBees(int numberOfBees) {
				_state.setNumberOfBees(numberOfBees);
				return this;
			}

			public Builder numberOfFlowers(int numberOfFlowers) {
				_state.setNumberOfFlowers(numberOfFlowers);
				return this;
			}

			public SimulationState build() {
				return _state;
			}

		}

		public double getTime() {
			return _time;
		}

		public double getNumberOfHives() {
			return _numberOfHives;
		}

		public double getNumberOfFlowers() {
			return _numberOfFlowers;
		}

		public double getNumberOfBees() {
			return _numberOfBees;
		}

		public void setNumberOfHives(int numberOfHives) {
			_numberOfHives = numberOfHives;
		}

		public void setNumberOfFlowers(int numberOfFlowers) {
			_numberOfFlowers = numberOfFlowers;
		}

		public void setNumberOfBees(int numberOfBees) {
			_numberOfBees = numberOfBees;
		}

		@Override
		public String toString() {
			return _time + ";" + _numberOfHives + ";" + _numberOfFlowers + ";" + _numberOfBees;
		}

		public static String getSchema() {
			return "time;numberOfHives;numberOfFlowers;numberOfBees";
		}

	}

	private List<SimulationState> _states = Lists.newArrayList();

	@Override
	public void notify(PositionedEntity e) {
		double currentTime = e.getTimeNow();
		int numberOfBees = BeeSimulation.getEnvironment().getNumberOfBees();
		int numberOfHives = BeeSimulation.getEnvironment().getNumberOfHives();
		int numberOfFlowers = BeeSimulation.getEnvironment().getNumberOfFlowers();
		_states.add(SimulationState.Builder.time(e.getTimeNow()).numberOfBees(numberOfBees).numberOfFlowers(
				numberOfFlowers).numberOfHives(numberOfHives).build());
	}

	public void close() {
		System.err.println(SimulationState.getSchema());
		for (SimulationState s : _states) {
			System.err.println(s);
		}
	}
}
