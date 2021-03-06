package com.riseapps.riseapp.view.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.riseapps.riseapp.Components.AppConstants;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Network.RequestInterface;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.executor.Utils;
import com.riseapps.riseapp.model.Pojo.Server.LoginRequest;
import com.riseapps.riseapp.model.Pojo.Server.ServerResponse;
import com.riseapps.riseapp.model.Pojo.Server.User;

import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.riseapps.riseapp.Components.AppConstants.RC_SIGN_IN;

public class Walkthrough extends AppCompatActivity {

    private SharedPreferenceSingelton sharedPreferenceSingleton = new SharedPreferenceSingelton();
    private static final int REQUEST_PERMISSION = 0;
    String[] permissionsRequired = new String[]{Manifest.permission.READ_CONTACTS};
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private CardView name_card;
    private EditText editText;
    private ImageView initial_background;
    private TextView initials;
    private int[] buttons = {R.id.b1, R.id.b2, R.id.b3};
    private ViewPager viewPager;
    private Button start;
    private int currentAPIVersion;
    private FloatingActionButton next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);
        name_card = findViewById(R.id.cardView);
        editText = findViewById(R.id.edit_name);
        initial_background = findViewById(R.id.bell);
        initials = findViewById(R.id.initials);
        viewPager = findViewById(R.id.viewpager);
        start = findViewById(R.id.start);
        findViewById(buttons[0]).setSelected(true);
        viewPager.setAdapter(new CustomViewPagerAdapter());
        viewPager.setOffscreenPageLimit(2);
        next = findViewById(R.id.next);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < 3; i++) {
                    findViewById(buttons[i]).setSelected(false);
                }
                findViewById(buttons[position]).setSelected(true);
                if (position == 2) {
                    start.setVisibility(View.VISIBLE);
                    next.hide();
                } else {
                    next.show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        currentAPIVersion = Build.VERSION.SDK_INT;

        if (currentAPIVersion < android.os.Build.VERSION_CODES.M) {
            start.setText(getString(R.string.verify));
        }
    }

    public void buttonClicked(View view) {      //Step 1
        if (Utils.isConnectedToNetwork(this)) {
            if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
                checkPermission();
            } else {
                startVerification();
            }
        } else
            Snackbar.make(viewPager, "Not connected to the internet", Snackbar.LENGTH_SHORT).show();
    }

    public void checkPermission() {

        if (ContextCompat.checkSelfPermission(this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0])) {
                Snackbar.make(findViewById(android.R.id.content),
                        R.string.permission_rational,
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(Walkthrough.this,
                                        permissionsRequired,
                                        REQUEST_PERMISSION);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(this, permissionsRequired, REQUEST_PERMISSION);
            }
        } else {
            startVerification();
        }
    }         //Step 2

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startVerification();
                } else {
                    Snackbar.make(viewPager, R.string.permission_rational,
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction(android.R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(Walkthrough.this,
                                            permissionsRequired,
                                            REQUEST_PERMISSION);
                                }
                            }).show();
                }
                break;
        }
    }

    //Step 3
    public void startVerification() {
        mAuth = FirebaseAuth.getInstance();
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

    //Step 4
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                currentUser = mAuth.getCurrentUser();
                FirebaseMessaging.getInstance().subscribeToTopic(currentUser.getUid());
                if (currentUser.getDisplayName() == null)
                    viewCardView();
                else
                    //Toast.makeText(this, ""+currentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                    loginUserOnServer();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, "Login Canceled", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.e("Login", "Unknown Error");
                    return;
                }
            }
            Log.e("Login", "Unknown sign in response");
        }
    }

    //Step 5
    private void viewCardView() {
        name_card.setVisibility(View.VISIBLE);
        editText.requestFocus();
    }

    //Step 6
    public void doneAll(final View view) {
        if (editText.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        } else {
            final String Name = editText.getText().toString();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(Name)
                    .build();
            currentUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            initials.setText(new Utils().getInitial(Name));
                            initial_background.startAnimation(AnimationUtils.loadAnimation(Walkthrough.this, R.anim.selection));
                            initial_background.setVisibility(View.VISIBLE);
                            view.setClickable(false);
                            loginUserOnServer();
                        }
                    });

            //;
        }
    }

    //Step 7
    private void loginUserOnServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setOperation(AppConstants.LOGIN);
        User user = new User(currentUser.getUid(), currentUser.getDisplayName(), currentUser.getPhoneNumber());
        loginRequest.setUser(user);

        Call<ServerResponse> response = requestInterface.operation(loginRequest);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                assert resp != null;
                Toast.makeText(Walkthrough.this, resp.getMessage(), Toast.LENGTH_SHORT).show();
                gotoMain();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(Walkthrough.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Step 8
    public void gotoMain() {
        editText.clearFocus();
        sharedPreferenceSingleton.saveAs(this, "Logged", true);
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.view_enter, R.anim.view_exit);
        finish();
    }

    //Step 9
    public void nextPage(View view) {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }


    private class CustomViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = getLayoutInflater().inflate(R.layout.pageritem, container, false);
            ImageView displayImage = (ImageView) view.findViewById(R.id.image);
            TextView heading = (TextView) view.findViewById(R.id.heading);
            TextView subheading = (TextView) view.findViewById(R.id.subheading);
            switch (position) {
                case 0:
                    displayImage.setImageResource(R.drawable.ic_walkthrough1);
                    heading.setText(getString(R.string.walkthrough_heading_1));
                    subheading.setText(getString(R.string.walkthrough_subheading_1));
                    break;
                case 1:
                    displayImage.setImageResource(R.drawable.ic_walkthrough2);
                    heading.setText(getString(R.string.walkthrough_heading_2));
                    subheading.setText(getString(R.string.walkthrough_subheading_2));
                    break;
                case 2:
                    displayImage.setImageResource(R.drawable.ic_walkthrough3);
                    heading.setText(getString(R.string.walkthrough_heading_3));
                    subheading.setText(getString(R.string.walkthrough_subheading_3));
                    break;
            }

            container.addView(view);
            return view;
        }
    }
}
