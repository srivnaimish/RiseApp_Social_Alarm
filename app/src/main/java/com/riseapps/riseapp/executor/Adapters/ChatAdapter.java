package com.riseapps.riseapp.executor.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.TimeToView;
import com.riseapps.riseapp.model.DB.Chat_Entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

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

                sentMessageViewHolder.note.setText(sent_note);
                sentMessageViewHolder.time.setText(sent_timeInString);
                sentMessageViewHolder.date.setText(sent_dateInString);

                Glide.with(context)
                        .load(sent_chat.getImage())
                        .dontAnimate()
                        .error(R.drawable.placeholder_image)
                        .into(sentMessageViewHolder.imageView);

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
                received_timeInString=timeToView.getTimeAsText(received_calendar.get(Calendar.HOUR_OF_DAY),received_calendar.get(Calendar.MINUTE));

                int received_am_pm=received_calendar.get(Calendar.AM_PM);
                if(received_am_pm == Calendar.AM)
                    received_timeInString+=" am,";
                else
                    received_timeInString+=" pm,";

                String received_dateInString=received_calendar.get(Calendar.DAY_OF_MONTH)+"-"+(received_calendar.get(Calendar.MONTH)+1)+"-"+received_calendar.get(Calendar.YEAR);

                receivedMessageViewHolder.note.setText(received_note);
                receivedMessageViewHolder.time.setText(received_timeInString);
                receivedMessageViewHolder.date.setText(received_dateInString);

                Glide.with(context)
                        .load(received_chat.getImage())
                        .dontAnimate()
                        .error(R.drawable.placeholder_image)
                        .into(receivedMessageViewHolder.imageView);

                receivedMessageViewHolder.chat_entity=received_chat;

                break;
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class SentMessageViewHolder extends RecyclerView.ViewHolder{
        private Chat_Entity chat_entity;
        private Context context;
        private ImageView imageView;
        private TextView note;
        private TextView time;
        private TextView date;

        public SentMessageViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            imageView=itemView.findViewById(R.id.image);
            note=itemView.findViewById(R.id.note);
            time=itemView.findViewById(R.id.time);
            date=itemView.findViewById(R.id.date);
        }
    }

    class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{
        private Chat_Entity chat_entity;
        private Context context;
        private ImageView imageView;
        private TextView note;
        private TextView time;
        private TextView date;
        public ReceivedMessageViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            imageView=itemView.findViewById(R.id.image);
            note=itemView.findViewById(R.id.note);
            time=itemView.findViewById(R.id.time);
            date=itemView.findViewById(R.id.date);
        }
    }
}
