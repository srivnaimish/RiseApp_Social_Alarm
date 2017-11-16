package com.riseapps.riseapp.view.fragment;

import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.view.activity.MainActivity;

public class Settings extends Fragment implements View.OnClickListener{

    private static final String TAG = "AUTH";
    private SharedPreferenceSingelton sharedPreferenceSingleton=new SharedPreferenceSingelton();

    public static Settings newInstance() {
        return new Settings();
    }

    ImageView pic;
    TextView name,email,initials;
    FirebaseUser firebaseUser;

    CardView rate,share;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_settings, container, false);

        pic=view.findViewById(R.id.profile_pic);
        name=view.findViewById(R.id.name);
        email=view.findViewById(R.id.email);
        share=view.findViewById(R.id.setting_share);
        rate=view.findViewById(R.id.setting_rate);
        initials=view.findViewById(R.id.initials);
        share.setOnClickListener(this);
        rate.setOnClickListener(this);

        firebaseUser=((MainActivity)getActivity()).currentUser;
        String username=firebaseUser.getDisplayName();
        name.setText(username);
        email.setText(firebaseUser.getEmail());

        initials.setText(new Tasks().getInitial(username));
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.setting_rate:
                break;
            case R.id.setting_share:
                break;
        }
    }


}
