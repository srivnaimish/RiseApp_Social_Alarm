package com.riseapps.riseapp.executor;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.riseapps.riseapp.Components.AppConstants;
import com.riseapps.riseapp.executor.Interface.ContactCallback;
import com.riseapps.riseapp.executor.Network.RequestInterface;
import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.DB.Contact_Entity;
import com.riseapps.riseapp.model.DB.MyDB;
import com.riseapps.riseapp.model.Pojo.Server.ContactRequest;
import com.riseapps.riseapp.model.Pojo.Server.ContactsResponse;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.riseapps.riseapp.Components.AppConstants.GET_CONTACTS;
import static com.riseapps.riseapp.Components.AppConstants.GET_CONTACTS_FROM_DB;
import static com.riseapps.riseapp.Components.AppConstants.RESYNC_CONTACTS;

/**
 * Created by naimish on 29/11/17.
 */

public class ChatSync extends AsyncTask<Void, Void, Void> {

    private MyDB myDB;
    private Tasks tasks=new Tasks();

    public ChatSync(Activity activity, MyDB myDB, int choice) {
        this.myDB=myDB;

    }

    @Override
    protected Void doInBackground(Void... voids) {

        return null;
    }


}