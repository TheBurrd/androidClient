package com.dgaf.happyhour.Model;

import com.parse.ParseClassName;
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

    public void setUpVotes(int upVotes) {
        put("upVotes", upVotes);
    }

    public int getDownVotes() {
        return getInt("downVotes");
    }

    public void setDownVotes(int downVotes) {
        put("downVotes", downVotes);
    }

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }
}