package com.dgaf.happyhour.Model;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
    private ParseGeoPoint userLocation;

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

        // TODO: Get users GPS coords
        double radiusMi = 100.0;
        // Geisel Library
        userLocation = new ParseGeoPoint(32.881122,-117.237631);

        switch(listType) {
            case DRINK:
                loadLocalDeals(userLocation, radiusMi);
                break;
            case FOOD:
                loadLocalDeals(userLocation, radiusMi);
                break;
            case FEATURED:
                loadLocalDeals(userLocation, radiusMi);
                break;
        }
    }

    public void loadLocalDeals(ParseGeoPoint location, double radiusMi) {
        // Setup the database Query
        ParseQuery<DealModel> localDeals = ParseQuery.getQuery(DealModel.class);
        localDeals.whereWithinMiles("location", location, radiusMi);
        localDeals.include("restaurantId");
        final DealListAdapter listAdapter = this;
        localDeals.findInBackground(new FindCallback<DealModel>() {
            public void done(List<DealModel> deals, ParseException e) {
                if (e == null) {
                    dealItems = deals;
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
        holder.distance.setText(String.format("%.1f", dealModel.getDistanceFrom(userLocation)) + " mi");
        holder.hours.setText("");
    }
}