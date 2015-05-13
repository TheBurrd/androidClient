package com.dgaf.happyhour.Model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dgaf.happyhour.DealListType;
import com.dgaf.happyhour.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trentonrobison on 4/28/15.
 */
public class DealListAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<DealModel> dealItems;
    //ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public DealListAdapter(Activity activity, DealListType listType) {
        this.activity = activity;
        dealItems = new ArrayList<DealModel>();

        // Get users GPS coords
        double radiusMi = 5.0;
        ParseGeoPoint location = new ParseGeoPoint(32.000,-117.0000);

        switch(listType) {
            case DRINK:
                loadLocalDeals(location, radiusMi);
                break;
            case FOOD:
                loadLocalDeals(location, radiusMi);
                break;
            case FEATURED:
                loadLocalDeals(location, radiusMi);
                break;
        }
    }

    public void loadLocalDeals(ParseGeoPoint location, double radiusMi) {
        // Setup the database Query
        ParseQuery<RestaurantModel> localRestaurants = ParseQuery.getQuery(RestaurantModel.class);
        localRestaurants.whereWithinMiles("location", location, radiusMi);
        localRestaurants.include("deals");

        final DealListAdapter listAdapter = this;
        localRestaurants.findInBackground(new FindCallback<RestaurantModel>() {
            public void done(List<RestaurantModel> restaurants, ParseException e) {
                if (e == null) {
                    dealItems = new ArrayList<DealModel>();
                    for (RestaurantModel res : restaurants) {
                        List<DealModel> deals = res.getList("deals");
                        dealItems.addAll(deals);
                    }
                    listAdapter.notifyDataSetChanged();
                } else {
                    Log.e("Parse error: ", e.getMessage());
                }
            }
        });
    }

    @Override
    public int getCount() {
        return dealItems.size();
    }

    @Override
    public Object getItem(int location) {
        return dealItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.deal_list_item, null);

       /* if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumb_nal);
*/
        TextView deal = (TextView) convertView.findViewById(R.id.deal);
        TextView description = (TextView) convertView.findViewById(R.id.description);
        TextView distance = (TextView) convertView.findViewById(R.id.distance);
        TextView restaurant = (TextView) convertView.findViewById(R.id.restaurant);
        TextView likes = (TextView) convertView.findViewById(R.id.likes);
        TextView hours = (TextView) convertView.findViewById(R.id.hours);



        // getting movie data for the row
        DealModel dealModel = dealItems.get(position);

        // thumbnail image
        //thumbNail.setImageUrl(dealModel.getPictureURL(), imageLoader);

        // Set Deal
        deal.setText(dealModel.getTitle());

        // Set Likes
        likes.setText("Rating: " + String.valueOf(dealModel.getUpVotes()));

        description.setText(dealModel.getDescription());

        //restaurant.setText(dealModel.getRestaurantID());

        hours.setText("");
        //do something with theses
        //distance.setText(dealModel.getLongitude() + dealModel.getLatitude());


        return convertView;
    }
}