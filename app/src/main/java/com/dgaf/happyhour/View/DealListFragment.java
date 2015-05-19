package com.dgaf.happyhour.View;


import android.app.ListFragment;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.ListFragment;
import android.widget.Toast;

import com.dgaf.happyhour.Controller.MainActivity;
import com.dgaf.happyhour.Controller.MyLocationListener;
import com.dgaf.happyhour.Model.DealFetch;
import com.dgaf.happyhour.Model.DealListAdapter;
import com.dgaf.happyhour.R;

import java.util.ArrayList;
import java.util.List;

/*This is the fragment that our page view loads*/
public class DealListFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String QUERY_DECISION = "query";
    private String[] list_items;
    MyLocationListener gps;
    Button btnShowLocation; //Dummy Button to get location.


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    //private int  querySearchNumber;


    //section ID acts like ID for the query search as well
    public static DealListFragment newInstance(int sectionNumber, int query)

    {
        DealListFragment fragment = new DealListFragment();
        Bundle args = new Bundle();
        args.putInt(QUERY_DECISION, query);
        fragment.setArguments(args);

        return fragment;
    }

    public DealListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feature_food_drink, container, false);


        //Test text
        //TextView tv = (TextView) rootView.findViewById(R.id.textView);

        //int querySearch = this.getArguments().getInt(QUERY_DECISION);
        //tv.setText("I am going to query " + (querySearch));

        //The deal List:
                //Get the list
        list_items = getResources().getStringArray(R.array.list);
                //Populate the adapter with the strings(for now)
        ListAdapter dealAdapter = new DealListAdapter(this.getActivity(), list_items);
                //Link the list with xml
        ListView dealList = (ListView) rootView.findViewById(R.id.deals);
                //Populate list with adapters
        dealList.setAdapter(dealAdapter);


        //Location Testing:
                //

        btnShowLocation = (Button) rootView.findViewById(R.id.show_location);


        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gps = new MyLocationListener(getActivity());

                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    Toast.makeText(
                            getActivity(),
                            "Your Location is -\nLat: " + latitude + "\nLong: "
                                    + longitude, Toast.LENGTH_LONG).show();
                } else {
                    gps.showSettingsAlert();
                }
            }
        });


            return rootView;
        }

    }