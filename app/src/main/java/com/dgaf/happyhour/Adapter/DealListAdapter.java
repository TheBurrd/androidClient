package com.dgaf.happyhour.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.dgaf.happyhour.Controller.LocationService;
import com.dgaf.happyhour.Controller.Restaurant;
import com.dgaf.happyhour.Model.AvailabilityModel;
import com.dgaf.happyhour.Model.DealListType;
import com.dgaf.happyhour.Model.DealModel;
import com.dgaf.happyhour.Model.QueryParameters;
import com.dgaf.happyhour.Model.RestaurantModel;
import com.dgaf.happyhour.R;
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
import java.util.Calendar;
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

    public DealListAdapter(Context context, RecyclerView recyclerView, SwipeRefreshLayout swipeRefresh, DealListType dealListType) {
        this.context = context;
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
        Log.v("Parse info", "Deal list query started" );
        final DealListAdapter listAdapter = this;
        localDeals.findInBackground(new FindCallback<DealModel>() {
            public void done(List<DealModel> deals, ParseException e) {
                if (e == null) {
                    Log.v("Parse info", "Deal list query returned " + String.valueOf(deals.size()));
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
                    } else if (prevDealItems.size() > dealItems.size()) {
                        listAdapter.notifyItemRangeChanged(0, dealItems.size());
                        listAdapter.notifyItemRangeRemoved(dealItems.size(), prevDealItems.size());
                    } else {
                        listAdapter.notifyItemRangeChanged(0, prevDealItems.size());
                        listAdapter.notifyItemRangeInserted(prevDealItems.size(), dealItems.size());
                    }
                } else {
                    Log.e("Parse error: ", e.getMessage());
                    //Toast.makeText(activity, "Unable to process request: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    // Update refresh indicator
                    swipeRefresh.setRefreshing(false);
                }
            }
        });
    }

    public ParseQuery<DealModel> applyDayOfWeekForQuery(ParseQuery<DealModel> query, ParseQuery<DealModel> orQuery) {
        switch (mQueryParams.getWeekDay()) {
            case TODAY:
                Calendar cal = Calendar.getInstance();
                //int time = cal.get(Calendar.HOUR_OF_DAY)*100 + cal.get(Calendar.MINUTE);
                int time = 1300;
                String today = "";
                switch (cal.get(Calendar.DAY_OF_WEEK)) {
                    case Calendar.MONDAY:
                        today = "mondayEn";
                        break;
                    case Calendar.TUESDAY:
                        today = "tuesdayEn";
                        break;
                    case Calendar.WEDNESDAY:
                        today = "wednesdayEn";
                        break;
                    case Calendar.THURSDAY:
                        today = "thursdayEn";
                        break;
                    case Calendar.FRIDAY:
                        today = "fridayEn";
                        break;
                    case Calendar.SATURDAY:
                        today = "saturdayEn";
                        break;
                    case Calendar.SUNDAY:
                        today = "sundayEn";
                        break;
                }
                if ((time + mQueryParams.TODAY_TIME_RANGE) / 2400.0 > 1.0) {
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                }
                String endDay = "";
                switch (cal.get(Calendar.DAY_OF_WEEK)) {
                    case Calendar.MONDAY:
                        endDay = "mondaySt";
                        break;
                    case Calendar.TUESDAY:
                        endDay = "tuesdaySt";
                        break;
                    case Calendar.WEDNESDAY:
                        endDay = "wednesdaySt";
                        break;
                    case Calendar.THURSDAY:
                        endDay = "thursdaySt";
                        break;
                    case Calendar.FRIDAY:
                        endDay = "fridaySt";
                        break;
                    case Calendar.SATURDAY:
                        endDay = "saturdaySt";
                        break;
                    case Calendar.SUNDAY:
                        endDay = "sundaySt";
                        break;
                }
                query.whereGreaterThanOrEqualTo(today, time);
                if (today != endDay) {
                    orQuery.whereLessThanOrEqualTo(endDay, (time + mQueryParams.TODAY_TIME_RANGE) % 2400);
                    List<ParseQuery<DealModel>> qList = new ArrayList<>();
                    qList.add(query);
                    qList.add(orQuery);
                    query = ParseQuery.or(qList);
                } else {
                    query.whereLessThanOrEqualTo(endDay, (time + mQueryParams.TODAY_TIME_RANGE) % 2400);
                }
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
        String dealAvail = dealModel.getAvailability().getDayAvailability(currentDayFilter, true);;
        if (AvailabilityModel.getDayOfWeek() == currentDayFilter) {
            if (dealAvail == "") {
                dealAvail = dealModel.getAvailability().getDayAvailability(AvailabilityModel.WeekDay.values()[(currentDayFilter.ordinal() + 1)%7], true);
                holder.hours.setTextColor(Color.BLACK);
            } else {
                holder.hours.setTextColor(Color.rgb(0,170,0));
            }
        } else {
            holder.hours.setTextColor(Color.BLACK);
        }
        holder.hours.setText(dealAvail);
        holder.restaurantId = dealModel.getRestaurantId();
    }
}
