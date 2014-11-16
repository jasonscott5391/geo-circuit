package edu.nyit.csci455.geocircuit.normalized;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Location.java</p>
 * </p>
 * <p>Represents a recorded point in time and geographical space.</p>
 *
 * @author jasonscott
 */
public class Location implements Serializable {

    private int locationId;

    private long date;

    private float latitude;

    private float longitude;

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
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
}
