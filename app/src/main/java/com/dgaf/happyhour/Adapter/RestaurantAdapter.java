package com.dgaf.happyhour.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dgaf.happyhour.Controller.LocationService;
import com.dgaf.happyhour.Model.AvailabilityModel;
import com.dgaf.happyhour.Model.DealModel;
import com.dgaf.happyhour.Model.RestaurantModel;
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


/**
 * Created by Adam on 5/24/2015.
 */
public class RestaurantAdapter {
    private Fragment fragment;
    private ImageLoader imageLoader;
    private RestaurantModel restaurant;
    private DealModel deal;
    private ParseGeoPoint parseLocation;
    private RestaurantViewHolder restaurantHolder;
    private ExpandDealListAdapter expandDealListAdapter;

    public static class RestaurantViewHolder implements ListView.OnTouchListener, View.OnClickListener {
        public TextView dealTitle;
        public TextView dealDescription;
        public TextView dealAvailability;
        public TextView dealRating;
        public ImageButton upVote;
        public ImageButton downVote;
        public TextView restaurantName;
        public TextView restaurantDescription;
        public ImageView dialPhone;
        public ImageView visitWebsite;
        public TextView proximity;
        public TextView hoursOfOperation;
        public TextView address;
        public ExpandableListView dealList;
        public ParseImageView thumbnail;
        public GoogleMap map;

        public Fragment fragment;
        public RestaurantAdapter parentAdapter;

        public RestaurantViewHolder(Fragment fragment, View rootView, RestaurantAdapter parentAdapter,ExpandDealListAdapter listAdapter) {
            this.fragment = fragment;
            this.parentAdapter = parentAdapter;
            dealTitle = (TextView) rootView.findViewById(R.id.deal_title);
            dealDescription = (TextView) rootView.findViewById(R.id.deal_description);
            dealAvailability = (TextView) rootView.findViewById(R.id.current_deal_avail);
            dealRating = (TextView) rootView.findViewById(R.id.deal_rating);
            upVote = (ImageButton) rootView.findViewById(R.id.upVote);
            downVote = (ImageButton) rootView.findViewById(R.id.downVote);
            restaurantName = (TextView) rootView.findViewById(R.id.restaurant_name);
            restaurantDescription = (TextView) rootView.findViewById(R.id.restaurant_description);
            dialPhone = (ImageView) rootView.findViewById(R.id.phoneButton);
            visitWebsite = (ImageView) rootView.findViewById(R.id.websiteButton);
            proximity = (TextView) rootView.findViewById(R.id.proximity);
            hoursOfOperation = (TextView) rootView.findViewById(R.id.hours_of_operation);
            address = (TextView) rootView.findViewById(R.id.address);
            dealList = (ExpandableListView) rootView.findViewById(R.id.deal_list);
            dealList.setFocusable(false);
            dealList.setAdapter(listAdapter);
            thumbnail = (ParseImageView) rootView.findViewById(R.id.picture);
            thumbnail.setPlaceholder(ContextCompat.getDrawable(rootView.getContext(), R.drawable.ic_food_placeholder));
            map = ((SupportMapFragment) fragment.getChildFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            map.getUiSettings().setScrollGesturesEnabled(false);

            upVote.setOnClickListener(this);
            downVote.setOnClickListener(this);
            dialPhone.setOnClickListener(this);
            visitWebsite.setOnClickListener(this);
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

        @Override
        public void onClick(View v) {
            if (v == dialPhone) {
                String phoneNumber = parentAdapter.getPhoneNumber();
                if (phoneNumber != null) {
                    Uri number = Uri.parse("tel:" + parentAdapter.getPhoneNumber());
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                    fragment.startActivity(callIntent);
                }
            } else if (v ==  visitWebsite) {
                String website = parentAdapter.getWebsite();
                if (website != null) {
                    if (!website.startsWith("http://") && !website.startsWith("https://"))
                        website = "http://" + website;
                    Uri webaddress = Uri.parse(website);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, webaddress);
                    fragment.startActivity(browserIntent);
                }
            } else if (v == upVote) {
                //Set the button's appearance
                v.setSelected(!v.isSelected());

                if (v.isSelected()) {
                    //Handle upVote
                    if (downVote.isSelected()) {
                        downVote.setSelected(false);
                    }
                } else {
                    //Handle undo downVote
                }
            } else if (v == downVote) {
                //Set the button's appearance
                v.setSelected(!v.isSelected());

                if (v.isSelected()) {
                    //Handle downVote
                    if (upVote.isSelected()) {
                        upVote.setSelected(false);
                    }
                } else {
                    //Handle undo downVote
                }
            }
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
        parseLocation = getLocation();
        createViewHolders(fragmentView);
        loadRestaurantDetails(restaurantId, dealId);
    }

    public ParseGeoPoint getLocation() {
        // Geisel Library - Default Location
        double latitude = 32.881122;
        double longitude = -117.237631;
        if (!Build.FINGERPRINT.startsWith("generic")) {
            LocationService userLocation = new LocationService(fragment.getActivity());
            // Is user location available and are we not running in an emulator
            if (userLocation.canGetLocation()) {
                latitude = userLocation.getLatitude();
                longitude = userLocation.getLongitude();
            } else {
                userLocation.showSettingsAlert();
            }
        }
        return new ParseGeoPoint(latitude,longitude);
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

    public void createViewHolders(View fragmentView) {
        restaurantHolder = new RestaurantViewHolder(fragment, fragmentView, this, expandDealListAdapter);
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
            restaurantHolder.dealAvailability.setText(deal.getAvailability().getDayAvailability(AvailabilityModel.getDayOfWeek(), true));
            restaurantHolder.dealTitle.setText(deal.getTitle());
            restaurantHolder.dealDescription.setText(deal.getDescription());
            restaurantHolder.dealRating.setText(String.valueOf(deal.getRating()) + "%");
            restaurantHolder.proximity.setText(String.format("%.1f", restaurant.getDistanceFrom(parseLocation)) + " mi");
            restaurantHolder.hoursOfOperation.setText(restaurant.getAvailability().getEntireAvailability());
            restaurantHolder.address.setText(restaurant.getStreetNumber() + " " + restaurant.getStreetAddress() + ", " + restaurant.getCity() + ", " + restaurant.getState()+ ", " + restaurant.getZipcode());

        }
    }
}
