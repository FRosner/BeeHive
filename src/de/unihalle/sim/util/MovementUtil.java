package de.unihalle.sim.util;

public class MovementUtil {

	public static double metersPerSecond(int mps) {
		return mps;
	}

	public static double kilometersPerHour(int kmps) {
		return kmps / 3.6d;
	}

	public static double calculateMovementTime(double distance, double movementSpeed) {
		return distance / movementSpeed;
	}

}
