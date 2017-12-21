package com.riseapps.riseapp.view.ui.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.executor.Tasks;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferenceSingelton sharedPreferenceSingleton = new SharedPreferenceSingelton();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*if (new Tasks().getCurrentTheme(this) == 0) {
            setTheme(R.style.AppTheme2);
        } else {
            setTheme(R.style.AppTheme);
        }*/
        super.onCreate(savedInstanceState);
        NotificationManager nManager = ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));
        nManager.cancelAll();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPreferenceSingleton.getSavedBoolean(SplashActivity.this, "Logged")) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, Walkthrough.class));
                    finish();
                }
            }
        },100);

    }
}
