package com.dgaf.happyhour.Controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dgaf.happyhour.Model.DealIcon;
import com.dgaf.happyhour.Model.DealModel;
import com.dgaf.happyhour.Model.RestaurantModel;
import com.dgaf.happyhour.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;


public class Restaurant extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String RESTAURANT_ID = "resId";
    private static final String DEAL_ID = "dealId";
    private Toolbar toolbar;
    private RestaurantModel restaurantModel;
    private DealModel dealModel;
    private ParseGeoPoint parseLocation;

    private TextView dealTitle;
    private TextView dealAvailability;
    private TextView dealFineprint;
    private TextView dealRating;
    private ToggleButton upVote;
    private ToggleButton downVote;
    private ImageView categoryIcon;
    private ImageView dialPhone;
    private ImageView visitWebsite;
    private TextView proximity;
    //private TextView hoursOfOperation;
    private TextView address;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        //add arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        View rootView = findViewById(android.R.id.content);//gets root view
        String restaurantArg = getIntent().getExtras().getString(RESTAURANT_ID);
        String dealArg = getIntent().getExtras().getString(DEAL_ID);

        parseLocation = getLocation();

        categoryIcon = (ImageView) findViewById(R.id.icon);
        dealTitle = (TextView) findViewById(R.id.deal_title);
        dealAvailability = (TextView) findViewById(R.id.deal_availability);
        dealFineprint = (TextView) findViewById(R.id.deal_fineprint);
        proximity = (TextView) findViewById(R.id.distance);
        dealRating = (TextView) findViewById(R.id.rating);
        dialPhone = (ImageView) findViewById(R.id.phone);
        visitWebsite = (ImageView) findViewById(R.id.website);
        upVote = (ToggleButton) findViewById(R.id.upVote);
        downVote = (ToggleButton) findViewById(R.id.downVote);
        address = (TextView) findViewById(R.id.address);
        //map = (GoogleMap) findViewById(R.id.map);

        //map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        //map.getUiSettings().setScrollGesturesEnabled(false);

        loadRestaurantDetails(restaurantArg, dealArg);

        //load a phone number
        dialPhone.setOnClickListener(this);
        visitWebsite.setOnClickListener(this);
        upVote.setOnCheckedChangeListener(this);
        downVote.setOnCheckedChangeListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restaurant, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == dialPhone && restaurantModel != null) {
            Uri number = Uri.parse("tel:" + restaurantModel.getPhoneNumber());
            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
            startActivity(callIntent);
        } else if (v ==  visitWebsite && restaurantModel != null) {
            String website = restaurantModel.getWebsite();
            if (!website.startsWith("http://") && !website.startsWith("https://")) {
                website = "http://" + website;
            }
            Uri webaddress = Uri.parse(website);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, webaddress);
            startActivity(browserIntent);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (compoundButton == upVote) {
            if(isChecked){
                upVote.setBackgroundResource(R.drawable.ic_thumb_up_selected);
                downVote.setChecked(false);
            }else{
                upVote.setBackgroundResource(R.drawable.ic_thumb_up);
            }
        } else if (compoundButton == downVote) {
            if (isChecked) {
                downVote.setBackgroundResource(R.drawable.ic_thumb_down_selected);
                upVote.setChecked(false);
            }else{
                downVote.setBackgroundResource(R.drawable.ic_thumb_down);
            }
        }

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

    public ParseGeoPoint getLocation() {
        // Geisel Library - Default Location
        double latitude = 32.881122;
        double longitude = -117.237631;
        if (!Build.FINGERPRINT.startsWith("generic")) {
            LocationService userLocation = new LocationService(this);
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
        //TODO remove logging
        Log.v("Parse info:", "Started restaurant query");
        restaurantQuery.getInBackground(restaurantId, new GetCallback<RestaurantModel>() {
            public void done(RestaurantModel res, ParseException e) {
                //TODO remove logging
                Log.v("Parse info:", "Restaurant query returned");
                if (e == null) {
                    restaurantModel = res;
                    onBindView();
                } else {
                    //TODO remove logging
                    Log.e("Parse error: ", e.getMessage());
                }
            }
        });
        ParseQuery<DealModel> dealQuery = ParseQuery.getQuery(DealModel.class);
        dealQuery.fromLocalDatastore();
        //TODO remove logging
        Log.v("Parse info:", "Started deal query");
        dealQuery.getInBackground(dealId, new GetCallback<DealModel>() {
            public void done(DealModel returnedDeal, ParseException e) {
                //TODO remove logging
                Log.v("Parse info:", "Deal query returned");
                if (e == null) {
                    dealModel = returnedDeal;
                    onBindView();
                } else {
                    //TODO remove logging
                    Log.e("Parse error: ", e.getMessage());
                }
            }
        });

    }


    public void onBindView() {
        if ( restaurantModel != null && dealModel != null) {
            setTitle(restaurantModel.getName());

            ParseGeoPoint parsePoint = restaurantModel.getLocation();
/*
            if (parsePoint != null) {
                updateMap(new LatLng(parsePoint.getLatitude(), parsePoint.getLongitude()), restaurantModel.getName());
            } else {
                // Defaults to Geisel library if a restaurant hasn't loaded yet.
                updateMap(new LatLng(32.881122, -117.237631), "UCSD - Geisel Library");
            }
*/
            dealTitle.setText(dealModel.getDealTitle());
            // Comment this out till all deals in database have new availability model
            // AvailabilityModel availability = new AvailabilityModel(dealModel);
            // dealAvailability.setText(availability.getDayAvailability(DayOfWeekMask.TODAY, true));
            dealAvailability.setText("Mon: 10a - 8p");
            dealFineprint.setText(dealModel.getFineprint());
            dealRating.setText(dealModel.getRatingString());
            proximity.setText(dealModel.getDistanceFromString(parseLocation));
            address.setText(restaurantModel.getAddressLine());

            DealIcon.setImageToDealCategory(categoryIcon, dealModel);

        }
    }

}