package edu.nyit.csci455.geocircuit;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;

import edu.nyit.csci455.geocircuit.util.DashboardGridAdapter;

/**
 * Created by jasonscott on 11/7/14.
 */
public class DashboardFragment extends Fragment {

    private GridView mDashGrid;

    private DashboardGridAdapter mDashGridAdapter;

    private Point mDimensions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        return mDashGrid;
    }

    /**
     * Interface defining methods for recording circuits.
     */
    public interface OnRecordingCircuitListener {
        public void recordCircuit(boolean recording);
        public boolean isRecordingCircuit();
    }
}
