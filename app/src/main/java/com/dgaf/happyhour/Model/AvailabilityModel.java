package com.dgaf.happyhour.Model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Adam on 5/12/2015.
 */
public class AvailabilityModel {
    public DayOfWeekMask recurrenceMask1;
    public short firstOpen1;
    public short lastOpen1;
    public short firstClose1;
    public short lastClose1;

    public DayOfWeekMask recurrenceMask2;
    public short firstOpen2;
    public short lastOpen2;
    public short firstClose2;
    public short lastClose2;


    public static final short RECUR_DAYOFWEEK_ST = 47;
    public static final int FIRST_MASK = 0x000111;
    public static final int LAST_MASK = 0x111000;


    public AvailabilityModel(DealModel deal) {
        String recur1 = deal.getRecurrence1();
        String recur2 = deal.getRecurrence2();

        this.recurrenceMask1 = new DayOfWeekMask((byte)Integer.parseInt(recur1.substring(RECUR_DAYOFWEEK_ST) + "0", 2));
        this.recurrenceMask2 = new DayOfWeekMask((byte)Integer.parseInt(recur2.substring(RECUR_DAYOFWEEK_ST) + "0", 2));

        int openTime1 = (int)deal.getOpenTime1();
        this.firstOpen1 = (short)(openTime1 & FIRST_MASK);
        this.lastOpen1 = (short)((openTime1 & LAST_MASK) >> 12);

        int closeTime1 = (int)deal.getCloseTime1();
        this.firstClose1 = (short)(closeTime1 & FIRST_MASK);
        this.lastClose1 = (short)((closeTime1 & LAST_MASK) >> 12);

        int openTime2 = (int)deal.getOpenTime2();
        this.firstOpen2 = (short)(openTime2 & FIRST_MASK);
        this.lastOpen2 = (short)((openTime2 & LAST_MASK) >> 12);

        int closeTime2 = (int)deal.getCloseTime2();
        this.firstClose2 = (short)(closeTime2 & FIRST_MASK);
        this.lastClose2 = (short)((closeTime2 & LAST_MASK) >> 12);
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
    public String getDayAvailability(byte weekday, boolean displayDay) {
        StringBuilder ret = new StringBuilder();

        // Mon
        if (displayDay) {
            ret.append(getDayShorthand(weekday));
        }

        int startTime = firstOpen1;
        int endTime = lastClose1;

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

    /** Return a shorthand string for a given weekday */
    private String getDayShorthand(byte weekday) {
        switch (weekday) {
            case DayOfWeekMask.MONDAY:
                return "Mon ";
                //break;

            case DayOfWeekMask.TUESDAY:
                return "Tue ";
                //break;

            case DayOfWeekMask.WEDNESDAY:
                return "Wed ";
                //break;

            case DayOfWeekMask.THURSDAY:
                return "Thu ";
                //break;

            case DayOfWeekMask.FRIDAY:
                return "Fri ";
                //break;

            case DayOfWeekMask.SATURDAY:
                return "Sat ";
                //break;

            case DayOfWeekMask.SUNDAY:
                return "Sun ";
                //break;
        }

        return null;
    }

}
