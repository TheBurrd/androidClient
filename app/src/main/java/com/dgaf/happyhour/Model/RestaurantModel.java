package com.dgaf.happyhour.Model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import org.json.JSONObject;

/**
 * Created by Adam on 5/12/2015.
 */
@ParseClassName("restaurants")
public class RestaurantModel extends ParseObject {

    public String getId() {
        return getObjectId();
    }

    public String getName() {
        return getString("name");
    }

    public String getDescription() { return getString("description"); }

    public String getStreetNumber() {
        return getString("streetNumber");
    }

    public String getStreetAddress() {
        return getString("streetAddress");
    }

    public String getCity() {
        return getString("city");
    }

    public String getState() {
        return getString("state");
    }

    public String getZipcode() {
        return getString("zipCode");
    }

    public String getPhoneNumber() {
        return getString("phoneNumber");
    }

    public ParseFile getThumbnailFile() { return getParseFile("picture"); }

    public ParseGeoPoint getLocation() { return getParseGeoPoint("location"); }

    public String getWebsite() {
        return getString("website");
    }

    public AvailabilityModel getAvailability() {
        JSONObject avail = getJSONObject("availability");

        if (avail == null) {
            return new AvailabilityModel();
        }

        return new AvailabilityModel(getJSONObject("availability"));

    }
}
