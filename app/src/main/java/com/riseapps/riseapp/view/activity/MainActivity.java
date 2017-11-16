package com.riseapps.riseapp.view.activity;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.riseapps.riseapp.Components.AppConstants;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Adapters.SectionPagerAdapter;
import com.riseapps.riseapp.executor.Interface.FabListener;
import com.riseapps.riseapp.executor.Interface.RingtonePicker;
import com.riseapps.riseapp.executor.Interface.ToggleShareDialog;
import com.riseapps.riseapp.executor.Network.RequestInterface;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.model.MyApplication;
import com.riseapps.riseapp.model.Pojo.LoginRequest;
import com.riseapps.riseapp.model.Pojo.ServerResponse;
import com.riseapps.riseapp.model.Pojo.User;
import com.riseapps.riseapp.view.fragment.ShareReminder;

import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.riseapps.riseapp.Components.AppConstants.RC_RINGTONE;
import static com.riseapps.riseapp.Components.AppConstants.RC_SIGN_IN;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ToggleShareDialog {

    private static final String TAG = "SIGN IN";
    private ViewPager mViewPager;
    private RingtonePicker ringtonePicker;
    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    public FloatingActionButton fab;
    private SharedPreferenceSingelton sharedPreferenceSingleton=new SharedPreferenceSingelton();
    private FabListener fabListener1,fabListener2;
    private MyApplication myapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myapp= (MyApplication) getApplicationContext();

        TabLayout tabLayout = findViewById(R.id.tablayout);
        fab=findViewById(R.id.fab);
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
                if(position==2)
                    fab.hide();
                else if(position==1){
                    fab.setImageResource(R.drawable.ic_quill);
                    fab.show();
                }else if(position==0){
                    fab.setImageResource(R.drawable.ic_add);
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
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                currentUser=mAuth.getCurrentUser();
                FirebaseMessaging.getInstance().subscribeToTopic(currentUser.getUid());
                loginUserOnServer();
                //Toast.makeText(this, "Approved", Toast.LENGTH_SHORT).show();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Log.e("Login","Login canceled by User");
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Log.e("Login","No Internet Connection");
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.e("Login","Unknown Error");
                    return;
                }
            }
            Log.e("Login","Unknown sign in response");
        }else if(requestCode==RC_RINGTONE && resultCode == RESULT_OK){
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            ringtonePicker.onRingtonePicked(uri);
        }
    }

    private void loginUserOnServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        LoginRequest loginRequest=new LoginRequest();
        loginRequest.setOperation(AppConstants.LOGIN);
        User user=new User(currentUser.getUid(), currentUser.getDisplayName(), currentUser.getEmail());
        loginRequest.setUser(user);

        Call<ServerResponse> response = requestInterface.operation(loginRequest);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                assert resp != null;
                if(resp.getResult().equalsIgnoreCase("Success")) {
                    Toast.makeText(MainActivity.this, resp.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Snackbar.make(mViewPager, t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                int pos=mViewPager.getCurrentItem();
                if(pos==0) {
                    fabListener1.onFabClick();
                }

                else if(pos==1) {
                    fabListener2.onFabClick();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            currentUser=mAuth.getCurrentUser();
        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setTheme(R.style.AppTheme)
                            .setAvailableProviders(
                                    Collections.singletonList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                                    ))
                            .build(),
                    RC_SIGN_IN);
        }

    }


    @Override
    public void toggleVisibility() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStackImmediate();
        else {
            ShareReminder shareReminder=new ShareReminder();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.background, shareReminder, "SharedReminder");
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStackImmediate();
        else
            super.onBackPressed();
    }

    public void setRingtonePicker(RingtonePicker ringtonePicker) {
        this.ringtonePicker = ringtonePicker;
    }

    public void setFabListener1(FabListener fabListener1) {
        this.fabListener1 = fabListener1;
    }

    public void setFabListener2(FabListener fabListener2) {
        this.fabListener2 = fabListener2;
    }

    public MyApplication getMyapp() {
        return myapp;
    }
}
