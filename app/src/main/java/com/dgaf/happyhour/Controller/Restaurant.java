package com.dgaf.happyhour.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.dgaf.happyhour.Adapter.RestaurantAdapter;
import com.dgaf.happyhour.Adapter.RestaurantDealAdapter;
import com.dgaf.happyhour.Device;
import com.dgaf.happyhour.Model.DealIcon;
import com.dgaf.happyhour.Model.DealModel;
import com.dgaf.happyhour.Model.ModelUpdater;
import com.dgaf.happyhour.Model.Queries.Parse.DealQuery;
import com.dgaf.happyhour.Model.Queries.Parse.RestaurantQuery;
import com.dgaf.happyhour.Model.Queries.QueryParameters;
import com.dgaf.happyhour.Model.RestaurantModel;
import com.dgaf.happyhour.Model.UserModel;
import com.dgaf.happyhour.R;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Restaurant extends AppCompatActivity {

    private static final String RESTAURANT_ID = "resId";
    private static final String DEAL_ID = "dealId";
    private Toolbar toolbar;
    private RestaurantAdapter restaurantAdapter;
    private RestaurantDealAdapter dealAdapter;
    private RestaurantQuery mRestaurantQuery;
    private DealQuery mDealQuery;

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

        String restaurantArg = getIntent().getExtras().getString(RESTAURANT_ID);
        String dealArg = getIntent().getExtras().getString(DEAL_ID);

        mRestaurantQuery = new RestaurantQuery(restaurantArg);
        restaurantAdapter = new RestaurantAdapter(this);
        mRestaurantQuery.fetch(this, restaurantAdapter);

        mDealQuery = new DealQuery(dealArg);
        dealAdapter = new RestaurantDealAdapter(this);
        mDealQuery.fetch(this, dealAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restaurant, menu);
        return true;
    }

}