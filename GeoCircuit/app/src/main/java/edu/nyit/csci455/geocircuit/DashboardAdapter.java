package edu.nyit.csci455.geocircuit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Matt on 12/1/2014.
 */
public class DashboardAdapter extends BaseAdapter {

        private Context context;
        private final int[] textViewValues;

        public DashboardAdapter(Context context, int[] textViewValues) {
            this.context = context;
            this.textViewValues = textViewValues;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View gridView;

            if (convertView == null) {

                gridView = new View(context);

                // get layout
                gridView = inflater.inflate(R.layout.fragment_dashboard, null);


                TextView textView = (TextView) gridView
                        .findViewById(R.id.info_message);
                textView.setText(textViewValues[position]);
            } else {
                gridView = (View) convertView;
            }

            return gridView;
        }

        @Override
        public int getCount() {
            return textViewValues.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

    }







