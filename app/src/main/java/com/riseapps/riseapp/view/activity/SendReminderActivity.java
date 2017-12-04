package com.riseapps.riseapp.view.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Adapters.ContactsAdapter;
import com.riseapps.riseapp.executor.ContactsSync;
import com.riseapps.riseapp.executor.Interface.ContactCallback;
import com.riseapps.riseapp.executor.Interface.ContactSelection;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.model.DB.Contact_Entity;
import com.riseapps.riseapp.model.MyApplication;
import com.riseapps.riseapp.view.fragment.ShareReminder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.riseapps.riseapp.Components.AppConstants.GET_CONTACTS_FROM_DB;
import static com.riseapps.riseapp.Components.AppConstants.RESYNC_CONTACTS;

public class SendReminderActivity extends AppCompatActivity implements ContactSelection, View.OnClickListener,ContactCallback {

    private ArrayList<Contact_Entity> contactArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactsAdapter contactsAdapter;
    private int CONTACT_LOADER = 1;
    private Tasks tasks = new Tasks();
    private ArrayList<Integer> selected_positions = new ArrayList<>();
    //private TextView selected_count;
    private FloatingActionButton done;
    private String UID;
    private MyApplication myapp;
    private SharedPreferenceSingelton sharedPreferenceSingleton=new SharedPreferenceSingelton();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (tasks.getCurrentTheme(this) == 0) {
            setTheme(R.style.AppTheme2);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_send_reminder);
        myapp = (MyApplication) getApplicationContext();
        UID=getIntent().getStringExtra("UID");
        toolbar=findViewById(R.id.toolbar);
        //selected_count = findViewById(R.id.selection_count);
        done = findViewById(R.id.button_done);
        done.setOnClickListener(this);
        recyclerView = findViewById(R.id.contacts);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.inflateMenu(R.menu.contacts_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.sync){
                    ContactsSync contactsSync = new ContactsSync(SendReminderActivity.this,getMyapp().getDatabase(),RESYNC_CONTACTS);
                    contactsSync.setContactCallback((ContactCallback) SendReminderActivity.this);
                    contactsSync.execute();
                }
                return true;
            }
        });

        getContacts();
        contactsAdapter = new ContactsAdapter(this, contactArrayList);
        recyclerView.setAdapter(contactsAdapter);
    }

    public void getContacts()  {
        ContactsSync contactsSync = new ContactsSync(this,getMyapp().getDatabase(),GET_CONTACTS_FROM_DB);
        contactsSync.setContactCallback((ContactCallback) this);
        contactsSync.execute();
    }

    @Override
    public void onContactSelected(boolean selected, int position) {
        if (selected) {
            selected_positions.add(position);
            toolbar.setTitle(selected_positions.size() + " contacts selected");
            //selected_count.setText(selected_positions.size() + " contacts selected");
            done.show();
        } else {
            Integer value = position;
            selected_positions.remove(value);
            if (selected_positions.size() == 0) {
                //selected_count.setText("Select Contacts to remind");
                toolbar.setTitle("Select Contacts");
                done.hide();
            } else {
                toolbar.setTitle(selected_positions.size() + " contacts selected");
                //selected_count.setText(selected_positions.size() + " contacts selected");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button_done) {
            ShareReminder shareReminder = new ShareReminder();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.background, shareReminder, "SharedReminder");
            ft.addToBackStack(null);
            ft.commit();
            done.hide();
        }else {
            finish();
        }
    }

    public String getUID() {
        return UID;
    }

    public ArrayList<Contact_Entity> getSelectedContacts(){
        ArrayList<Contact_Entity> selected_contacts=new ArrayList<>();
        for(int i=0;i<selected_positions.size();i++){
            selected_contacts.add(contactArrayList.get(selected_positions.get(i)));
        }
        return selected_contacts;
    }

    public MyApplication getMyapp() {
        return myapp;
    }

    @Override
    public void onSuccessfulFetch(ArrayList<Contact_Entity> contacts,boolean restart_Async) {

        contactArrayList=contacts;
        contactsAdapter = new ContactsAdapter(this, contactArrayList);
        recyclerView.setAdapter(contactsAdapter);

        if(restart_Async){
            ContactsSync contactsSync = new ContactsSync(getMyapp().getDatabase(),2,contacts);
            contactsSync.execute();
            Toast.makeText(this, "Contacts synced", Toast.LENGTH_SHORT).show();
        }

        selected_positions.clear();
        toolbar.setTitle("Select Contacts");
        done.hide();
    }

    @Override
    public void onUnsuccessfulFetch() {

    }
}
