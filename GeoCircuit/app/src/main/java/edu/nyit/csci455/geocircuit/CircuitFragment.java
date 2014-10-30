package edu.nyit.csci455.geocircuit;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by jasonscott on 10/23/14.
 */
public class CircuitFragment extends ListFragment {

    private ListView mCircuitList;

    private static LayoutInflater sInflater;

    private CircuitListAdapter mCircuitListAdapter;

    private CircuitListOnItemClickListener mCircuitListOnItemClickListener;

    private View mCircuitListHeader;

    private TextView mCircuitListHeaderText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        sInflater = getActivity().getLayoutInflater();
        mCircuitListHeader = sInflater.inflate(R.layout.item_circuit_header, null, true);
        mCircuitListHeaderText = (TextView) mCircuitListHeader.findViewById(R.id.item_circuit_header);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceStat) {
        View view;

        view = inflater.inflate(R.layout.fragment_circuit, container, false);
        mCircuitList = (ListView) view.findViewById(R.id.list_circuit);
        mCircuitList.addHeaderView(mCircuitListHeader);

        mCircuitListAdapter = new CircuitListAdapter();
        mCircuitList.setAdapter(mCircuitListAdapter);

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
                sInflater.inflate(R.layout.item_circuit, null);
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
