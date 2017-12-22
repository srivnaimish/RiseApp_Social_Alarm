package com.riseapps.riseapp.executor;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.riseapps.riseapp.Components.AppConstants;
import com.riseapps.riseapp.executor.Network.RequestInterface;
import com.riseapps.riseapp.model.DB.Contact_Entity;
import com.riseapps.riseapp.model.DB.MyDB;
import com.riseapps.riseapp.model.Pojo.ContactFetch;
import com.riseapps.riseapp.model.Pojo.Server.ContactRequest;
import com.riseapps.riseapp.model.Pojo.Server.ContactsResponse;

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

    private MyDB myDB;
    private ContentResolver contentResolver;
    private ArrayList<Contact_Entity> riseappContacts;     //Fetch all feeds
    private ArrayList<ContactFetch> allContactsList;
    private Utils utils =new Utils();

    public ContactsSync(ContentResolver contentResolver, MyDB myDB) {
        this.myDB=myDB;
        this.contentResolver=contentResolver;
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

        Cursor data = contentResolver.query(CONTENT_URI, PROJECTION, null, null, sortOrder);
        while (data.moveToNext()) {
            String name = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY));
            String number = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            allContactsList.add(new ContactFetch(name, number));     // fetch all contacts from phone
            numberList.add(number);     //get number list to send to server for verification
        }
        data.close();

        myDB.contactDao().clearContacts();

        getRiseAppContacts(numberList);

        }

    private void getRiseAppContacts(ArrayList<String> numberList){   //get RiseApp Contacts from Server
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        ContactRequest contactRequest=new ContactRequest();
        contactRequest.setOperation(GET_CONTACTS);
        contactRequest.setPhones(numberList);

        Gson gson = new Gson();
        String json = gson.toJson(contactRequest);
        Log.d("contacts",json);
        Call<ContactsResponse> response = requestInterface.contacts(contactRequest);
        response.enqueue(new Callback<ContactsResponse>() {
            @Override
            public void onResponse(Call<ContactsResponse> call, retrofit2.Response<ContactsResponse> response) {
                ContactsResponse resp = response.body();
                assert resp != null;
                if(resp.getMessage().equalsIgnoreCase("Fetched")){
                    String[] myContacts=resp.getResult();
                    for(int i=0;i<myContacts.length;i++){
                        if(myContacts[i]!=null && !myContacts[i].equalsIgnoreCase(" ")){
                            String[] details=myContacts[i].split("\\s+");
                            Contact_Entity contact_entity=new Contact_Entity(
                                    allContactsList.get(i).getContact_name(),
                                    details[0],
                                    false);
                            riseappContacts.add(contact_entity);
                            FirebaseMessaging.getInstance().subscribeToTopic(details[1]);
                        }
                    }
                    if(ContactsSync.this.getStatus()==Status.FINISHED) {
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                myDB.contactDao().insertFeed(riseappContacts);
                            }
                        });
                    }else {
                        myDB.contactDao().insertFeed(riseappContacts);
                    }
                }
            }

            @Override
            public void onFailure(Call<ContactsResponse> call, Throwable t) {
            }
        });
    }

}