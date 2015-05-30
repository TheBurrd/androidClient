package com.dgaf.happyhour.View;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dgaf.happyhour.DealListType;
import com.dgaf.happyhour.R;

import java.util.Locale;

public class ViewPagerFragment extends Fragment{

    SectionsPagerAdapter mPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.view_pager,container,false);
        ViewPager vp = (ViewPager) result.findViewById(R.id.pager);

        // We only have 3 tabs. Setting this limit to 3 prevents the fragments from being recreated
        // constantly at the expense of a little bit more memory usage.
        vp.setOffscreenPageLimit(2);

        mPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), 100.0);
        vp.setAdapter(mPagerAdapter);

        return result;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private DealListFragment mFoodFragment;
        private DealListFragment mDrinkFragment;
        private DealListFragment mFeaturedFragment;

        public SectionsPagerAdapter(FragmentManager fm, double searchRadius) {
            super(fm);
            mDrinkFragment = DealListFragment.newInstance(DealListType.DRINK, searchRadius);
            mFoodFragment = DealListFragment.newInstance(DealListType.FOOD, searchRadius);
            mFeaturedFragment = DealListFragment.newInstance(DealListType.FEATURED, searchRadius);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch(position) {
                case 0: {
                    return mDrinkFragment;
                }
                case 1: {
                    return mFoodFragment;
                }
                case 2: {
                    return mFeaturedFragment;
                }
                default:{
                    Log.e("Sections Adapter: ", "Bad page position requested");
                    return null;
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
                    return getString(R.string.drink).toUpperCase(l);
                case 1:
                    return getString(R.string.food).toUpperCase(l);
                case 2:
                    return getString(R.string.featured).toUpperCase(l);
            }
            return null;
        }
    }

}
