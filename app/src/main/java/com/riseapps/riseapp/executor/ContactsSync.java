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
import com.riseapps.riseapp.model.Pojo.Contact;
import com.riseapps.riseapp.model.Pojo.Server.ContactRequest;
import com.riseapps.riseapp.model.Pojo.Server.ContactsResponse;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.riseapps.riseapp.Components.AppConstants.GET_CONTACTS;

/**
 * Created by naimish on 29/11/17.
 */

public class ContactsSync extends AsyncTask<Void, Void, Void> {

    private ContactCallback contactCallback;
    private WeakReference<Activity> activity;
    private ArrayList<Contact> allContactsList,riseappContacts;     //Fetch all feeds
    private Tasks tasks=new Tasks();

    public ContactsSync(Activity activity) {
        this.activity=new WeakReference<Activity>(activity);
        allContactsList=new ArrayList<>();
        riseappContacts=new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        getContacts();
        return null;
    }

    private void getContacts() {     //Fetch all Contacts from phone

        Uri CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " COLLATE LOCALIZED ASC";
        final String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        ArrayList<String> numberList=new ArrayList<>();

        ContentResolver cr = activity.get().getContentResolver();
        Cursor data = cr.query(CONTENT_URI, PROJECTION, null, null, sortOrder);
        while (data.moveToNext()) {
            String name = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY));
            String number = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            allContactsList.add(new Contact(null,name, number,false));     // fetch all contacts from phone
            numberList.add(number);     //get number list to send to server for verification
        }
        data.close();

        getRiseAppContacts(numberList);

        }

    public void getRiseAppContacts(ArrayList<String> numberList){   //get RiseApp Contacts from Server
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        ContactRequest contactRequest=new ContactRequest();
        contactRequest.setOperation(GET_CONTACTS);
        contactRequest.setPhones(numberList);

        Call<ContactsResponse> response = requestInterface.contacts(contactRequest);
        response.enqueue(new Callback<ContactsResponse>() {
            @Override
            public void onResponse(Call<ContactsResponse> call, retrofit2.Response<ContactsResponse> response) {
                ContactsResponse resp = response.body();
                assert resp != null;
                Log.d("Contacts",resp.getMessage());
                if(resp.getMessage().equalsIgnoreCase("Fetched")){
                    boolean[] myContacts=resp.getResult();
                    for(int i=0;i<myContacts.length;i++){
                        if(myContacts[i]){
                            Contact contact=allContactsList.get(i);
                            contact.setInitials(tasks.getInitial(allContactsList.get(i).getName()));
                            riseappContacts.add(contact);
                        }
                    }
                    contactCallback.onSuccessfulFetch(riseappContacts);
                }else {
                    contactCallback.onUnsuccessfulFetch();
                }
            }

            @Override
            public void onFailure(Call<ContactsResponse> call, Throwable t) {
            }
        });

    }

    public void setContactCallback(ContactCallback contactCallback) {
        this.contactCallback = contactCallback;
    }
}