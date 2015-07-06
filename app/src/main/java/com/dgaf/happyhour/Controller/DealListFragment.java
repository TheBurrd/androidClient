package com.dgaf.happyhour.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dgaf.happyhour.Model.DealListType;
import com.dgaf.happyhour.Adapter.DealListAdapter;
import com.dgaf.happyhour.R;

import jp.wasabeef.recyclerview.animators.LandingAnimator;

/*This is the fragment that our page view loads*/
public class DealListFragment extends Fragment {

    private static final String DEAL_LIST_TYPE = "listType";
    private DealListType listType;
    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecyclerView;
    private DealListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

        //section ID acts like ID for the query search as well
    public static DealListFragment newInstance(DealListType listType) {
        DealListFragment fragment = new DealListFragment();
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

        mAdapter = new DealListAdapter(getActivity(), mRecyclerView, mSwipeRefresh, listType);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new LandingAnimator());
        //mRecyclerView.addItemDecoration(new DealListDecoration(getActivity()));
        //mRecyclerView.setHasFixedSize(true);

    }
}
