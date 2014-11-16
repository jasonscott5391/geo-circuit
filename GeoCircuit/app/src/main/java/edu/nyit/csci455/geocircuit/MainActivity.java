package edu.nyit.csci455.geocircuit;

import android.support.v4.app.FragmentActivity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import edu.nyit.csci455.geocircuit.Interface.Constants;
import edu.nyit.csci455.geocircuit.util.DrawerItemListAdapter;


public class MainActivity extends FragmentActivity {

    private GeoMapFragment mMapFragment;

    private CircuitFragment mCircuitFragment;

    private DrawerLayout mDrawerLayout;

    private ListView mDrawerList;

    private DrawerItemListAdapter mDrawerListAdapter;

    private ActionBarDrawerToggle mDrawerToggle;

    private SettingsFragment mSettings;

    private CharSequence mTitle;

    private DrawerItemClickListener mDrawerItemClickListener;

    private SharedPreferences mSharedPreferences;

    private int mCurrentFeature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        initializeDrawerMenu();

        mTitle = getTitle();

        mMapFragment = (GeoMapFragment) getFragmentManager().findFragmentById(R.id.map);

        mSettings = new SettingsFragment();

        if (mMapFragment == null) {
            mMapFragment = new GeoMapFragment();
        }

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_layout, mMapFragment)
                .commit();

        //TODO (jasonscott) Change other Activities to Fragments.

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSharedPreferences = getSharedPreferences("user_preferences", 0);
        mCurrentFeature = mSharedPreferences.getInt(
                "current_feature",
                Constants.DASHBOARD);
        selectItem(mCurrentFeature);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.action_settings) {
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, mSettings)
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    private void initializeDrawerMenu() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerList = (ListView) findViewById(R.id.drawer_list);

        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(Constants.FEATURES[mCurrentFeature]);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerListAdapter = new DrawerItemListAdapter(this);

        mDrawerList.setAdapter(mDrawerListAdapter);

        mDrawerItemClickListener = new DrawerItemClickListener();

        mDrawerList.setOnItemClickListener(mDrawerItemClickListener);
    }

    /**
     * @param position
     */
    private void selectItem(int position) {
        //TODO (jasonscott) Switch statement to handle different cases of onClick.
        mCurrentFeature = position;
        mDrawerLayout.closeDrawer(mDrawerList);

        switch (position) {
            case Constants.DASHBOARD:
                break;
            case Constants.CIRCUIT_MANAGER:
                if (mCircuitFragment == null) {
                    mCircuitFragment = new CircuitFragment();
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_layout, mCircuitFragment)
                        .commit();
                break;
            case Constants.NEAR_ME:
                break;

            default:
                break;
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }


}
