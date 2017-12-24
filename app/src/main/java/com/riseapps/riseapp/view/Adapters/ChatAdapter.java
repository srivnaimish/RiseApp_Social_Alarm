package com.riseapps.riseapp.view.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.AlarmCreator;
import com.riseapps.riseapp.executor.TimeToView;
import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.MyApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.riseapps.riseapp.Components.AppConstants.RECEIVED_MESSAGE;
import static com.riseapps.riseapp.Components.AppConstants.SENT_MESSAGE;

/**
 * Created by naimish on 2/12/17.
 */

public class ChatAdapter extends RecyclerView.Adapter {

    private ArrayList<Chat_Entity> chatList;
    private Context context;
    private TimeToView timeToView=new TimeToView();


    public ChatAdapter(Context context,ArrayList<Chat_Entity> chatList){
        this.context=context;
        this.chatList=chatList;
    }

    @Override
    public int getItemViewType(int position) {
        if(chatList.get(position).getSent_or_recieved()==SENT_MESSAGE){
            return SENT_MESSAGE;
        }else {
            return RECEIVED_MESSAGE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case SENT_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_sent, parent, false);
                return new SentMessageViewHolder(context, view);

            case RECEIVED_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_received, parent, false);
                return new ReceivedMessageViewHolder(context, view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case SENT_MESSAGE:
                SentMessageViewHolder sentMessageViewHolder= (SentMessageViewHolder) holder;
                Chat_Entity sent_chat=chatList.get(position);
                String sent_note=sent_chat.getNote();

                String sent_timeInString="";
                long sent_time=sent_chat.getTime();
                Calendar sent_calendar=Calendar.getInstance();
                sent_calendar.setTimeInMillis(sent_time);

                sent_timeInString=timeToView.getTimeAsText(sent_calendar.get(Calendar.HOUR),sent_calendar.get(Calendar.MINUTE));
                int sent_am_pm=sent_calendar.get(Calendar.AM_PM);
                if(sent_am_pm == Calendar.AM)
                    sent_timeInString+=" am,";
                else
                    sent_timeInString+=" pm,";

                String sent_dateInString=sent_calendar.get(Calendar.DAY_OF_MONTH)+"-"+(sent_calendar.get(Calendar.MONTH)+1)+"-"+sent_calendar.get(Calendar.YEAR);

                sentMessageViewHolder.note.setText(processNote(sent_note));
                sentMessageViewHolder.time.setText(sent_timeInString);
                sentMessageViewHolder.date.setText(sent_dateInString);
                sentMessageViewHolder.chat_entity=sent_chat;

                break;
            case RECEIVED_MESSAGE:
                ReceivedMessageViewHolder receivedMessageViewHolder= (ReceivedMessageViewHolder) holder;
                Chat_Entity received_chat=chatList.get(position);
                String received_note=received_chat.getNote();

                String received_timeInString="";
                long received_time=received_chat.getTime();
                Calendar received_calendar=Calendar.getInstance();
                received_calendar.setTimeInMillis(received_time);
                received_timeInString=timeToView.getTimeAsText(received_calendar.get(Calendar.HOUR),received_calendar.get(Calendar.MINUTE));

                int received_am_pm=received_calendar.get(Calendar.AM_PM);
                if(received_am_pm == Calendar.AM)
                    received_timeInString+=" am,";
                else
                    received_timeInString+=" pm,";

                String received_dateInString=received_calendar.get(Calendar.DAY_OF_MONTH)+"-"+(received_calendar.get(Calendar.MONTH)+1)+"-"+received_calendar.get(Calendar.YEAR);


                if(received_chat.isRead()){
                    receivedMessageViewHolder.done.setVisibility(View.VISIBLE);
                }

                receivedMessageViewHolder.note.setText(processNote(received_note));
                receivedMessageViewHolder.time.setText(received_timeInString);
                receivedMessageViewHolder.date.setText(received_dateInString);
                receivedMessageViewHolder.chat_entity=received_chat;

                break;
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public void addItems(List<Chat_Entity> chatList) {
        this.chatList = (ArrayList<Chat_Entity>) chatList;
        notifyDataSetChanged();
    }

    public ArrayList<Chat_Entity> getItems(){
        return chatList;
    }

    class SentMessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Chat_Entity chat_entity;
        private CardView cardView;
        private TextView note;
        private TextView time;
        private TextView date;

        public SentMessageViewHolder(Context context, View itemView) {
            super(itemView);
            note=itemView.findViewById(R.id.note);
            time=itemView.findViewById(R.id.time);
            date=itemView.findViewById(R.id.date);
            cardView=itemView.findViewById(R.id.sent_card);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            shareTaskOnOther(chat_entity.getNote());
        }
    }

    class ReceivedMessageViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        private Chat_Entity chat_entity;
        private ConstraintLayout constraintLayout;
        private TextView note;
        private TextView time;
        private TextView date;
        private ImageView done;
        public ReceivedMessageViewHolder(Context context, View itemView) {
            super(itemView);
            note=itemView.findViewById(R.id.note);
            time=itemView.findViewById(R.id.time);
            date=itemView.findViewById(R.id.date);
            constraintLayout=itemView.findViewById(R.id.received_card);
            done=itemView.findViewById(R.id.done);
            done.setOnClickListener(this);
            constraintLayout.setOnClickListener(this);
            constraintLayout.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setMessage("This action will delete the reminder for this task");
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    new AlarmCreator().setAlarmOff(context,chat_entity.getMessage_id());
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            ((MyApplication)context.getApplicationContext()).getDatabase().chatDao().deleteChatTask(chat_entity.getMessage_id());
                        }
                    });
                    deleteItem(getAdapterPosition());
                }
            });
            alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alert.show();
            return true;
        }

        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.done)
            Toast.makeText(context, "This task is completed", Toast.LENGTH_SHORT).show();
            else
                shareTaskOnOther(chat_entity.getNote());
        }
    }

    private void deleteItem(int adapterPosition) {
        chatList.remove(adapterPosition);
        notifyDataSetChanged();
    }

    public void shareTaskOnOther(String task){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, task);
        context.startActivity(Intent.createChooser(share, "Share Task via.."));
    }

    private String processNote(String note) {
        String[] points=note.split("\\s*\\r?\\n\\s*");
        if(points.length==1){
            return note;
        }else {
            StringBuilder noteBuilder = new StringBuilder();
            int i;
            for(i=0;i<points.length-1;i++){
                noteBuilder.append(i + 1).append(". ").append(points[i]).append("\n");
            }
            noteBuilder.append(i + 1).append(". ").append(points[i]);
            note = noteBuilder.toString();
        }
        return note;
    }
}
