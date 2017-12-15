package com.riseapps.riseapp.view.ui.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.riseapps.riseapp.Components.AppConstants;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.viewModel.ChatViewModel;
import com.riseapps.riseapp.view.Adapters.ChatAdapter;
import com.riseapps.riseapp.executor.ChatSync;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.executor.TimeToView;
import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.DB.Contact_Entity;
import com.riseapps.riseapp.model.MyApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.riseapps.riseapp.Components.AppConstants.CLEAR_CHAT;
import static com.riseapps.riseapp.Components.AppConstants.INSERT_NEW_CHAT;
import static com.riseapps.riseapp.Components.AppConstants.SENT_MESSAGE;

public class ChatActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener{

    private BottomSheetBehavior behavior;
    private EditText  edit_note, edit_image;
    private TextView time, date;
    private Calendar calendar;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private String chat_id;
    private ChatViewModel chatViewModel;
    //private ArrayList<Chat_Entity> chatList=new ArrayList<>();
    private LinearLayout hiddenCardView;
    private Tasks tasks=new Tasks();
    private MyApplication myApplication;
    private String nameString;

    private CoordinatorLayout chat_background;
    private SharedPreferenceSingelton sharedPreferenceSingelton=new SharedPreferenceSingelton();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (tasks.getCurrentTheme(this) == 0) {
            setTheme(R.style.AppTheme2);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        myApplication= (MyApplication) getApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageButton back = findViewById(R.id.back);
        TextView initials = findViewById(R.id.initials);
        TextView name = findViewById(R.id.chat_title);
        recyclerView=findViewById(R.id.chat_messages);

        hiddenCardView=findViewById(R.id.card_dialog);
        time=findViewById(R.id.time_pick);
        date=findViewById(R.id.date_pick);
        edit_image=findViewById(R.id.edit_image);
        edit_note=findViewById(R.id.edit_note);

        View bottomSheet = findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);

        chat_background=findViewById(R.id.background);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chat_id=getIntent().getStringExtra("chat_id");
        nameString=getIntent().getStringExtra("contact_name");
        name.setText(nameString);
        initials.setText(tasks.getInitial(nameString));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        toolbar.inflateMenu(R.menu.chat_menu);
        toolbar.setOnMenuItemClickListener(this);

        chatAdapter=new ChatAdapter(this,new ArrayList<Chat_Entity>());
        recyclerView.setAdapter(chatAdapter);

        chatViewModel= ViewModelProviders.of(this).get(ChatViewModel.class);

        chatViewModel.getChatList(myApplication.getDatabase(),chat_id).observe(ChatActivity.this, observer);

        calendar=Calendar.getInstance();

    }

    Observer<List<Chat_Entity>> observer=new Observer<List<Chat_Entity>>() {
        @Override
        public void onChanged(@Nullable List<Chat_Entity> chat_entities) {
            chatAdapter.addItems(chat_entities);
            if(chat_entities.size()!=0)
            recyclerView.scrollToPosition(chat_entities.size()-1);
        }
    };


    public void openTimePicker(View view) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker = new TimePickerDialog(ChatActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                calendar.set(Calendar.MINUTE, selectedMinute);
                time.setText(new TimeToView().getTimeAsText(selectedHour, selectedMinute));
            }
        }, hour, minute, true);

        mTimePicker.show();
    }

    public void openDatePicker(View view) {
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

        DatePickerDialog dpDialog = new DatePickerDialog(ChatActivity.this, listener, year, month, day);

        dpDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dpDialog.show();

    }

    public void sendButton(View view){
        if(hiddenCardView.getVisibility()==View.GONE){
            hiddenCardView.setVisibility(View.VISIBLE);
            return;
        }
        if (time.getText().toString().length() == 0 || date.getText().toString().length() == 0) {
            Toast.makeText(ChatActivity.this, "Enter a valid time and date", Toast.LENGTH_SHORT).show();
            return;
        } else if (edit_note.getText().toString().length() == 0) {
            Toast.makeText(ChatActivity.this, "Enter a reminder_row note", Toast.LENGTH_SHORT).show();
            return;
        }

        new AppConstants().sendReminderToSingleUser(myApplication.getUID(), chat_id, calendar.getTimeInMillis(), edit_note.getText().toString(), edit_image.getText().toString());

        ArrayList<Contact_Entity> contact_entities=new ArrayList<>();
        contact_entities.add(new Contact_Entity(tasks.getInitial(nameString),nameString,chat_id,true));
        ChatSync chatSync=new ChatSync(contact_entities,calendar.getTimeInMillis(),edit_note.getText().toString(),edit_image.getText().toString(),SENT_MESSAGE,true,myApplication.getDatabase(),INSERT_NEW_CHAT);
        chatSync.execute();

        Chat_Entity chat_entity=new Chat_Entity();
        chat_entity.setTime(calendar.getTimeInMillis());
        chat_entity.setNote(edit_note.getText().toString());
        chat_entity.setImage(edit_image.getText().toString());

        hiddenCardView.setVisibility(View.GONE);
        edit_note.setText("");
        edit_note.clearFocus();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.wallpaper:
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.clear:
                ChatSync chatSync=new ChatSync(chat_id,((MyApplication)getApplicationContext()).getDatabase(),CLEAR_CHAT);
                chatSync.execute();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(behavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else {
            super.onBackPressed();
        }
    }

    public void setBackground(View view) {
        switch (view.getId()){
            case R.id.im1:
                chat_background.setBackgroundColor(0);
                sharedPreferenceSingelton.saveAs(this,"background",0);

                break;
            case R.id.im2:
                chat_background.setBackgroundColor(ContextCompat.getColor(this,R.color.color1));
                sharedPreferenceSingelton.saveAs(this,"background",1);
                break;
            case R.id.im3:
                chat_background.setBackgroundColor(ContextCompat.getColor(this,R.color.color2));
                sharedPreferenceSingelton.saveAs(this,"background",2);
                break;
            case R.id.im4:
                chat_background.setBackgroundColor(ContextCompat.getColor(this,R.color.color3));
                sharedPreferenceSingelton.saveAs(this,"background",3);
                break;
            case R.id.im5:
                chat_background.setBackgroundColor(ContextCompat.getColor(this,R.color.color4));
                sharedPreferenceSingelton.saveAs(this,"background",4);
                break;
            case R.id.im6:
                chat_background.setBackgroundColor(ContextCompat.getColor(this,R.color.color5));
                sharedPreferenceSingelton.saveAs(this,"background",5);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        int choice=sharedPreferenceSingelton.getSavedInt(this,"background");
        if(choice==1){
            chat_background.setBackgroundColor(ContextCompat.getColor(this,R.color.color1));
        }else if(choice==2){
            chat_background.setBackgroundColor(ContextCompat.getColor(this,R.color.color2));
        }else if(choice==3){
            chat_background.setBackgroundColor(ContextCompat.getColor(this,R.color.color3));
        }else if(choice==4){
            chat_background.setBackgroundColor(ContextCompat.getColor(this,R.color.color4));
        }else if(choice==5){
            chat_background.setBackgroundColor(ContextCompat.getColor(this,R.color.color5));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
