package com.dgaf.happyhour.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dgaf.happyhour.Model.RestaurantAdapter;
import com.dgaf.happyhour.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by trentonrobison on 4/26/15.
 */

/*This is the fragment that our page view loads*/
public class RestaurantFragment extends Fragment {
    private static final String RESTAURANT_ID = "resId";
    private RestaurantAdapter mAdapter;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */


    public static RestaurantFragment newInstance(String restaurantId) {
        RestaurantFragment fragment = new RestaurantFragment();
        Bundle args = new Bundle();
        args.putString(RESTAURANT_ID, restaurantId);
        fragment.setArguments(args);
        return fragment;
    }

    public RestaurantFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.restaurant_fragment, container, false);

        Bundle args = this.getArguments();
        if (mAdapter == null) {
            mAdapter = new RestaurantAdapter(this, args.getString(RESTAURANT_ID), rootView);
        } else {
            mAdapter.loadRestaurantDetails(args.getString(RESTAURANT_ID));
            mAdapter.createViewHolders(container);
        }

        return rootView;
    }
}
