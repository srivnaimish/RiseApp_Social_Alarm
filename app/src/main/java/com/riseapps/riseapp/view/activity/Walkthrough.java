package com.riseapps.riseapp.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.executor.Tasks;

public class Walkthrough extends AppCompatActivity {

    private SharedPreferenceSingelton sharedPreferenceSingleton=new SharedPreferenceSingelton();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(new Tasks().getCurrentTheme(this)==0){
            setTheme(R.style.AppTheme2);
        }else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);
    }

    public void gotoMain(View view) {
        sharedPreferenceSingleton.saveAs(this,"Logged",true);
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
