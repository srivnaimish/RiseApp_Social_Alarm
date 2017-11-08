package com.riseapps.riseapp.view.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.riseapps.riseapp.Components.AppConstants;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Network.RequestInterface;
import com.riseapps.riseapp.executor.SharedPreferenceSingelton;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.model.LoginRequest;
import com.riseapps.riseapp.model.LoginResponse;
import com.riseapps.riseapp.model.User;
import com.riseapps.riseapp.view.activity.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Settings extends Fragment implements View.OnClickListener{

    private static final String TAG = "AUTH";
    private CardView edit_dialog;
    private SharedPreferenceSingelton sharedPreferenceSingleton=new SharedPreferenceSingelton();

    public static Settings newInstance() {
        return new Settings();
    }

    ImageButton edit;
    EditText editname;
    Button done;
    TextView name,phone,initials;
    FirebaseUser firebaseUser;

    CardView rate,share;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_settings, container, false);

        name=view.findViewById(R.id.name);
        phone=view.findViewById(R.id.phone);
        edit=view.findViewById(R.id.edit);
        share=view.findViewById(R.id.setting_share);
        rate=view.findViewById(R.id.setting_rate);
        edit_dialog=view.findViewById(R.id.edit_name_card);
        editname=view.findViewById(R.id.edit_name);
        done=view.findViewById(R.id.button_done);
        initials=view.findViewById(R.id.initials);

        share.setOnClickListener(this);
        rate.setOnClickListener(this);
        edit.setOnClickListener(this);
        done.setOnClickListener(this);

        firebaseUser=((MainActivity)getActivity()).currentUser;
        String name=sharedPreferenceSingleton.getSavedString(getContext(),"UserName");
        if(name!=null)
        changeNameAndInitials(name);
        else
            changeNameAndInitials("User");
        //name.setText(firebaseUser.getDisplayName());
        phone.setText(firebaseUser.getPhoneNumber());

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit:
                edit_dialog.setVisibility(View.VISIBLE);
                break;
            case R.id.button_done:
                final String s=editname.getText().toString();
                changeNameAndInitials(s);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(AppConstants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RequestInterface requestInterface = retrofit.create(RequestInterface.class);
                LoginRequest loginRequest=new LoginRequest();
                loginRequest.setOperation(AppConstants.UPDATE);
                User user=new User(firebaseUser.getUid(),s);
                loginRequest.setUser(user);

                Call<LoginResponse> response = requestInterface.operation(loginRequest);
                response.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                        LoginResponse resp = response.body();
                        assert resp != null;
                        if(resp.getResult().equalsIgnoreCase("Success")) {
                            Toast.makeText(getContext(), "" + resp.getMessage(), Toast.LENGTH_SHORT).show();
                            sharedPreferenceSingleton.saveAs(getContext(), "UserName", s);
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {

                        Snackbar.make(share, t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });


            case R.id.setting_rate:
                break;
            case R.id.setting_share:
                break;
        }
    }

    public void changeNameAndInitials(String s){
        if(s.length()>3) {
            initials.setText(new Tasks().getInitials(s));
            name.setText(s);
            edit_dialog.setVisibility(View.GONE);
        }else {
            Toast.makeText(getContext(), "Not Valid Name", Toast.LENGTH_SHORT).show();
        }
    }

}
