package com.dgaf.happyhour.Model;

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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseImageView;
import com.parse.ParseQuery;

import org.w3c.dom.Text;


/**
 * Created by Adam on 5/24/2015.
 */
public class RestaurantAdapter {
    private Fragment fragment;
    private ImageLoader imageLoader;
    private RestaurantModel restaurant;
    private DealModel deal;
    private RestaurantViewHolder restaurantHolder;
    private ExpandDealListAdapter expandDealListAdapter;

    public static class RestaurantViewHolder implements ListView.OnTouchListener {
        public TextView dealTitle;
        public TextView dealDescription;
        public TextView rating;
        public TextView restaurantName;
        public TextView restaurantDescription;
        public TextView proximity;
        public TextView hoursOfOperation;
        public TextView address;
        public TextView phoneNo;
        public TextView website;
        public ExpandableListView dealList;
        public ParseImageView thumbnail;
        public GoogleMap map;

        public RestaurantViewHolder(Fragment fragment, View rootView, ExpandDealListAdapter listAdapter) {
            dealTitle = (TextView) rootView.findViewById(R.id.deal_title);
            dealDescription = (TextView) rootView.findViewById(R.id.deal_description);
            rating = (TextView) rootView.findViewById(R.id.rating);
            restaurantName = (TextView) rootView.findViewById(R.id.restaurant_name);
            restaurantDescription = (TextView) rootView.findViewById(R.id.restaurant_description);
            proximity = (TextView) rootView.findViewById(R.id.proximity);
            hoursOfOperation = (TextView) rootView.findViewById(R.id.hours_of_operation);
            address = (TextView) rootView.findViewById(R.id.address);
            phoneNo = (TextView) rootView.findViewById(R.id.phoneNo);
            website = (TextView) rootView.findViewById(R.id.website);
            dealList = (ExpandableListView) rootView.findViewById(R.id.deal_list);
            dealList.setAdapter(listAdapter);
            thumbnail = (ParseImageView) rootView.findViewById(R.id.picture);
            thumbnail.setPlaceholder(ContextCompat.getDrawable(rootView.getContext(), R.drawable.ic_food_placeholder));
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

    public RestaurantAdapter(Fragment fragment,View fragmentView, String restaurantId, String dealId) {
        this.fragment = fragment;
        this.imageLoader = ImageLoader.getInstance();
        this.expandDealListAdapter = new ExpandDealListAdapter(fragment.getActivity(), restaurantId);
        this.restaurant = new RestaurantModel();
        this.deal = new DealModel();
        createViewHolders(fragmentView);
        loadRestaurantDetails(restaurantId, dealId);
    }

    public void loadRestaurantDetails(String restaurantId, String dealId) {
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
        ParseQuery<DealModel> dealQuery = ParseQuery.getQuery(DealModel.class);
        dealQuery.fromLocalDatastore();
        Log.v("Parse info:", "Started deal query");
        dealQuery.getInBackground(dealId, new GetCallback<DealModel>() {
            public void done(DealModel returnedDeal, ParseException e) {
                Log.v("Parse info:", "Deal query returned");
                if (e == null) {
                    deal = returnedDeal;
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
            restaurantHolder.restaurantDescription.setText(restaurant.getDescription());
            restaurantHolder.dealTitle.setText(deal.getTitle());
            restaurantHolder.dealDescription.setText(deal.getDescription());
            //restaurantHolder.rating.setText(deal.getRating());
            //restaurantHolder.hoursOfOperation.setText(restaurant.getAvailability());
            restaurantHolder.address.setText(restaurant.getStreetNumber() + " " + restaurant.getStreetAddress() + ", " + restaurant.getCity() + ", " + restaurant.getState()+ ", " + restaurant.getZipcode());
            restaurantHolder.phoneNo.setText(restaurant.getPhoneNumber());
            restaurantHolder.website.setText(restaurant.getWebsite());

        }
    }
}
