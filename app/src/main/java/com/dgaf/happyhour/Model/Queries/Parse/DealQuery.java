package com.dgaf.happyhour.Model.Queries.Parse;

import android.content.Context;
import android.util.Log;

import com.dgaf.happyhour.Model.DealModel;
import com.dgaf.happyhour.Model.ModelUpdater;
import com.dgaf.happyhour.Model.Queries.Query;
import com.dgaf.happyhour.Model.Queries.QueryParameters;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 8/17/2015.
 */
public class DealQuery implements Query<DealModel> {
    private String dealId;
    public DealQuery(String dealId) {
        this.dealId = dealId;
    }
    public void fetch(Context context, final ModelUpdater<DealModel> modelUpdater) {
        ParseQuery<DealModel> restaurantQuery = ParseQuery.getQuery(DealModel.class);
        restaurantQuery.fromLocalDatastore();
        Log.v("Parse info:", "Started deal query");
        restaurantQuery.getInBackground(dealId, new GetCallback<DealModel>() {
            public void done(DealModel res, ParseException e) {
                Log.v("Parse info:", "Dealquery returned");
                List<DealModel> result = new ArrayList<DealModel>();
                result.add(res);
                modelUpdater.onDataModelUpdate(result, e);
            }
        });
    }

}
