package edu.nyit.csci455.geocircuit;

import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import edu.nyit.csci455.geocircuit.Interface.*;

/**
 * <p>Title: GeoMapFragment.java</p>
 * <p>Description: </p>
 *
 * @author jasonscott
 */
public class GeoMapFragment extends MapFragment {

    private GoogleMap mGoogleMap;

    private Point mDimensions;

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

    public void dashboardMode(Location location) {
//        mGoogleMap.setMyLocationEnabled(true);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }
}
