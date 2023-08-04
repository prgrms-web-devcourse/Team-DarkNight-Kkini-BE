package com.prgrms.mukvengers.global.utils;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryUtils {

	private static final GeometryFactory INSTANCE
		= new GeometryFactory(new PrecisionModel(), 5179);

	private GeometryUtils() {
		/* no-op */
	}

	public static GeometryFactory getInstance() {
		return INSTANCE;
	}

	// Meter -> radius
	public static double calculateApproximateRadius(int distanceInMeters) {
		// The approximate radius of the earth in meters
		double earthRadius = 6371000;

		// convert distance to radius in radians
		double radiusInRadians = distanceInMeters / earthRadius;

		// convert radians to degrees
		return Math.toDegrees(radiusInRadians);
	}

}
