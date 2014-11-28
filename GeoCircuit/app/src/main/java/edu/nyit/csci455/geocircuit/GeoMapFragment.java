package edu.nyit.csci455.geocircuit;

import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.nyit.csci455.geocircuit.Interface.*;

/**
 * <p>Title: GeoMapFragment.java</p>
 * <p>GeoCircuit's map for displaying the user travelling in Dashboard, viewing
 * circuits in Circuit Manager, and displaying user location and location
 * of desirable locations nearby in Near Me.</p>
 *
 * @author jasonscott
 */
public class GeoMapFragment extends MapFragment {

    private GoogleMap mGoogleMap;

    private Point mDimensions;

    private Marker mMarker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDimensions = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(mDimensions);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        int actionBarHeight = 0;
        TypedValue typedValue = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
        }

        int statusBarHeight = this.getStatusBarHeight();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ((2 * mDimensions.y) / 3) - actionBarHeight - statusBarHeight);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        view.setLayoutParams(layoutParams);

        mGoogleMap = this.getMap();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMarker != null) {
            mMarker.remove();
            mMarker = null;
        }
    }

    /**
     * Returns the height of the device status bar.
     *
     * @return Height of the device status bar in pixels.
     */
    private int getStatusBarHeight() {
        int height = 0;
        int resourceId = getResources().getIdentifier(
                Constants.STATUS_BAR_HEIGHT,
                Constants.DIMEN,
                Constants.ANDROID);

        if (resourceId > 0) {
            height = getResources().getDimensionPixelSize(resourceId);
        }

        return height;
    }

    /**
     * Enables the GeoMap to operate for Dashboard.
     *
     * @param location The current location of the user.
     * @param azimuth  The user's bearing.
     */
    public void dashboardMode(Location location, float azimuth) {
        float azimuthDegrees = (float) Math.toDegrees(azimuth);

        if (azimuthDegrees < 0.0f) {
            azimuthDegrees += 360.0f;
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mGoogleMap.setMyLocationEnabled(false);

        if (mMarker != null) {
            toggleMarker();
        }

        mMarker = mGoogleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))
                .position(latLng)
                .rotation(azimuthDegrees)
                .flat(true));

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(latLng)
                .zoom(18)
                .bearing(azimuthDegrees)
                .build();

        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * Enables the GeoMap to operate for Circuit Manager.
     */
    public void circuitManagerMode() {
        mGoogleMap.clear();
        if (mMarker != null) {
            toggleMarker();
        }
    }

    /**
     * Enables the GeoMap to operate for Near Me.
     */
    public void nearMeMode() {
        mGoogleMap.clear();
        if (mMarker != null) {
            toggleMarker();
        }
        mGoogleMap.setMyLocationEnabled(true);
    }

    /**
     * Toggles user marker for Dashboard.
     */
    private void toggleMarker() {
        mMarker.remove();
        mMarker = null;
    }

}
