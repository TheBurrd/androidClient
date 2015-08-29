package com.dgaf.happyhour.Model;

/**
 * Created by Adam on 5/12/2015.
 */
public class AvailabilityModel {
    public DayOfWeekMask recurrenceMask;
    public int firstOpen;
    public int lastOpen;
    public int firstClose;
    public int lastClose;

    public interface Provider {
        String getRecurrence(int index);

        int getFirstOpenTime(int index);

        int getLastOpenTime(int index);

        int getFirstCloseTime(int index);

        int getLastCloseTime(int index);
    }

    public static final short RECUR_DAYOFWEEK_START = 48;

    public AvailabilityModel(Provider prov, int index) {
        String recur = prov.getRecurrence(index);
        if (recur == null) {
            this.recurrenceMask = new DayOfWeekMask((byte)0);

        } else {
            this.recurrenceMask = new DayOfWeekMask((byte)Integer.parseInt(recur.substring(RECUR_DAYOFWEEK_START) + "0", 2));
            byte today = DayOfWeekMask.getCurrentDayOfWeekAsMask();
            if (this.recurrenceMask.isDaySelected(today)) {
                this.recurrenceMask.selectToday();
            }
        }

        this.firstOpen = prov.getFirstOpenTime(index);
        this.lastOpen = prov.getLastOpenTime(index);

        this.firstClose = prov.getFirstCloseTime(index);
        this.lastClose = prov.getLastCloseTime(index);

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
    // TODO display split open times
    public String getDayAvailability(byte weekday, boolean displayDay) {
        StringBuilder ret = new StringBuilder();
        DayOfWeekMask mask = new DayOfWeekMask(weekday);

        if (mask.isTodaySelected()) {
            byte today = DayOfWeekMask.getCurrentDayOfWeekAsMask();
            mask.selectDay(today);
        }

        // Mon
        if (displayDay) {
            ret.append(getDayShorthand(mask));
        }

        int startTime = firstOpen;
        int endTime = lastClose;

        // If weekday does not occur in recurrence, return no deal for day
        if (!recurrenceMask.isDaySelected(weekday)) {
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

        if (endHour > 24) {
            endHour -= 24;
        } else if (endHour > 12) {
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
        else if (hour <= 35){
            return "a";
        }
        else {
            return "p";
        }
    }

    /** Return a shorthand string for a given weekday */
    private String getDayShorthand(DayOfWeekMask mask) {
        String dayShort = "";
        if (mask.isSundaySelected()) {
            dayShort += "Sun,";
        }
        if (mask.isMondaySelected()) {
            dayShort += "Mon,";
        }
        if (mask.isTuesdaySelected()) {
            dayShort += "Tue,";
        }
        if (mask.isWednesdaySelected()) {
            dayShort += "Wed,";
        }
        if (mask.isThursdaySelected()) {
            dayShort += "Thu,";
        }
        if (mask.isFridaySelected()) {
            dayShort += "Fri,";
        }
        if (mask.isSaturdaySelected()) {
            dayShort += "Sat,";
        }
        if (dayShort.length() > 0) {
            dayShort = dayShort.substring(0, dayShort.length() - 1) + " ";
        }
        return dayShort;
    }
}
