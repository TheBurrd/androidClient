package com.dgaf.happyhour.Model.Queries.Parse;

import android.content.Context;
import android.util.Log;

import com.dgaf.happyhour.Controller.Restaurant;
import com.dgaf.happyhour.Model.DealModel;
import com.dgaf.happyhour.Model.ModelUpdater;
import com.dgaf.happyhour.Model.Queries.Query;
import com.dgaf.happyhour.Model.Queries.QueryParameters;
import com.dgaf.happyhour.Model.RestaurantModel;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

/**
 * Created by Adam on 8/10/2015.
 */
public class RestaurantQuery  implements Query<RestaurantModel> {
    public void fetch(Context context, QueryParameters params, final ModelUpdater<RestaurantModel> modelUpdater) {
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
}
