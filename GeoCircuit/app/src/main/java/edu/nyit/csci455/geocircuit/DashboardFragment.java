package edu.nyit.csci455.geocircuit;

import android.app.Activity;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p>DashboardFragment.java</p>
 * <p></p>
 *
 * @author jasonscott
 */
public class DashboardFragment extends Fragment {

    private GridView mDashGrid;

    private DashboardGridAdapter mDashGridAdapter;

    private OnRecordingCircuitListener mOnRecordingCircuitListener;

    private static LayoutInflater sInflater;

    private Point mDimensions;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mOnRecordingCircuitListener = (OnRecordingCircuitListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnRecordingCircuitListener");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sInflater = getActivity().getLayoutInflater();
        mDimensions = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(mDimensions);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (mDimensions.y / 3));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        view.setLayoutParams(layoutParams);

        mDashGrid = (GridView) view.findViewById(R.id.dashboard);

        mDashGridAdapter = new DashboardGridAdapter();

        mDashGrid.setAdapter(mDashGridAdapter);


        return view;
    }

    /**
     * @param temp
     */
    public void updateTemperature(float temp) {
        mDashGridAdapter.updateThermometer(temp);
    }

    /**
     * @param speed
     */
    public void updateSpeed(float speed) {
        mDashGridAdapter.updateSpeedometer(speed);
    }

    /**
     * @param azimuth
     */
    public void updateCompass(float azimuth) {
        mDashGridAdapter.updateMagnetometer(azimuth);
    }

    /**
     * @param duration
     */
    public void updateDuration(long duration) {
        mDashGridAdapter.updateDuration(duration);
    }

    /**
     * @param distance
     */
    public void updateDistance(float distance) {
        mDashGridAdapter.updateDistance(distance);
    }

    /**
     * Interface defining methods for recording circuits.
     */
    public interface OnRecordingCircuitListener {
        public void recordCircuit(boolean recording);

        public boolean isRecordingCircuit();
    }

    /**
     *
     */
    private class DashboardGridAdapter extends BaseAdapter {

        private final String[] MEASUREMENTS = {"THERMOMETER", "SPEEDOMETER", "MAGNETOMETER",
                "DURATION", "START/STOP", "DISTANCE"};

        private HashMap views;

        private ArrayList values;

        private ArrayList units;

        private DashboardGridAdapter() {

            views = new HashMap();
            values = new ArrayList();
            units = new ArrayList();

            values.add(0.0);
            units.add("\u2109");
            values.add(0.0);
            units.add("MPH");
            values.add(0.0);
            units.add("");
            values.add(0);
            units.add("Duration");
            values.add(false);
            units.add("");
            values.add(0);
            units.add("Mi");


        }

        @Override
        public int getCount() {
            return values.size();
        }

        @Override
        public Object getItem(int position) {
            return values.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (position == 4) {

                if (view == null) {
                    view = sInflater.inflate(R.layout.item_dash_image, null);
                }

                ImageView imageView = (ImageView) views.get(MEASUREMENTS[position]);

                if (imageView == null) {
                    imageView = (ImageView) view.findViewById(R.id.dash_image_button);
                    views.put(MEASUREMENTS[position], imageView);
                }

                Boolean recording = (Boolean) values.get(position);

                if (!recording) {
                    // Start button
                    imageView.setImageResource(R.drawable.ic_location);
                } else {
                    // Stop button
                    imageView.setImageResource(R.drawable.ic_location_marker);
                }

            } else {

                if (view == null) {
                    view = sInflater.inflate(R.layout.item_dash_text, null);
                }

                TextView valueText = (TextView) views.get(MEASUREMENTS[position]);

                if (valueText == null) {
                    valueText = (TextView) view.findViewById(R.id.dash_text_value);
                    views.put(MEASUREMENTS[position], valueText);
                }

                TextView unitText = (TextView) view.findViewById(R.id.dash_text_units);

                valueText.setTextSize(48);
                valueText.setText(String.valueOf(values.get(position)));

                unitText.setTextSize(36);
                unitText.setText(String.valueOf(units.get(position)));
            }

            return view;
        }

        /**
         * @param temp
         */
        private void updateThermometer(float temp) {
            values.remove(0);
            values.add(0, temp);
            TextView textView = (TextView) views.get(MEASUREMENTS[0]);
            textView.setText(String.valueOf(temp));
        }

        /**
         * @param speed
         */
        private void updateSpeedometer(float speed) {
            values.remove(1);
            values.add(1, speed);
            double speedMph = 2.2369362920544 * speed;
            TextView textView = (TextView) views.get(MEASUREMENTS[1]);
            textView.setText(String.valueOf(speedMph));
        }

        /**
         * @param azimuth
         */
        private void updateMagnetometer(float azimuth) {
            values.remove(2);
            values.add(2, azimuth);

            TextView textView = (TextView) views.get(MEASUREMENTS[2]);
            textView.setText(getCompassHeading(azimuth));
        }

        /**
         * @param duration
         */
        private void updateDuration(long duration) {
            values.remove(3);
            values.add(3, duration);
            TextView textView = (TextView) views.get(MEASUREMENTS[3]);
            textView.setText(String.valueOf(duration));
        }

        /**
         * @param distance
         */
        private void updateDistance(float distance) {
            values.remove(5);
            values.add(5, distance);
            TextView textView = (TextView) views.get(MEASUREMENTS[5]);
            textView.setText(String.valueOf(distance));
        }

        /**
         * Returns the compass heading of a specified
         * azimuth.
         *
         * @param azimuth Specified azimuth.
         * @return String representation of compass heading.
         */
        private String getCompassHeading(float azimuth) {
            float azimuthDegrees = (float) Math.toDegrees(azimuth);

            if (azimuthDegrees < 0.0f) {
                azimuthDegrees += 360.0f;
            }
            
            if (azimuthDegrees >= 0 && azimuthDegrees < 30
                    || azimuthDegrees >= 330 && azimuthDegrees < 360) {
                return "N";
            }

            if (azimuthDegrees >= 30 && azimuthDegrees < 60) {
                return "NE";
            }

            if (azimuthDegrees >= 60 && azimuthDegrees < 120) {
                return "E";
            }

            if (azimuthDegrees >= 120 && azimuthDegrees < 150) {
                return "SE";
            }

            if (azimuthDegrees >= 150 && azimuthDegrees < 210) {
                return "S";
            }

            if (azimuthDegrees >= 210 && azimuthDegrees < 240) {
                return "SW";
            }

            if (azimuthDegrees >= 240 && azimuthDegrees < 300) {
                return "W";
            }

            if (azimuthDegrees >= 300 && azimuthDegrees < 330) {
                return "NW";
            }

            return "";
        }
    }

    //TODO Write HTTP client for open source weather API
    /**
     * AsyncTask for HTTP Client calling open source weather api.
     */
    private class WeatherAsyncTask extends AsyncTask<Location, Integer, Double> {

        @Override
        protected Double doInBackground(Location... params) {
            return null;
        }
    }

}
