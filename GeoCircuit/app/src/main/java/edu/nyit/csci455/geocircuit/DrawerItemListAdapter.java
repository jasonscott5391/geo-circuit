package edu.nyit.csci455.geocircuit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jasonscott on 10/25/14.
 */
public class DrawerItemListAdapter extends BaseAdapter {

    private Activity mActivity;
    private static LayoutInflater sInflater;

    public DrawerItemListAdapter(Activity activity) {
        mActivity = activity;
        sInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // TODO (jasonscott) Modify to handle swapping out of fragment options.

        View view = convertView;

        if (view == null) {
            view = sInflater.inflate(R.layout.item_drawer, null);
        }

        ImageView image = (ImageView) view.findViewById(R.id.item_image);
        TextView title = (TextView) view.findViewById(R.id.item_text);

        switch (position) {
            case 0:
                image.setImageResource(R.drawable.ic_circuit_manager);
                title.setText(R.string.action_circuit_manager);
                break;

            case 1:
                image.setImageResource(R.drawable.ic_near_me);
                title.setText(R.string.action_near_me);
                break;

            default:
                break;
        }

        return view;
    }
}
