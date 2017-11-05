package com.riseapps.riseapp.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.riseapps.riseapp.Components.AppConstants;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Adapters.SectionPagerAdapter;
import com.riseapps.riseapp.executor.Interface.ToggleShareDialog;
import com.riseapps.riseapp.executor.Network.RequestInterface;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.model.LoginRequest;
import com.riseapps.riseapp.model.LoginResponse;
import com.riseapps.riseapp.model.User;
import com.riseapps.riseapp.utils.ZoomOutPageTransformer;
import com.riseapps.riseapp.view.fragment.ShareReminder;

import java.util.Arrays;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.riseapps.riseapp.Components.AppConstants.RC_SIGN_IN;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ToggleShareDialog {

    private static final String TAG = "SIGN IN";
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    CardView sign_in_dialog;
    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    private SharedPreferenceSingelton sharedPreferenceSingleton=new SharedPreferenceSingelton();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tablayout);

        SectionPagerAdapter mSectionsPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());


        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this,R.color.colorAccent));
        tabLayout.setSelectedTabIndicatorHeight(3);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            int iconId = -1;
            switch (i) {
                case 0:
                    iconId = R.drawable.ic_alarm_clock;
                    break;
                case 1:
                    iconId = R.drawable.ic_sharing;
                    break;
                case 2:
                    iconId = R.drawable.ic_umbrella;
                    break;
                case 3:
                    iconId = R.drawable.ic_settings;
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
        }
    }

    private void loginUserOnServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Log.d(TAG,currentUser.getUid());
        Log.d(TAG,currentUser.getPhoneNumber());
        LoginRequest loginRequest=new LoginRequest();
        loginRequest.setOperation(AppConstants.LOGIN);
        User user=new User(currentUser.getUid(),currentUser.getPhoneNumber());
        loginRequest.setUser(user);

        Call<LoginResponse> response = requestInterface.operation(loginRequest);
        response.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                LoginResponse resp = response.body();
                assert resp != null;
                if(resp.getResult().equalsIgnoreCase("Success")) {
                    Toast.makeText(MainActivity.this, "" + resp.getMessage(), Toast.LENGTH_SHORT).show();
                    sharedPreferenceSingleton.saveAs(MainActivity.this, "UserName", resp.getName());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                Snackbar.make(mViewPager, t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

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
                                            new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
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
}
