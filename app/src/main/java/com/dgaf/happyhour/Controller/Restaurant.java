package com.dgaf.happyhour.Controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.dgaf.happyhour.Adapter.RestaurantAdapter;
import com.dgaf.happyhour.Adapter.RestaurantDealAdapter;
import com.dgaf.happyhour.Model.Queries.Parse.DealQuery;
import com.dgaf.happyhour.Model.Queries.Parse.RestaurantQuery;
import com.dgaf.happyhour.Model.Queries.QueryParameters;
import com.dgaf.happyhour.R;

public class Restaurant extends AppCompatActivity {

    private static final String RESTAURANT_ID = "resId";
    private static final String DEAL_ID = "dealId";
    private Toolbar toolbar;
    private RestaurantAdapter restaurantAdapter;
    private RestaurantDealAdapter dealAdapter;
    private RestaurantQuery mRestaurantQuery;
    private DealQuery mDealQuery;
    private QueryParameters mQueryParams;

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

        mQueryParams = QueryParameters.getInstance();

        mDealQuery = new DealQuery(dealArg);
        dealAdapter = new RestaurantDealAdapter(this, mQueryParams);
        mDealQuery.fetch(this, dealAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restaurant, menu);
        return true;
    }

}