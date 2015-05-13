package com.dgaf.happyhour.View;

/**
 * Created by trentonrobison on 4/26/15.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.dgaf.happyhour.DealListType;
import com.dgaf.happyhour.Model.DealListAdapter;
import com.dgaf.happyhour.Model.DealModel;
import com.dgaf.happyhour.R;

import java.util.ArrayList;
import java.util.List;

/*This is the fragment that our page view loads*/
public class DealListFragment extends Fragment {
    private static final String DEAL_LIST_TYPE = "listType";
    private DealListType listType;
    //section ID acts like ID for the query search as well
    public static DealListFragment newInstance(DealListType listType) {
        DealListFragment fragment = new DealListFragment();
        Bundle args = new Bundle();
        args.putInt(DEAL_LIST_TYPE, listType.ordinal());
        fragment.setArguments(args);

        return fragment;
    }

    public DealListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feature_food_drink, container, false);

        Bundle args = this.getArguments();
        listType = DealListType.values()[args.getInt(DEAL_LIST_TYPE)];

        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(new DealListAdapter(getActivity(), listType));


        return rootView;
    }
}