package edu.nyit.csci455.geocircuit;

import android.app.Activity;
import android.content.SharedPreferences;
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

    private SharedPreferences mSharedPreferences;

    private int mCurrentPosition;

    private OnCircuitSelectedListener mCircuitSelectedListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCircuitSelectedListener = (OnCircuitSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCircuitSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sInflater = getActivity().getLayoutInflater();
        mCircuitListHeader = sInflater.inflate(R.layout.item_circuit_header, null, true);
        mCircuitListHeaderText = (TextView) mCircuitListHeader.findViewById(R.id.item_circuit_header);
        mDimensions = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(mDimensions);
        mCircuits = GeoCircuitDbHelper.getInstance(getActivity()).retrieveAllCircuits();
        mSharedPreferences = getActivity()
                .getSharedPreferences("user_preferences", 0);

        mCurrentPosition = mSharedPreferences.getInt("current_position", 0);
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

        mCircuitList.addHeaderView(mCircuitListHeader, null, false);

        mCircuitList.setAdapter(mCircuitListAdapter);
        mCircuitListHeaderText.setText(mCircuitListAdapter.getCount() + " Circuit(s)");

        mCircuitListOnItemClickListener = new CircuitListOnItemClickListener();
        mCircuitList.setOnItemClickListener(mCircuitListOnItemClickListener);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public Circuit getCurrentCircuit() {
        Circuit circuit = (Circuit) mCircuits.get(mCurrentPosition);
        return circuit;
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

            Date date = new Date(((GeoLocation) circuit.getGeoLocations().get(0)).getDate());
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aaa");
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");

            timeView.setText(timeFormat.format(date));
            dateView.setText(dateFormat.format(date));
            nameView.setText(circuit.getCircuitName());

            durationView.setText(
                    circuit.calculateCircuitDuration());

            distanceView.setText(circuit.calculateCircuitDistance() + " mi");

            return view;
        }
    }

    /**
     * Inner class, OnItemClickListener for Circuit Manager ListView.
     */
    private class CircuitListOnItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mCircuitSelectedListener.onCircuitSelected((Circuit) mCircuits.get(position - 1));

        }
    }

    /**
     * Inner callback interface that must be implemented by MainActivity.
     */
    public interface OnCircuitSelectedListener {
        public void onCircuitSelected(Circuit circuit);
    }
}
