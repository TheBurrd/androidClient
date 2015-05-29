package com.dgaf.happyhour.Controller;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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

 //   private String[] drawerItems = {"Deals","Favorites","Rating", "Proximity", "Monday",
 //           "Tuesday", "Wednesday", "Thursday", "Friday", "About Us"};
    private ListView mDrawerList;
    private RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View mRootDrawerView;

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
        mNavItems.add(new NavItem("Rating", R.drawable.ic_drawer));
        mNavItems.add(new NavItem("Proximity", R.drawable.ic_proximity));
        mNavItems.add(new NavItem("SeekBar", R.drawable.ic_drawer));
        mNavItems.add(new NavItem("Days of the Week", R.drawable.ic_calendar));
        mNavItems.add(new NavItem("week days", R.drawable.ic_drawer));
        mNavItems.add(new NavItem("divider", R.drawable.ic_drawer));
        mNavItems.add(new NavItem("Favorites", R.drawable.llama));
        mNavItems.add(new NavItem("divider", R.drawable.ic_drawer));
        mNavItems.add(new NavItem("About Us", R.drawable.ic_aboutus));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mRootDrawerView = (View) findViewById(R.id.root_drawer);
        // Populate the Navigation Drawer with options
        //mDrawerPane = (RelativeLayout) findViewById(R.id.drawer_pane);
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


/*
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, drawerItems));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.open,
                R.string.closed) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("HappyHour");
                // invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Options");
                // invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
    }

    /* TODO this needs to be declared in its own file*/
 /*
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }
 */


    /* This can be used if we want to open a new fragment such as the
     * PreferencesFragment for when we click an item in the nav drawer.
     */
 /*
    private void selectItemFromDrawer(int position) {
        Fragment fragment = new PreferencesFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fragment, fragment)
                .commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).mTitle);

        mDrawerLayout.closeDrawer(mDrawerPane);
    }
 */



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

            // bar
            case 1:
                break;

            // rating
            case 2:
                sortByRating();
                break;

            //proximity
            case 3:
                break;

            //seek
            case 4:
                break;

            //days of the week
            case 5:
                break;

            //days
            case 6:
                break;

            //bar
            case 7:
                break;

            //favorites
            case 8:
                displayFavorites();
                break;

            //bar
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
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            //setTitle(mNames[position]);

            //TODO we need to get the rootview not the list
            //mDrawerLayout.closeDrawer(mDrawerList);
            mDrawerLayout.closeDrawer(mRootDrawerView);

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
        else if (id == R.id.action_settings) {
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

/* NavItem class that defines a single row in the Navigation Drawer
 * It consists of an icon for the element and a string for the element itself
 */
class NavItem {
    String mTitle;
    int mIcon;

    public NavItem(String title, int icon) {
        mTitle = title;
        mIcon = icon;
    }
}

/* An adapter used for the drawer list view.  Contains the actual rows for the
 * Navigation Drawer in terms of an ArrayList of NavItems
 */
class DrawerListAdapter extends BaseAdapter  {

    Context mContext;
    ArrayList<NavItem> mNavItems;
    private static final int NORMAL = 0;
    private static final int DAYS = 1;
    private static final int SEEK = 2;
    private static final int DIVIDER = 3;

    public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
        mContext = context;
        mNavItems = navItems;
    }

    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    /** Return the view that should be inflated */
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        View v = convertView;
        LayoutInflater inflater;

        switch (type) {
            // Ordinary row
            case NORMAL:
                    inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = inflater.inflate(R.layout.drawer_item, null);

                    TextView text = (TextView) v.findViewById(R.id.title);
                    ImageView icon =  (ImageView) v.findViewById(R.id.icon);

                    // Update the row with the correct title and image corresponding to the item
                    text.setText(mNavItems.get(position).mTitle);
                    icon.setImageResource(mNavItems.get(position).mIcon);
                break;

            // Row containing the days of the week
            case DAYS:
                    inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = inflater.inflate(R.layout.weekday_item, null);

                    Button monday =  (Button) v.findViewById(R.id.monday);
                    Button tuesday = (Button) v.findViewById(R.id.tuesday);
                    Button wednesday = (Button) v.findViewById(R.id.wednesday);
                    Button thursday = (Button) v.findViewById(R.id.thursday);
                    Button friday = (Button) v.findViewById(R.id.friday);
                    Button saturday = (Button) v.findViewById(R.id.saturday);
                    Button sunday =  (Button) v.findViewById(R.id.sunday);

                    //TODO Respond to when the buttons are clicked
                    monday.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(mContext, "monday was clicked", Toast.LENGTH_SHORT).show();
                        }
                    });

                    tuesday.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(mContext, "tuesday was clicked", Toast.LENGTH_SHORT).show();
                        }
                    });

                    wednesday.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(mContext, "wednesday was clicked", Toast.LENGTH_SHORT).show();
                        }
                    });

                    thursday.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(mContext, "thursday was clicked", Toast.LENGTH_SHORT).show();
                        }
                    });

                    friday.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(mContext, "friday was clicked", Toast.LENGTH_SHORT).show();
                        }
                    });

                    saturday.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(mContext, "saturday was clicked", Toast.LENGTH_SHORT).show();
                        }
                    });

                    sunday.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(mContext, "sunday was clicked", Toast.LENGTH_SHORT).show();
                        }
                    });

                break;

            // Black line that divides the navdrawer
            case DIVIDER:
                inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.line_item, null);
               break;

            // Seek bar for proximity
            case SEEK:
                inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.seekbar_item, null);

                // Set a listener for the view to disable navdrawer movements
                // when scrubbing the seekbar
                v.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    // Disallow user access to navigation drawer when they are
                    // touching the seekbar row
                    public boolean onTouch(View v, MotionEvent event) {
                        int action = event.getAction();
                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                // Disallow ScrollView to intercept touch events.
                                v.getParent().requestDisallowInterceptTouchEvent(true);
                                break;

                            case MotionEvent.ACTION_UP:
                                // Allow ScrollView to intercept touch events.
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                break;
                        }

                        // Handle ListView touch events.
                        v.onTouchEvent(event);
                        return true;
                    }
                });

                SeekBar seekBar = (SeekBar) v.findViewById(R.id.seek_bar);
                final TextView seekBarText = (TextView) v.findViewById(R.id.seek_bar_text);

                // TODO what should happen when the seek bar is changed
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }


                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    }

                });
                break;
        }

        return v;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    /** Given a position of a row on the navigation drawer, return the type
     * of view that should be inflated.
     */
    public int getItemViewType(int position) {
        int type = 0;

        if (position == 0 || position == 2 || position == 3 || position == 5 || position == 8 || position == 10) {
            type = NORMAL;
        }else if (position == 1 || position == 7 || position == 9) {
            type = DIVIDER;
        }else if( position == 4 ){
            type = SEEK;
        }else if( position == 6){
            type = DAYS;
        }
        return type;
    }

    // These are not used in the current code as indicated by Android studio
    /*
    //These view holders will speed up scrolling
    static class NormalHolder {
        TextView text;
        ImageView icon;
    }
    static class SeekHolder {
        TextView text;
        SeekBar radius;
    }
    static class DaysHolder {
        Button monday;
        Button tuesday;
        Button wednesday;
        Button thursday;
        Button friday;
        Button saturday;
        Button sunday;

    }
    */
}
