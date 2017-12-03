package com.riseapps.riseapp.view.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Adapters.ChatAdapter;
import com.riseapps.riseapp.executor.ChatSync;
import com.riseapps.riseapp.executor.Interface.ChatCallback;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.model.DB.ChatSummary;
import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.MyApplication;

import java.util.ArrayList;
import java.util.Calendar;

import static com.riseapps.riseapp.Components.AppConstants.GET_CHAT;
import static com.riseapps.riseapp.Components.AppConstants.RECEIVED_MESSAGE;

public class ChatActivity extends AppCompatActivity implements ChatCallback{

    private EditText  edit_note, edit_image;
    private TextView time, date;
    private Calendar calendar;
    private ImageButton send1,send2;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private TextView initials,name;
    private Toolbar toolbar;
    private int contact_id;
    private ArrayList<Chat_Entity> chatList;
    private Tasks tasks=new Tasks();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar=findViewById(R.id.toolbar);
        initials=findViewById(R.id.initials);
        name=findViewById(R.id.chat_title);
        recyclerView=findViewById(R.id.chat_messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contact_id=getIntent().getIntExtra("contact_id",0);

        getChat();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Chat_Entity chat_entity=new Chat_Entity();
                chat_entity.setContact_id(1);
                chat_entity.setContact_name("Papa");
                chat_entity.setNote("Hey there");
                chat_entity.setRead_status(true);
                chat_entity.setSent_or_recieved(RECEIVED_MESSAGE);
                chat_entity.setTime(System.currentTimeMillis());
                chat_entity.setImage("https://cdn.pixabay.com/photo/2017/09/22/19/05/tomato-2776735_640.jpg");
                chatList.add(chat_entity);
                chatAdapter.notifyItemInserted(chatList.size());
            }
        },3000);
    }

    private void getChat() {
        ChatSync chatSync=new ChatSync(contact_id,((MyApplication)getApplicationContext()).getDatabase(),GET_CHAT);
        chatSync.setChatCallback(this);
        chatSync.execute();
    }

    @Override
    public void summariesFetched(ArrayList<ChatSummary> chatSummaries) {

    }

    @Override
    public void chatFetched(ArrayList<Chat_Entity> chatList) {
        //Toast.makeText(this, chatList.size()+"", Toast.LENGTH_SHORT).show();
        this.chatList=chatList;

        String nameString=chatList.get(0).getContact_name();
        name.setText(nameString);
        initials.setText(tasks.getInitial(nameString));

        chatAdapter=new ChatAdapter(this,chatList);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.scrollToPosition(chatList.size()-1);
    }
}
