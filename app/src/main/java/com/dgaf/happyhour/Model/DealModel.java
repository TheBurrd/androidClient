package com.dgaf.happyhour.Model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

/**
 * Created by trentonrobison on 5/9/15.
 */
@ParseClassName("deals")
public class DealModel extends ParseObject {


    public String getId() {
        return getObjectId();
    }

    public int getUpVotes() {
        return getInt("upVotes");
    }

    public int getRating() {
        return getInt("rating");
    }

    public void addUpVote() {
        increment("upVotes");
    }

    public int getDownVotes() {
        return getInt("downVotes");
    }

    public void addDownVote() {
        increment("downVotes");
    }

    public String getItem() {
        return getString("item");
    }

    public String getCategory() {
        return getString("category");
    }

    public double getAmountOff() {
        return getDouble("amountOff");
    }

    public double getPercentOff() {
        return getDouble("percentOff");
    }

    public String getFineprint() {
        return getString("fineprint");
    }

    public String getPrimaryTag() {
        return getString("primaryTag");
    }

    public double getReducedPrice() {
        return getDouble("reducedPrice");
    }

    public boolean getGlutenFree() {
        return getBoolean("gluttenFree");
    }

    public boolean getVegan() {
        return getBoolean("vegan");
    }

    public boolean getVegetarian() {
        return getBoolean("vegetarian");
    }

    public String getRestaurant() { return getParseObject("restaurantId").getString("name");}

    public String getRecurrence1() { return getString("recurrence1");}

    public long getOpenTime1() { return getLong("openTime1");}

    public long getCloseTime1() { return getLong("closeTime1"); }

    public String getRecurrence2() { return getString("recurrence2");}

    public long getOpenTime2() { return getLong("openTime2");}

    public long getCloseTime2() { return getLong("closeTime2");}

    public String getRestaurantId() { return getParseObject("restaurantId").getObjectId();}

    public double getDistanceFrom(ParseGeoPoint location) { return location.distanceInMilesTo(getParseObject("restaurantId").getParseGeoPoint("location"));
    }

}