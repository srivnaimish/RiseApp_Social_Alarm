package com.riseapps.riseapp.view.fragment;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Adapters.PersonalAlarmAdapter;
import com.riseapps.riseapp.executor.AlarmCreator;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.model.PersonalAlarm;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class PersonalFragment extends Fragment {

    ArrayList<PersonalAlarm> personalAlarms=new ArrayList<>();
    RecyclerView recyclerView;
    PersonalAlarmAdapter alarmsAdapter;
    ConstraintLayout add_button;
    private TimePickerDialog mTimePicker;

    private Tasks tasks=new Tasks();

    AlarmCreator alarmCreator=new AlarmCreator();
    private SharedPreferenceSingelton sharedPreferenceSingelton=new SharedPreferenceSingelton();

    public PersonalFragment() {
    }

    public static PersonalFragment newInstance() {
        return new PersonalFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_personal, container, false);

        add_button=view.findViewById(R.id.add_alarm);
        recyclerView=view.findViewById(R.id.personal_alarms);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        personalAlarms.add(alarmCreator.getPersonalAlarm(selectedHour,selectedMinute,true,false));
                        alarmsAdapter.notifyItemInserted(personalAlarms.size()-1);
                        recyclerView.smoothScrollToPosition(personalAlarms.size()-1);

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.show();
            }
        });
        ArrayList<PersonalAlarm> alarms=tasks.getPersonalAlarms(getContext());
        if(alarms!=null){
            personalAlarms=alarms;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        alarmsAdapter=new PersonalAlarmAdapter(getContext(),personalAlarms,recyclerView);
        recyclerView.setAdapter(alarmsAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                alarmsAdapter.delete(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
