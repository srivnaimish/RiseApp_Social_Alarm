package com.riseapps.riseapp.view.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Interface.RemindersCallback;
import com.riseapps.riseapp.executor.ReminderSync;
import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.MyApplication;
import com.riseapps.riseapp.view.Adapters.ReminderAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReminderActivity extends AppCompatActivity implements RemindersCallback{

    RecyclerView recyclerView;
    ReminderAdapter reminderAdapter;
    ArrayList<Chat_Entity> reminderList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        recyclerView=findViewById(R.id.reminders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ReminderSync reminderSync=new ReminderSync(this,((MyApplication)getApplicationContext()).getDatabase());
        reminderSync.execute();
    }

    @Override
    public void onRemindersFetch(List<Chat_Entity> reminderList) {
        this.reminderList= (ArrayList<Chat_Entity>) reminderList;
        reminderAdapter=new ReminderAdapter(this,this.reminderList);
        recyclerView.setAdapter(reminderAdapter);
    }
}
