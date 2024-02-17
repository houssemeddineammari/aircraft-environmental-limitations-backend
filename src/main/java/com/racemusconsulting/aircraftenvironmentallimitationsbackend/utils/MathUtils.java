package com.racemusconsulting.aircraftenvironmentallimitationsbackend.utils;

public class MathUtils {
	private MathUtils() {
	}

	public static double roundToTwoDecimalPlaces(double value) {
		return Math.round(value * 100.0) / 100.0;
	}

}
