package com.dgaf.happyhour.Model;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

/**
 * Created by Adam on 5/12/2015.
 */
@ParseClassName("restaurants")
public class RestaurantModel extends ParseObject implements AvailabilityModel.Provider {

    public String getId() {
        return getObjectId();
    }

    public String getName() {
        return getString("name");
    }

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

    public ParseGeoPoint getLocation() { return getParseGeoPoint("location"); }

    public String getWebsite() {
        return getString("website");
    }

    public double getDistanceFrom(ParseGeoPoint location) {
        ParseGeoPoint restaurant = getParseGeoPoint("location");

        if (restaurant == null) {
            return 0.0;
        }
        return location.distanceInMilesTo(restaurant);
    }

    public String getDistanceFromString(ParseGeoPoint location) {
        return String.format("%.1f", getDistanceFrom(location)) + " mi";
    }

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

    public String getAddressLine() {
        return getStreetNumber() + " " + getStreetAddress() + ", " + getCity() + ", " + getState()+ ", " + getZipcode();
    }
}
