package com.dgaf.happyhour.Model.Adapter;
import com.dgaf.happyhour.Controller.LocationService;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dgaf.happyhour.DealListType;
import com.dgaf.happyhour.Model.AvailabilityModel;
import com.dgaf.happyhour.Model.DealModel;
import com.dgaf.happyhour.Model.QueryParameters;
import com.dgaf.happyhour.Model.RestaurantModel;
import com.dgaf.happyhour.R;
import com.dgaf.happyhour.View.RestaurantFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
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
 * Created by trentonrobison on 4/28/15.
 */
public class DealListAdapter extends RecyclerView.Adapter<DealListAdapter.ViewHolder> implements SwipeRefreshLayout.OnRefreshListener, QueryParameters.Listener, View.OnClickListener {
    private FragmentActivity activity;
    private RecyclerView mRecyclerView;
    private ImageLoader imageLoader;
    private List<DealModel> dealItems;
    private ParseGeoPoint parseLocation;
    private DealListType listType;
    private LocationService userLocation;
    private SwipeRefreshLayout swipeRefresh;
    private QueryParameters mQueryParams;
    private AvailabilityModel.WeekDay currentDayFilter;
    private static final String DEAL_LIST_CACHE = "dealList";
    private static HashMap queryHashes = new HashMap<>();


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView deal;
        public TextView description;
        public TextView distance;
        public TextView rating;
        public TextView hours;
        public ParseImageView thumbnail;
        public String restaurantId;

        public ViewHolder(View itemView, DealListType listType) {
            super(itemView);
            deal = (TextView) itemView.findViewById(R.id.deal);
            description = (TextView) itemView.findViewById(R.id.description);
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

    public DealListAdapter(FragmentActivity activity, RecyclerView recyclerView, SwipeRefreshLayout swipeRefresh, DealListType dealListType) {
        this.activity = activity;
        this.mRecyclerView = recyclerView;
        this.imageLoader = ImageLoader.getInstance();
        this.swipeRefresh = swipeRefresh;
        this.dealItems = new ArrayList<>();

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
        localDeals.whereMatchesQuery("restaurantId", localRestaurants);
        switch(listType) {
            case DRINK:
                localDeals.whereEqualTo("tags","drink");
                break;
            case FOOD:
                localDeals.whereEqualTo("tags","food");
                break;
            case FEATURED:
                localDeals.whereEqualTo("tags","featured");
                break;
        }
        applyDayOfWeekForQuery(localDeals);
        localDeals.include("restaurantId");
        Log.v("Parse info", "Deal list query started" );
        final DealListAdapter listAdapter = this;
        localDeals.findInBackground(new FindCallback<DealModel>() {
            public void done(List<DealModel> deals, ParseException e) {
                Log.v("Parse info","Deal list query returned");
                if (e == null) {
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
                    queryHashes.put(mQueryParams.hashCode(), true);
                    // Release any objects previously pinned for this query.
                    ParseObject.unpinAllInBackground(DEAL_LIST_CACHE, dealItems, new DeleteCallback() {
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e("Parse error: ", e.getMessage());
                                return;
                            }
                            // Update refresh indicator
                            swipeRefresh.setRefreshing(false);
                            // Add the latest results for this query to the cache.
                            ParseObject.pinAllInBackground(DEAL_LIST_CACHE, dealItems);
                        }
                    });
                    int i = 0;
                    List<DealModel> prevDealItems = dealItems;
                    dealItems = deals;
                    if (prevDealItems.size() == 0) {
                        listAdapter.notifyItemRangeInserted(0, dealItems.size());
                    } else {
                        listAdapter.notifyItemRangeChanged(0, prevDealItems.size());
                        listAdapter.notifyItemRangeInserted(prevDealItems.size(),dealItems.size());
                    }
                } else {
                    Log.e("Parse error: ", e.getMessage());
                }
            }
        });
    }

    public void applyDayOfWeekForQuery(ParseQuery<DealModel> query) {
        switch (mQueryParams.getWeekDay()) {
            case TODAY:
                // TODO THIS IS HARDCODED FOR THE PRESNETATION
                // IMPLEMENT ME!!!
                int time = 1500;
                String today = "wednesdayEn";
                String endDay = "wednesdaySt";
                query.whereGreaterThanOrEqualTo(today, time);
                query.whereLessThanOrEqualTo(endDay, (time + mQueryParams.TODAY_TIME_RANGE) % 2400);
                break;
            case MONDAY:
                query.whereGreaterThanOrEqualTo("mondayEn", 0);
                query.whereLessThanOrEqualTo("mondaySt", 2400);
                break;
            case TUESDAY:
                query.whereGreaterThanOrEqualTo("tuesdayEn",0);
                query.whereLessThanOrEqualTo("tuesdaySt",2400);
                break;
            case WEDNESDAY:
                query.whereGreaterThanOrEqualTo("wednesdayEn",0);
                query.whereLessThanOrEqualTo("wednesdaySt",2400);
                break;
            case THURSDAY:
                query.whereGreaterThanOrEqualTo("thursdayEn",0);
                query.whereLessThanOrEqualTo("thursdaySt",2400);
                break;
            case FRIDAY:
                query.whereGreaterThanOrEqualTo("fridayEn",0);
                query.whereLessThanOrEqualTo("fridaySt",2400);
                break;
            case SATURDAY:
                query.whereGreaterThanOrEqualTo("saturdayEn",0);
                query.whereLessThanOrEqualTo("saturdaySt",2400);
                break;
            case SUNDAY:
                query.whereGreaterThanOrEqualTo("sundayEn",0);
                query.whereLessThanOrEqualTo("sundaySt",2400);
                break;
        }
        currentDayFilter = AvailabilityModel.getDayOfWeek( mQueryParams.getWeekDay());
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
        Fragment restaurant = RestaurantFragment.newInstance(restaurantId, dealId);
        restaurant.setEnterTransition(new Fade());
        restaurant.setExitTransition(new Fade());
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.main_fragment, restaurant).addToBackStack(null).commit();
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

        ParseFile imageFile = dealModel.getThumbnailFile();
        if (imageFile != null) {
            imageLoader.displayImage(imageFile.getUrl(), holder.thumbnail);
        }

        holder.deal.setText(dealModel.getTitle());
        int rating = dealModel.getRating();
        if (rating != 0) {
            holder.rating.setText(String.valueOf(dealModel.getRating()) + "%");
        }
        holder.description.setText(dealModel.getDescription());
        holder.distance.setText(String.format("%.1f", dealModel.getDistanceFrom(parseLocation)) + " mi");
        holder.hours.setText(dealModel.getAvailability().getDayAvailability(currentDayFilter, true));
        if (AvailabilityModel.getDayOfWeek() != currentDayFilter) {
            holder.hours.setTextColor(Color.BLACK);
        } else {
            holder.hours.setTextColor(Color.rgb(0,170,0));
        }
        holder.restaurantId = dealModel.getRestaurantId();
    }
}
