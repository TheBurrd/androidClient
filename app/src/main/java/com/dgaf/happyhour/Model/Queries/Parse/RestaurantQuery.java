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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 8/10/2015.
 */
public class RestaurantQuery  implements Query<RestaurantModel> {
    private String restaurantId;
    public RestaurantQuery(String restaurantId) {
        this.restaurantId = restaurantId;
    }
    public void fetch(Context context, QueryParameters params, final ModelUpdater<RestaurantModel> modelUpdater) {
        ParseQuery<RestaurantModel> restaurantQuery = ParseQuery.getQuery(RestaurantModel.class);
        restaurantQuery.fromLocalDatastore();
        Log.v("Parse info:", "Started restaurant query");
        restaurantQuery.getInBackground(restaurantId, new GetCallback<RestaurantModel>() {
            public void done(RestaurantModel res, ParseException e) {
                Log.v("Parse info:", "Restaurant query returned");
                List<RestaurantModel> result = new ArrayList<RestaurantModel>();
                result.add(res);
                modelUpdater.onDataModelUpdate(result, e);
            }
        });
    }
}
