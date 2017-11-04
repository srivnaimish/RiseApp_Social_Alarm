package com.riseapps.riseapp.executor;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riseapps.riseapp.model.PersonalAlarm;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by naimish on 2/11/17.
 */

public class Tasks {

    private SharedPreferenceSingelton sharedPreferenceSingelton=new SharedPreferenceSingelton();

    public void savePersonalAlarms(Context context,ArrayList<PersonalAlarm> personalAlarms){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<PersonalAlarm>>() {
        }.getType();
        String cachedJSON = gson.toJson(personalAlarms, type);
        sharedPreferenceSingelton.saveAs(context, "Personal Cached", cachedJSON);
    }

    public ArrayList<PersonalAlarm> getPersonalAlarms(Context context){
        String cachedJSON=sharedPreferenceSingelton.getSavedString(context, "Personal Cached");  //Fetch cached JSON string
        return new Gson().fromJson(cachedJSON, new TypeToken<ArrayList<PersonalAlarm>>() {
        }.getType());
    }

    public int getPxfromDp(Context context,int dp){
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
    }

}
