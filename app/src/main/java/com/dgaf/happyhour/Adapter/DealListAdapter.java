package com.dgaf.happyhour.Adapter;

import android.app.Activity;
import android.content.Intent;
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
import com.dgaf.happyhour.Controller.Restaurant;
import com.dgaf.happyhour.Model.AvailabilityModel;
import com.dgaf.happyhour.Model.DealIcon;
import com.dgaf.happyhour.Model.DealModel;
import com.dgaf.happyhour.Model.ModelUpdater;
import com.dgaf.happyhour.Model.Queries.QueryParameters;
import com.dgaf.happyhour.R;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by adam on 4/28/15.
 */
public class DealListAdapter extends RecyclerView.Adapter<DealListAdapter.ViewHolder> implements View.OnClickListener, ModelUpdater<DealModel> {

    private Activity activity;
    private RecyclerView mRecyclerView;
    private List<DealModel> dealItems;
    private SwipeRefreshLayout swipeRefresh;
    private QueryParameters queryParams;
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

        public ViewHolder(View itemView) {
            super(itemView);
            dealTitle = (TextView) itemView.findViewById(R.id.deal_title);
            restaurantName = (TextView) itemView.findViewById(R.id.restaurant_name);
            distance = (TextView) itemView.findViewById(R.id.deal_distance);
            rating = (TextView) itemView.findViewById(R.id.deal_rating);
            availability = (TextView) itemView.findViewById(R.id.deal_availability);
            icon = (ImageView) itemView.findViewById(R.id.deal_icon);
            icon.setImageResource(R.drawable.ic_drinks_placeholder);
        }
    }

    public DealListAdapter(Activity activity, RecyclerView recyclerView, SwipeRefreshLayout swipeRefresh, DealListAdapterNotifier dealListFragment, QueryParameters queryParameters) {
        this.activity = activity;
        this.mRecyclerView = recyclerView;
        this.swipeRefresh = swipeRefresh;
        this.dealItems = new ArrayList<>();
        this.queryParams = queryParameters;
        this.dealListFragment = dealListFragment;

        drawerFragment = (DrawerFragment)((AppCompatActivity)activity).
                getSupportFragmentManager().findFragmentById(R.id.drawerItems);
    }

    //TODO This method is still coupled to Parse and needs refactoring
    @Override
    public void onDataModelUpdate(List<DealModel> deals, Exception e) {
        if (e == null) {
            //TODO remove logging
            Log.v("Parse info", "Deal list query returned " + String.valueOf(deals.size()));

            // Remove deals that aren't available today
            for (Iterator<DealModel> iter = deals.listIterator(); iter.hasNext(); ) {
                DealModel deal = iter.next();
                AvailabilityModel availability1 = new AvailabilityModel(deal,1);
                AvailabilityModel availability2 = new AvailabilityModel(deal,2);

                byte paramMask = queryParams.getDayOfWeekMask().getMask();
                if (!availability1.recurrenceMask.isDaySelected(paramMask) && !availability2.recurrenceMask.isDaySelected(paramMask)) {
                    iter.remove();
                }
            }

            //add place holder to empty deal
            if(deals.size() == 0){
                dealListFragment.notifyEmpty();
            }else{
                dealListFragment.notifyNotEmpty();
            }

            // Sort objects by rating or proximity
            final ParseGeoPoint location = queryParams.getLocation(activity);
            if (queryParams.getQueryType() == QueryParameters.QueryType.PROXIMITY) {
                Collections.sort(deals, new Comparator<DealModel>() {
                    @Override
                    public int compare(DealModel lhs, DealModel rhs) {
                        double diff = lhs.getDistanceFrom(location) - rhs.getDistanceFrom(location);
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
            } else if (queryParams.getQueryType() == QueryParameters.QueryType.RATING){
                Collections.sort(deals, new Comparator<DealModel>() {
                    @Override
                    public int compare(DealModel lhs, DealModel rhs) {
                        double diff = (double)(rhs.getRating() - lhs.getRating());
                        if (diff == 0) {
                            diff = lhs.getDistanceFrom(location) - rhs.getDistanceFrom(location);
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

            // Update items and notify UI of changes
            List<DealModel> prevDealItems = dealItems;
            dealItems = deals;
            if (prevDealItems.size() == 0) {
                notifyItemRangeInserted(0, dealItems.size());
            } else if (prevDealItems.size() > dealItems.size()) {
                notifyItemRangeChanged(0, dealItems.size());
                notifyItemRangeRemoved(dealItems.size(), prevDealItems.size());
            } else {
                notifyItemRangeChanged(0, prevDealItems.size());
                notifyItemRangeInserted(prevDealItems.size(), dealItems.size());
            }
        } else {
            //TODO remove logging
            Log.e("Parse error: ", e.getMessage());
            Toast.makeText(activity, "Unable to process request: " + e.getMessage(), Toast.LENGTH_LONG).show();
            swipeRefresh.setRefreshing(false);    // Update refresh indicator
        }

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
        intent.putExtra("dealId", dealId);

        activity.startActivity(intent);
    }

    @Override
    public DealListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deal_list_item, parent, false);
        v.setOnClickListener(this);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public int getItemCount() {
        return dealItems.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DealModel dealModel = dealItems.get(position);

        holder.dealTitle.setText(dealModel.getDealTitle());
        holder.rating.setText(dealModel.getRatingString());
        holder.restaurantName.setText(dealModel.getRestaurant());

        AvailabilityModel availability1 = new AvailabilityModel(dealModel,1);
        AvailabilityModel availability2 = new AvailabilityModel(dealModel,2);
        String availText1 = availability1.getDayAvailability(queryParams.getDayOfWeekMask().getMask(), true);
        String availText2 = availability2.getDayAvailability(queryParams.getDayOfWeekMask().getMask(), true);
        if (availText1.length() != 0) {
            holder.availability.setText(availText1);
        } else {
            holder.availability.setText(availText2);
        }
        holder.distance.setText(String.format("%.1f", dealModel.getDistanceFrom(queryParams.getLocation(activity))) + " mi");

        DealIcon.setImageToDealCategory(holder.icon, dealModel);

        holder.restaurantId = dealModel.getRestaurantId();
    }

}
