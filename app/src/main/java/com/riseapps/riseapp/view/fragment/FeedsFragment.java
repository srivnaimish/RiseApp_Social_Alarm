package com.riseapps.riseapp.view.fragment;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Adapters.FeedsAdapter;
import com.riseapps.riseapp.executor.ChatSync;
import com.riseapps.riseapp.executor.Interface.ChatCallback;
import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.DB.ChatSummary;
import com.riseapps.riseapp.view.activity.MainActivity;

import java.util.ArrayList;

import static com.riseapps.riseapp.Components.AppConstants.GET_CHAT_SUMMARIES;


public class FeedsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,ChatCallback {

    SwipeRefreshLayout swipeRefreshLayout;
    //ToggleShareDialog toggleShareDialog;
    RecyclerView recyclerView;
    ArrayList<ChatSummary> summaryList = new ArrayList<>();
    FeedsAdapter feedsAdapter;
    private ConstraintLayout empty_state;

    public FeedsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        empty_state = view.findViewById(R.id.empty_state);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = view.findViewById(R.id.shared_reminder);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(feedsAdapter);

        ChatSync chatSync=new ChatSync(((MainActivity)getActivity()).getMyapp().getDatabase(),GET_CHAT_SUMMARIES);
        chatSync.setChatCallback((ChatCallback) this);
        chatSync.execute();


        return view;
    }

    public static FeedsFragment newInstance() {
        return new FeedsFragment();
    }

    @Override
    public void onRefresh() {
        ChatSync chatSync=new ChatSync(((MainActivity)getActivity()).getMyapp().getDatabase(),GET_CHAT_SUMMARIES);
        chatSync.setChatCallback((ChatCallback) this);
        chatSync.execute();
    }

    @Override
    public void summariesFetched(ArrayList<ChatSummary> chatSummaries) {
        if(chatSummaries.size()!=0) {
            summaryList = chatSummaries;
            feedsAdapter = new FeedsAdapter(getContext(), summaryList);
            recyclerView.setAdapter(feedsAdapter);
            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);

            empty_state.setVisibility(View.GONE);
        }else {
            empty_state.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void chatFetched(ArrayList<Chat_Entity> chatList) {

    }
}


