package com.dgaf.happyhour.Model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

/**
 * Created by trentonrobison on 5/9/15.
 */
@ParseClassName("deals")
public class DealModel extends ParseObject implements AvailabilityModel.Provider {


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

    @Override
    public String getRecurrence(int index) { return getString("recurrence" + index);}

    @Override
    public int getFirstOpenTime(int index) { return getInt("firstOpenTime" + index);}

    @Override
    public int getLastOpenTime(int index) { return getInt("lastOpenTime" + index);}

    @Override
    public int getFirstCloseTime(int index) { return getInt("firstCloseTime" + index); }

    @Override
    public int getLastCloseTime(int index) { return getInt("lastCloseTime" + index); }

    public String getRestaurantId() { return getParseObject("restaurantId").getObjectId();}

    public double getDistanceFrom(ParseGeoPoint location) { return location.distanceInMilesTo(getParseObject("restaurantId").getParseGeoPoint("location"));
    }

    public String getDealTitle() {
        String value;
        double amountOff = getAmountOff();
        double percentOff = getPercentOff();
        double reducedPrice = getReducedPrice();
        if (amountOff != 0) {
            if (amountOff == (long) amountOff) {
                value = String.format("%d", (long) amountOff);
            } else {
                value = String.format("%.2f", amountOff);
            }
            value = "$" + value + " off";
        } else if (percentOff != 0) {
            value = String.format("%d", (long)percentOff) + "% off";
        } else {
            if (reducedPrice == (long) reducedPrice) {
                value = String.format("%d", (long) reducedPrice);
            } else {
                value = String.format("%.2f", reducedPrice);
            }
            value = "$" + value;
        }

        String content = "";
        String item = getItem();
        String category = getCategory();
        if (item != null && item.length() > 0) {
            content = item;
        } else if (category != null &&  category.length() > 0) {
            content = category;
        }

        return value + " " + content;
    }

    public String getRatingString() {
        return String.valueOf(getRating()) + "%";
    }

    public String getDistanceFromString(ParseGeoPoint location) {
        return String.format("%.1f", getDistanceFrom(location)) + " mi";
    }

}