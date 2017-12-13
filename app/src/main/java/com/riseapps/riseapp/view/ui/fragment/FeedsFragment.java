package com.riseapps.riseapp.view.ui.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.SummaryViewModel;
import com.riseapps.riseapp.view.Adapters.FeedsAdapter;
import com.riseapps.riseapp.model.DB.ChatSummary;
import com.riseapps.riseapp.view.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;


public class FeedsFragment extends Fragment {

    RecyclerView recyclerView;
    FeedsAdapter feedsAdapter;
    private ConstraintLayout empty_state;
    private SummaryViewModel summaryViewModel;
    public FeedsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);

        empty_state = view.findViewById(R.id.empty_state);
        recyclerView = view.findViewById(R.id.shared_reminder);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);*/
        feedsAdapter=new FeedsAdapter(getContext(),new ArrayList<ChatSummary>());
        recyclerView.setAdapter(feedsAdapter);

        summaryViewModel= ViewModelProviders.of(this).get(SummaryViewModel.class);

        summaryViewModel.getSummaryList(((MainActivity)getActivity()).getMyapp().getDatabase()).observe(FeedsFragment.this, observer);

        return view;
    }

    Observer<List<ChatSummary>> observer=new Observer<List<ChatSummary>>() {
        @Override
        public void onChanged(@Nullable List<ChatSummary> summaryList) {
            feedsAdapter.addItems(summaryList);
            if(summaryList.size()>0) {
                empty_state.setVisibility(View.GONE);
            }else {
                empty_state.setVisibility(View.VISIBLE);
            }
        }
    };

    public static FeedsFragment newInstance() {
        return new FeedsFragment();
    }

}


