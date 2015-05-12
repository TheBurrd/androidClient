package com.dgaf.happyhour.Model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dgaf.happyhour.R;

import java.util.List;

/**
 * Created by trentonrobison on 4/28/15.
 */
public class DealListAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<DealModel> dealItems;
    //ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public DealListAdapter(Activity activity, List<DealModel> dealItems) {
        this.activity = activity;
        this.dealItems = dealItems;
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

        restaurant.setText(dealModel.getRestaurantID());

        hours.setText("");
        //do something with theses
        //distance.setText(dealModel.getLongitude() + dealModel.getLatitude());


        return convertView;
    }
}