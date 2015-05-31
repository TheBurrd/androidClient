package com.dgaf.happyhour.Controller;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dgaf.happyhour.Model.DrawerListAdapter;
import com.dgaf.happyhour.Model.NavItem;
import com.dgaf.happyhour.R;
import com.dgaf.happyhour.View.About;
import com.dgaf.happyhour.View.ViewPagerFragment;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    @Override
    public boolean onSupportNavigateUp(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getSupportFragmentManager().findFragmentById(R.id.main_fragment) == null){
            getSupportFragmentManager().beginTransaction().add(R.id.main_fragment, new ViewPagerFragment()).commit();
        }

        mNavItems.add(new NavItem("Deals", R.drawable.ic_launcher));
        mNavItems.add(new NavItem("divider", R.drawable.ic_drawer));
        mNavItems.add(new NavItem("Rating", R.drawable.ic_thumb_up));
        mNavItems.add(new NavItem("Proximity", R.drawable.ic_proximity));
        mNavItems.add(new NavItem("SeekBar", R.drawable.ic_drawer));
        mNavItems.add(new NavItem("Days of the Week", R.drawable.ic_calendar));
        mNavItems.add(new NavItem("week days", R.drawable.ic_drawer));
        mNavItems.add(new NavItem("divider", R.drawable.ic_drawer));
        mNavItems.add(new NavItem("Favorites", R.drawable.llama));
        mNavItems.add(new NavItem("divider", R.drawable.ic_drawer));
        mNavItems.add(new NavItem("About Us", R.drawable.ic_aboutus));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Populate the Navigation Drawer with options
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setCacheColorHint(0);//avoids changing list color when scrolling
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
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
                R.drawable.ic_drawer,
                R.string.open,
                R.string.closed) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("Burrd");
                // invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Options");
                // invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {

        Fragment fragment = null;
        String identifier = null;
        /*we will open all the various fragments from the sliding drawer here*/
        switch(position){
            // List of deals
            case 0:
                fragment = new ViewPagerFragment();
                break;

            // divider
            case 1:
                break;

            // rating
            case 2:
                sortByRating();
                break;

            // proximity
            case 3:
                sortByProximity();
                break;

            //seek
            case 4:
                break;

            // days of the week
            case 5:
                break;

            // days
            case 6:
                break;

            // divider
            case 7:
                break;

            // favorites
            case 8:
                displayFavorites();
                break;

            // divider
            case 9:
                break;

            // About Us
            case 10:
                fragment = new About();
                identifier = "about";
                break;
        }
        if (fragment != null)
        {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment).addToBackStack(identifier).commit();
            if (position == 0)
            {
                while (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStackImmediate();
                }
            }

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, false);
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

    /*This is were all the action events happen*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
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

    }

    // Change the sorting mechanism for deals to sort by proximity
    private void sortByProximity() {

    }

}

