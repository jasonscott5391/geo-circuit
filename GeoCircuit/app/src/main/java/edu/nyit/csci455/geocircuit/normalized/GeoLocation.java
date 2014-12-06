package edu.nyit.csci455.geocircuit.normalized;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * <p>Location.java</p>
 * </p>
 * <p>Represents a recorded point in time and geographical space.</p>
 *
 * @author jasonscott
 */
public class GeoLocation implements Serializable {

    private int locationId;

    private int circuitId;

    private long date;

    private float azimuth;

    private float speed;

    private float latitude;

    private float longitude;

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getCircuitId() {
        return circuitId;
    }

    public void setCircuitId(int circuitId) {
        this.circuitId = circuitId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public float getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(float azimuth) {
        this.azimuth = azimuth;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
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
     * Returns the time duration between this and the start
     * GeoLocation.
     *
     * @param start Specified start GeoLocation.
     * @return Time from start to GeoLocation to this GeoLocation.
     */
    public String calculateTime(GeoLocation start) {

        long difference = this.getDate() - start.getDate();

        String time = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(difference),
                TimeUnit.MILLISECONDS.toMinutes(difference) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(difference)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(difference) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(difference)));

        return time;
    }

}
