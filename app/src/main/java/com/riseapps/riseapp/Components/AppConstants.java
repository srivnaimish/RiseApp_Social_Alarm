package com.riseapps.riseapp.Components;

import android.widget.Button;

import com.riseapps.riseapp.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by naimish on 31/10/17.
 */

public class AppConstants {

    public static final int RC_SIGN_IN = 9001;
    public static final int RC_RINGTONE = 101;

    public static final String[] weekdays={"Mo","Tu","We","Th","Fr","Sa","Su"};

    public static final int[] daysButtons={R.id.sun,R.id.mon,R.id.tue,R.id.wed,R.id.thu,R.id.fri,R.id.sat};

    public static final int[] numberButtons={R.id.n0,R.id.n1,R.id.n2,R.id.n3,R.id.n4,R.id.n5,R.id.n6,R.id.n7,R.id.n8,R.id.n9,R.id.bck,R.id.clear_all};

    public static final String LOGIN = "login";
    public static final String UPDATE = "updateName";

    public static boolean isValidEmail(String email)
    {
        return email.contains("@");
    }

    public static final String BASE_URL = "http://192.168.1.101/";
}
