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
    private byte mDayOfWeekMask;
    private int mRadiusMi;
    private List<Listener> listeners = new ArrayList<>();

    public interface Listener {
        public void onUpdate();
    }

    public enum QueryType {
        RATING,
        PROXIMITY
    }

    public static final byte SUNDAY     = (byte) 0x80;
    public static final byte MONDAY     = (byte) 0x40;
    public static final byte TUESDAY    = (byte) 0x20;
    public static final byte WEDNESDAY  = (byte) 0x10;
    public static final byte THURSDAY   = (byte) 0x08;
    public static final byte FRIDAY     = (byte) 0x04;
    public static final byte SATURDAY   = (byte) 0x02;
    public static final byte TODAY      = (byte) 0x01;

    public static QueryParameters getInstance() {
        return ourInstance;
    }

    private QueryParameters() {
        // Default Query
        mQueryType = QueryType.RATING;
        mDayOfWeekMask = TODAY;
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

    public byte getDayOfWeekMask() {
        return mDayOfWeekMask;
    }

    public void setDayOfWeekMask(byte dayOfWeekMask) {
        this.mDayOfWeekMask = dayOfWeekMask;
    }

    public int getRadiusMi() {
        return mRadiusMi;
    }

    public void setRadiusMi(int mRadiusMi) {
        this.mRadiusMi = mRadiusMi;
    }

}