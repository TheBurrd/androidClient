package com.dgaf.happyhour.Model;

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
    private WeekDay mWeekDay;
    private int mRadiusMi;
    private List<Listener> listeners = new ArrayList<>();

    public interface Listener {
        public void onUpdate();
    }

    public enum QueryType {
        RATING,
        PROXIMITY
    }

    public enum WeekDay {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        TODAY
    }

    public static QueryParameters getInstance() {
        return ourInstance;
    }

    private QueryParameters() {
        // Default Query
        mQueryType = QueryType.RATING;
        mWeekDay = WeekDay.TODAY;
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

    public WeekDay getWeekDay() {
        return mWeekDay;
    }

    public void setWeekDay(WeekDay mWeekDay) {
        this.mWeekDay = mWeekDay;
    }

    public int getRadiusMi() {
        return mRadiusMi;
    }

    public void setRadiusMi(int mRadiusMi) {
        this.mRadiusMi = mRadiusMi;
    }

}