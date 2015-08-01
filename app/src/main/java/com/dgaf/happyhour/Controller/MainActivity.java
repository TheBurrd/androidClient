package com.dgaf.happyhour.Controller;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dgaf.happyhour.Adapter.DrawerListAdapter;
import com.dgaf.happyhour.Adapter.NavItem;
import com.dgaf.happyhour.Model.QueryParameters;
import com.dgaf.happyhour.R;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private static final int HEADER = 0;
    private static final int RATING = 1;
    private static final int PROXIMITY = 2;
    private static final int ABOUT_US = 5;

    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ViewPagerFragment viewPagerFragment;
    private Toolbar toolbar;



    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    @Override
    public boolean onSupportNavigateUp(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
        return true;
    }

    /**
     * Sets up the Navigation Drawer and load the DealListFragment
     * with RATING as the default Query.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        this.setTitle("Today's Deals");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        if(getSupportFragmentManager().findFragmentById(R.id.main_fragment) == null){
            getSupportFragmentManager().beginTransaction().add(R.id.main_fragment, new ViewPagerFragment()).commit();
        }

        mNavItems.add(new NavItem("Top Rated", R.drawable.ic_thumb_up));
        mNavItems.add(new NavItem("Nearby", R.drawable.ic_proximity));
        mNavItems.add(new NavItem("Explore", R.drawable.ic_drawer));
        mNavItems.add(new NavItem("Days of the Week", R.drawable.ic_calendar));
        mNavItems.add(new NavItem("About Us", R.drawable.ic_aboutus));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Populate the Navigation Drawer with options
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setCacheColorHint(0);//avoids changing list color when scrolling


        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems, mDrawerLayout);
        View header = getLayoutInflater().inflate(R.layout.header_nav, null);//inflate header view

        //header is not clickable
        header.setEnabled(false);
        header.setOnClickListener(null);

        //add a header to our navigation drawer
        mDrawerList.addHeaderView(header);
        mDrawerList.setSelectionAfterHeaderView();


        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.open,
                R.string.closed) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        viewPagerFragment = ViewPagerFragment.newInstance();

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        selectItem(RATING);// update selected item and title, then close the drawer
        //adapter.setSelectedPosition(0);
        //mDrawerList.setItemChecked(RATING, true);
        //adapter.notifyDataSetChanged();

    }

    /**Swaps fragments in the main content view.
     * Handles all of the Nav Drawer navigation*/
    private void selectItem(int position) {

        Fragment fragment = null;
        boolean createFragment = true;

        /*we will open all the various fragments from the sliding drawer here*/
        switch(position){

            //this is currently not used
            //we need to figure out if we want to implement clicking the header
            case HEADER:
                fragment = viewPagerFragment;
                break;

            case RATING:
                fragment = viewPagerFragment;
                sortByRating();
                break;

            case PROXIMITY:
                fragment = viewPagerFragment;
                sortByProximity();
                break;

            case ABOUT_US:
                fragment = new AboutFragment();
                break;
        }
        if (fragment != null && createFragment == true ){


            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_fragment, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            mDrawerLayout.closeDrawers();//adds animation
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    // Displays the users favorites
    private void displayFavorites() {

    }

    // Change the sorting mechanism for deals to sort by rating
    private void sortByRating() {
        QueryParameters queryParams = QueryParameters.getInstance();
        queryParams.setQueryType(QueryParameters.QueryType.RATING);
    }

    // Change the sorting mechanism for deals to sort by proximity
    private void sortByProximity() {
        QueryParameters queryParams = QueryParameters.getInstance();
        queryParams.setQueryType(QueryParameters.QueryType.PROXIMITY);
    }
}

