package com.dgaf.happyhour.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dgaf.happyhour.Controller.LocationService;
import com.dgaf.happyhour.Model.DealModel;
import com.dgaf.happyhour.Model.ModelUpdater;
import com.dgaf.happyhour.Model.RestaurantModel;
import com.dgaf.happyhour.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseGeoPoint;

import java.util.List;


/**
 * Created by Adam on 5/24/2015.
 */
public class RestaurantAdapter implements ModelUpdater<RestaurantModel> {
    private Activity activity;
    private RestaurantModel restaurant;
    private ParseGeoPoint parseLocation;
    private RestaurantViewHolder restaurantHolder;

    public static class RestaurantViewHolder implements View.OnClickListener {
        private ImageView dialPhone;
        private ImageView visitWebsite;
        private TextView proximity;
        private TextView hoursOfOperation;
        private TextView address;
        private GoogleMap map;

        public Activity activity;
        public RestaurantAdapter parentAdapter;

        public RestaurantViewHolder(Activity activity, RestaurantAdapter parentAdapter) {
            this.activity = activity;
            this.parentAdapter = parentAdapter;

            proximity = (TextView) activity.findViewById(R.id.distance);
            dialPhone = (ImageView) activity.findViewById(R.id.phone);
            visitWebsite = (ImageView) activity.findViewById(R.id.website);
            address = (TextView) activity.findViewById(R.id.address);
            map = ((MapFragment) activity.getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            map.getUiSettings().setScrollGesturesEnabled(false);

            dialPhone.setOnClickListener(this);
            visitWebsite.setOnClickListener(this);

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

        @Override
        public void onClick(View v) {
            if (v == dialPhone) {
                String phoneNumber = parentAdapter.getPhoneNumber();
                if (phoneNumber != null) {
                    Uri number = Uri.parse("tel:" + parentAdapter.getPhoneNumber());
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                    activity.startActivity(callIntent);
                }
            } else if (v ==  visitWebsite) {
                String website = parentAdapter.getWebsite();
                if (website != null) {
                    if (!website.startsWith("http://") && !website.startsWith("https://"))
                        website = "http://" + website;
                    Uri webaddress = Uri.parse(website);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, webaddress);
                    activity.startActivity(browserIntent);
                }
            }
        }
    }

    public RestaurantAdapter(Activity activity) {
        this.activity = activity;
        createViewHolders();
    }

    @Override
    public void onDataModelUpdate(List<RestaurantModel> restaurants, Exception e) {
        if (e == null) {
            if (restaurants.size() == 1) {
                restaurant = restaurants.get(0);
                parseLocation = restaurant.getLocation();
                bindRestaurantViewHolder();
            } else {
                Log.e("Parse error: ", "Unable to find restaurant");
            }
        } else {
            //TODO remove logging
            Log.e("Parse error: ", e.getMessage());
            Toast.makeText(activity, "Unable to process request: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public String getPhoneNumber() {
        if (restaurantHolder != null && restaurant != null) {
            return restaurant.getPhoneNumber();
        }
        return null;
    }

    public String getWebsite() {
        if (restaurantHolder != null && restaurant != null) {
            return restaurant.getWebsite();
        }
        return null;
    }

    public void createViewHolders() {
        restaurantHolder = new RestaurantViewHolder(activity, this);
        bindRestaurantViewHolder();
    }

    public void bindRestaurantViewHolder() {
        if (restaurantHolder != null && restaurant != null) {
            activity.setTitle(restaurant.getName());

            restaurantHolder.address.setText(restaurant.getAddressLine());

            parseLocation = restaurant.getLocation();
            if (parseLocation != null) {
                LatLng deviceLoc = LocationService.pollDeviceLocation(activity);
                restaurantHolder.proximity.setText(restaurant.getDistanceFromString(new ParseGeoPoint(deviceLoc.latitude,deviceLoc.longitude)));

                LatLng resLoc = new LatLng(parseLocation.getLatitude(), parseLocation.getLongitude());
                restaurantHolder.updateMap(resLoc, restaurant.getName());
            }
        }
    }
}
