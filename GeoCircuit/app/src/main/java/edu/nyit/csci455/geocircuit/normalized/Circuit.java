package edu.nyit.csci455.geocircuit.normalized;

import java.io.Serializable;
import java.util.ArrayList;

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
}
