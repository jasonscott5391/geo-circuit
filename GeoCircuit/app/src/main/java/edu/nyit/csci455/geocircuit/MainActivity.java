package edu.nyit.csci455.geocircuit;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.FragmentActivity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.nyit.csci455.geocircuit.Interface.Constants;
import edu.nyit.csci455.geocircuit.normalized.Circuit;
import edu.nyit.csci455.geocircuit.normalized.GeoLocation;
import edu.nyit.csci455.geocircuit.util.DrawerItemListAdapter;
import edu.nyit.csci455.geocircuit.util.GeoCircuitDbHelper;

/**
 *
 */
public class MainActivity extends FragmentActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener,
        SensorEventListener {

    private GeoMapFragment mMapFragment;

    private DashboardFragment mDashboardFragment;

    private CircuitFragment mCircuitFragment;

    private DrawerLayout mDrawerLayout;

    private ListView mDrawerList;

    private DrawerItemListAdapter mDrawerListAdapter;

    private ActionBarDrawerToggle mDrawerToggle;

    private SettingsFragment mSettings;

    private CharSequence mTitle;

    private DrawerItemClickListener mDrawerItemClickListener;

    private SharedPreferences mSharedPreferences;

    private GeoCircuitDbHelper mGeoDbHelper;

    private LocationClient mLocationClient;

    private LocationRequest mLocationRequest;

    private SensorManager mSensorManager;

    private Sensor mMagnetometer;

    private Sensor mAccelerometer;

    private int mCurrentFeature;

    private float mAzimuth;

    private float[] mGravity;

    private float[] mGeoMagnetic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        initializeDrawerMenu();

        mTitle = getTitle();

        mMapFragment = (GeoMapFragment) getFragmentManager().findFragmentById(R.id.map);

        mSettings = new SettingsFragment();

        if (mMapFragment == null) {
            mMapFragment = new GeoMapFragment();
        }

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_layout, mMapFragment)
                .commit();

        mLocationClient = new LocationClient(this, this, this);

        mLocationRequest = new LocationRequest();

        mLocationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationRequest.setInterval(Constants.LOC_UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(Constants.FASTEST_LOC_UPDATE);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSharedPreferences = getSharedPreferences("user_preferences", 0);
        mCurrentFeature = mSharedPreferences.getInt(
                "current_feature",
                Constants.DASHBOARD);

        mGeoDbHelper = GeoCircuitDbHelper.getInstance(this);
        populateDatabaseTest(mGeoDbHelper);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient.connect();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationClient.removeLocationUpdates(this);
        mLocationClient.disconnect();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onStop() {
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.action_settings) {
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, mSettings)
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    private void initializeDrawerMenu() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerList = (ListView) findViewById(R.id.drawer_list);

        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(Constants.FEATURES[mCurrentFeature]);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerListAdapter = new DrawerItemListAdapter(this);

        mDrawerList.setAdapter(mDrawerListAdapter);

        mDrawerItemClickListener = new DrawerItemClickListener();

        mDrawerList.setOnItemClickListener(mDrawerItemClickListener);
    }

    /**
     * @param position
     */
    private void selectItem(int position) {
        //TODO (jasonscott) Switch statement to handle different cases of onClick.
        mCurrentFeature = position;
        mDrawerLayout.closeDrawer(mDrawerList);

        switch (position) {
            case Constants.DASHBOARD:
                if (mDashboardFragment == null) {
                    mDashboardFragment = new DashboardFragment();
                }
                mMapFragment.dashboardMode(mLocationClient.getLastLocation(), mAzimuth);
                mLocationClient.requestLocationUpdates(mLocationRequest, this);
                break;
            case Constants.CIRCUIT_MANAGER:
                if (mCircuitFragment == null) {
                    mCircuitFragment = new CircuitFragment();
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_layout, mCircuitFragment)
                        .commit();
                mMapFragment.circuitManagerMode();
                break;
            case Constants.NEAR_ME:
                mMapFragment.nearMeMode();
                break;

            default:
                break;
        }
    }

    private void populateDatabaseTest(GeoCircuitDbHelper dbHelper) {
        ArrayList testLocations = readInTestLocations();
        for (Object location : testLocations) {
            dbHelper.insertLocation((GeoLocation) location);
        }

        ArrayList testCircuits = readInTestCircuits(dbHelper);
        String[] testCircuitNames = {"Work", "School", "Home", "Hell", "The End"};
        int i = 0;
        for (Object circuit : testCircuits) {
            dbHelper.insertCircuit((Circuit) circuit, testCircuitNames[i++]);
        }
    }

    private ArrayList readInTestLocations() {
        BufferedReader bufferedReader =
                new BufferedReader(
                        new InputStreamReader(
                                this.getResources()
                                        .openRawResource(R.raw.test_locations)));

        ArrayList geoLocationList = new ArrayList();

        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");

                GeoLocation geoLocation = new GeoLocation();
                geoLocation.setLocationId(Integer.parseInt(parts[0]));
                geoLocation.setLatitude(Float.parseFloat(parts[1]));
                geoLocation.setLongitude(Float.parseFloat(parts[2]));
                geoLocation.setDate(Long.parseLong(parts[3]) * 1000);

                geoLocationList.add(geoLocation);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return geoLocationList;
    }

    private ArrayList readInTestCircuits(GeoCircuitDbHelper dbHelper) {


        BufferedReader bufferedReader =
                new BufferedReader(
                        new InputStreamReader(
                                this.getResources()
                                        .openRawResource(R.raw.test_circuits)));

        ArrayList circuitList = new ArrayList();

        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");

                Circuit circuit = new Circuit();
                circuit.setCircuitId(Integer.parseInt(parts[0]));
                circuit.setStartLocation(dbHelper.retrieveLocationById(Integer.parseInt(parts[1])));
                circuit.setEndLocation(dbHelper.retrieveLocationById(Integer.parseInt(parts[2])));

                circuitList.add(circuit);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return circuitList;
    }

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt("error_dialog", errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(), "errordialog");
    }

    /*
    * Handle results returned to the FragmentActivity
    * by Google Play services
    */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
            case Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST:
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                    /*
                     * Try the request again
                     */
                        servicesConnected();
                        break;
                }
        }
    }

    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason.
            // resultCode holds the error code.
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    resultCode,
                    this,
                    Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getFragmentManager(),
                        "Location Updates");
            }
        }
        return false;
    }

    @Override
    public void onConnected(Bundle bundle) {
//        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        servicesConnected();
        selectItem(mCurrentFeature);
    }

    @Override
    public void onDisconnected() {
        //TODO (jasonscott) Test case for this.
//        Toast.makeText(this, "Disconnected. Please re-connect.",
//                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
  /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            showErrorDialog(connectionResult.getErrorCode());
        }

    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        if (mCurrentFeature == Constants.DASHBOARD) {
            mMapFragment.dashboardMode(location, mAzimuth);

            // TODO (jasonscott) Store location into database if trip is being recorded and interval has passed.
            // if (mDashboardFragment.isRecordingCircuit() {
//            GeoLocation geoLocation = new GeoLocation();
//            geoLocation.setLatitude((float) location.getLatitude());
//            geoLocation.setLongitude((float) location.getLongitude());
//                mGeoDbHelper.insertLocation();
            // }
        }

    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values.clone();
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mGeoMagnetic = event.values.clone();
        }

        float R[] = new float[9];
        float I[] = new float[9];
        boolean success = false;

        if (mGravity != null && mGeoMagnetic != null) {
            success = SensorManager.getRotationMatrix(R, I, mGravity, mGeoMagnetic);
        }

        if (success) {
            float orientation[] = new float[3];
            SensorManager.getOrientation(R, orientation);
            mAzimuth = orientation[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }


}
