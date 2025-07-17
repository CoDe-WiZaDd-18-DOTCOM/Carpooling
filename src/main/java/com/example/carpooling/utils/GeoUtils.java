package com.example.carpooling.utils;

import com.example.carpooling.entities.RouteStop;

public class GeoUtils {

    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final double radiusKm = 2.0;

    public static boolean nearBy(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));

        double distance = EARTH_RADIUS_KM * c;

        return distance <= radiusKm;
    }
}

