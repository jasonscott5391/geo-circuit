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

    private Date date;

    private float latitude;

    private float longitude;

    public int getlocationId() {
        return locationId;
    }

    public void setlocationId(int locationId) {
        this.locationId = locationId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
