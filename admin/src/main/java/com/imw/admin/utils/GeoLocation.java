package com.imw.admin.utils;

import java.lang.Math;

public class GeoLocation {

    private static final int EARTH_RADIUS_METERS = 6371000; // Earth radius in meters

    // Method to calculate distance between two points using Haversine formula
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_METERS * c;
    }

    // Method to check if a location falls inside a circle
    public static boolean isLocationInsideCircle(double centerLat, double centerLon,
                                                 double targetLat, double targetLon,
                                                 double radiusMeters) {
        double distance = calculateDistance(centerLat, centerLon, targetLat, targetLon);
        return distance <= radiusMeters;
    }

}
