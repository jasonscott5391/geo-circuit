package edu.nyit.csci455.geocircuit;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.nyit.csci455.geocircuit.normalized.Place;

/**
 * Created by jasonscott on 10/23/14.
 */
public class NearMeFragment extends Fragment {

    private ListView mPlaceList;

    private static LayoutInflater sInflater;

    private NearMeListAdapter mPlaceListAdapter;

    private PlaceListOnItemClickListener mPlaceListOnItemClickListener;

    private View mPlaceListHeader;

    private TextView mPlaceListHeaderText;

    private ArrayList mPlaces;

    private OnPlaceSelectedListener mPlaceSelectedListener;

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
    }


    /**
     *
     */
    private class NearMeListAdapter extends BaseAdapter {

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
            return null;
        }
    }

    /**
     *
     */
    private class PlaceListOnItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mPlaceSelectedListener.onPlaceSelected((Place) mPlaces.get(position));
        }
    }

    //TODO (jasonscott) HTTP Client to make calls to Google Places API.
    //TODO (jasonscott) Limit number of queries to 1,000/day.

    /**
     *
     */
    private class GeoPlacesAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }
    }

    public interface OnPlaceSelectedListener {
        public void onPlaceSelected(Place place);
    }
}

