package com.dgaf.happyhour.Controller;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dgaf.happyhour.Model.Queries.QueryParameters;
import com.dgaf.happyhour.R;


public class MainActivity extends AppCompatActivity implements DrawerFragment.OnFragmentInteractionListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private ViewPagerFragment viewPager;

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


        //Load the viewpager. It will inflate the viewpager fragment into the main_fragment in main
        if(getSupportFragmentManager().findFragmentById(R.id.main_fragment) == null){
            getSupportFragmentManager().beginTransaction().add(R.id.main_fragment, new ViewPagerFragment()).commit();
        }

        //inflate drawer fragment
        DrawerFragment drawerFragment = new DrawerFragment();
        FragmentManager frgManager = getSupportFragmentManager();
        FragmentTransaction trans = frgManager.beginTransaction();
        trans.replace(R.id.drawerItems, drawerFragment);
        trans.commit();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.open,
                R.string.closed) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                ((DrawerFragment)getSupportFragmentManager().findFragmentById(R.id.drawerItems)).giveFeedBack();
                QueryParameters.getInstance().notifyAllListeners();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        viewPager = ViewPagerFragment.newInstance();

        mDrawerLayout.setDrawerListener(mDrawerToggle);

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

    @Override
    public void loadAbout() {
        launchFragment(new AboutFragment(), "about");
        mDrawerLayout.closeDrawers();//adds animation
    }

    @Override
    public void loadViewPager() {
        launchFragment(viewPager,"viewPager");
        mDrawerLayout.closeDrawers();//adds animation
    }

    public void launchFragment(Fragment fragment,String identifier){
        if (fragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();

            while (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStackImmediate();
            }

            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, fragment).addToBackStack(identifier).commit();

        }
    }
}

