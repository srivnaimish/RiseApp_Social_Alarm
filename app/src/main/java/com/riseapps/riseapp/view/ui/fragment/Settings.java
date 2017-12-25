package com.riseapps.riseapp.view.ui.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.riseapps.riseapp.Components.AppConstants;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.billing.IabHelper;
import com.riseapps.riseapp.executor.Network.RequestInterface;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.executor.Utils;
import com.riseapps.riseapp.model.Pojo.Server.ServerResponse;
import com.riseapps.riseapp.view.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;


public class Settings extends Fragment implements View.OnClickListener {

    private static final int REQUEST_PERMISSION = 0;
    private SharedPreferenceSingelton sharedPreferenceSingleton = new SharedPreferenceSingelton();
    private Dialog dialog;
    private static final String TAG = "In-App Billing";

    boolean billinSupported = false;

    ImageView pic, method;
    TextView name,initials, phone;
    FirebaseUser firebaseUser;
    CardView method_card, rate, share,privacy_policy,support;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        pic = view.findViewById(R.id.profile_pic);
        method = view.findViewById(R.id.setting_method_image);
        name = view.findViewById(R.id.name);
        initials=view.findViewById(R.id.initials);
        phone = view.findViewById(R.id.phone);
        share = view.findViewById(R.id.setting_share);
        rate = view.findViewById(R.id.setting_rate);
        privacy_policy=view.findViewById(R.id.setting_privacy);
        method_card = view.findViewById(R.id.setting_alarm_method);
        support=view.findViewById(R.id.setting_support);


        method_card.setOnClickListener(this);
        privacy_policy.setOnClickListener(this);
        support.setOnClickListener(this);
        share.setOnClickListener(this);
        rate.setOnClickListener(this);

        assert (getActivity()) != null;
        firebaseUser = ((MainActivity)getActivity()).currentUser;

        String username = firebaseUser.getDisplayName();
        name.setText(username);
        phone.setText(firebaseUser.getPhoneNumber());
        initials.setText(new Utils().getInitial(username));


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

            case R.id.setting_privacy:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.POLICY_URL));
                startActivity(browserIntent);
                break;

            case R.id.setting_support:
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.support_dialog);
                try {
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                } catch (Exception e) {
                }
                dialog.findViewById(R.id.support).setOnClickListener(this);
                dialog.show();
                break;

            case R.id.setting_rate:
                break;
            case R.id.setting_share:
                String message = "Checkout RiseApp.Simplest way to send reminders to people.\n\nhttps://play.google.com/store/apps/details?id=com.riseapps.riseapp";
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(share, "Invite via.."));
                break;

            case R.id.support:

                break;

        }
    }

}
