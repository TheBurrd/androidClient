package com.dgaf.happyhour.Model;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
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
    private List<DealModel> dealItems;
    private RestaurantViewHolder restaurantHolder;

    public static class RestaurantViewHolder {
        public TextView restaurantName;
        public TextView description;
        public TextView address;
        public TextView phoneNo;
        public TextView website;
        public ParseImageView thumbnail;
        public GoogleMap map;


        public RestaurantViewHolder(Fragment fragment, View rootView) {
            restaurantName = (TextView) rootView.findViewById(R.id.restaurantName);
            description = (TextView) rootView.findViewById(R.id.description);
            address = (TextView) rootView.findViewById(R.id.address);
            phoneNo = (TextView) rootView.findViewById(R.id.phoneNo);
            website = (TextView) rootView.findViewById(R.id.website);
            thumbnail = (ParseImageView) rootView.findViewById(R.id.picture);
            thumbnail.setPlaceholder(ContextCompat.getDrawable(rootView.getContext(), R.drawable.llama));
            map = ((SupportMapFragment) fragment.getChildFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        }

        public void updateMap(LatLng location)
        {
            map.addMarker(new MarkerOptions().position(location).title("Here"));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            // Zoom in, animating the camera.
            map.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }
    }

    public RestaurantAdapter(Fragment fragment, String restaurantId, View fragmentView) {
        this.fragment = fragment;
        this.imageLoader = ImageLoader.getInstance();
        restaurant = new RestaurantModel();
        dealItems = new ArrayList<>();
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

                    ParseQuery<DealModel> restaurantDeals = ParseQuery.getQuery(DealModel.class);
                    restaurantDeals.whereEqualTo("restaurantId", restaurant);
                    Log.v("Parse info:", "Started restaurant deals query");
                    restaurantDeals.findInBackground(new FindCallback<DealModel>() {
                        public void done(List<DealModel> deals, ParseException e) {
                            Log.v("Parse info:", "Restaurant deals query returned");
                            if (e == null) {
                                dealItems = deals;
                            } else {
                                Log.e("Parse error: ", e.getMessage());
                            }
                        }
                    });
                } else {
                    Log.e("Parse error: ", e.getMessage());
                }
            }
        });

    }

    public void createViewHolders(View fragmentView) {
        restaurantHolder = new RestaurantViewHolder(fragment, fragmentView);
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
                restaurantHolder.updateMap(new LatLng(parsePoint.getLatitude(), parsePoint.getLongitude()));
            } else {
                // Defaults to Geisel library if a restaurant hasn't loaded yet.
                restaurantHolder.updateMap(new LatLng(32.881122,-117.237631));
            }
            restaurantHolder.restaurantName.setText(restaurant.getName());
            restaurantHolder.description.setText(restaurant.getDescription());
            restaurantHolder.address.setText(restaurant.getStreetNumber() + " " + restaurant.getStreetAddress() + ", " + restaurant.getCity() + ", " + restaurant.getState()+ ", " + restaurant.getZipcode());
            restaurantHolder.phoneNo.setText(restaurant.getPhoneNumber());
            restaurantHolder.website.setText(restaurant.getWebsite());

        }
    }
}
