package com.dgaf.happyhour.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.dgaf.happyhour.Model.QueryParameters;
import com.dgaf.happyhour.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrawerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DrawerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrawerFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int NEARBY = 1;
    private static final int TOP_RATED = 2;
    private static final int ABOUT_US = 3;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ViewPagerFragment viewPagerFragment;
    private View topRated,nearby,aboutUs;

    private OnFragmentInteractionListener mListener;

    /* Lock the navigation drawer from closing when the user is
     * changing the seekbar
     */
    private View.OnTouchListener navLockListener= new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow Drawer to intercept touch events.
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    break;

                case MotionEvent.ACTION_UP:
                    // Allow Drawer to intercept touch events.
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }

            // Handle seekbar touch events.
            v.onTouchEvent(event);
            return true;
        }
    };


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DrawerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DrawerFragment newInstance(String param1, String param2) {
        DrawerFragment fragment = new DrawerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        viewPagerFragment = ViewPagerFragment.newInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_drawer, container, false);

        topRated = v.findViewById(R.id.topRatedLayout);
        topRated.setOnClickListener(this);
        nearby = v.findViewById(R.id.nearbyLayout);
        nearby.setOnClickListener(this);
        aboutUs = v.findViewById(R.id.aboutUsLayout);
        aboutUs.setOnClickListener(this);

        // Inflate the layout for this fragment
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void update(Fragment fragment,String identifier) {
        if (mListener != null) {
            mListener.onFragmentInteraction(fragment,identifier);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //listiners for "Normal Nav items" i.e. about us, top rated, nearby
    //TODO make it look like an actual click
    @Override
    public void onClick(View view) {

        //get rid of all higlight selection and then re add it
        unSelectOtherNormalNavItems();

        switch (view.getId()){
            case R.id.aboutUsLayout:
                view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                aboutUs();
                break;
            case R.id.topRatedLayout:
                view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                topRated();
                break;
            case R.id.nearbyLayout:
                view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                nearby();
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Fragment fragment,String identifier);
    }

    private void aboutUs(){
        mListener.onFragmentInteraction(new AboutFragment(),"about");
    }
    private void topRated(){
        sortByRating();
        mListener.onFragmentInteraction(viewPagerFragment,"viewPager");
    }
    private void nearby(){
        sortByProximity();
        mListener.onFragmentInteraction(viewPagerFragment, "viewPager");
    }

    /** Swaps fragments in the main content view */
    /*private void selectItem(int position) {

        Fragment fragment = null;
        String identifier = null;
        switch(position){
            // List of deals
            case HEADER:
                fragment = viewPagerFragment;
                identifier = "viewPager";
                mDrawerLayout.closeDrawers();
                break;

            case RATING:
                fragment = viewPagerFragment;
                identifier = "viewPager";
                mDrawerLayout.closeDrawers();
                break;

            case PROXIMITY:
                fragment = viewPagerFragment;
                identifier = "viewPager";
                mDrawerLayout.closeDrawers();
                break;

            case ABOUT_US:
                fragment = new AboutFragment();
                identifier = "about";
                this.setTitle("Burrd");
                break;
        }

    }*/

    //this will unselect normal items. this does'nt include days of the week.
    private void unSelectOtherNormalNavItems(){
        topRated.setBackground(null);
        nearby.setBackground(null);
        aboutUs.setBackground(null);
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
