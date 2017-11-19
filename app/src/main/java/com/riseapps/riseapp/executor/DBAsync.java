package com.riseapps.riseapp.executor;

import android.os.AsyncTask;

import com.riseapps.riseapp.model.DB.Feed_Entity;
import com.riseapps.riseapp.model.DB.MyDB;
import com.riseapps.riseapp.model.Pojo.ReceivedFeed;
import com.riseapps.riseapp.model.Pojo.SentFeed;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by naimish on 16/11/17.
 */

public class DBAsync extends AsyncTask<Void,Void,ArrayList<Object>> {

    private int choice;
    private MyDB myDB;

    private ArrayList<Object> reminderList;     //Fetch all feeds

    private String alarm_time;

    private String people,sent_note,sent_image;     //Insert Sent reminder feed
    private long sent_time;

    public DBAsync(MyDB myDB,int choice){
        this.myDB=myDB;
        this.choice=choice;
    }

    @Override
    protected ArrayList<Object> doInBackground(Void... voids) {
        switch (choice){
            case 1:
                fillList();
                return reminderList;
            case 2:
                insertAlarm();
                break;
            case 3:
                insertSentReminder();
                break;

        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Object> objects) {
        super.onPostExecute(objects);
    }

    private void insertAlarm() {
        Feed_Entity feed_entity=new Feed_Entity();
        feed_entity.setType(1);
        feed_entity.setMessage("You set an alarm for "+alarm_time);
        myDB.feedDao().insertFeed(feed_entity);
    }

    private void insertSentReminder() {
        Feed_Entity feed_entity=new Feed_Entity();
        feed_entity.setType(2);
        feed_entity.setMessage(people);
        feed_entity.setTime(sent_time);
        feed_entity.setNote(sent_note);
        feed_entity.setImageurl(sent_image);
        myDB.feedDao().insertFeed(feed_entity);
    }

    private void fillList(){
        reminderList=new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        TimeToView timeToView = new TimeToView();

        ArrayList<Feed_Entity> feed_entities = (ArrayList<Feed_Entity>) myDB.feedDao().getAll();

        for(int i=feed_entities.size()-1;i>=0;i--){
            Feed_Entity row=feed_entities.get(i);
            if (row.getType() == 1) {
                reminderList.add(row.getMessage());
                //personal
            } else if (row.getType() == 2) {
                calendar.setTimeInMillis(row.getTime());
                String timeString = timeToView.getTimeAsText(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

                reminderList.add(new SentFeed(row.getMessage(), timeString, row.getNote(), row.getImageurl()));

            } else {

                calendar.setTimeInMillis(row.getTime());
                String timeString = timeToView.getTimeAsText(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

                reminderList.add(new ReceivedFeed(row.getMessage(), timeString, row.getNote(), row.getImageurl()));

            }
        }

    }

    public void setSentParams(String people,long sent_time,String sent_note,String sent_image){
        this.people=people;
        this.sent_time=sent_time;
        this.sent_note=sent_note;
        this.sent_image=sent_image;
    }

    public void setAlarmParams(String time){
        alarm_time=time;
    }

}
