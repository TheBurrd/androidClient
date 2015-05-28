package com.dgaf.happyhour.Model;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.dgaf.happyhour.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseImageView;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 5/24/2015.
 */
public class RestaurantAdapter {
    private Fragment fragment;
    private ImageLoader imageLoader;
    private RestaurantModel restaurant;
    private RestaurantViewHolder restaurantHolder;
    private ExpandDealListAdapter expandDealListAdapter;

    public static class RestaurantViewHolder  implements ListView.OnTouchListener {
        public TextView restaurantName;
        public TextView description;
        public TextView address;
        public TextView phoneNo;
        public TextView website;
        public ExpandableListView dealList;
        public ParseImageView thumbnail;
        public GoogleMap map;


        public RestaurantViewHolder(Fragment fragment, View rootView, ExpandDealListAdapter listAdapter) {
            restaurantName = (TextView) rootView.findViewById(R.id.restaurantName);
            description = (TextView) rootView.findViewById(R.id.description);
            address = (TextView) rootView.findViewById(R.id.address);
            phoneNo = (TextView) rootView.findViewById(R.id.phoneNo);
            website = (TextView) rootView.findViewById(R.id.website);
            dealList = (ExpandableListView) rootView.findViewById(R.id.deal_list);
            dealList.setAdapter(listAdapter);
            thumbnail = (ParseImageView) rootView.findViewById(R.id.picture);
            thumbnail.setPlaceholder(ContextCompat.getDrawable(rootView.getContext(), R.drawable.llama));
            map = ((SupportMapFragment) fragment.getChildFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            map.getUiSettings().setScrollGesturesEnabled(false);

            dealList.setOnTouchListener(this);
        }

        public void updateMap(LatLng location, String markerText)
        {
            map.addMarker(new MarkerOptions().position(location).title(markerText));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            // Zoom in, animating the camera.
            map.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }

        //Disable scrolling in parent scroll view
        @Override
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
    }

    public RestaurantAdapter(Fragment fragment, String restaurantId, View fragmentView) {
        this.fragment = fragment;
        this.imageLoader = ImageLoader.getInstance();
        this.expandDealListAdapter = new ExpandDealListAdapter(fragment.getActivity(), restaurantId);
        this.restaurant = new RestaurantModel();
        createViewHolders(fragmentView);
        loadRestaurantDetails(restaurantId);
    }

    public void loadRestaurantDetails(String restaurantId) {
        ParseQuery<RestaurantModel> restaurantQuery = ParseQuery.getQuery(RestaurantModel.class);
        restaurantQuery.fromLocalDatastore();
        Log.v("Parse info:", "Started restaurant query");
        restaurantQuery.getInBackground(restaurantId, new GetCallback<RestaurantModel>() {
            public void done(RestaurantModel res, ParseException e) {
                Log.v("Parse info:", "Restaurant query returned");
                if (e == null) {
                    restaurant = res;
                    bindRestaurantViewHolder();
                } else {
                    Log.e("Parse error: ", e.getMessage());
                }
            }
        });

    }

    public void createViewHolders(View fragmentView) {
        restaurantHolder = new RestaurantViewHolder(fragment, fragmentView, expandDealListAdapter);
        bindRestaurantViewHolder();
    }

    public void bindRestaurantViewHolder() {
        if (restaurantHolder != null && restaurant != null) {
            ParseFile imageFile = restaurant.getThumbnailFile();
            if (imageFile != null) {
                imageLoader.displayImage(imageFile.getUrl(), restaurantHolder.thumbnail);
            }
            ParseGeoPoint parsePoint = restaurant.getLocation();
            if (parsePoint != null) {
                restaurantHolder.updateMap(new LatLng(parsePoint.getLatitude(), parsePoint.getLongitude()), restaurant.getName());
            } else {
                // Defaults to Geisel library if a restaurant hasn't loaded yet.
                restaurantHolder.updateMap(new LatLng(32.881122,-117.237631),"UCSD - Geisel Library");
            }
            restaurantHolder.restaurantName.setText(restaurant.getName());
            restaurantHolder.description.setText(restaurant.getDescription());
            restaurantHolder.address.setText(restaurant.getStreetNumber() + " " + restaurant.getStreetAddress() + ", " + restaurant.getCity() + ", " + restaurant.getState()+ ", " + restaurant.getZipcode());
            restaurantHolder.phoneNo.setText(restaurant.getPhoneNumber());
            restaurantHolder.website.setText(restaurant.getWebsite());

        }
    }
}
