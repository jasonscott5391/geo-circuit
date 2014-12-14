package edu.nyit.csci455.geocircuit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import edu.nyit.csci455.geocircuit.normalized.Place;

/**
 * <p>NearMeFragment.java</p>
 * <p>GeoCircuit's Near Me features is a tool that
 * searches and displays for nearby desirable places.</p>
 *
 * @author jasonscott
 */
public class NearMeFragment extends Fragment {

    private ListView mPlacesList;

    private static LayoutInflater sInflater;

    private PlacesListAdapter mPlacesListAdapter;

    private PlacesListOnItemClickListener mPlacesListOnItemClickListener;

    private View mPlacesListHeader;

    private TextView mPlacesListHeaderText;

    private ArrayList mPlaces;

    private OnPlaceSelectedListener mPlaceSelectedListener;

    private GeoPlacesAsyncTask mPlacesAsyncTask;

    private Location mLocation;

    private Point mDimensions;

    private boolean mRefreshing = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mPlaceSelectedListener = (OnPlaceSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnPlaceSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sInflater = getActivity().getLayoutInflater();
        mPlacesListHeader = sInflater.inflate(R.layout.item_place_header, null, true);
        mPlacesListHeaderText = (TextView) mPlacesListHeader.findViewById(R.id.item_place_header);
        mPlacesAsyncTask = new GeoPlacesAsyncTask();
        mDimensions = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(mDimensions);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceStat) {
        View view;

        view = inflater.inflate(R.layout.fragment_near_me, container, false);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (mDimensions.y / 3));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        view.setLayoutParams(layoutParams);

        mPlaces = new ArrayList();

        mPlacesList = (ListView) view.findViewById(R.id.list_place);

        mPlacesListAdapter = new PlacesListAdapter();

        mPlacesList.addHeaderView(mPlacesListHeader, null, false);
        mPlacesList.setAdapter(mPlacesListAdapter);

        mPlacesListHeaderText.setText(mPlacesListAdapter.getCount() + " Places nearby");

        mPlacesListOnItemClickListener = new PlacesListOnItemClickListener();
        mPlacesList.setOnItemClickListener(mPlacesListOnItemClickListener);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLocation == null) {
            mLocation = mPlaceSelectedListener.getLastLocation();
        }

        runPlacesHttpClient(mLocation);
    }

    /**
     * Executes the Asynchronous task for calling Google Places API for the
     * specified Location.
     *
     * @param location Specified Location.
     */
    public void runPlacesHttpClient(Location location) {
        if (!mRefreshing) {
            mPlacesAsyncTask.execute(location);
        }

    }


    /**
     * Adapter for Place ListView.
     */
    private class PlacesListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mPlaces.size();
        }

        @Override
        public Object getItem(int position) {
            return mPlaces.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                view = sInflater.inflate(R.layout.item_place, null);
            }

            Place place = (Place) mPlaces.get(position);

            ImageView icon = (ImageView) view.findViewById(R.id.place_icon);
            TextView name = (TextView) view.findViewById(R.id.place_title);
            TextView distance = (TextView) view.findViewById(R.id.place_distance);

            icon.setImageBitmap(place.getIcon());
            name.setText(place.getName());
            distance.setText(place.getDistance() + "Mi");

            return view;
        }
    }

    /**
     * OnItemClickListener for Place ListView.  Calls back to MainActivity to call dropPlace
     * in GeoMapFragment.
     */
    private class PlacesListOnItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mPlaceSelectedListener.onPlaceSelected((Place) mPlaces.get(position - 1));
        }
    }

    /**
     * Asynchronous task for creating HTTP client for Google Places API.
     */
    private class GeoPlacesAsyncTask extends AsyncTask<Location, Integer, ArrayList<Place>> {

        private Location location;

        @Override
        protected void onPreExecute() {
            mRefreshing = true;
        }

        @Override
        protected ArrayList<Place> doInBackground(Location... params) {
            ArrayList<Place> places = new ArrayList<Place>();

            String result;
            InputStream inputStream = null;

            try {
                location = params[0];
                Uri uri = new Uri.Builder()
                        .scheme("https")
                        .authority(getResources().getString(R.string.places_authority))
                        .path(getResources().getString(R.string.places_path))
                        .appendQueryParameter("location", location.getLatitude() + "," + location.getLongitude())
                        .appendQueryParameter("radius", "500")
                        .appendQueryParameter("key", getResources().getString(R.string.places_key)).build();

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

                places = getPlacesFromJson(result);
                //TODO (jasonscott) Call publishProgress(Integer... progress)

            } catch (Exception e) {

            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {

                }
            }

            return places;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        //TODO (jasonscott) Add progress dialog or spinner in header view.
        }

        @Override
        protected void onPostExecute(ArrayList<Place> result) {
            if (!result.isEmpty()) {
                mPlaces = result;
                Collections.sort(mPlaces);
                mPlacesListAdapter.notifyDataSetChanged();
                mPlacesListHeaderText.setText(mPlacesListAdapter.getCount() + " Places nearby");
            }

            mRefreshing = false;
        }

        @Override
        protected void onCancelled(ArrayList<Place> result) {
            mRefreshing = false;
        }

        /**
         * Returns an InputStream from an HTTP connection to the
         * specified URL.
         *
         * @param url Specified URL.
         * @return InputStream from HTTP connection.
         * @throws IOException
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
         * Parses json response creating Place objects from the results.
         *
         * @param json String representation of json objects.
         * @return Returns and ArrayList of Place objects.
         */
        private ArrayList<Place> getPlacesFromJson(String json) throws JSONException, IOException {
            ArrayList<Place> places = new ArrayList<Place>();
            if (json != null) {

                JSONArray jsonArray = new JSONObject(json).getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    Place place = new Place();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    System.out.println();

                    place.setName(jsonObject.getString("name"));
                    place.setIcon(
                            BitmapFactory.decodeStream(
                                    connect(new URL(jsonObject.getString("icon")))));

                    JSONObject locationObject = jsonObject
                            .getJSONObject("geometry")
                            .getJSONObject("location");

                    float lat = (float) locationObject.getDouble("lat");
                    float lng = (float) locationObject.getDouble("lng");
                    place.setLatitude(lat);
                    place.setLongitude(lng);

                    String distance = place.getPlaceDistance(location.getLatitude(), location.getLongitude());
                    place.setDistance(distance);

                    places.add(place);
                }


            }

            return places;
        }
    }

    /**
     * Listener for selecting a Place and getting last Location.
     */
    public interface OnPlaceSelectedListener {
        public void onPlaceSelected(Place place);
        public Location getLastLocation();
    }
}

