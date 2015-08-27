package com.dgaf.happyhour.Model.Queries.Parse;

import android.content.Context;
import android.util.Log;

import com.dgaf.happyhour.Model.DealModel;
import com.dgaf.happyhour.Model.ModelUpdater;
import com.dgaf.happyhour.Model.Queries.Query;
import com.dgaf.happyhour.Model.Queries.QueryParameters;
import com.dgaf.happyhour.Model.RestaurantModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Adam on 8/6/2015.
 */
public class FeaturedDealListQuery implements Query<DealModel> {
    QueryParameters mParams;
    public FeaturedDealListQuery(QueryParameters params) {
        mParams = params;
    }
    public void fetch(Context context, final ModelUpdater<DealModel> modelUpdater) {
        // Setup the database Query
        ParseQuery<RestaurantModel> localRestaurants = ParseQuery.getQuery(RestaurantModel.class);
        localRestaurants.whereWithinMiles("location", mParams.getLocation(context), mParams.getRadiusMi());

        ParseQuery<DealModel> localDeals = ParseQuery.getQuery(DealModel.class);
        ParseQuery<DealModel> orLocalDeals = ParseQuery.getQuery(DealModel.class);
        localDeals.whereMatchesQuery("restaurantId", localRestaurants);
        orLocalDeals.whereMatchesQuery("restaurantId", localRestaurants);
        localDeals.whereEqualTo("tags", "featured");
        orLocalDeals.whereEqualTo("tags", "featured");

        //localDeals = applyDayOfWeekForQuery(localDeals, orLocalDeals);

        localDeals.include("restaurantId");
        //TODO remove logging
        Log.v("Parse info", "Deal list query started");

        localDeals.findInBackground(new FindCallback<DealModel>() {
            @Override
            public void done(List<DealModel> list, ParseException e) {
                modelUpdater.onDataModelUpdate(list, e);
            }
        });
    }
}
