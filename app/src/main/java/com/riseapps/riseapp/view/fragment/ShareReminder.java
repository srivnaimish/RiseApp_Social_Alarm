package com.riseapps.riseapp.view.fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.auth.FirebaseUser;
import com.riseapps.riseapp.Components.AppConstants;
import com.riseapps.riseapp.R;

import com.riseapps.riseapp.executor.Interface.ToggleShareDialog;
import com.riseapps.riseapp.executor.TimeToView;
import com.riseapps.riseapp.model.DB.Contact_Entity;

import com.riseapps.riseapp.view.activity.MainActivity;
import com.riseapps.riseapp.view.activity.SendReminderActivity;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by naimish on 4/11/17.
 */

public class ShareReminder extends Fragment implements View.OnClickListener {

    ImageButton closeFragment, send;
    FlexboxLayout linearLayout;
    EditText  edit_note, edit_image;
    TextView time, date;
    ArrayList<String> phones = new ArrayList<>();
    private Calendar calendar;
    ArrayList<Contact_Entity> selected_Contacts;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send, container, false);
        calendar = Calendar.getInstance();

        closeFragment = view.findViewById(R.id.closeFragment);
        send = view.findViewById(R.id.send);

        send.setOnClickListener(this);
        closeFragment.setOnClickListener(this);
        linearLayout = view.findViewById(R.id.linearLayout);

        edit_note = view.findViewById(R.id.edit_note);
        edit_image = view.findViewById(R.id.edit_image);
        time = view.findViewById(R.id.time_pick);
        date = view.findViewById(R.id.date_pick);

        time.setOnClickListener(this);
        date.setOnClickListener(this);

        selected_Contacts=((SendReminderActivity)getActivity()).getSelectedContacts();
        addTextStrips();
        return view;
    }

    private void addTextStrips() {
        for(int i=0;i<selected_Contacts.size();i++){
            String phone=selected_Contacts.get(i).getNumber();
            phones.add(phone);
            String name=selected_Contacts.get(i).getName();
            addTextStrip(name);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.time_pick:
                openTimePicker();
                break;

            case R.id.date_pick:
                openDatePicker();
                break;

            case R.id.closeFragment:
                getActivity().finish();
                break;
            case R.id.send:
                if (time.getText().toString().length() == 0 || date.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Enter a valid time and date", Toast.LENGTH_SHORT).show();
                    return;
                } else if (edit_note.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), "Enter a reminder note", Toast.LENGTH_SHORT).show();
                    return;
                }

                new AppConstants().sendReminder(((SendReminderActivity)getActivity()).getUID(), phones, calendar.getTimeInMillis(), edit_note.getText().toString(), edit_image.getText().toString());

                /*StringBuilder people = new StringBuilder();
                for (String s : phones)
                    people.append(s).append(",");

                DBAsync dbAsync = new DBAsync(((SendReminderActivity)getActivity()).getMyapp().getDatabase(), 3);
                dbAsync.setSentParams(people.toString(), calendar.getTimeInMillis(), edit_note.getText().toString(), edit_image.getText().toString());
                dbAsync.execute();
*/
                getActivity().finish();
                break;

        }
    }

    public void addTextStrip(String charSequence) {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(16, 0, 0, 16);
        CardView phone_strip = (CardView) getLayoutInflater().inflate(R.layout.phones_strip, null);
        TextView textView = phone_strip.findViewById(R.id.text);

        textView.setText(charSequence);

        ImageButton imageButton = phone_strip.findViewById(R.id.close);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardView cardView = (CardView) view.getParent().getParent();
                phones.remove(linearLayout.indexOfChild(cardView));
                selected_Contacts.remove(linearLayout.indexOfChild(cardView));
                linearLayout.removeViewAt(linearLayout.indexOfChild(cardView));
            }
        });
        phone_strip.setLayoutParams(layoutParams);
        linearLayout.addView(phone_strip);
    }

    public void openTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendar.set(Calendar.MINUTE, selectedMinute);
                time.setText(new TimeToView().getTimeAsText(selectedHour, selectedMinute));
            }
        }, hour, minute, true);

        mTimePicker.show();
    }

    public void openDatePicker() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int pickedyear, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.YEAR, pickedyear);
                date.setText(String.valueOf(dayOfMonth + "//" + (monthOfYear + 1) + "//" + pickedyear));
            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(getContext(), listener, year, month, day);

        dpDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dpDialog.show();

    }


}
