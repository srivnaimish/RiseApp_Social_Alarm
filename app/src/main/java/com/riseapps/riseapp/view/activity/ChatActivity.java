package com.riseapps.riseapp.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.riseapps.riseapp.R;

import java.util.Calendar;

public class ChatActivity extends AppCompatActivity {

    private EditText  edit_note, edit_image;
    private TextView time, date;
    private Calendar calendar;
    private ImageButton send1,send2;
    private RecyclerView recyclerView;
    private TextView initials,name;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }
}
