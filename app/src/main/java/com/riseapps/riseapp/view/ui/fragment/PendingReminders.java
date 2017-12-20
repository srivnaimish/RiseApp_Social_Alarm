package com.riseapps.riseapp.view.ui.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Interface.RemindersCallback;
import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.view.Adapters.ReminderAdapter;
import com.riseapps.riseapp.view.ui.activity.MainActivity;
import com.riseapps.riseapp.viewModel.PendingViewModel;

import java.util.ArrayList;
import java.util.List;


public class PendingReminders extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private ImageButton filter;
    private RecyclerView recyclerView;
    private ReminderAdapter reminderAdapter;
    private PendingViewModel pendingViewModel;
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
        reminderAdapter=new ReminderAdapter(getContext(),new ArrayList<Chat_Entity>());
        recyclerView.setAdapter(reminderAdapter);

        filter=view.findViewById(R.id.filter);
        filter.setOnClickListener(this);

        pendingViewModel= ViewModelProviders.of(this).get(PendingViewModel.class);
        pendingViewModel.getPendingList(((MainActivity)getActivity()).getMyapp().getDatabase()).observe(PendingReminders.this, observer);

        return view;
    }

    Observer<List<Chat_Entity>> observer=new Observer<List<Chat_Entity>>() {
        @Override
        public void onChanged(@Nullable List<Chat_Entity> reminderList) {
            reminderAdapter.addItems(reminderList);
            if(reminderList.size()>0) {
                empty_state.setVisibility(View.GONE);
            }else {
                empty_state.setVisibility(View.VISIBLE);
            }
        }
    };

    public static PendingReminders newInstance() {
        return new PendingReminders();
    }

    @Override
    public void onClick(View view) {
        PopupMenu popup = new PopupMenu(getContext(),view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.filter);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pending:
                pendingViewModel.getPendingList(((MainActivity)getActivity()).getMyapp().getDatabase()).observe(PendingReminders.this, observer);
                return true;
            case R.id.today:
                pendingViewModel.getTodayList(((MainActivity)getActivity()).getMyapp().getDatabase()).observe(PendingReminders.this, observer);
                return true;
            default:
                return false;
        }
    }
}


