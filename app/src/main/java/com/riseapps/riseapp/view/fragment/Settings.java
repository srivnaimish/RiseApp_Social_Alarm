package com.riseapps.riseapp.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.view.activity.MainActivity;

public class Settings extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "AUTH";
    private SharedPreferenceSingelton sharedPreferenceSingleton = new SharedPreferenceSingelton();
    private Dialog dialog;

    public static Settings newInstance() {
        return new Settings();
    }

    Tasks tasks = new Tasks();
    ImageView pic, method;
    TextView name, phone, initials;
    FirebaseUser firebaseUser;
    Switch theme_switch;
    CardView method_card, rate, share, theme;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        pic = view.findViewById(R.id.profile_pic);
        method = view.findViewById(R.id.setting_method_image);
        name = view.findViewById(R.id.name);
        phone = view.findViewById(R.id.phone);
        share = view.findViewById(R.id.setting_share);
        rate = view.findViewById(R.id.setting_rate);
        initials = view.findViewById(R.id.initials);
        theme_switch = view.findViewById(R.id.setting_theme_switch);
        theme = view.findViewById(R.id.setting_theme);
        method_card = view.findViewById(R.id.setting_alarm_method);


        method_card.setOnClickListener(this);
        share.setOnClickListener(this);
        rate.setOnClickListener(this);
        theme.setOnClickListener(this);
        theme_switch.setOnCheckedChangeListener(this);

        if (tasks.getCurrentTheme(getContext()) == 1) {
            theme_switch.setChecked(true);
        }

        assert ((MainActivity)getActivity()) != null;
        firebaseUser = ((MainActivity)getActivity()).currentUser;

        String username = sharedPreferenceSingleton.getSavedString(getContext(),"Name");
        name.setText(username);
        phone.setText(firebaseUser.getPhoneNumber());

        initials.setText(new Tasks().getInitial(username));


        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_alarm_method:
                method.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.vibrate));

                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.method_dialog);
                try {
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                } catch (Exception e) {
                }
                LinearLayout simple = dialog.findViewById(R.id.simple);
                LinearLayout math = dialog.findViewById(R.id.math);
                simple.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharedPreferenceSingleton.saveAs(getContext(), "Method", 0);
                        dialog.dismiss();
                    }
                });
                math.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharedPreferenceSingleton.saveAs(getContext(), "Method", 1);
                        dialog.dismiss();
                    }
                });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                }, 100);

                break;

            case R.id.setting_rate:
                break;
            case R.id.setting_share:
                break;
            case R.id.setting_theme:
                theme_switch.toggle();
                restartApp();
                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            sharedPreferenceSingleton.saveAs(getContext(), "Theme", 1);
        } else
            sharedPreferenceSingleton.saveAs(getContext(), "Theme", 0);
        //restartApp();
    }

    public void restartApp() {
        Intent i = getActivity().getBaseContext().getPackageManager().
                getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
        assert i != null;
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
