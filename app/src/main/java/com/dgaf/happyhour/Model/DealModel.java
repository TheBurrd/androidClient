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
        return getString("objectId");
    }

    public int getUpVotes() {
        return getInt("upVotes");
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

    public ParseFile getThumbnailFile() {
        return getParseFile("picture");
    }

    public double getDistanceFrom(ParseGeoPoint location) {
        return location.distanceInMilesTo(getParseGeoPoint("location"));
    }

    public AvailabilityModel getAvailability() {
        return new AvailabilityModel(getJSONObject("availability"));
    }

}