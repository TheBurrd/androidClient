package com.dgaf.happyhour.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dgaf.happyhour.Controller.DealListEmptyNotifier;
import com.dgaf.happyhour.Controller.LocationService;
import com.dgaf.happyhour.Controller.Restaurant;
import com.dgaf.happyhour.Model.AvailabilityModel;
import com.dgaf.happyhour.Model.DealListType;
import com.dgaf.happyhour.Model.DealModel;
import com.dgaf.happyhour.Model.QueryParameters;
import com.dgaf.happyhour.Model.RestaurantModel;
import com.dgaf.happyhour.R;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by adam on 4/28/15.
 */
public class DealListAdapter extends RecyclerView.Adapter<DealListAdapter.ViewHolder> implements SwipeRefreshLayout.OnRefreshListener, QueryParameters.Listener, View.OnClickListener {

    //private FragmentActivity activity; not needed we will change to context
    private Context context;
    private RecyclerView mRecyclerView;
    private List<DealModel> dealItems;
    private ParseGeoPoint parseLocation;
    private DealListType listType;
    private LocationService userLocation;
    private SwipeRefreshLayout swipeRefresh;
    private QueryParameters mQueryParams;
    private AvailabilityModel.WeekDay currentDayFilter;
    private static final String DEAL_LIST_CACHE = "dealList";
    private DealListEmptyNotifier notifier;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView deal;
        public TextView fineprint;
        public TextView distance;
        public TextView rating;
        public TextView hours;
        public ParseImageView thumbnail;
        public String restaurantId;

