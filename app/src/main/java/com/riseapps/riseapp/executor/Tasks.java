package com.riseapps.riseapp.executor;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.model.Pojo.PersonalAlarm;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by naimish on 2/11/17.
 */

public class Tasks {

    private SharedPreferenceSingelton sharedPreferenceSingelton = new SharedPreferenceSingelton();

    public void savePersonalAlarms(Context context, ArrayList<PersonalAlarm> personalAlarms) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<PersonalAlarm>>() {
        }.getType();
        String cachedJSON = gson.toJson(personalAlarms, type);
        sharedPreferenceSingelton.saveAs(context, "Personal Cached", cachedJSON);
    }

    public ArrayList<PersonalAlarm> getPersonalAlarms(Context context) {
        String cachedJSON = sharedPreferenceSingelton.getSavedString(context, "Personal Cached");  //Fetch cached JSON string
        return new Gson().fromJson(cachedJSON, new TypeToken<ArrayList<PersonalAlarm>>() {
        }.getType());
    }

    public int getPxfromDp(Context context, int dp) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
    }

    public int getRandomInteger() {
        Random random = new Random();
        return random.nextInt(100);
    }

    public String getInitial(String s) {
        int spaceIndex = s.indexOf(' ');
        try {
            if (spaceIndex != -1) {
                return "" + s.charAt(0) + s.charAt(spaceIndex + 1);
            }
        } catch (Exception e) {
        }

        return "" + s.charAt(0);
    }

    public int getCurrentTheme(Context context) {
        if (sharedPreferenceSingelton.getSavedInt(context, "Theme") == 1)
            return 1;
        return 0;
    }

}
