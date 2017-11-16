package com.riseapps.riseapp.view.fragment;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.auth.FirebaseUser;
import com.riseapps.riseapp.Components.AppConstants;
import com.riseapps.riseapp.executor.DBAsync;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Interface.ToggleShareDialog;
import com.riseapps.riseapp.executor.TimeToView;
import com.riseapps.riseapp.view.activity.MainActivity;
import com.riseapps.riseapp.widgets.TextStrips;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by naimish on 4/11/17.
 */

public class ShareReminder extends Fragment implements View.OnClickListener, TextWatcher {

    ToggleShareDialog toggleShareDialog;
    ImageButton closeFragment,send;
    FlexboxLayout linearLayout;
    EditText edit_email,edit_note,edit_image;
    TextView time;
    long timestamp;
    ArrayList<String> emails=new ArrayList<>();
    FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_send, container, false);

        firebaseUser=((MainActivity)getActivity()).currentUser;

        toggleShareDialog= (ToggleShareDialog) getActivity();
        closeFragment=view.findViewById(R.id.closeFragment);
        send=view.findViewById(R.id.send);

        send.setOnClickListener(this);
        closeFragment.setOnClickListener(this);
        linearLayout=view.findViewById(R.id.linearLayout);

        edit_email=view.findViewById(R.id.edit_email);
        edit_note=view.findViewById(R.id.edit_note);
        edit_image=view.findViewById(R.id.edit_image);
        time=view.findViewById(R.id.time_pick);

        time.setOnClickListener(this);
        edit_email.addTextChangedListener(this);

        edit_email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    addTextStrip(edit_email.getText().toString(),true);
                }

                return handled;
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.time_pick:
                openTimePicker();
                break;

            case R.id.closeFragment:
                toggleShareDialog.toggleVisibility();
                break;
            case R.id.send:
                if(emails.size()==0){
                    Toast.makeText(getContext(), "Enter atleast 1 email", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(time.getText().toString().length()==0){
                    Toast.makeText(getContext(), "Enter a reminder time", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(edit_note.getText().toString().length()==0){
                    Toast.makeText(getContext(), "Enter a reminder note", Toast.LENGTH_SHORT).show();
                    return;
                }

                new AppConstants().sendReminder(firebaseUser.getUid(), emails, timestamp, edit_note.getText().toString(), edit_image.getText().toString());

                StringBuilder people= new StringBuilder();
                for(String s:emails)
                    people.append(s).append(",");

                DBAsync dbAsync=new DBAsync(((MainActivity)getActivity()).getMyapp().getDatabase(),3);
                dbAsync.setSentParams(people.toString(),timestamp,edit_note.getText().toString(),edit_image.getText().toString());
                dbAsync.execute();
                
                Toast.makeText(getContext(), "Sending reminder", Toast.LENGTH_SHORT).show();

                break;

        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(charSequence.length()!=0) {
            char c = charSequence.charAt(charSequence.length() - 1);
            if (c == ' ' || c == ',') {
                addTextStrip(edit_email.getText().toString(),false);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public void addTextStrip(String charSequence,boolean enterpress){

            TextStrips textStrips = new TextStrips(getContext());

            TextView textView = (TextView) textStrips.getChildAt(0);
            String phone;
            if(!enterpress)
            phone = charSequence.substring(0, charSequence.length() - 1);
            else
                phone = charSequence.substring(0, charSequence.length());
            emails.add(phone);
            textView.setText(phone);
            linearLayout.addView(textStrips);

            ImageButton imageButton = (ImageButton) textStrips.getChildAt(1);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextStrips textStrips1 = (TextStrips) view.getParent();
                    emails.remove(linearLayout.indexOfChild(textStrips1));
                    linearLayout.removeViewAt(linearLayout.indexOfChild(textStrips1));
                }
            });
        edit_email.setText("");
    }

    public void openTimePicker(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,selectedHour);
                calendar.set(Calendar.MINUTE,selectedMinute);
                timestamp=calendar.getTimeInMillis();
                time.setText(new TimeToView().getTimeAsText(selectedHour,selectedMinute));
            }
        }, hour, minute, true);
        mTimePicker.show();
    }



}
