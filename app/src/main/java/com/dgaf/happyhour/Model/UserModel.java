package com.dgaf.happyhour.Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Collections;
import java.util.List;

/**
 * Created by Adam on 8/13/2015.
 */
@ParseClassName("User")
public class UserModel extends ParseObject {
    public boolean isDealUpVoted(String dealId) {
        List<String> upVotes = getList("upVotes");
        Collections.sort(upVotes);
        return Collections.binarySearch(upVotes, dealId) > 0;
    }

    public boolean isDealDownVoted(String dealId) {
        List<String> upVotes = getList("downVotes");
        Collections.sort(upVotes);
        return Collections.binarySearch(upVotes, dealId) > 0;
    }
}
