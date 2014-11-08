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

/**
 * <p>Title: CircuitFragment.java</p>
 * <p>Description: </p>
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sInflater = getActivity().getLayoutInflater();
        mCircuitListHeader = sInflater.inflate(R.layout.item_circuit_header, null, true);
        mCircuitListHeaderText = (TextView) mCircuitListHeader.findViewById(R.id.item_circuit_header);
        mDimensions = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(mDimensions);

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


    private class CircuitListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO (jasonscott) Query database for number of circuits.
            return 4;
        }

        @Override
        public Object getItem(int position) {
            // TODO (jasonscott) Query database for the position recent circuit.
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                view = sInflater.inflate(R.layout.item_circuit, null);
            }

            TextView time = (TextView) view.findViewById(R.id.item_circuit_time);
            TextView date = (TextView) view.findViewById(R.id.item_circuit_date);
            TextView name = (TextView) view.findViewById(R.id.item_circuit_name);
            TextView duration = (TextView) view.findViewById(R.id.item_circuit_duration);
            TextView distance = (TextView) view.findViewById(R.id.item_circuit_distance);

            // TODO Query database for item values.

            return view;
        }
    }

    private class CircuitListOnItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Callback to MainActivity and modify the map.
            // IF this is even possible.  Need to investigate/research.
        }
    }
}
