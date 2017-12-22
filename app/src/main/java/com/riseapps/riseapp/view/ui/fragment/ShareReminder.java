package com.riseapps.riseapp.view.ui.fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.riseapps.riseapp.Components.AppConstants;
import com.riseapps.riseapp.R;

import com.riseapps.riseapp.executor.ChatSync;
import com.riseapps.riseapp.executor.Network.RequestInterface;
import com.riseapps.riseapp.executor.Utils;
import com.riseapps.riseapp.executor.TimeToView;
import com.riseapps.riseapp.model.DB.Contact_Entity;

import com.riseapps.riseapp.model.Pojo.Server.Message;
import com.riseapps.riseapp.model.Pojo.Server.MessageRequest;
import com.riseapps.riseapp.model.Pojo.Server.ServerResponse;
import com.riseapps.riseapp.view.ui.activity.PickContacts;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.riseapps.riseapp.Components.AppConstants.INSERT_NEW_CHAT;
import static com.riseapps.riseapp.Components.AppConstants.REMINDER;
import static com.riseapps.riseapp.Components.AppConstants.SENT_MESSAGE;

/**
 * Created by naimish on 4/11/17.
 */

public class ShareReminder extends Fragment implements View.OnClickListener {

    /*ImageButton closeFragment, send;*/
    FlexboxLayout linearLayout;
    EditText  edit_note;
    TextView time, date;
    ArrayList<String> phones = new ArrayList<>();
    private Calendar calendar;
    ArrayList<Contact_Entity> selected_Contacts;
    Toolbar toolbar;
    Button send;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send, container, false);
        calendar = Calendar.getInstance();

        toolbar=view.findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        linearLayout = view.findViewById(R.id.linearLayout);

        edit_note = view.findViewById(R.id.edit_note);
        time = view.findViewById(R.id.time_pick);
        date = view.findViewById(R.id.date_pick);

        send=view.findViewById(R.id.send);

        time.setOnClickListener(this);
        date.setOnClickListener(this);
        send.setOnClickListener(this);

        selected_Contacts=((PickContacts)getActivity()).getSelectedContacts();
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

            case R.id.send:
                if(Utils.isConnectedToNetwork(getContext())) {
                    if (time.getText().toString().length() == 0 || date.getText().toString().length() == 0) {
                        Snackbar.make(edit_note,"Pick a time and date",Snackbar.LENGTH_SHORT).show();
                        return;
                    } else if (edit_note.getText().toString().length() == 0) {
                        Snackbar.make(edit_note,"Enter a task",Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    sendReminder(((PickContacts) getActivity()).getUID(), phones, calendar.getTimeInMillis(), edit_note.getText().toString());

                    ChatSync chatSync = new ChatSync(selected_Contacts, calendar.getTimeInMillis(), edit_note.getText().toString(), SENT_MESSAGE, true, ((PickContacts) getActivity()).getMyapp().getDatabase(), INSERT_NEW_CHAT);
                    chatSync.execute();

                }else {
                    Snackbar.make(edit_note,"Not connected to the internet",Snackbar.LENGTH_SHORT).show();
                }
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

    public void sendReminder(String sender, ArrayList<String> Recipients, long Time, String Note) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setOperation(REMINDER);

        Message message = new Message(sender, Recipients, Time, Note);
        messageRequest.setMessage(message);

        Snackbar.make(edit_note, "Sending Task ...", Snackbar.LENGTH_LONG).show();

        Call<ServerResponse> response = requestInterface.chat(messageRequest);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                assert resp != null;
                if (resp.getResult().equalsIgnoreCase("Success")) {
                    Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                 Snackbar.make(edit_note, t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
