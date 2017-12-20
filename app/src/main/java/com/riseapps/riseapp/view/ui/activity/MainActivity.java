package com.riseapps.riseapp.view.ui.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.SyncReciever;
import com.riseapps.riseapp.view.Adapters.SectionPagerAdapter;
import com.riseapps.riseapp.executor.ContactsSync;
import com.riseapps.riseapp.executor.Interface.FabListener;
import com.riseapps.riseapp.executor.Interface.RingtonePicker;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.model.MyApplication;
import com.riseapps.riseapp.view.ui.fragment.Settings;

import java.util.Calendar;

import static com.riseapps.riseapp.Components.AppConstants.RC_RINGTONE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SIGN IN";
    private ViewPager mViewPager;
    private RingtonePicker ringtonePicker;
    public FirebaseUser currentUser;
    public FloatingActionButton fab;
    private SharedPreferenceSingelton sharedPreferenceSingleton = new SharedPreferenceSingelton();
    private Tasks tasks = new Tasks();
    private FabListener fabListener1;
    private MyApplication myapp;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*if (tasks.getCurrentTheme(this) == 0) {
            setTheme(R.style.AppTheme2);
        } else {
            setTheme(R.style.AppTheme);
        }*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myapp = (MyApplication) getApplicationContext();

        TabLayout tabLayout = findViewById(R.id.tablayout);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        SectionPagerAdapter mSectionsPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        boolean reminder_clicked=getIntent().getBooleanExtra("Reminder Clicked",false);
        if(!reminder_clicked)
        mViewPager.setCurrentItem(1);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.settings);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Settings settings = new Settings();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.replace(R.id.main_background, settings, "Settings");
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    fab.setImageResource(R.drawable.ic_add_alarm);
                    fab.show();
                } else if (position == 1) {
                    fab.setImageResource(R.drawable.ic_quill);
                    fab.show();
                } else {
                    fab.hide();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_pending);
        tabLayout.getTabAt(1).setIcon(R.drawable.tab_chat);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_time);

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
                if (pos == 1) {
                    Intent intent=new Intent(this, PickContacts.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.view_enter,R.anim.view_exit);
                } else if (pos == 2) {
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

        if(sharedPreferenceSingleton.getSavedBoolean(this,"Clear")){
            myapp.clearMemory();
            sharedPreferenceSingleton.saveAs(this,"Cache",false);
        }

        if(!sharedPreferenceSingleton.getSavedBoolean(this,"Cached_Contacts")) {
            ContactsSync contactsSync = new ContactsSync(getContentResolver(),getMyapp().getDatabase());
            contactsSync.execute();
            Log.d("MainActivity","Syncing");
            sharedPreferenceSingleton.saveAs(this,"Cached_Contacts",true);
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
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStackImmediate();
        else
        super.onBackPressed();
    }

}
