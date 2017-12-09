package com.riseapps.riseapp.view.activity;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Adapters.SectionPagerAdapter;
import com.riseapps.riseapp.executor.ContactsSync;
import com.riseapps.riseapp.executor.Interface.ContactCallback;
import com.riseapps.riseapp.executor.Interface.FabListener;
import com.riseapps.riseapp.executor.Interface.RingtonePicker;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.model.DB.Contact_Entity;
import com.riseapps.riseapp.model.MyApplication;
import com.riseapps.riseapp.view.fragment.Settings;

import java.util.ArrayList;

import static com.riseapps.riseapp.Components.AppConstants.RC_RINGTONE;
import static com.riseapps.riseapp.Components.AppConstants.RESYNC_CONTACTS;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ContactCallback {

    private static final String TAG = "SIGN IN";
    private ViewPager mViewPager;
    private RingtonePicker ringtonePicker;
    public FirebaseUser currentUser;
    public FloatingActionButton fab;
    private SharedPreferenceSingelton sharedPreferenceSingleton = new SharedPreferenceSingelton();
    private Tasks tasks = new Tasks();
    private FabListener fabListener1, fabListener2;
    private MyApplication myapp;
    private TextView toolbar_title;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (tasks.getCurrentTheme(this) == 0) {
            setTheme(R.style.AppTheme2);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myapp = (MyApplication) getApplicationContext();

        TabLayout tabLayout = findViewById(R.id.tablayout);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        toolbar_title=findViewById(R.id.toolbar_title);

        SectionPagerAdapter mSectionsPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        toolbar=findViewById(R.id.toolbar);

        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Settings settings = new Settings();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main_background, settings, "Settings");
                ft.addToBackStack(null);
                ft.commit();
                return false;
            }
        });
        // mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position==0){
                    toolbar_title.setText("Messages");
                }else if(position==1){
                    toolbar_title.setText("Personal Alarms");
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    fab.setImageResource(R.drawable.ic_add_alarm);
                    fab.show();
                } else if (position == 0) {
                    fab.setImageResource(R.drawable.ic_quill);
                    fab.show();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            int iconId = -1;
            switch (i) {
                case 0:
                    iconId = R.drawable.menu_feeds;
                    break;
                case 1:
                    iconId = R.drawable.menu_alarm;
                    break;
            }
            tabLayout.getTabAt(i).setIcon(iconId);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_RINGTONE && resultCode == RESULT_OK) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            ringtonePicker.onRingtonePicked(uri);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                int pos = mViewPager.getCurrentItem();
                if (pos == 0) {
                    Intent intent=new Intent(this, PickContacts.class);
                    startActivity(intent);
                } else if (pos == 1) {
                    fabListener1.onFabClick();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            currentUser = mAuth.getCurrentUser();
            myapp.setUID(currentUser.getUid());
        }
        if(!sharedPreferenceSingleton.getSavedBoolean(this,"Cached_Contacts")) {
            ContactsSync contactsSync = new ContactsSync(this,getMyapp().getDatabase(),RESYNC_CONTACTS);
            contactsSync.setContactCallback((ContactCallback) this);
            contactsSync.execute();

        }
    }


    public void setRingtonePicker(RingtonePicker ringtonePicker) {
        this.ringtonePicker = ringtonePicker;
    }

    public void setFabListener1(FabListener fabListener1) {
        this.fabListener1 = fabListener1;
    }

    public MyApplication getMyapp() {
        return myapp;
    }

    @Override
    public void onSuccessfulFetch(ArrayList<Contact_Entity> contacts,boolean restart_Async) {
        sharedPreferenceSingleton.saveAs(this,"Cached_Contacts",true);
        if(restart_Async) {
            Toast.makeText(this, "Contacts synced", Toast.LENGTH_SHORT).show();
            ContactsSync contactsSync = new ContactsSync(getMyapp().getDatabase(), 2, contacts);
            contactsSync.execute();
        }
    }

    @Override
    public void onUnsuccessfulFetch() {
        Toast.makeText(this, "Error Fetching contacts", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStackImmediate();
        else
        super.onBackPressed();
    }
}
