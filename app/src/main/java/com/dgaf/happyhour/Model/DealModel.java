package com.dgaf.happyhour.Model;

/**
 * Created by trentonrobison on 5/9/15.
 */
public class DealModel {

    private String id;
    private int upVotes;
    private int downVotes;
    private int flags;
    private int longitude;
    private int latitude;
    private String title;
    private String description;
    private String restaurantID;
    private String pictureURL;
    private String [] tag;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String[] getTag() {
        return tag;
    }

    public void setTag(String[] tag) {
        this.tag = tag;
    }

    public DealModel(String id,int upVotes,int downVotes, int flags, int longitude,
                     int latitude,String title, String description, String restaurantID,
                     String pictureURL,String [] tag){
        this.id = id;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.flags = flags;
        this.longitude = longitude;
        this.latitude = latitude;
        this.title = title;
        this.description = description;
        this.restaurantID = restaurantID;
        this.pictureURL = pictureURL;
        this.tag = tag;



    }


}