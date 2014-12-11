package edu.nyit.csci455.geocircuit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

    private boolean mRefreshing = false;

    private WeatherAsyncTask mWeatherTask;

    private Location mStartLocation;

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
        mWeatherTask = new WeatherAsyncTask();
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

    @Override
    public void onResume() {
        super.onResume();

        if (mStartLocation == null) {
            mStartLocation = mOnRecordingCircuitListener.getLastLocation();
        }

        runWeatherHttpClient(mStartLocation);
    }

    /**
     * Updates the temperature with the specified temp.
     *
     * @param temp Specified temp.
     */
    public void updateTemperature(double temp) {
        int tempDegF = (int) Math.round(((temp - 273) * (9 / 5)) + 32);

        mDashGridAdapter.updateThermometer(tempDegF);
    }

    /**
     * Updates the speed with the specified speed.
     *
     * @param speed Specified speed.
     */
    public void updateSpeed(float speed) {
        mDashGridAdapter.updateSpeedometer(speed);
    }

    /**
     * Updates the compass with the specified azimuth.
     *
     * @param azimuth Specified azimuth.
     */
    public void updateCompass(float azimuth) {
        mDashGridAdapter.updateMagnetometer(azimuth);
    }

    /**
     * Updates the duration of the circuit with the specified duration.
     *
     * @param duration Specified duration.
     */
    public void updateDuration(long duration) {
        mDashGridAdapter.updateDuration(duration);
    }

    /**
     * Updates the distance traveled of the circuit with the specified distance.
     *
     * @param distance Specified distance.
     */
    public void updateDistance(float distance) {
        mDashGridAdapter.updateDistance(distance);
    }

    /**
     * Interface defining methods for recording circuits.
     */
    public interface OnRecordingCircuitListener {
        public void recordCircuit();

        public boolean isRecordingCircuit();

        public Location getLastLocation();
    }

    /**
     * Runs the HTTP client for getting the ambient temperature for the specified location.
     *
     * @param location Specified location.
     */
    private void runWeatherHttpClient(Location location) {
        if (!mRefreshing) {
            mWeatherTask.execute(location);
        }
    }

    /**
     * Inner class, adapter for GridView.
     */
    private class DashboardGridAdapter extends BaseAdapter {

        private final String[] MEASUREMENTS = {"THERMOMETER", "SPEEDOMETER", "MAGNETOMETER",
                "DURATION", "START/STOP", "DISTANCE"};

        private HashMap valueViews;

        private HashMap unitViews;

        private ArrayList values;

        private ArrayList units;

        private DashboardGridAdapter() {

            valueViews = new HashMap();
            unitViews = new HashMap();

            values = new ArrayList();
            units = new ArrayList();

            values.add("-\u2109");
            units.add("Ambient");
            values.add(0);
            units.add("MPH");
            values.add("-");
            units.add(0);
            values.add("--:--:--");
            units.add("Duration");
            values.add(mOnRecordingCircuitListener.isRecordingCircuit());
            units.add("Start");
            values.add("-");
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

                Button imageButton = (Button) valueViews.get(MEASUREMENTS[position]);
                TextView textView = (TextView) unitViews.get(MEASUREMENTS[position]);

                if (imageButton == null) {
                    imageButton = (Button) view.findViewById(R.id.dash_button);
                    valueViews.put(MEASUREMENTS[position], imageButton);
                }

                if (textView == null) {
                    textView = (TextView) view.findViewById(R.id.dash_button_text);
                    unitViews.put(MEASUREMENTS[position], textView);
                }

                textView.setTextSize(36);
                textView.setText(String.valueOf(units.get(position)));

                flipImageButton();

                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnRecordingCircuitListener.recordCircuit();
                        flipImageButton();
                    }
                });

            } else {

                if (view == null) {
                    view = sInflater.inflate(R.layout.item_dash_text, null);
                }

                TextView valueText = (TextView) valueViews.get(MEASUREMENTS[position]);
                TextView unitText = (TextView) unitViews.get(MEASUREMENTS[position]);

                if (valueText == null) {
                    valueText = (TextView) view.findViewById(R.id.dash_text_value);
                    valueViews.put(MEASUREMENTS[position], valueText);
                }

                if (unitText == null) {
                    unitText = (TextView) view.findViewById(R.id.dash_text_units);
                    unitViews.put(MEASUREMENTS[position], unitText);
                }

                valueText.setTextSize(48);
                valueText.setText(String.valueOf(values.get(position)));

                unitText.setTextSize(36);
                unitText.setText(String.valueOf(units.get(position)));
            }

            int width = (mDimensions.x / 3);
            int height = ((mDimensions.y / 3) / 4);

            view.setBackground(
                    resizeDrawable(
                            getResources()
                                    .getDrawable(R.drawable.bg_grid),
                            width,
                            height));

            return view;
        }

        /**
         * Flips the image background of the Start/Finish Button.
         */
        private void flipImageButton() {
            Button imageButton = (Button) valueViews.get(MEASUREMENTS[4]);
            TextView textView = (TextView) unitViews.get(MEASUREMENTS[4]);

            if (!mOnRecordingCircuitListener.isRecordingCircuit()) {
                // Start button
                imageButton.setBackground(getResources().getDrawable(R.drawable.ic_start));
                textView.setText("Start");
            } else {
                // Finish button
                imageButton.setBackground(getResources().getDrawable(R.drawable.ic_finish));
                textView.setText("Finish");
            }
        }

        /**
         * Updates the thermometer TextView with the specified temperature value in degrees F.
         *
         * @param temp Specified temperature.
         */
        private void updateThermometer(int temp) {
            values.remove(0);
            values.add(0, temp);

            TextView textView = (TextView) valueViews.get(MEASUREMENTS[0]);
            String tempString = temp + "\u2109";
            textView.setText(tempString);
        }

        /**
         * Updates the speedometer TextView with the specified speed value in MPH.
         *
         * @param speed
         */
        private void updateSpeedometer(float speed) {
            values.remove(1);
            values.add(1, speed);
            int speedMph = (int) Math.round(2.2369362920544 * speed);
            TextView textView = (TextView) valueViews.get(MEASUREMENTS[1]);
            textView.setText(String.valueOf(speedMph));
        }

        /**
         * Updates the magnetometer TextView with specified azimuth.
         *
         * @param azimuth Specified azimuth.
         */
        private void updateMagnetometer(float azimuth) {
            values.remove(2);
            values.add(2, azimuth);

            float azimuthDegrees = (float) Math.toDegrees(azimuth);

            if (azimuthDegrees < 0.0f) {
                azimuthDegrees += 360.0f;
            }

            TextView textView = (TextView) valueViews.get(MEASUREMENTS[2]);
            TextView unitView = (TextView) unitViews.get(MEASUREMENTS[2]);

            textView.setText(getCompassHeading(azimuthDegrees));
            unitView.setText(String.valueOf(Math.round(azimuthDegrees)) + "\u00B0");
        }

        /**
         * Updates the duration TextView with the specified duration.
         *
         * @param duration Specified duration.
         */
        private void updateDuration(long duration) {
            values.remove(3);
            values.add(3, duration);
            TextView textView = (TextView) valueViews.get(MEASUREMENTS[3]);
            textView.setText(String.valueOf(duration));
        }

        /**
         * Updates the distance TextView with the sepcified distance.
         *
         * @param distance Specified distance.
         */
        private void updateDistance(float distance) {
            values.remove(5);
            values.add(5, distance);
            TextView textView = (TextView) valueViews.get(MEASUREMENTS[5]);
            textView.setText(String.valueOf(distance));
        }

        /**
         * Returns the compass heading of a specified
         * azimuth.
         *
         * @param azimuthDegrees Specified azimuth.
         * @return String representation of compass heading.
         */
        private String getCompassHeading(float azimuthDegrees) {

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

        /**
         * Returns the specified Drawable re-sized to the specified width and height.
         *
         * @param drawable
         * @param width
         * @param height
         * @return
         */
        private Drawable resizeDrawable(Drawable drawable, int width, int height) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, width, height, false);
            return new BitmapDrawable(getResources(), bitmapResized);
        }
    }

    /**
     * AsyncTask for HTTP Client calling open source weather api.
     */
    private class WeatherAsyncTask extends AsyncTask<Location, Integer, Double> {

        private Location location;

        @Override
        protected void onPreExecute() {
            mRefreshing = true;
        }

        @Override
        protected Double doInBackground(Location... params) {
            Double temperature = null;

            String result;
            InputStream inputStream;

            try {
                location = params[0];

                Uri uri = new Uri.Builder()
                        .scheme("http")
                        .authority(getResources().getString(R.string.weather_authority))
                        .path(getResources().getString(R.string.weather_path))
                        .appendQueryParameter("lat", String.valueOf(location.getLatitude()))
                        .appendQueryParameter("lon", String.valueOf(location.getLongitude()))
                        .build();

                URL url = new URL(uri.toString());

                inputStream = connect(url);

                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                result = stringBuilder.toString();

                temperature = getTemperatureFromJson(result);

            } catch (Exception e) {

            }

            return temperature;
        }

        @Override
        protected void onPostExecute(Double result) {
            if (result != null) {
                updateTemperature(result);
            }

            mRefreshing = false;
        }

        @Override
        protected void onCancelled(Double result) {
            mRefreshing = false;
        }

        /**
         * Returns an InputStream from an HTTP connection to the
         * specified URL.
         *
         * @param url Specified URL.
         * @return InputStream from HTTP connection.
         * @throws java.io.IOException
         */
        private InputStream connect(URL url) throws IOException {
            InputStream inputStream;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(5000 /* milliseconds */);
            conn.setConnectTimeout(5000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            inputStream = conn.getInputStream();

            return inputStream;
        }

        /**
         * Returns temperature in Kelvins parsed from the specified JSON.
         *
         * @param json Specified JSON
         * @return Double of temperature in Kelvin.
         * @throws JSONException
         * @throws IOException
         */
        private Double getTemperatureFromJson(String json) throws JSONException, IOException {
            Double temperature = null;

            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                temperature = jsonObject.getJSONObject("main").getDouble("temp");
            }

            return temperature;
        }
    }

}