        public ViewHolder(View itemView, DealListType listType) {
            super(itemView);
            deal = (TextView) itemView.findViewById(R.id.deal);
            fineprint = (TextView) itemView.findViewById(R.id.finePrint);
            distance = (TextView) itemView.findViewById(R.id.distance);
            rating = (TextView) itemView.findViewById(R.id.rating);
            hours = (TextView) itemView.findViewById(R.id.hours);
            thumbnail = (ParseImageView) itemView.findViewById(R.id.thumb_nal);
            if (listType == DealListType.FOOD) {
                thumbnail.setPlaceholder(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_food_placeholder));
            } else {
                thumbnail.setPlaceholder(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_drinks_placeholder));
            }
        }
    }

    public DealListAdapter(Context context, RecyclerView recyclerView, SwipeRefreshLayout swipeRefresh, DealListType dealListType, DealListEmptyNotifier notifier) {
        this.context = context;
        this.mRecyclerView = recyclerView;
        this.swipeRefresh = swipeRefresh;
        this.dealItems = new ArrayList<>();
        this.notifier = notifier;

        swipeRefresh.setOnRefreshListener(this);
        listType = dealListType;
        parseLocation = getLocation();
        mQueryParams = QueryParameters.getInstance();
        mQueryParams.addListener(this);
        onRefresh();
    }

    public ParseGeoPoint getLocation() {
        // Geisel Library - Default Location
        double latitude = 32.881122;
        double longitude = -117.237631;
        if (!Build.FINGERPRINT.startsWith("generic")) {
            userLocation = new LocationService(context);
            // Is user location available and are we not running in an emulator
            if (userLocation.canGetLocation()) {
                latitude = userLocation.getLatitude();
                longitude = userLocation.getLongitude();
            } else {
                userLocation.showSettingsAlert();
            }
        }
        return new ParseGeoPoint(latitude,longitude);
    }

    public void loadDeals() {
        // Setup the database Query
        ParseQuery<RestaurantModel> localRestaurants = ParseQuery.getQuery(RestaurantModel.class);
        localRestaurants.whereWithinMiles("location", parseLocation, mQueryParams.getRadiusMi());
        ParseQuery<DealModel> localDeals = ParseQuery.getQuery(DealModel.class);
        ParseQuery<DealModel> orLocalDeals = ParseQuery.getQuery(DealModel.class);
        localDeals.whereMatchesQuery("restaurantId", localRestaurants);
        orLocalDeals.whereMatchesQuery("restaurantId", localRestaurants);
        //TODO remove hardcoded tag search
        switch(listType) {
            case DRINK:
                localDeals.whereEqualTo("tags","drink");
                orLocalDeals.whereEqualTo("tags","drink");
                break;
            case FOOD:
                localDeals.whereEqualTo("tags","food");
                orLocalDeals.whereEqualTo("tags","food");
                break;
            case FEATURED:
                localDeals.whereEqualTo("tags","featured");
                orLocalDeals.whereEqualTo("tags","featured");
                break;
        }
        localDeals = applyDayOfWeekForQuery(localDeals, orLocalDeals);
        localDeals.include("restaurantId");
        //TODO remove logging
        Log.v("Parse info", "Deal list query started" );
        final DealListAdapter listAdapter = this;
        localDeals.findInBackground(new FindCallback<DealModel>() {
            public void done(List<DealModel> deals, ParseException e) {
                if (e == null) {
                    //TODO remove logging
                    Log.v("Parse info", "Deal list query returned " + String.valueOf(deals.size()));

                    //add place holder to empty deal
                    if(deals.size() == 0){
                        notifier.notifyEmpty();
                    }else{
                        notifier.notifyNotEmpty();
                    }

                    if (mQueryParams.getQueryType() == QueryParameters.QueryType.PROXIMITY) {
                        Collections.sort(deals, new Comparator<DealModel>() {
                            @Override
                            public int compare(DealModel lhs, DealModel rhs) {
                                double diff = lhs.getDistanceFrom(parseLocation) - rhs.getDistanceFrom(parseLocation);
                                if (diff < 0 && diff > -1.0) {
                                    diff = -1.0;
                                } else if (diff > 0 && diff < 1.0) {
                                    diff = 1.0;
                                }
                                if (diff == 0) {
                                    diff = rhs.getRating() - lhs.getRating();
                                }
                                return (int) (diff);
                            }
                        });
                    } else if (mQueryParams.getQueryType() == QueryParameters.QueryType.RATING){
                        Collections.sort(deals, new Comparator<DealModel>() {
                            @Override
                            public int compare(DealModel lhs, DealModel rhs) {
                                double diff = (double)(rhs.getRating() - lhs.getRating());
                                if (diff == 0) {
                                    diff = lhs.getDistanceFrom(parseLocation) - rhs.getDistanceFrom(parseLocation);
                                    if (diff < 0 && diff > -1.0) {
                                        diff = -1.0;
                                    } else if (diff > 0 && diff < 1.0) {
                                        diff = 1.0;
                                    }
                                }
                                return (int) (diff);
                            }
                        });
                    }
                    // Release any objects previously pinned for this query.
                    ParseObject.unpinAllInBackground(DEAL_LIST_CACHE, dealItems, new DeleteCallback() {
                        public void done(ParseException e) {
                            if (e != null) {
                                //TODO remove logging
                                Log.e("Parse error: ", e.getMessage());
                                return;
                            }
                            // Update refresh indicator
                            swipeRefresh.setRefreshing(false);
                            // Add the latest results for this query to the cache.
                            ParseObject.pinAllInBackground(DEAL_LIST_CACHE, dealItems);
                        }
                    });
                    List<DealModel> prevDealItems = dealItems;
                    dealItems = deals;
                    if (prevDealItems.size() == 0) {
                        listAdapter.notifyItemRangeInserted(0, dealItems.size());
                    } else if (prevDealItems.size() > dealItems.size()) {
                        listAdapter.notifyItemRangeChanged(0, dealItems.size());
                        listAdapter.notifyItemRangeRemoved(dealItems.size(), prevDealItems.size());
                    } else {
                        listAdapter.notifyItemRangeChanged(0, prevDealItems.size());
                        listAdapter.notifyItemRangeInserted(prevDealItems.size(), dealItems.size());
                    }
                } else {
                    //TODO remove logging
                    Log.e("Parse error: ", e.getMessage());
                    Toast.makeText(context, "Unable to process request: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    swipeRefresh.setRefreshing(false);    // Update refresh indicator
                }
            }
        });
    }

    public ParseQuery<DealModel> applyDayOfWeekForQuery(ParseQuery<DealModel> query, ParseQuery<DealModel> orQuery) {
        return query;
    }

    @Override
    public void onUpdate() {
        mQueryParams = QueryParameters.getInstance();
        onRefresh();
    }

    @Override
    public void onRefresh() {
        parseLocation = getLocation();
        loadDeals();
    }

    @Override
    public void onClick(View v) {

        int position = mRecyclerView.getChildAdapterPosition(v);
        DealModel dealModel = dealItems.get(position);
        String restaurantId = dealModel.getRestaurantId();
        String dealId = dealModel.getId();

        Intent intent = new Intent(context, Restaurant.class);

        intent.putExtra("resId",restaurantId);
        intent.putExtra("dealId",dealId);

        context.startActivity(intent);
    }

    @Override
    public DealListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deal_list_item, parent, false);
        v.setOnClickListener(this);
        ViewHolder vh = new ViewHolder(v, listType);
        return vh;
    }

    @Override
    public int getItemCount() {
        return dealItems.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DealModel dealModel = dealItems.get(position);

        holder.deal.setText(this.getDealTitle(dealModel));
        int rating = dealModel.getRating();
        if (rating != 0) {
            holder.rating.setText(String.valueOf(dealModel.getRating()) + "%");
        }
        holder.fineprint.setText(dealModel.getDescription());
        holder.distance.setText(String.format("%.1f", dealModel.getDistanceFrom(parseLocation)) + " mi");


        holder.restaurantId = dealModel.getRestaurantId();
    }

    public String getDealTitle(DealModel deal) {
        //TODO make nice title
        return String.valueOf(deal.getAmountOff()) + " " + deal.getItem();
    }
}
