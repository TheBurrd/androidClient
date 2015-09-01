package com.dgaf.happyhour.Model.Queries.Parse;

import android.content.Context;
import android.util.Log;

import com.dgaf.happyhour.Model.DayOfWeekMask;
import com.dgaf.happyhour.Model.DealListType;
import com.dgaf.happyhour.Model.DealModel;
import com.dgaf.happyhour.Model.ModelUpdater;
import com.dgaf.happyhour.Model.Queries.Query;
import com.dgaf.happyhour.Model.Queries.QueryParameters;
import com.dgaf.happyhour.Model.RestaurantModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Adam on 8/6/2015.
 */
public class DealListQuery implements Query<DealModel> {
    QueryParameters mParams;
    DealListType mListType;
    public DealListQuery(QueryParameters params, DealListType listType) {
        mParams = params;
        mListType = listType;
    }
    public void fetch(Context context, final ModelUpdater<DealModel> modelUpdater) {
        // Setup the database Query
        ParseQuery<RestaurantModel> localRestaurants = ParseQuery.getQuery(RestaurantModel.class);
        localRestaurants.whereWithinMiles("location", mParams.getLocation(context), mParams.getRadiusMi());

        ParseQuery<DealModel> localDeals = ParseQuery.getQuery(DealModel.class);
        ParseQuery<DealModel> orLocalDeals = ParseQuery.getQuery(DealModel.class);
        localDeals.whereMatchesQuery("restaurantId", localRestaurants);
        orLocalDeals.whereMatchesQuery("restaurantId", localRestaurants);
        localDeals.whereEqualTo("tags", mListType.toString().toLowerCase());
        orLocalDeals.whereEqualTo("tags", mListType.toString().toLowerCase());

        //localDeals = applyDayOfWeekForQuery(localDeals, orLocalDeals);
        // All deals with
        localDeals.whereMatches("recurrence1", getQueryRegexFromDayOfWeekMask(mParams.getDayOfWeekMask()));

        Calendar cal = Calendar.getInstance();
        int now = cal.get(Calendar.HOUR_OF_DAY)*100 + cal.get(Calendar.MINUTE);


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

    private String getQueryRegexFromDayOfWeekMask(DayOfWeekMask dayOfWeekMask) {
        // Regex that skips the first 48 characters of a string
        String dayOfWeekRegex = "^.{48}";
        String dayOfWeekByte = Integer.toBinaryString(dayOfWeekMask.getMask() & 0xFF);
        for (int i = 0; i < dayOfWeekByte.length() - 1; i++) {
            // Append to the regex a search for the particular day of the week
            if (dayOfWeekByte.charAt(i) == '1') {
                char[] anyDay = ".......|".toCharArray();
                anyDay[i] = '1';
                dayOfWeekRegex += String.valueOf(anyDay);
            }
        }
        if (dayOfWeekMask.isTodaySelected()) {
            // Select todays day of the week if the filter is set
            dayOfWeekRegex += Integer.toBinaryString(DayOfWeekMask.getCurrentDayOfWeekAsMask()).replace('0','.') + "|";
        }
        return dayOfWeekRegex.substring(0, dayOfWeekRegex.length() - 1);
    }

}
