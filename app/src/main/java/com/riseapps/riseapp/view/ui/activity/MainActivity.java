package com.riseapps.riseapp.view.ui.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    private FabListener fabListener1, fabListener2;
    private MyApplication myapp;
    private TextView toolbar_title;
    private Toolbar toolbar;
    private TabLayout tabLayout;

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

        tabLayout = findViewById(R.id.tablayout);
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
                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.replace(R.id.main_background, settings, "Settings");
                ft.addToBackStack(null);
                ft.commit();
                return false;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
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
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
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
                    overridePendingTransition(R.anim.view_enter,R.anim.view_exit);
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
            ContactsSync contactsSync = new ContactsSync(getContentResolver(),getMyapp().getDatabase());
            contactsSync.execute();
            Log.d("MainActivity","Syncing");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 9);

            AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, SyncReciever.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 123, intent, 0);

            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);

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

    public View getTabView(int position) {
        View tab = LayoutInflater.from(MainActivity.this).inflate(R.layout.customtab, null);
        TextView tv = (TextView) tab.findViewById(R.id.tab_text);
        if(position==0) {
            tv.setText("Messages");
        }
        else {
            tv.setText("Alarms");
        }
        return tab;
    }
}
