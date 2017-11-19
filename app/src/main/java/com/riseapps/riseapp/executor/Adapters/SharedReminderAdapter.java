package com.riseapps.riseapp.executor.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.model.Pojo.ReceivedFeed;
import com.riseapps.riseapp.model.Pojo.SentFeed;

import java.util.ArrayList;

public class SharedReminderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int PERSONAL_ALARM = 0;
    private static final int SENT_REMINDER = 1;
    private static final int RECEIVED_REMINDER = 2;
    private Context context;
    private ArrayList<Object> remindersList;
    private Tasks tasks=new Tasks();

    public SharedReminderAdapter(Context context, ArrayList<Object> remindersList) {
        this.context = context;
        this.remindersList = remindersList;
    }

    @Override
    public int getItemViewType(int position) {
        if (remindersList.get(position) instanceof SentFeed) {
            return SENT_REMINDER;
        } else if (remindersList.get(position) instanceof ReceivedFeed) {
            return RECEIVED_REMINDER;
        } else if (remindersList.get(position) instanceof String) {
            return PERSONAL_ALARM;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case SENT_REMINDER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_sent, parent, false);
                return new SentViewHolder(context, view);

            case RECEIVED_REMINDER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_received, parent, false);
                return new ReceivedViewHolder(context, view);

            case PERSONAL_ALARM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_alarm, parent, false);
                return new PersonalAlarmViewHolder(context, view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case SENT_REMINDER:
                SentViewHolder sentViewHolder = (SentViewHolder) holder;
                SentFeed sentReminder= (SentFeed) remindersList.get(position);
                sentViewHolder.people.setText(sentReminder.getPeople());
                sentViewHolder.time.setText(sentReminder.getTime());
                sentViewHolder.note.setText(sentReminder.getNote());
                sentViewHolder.image.setText(sentReminder.getImage());
                sentViewHolder.hiddenLayout.setVisibility(View.GONE);
                break;

            case RECEIVED_REMINDER:
                ReceivedViewHolder receivedViewHolder = (ReceivedViewHolder) holder;
                ReceivedFeed receivedReminder= (ReceivedFeed) remindersList.get(position);
                receivedViewHolder.background.setImageResource(tasks.getRandomColor());
                receivedViewHolder.initials.setText(tasks.getInitial(receivedReminder.getSender()));
                receivedViewHolder.sender.setText(receivedReminder.getSender());
                receivedViewHolder.time.setText(receivedReminder.getTime());
                receivedViewHolder.note.setText(receivedReminder.getNote());
                receivedViewHolder.image.setText(receivedReminder.getImage());
                receivedViewHolder.hiddenLayout.setVisibility(View.GONE);
                break;

            case PERSONAL_ALARM:
                PersonalAlarmViewHolder personalAlarmViewHolder= (PersonalAlarmViewHolder) holder;
                String s= (String) remindersList.get(position);
                personalAlarmViewHolder.message.setText(s);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return remindersList.size();
    }

    class SentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Context context;
        private ImageView imageView;
        private TextView people, time, note, image;
        private CardView cardView;
        private LinearLayout hiddenLayout;

        public SentViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            imageView = itemView.findViewById(R.id.bell);
            people = itemView.findViewById(R.id.people);
            time = itemView.findViewById(R.id.time);
            note = itemView.findViewById(R.id.note);
            image = itemView.findViewById(R.id.image);
            cardView=itemView.findViewById(R.id.sent_card);
            cardView.setOnClickListener(this);
            hiddenLayout=itemView.findViewById(R.id.sent_hidden);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.sent_card:
                    if(hiddenLayout.getVisibility()==View.GONE){
                        hiddenLayout.setVisibility(View.VISIBLE);
                    }else {
                        hiddenLayout.setVisibility(View.GONE);
                    }                break;
            }

        }
    }

    class ReceivedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Context context;
        ImageView background;
        private TextView initials,sender, time, note, image;
        private CardView cardView;
        private LinearLayout hiddenLayout;

        public ReceivedViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            background=itemView.findViewById(R.id.bell);
            initials=itemView.findViewById(R.id.initials);
            sender = itemView.findViewById(R.id.Sender);
            time = itemView.findViewById(R.id.time);
            note = itemView.findViewById(R.id.note);
            image = itemView.findViewById(R.id.image);

            cardView=itemView.findViewById(R.id.received_card);

            cardView.setOnClickListener(this);
            hiddenLayout=itemView.findViewById(R.id.received_hidden);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.received_card:
                    if(hiddenLayout.getVisibility()==View.GONE){
                        hiddenLayout.setVisibility(View.VISIBLE);
                        sender.setSingleLine(false);
                    }else {
                        hiddenLayout.setVisibility(View.GONE);
                        sender.setSingleLine(true);
                    }
                    break;
            }

        }
    }

    class PersonalAlarmViewHolder extends RecyclerView.ViewHolder{
        private Context context;
        private TextView message;

        public PersonalAlarmViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            message = itemView.findViewById(R.id.message);
        }
    }
}