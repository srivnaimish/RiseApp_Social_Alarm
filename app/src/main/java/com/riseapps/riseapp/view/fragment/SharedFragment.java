package com.riseapps.riseapp.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Adapters.SharedReminderAdapter;
import com.riseapps.riseapp.executor.Interface.FabListener;
import com.riseapps.riseapp.executor.Interface.ToggleShareDialog;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.model.Pojo.ReceivedReminder;
import com.riseapps.riseapp.model.Pojo.SentReminder;
import com.riseapps.riseapp.view.activity.MainActivity;

import java.util.ArrayList;


public class SharedFragment extends Fragment{

    ToggleShareDialog toggleShareDialog;
    RecyclerView recyclerView;
    ArrayList<Object> reminderList=new ArrayList<>();
    SharedReminderAdapter sharedReminderAdapter;
    public SharedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_shared, container, false);
        toggleShareDialog= (ToggleShareDialog) getActivity();

        ((MainActivity)getActivity()).setFabListener2(new FabListener() {
            @Override
            public void onFabClick() {
                toggleShareDialog.toggleVisibility();
            }
        });
        fillList();
        sharedReminderAdapter=new SharedReminderAdapter(getContext(),reminderList);
        recyclerView=view.findViewById(R.id.shared_reminder);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(sharedReminderAdapter);
        return view;
    }

    public static SharedFragment newInstance() {
        return new SharedFragment();
    }


   public void fillList(){
        reminderList.add(new SentReminder("abc@xyz.com,aaa@gmail.com","08:00","Get up","Some Image"));
        reminderList.add(new ReceivedReminder("Naimish Srivastava"+" sent you a reminder","08:00","Get up","Some Image"));
        reminderList.add(new ReceivedReminder("Taylor Swift"+" sent you a reminder","08:00","Get up","Some Image"));
        reminderList.add(new SentReminder("abc@xyz.com,aaa@gmail.com","09:00","Get up","Some Image"));
        reminderList.add(new ReceivedReminder("Ashish Srivastava"+" sent you a reminder","08:00","Get up","Some Image"));
        reminderList.add(new SentReminder("abc@xyz.com,aaa@gmail.com","10:00","Get up","Some Image"));
        reminderList.add(new SentReminder("abc@xyz.com,aaa@gmail.com","07:00","Get up","Some Image"));
        reminderList.add(new ReceivedReminder("Naimish Srivastava"+" sent you a reminder","08:00","Get up","Some Image"));

   }

}
