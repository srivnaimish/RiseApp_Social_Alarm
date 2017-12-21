package com.riseapps.riseapp.view.ui.activity;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Interface.Filter;
import com.riseapps.riseapp.view.Adapters.SectionPagerAdapter;
import com.riseapps.riseapp.executor.ContactsSync;
import com.riseapps.riseapp.executor.Interface.FabListener;
import com.riseapps.riseapp.executor.Interface.RingtonePicker;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.model.MyApplication;
import com.riseapps.riseapp.view.ui.fragment.Settings;

import static com.riseapps.riseapp.Components.AppConstants.RC_RINGTONE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,PopupMenu.OnMenuItemClickListener {

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
    private ImageButton filter;
    private BottomNavigationView bottomNavigationView;
    private MenuItem prevMenuItem;
    private Filter filter_menu;

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

        //TabLayout tabLayout = findViewById(R.id.tablayout);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        SectionPagerAdapter mSectionsPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

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

        filter = findViewById(R.id.filter);
        filter.setOnClickListener(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    fab.setImageResource(R.drawable.ic_add_alarm);
                    filter.setVisibility(View.GONE);
                    fab.show();
                } else if (position == 1) {
                    fab.setImageResource(R.drawable.ic_quill);
                    filter.setVisibility(View.GONE);
                    fab.show();
                } else {
                    fab.hide();
                    filter.setVisibility(View.VISIBLE);
                }

                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.tab1:
                                mViewPager.setCurrentItem(0);
                                break;
                            case R.id.tab2:
                                mViewPager.setCurrentItem(1);
                                break;
                            case R.id.tab3:
                                mViewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });

        boolean reminder_clicked=getIntent().getBooleanExtra("Reminder Clicked",false);
        if(!reminder_clicked)
            mViewPager.setCurrentItem(1);

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
            case R.id.filter:
                PopupMenu popup = new PopupMenu(this,view);
                popup.setOnMenuItemClickListener(this);
                popup.inflate(R.menu.filter);
                popup.show();
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


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pending:
                filter_menu.filterByPending();
                return true;
            case R.id.today:
                filter_menu.filterByToday();
                return true;
            default:
                return false;
        }
    }

    public void addFilterClickListener(Filter filter_menu) {
        this.filter_menu = filter_menu;
    }
}
