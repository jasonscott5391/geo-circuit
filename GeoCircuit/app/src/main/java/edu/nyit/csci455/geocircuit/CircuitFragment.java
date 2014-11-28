package edu.nyit.csci455.geocircuit;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.nyit.csci455.geocircuit.normalized.Circuit;
import edu.nyit.csci455.geocircuit.normalized.GeoLocation;
import edu.nyit.csci455.geocircuit.util.GeoCircuitDbHelper;

/**
 * <p>CircuitFragment.java</p>
 * <p>GeoCircuit's Circuit Manager is the tool that allows
 * user to view past circuits recorded.</p>
 *
 * @author jasonscott
 */
public class CircuitFragment extends Fragment {

    private ListView mCircuitList;

    private static LayoutInflater sInflater;

    private CircuitListAdapter mCircuitListAdapter;

    private CircuitListOnItemClickListener mCircuitListOnItemClickListener;

    private View mCircuitListHeader;

    private TextView mCircuitListHeaderText;

    private Point mDimensions;

    private ArrayList mCircuits;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sInflater = getActivity().getLayoutInflater();
        mCircuitListHeader = sInflater.inflate(R.layout.item_circuit_header, null, true);
        mCircuitListHeaderText = (TextView) mCircuitListHeader.findViewById(R.id.item_circuit_header);
        mDimensions = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(mDimensions);
        mCircuits = GeoCircuitDbHelper.getInstance(getActivity()).retrieveAllCircuits();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceStat) {
        View view;

        view = inflater.inflate(R.layout.fragment_circuit, container, false);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (mDimensions.y / 3));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        view.setLayoutParams(layoutParams);

        mCircuitList = (ListView) view.findViewById(R.id.list_circuit);

        mCircuitListAdapter = new CircuitListAdapter();
        mCircuitList.setAdapter(mCircuitListAdapter);

        mCircuitList.addHeaderView(mCircuitListHeader);

        mCircuitListHeaderText.setText(mCircuitListAdapter.getCount() + " Circuit(s)");

        mCircuitListOnItemClickListener = new CircuitListOnItemClickListener();
        mCircuitList.setOnItemClickListener(mCircuitListOnItemClickListener);

        return view;
    }

    /**
     * Returns the time duration of circuit between the
     * specified starting and ending locations.
     *
     * @param start Specified start Location.
     * @param end   Specified end Location.
     * @return String duration time traveled.
     */
    private String getCircuitDuration(GeoLocation start, GeoLocation end) {
        long difference = start.getDate() - end.getDate();

        long seconds = difference / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        String time = hours % 24 + " hours, " + minutes % 60 + " minutes, " + seconds % 60 + " seconds";

        return time;
    }

    /**
     * Returns the distance traveled of a circuit between the
     * specified starting and ending locations.
     *
     * @param start Specified start Location.
     * @param end   Specified end Location.
     * @return String distance traveled.
     */
    private String getCircuitDistance(GeoLocation start, GeoLocation end) {
        int earthRadius = 6371;
        double kmToMi = 0.621371;

        // Difference in latitude and longitude in radians.
        double dLat = Math.toRadians(end.getLatitude() - start.getLatitude());
        double dLng = Math.toRadians(end.getLongitude() - start.getLongitude());

        // Convert latitudes to radians.
        double startlatRads = Math.toRadians(start.getLatitude());
        double endLatRads = Math.toRadians(end.getLatitude());

        // Calculate the angle
        double angle = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLng / 2) * Math.sin(dLng / 2) * Math.cos(startlatRads) * Math.cos(endLatRads);

        // Calculate the angular distance
        double angularDistance = 2 * Math.atan2(Math.sqrt(angle), Math.sqrt(1 - angle));

        // Convert to kilometers
        double distanceKm = earthRadius * angularDistance;

        // Convert to Miles
        double distanceMi = distanceKm * kmToMi;

        return String.format("%.2f", distanceMi);
    }

    /**
     * Inner class, adapter for Circuit Manager ListView.
     */
    private class CircuitListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mCircuits.size();
        }

        @Override
        public Object getItem(int position) {
            return mCircuits.get(position);
        }

        @Override
        public long getItemId(int position) {
            return ((Circuit) mCircuits.get(position)).getCircuitId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                view = sInflater.inflate(R.layout.item_circuit, null);
            }

            Circuit circuit = (Circuit) getItem(position);

            TextView timeView = (TextView) view.findViewById(R.id.item_circuit_time);
            TextView dateView = (TextView) view.findViewById(R.id.item_circuit_date);
            TextView nameView = (TextView) view.findViewById(R.id.item_circuit_name);
            TextView durationView = (TextView) view.findViewById(R.id.item_circuit_duration);
            TextView distanceView = (TextView) view.findViewById(R.id.item_circuit_distance);

            Date date = new Date(circuit.getStartLocation().getDate());
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aaa");
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");

            timeView.setText(timeFormat.format(date));
            dateView.setText(dateFormat.format(date));
            nameView.setText(circuit.getCircuitName());

            durationView.setText(
                    getCircuitDuration(
                            circuit.getStartLocation(),
                            circuit.getEndLocation()));
            distanceView.setText(
                    String.valueOf(
                            getCircuitDistance(
                                    circuit.getStartLocation(),
                                    circuit.getEndLocation()))
                            + " mi");

            return view;
        }
    }

    /**
     * Inner class, OnItemClickListener for Circuit Manager ListView.
     */
    private class CircuitListOnItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Callback to MainActivity and modify the map.
            // IF this is even possible.  Need to investigate/research.
        }
    }
}
