package com.dgaf.happyhour.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

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
public class DrawerFragment extends Fragment implements View.OnClickListener, ToggleButton.OnCheckedChangeListener {

    private View topRated,nearby,aboutUs;
    private ToggleButton monday,tuesday,wednesday,thursday,friday,saturday,sunday,today;

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

    // TODO: Rename and change types and number of parameters
    public static DrawerFragment newInstance() {
        DrawerFragment fragment = new DrawerFragment();
        return fragment;
    }

    public DrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_drawer, container, false);

        //normal drawer items
        topRated = v.findViewById(R.id.topRatedLayout);
        topRated.setOnClickListener(this);
        nearby = v.findViewById(R.id.nearbyLayout);
        nearby.setOnClickListener(this);
        aboutUs = v.findViewById(R.id.aboutUsLayout);
        aboutUs.setOnClickListener(this);

        //weekday toggle buttons
        monday = (ToggleButton) v.findViewById(R.id.monday);
        tuesday = (ToggleButton) v.findViewById(R.id.tuesday);
        wednesday = (ToggleButton) v.findViewById(R.id.wednesday);
        thursday = (ToggleButton) v.findViewById(R.id.thursday);
        friday = (ToggleButton) v.findViewById(R.id.friday);
        saturday = (ToggleButton) v.findViewById(R.id.saturday);
        sunday = (ToggleButton) v.findViewById(R.id.sunday);
        today = (ToggleButton) v.findViewById(R.id.today);


        today.setOnCheckedChangeListener(this);
        monday.setOnCheckedChangeListener(this);
        tuesday.setOnCheckedChangeListener(this);
        wednesday.setOnCheckedChangeListener(this);
        thursday.setOnCheckedChangeListener(this);
        friday.setOnCheckedChangeListener(this);
        saturday.setOnCheckedChangeListener(this);
        sunday.setOnCheckedChangeListener(this);


        //seekbar stuff
        SeekBar seekBar = (SeekBar) v.findViewById(R.id.seek_bar);
        //seekBar.setProgress(queryParameters.getRadiusMi() - 1);
        final TextView seekBarText = (TextView) v.findViewById(R.id.seekbarText);
        //seekBarText.setText(queryParameters.getRadiusMi() + " mi");
        seekBarText.setOnTouchListener(navLockListener);
        seekBar.setOnTouchListener(navLockListener);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //queryParameters.setRadiusMi(seekBar.getProgress() + 1);
                //queryParameters.notifyAllListeners();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                seekBarText.setText((progress + 1) + " mi");
            }
        });








        topRated.setBackgroundColor(getResources().getColor(R.color.colorPrimary));


        // Inflate the layout for this fragment
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void update(Fragment fragment,String identifier) {
        if (mListener != null) {
            mListener.onFragmentInteraction(fragment,identifier);
        }
    }

    public void uncheckWeekdayToggle(){
        monday.setBackgroundResource(R.drawable.ic_monday);
        tuesday.setBackgroundResource(R.drawable.ic_tuesday);
        wednesday.setBackgroundResource(R.drawable.ic_wednesday);
        thursday.setBackgroundResource(R.drawable.ic_thursday);
        friday.setBackgroundResource(R.drawable.ic_friday);
        saturday.setBackgroundResource(R.drawable.ic_saturday);
        sunday.setBackgroundResource(R.drawable.ic_sunday);

        monday.setChecked(false);
        tuesday.setChecked(false);
        wednesday.setChecked(false);
        thursday.setChecked(false);
        friday.setChecked(false);
        saturday.setChecked(false);
        sunday.setChecked(false);
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

    //listeners for "Normal Nav items" i.e. about us, top rated, nearby
    @Override
    public void onClick(View view) {

        //get rid of all higlight selection and then re add it

        switch (view.getId()){
            case R.id.aboutUsLayout:
                unSelectOtherNormalNavItems();
                view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                aboutUs();
                break;
            case R.id.topRatedLayout:
                unSelectOtherNormalNavItems();
                view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                topRated();
                break;
            case R.id.nearbyLayout:
                unSelectOtherNormalNavItems();
                view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                nearby();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean enabled) {
        if(enabled){
            switch (compoundButton.getId()){
                case R.id.monday:
                    compoundButton.setBackgroundResource(R.drawable.ic_monday_active);
                    break;
                case R.id.tuesday:
                    compoundButton.setBackgroundResource(R.drawable.ic_tuesday_active);
                    break;
                case R.id.wednesday:
                    compoundButton.setBackgroundResource(R.drawable.ic_wednesday_active);
                    break;
                case R.id.thursday:
                    compoundButton.setBackgroundResource(R.drawable.ic_thursday_active);
                    break;
                case R.id.friday:
                    compoundButton.setBackgroundResource(R.drawable.ic_friday_active);
                    break;
                case R.id.saturday:
                    compoundButton.setBackgroundResource(R.drawable.ic_saturday_active);
                    break;
                case R.id.sunday:
                    compoundButton.setBackgroundResource(R.drawable.ic_sunday_active);
                    break;
                case R.id.today:
                    compoundButton.setBackgroundResource(R.drawable.ic_thursday_active);
                    uncheckWeekdayToggle();
                    break;
            }
        }else{
            switch(compoundButton.getId()){
                case R.id.monday:
                    compoundButton.setBackgroundResource(R.drawable.ic_monday);
                    break;
                case R.id.tuesday:
                    compoundButton.setBackgroundResource(R.drawable.ic_tuesday);
                    break;
                case R.id.wednesday:
                    compoundButton.setBackgroundResource(R.drawable.ic_wednesday);
                    break;
                case R.id.thursday:
                    compoundButton.setBackgroundResource(R.drawable.ic_thursday);
                    break;
                case R.id.friday:
                    compoundButton.setBackgroundResource(R.drawable.ic_friday);
                    break;
                case R.id.saturday:
                    compoundButton.setBackgroundResource(R.drawable.ic_saturday);
                    break;
                case R.id.sunday:
                    compoundButton.setBackgroundResource(R.drawable.ic_sunday);
                    break;
                case R.id.today:
                    compoundButton.setBackgroundResource(R.drawable.ic_thursday);
                    break;

            }
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
        void loadViewPager(String identifier);
    }

    private void aboutUs(){
        mListener.onFragmentInteraction(new AboutFragment(),"about");
    }
    private void topRated(){
        sortByRating();
        mListener.loadViewPager("viewPager");
    }
    private void nearby(){
        sortByProximity();
        mListener.loadViewPager("viewPager");
    }

    //this will unselect normal items. this doesn't include days of the week.
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
