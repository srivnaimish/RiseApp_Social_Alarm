package com.riseapps.riseapp.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.executor.Tasks;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferenceSingelton sharedPreferenceSingleton=new SharedPreferenceSingelton();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(new Tasks().getCurrentTheme(this)==0){
            setTheme(R.style.AppTheme2);
        }else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(sharedPreferenceSingleton.getSavedBoolean(this,"Logged")){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }else {
            startActivity(new Intent(this,Walkthrough.class));
            finish();
        }
    }
}
