package edu.nyit.csci455.geocircuit;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
/**
 * Created by jasonscott on 11/7/14.
 */
public class DashboardFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        GridView gridView = new GridView(getActivity());



        return gridView;
    }
}
