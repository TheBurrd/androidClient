package com.dgaf.happyhour.Model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Adam on 5/12/2015.
 * Written by Sherman
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

    public enum WeekDay {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY
    }

    public AvailabilityModel() {

    }

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

    public AvailabilityModel(DealModel avail) {
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
    }

    public static WeekDay getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        return WeekDay.values()[calendar.get(Calendar.DAY_OF_WEEK)-1];
    }

    /** Return a string representing the availability for the input day. Takes
     * a weekday and a boolean, displayDay as a parameter.  If displayDay is true,
     * the weekday will be included in the returned String.  Otherwise, it will
     * not.
     *
     * Example format for Monday 8am-10pm
     * "Mon 8:00a-10:00p"
     *
     * Returns an empty string if there is no deal for that day
     * */
    public String getDayAvailability(WeekDay weekday, boolean displayDay) {
        StringBuilder ret = new StringBuilder();

        // Mon
        if (displayDay) {
            ret.append(getDayShorthand(weekday));
        }

        int startTime = getStartTime(weekday);
        int endTime = getEndTime(weekday);

        // If the startTime is 0 then there is no deal for that day so return an empty string
        if (startTime == 0) {
            return "";
        }

        int startHour = startTime / 100;
        int startMinute = startTime % 100;
        int endHour = endTime / 100;
        int endMinute = endTime % 100;

        // Get "am" or "pm"
        String startPeriod = getPeriod(startHour);
        String endPeriod = getPeriod(endHour);

        // Convert from military time to regular time
        if (startHour > 12) {
            startHour -= 12;
        }

        if (endHour > 12) {
            endHour -= 12;
        }

        // Mon 8:00a-
        ret.append(startHour);
        ret.append(":");

        // Minutes is less than 10
        if (startMinute < 10) {
            ret.append("0");
            ret.append(startMinute);
        }
        else {
            ret.append(startMinute);
        }
        ret.append(startPeriod);

        // Mon 8:00a-10:00p
        ret.append("-");
        ret.append(endHour);
        ret.append(":");

        // Minutes is less than 10
        if (endMinute < 10) {
            ret.append("0");
            ret.append(endMinute);
        }
        else {
            ret.append(endMinute);
        }
        ret.append(endPeriod);

        return ret.toString();
    }

    /** Get the availability for all days by getting the availability for each
     * day consecutively */
    public String getEntireAvailability() {
        StringBuilder ret = new StringBuilder();
        for (WeekDay w : WeekDay.values()) {
            String day = getDayAvailability(w, true);
            // If there is no deal for that day, do no append the string
            if (day.length() > 0) {
                ret.append(day);
                ret.append("\n");
            }
        }
        return ret.toString();
    }

    /** Get either the "am" as "a" or the "pm" as "p" */
    private String getPeriod(int hour) {
        if (hour <= 11) {
            return "a";
        }
        else if (hour <= 23) {
            return "p";
        }
        else {
            return "a";
        }
    }

    /** Helper method to return the start time for the day */
    private int getStartTime(WeekDay weekday) {
        switch (weekday) {
            case MONDAY:
                return mondayStart;
                //break;

            case TUESDAY:
                return tuesdayStart;
                //break;

            case WEDNESDAY:
                return wednesdayStart;
                //break;

            case THURSDAY:
                return thursdayStart;
                //break;

            case FRIDAY:
                return fridayStart;
                //break;

            case SATURDAY:
                return saturdayStart;
                //break;

            case SUNDAY:
                return sundayStart;
                //break;
        }
        return -1;
    }

    /** Helper method to return the end time for the day */
    private int getEndTime(WeekDay weekday) {
        switch (weekday) {
            case MONDAY:
                return mondayEnd;
                //break;

            case TUESDAY:
                return tuesdayEnd;
                //break;

            case WEDNESDAY:
                return wednesdayEnd;
                //break;

            case THURSDAY:
                return thursdayEnd;
                //break;

            case FRIDAY:
                return fridayEnd;
                //break;

            case SATURDAY:
                return saturdayEnd;
                //break;

            case SUNDAY:
                return sundayEnd;
                //break;
        }
        return -1;
    }


    /** Return a shorthand string for a given weekday */
    private String getDayShorthand(WeekDay weekday) {
        switch (weekday) {
            case MONDAY:
                return "Mon ";
                //break;

            case TUESDAY:
                return "Tue ";
                //break;

            case WEDNESDAY:
                return "Wed ";
                //break;

            case THURSDAY:
                return "Thu ";
                //break;

            case FRIDAY:
                return "Fri ";
                //break;

            case SATURDAY:
                return "Sat ";
                //break;

            case SUNDAY:
                return "Sun ";
                //break;
        }

        return null;
    }

}
