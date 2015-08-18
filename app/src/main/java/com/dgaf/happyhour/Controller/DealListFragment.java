package com.dgaf.happyhour.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dgaf.happyhour.Adapter.DealListAdapter;
import com.dgaf.happyhour.Model.DealListType;
import com.dgaf.happyhour.Model.Queries.Parse.DrinkDealListQuery;
import com.dgaf.happyhour.Model.Queries.Parse.FeaturedDealListQuery;
import com.dgaf.happyhour.Model.Queries.Parse.FoodDealListQuery;
import com.dgaf.happyhour.Model.Queries.Query;
import com.dgaf.happyhour.Model.Queries.QueryParameters;
import com.dgaf.happyhour.R;
import com.dgaf.happyhour.View.DealListDecoration;

import jp.wasabeef.recyclerview.animators.LandingAnimator;

/*This is the fragment that our page view loads*/
public class DealListFragment extends Fragment implements DealListEmptyNotifier, SwipeRefreshLayout.OnRefreshListener, QueryParameters.Listener {

    private static final String DEAL_LIST_TYPE = "listType";
    private DealListType listType;
    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecyclerView;
    private DealListAdapter mAdapter;
    private Query mQuery;
    private QueryParameters mQueryParams;
    private LinearLayoutManager mLayoutManager;
    private RelativeLayout errorLayout;

        //section ID acts like ID for the query search as well
    public static DealListFragment newInstance(DealListType listType) {
        DealListFragment fragment = new DealListFragment();
        fragment.mQueryParams = QueryParameters.getInstance();
        fragment.mQueryParams.addListener(fragment);
        Bundle args = new Bundle();
        args.putInt(DEAL_LIST_TYPE, listType.ordinal());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.deal_list, container, false);



        mSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefresh.setColorSchemeResources(R.color.colorWine, R.color.colorPrimaryDark, R.color.colorPrimary);
        mSwipeRefresh.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        errorLayout = (RelativeLayout) rootView.findViewById(R.id.error);

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

        switch (listType) {
            case DRINK:
                mQuery = new DrinkDealListQuery();
                break;
            case FOOD:
                mQuery = new FoodDealListQuery();
                break;
            case FEATURED:
                mQuery = new FeaturedDealListQuery();
                break;
        }
        mAdapter = new DealListAdapter(getActivity(), mRecyclerView, mSwipeRefresh, this, mQueryParams);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new LandingAnimator());

        mRecyclerView.addItemDecoration(new DealListDecoration(getActivity()));

        onRefresh();

    }

    @Override
    public void onUpdate() {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        mQuery.fetch(getActivity(), mQueryParams, mAdapter);
    }

    @Override
    public void notifyEmpty() {
        errorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void notifyNotEmpty() {
        errorLayout.setVisibility(View.GONE);
    }
}
