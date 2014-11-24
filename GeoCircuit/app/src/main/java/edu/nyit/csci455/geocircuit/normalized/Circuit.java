package edu.nyit.csci455.geocircuit.normalized;

import java.io.Serializable;

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

    private GeoLocation startLocation;

    private GeoLocation endLocation;

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

    public GeoLocation getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(GeoLocation startLocation) {
        this.startLocation = startLocation;
    }

    public GeoLocation getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(GeoLocation endLocation) {
        this.endLocation = endLocation;
    }
}
