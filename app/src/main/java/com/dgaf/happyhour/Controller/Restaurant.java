package com.dgaf.happyhour.Controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dgaf.happyhour.Adapter.RestaurantAdapter;
import com.dgaf.happyhour.R;


public class Restaurant extends AppCompatActivity {

    private RestaurantAdapter mAdapter;
    private static final String RESTAURANT_ID = "resId";
    private static final String DEAL_ID = "dealId";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

        }

        //add arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        View rootView = findViewById(android.R.id.content);//gets root view
        String restaurantArg = getIntent().getExtras().getString(RESTAURANT_ID);
        String aboutArg = getIntent().getExtras().getString(DEAL_ID);

        if (mAdapter == null) {
            mAdapter = new RestaurantAdapter(this, rootView, restaurantArg, aboutArg);
        } else {
            mAdapter.loadRestaurantDetails(restaurantArg, aboutArg);
            mAdapter.createViewHolders(rootView);
        }

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
}
