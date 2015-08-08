package com.dgaf.happyhour.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dgaf.happyhour.Controller.DealListAdapterNotifier;
import com.dgaf.happyhour.Controller.DrawerFragment;
import com.dgaf.happyhour.Controller.LocationService;
import com.dgaf.happyhour.Controller.Restaurant;
import com.dgaf.happyhour.Model.DealIcon;
import com.dgaf.happyhour.Model.DealListType;
import com.dgaf.happyhour.Model.DealModel;
import com.dgaf.happyhour.Model.QueryParameters;
import com.dgaf.happyhour.Model.RestaurantModel;
import com.dgaf.happyhour.R;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by adam on 4/28/15.
 */
public class DealListAdapter extends RecyclerView.Adapter<DealListAdapter.ViewHolder> implements SwipeRefreshLayout.OnRefreshListener, QueryParameters.Listener, View.OnClickListener {

    //private FragmentActivity activity; not needed we will change to context
    private Activity activity;
    private RecyclerView mRecyclerView;
    private List<DealModel> dealItems;
    private ParseGeoPoint parseLocation;
    private DealListType listType;
    private LocationService userLocation;
    private SwipeRefreshLayout swipeRefresh;
    private QueryParameters mQueryParams;
    private static final String DEAL_LIST_CACHE = "dealList";
    private DealListAdapterNotifier dealListFragment;
    private DealListAdapterNotifier drawerFragment;




    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dealTitle;
        public TextView restaurantName;
        public TextView distance;
        public TextView rating;
        public TextView availability;
        public ImageView icon;
        public String restaurantId;

        public ViewHolder(View itemView, DealListType listType) {
            super(itemView);
            dealTitle = (TextView) itemView.findViewById(R.id.deal_title);
            restaurantName = (TextView) itemView.findViewById(R.id.restaurant_name);
            distance = (TextView) itemView.findViewById(R.id.deal_distance);
            rating = (TextView) itemView.findViewById(R.id.deal_rating);
            availability = (TextView) itemView.findViewById(R.id.deal_availability);
            icon = (ImageView) itemView.findViewById(R.id.deal_icon);
            if (listType == DealListType.FOOD) {
                icon.setImageResource(R.drawable.ic_food_placeholder);
            } else {
                icon.setImageResource(R.drawable.ic_drinks_placeholder);
            }
        }
    }

    public DealListAdapter(Activity activity, RecyclerView recyclerView, SwipeRefreshLayout swipeRefresh, DealListType dealListType, DealListAdapterNotifier dealListFragment) {
        this.activity = activity;
        this.mRecyclerView = recyclerView;
        this.swipeRefresh = swipeRefresh;
        this.dealItems = new ArrayList<>();
        this.dealListFragment = dealListFragment;

        swipeRefresh.setOnRefreshListener(this);
        listType = dealListType;
        parseLocation = getLocation();
        mQueryParams = QueryParameters.getInstance();
        mQueryParams.addListener(this);

        drawerFragment = (DrawerFragment)((AppCompatActivity)activity).
                getSupportFragmentManager().findFragmentById(R.id.drawerItems);
        onRefresh();
    }

    public ParseGeoPoint getLocation() {
        // Geisel Library - Default Location
        double latitude = 32.881122;
        double longitude = -117.237631;
        if (!Build.FINGERPRINT.startsWith("generic")) {
            userLocation = new LocationService(activity);
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
                        dealListFragment.notifyEmpty();
                    }else{
                        dealListFragment.notifyNotEmpty();
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
                    Toast.makeText(activity, "Unable to process request: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    swipeRefresh.setRefreshing(false);    // Update refresh indicator
                }

            }
        });
    }

    public ParseQuery<DealModel> applyDayOfWeekForQuery(ParseQuery<DealModel> query, ParseQuery<DealModel> orQuery) {
        //TODO implement query based on new availability model
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

        //notify all listeners that adapter has been updated
        drawerFragment.adapterUpdate();

    }

    @Override
    public void onClick(View v) {

        int position = mRecyclerView.getChildAdapterPosition(v);
        DealModel dealModel = dealItems.get(position);
        String restaurantId = dealModel.getRestaurantId();
        String dealId = dealModel.getId();

        Intent intent = new Intent(activity, Restaurant.class);

        intent.putExtra("resId", restaurantId);
        intent.putExtra("dealId",dealId);

        activity.startActivity(intent);
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

        holder.dealTitle.setText(this.getDealTitle(dealModel));
        holder.rating.setText(String.valueOf(dealModel.getRating()) + "%");
        holder.restaurantName.setText(dealModel.getRestaurant());
        // Comment this out till all deals in database have new availability model
        // AvailabilityModel availability = new AvailabilityModel(dealModel);
        // holder.availability.setText(availability.getDayAvailability(DayOfWeekMask.TODAY, true));
        holder.availability.setText("Mon: 10a - 8p");
        holder.distance.setText(String.format("%.1f", dealModel.getDistanceFrom(parseLocation)) + " mi");


        //DealIcon.setImageToDealCategory(holder.icon, dealModel);
        DealIcon.setImageToDealCategory(holder.icon, dealModel);//test to see performance with smaller category images

        holder.restaurantId = dealModel.getRestaurantId();
    }

    public String getDealTitle(DealModel deal) {
        String value;
        double amountOff = deal.getAmountOff();
        double percentOff = deal.getPercentOff();
        double reducedPrice = deal.getReducedPrice();
        if (amountOff != 0) {
            if (amountOff == (long) amountOff) {
                value = String.format("%d", (long) amountOff);
            } else {
                value = String.format("%.2f", amountOff);
            }
            value = "$" + value + " off";
        } else if (percentOff != 0) {
            value = String.format("%d", (long)percentOff) + "% off";
        } else {
            if (reducedPrice == (long) reducedPrice) {
                value = String.format("%d", (long) reducedPrice);
            } else {
                value = String.format("%.2f", reducedPrice);
            }
            value = "$" + value;
        }

        String content = "";
        String item = deal.getItem();
        String category = deal.getCategory();
        if (item != null && item.length() > 0) {
            content = item;
        } else if (category != null &&  category.length() > 0) {
            content = category;
        }

        return value + " " + content;
    }

}
