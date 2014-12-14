package edu.nyit.csci455.geocircuit.normalized;

import android.graphics.Bitmap;

public class Place implements Comparable {

    private Bitmap icon;

    private String name;

    private String distance;

    private float latitude;

    private float longitude;

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    /**
     * Returns the a formatted String of the distance between
     * the specified latitude and longitude and this place.
     *
     * @param latitude Specified latitude;
     * @param longitude Specified longitude;
     * @return Formatted String representation of distance.
     */
    public String getPlaceDistance(double latitude, double longitude) {

        int earthRadius = 6371;
        double kmToMi = 0.621371;

        // Difference in latitude and longitude in radians.
        double dLat = Math.toRadians(this.latitude - latitude);
        double dLng = Math.toRadians(this.longitude - longitude);

        // Convert latitudes to radians.
        double startlatRads = Math.toRadians(latitude);
        double endLatRads = Math.toRadians(this.latitude);

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

    @Override
    public int compareTo(Object another) {
        return this.getDistance().compareTo(((Place)another).getDistance());
    }
}
