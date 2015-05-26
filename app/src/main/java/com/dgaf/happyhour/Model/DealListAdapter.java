package com.dgaf.happyhour.Model;
import com.dgaf.happyhour.Controller.MyLocationListener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.Activity;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dgaf.happyhour.DealListType;
import com.dgaf.happyhour.R;
import com.dgaf.happyhour.View.Restaurant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseImageView;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trentonrobison on 4/28/15.
 */
public class DealListAdapter extends RecyclerView.Adapter<DealListAdapter.ViewHolder>{
    private FragmentActivity activity;
    private ImageLoader imageLoader;
    private List<DealModel> dealItems;
    private ParseGeoPoint parseLocation;
    private MyLocationListener userLocation;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView deal;
        public TextView description;
        public TextView distance;
        public TextView restaurant;
        public TextView likes;
        public TextView hours;
        public ParseImageView thumbnail;
        public FragmentActivity activity;

        public ViewHolder(View itemView,FragmentActivity activity) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.activity = activity;
            deal = (TextView) itemView.findViewById(R.id.deal);
            description = (TextView) itemView.findViewById(R.id.description);
            distance = (TextView) itemView.findViewById(R.id.distance);
            restaurant = (TextView) itemView.findViewById(R.id.restaurant);
            likes = (TextView) itemView.findViewById(R.id.likes);
            hours = (TextView) itemView.findViewById(R.id.hours);
            thumbnail = (ParseImageView) itemView.findViewById(R.id.thumb_nal);
            thumbnail.setPlaceholder(ContextCompat.getDrawable(itemView.getContext(), R.drawable.llama));
        }
        @Override
        public void onClick(View v) {
            Fragment restaurant = new Restaurant();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainfragment, restaurant).addToBackStack(null).commit();
        }

    }

    public DealListAdapter(FragmentActivity activity, DealListType listType) {
        this.activity = activity;
        this.imageLoader = ImageLoader.getInstance();
        dealItems = new ArrayList<>();

        // Geisel Library
        double latitude = 32.881122;
        double longitude = -117.237631;
        if (!Build.FINGERPRINT.startsWith("generic")) {
            userLocation = new MyLocationListener(activity);
            // Is user location available and are we not running in an emulator
            if (userLocation.canGetLocation()) {
                latitude = userLocation.getLatitude();
                longitude = userLocation.getLongitude();
            } else {
                userLocation.showSettingsAlert();
            }
        }

        double radiusMi = 100.0;
        parseLocation = new ParseGeoPoint(latitude,longitude);

        switch(listType) {
            case DRINK:
                loadLocalDeals(parseLocation, radiusMi);
                break;
            case FOOD:
                loadLocalDeals(parseLocation, radiusMi);
                break;
            case FEATURED:
                loadLocalDeals(parseLocation, radiusMi);
                break;
        }
    }

    public void loadLocalDeals(ParseGeoPoint location, double radiusMi) {
        // Setup the database Query
        ParseQuery<RestaurantModel> localRestaurants = ParseQuery.getQuery(RestaurantModel.class);
        localRestaurants.whereWithinMiles("location", location, radiusMi);
        ParseQuery<DealModel> localDeals = ParseQuery.getQuery(DealModel.class);
        localDeals.whereMatchesQuery("restaurantId", localRestaurants);
        localDeals.include("restaurantId");
        Log.v("Parse info", "Parse query started" );
        final DealListAdapter listAdapter = this;
        localDeals.findInBackground(new FindCallback<DealModel>() {
            public void done(List<DealModel> deals, ParseException e) {
                Log.v("Parse info","Parse query returned");
                if (e == null) {
                    dealItems = deals;
                    //Log.e("Parse info",deals.toString());
                    listAdapter.notifyDataSetChanged();
                } else {
                    Log.e("Parse error: ", e.getMessage());
                }
            }
        });
    }

    @Override
    public DealListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deal_list_item, parent, false);
        //v.setOnClickListener(mOnClickListener);
        ViewHolder vh = new ViewHolder(v,activity);
        return vh;
    }

    @Override
    public int getItemCount() {
        return dealItems.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DealModel dealModel = dealItems.get(position);

        ParseFile imageFile = dealModel.getThumbnailFile();
        if (imageFile != null) {
            imageLoader.displayImage(imageFile.getUrl(), holder.thumbnail);
        }

        holder.deal.setText(dealModel.getTitle());
        holder.likes.setText("Rating: " + String.valueOf(dealModel.getUpVotes()));
        holder.description.setText(dealModel.getDescription());
        holder.restaurant.setText(dealModel.getRestaurant());
        holder.distance.setText(String.format("%.1f", dealModel.getDistanceFrom(parseLocation)) + " mi");
        holder.hours.setText("");
    }
}
