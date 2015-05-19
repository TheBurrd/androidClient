package com.dgaf.happyhour.Model;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgaf.happyhour.R;
import com.dgaf.happyhour.View.DealListFragment;


public class DealListAdapter extends ArrayAdapter<String>{

    private LayoutInflater inflater;

    public DealListAdapter(Context context, String[] details) {
        super(context, R.layout.deal_adaptor, details);

    }

    @Override
    public View getView(int position, View controllerView, ViewGroup parent)
    {
        LayoutInflater dealInflater = LayoutInflater.from(getContext());
        View customView = dealInflater.inflate(R.layout.deal_adaptor,parent,false);

        String singleDeals = getItem(position);
        TextView Deal = (TextView) customView.findViewById(R.id.Deal);
        ImageView Picture = (ImageView) customView.findViewById(R.id.picture);


        Deal.setText(singleDeals);
        Picture.setImageResource(R.drawable.llama);

        return customView;
    }

}
