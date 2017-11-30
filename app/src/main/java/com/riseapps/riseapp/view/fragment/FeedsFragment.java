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
import android.widget.Toast;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Adapters.SharedReminderAdapter;
import com.riseapps.riseapp.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class FeedsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipeRefreshLayout;
    //ToggleShareDialog toggleShareDialog;
    RecyclerView recyclerView;
    ArrayList<Object> reminderList = new ArrayList<>();
    SharedReminderAdapter sharedReminderAdapter;
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
        //toggleShareDialog = (ToggleShareDialog) getActivity();

        /*((MainActivity) getActivity()).setFabListener2(new FabListener() {
            @Override
            public void onFabClick() {
                toggleShareDialog.toggleVisibility();
            }
        });*/

        sharedReminderAdapter = new SharedReminderAdapter(getContext(), reminderList);
        recyclerView = view.findViewById(R.id.shared_reminder);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(sharedReminderAdapter);
        return view;
    }

    public static FeedsFragment newInstance() {
        return new FeedsFragment();
    }

    @Override
    public void onRefresh() {


        /*DBAsync dbAsync = new DBAsync(((MainActivity) getActivity()).getMyapp().getDatabase(), 1);
        try {
            reminderList = dbAsync.execute().get();
            if (reminderList.size() == 0)
                empty_state.setVisibility(View.VISIBLE);
            else
                empty_state.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
        Toast.makeText(getContext(), "Feeds Refreshed ", Toast.LENGTH_SHORT).show();
        sharedReminderAdapter = new SharedReminderAdapter(getContext(), reminderList);
        recyclerView.setAdapter(sharedReminderAdapter);

        //
    }
}


