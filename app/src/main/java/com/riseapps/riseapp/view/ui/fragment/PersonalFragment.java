package com.riseapps.riseapp.view.ui.fragment;


import android.app.TimePickerDialog;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.view.Adapters.PersonalAlarmAdapter;
import com.riseapps.riseapp.executor.AlarmCreator;

import com.riseapps.riseapp.executor.Interface.FabListener;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.executor.TimeToView;
import com.riseapps.riseapp.model.Pojo.PersonalAlarm;
import com.riseapps.riseapp.view.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class PersonalFragment extends Fragment {

    ArrayList<PersonalAlarm> personalAlarms = new ArrayList<>();
    RecyclerView recyclerView;
    PersonalAlarmAdapter alarmsAdapter;
    Tasks tasks = new Tasks();
    AlarmCreator alarmCreator = new AlarmCreator();
    private LinearLayout empty_state;
    TimeToView timeToView = new TimeToView();

    public PersonalFragment() {
    }

    public static PersonalFragment newInstance() {
        return new PersonalFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal, container, false);

        recyclerView = view.findViewById(R.id.personal_alarms);
        empty_state = view.findViewById(R.id.empty_state);
        ArrayList<PersonalAlarm> alarms = tasks.getPersonalAlarms(getContext());
        if (alarms != null) {
            personalAlarms = alarms;
        }

        if (personalAlarms.size() > 0) {
            empty_state.setVisibility(View.GONE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        alarmsAdapter = new PersonalAlarmAdapter(getContext(), personalAlarms, empty_state);
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

        ((MainActivity) getActivity()).setFabListener1(new FabListener() {
            @Override
            public void onFabClick() {
                openTimePicker();
            }
        });

        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void openTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                personalAlarms.add(alarmCreator.getPersonalAlarm(RingtoneManager.getActualDefaultRingtoneUri(getContext(), RingtoneManager.TYPE_ALARM), selectedHour, selectedMinute, true, false));
                alarmsAdapter.notifyItemInserted(personalAlarms.size() - 1);
                recyclerView.smoothScrollToPosition(personalAlarms.size() - 1);
                empty_state.setVisibility(View.GONE);

            }
        }, hour, minute, true);
        mTimePicker.show();
    }

}
