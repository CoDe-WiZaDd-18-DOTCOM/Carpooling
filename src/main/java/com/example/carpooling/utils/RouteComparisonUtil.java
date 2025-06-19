package com.example.carpooling.utils;

import com.example.carpooling.dto.RouteMatchResult;
import com.example.carpooling.entities.Location;
import com.example.carpooling.entities.Preferences;
import com.example.carpooling.entities.RouteStop;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
public class RouteComparisonUtil {

    static final int DESTINATION_NOT_FOUND_PENALTY = -20;
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

        HashSet<String> preferredSet = new HashSet<>();
        preferredSet.add(pickupPoint.getArea());
        preferredSet.add(dropPoint.getArea());

        preferredRoute.forEach(loc -> preferredSet.add(loc.getArea()));

        if (preferredRoute.isEmpty()) {
            for (RouteStop stop : driverRoute) {
                String area = stop.getLocation().getArea();
                if (area.equals(pickupPoint.getArea())) pickupFound = true;
                else if (area.equals(dropPoint.getArea()) && pickupFound) dropFound = true;

                if (pickupFound && dropFound) break;
            }

            if (!pickupFound) return new RouteMatchResult(0, "Pickup point not found in driver's route");
            if (!dropFound) return new RouteMatchResult(50, "Drop point not found in driver's route");

        } else {
            double totalCount = 0, matchedCount = 0;

            for (RouteStop stop : driverRoute) {
                String area = stop.getLocation().getArea();
                if (area.equals(pickupPoint.getArea())) pickupFound = true;
                else if (area.equals(dropPoint.getArea()) && pickupFound) dropFound = true;

                if (pickupFound) {
                    totalCount++;
                    if (preferredSet.contains(area)) matchedCount++;
                }

                if (pickupFound && dropFound) break;
            }

            if (!pickupFound) return new RouteMatchResult(0, "Pickup point not found in driver's route");
            if (!dropFound) score += DESTINATION_NOT_FOUND_PENALTY;

            score *= (matchedCount / totalCount);
        }

        score += calculatePreferencePenalty(current, preferred);

        if (score <= 0) return new RouteMatchResult(0, "Route and preferences mismatch");

        return new RouteMatchResult(score, "Route match successful");
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
