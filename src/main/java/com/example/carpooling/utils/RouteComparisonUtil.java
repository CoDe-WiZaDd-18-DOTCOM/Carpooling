package com.example.carpooling.utils;

import com.example.carpooling.dto.RouteMatchResult;
import com.example.carpooling.entities.Location;
import com.example.carpooling.entities.Preferences;
import com.example.carpooling.entities.RouteStop;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;

@Component
public class RouteComparisonUtil {

    static final int PREFERENCE_MISMATCH_PENALTY = -3;

    public RouteMatchResult compareRoute(List<RouteStop> driverRoute,
                                         List<Location> preferredRoute,
                                         Location pickupPoint,
                                         Location dropPoint,
                                         Preferences current,
                                         Preferences preferred) {

        double score = 100.0;

        boolean pickupFound = false;
        boolean dropFound = false;

        LocalTime currentTime = LocalTime.now();

        HashSet<String> preferredSet = new HashSet<>();
        preferredSet.add(pickupPoint.getLabel());
        preferredSet.add(dropPoint.getLabel());


        LocalTime localTime = LocalTime.MIN;


        if (preferredRoute==null || preferredRoute.isEmpty()) {
            System.out.println("entered");
            for (RouteStop stop : driverRoute) {
                if(!pickupFound) pickupFound = GeoUtils.nearBy(pickupPoint.getLat(), pickupPoint.getLon(),
                        stop.getLocation().getLat(),stop.getLocation().getLon());

                dropFound = GeoUtils.nearBy(dropPoint.getLat(), dropPoint.getLon(),
                        stop.getLocation().getLat(),stop.getLocation().getLon());

                if (pickupFound) {
                    if(localTime.equals(LocalTime.MIN)) localTime=stop.getArrivalTime();
                }

                if(pickupFound && dropFound) break;
            }

            if (!pickupFound || localTime.isBefore(currentTime) || !dropFound) return new RouteMatchResult(0, "Pickup/drop point not found in driver's route",localTime);
            return new RouteMatchResult(100, "Ride found",localTime);

        }
        else {
            double totalCount = 0, matchedCount = 0;
            System.out.println("entered_down");
            preferredRoute.forEach(loc -> preferredSet.add(loc.getLabel()));

            for (RouteStop stop : driverRoute) {
                String area = stop.getLocation().getLabel();

                if(!pickupFound) pickupFound = GeoUtils.nearBy(pickupPoint.getLat(), pickupPoint.getLon(),
                        stop.getLocation().getLat(),stop.getLocation().getLon());

                dropFound = GeoUtils.nearBy(dropPoint.getLat(), dropPoint.getLon(),
                        stop.getLocation().getLat(),stop.getLocation().getLon());


                if (pickupFound) {
                    totalCount++;
                    if(localTime.equals(LocalTime.MIN)) localTime=stop.getArrivalTime();

                    if (preferredSet.contains(area)) matchedCount++;
                }

                if (pickupFound && dropFound) break;
            }

            if (!pickupFound || localTime.isBefore(currentTime) || !dropFound) return new RouteMatchResult(0, "Pickup/drop point not found in driver's route",localTime);

            score *= (matchedCount / totalCount);
            score*=1.2;
        }

        score += calculatePreferencePenalty(current, preferred);
        if(score>100) score=100;

        if (score <= 0) return new RouteMatchResult(0, "Route and preferences mismatch",localTime);
        return new RouteMatchResult(score, "Route match successful",localTime);
    }

    private int calculatePreferencePenalty(Preferences current, Preferences preferred) {
        int penalty = 0;

        if (!matchPreference(current.getSmoking(), preferred.getSmoking()))
            penalty += PREFERENCE_MISMATCH_PENALTY;

        if (!matchPreference(current.getAc(), preferred.getAc()))
            penalty += PREFERENCE_MISMATCH_PENALTY;

        if (!matchPreference(current.getMusic(), preferred.getMusic()))
            penalty += PREFERENCE_MISMATCH_PENALTY;

        if (!matchPreference(current.getPetFriendly(), preferred.getPetFriendly()))
            penalty += PREFERENCE_MISMATCH_PENALTY;

        if (!matchPreference(current.getGenderBased(), preferred.getGenderBased()))
            penalty += PREFERENCE_MISMATCH_PENALTY;

        return penalty;
    }

    private boolean matchPreference(Enum<?> current, Enum<?> preferred) {
        if (preferred == null || current == null) return true;
        if (preferred.name().equals("NONE")) return true;
        return preferred.name().equals(current.name());
    }
}
