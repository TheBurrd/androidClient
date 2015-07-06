package com.dgaf.happyhour.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.dgaf.happyhour.Model.DealModel;
import com.dgaf.happyhour.Model.RestaurantModel;
import com.dgaf.happyhour.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 5/27/2015.
 */
public class ExpandDealListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<DealModel> dealItems;

    public static class DealHeaderHolder {
        public TextView title;

        public DealHeaderHolder(View rootView) {
            title = (TextView) rootView.findViewById(R.id.deal_title);
        }
    }

    public static class DealBodyHolder {
        public TextView description;

        public DealBodyHolder(View rootView) {
            description = (TextView) rootView.findViewById(R.id.deal_description);
        }
    }

    public ExpandDealListAdapter(Context context, String restaurantId) {
        this.context = context;
        this.dealItems = new ArrayList<>();
        loadRestaurantDeals(restaurantId);
    }

    public void loadRestaurantDeals(String restaurantId) {
        ParseQuery<DealModel> dealsQuery = ParseQuery.getQuery(DealModel.class);
        ParseObject restaurant = ParseObject.createWithoutData(RestaurantModel.class, restaurantId);
        dealsQuery.whereEqualTo("restaurantId", restaurant);
        dealsQuery.fromLocalDatastore();
        Log.v("Parse info:", "Started restaurant deals query");
        final ExpandDealListAdapter listAdapter = this;
        dealsQuery.findInBackground(new FindCallback<DealModel>() {
            public void done(List<DealModel> deals, ParseException e) {
                Log.v("Parse info:", "Restaurant deals query returned");
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
    public DealModel getGroup(int groupPosition) {
        return this.dealItems.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        DealHeaderHolder header;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.restaurant_deal_header, null);

            header = new DealHeaderHolder(convertView);
            convertView.setTag(header);
        } else {
            header = (DealHeaderHolder) convertView.getTag();
        }

        DealModel deal = getGroup(groupPosition);
        header.title.setText(deal.getTitle());

        return convertView;
    }

    @Override
    public int getGroupCount() {
        return this.dealItems.size();
    }


    @Override
    public DealModel getChild(int groupPosition, int childPosition) {
        return dealItems.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        DealBodyHolder body;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.restaurant_deal_body, null);

            body = new DealBodyHolder(convertView);
            convertView.setTag(body);
        } else {
            body = (DealBodyHolder) convertView.getTag();
        }

        DealModel deal = getChild(groupPosition, childPosition);
        body.description.setText(deal.getDescription());

        return  convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}
