package com.riseapps.riseapp.Components;

import android.content.Context;
import android.widget.Toast;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Network.RequestInterface;
import com.riseapps.riseapp.model.Message;
import com.riseapps.riseapp.model.MessageRequest;
import com.riseapps.riseapp.model.ServerResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    public static final String REMINDER = "reminder";

    public static final String BASE_URL = "http://192.168.1.101/";

    public void sendReminder(final Context context, String sender, ArrayList<String> Recipients, long Time, String Note, String ImageURL) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        MessageRequest messageRequest=new MessageRequest();
        messageRequest.setOperation(AppConstants.REMINDER);

        Message message=new Message(sender,Recipients,Time,Note,ImageURL);
        messageRequest.setMessage(message);

        Call<ServerResponse> response = requestInterface.chat(messageRequest);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                assert resp != null;
                if(resp.getResult().equalsIgnoreCase("Success")) {
                    Toast.makeText(context, resp.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
               // Snackbar.make(edit_image, t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
