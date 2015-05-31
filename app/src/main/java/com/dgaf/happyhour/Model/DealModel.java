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

    public String getTitle() {
        return getString("title");
    }

    public String getDescription() {
        return getString("description");
    }

    public String getRestaurant() { return getParseObject("restaurantId").getString("name");}

    public String getRestaurantId() { return getParseObject("restaurantId").getObjectId();}

    public ParseFile getThumbnailFile() { return getParseObject("restaurantId").getParseFile("picture"); }

    public double getDistanceFrom(ParseGeoPoint location) { return location.distanceInMilesTo(getParseObject("restaurantId").getParseGeoPoint("location"));
    }
    public int getMondaySt() {
        return getInt("mondaySt");
    }

    public int getMondayEn() {
        return getInt("mondayEn");
    }

    public int getTuesdaySt() {
        return getInt("tuesdaySt");
    }

    public int getTuesdayEn() {
        return getInt("tuesdayEn");
    }

    public int getWednesdaySt() {
        return getInt("wednesdaySt");
    }

    public int getWednesdayEn() {
        return getInt("wednesdayEn");
    }

    public int getThursdaySt() {
        return getInt("thursdaySt");
    }

    public int getThrusdayEn() {
        return getInt("thursdayEn");
    }

    public int getFridaySt() {
        return getInt("fridaySt");
    }

    public int getFridayEn() {
        return getInt("fridayEn");
    }

    public int getSaturdaySt() {
        return getInt("saturdaySt");
    }

    public int getSaturdayEnd() {
        return getInt("saturdayEn");
    }

    public int getSundaySt() {
        return getInt("sundaySt");
    }

    public int getSundayEn() {
        return getInt("sundayEn");
    }


}