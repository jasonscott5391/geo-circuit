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

    private Location startLocation;

    private Location endLocation;

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

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }
}
