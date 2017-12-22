package com.riseapps.riseapp.view.ui.fragment;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.ChatSync;
import com.riseapps.riseapp.executor.Interface.Filter;
import com.riseapps.riseapp.executor.Interface.RemindersCallback;
import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.view.Adapters.ReminderAdapter;
import com.riseapps.riseapp.view.ui.activity.MainActivity;

import java.util.ArrayList;

import static com.riseapps.riseapp.Components.AppConstants.GET_PENDING_REMINDERS;
import static com.riseapps.riseapp.Components.AppConstants.GET_TODAY_REMINDERS;


public class PendingReminders extends Fragment implements RemindersCallback{

    private RecyclerView recyclerView;
    private ReminderAdapter reminderAdapter;
    //private PendingViewModel pendingViewModel;
    private LinearLayout empty_state;
    public PendingReminders() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);
        empty_state=view.findViewById(R.id.empty_state);
        recyclerView=view.findViewById(R.id.reminders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        ChatSync chatSync=new ChatSync(((MainActivity) getActivity()).getMyapp().getDatabase(),GET_PENDING_REMINDERS,this);
        chatSync.execute();

        ((MainActivity) getActivity()).addFilterClickListener(new Filter() {
            @Override
            public void filterByPending() {
                Snackbar.make(recyclerView,"Pending Reminders",Snackbar.LENGTH_SHORT).show();
                ChatSync chatSync=new ChatSync(((MainActivity) getActivity()).getMyapp().getDatabase(),GET_PENDING_REMINDERS,PendingReminders.this);
                chatSync.execute();
            }

            @Override
            public void filterByToday() {
                Snackbar.make(recyclerView,"Today Reminders",Snackbar.LENGTH_SHORT).show();
                ChatSync chatSync=new ChatSync(((MainActivity) getActivity()).getMyapp().getDatabase(),GET_TODAY_REMINDERS,PendingReminders.this);
                chatSync.execute();
            }
        });

        return view;
    }

    public static PendingReminders newInstance() {
        return new PendingReminders();
    }

    @Override
    public void onPendingFetch(ArrayList<Chat_Entity> pendingList) {
        if(pendingList.size()>0) {
            reminderAdapter=new ReminderAdapter(getContext(),pendingList);
            recyclerView.setAdapter(reminderAdapter);
            empty_state.setVisibility(View.GONE);
        }else {
            empty_state.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTodaysFetch(ArrayList<Chat_Entity> todaysList) {
        if(todaysList.size()>0) {
            reminderAdapter=new ReminderAdapter(getContext(),todaysList);
            recyclerView.setAdapter(reminderAdapter);
            empty_state.setVisibility(View.GONE);
        }else {
            empty_state.setVisibility(View.VISIBLE);
        }
    }
}


