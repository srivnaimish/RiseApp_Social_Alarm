package com.riseapps.riseapp.view.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.riseapps.riseapp.Components.AppConstants;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Adapters.ChatAdapter;
import com.riseapps.riseapp.executor.ChatSync;
import com.riseapps.riseapp.executor.FetchChat;
import com.riseapps.riseapp.executor.Interface.ChatCallback;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.executor.TimeToView;
import com.riseapps.riseapp.model.DB.ChatSummary;
import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.DB.Contact_Entity;
import com.riseapps.riseapp.model.MyApplication;

import java.util.ArrayList;
import java.util.Calendar;

import static com.riseapps.riseapp.Components.AppConstants.CLEAR_CHAT;
import static com.riseapps.riseapp.Components.AppConstants.DELETE_CHAT;
import static com.riseapps.riseapp.Components.AppConstants.INSERT_NEW_CHAT;
import static com.riseapps.riseapp.Components.AppConstants.RECEIVED_MESSAGE;
import static com.riseapps.riseapp.Components.AppConstants.SENT_MESSAGE;

public class ChatActivity extends AppCompatActivity implements ChatCallback,Toolbar.OnMenuItemClickListener{

    private BottomSheetBehavior behavior;
    private EditText  edit_note, edit_image;
    private TextView time, date;
    private Calendar calendar;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private String chat_id;
    private ArrayList<Chat_Entity> chatList=new ArrayList<>();
    private LinearLayout hiddenCardView;
    private Tasks tasks=new Tasks();
    private MyApplication myApplication;
    private String nameString;

    private ImageView chat_background;
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

        chat_background=findViewById(R.id.chat_background);

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

        getChat();

        calendar=Calendar.getInstance();

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
        if(chatList.size()>0) {
            this.chatList = chatList;
            chatAdapter = new ChatAdapter(this, chatList);
            recyclerView.setAdapter(chatAdapter);
            recyclerView.scrollToPosition(chatList.size() - 1);
        }
    }

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
            Toast.makeText(ChatActivity.this, "Enter a reminder note", Toast.LENGTH_SHORT).show();
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

        chatList.add(chat_entity);
        LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_from_bottom);
        recyclerView.setLayoutAnimation(controller);
        chatAdapter.notifyItemInserted(chatList.size()-1);
        recyclerView.scheduleLayoutAnimation();
        recyclerView.scrollToPosition(chatList.size()-1);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.wallpaper:
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.clear:
                if(chatList.size()>0){
                    chatList.clear();
                    chatAdapter.notifyDataSetChanged();
                    ChatSync chatSync=new ChatSync(chat_id,((MyApplication)getApplicationContext()).getDatabase(),CLEAR_CHAT);
                    chatSync.execute();
                }
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
                chat_background.setImageResource(R.drawable.background_1);
                sharedPreferenceSingelton.saveAs(this,"background",0);

                break;
            case R.id.im2:
                chat_background.setImageResource(R.drawable.background_2);
                sharedPreferenceSingelton.saveAs(this,"background",1);
                break;
            case R.id.im3:
                chat_background.setImageResource(R.drawable.background_3);
                sharedPreferenceSingelton.saveAs(this,"background",2);
                break;
            case R.id.im4:
                chat_background.setImageResource(R.drawable.background_4);
                sharedPreferenceSingelton.saveAs(this,"background",3);
                break;
            case R.id.im5:
                chat_background.setImageResource(R.drawable.background_5);
                sharedPreferenceSingelton.saveAs(this,"background",4);
                break;
            case R.id.im6:
                chat_background.setImageResource(R.drawable.background_6);
                sharedPreferenceSingelton.saveAs(this,"background",5);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        int choice=sharedPreferenceSingelton.getSavedInt(this,"background");
        if(choice==0){
            chat_background.setImageResource(R.drawable.background_1);
        }else if(choice==1){
            chat_background.setImageResource(R.drawable.background_2);
        }else if(choice==2){
            chat_background.setImageResource(R.drawable.background_3);
        }else if(choice==3){
            chat_background.setImageResource(R.drawable.background_4);
        }else if(choice==4){
            chat_background.setImageResource(R.drawable.background_5);
        }else if(choice==5){
            chat_background.setImageResource(R.drawable.background_6);
        }
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("Chat Update")
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String id=intent.getStringExtra("chat_id");
            if(chat_id.equalsIgnoreCase(id)){
                Chat_Entity chat_entity=new Chat_Entity();
                chat_entity.setTime(intent.getLongExtra("time",0));
                chat_entity.setNote(intent.getStringExtra("note"));
                chat_entity.setImage(intent.getStringExtra("image"));
                chat_entity.setSent_or_recieved(RECEIVED_MESSAGE);
                chatList.add(chat_entity);
                LayoutAnimationController controller =
                        AnimationUtils.loadLayoutAnimation(ChatActivity.this, R.anim.layout_animation_from_bottom);
                recyclerView.setLayoutAnimation(controller);
                chatAdapter.notifyItemInserted(chatList.size()-1);
                recyclerView.scheduleLayoutAnimation();
                recyclerView.scrollToPosition(chatList.size()-1);

            }
        }
    };

}
