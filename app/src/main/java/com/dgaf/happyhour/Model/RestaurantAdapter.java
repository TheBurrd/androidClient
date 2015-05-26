package com.dgaf.happyhour.Model;

import android.app.Activity;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Adam on 5/24/2015.
 */
public class RestaurantAdapter {
    private Activity activity;

    public RestaurantAdapter(Activity activity) {
        this.activity = activity;
    }

    public void loadRestaurantDetails(String restaurantId) {
        ParseQuery<RestaurantModel> restaurantQuery = ParseQuery.getQuery(RestaurantModel.class);
        restaurantQuery.whereEqualTo("id", restaurantId);
        restaurantQuery.include("deals");
        restaurantQuery.findInBackground(new FindCallback<RestaurantModel>() {
            public void done(List<RestaurantModel> restaurant, ParseException e) {
                if (e == null) {
                    // Do stuff
                } else {
                    Log.e("Parse error: ", e.getMessage());
                }
            }
        });
    }
}
