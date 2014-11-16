package app.src.main.java.edu.nyit.csci455.geocircuit.Interface;

import android.location.Location;

/**
 * Created by Matt on 10/25/2014.
 */
public interface GPScallback

{
    public abstract void onGPSUpdate(Location location);


}
