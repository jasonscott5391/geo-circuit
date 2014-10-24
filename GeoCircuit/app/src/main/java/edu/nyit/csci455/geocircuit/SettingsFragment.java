package edu.nyit.csci455.geocircuit;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by gurpreet on 10/23/2014.
 */
public class SettingsFragment extends PreferenceFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
