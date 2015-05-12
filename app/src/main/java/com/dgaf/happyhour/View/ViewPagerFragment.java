package com.dgaf.happyhour.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dgaf.happyhour.Model.DealListAdapter;
import com.dgaf.happyhour.R;

import java.util.Locale;

/**
 * Created by trentonrobison on 5/9/15.
 */
public class ViewPagerFragment extends Fragment{

    private DealListAdapter food;
    private DealListAdapter drink;
    private DealListAdapter feature;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View result = inflater.inflate(R.layout.view_pager,container,false);
        ViewPager vp = (ViewPager) result.findViewById(R.id.pager);

        vp.setAdapter(new SectionsPagerAdapter(getChildFragmentManager()));

        return result;
    }




    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).


            switch(position) {
                case 0: {
                    return DealListFragment.newInstance(position, 0);
                    //return BarFragment.newInstance(position,23);
                }
                case 1: {
                    return DealListFragment.newInstance(position, 1);
                    //return FeaturedFragment.newInstance(position);
                }
                case 2: {
                    return DealListFragment.newInstance(position, 2);
                    //return FoodFragment.newInstance(position);
                }
                default:{
                    return DealListFragment.newInstance(position, 0);
                }
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.bar).toUpperCase(l);
                case 1:
                    return getString(R.string.food).toUpperCase(l);
                case 2:
                    return getString(R.string.featured).toUpperCase(l);

            }
            return null;
        }
    }

}
