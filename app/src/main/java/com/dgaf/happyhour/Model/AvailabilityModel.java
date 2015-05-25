package com.dgaf.happyhour.Model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 5/12/2015.
 */
public class AvailabilityModel {
    private int sundayStart;
    private int sundayEnd;
    private int mondayStart;
    private int mondayEnd;
    private int tuesdayStart;
    private int tuesdayEnd;
    private int wednesdayStart;
    private int wednesdayEnd;
    private int thursdayStart;
    private int thursdayEnd;
    private int fridayStart;
    private int fridayEnd;
    private int saturdayStart;
    private int saturdayEnd;

    public AvailabilityModel(JSONObject avail) {
        try {
            sundayStart = avail.getInt("sundaySt");
            sundayEnd = avail.getInt("sundayEn");
            mondayStart = avail.getInt("mondaySt");
            mondayEnd = avail.getInt("mondayEn");
            tuesdayStart = avail.getInt("tuesdaySt");
            tuesdayEnd = avail.getInt("tuesdayEn");
            wednesdayStart = avail.getInt("wednesdaySt");
            wednesdayEnd = avail.getInt("wednesdayEn");
            thursdayStart = avail.getInt("thursdaySt");
            thursdayEnd = avail.getInt("thursdayEn");
            fridayStart = avail.getInt("fridaySt");
            fridayEnd = avail.getInt("fridayEn");
            saturdayStart = avail.getInt("saturdaySt");
            saturdayEnd = avail.getInt("saturdayEn");
        } catch(JSONException e) {
            Log.e("Parse error: ", "Invalid availability JSONObject");
        }
    }
}
