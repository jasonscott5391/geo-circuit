package edu.nyit.csci455.geocircuit.normalized;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * <p>Circuit.java</p>
 * <p/>
 * <p>Represents a group of locations recorded for a period in time.</p>
 *
 * @author jasonscott
 */
public class Circuit implements Serializable {

    private int circuitId;

    private String circuitName;

    private ArrayList geoLocations;

    public int getCircuitId() {
        return circuitId;
    }

    public void setCircuitId(int circuitId) {
        this.circuitId = circuitId;
    }

    public String getCircuitName() {
        return circuitName;
    }

    public void setCircuitName(String circuitName) {
        this.circuitName = circuitName;
    }

    public ArrayList getGeoLocations() {
        return geoLocations;
    }

    public void setGeoLocations(ArrayList geoLocations) {
        this.geoLocations = geoLocations;
    }

    /**
     * Returns the distance traveled of a circuit between the
     * specified starting and ending locations.
     *
     * @return String distance traveled.
     */
    public String calculateCircuitDistance() {
        GeoLocation start = (GeoLocation) geoLocations.get(0);
        GeoLocation end = (GeoLocation) geoLocations.get(geoLocations.size() - 1);

        int earthRadius = 6371;
        double kmToMi = 0.621371;

        // Difference in latitude and longitude in radians.
        double dLat = Math.toRadians(end.getLatitude() - start.getLatitude());
        double dLng = Math.toRadians(end.getLongitude() - start.getLongitude());

        // Convert latitudes to radians.
        double startlatRads = Math.toRadians(start.getLatitude());
        double endLatRads = Math.toRadians(end.getLatitude());

        // Calculate the angle
        double angle = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLng / 2) * Math.sin(dLng / 2) * Math.cos(startlatRads) * Math.cos(endLatRads);

        // Calculate the angular distance
        double angularDistance = 2 * Math.atan2(Math.sqrt(angle), Math.sqrt(1 - angle));

        // Convert to kilometers
        double distanceKm = earthRadius * angularDistance;

        // Convert to Miles
        double distanceMi = distanceKm * kmToMi;

        return String.format("%.2f", distanceMi);
    }

    /**
     * Returns the time duration of circuit between the
     * specified starting and ending locations.
     *
     * @return String duration time traveled.
     */
    public String calculateCircuitDuration() {
        GeoLocation start = (GeoLocation) geoLocations.get(0);
        GeoLocation end = (GeoLocation) geoLocations.get(geoLocations.size() - 1);

        long difference = end.getDate() - start.getDate();

        String time = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(difference),
                TimeUnit.MILLISECONDS.toMinutes(difference) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(difference)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(difference) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(difference)));

        return time;
    }
}
