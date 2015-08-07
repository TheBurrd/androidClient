package com.dgaf.happyhour.Model.Queries;

import com.dgaf.happyhour.Model.DayOfWeekMask;
import com.parse.Parse;
import com.parse.ParseGeoPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 5/30/2015.
 */
public class QueryParameters {
    // Today's deals show all deals from now till TODAY_TIME_RANGE/100 hours in the future
    public static final int TODAY_TIME_RANGE = 800;
    private static QueryParameters ourInstance = new QueryParameters();
    private QueryType mQueryType;
    private DayOfWeekMask mDayOfWeekMask;
    private int mRadiusMi;
    private List<Listener> listeners = new ArrayList<>();

    public interface Listener {
        void onUpdate();
    }

    public enum QueryType {
        RATING,
        PROXIMITY
    }

    public static QueryParameters getInstance() {
        return ourInstance;
    }

    private QueryParameters() {
        // Default Query
        mQueryType = QueryType.RATING;
        mDayOfWeekMask = new DayOfWeekMask();
        mDayOfWeekMask.selectToday();
        mRadiusMi = 5;
    }

    public void notifyAllListeners() {
        for (Listener listener : listeners) {
            listener.onUpdate();
        }
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void detachAllListeners() {
        listeners.clear();
    }

    public QueryType getQueryType() {
        return mQueryType;
    }

    public void setQueryType(QueryType mQueryType) {
        this.mQueryType = mQueryType;
    }

    public int getRadiusMi() {
        return mRadiusMi;
    }

    public void setRadiusMi(int radius) {
        this.mRadiusMi = radius;
    }

    public DayOfWeekMask getDayOfWeekMask() {
        return mDayOfWeekMask;
    }

    public ParseGeoPoint getLocation() {
        return new ParseGeoPoint();
    }


}