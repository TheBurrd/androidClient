package com.dgaf.happyhour.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dgaf.happyhour.DealListType;
import com.dgaf.happyhour.Model.DealListAdapter;
import com.dgaf.happyhour.R;

/*This is the fragment that our page view loads*/
public class DealListFragment extends Fragment {

    private static final String DEAL_LIST_TYPE = "listType";
    private static final String DEAL_LIST_RADIUS = "radius";
    private DealListType listType;
    private double radiusMi;
    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;

        //section ID acts like ID for the query search as well
    public static DealListFragment newInstance(DealListType listType, double radiusMi) {

        DealListFragment fragment = new DealListFragment();
        Bundle args = new Bundle();
        args.putInt(DEAL_LIST_TYPE, listType.ordinal());
        args.putDouble(DEAL_LIST_RADIUS, radiusMi);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feature_food_drink, container, false);

        mSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.scrollToPosition(0);

        Bundle args = this.getArguments();
        listType = DealListType.values()[args.getInt(DEAL_LIST_TYPE)];
        radiusMi = args.getDouble(DEAL_LIST_RADIUS);

        mAdapter = new DealListAdapter(getActivity(), mSwipeRefresh, listType, radiusMi);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);


    }
}
