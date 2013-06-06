package de.unihalle.sim.main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import de.unihalle.sim.entities.PositionedEntity;

public class ReportEventListener implements EventListener {

	double _lastNotificationTime = -1;
	PrintWriter _out;

	private static class SimulationState {

		private double _time;
		private int _numberOfHives;
		private int _numberOfFlowers;
		private int _numberOfBees;
		private int _numberOfInfectedBees;
		private double _beeInfectionRatio;

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

			public Builder numberOfInfectedBees(int numberOfInfectedBees) {
				_state.setNumberOfInfectedBees(numberOfInfectedBees);
				return this;
			}

			public Builder beeInfectionRatio(double infectionRatio) {
				_state.setBeeInfectionRatio(infectionRatio);
				return this;
			}

			public SimulationState build() {
				return _state;
			}

		}

		private void setNumberOfHives(int numberOfHives) {
			_numberOfHives = numberOfHives;
		}

		private void setNumberOfFlowers(int numberOfFlowers) {
			_numberOfFlowers = numberOfFlowers;
		}

		private void setNumberOfBees(int numberOfBees) {
			_numberOfBees = numberOfBees;
		}

		private void setNumberOfInfectedBees(int numberOfInfectedBees) {
			_numberOfInfectedBees = numberOfInfectedBees;
		}

		private void setBeeInfectionRatio(double infectionRatio) {
			_beeInfectionRatio = infectionRatio;
		}

		@Override
		public String toString() {
			return _time + ";" + _numberOfHives + ";" + _numberOfFlowers + ";" + _numberOfBees + ";"
					+ _numberOfInfectedBees + ";" + _beeInfectionRatio;
		}

		public static String getSchema() {
			return "time;numberOfHives;numberOfFlowers;numberOfBees;numberOfInfectedBees;beeInfectionRatio";
		}

	}

	private List<SimulationState> _states;

	public ReportEventListener(PrintWriter out) {
		_out = out;
		_states = Lists.newArrayList();
		_states = Collections.synchronizedList(_states);
	}

	public ReportEventListener(String fileName) throws FileNotFoundException {
		this(new PrintWriter(fileName));
	}

	@Override
	public void notify(PositionedEntity e) {
		double currentTime = (Math.round(e.getTimeNow() * 1000)) / 1000d;
		int numBees = BeeSimulation.getEnvironment().getNumberOfBees();
		int numHives = BeeSimulation.getEnvironment().getNumberOfHives();
		int numFlowers = BeeSimulation.getEnvironment().getNumberOfFlowers();
		int numInfected = BeeSimulation.getEnvironment().getNumberOfInfectedBees();
		double infectionRatio = (double) numInfected / (double) numBees;
		if (_lastNotificationTime == currentTime) {
			_states.remove(_states.size() - 1);
		}
		_states.add(SimulationState.Builder.time(currentTime).numberOfBees(numBees).numberOfFlowers(numFlowers)
				.numberOfHives(numHives).numberOfInfectedBees(numInfected).beeInfectionRatio(infectionRatio).build());
		_lastNotificationTime = currentTime;
	}

	public void close() {
		_out.write(SimulationState.getSchema() + "\n");
		for (SimulationState s : _states) {
			_out.write(s.toString() + "\n");
		}
		_out.close();
	}
}
