package com.riseapps.riseapp.view.ui.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.view.Adapters.ContactsAdapter;
import com.riseapps.riseapp.executor.ContactsSync;
import com.riseapps.riseapp.executor.Interface.ContactCallback;
import com.riseapps.riseapp.executor.Interface.ContactSelection;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.model.DB.Contact_Entity;
import com.riseapps.riseapp.model.MyApplication;
import com.riseapps.riseapp.view.ui.fragment.ShareReminder;
import com.riseapps.riseapp.viewModel.ChatViewModel;
import com.riseapps.riseapp.viewModel.ContactsViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.riseapps.riseapp.Components.AppConstants.GET_CONTACTS_FROM_DB;
import static com.riseapps.riseapp.Components.AppConstants.INSERT_CONTACTS_IN_DB;
import static com.riseapps.riseapp.Components.AppConstants.RESYNC_CONTACTS;

public class PickContacts extends AppCompatActivity implements ContactSelection, View.OnClickListener {

    //private ArrayList<Contact_Entity> contactArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactsAdapter contactsAdapter;
    private int CONTACT_LOADER = 1;
    private Tasks tasks = new Tasks();
    private ArrayList<Integer> selected_positions = new ArrayList<>();
    //private TextView selected_count;
    private ContactsViewModel contactsViewModel;
    private FloatingActionButton done;
    private String UID;
    private MyApplication myapp;
    private SharedPreferenceSingelton sharedPreferenceSingleton=new SharedPreferenceSingelton();
    private Toolbar toolbar;
    private ConstraintLayout empty_state;
    private Button invite;
    private RelativeLayout loading_screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (tasks.getCurrentTheme(this) == 0) {
            setTheme(R.style.AppTheme2);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_pick_contacts);
        UID=getMyapp().getUID();
        toolbar=findViewById(R.id.toolbar);
        empty_state=findViewById(R.id.empty_state);
        loading_screen=findViewById(R.id.loading_screen);
        invite=findViewById(R.id.invite);
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
                    ContactsSync contactsSync = new ContactsSync(getContentResolver(),getMyapp().getDatabase());
                    contactsSync.execute();
                    Log.d("PickContact","Syncing");
                    loading_screen.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        //getContacts();
        contactsAdapter = new ContactsAdapter(this, new ArrayList<Contact_Entity>());
        recyclerView.setAdapter(contactsAdapter);

        contactsViewModel= ViewModelProviders.of(this).get(ContactsViewModel.class);

        contactsViewModel.getContactList(getMyapp().getDatabase()).observe(PickContacts.this, observer);


        invite.setOnClickListener(this);
    }

    Observer<List<Contact_Entity>> observer=new Observer<List<Contact_Entity>>() {
        @Override
        public void onChanged(@Nullable List<Contact_Entity> contact_entities) {
            contactsAdapter.addItems(contact_entities);
            Log.d("Size",""+contact_entities.size());
            loading_screen.setVisibility(View.GONE);
            if (contact_entities.size()>0){
                empty_state.setVisibility(View.GONE);
            }
            selected_positions.clear();
            toolbar.setTitle("Select Contacts");
            done.hide();
        }
    };


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
        }else if(v.getId()==R.id.invite){
            String message = "Let's share reminders with each others .\n\nhttps://play.google.com/store/apps/details?id=com.riseapps.riseapp";
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(share, "Invite via.."));
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
            selected_contacts.add(contactsViewModel.getContacts().get(selected_positions.get(i)));
        }
        return selected_contacts;
    }

    public MyApplication getMyapp() {
        return (MyApplication) getApplicationContext();
    }

}