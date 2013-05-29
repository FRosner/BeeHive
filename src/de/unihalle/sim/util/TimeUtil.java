package de.unihalle.sim.util;

public class TimeUtil {

	public static double months(double months) {
		return weeks(months * 4);
	}

	public static double weeks(double weeks) {
		return days(weeks * 7);
	}

	public static double days(double days) {
		return hours(days * 24);
	}

	public static double hours(double hours) {
		return minutes(hours * 60);
	}

	public static double minutes(double minutes) {
		return seconds(minutes * 60);
	}

	public static double seconds(double seconds) {
		return seconds;
	}

}
