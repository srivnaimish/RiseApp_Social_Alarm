package com.riseapps.riseapp.view.activity;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Adapters.SectionPagerAdapter;
import com.riseapps.riseapp.executor.Interface.FabListener;
import com.riseapps.riseapp.executor.Interface.RingtonePicker;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.model.MyApplication;

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

        SectionPagerAdapter mSectionsPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2)
                    fab.hide();
                else if (position == 1) {
                    fab.setImageResource(R.drawable.ic_quill);
                    fab.show();
                } else if (position == 0) {
                    fab.setImageResource(R.drawable.ic_add_alarm);
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
                    iconId = R.drawable.menu_alarm;
                    break;
                case 1:
                    iconId = R.drawable.menu_feeds;
                    break;
                case 2:
                    iconId = R.drawable.menu_settings;
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
                    fabListener1.onFabClick();
                } else if (pos == 1) {
                    Intent intent=new Intent(this, SendReminderActivity.class);
                    intent.putExtra("UID",currentUser.getUid());
                    startActivity(intent);
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
}
