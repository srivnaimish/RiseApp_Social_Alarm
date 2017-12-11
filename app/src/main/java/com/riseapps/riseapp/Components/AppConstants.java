package com.riseapps.riseapp.Components;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Network.RequestInterface;
import com.riseapps.riseapp.model.Pojo.Server.Message;
import com.riseapps.riseapp.model.Pojo.Server.MessageRequest;
import com.riseapps.riseapp.model.Pojo.Server.ServerResponse;

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

    public static final int GET_CONTACTS_FROM_DB = 0;
    public static final int RESYNC_CONTACTS = 1;
    public static final int INSERT_CONTACTS_IN_DB= 2;

    public static final int GET_CHAT_SUMMARIES=0;
    public static final int GET_CHAT=1;
    public static final int INSERT_NEW_CHAT=2;
    public static final int DELETE_CHAT=3;

    public static final int SENT_MESSAGE=0;
    public static final int RECEIVED_MESSAGE =1;

    public static final String[] weekdays = {"Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"};

    public static final int[] daysButtons = {R.id.sun, R.id.mon, R.id.tue, R.id.wed, R.id.thu, R.id.fri, R.id.sat};

    public static final int[] numberButtons = {R.id.n0, R.id.n1, R.id.n2, R.id.n3, R.id.n4, R.id.n5, R.id.n6, R.id.n7, R.id.n8, R.id.n9, R.id.bck, R.id.clear_all};

    public static final String LOGIN = "login";
    public static final String REMINDER = "reminder";
    public static final String GET_CONTACTS = "GET_CONTACTS";

    public static final String BASE_URL = "http://riseapp.000webhostapp.com/";

    public void sendReminder(String sender, ArrayList<String> Recipients, long Time, String Note, String ImageURL) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setOperation(AppConstants.REMINDER);

        Message message = new Message(sender, Recipients, Time, Note, ImageURL);
        messageRequest.setMessage(message);
        Call<ServerResponse> response = requestInterface.chat(messageRequest);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                assert resp != null;
                Log.d(REMINDER, resp.getResult());
                if (resp.getResult().equalsIgnoreCase("Success")) {
                    Log.d(REMINDER, resp.getMessage());
                    //Toast.makeText(context, resp.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                // Snackbar.make(get, t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void sendReminderToSingleUser(String sender, String Recipient, long Time, String Note, String ImageURL) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setOperation(AppConstants.REMINDER);

        ArrayList<String> Recipients=new ArrayList<>();
        Recipients.add(Recipient);

        Message message = new Message(sender, Recipients, Time, Note, ImageURL);
        messageRequest.setMessage(message);
        Call<ServerResponse> response = requestInterface.chat(messageRequest);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                assert resp != null;
                Log.d(REMINDER, resp.getResult());
                if (resp.getResult().equalsIgnoreCase("Success")) {
                    Log.d(REMINDER, resp.getMessage());
                    //Toast.makeText(context, resp.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                // Snackbar.make(get, t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }


}
