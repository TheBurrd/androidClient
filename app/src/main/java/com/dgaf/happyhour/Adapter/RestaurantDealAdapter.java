package com.dgaf.happyhour.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.dgaf.happyhour.Controller.LocationService;
import com.dgaf.happyhour.Device;
import com.dgaf.happyhour.Model.DealIcon;
import com.dgaf.happyhour.Model.DealModel;
import com.dgaf.happyhour.Model.ModelUpdater;
import com.dgaf.happyhour.Model.RestaurantModel;
import com.dgaf.happyhour.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adam on 8/26/2015.
 */
public class RestaurantDealAdapter implements ModelUpdater<DealModel> {
    private Activity activity;
    private DealModel dealModel;
    private RestaurantDealViewHolder dealHolder;

    public static class RestaurantDealViewHolder implements CompoundButton.OnCheckedChangeListener {
        public TextView dealTitle;
        public TextView dealAvailability;
        public TextView dealFineprint;
        public TextView dealRating;
        public ToggleButton upVote;
        public ToggleButton downVote;
        public ImageView categoryIcon;

        public Activity activity;
        public RestaurantDealAdapter parentAdapter;

        public RestaurantDealViewHolder(Activity activity, RestaurantDealAdapter parentAdapter) {
            this.activity = activity;
            this.parentAdapter = parentAdapter;

            categoryIcon = (ImageView) activity.findViewById(R.id.icon);
            dealTitle = (TextView) activity.findViewById(R.id.deal_title);
            dealAvailability = (TextView) activity.findViewById(R.id.deal_availability);
            dealFineprint = (TextView) activity.findViewById(R.id.deal_fineprint);
            dealRating = (TextView) activity.findViewById(R.id.rating);
            upVote = (ToggleButton) activity.findViewById(R.id.upVote);
            downVote = (ToggleButton) activity.findViewById(R.id.downVote);

            upVote.setOnCheckedChangeListener(this);
            downVote.setOnCheckedChangeListener(this);

        }
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (compoundButton == upVote) {
                if(isChecked){
                    upVote.setBackgroundResource(R.drawable.ic_thumb_up_selected);
                    if (downVote.isChecked()) {
                        downVote.setChecked(false);
                        parentAdapter.undoDownVote();
                    }
                    parentAdapter.upVote();
                }else{
                    upVote.setBackgroundResource(R.drawable.ic_thumb_up);
                    parentAdapter.undoUpVote();
                }
            } else if (compoundButton == downVote) {
                if (isChecked) {
                    downVote.setBackgroundResource(R.drawable.ic_thumb_down_selected);
                    if (upVote.isChecked()) {
                        upVote.setChecked(false);
                        parentAdapter.undoUpVote();
                    }
                    parentAdapter.downVote();
                }else{
                    downVote.setBackgroundResource(R.drawable.ic_thumb_down);
                    parentAdapter.undoDownVote();
                }
            }

        }
    }

    public RestaurantDealAdapter(Activity activity) {
        this.activity = activity;
        createViewHolders();
    }

    @Override
    public void onDataModelUpdate(List<DealModel> deals, Exception e) {
        if (e == null) {
            if (deals.size() == 1) {
                dealModel = deals.get(0);
                bindRestaurantViewHolder();
            } else {
                Log.e("Parse error: ", "Unable to find deal");
            }
        } else {
            //TODO remove logging
            Log.e("Parse error: ", e.getMessage());
            Toast.makeText(activity, "Unable to process request: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void upVote() {
        if (dealModel != null) {
            Map<String,String> params = new HashMap<>();
            final Activity restaurantActivity = activity;
            params.put("objectId", dealModel.getId());
            params.put("deviceId", Device.getDeviceId(activity));
            ParseCloud.callFunctionInBackground("upVote", params, new FunctionCallback<String>() {
                public void done(String results, ParseException e) {
                    if (results != null) {
                        Toast.makeText(restaurantActivity, results, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(restaurantActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void downVote() {
        if (dealModel != null) {
            Map<String, String> params = new HashMap<>();
            final Activity restaurantActivity = activity;
            params.put("objectId", dealModel.getId());
            params.put("deviceId", Device.getDeviceId(activity));
            ParseCloud.callFunctionInBackground("downVote", params, new FunctionCallback<String>() {
                public void done(String results, ParseException e) {
                    if (results != null) {
                        Toast.makeText(restaurantActivity, results, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(restaurantActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void undoUpVote() {
        if (dealModel != null) {
            Map<String,String> params = new HashMap<>();
            final Activity restaurantActivity = activity;
            params.put("objectId", dealModel.getId());
            params.put("deviceId", Device.getDeviceId(activity));
            ParseCloud.callFunctionInBackground("undoUpVote", params, new FunctionCallback<String>() {
                public void done(String results, ParseException e) {
                    if (results != null) {
                        Toast.makeText(restaurantActivity, results, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(restaurantActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void undoDownVote() {
        if (dealModel != null) {
            Map<String,String> params = new HashMap<>();
            final Activity restaurantActivity = activity;
            params.put("objectId", dealModel.getId());
            params.put("deviceId", Device.getDeviceId(activity));
            ParseCloud.callFunctionInBackground("undoDownVote", params, new FunctionCallback<String>() {
                public void done(String results, ParseException e) {
                    if (results != null){
                        Toast.makeText(restaurantActivity, results, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(restaurantActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void createViewHolders() {
        dealHolder = new RestaurantDealViewHolder(activity, this);
        bindRestaurantViewHolder();
    }

    public void bindRestaurantViewHolder() {
        if (dealHolder != null && dealModel != null) {

            dealHolder.dealTitle.setText(dealModel.getDealTitle());
            // Comment this out till all deals in database have new availability model
            // AvailabilityModel availability = new AvailabilityModel(dealModel);
            // dealAvailability.setText(availability.getDayAvailability(DayOfWeekMask.TODAY, true));
            dealHolder.dealAvailability.setText("Mon: 10a - 8p");
            dealHolder.dealFineprint.setText(dealModel.getFineprint());
            dealHolder.dealRating.setText(dealModel.getRatingString());

            DealIcon.setImageToDealCategory(dealHolder.categoryIcon, dealModel);

        }
    }
}
