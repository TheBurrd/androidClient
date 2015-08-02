package com.dgaf.happyhour.Model;

import java.util.Calendar;

/**
 * Created by Adam on 8/2/2015.
 */
public class DayOfWeekMask {
    private byte mDayOfWeekMask;

    public static final byte SUNDAY     = (byte) 0x80;
    public static final byte MONDAY     = (byte) 0x40;
    public static final byte TUESDAY    = (byte) 0x20;
    public static final byte WEDNESDAY  = (byte) 0x10;
    public static final byte THURSDAY   = (byte) 0x08;
    public static final byte FRIDAY     = (byte) 0x04;
    public static final byte SATURDAY   = (byte) 0x02;
    public static final byte TODAY      = (byte) 0x01;

    public DayOfWeekMask() {
        mDayOfWeekMask = 0;
    }

    public DayOfWeekMask(byte dayOfWeekMask) {
        this.mDayOfWeekMask = dayOfWeekMask;
    }

    public static byte getCurrentDayOfWeekAsMask() {
        Calendar calendar = Calendar.getInstance();
        return (byte)(128 >> (calendar.get(Calendar.DAY_OF_WEEK) - 1));
    }

    public byte getDayOfWeekMask() {
        return mDayOfWeekMask;
    }

    public void setDayOfWeekMask(byte dayOfWeekMask) {
        this.mDayOfWeekMask = dayOfWeekMask;
    }

    public boolean isDaySelected(byte dayOfWeek) {
        return ((this.mDayOfWeekMask & dayOfWeek) != 0);
    }

    public byte selectDay(byte dayOfWeek) {
        return this.mDayOfWeekMask = (byte)(this.mDayOfWeekMask | dayOfWeek);
    }

    public byte unselectDay(byte dayOfWeek) {
        return this.mDayOfWeekMask = (byte)(this.mDayOfWeekMask & ~dayOfWeek);
    }

    public byte selectMonday() {
        return this.mDayOfWeekMask = (byte)(this.mDayOfWeekMask | MONDAY);
    }

    public byte selectTuesday() {
        return this.mDayOfWeekMask = (byte)(this.mDayOfWeekMask | TUESDAY);
    }

    public byte selectWednesday() {
        return this.mDayOfWeekMask = (byte)(this.mDayOfWeekMask | WEDNESDAY);
    }

    public byte selectThursday() {
        return this.mDayOfWeekMask = (byte)(this.mDayOfWeekMask | THURSDAY);
    }

    public byte selectFriday() {
        return this.mDayOfWeekMask = (byte)(this.mDayOfWeekMask | FRIDAY);
    }

    public byte selectSaturday() {
        return this.mDayOfWeekMask = (byte)(this.mDayOfWeekMask | SATURDAY);
    }

    public byte selectSunday() {
        return this.mDayOfWeekMask = (byte)(this.mDayOfWeekMask | SUNDAY);
    }

    public byte selectToday() {
        return this.mDayOfWeekMask = (byte)(this.mDayOfWeekMask | TODAY);
    }

    public byte unselectMonday() {
        return this.mDayOfWeekMask = (byte)(this.mDayOfWeekMask & ~MONDAY);
    }

    public byte unselectTuesday() {
        return this.mDayOfWeekMask = (byte)(this.mDayOfWeekMask & ~TUESDAY);
    }

    public byte unselectWednesday() {
        return this.mDayOfWeekMask = (byte)(this.mDayOfWeekMask & ~WEDNESDAY);
    }

    public byte unselectThursday() {
        return this.mDayOfWeekMask = (byte)(this.mDayOfWeekMask & ~THURSDAY);
    }

    public byte unselectFriday() {
        return this.mDayOfWeekMask = (byte)(this.mDayOfWeekMask & ~FRIDAY);
    }

    public byte unselectSaturday() {
        return this.mDayOfWeekMask = (byte)(this.mDayOfWeekMask & ~SATURDAY);
    }

    public byte unselectSunday() {
        return this.mDayOfWeekMask = (byte)(this.mDayOfWeekMask & ~SUNDAY);
    }

    public byte unselectToday() {
        return this.mDayOfWeekMask = (byte)(this.mDayOfWeekMask & ~TODAY);
    }

    public boolean isMondaySelected() {
        return ((this.mDayOfWeekMask & MONDAY) != 0);
    }

    public boolean isTuesdaySelected() {
        return ((this.mDayOfWeekMask & TUESDAY) != 0);
    }

    public boolean isWednesdaySelected() {
        return ((this.mDayOfWeekMask & WEDNESDAY) != 0);
    }

    public boolean isThursdaySelected() {
        return ((this.mDayOfWeekMask & THURSDAY) != 0);
    }

    public boolean isFridaySelected() {
        return ((this.mDayOfWeekMask & FRIDAY) != 0);
    }

    public boolean isSaturdaySelected() {
        return ((this.mDayOfWeekMask & SATURDAY) != 0);
    }

    public boolean isSundaySelected() {
        return ((this.mDayOfWeekMask & SUNDAY) != 0);
    }

    public boolean isTodaySelected() {
        return ((this.mDayOfWeekMask & TODAY) != 0);
    }

}
