package com.riseapps.riseapp.view.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.riseapps.riseapp.Components.AppConstants;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Adapters.ChatAdapter;
import com.riseapps.riseapp.executor.ChatSync;
import com.riseapps.riseapp.executor.FetchChat;
import com.riseapps.riseapp.executor.Interface.ChatCallback;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.executor.TimeToView;
import com.riseapps.riseapp.model.DB.ChatSummary;
import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.DB.Contact_Entity;
import com.riseapps.riseapp.model.MyApplication;

import java.util.ArrayList;
import java.util.Calendar;

import static com.riseapps.riseapp.Components.AppConstants.INSERT_NEW_CHAT;
import static com.riseapps.riseapp.Components.AppConstants.SENT_MESSAGE;

public class ChatActivity extends AppCompatActivity implements ChatCallback,View.OnClickListener{

    private EditText  edit_note, edit_image;
    private TextView time, date;
    private Calendar calendar;
    private ImageButton send;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private TextView initials,name;
    private Toolbar toolbar;
    private String chat_id;
    private String contact_number;
    private ArrayList<Chat_Entity> chatList;
    private CardView hiddenCardView;
    private Tasks tasks=new Tasks();
    private MyApplication myApplication;
    private String nameString;

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

        toolbar=findViewById(R.id.toolbar);
        ImageButton back = findViewById(R.id.back);
        initials=findViewById(R.id.initials);
        name=findViewById(R.id.chat_title);
        recyclerView=findViewById(R.id.chat_messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chat_id=getIntent().getStringExtra("chat_id");
        contact_number=getIntent().getStringExtra("contact_number");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        hiddenCardView=findViewById(R.id.card_dialog);

        time=findViewById(R.id.time_pick);
        date=findViewById(R.id.date_pick);
        edit_image=findViewById(R.id.edit_image);
        edit_note=findViewById(R.id.edit_note);
        send=findViewById(R.id.send);

        time.setOnClickListener(this);
        date.setOnClickListener(this);
        send.setOnClickListener(this);

        getChat();

    }

    private void getChat() {
        FetchChat fetchChat=new FetchChat(chat_id,((MyApplication)getApplicationContext()).getDatabase(),this);
        fetchChat.execute();
    }

    @Override
    public void summariesFetched(ArrayList<ChatSummary> chatSummaries) {

    }

    @Override
    public void chatFetched(ArrayList<Chat_Entity> chatList) {
        this.chatList=chatList;
        nameString=chatList.get(0).getContact_name();
        name.setText(nameString);
        initials.setText(tasks.getInitial(nameString));

        chatAdapter=new ChatAdapter(this,chatList);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.scrollToPosition(chatList.size()-1);
    }

    public void openTimePicker() {
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

        DatePickerDialog dpDialog = new DatePickerDialog(ChatActivity.this, listener, year, month, day);

        dpDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dpDialog.show();

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
                if(hiddenCardView.getVisibility()==View.GONE){
                    hiddenCardView.setVisibility(View.VISIBLE);
                    break;
                }
                if (time.getText().toString().length() == 0 || date.getText().toString().length() == 0) {
                    Toast.makeText(ChatActivity.this, "Enter a valid time and date", Toast.LENGTH_SHORT).show();
                    return;
                } else if (edit_note.getText().toString().length() == 0) {
                    Toast.makeText(ChatActivity.this, "Enter a reminder note", Toast.LENGTH_SHORT).show();
                    return;
                }

                new AppConstants().sendReminderToSingleUser(myApplication.getUID(), contact_number, calendar.getTimeInMillis(), edit_note.getText().toString(), edit_image.getText().toString());

                ArrayList<Contact_Entity> contact_entities=new ArrayList<>();
                contact_entities.add(new Contact_Entity(tasks.getInitial(nameString),nameString,contact_number,true));
                ChatSync chatSync=new ChatSync(contact_entities,calendar.getTimeInMillis(),edit_note.getText().toString(),edit_image.getText().toString(),SENT_MESSAGE,true,myApplication.getDatabase(),INSERT_NEW_CHAT);
                chatSync.execute();

                Chat_Entity chat_entity=new Chat_Entity();
                chat_entity.setTime(calendar.getTimeInMillis());
                chat_entity.setNote(edit_note.getText().toString());
                chat_entity.setImage(edit_image.getText().toString());

                chatList.add(chat_entity);
                chatAdapter.notifyItemInserted(chatList.size()-1);
                break;
        }
    }
}
