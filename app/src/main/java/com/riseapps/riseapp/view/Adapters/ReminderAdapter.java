package com.riseapps.riseapp.view.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.ChatSync;
import com.riseapps.riseapp.executor.Utils;
import com.riseapps.riseapp.executor.TimeToView;
import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.MyApplication;

import java.util.ArrayList;
import java.util.Calendar;

import static com.riseapps.riseapp.Components.AppConstants.UPDATE_PENDING;

/**
 * Created by naimish on 2/12/17.
 */

public class ReminderAdapter extends RecyclerView.Adapter {

    private ArrayList<Chat_Entity> reminderList;
    private Context context;
    private TimeToView timeToView=new TimeToView();
    private Utils utils =new Utils();
    private LinearLayout empty_state;


    public ReminderAdapter(Context context, ArrayList<Chat_Entity> reminderList, LinearLayout empty_state){
        this.context=context;
        this.reminderList=reminderList;
        this.empty_state=empty_state;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_row, parent, false);
        return new ReminderViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ReminderViewHolder reminderViewHolder= (ReminderViewHolder) holder;
        Chat_Entity reminder=reminderList.get(position);
        String note=reminder.getNote();

        String time="";
        long received_time=reminder.getTime();
        Calendar received_calendar=Calendar.getInstance();
        received_calendar.setTimeInMillis(received_time);
        time=timeToView.getTimeAsText(received_calendar.get(Calendar.HOUR_OF_DAY),received_calendar.get(Calendar.MINUTE));

        int received_am_pm=received_calendar.get(Calendar.AM_PM);
        if(received_am_pm == Calendar.AM)
            time+="am";
        else
            time+="pm";

        String received_dateInString=received_calendar.get(Calendar.DAY_OF_MONTH)+"-"+(received_calendar.get(Calendar.MONTH)+1)+"-"+received_calendar.get(Calendar.YEAR);

        String name=reminder.getContact_name();
        reminderViewHolder.initials.setText(utils.getInitial(name));
        reminderViewHolder.name.setText(name);
        reminderViewHolder.note.setText(note);
        reminderViewHolder.time.setText(time);
        reminderViewHolder.date.setText(received_dateInString);
        reminderViewHolder.chat_entity=reminder;

    }

    public void deleteItem(int position){
        reminderList.remove(position);
        notifyItemRemoved(position);
        if (reminderList.size()==0){
            empty_state.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Chat_Entity chat_entity;
        private Context context;
        private ImageView imageView;
        private TextView initials;
        private TextView name;
        private TextView note;
        private TextView time;
        private TextView date;
        private ImageButton done;

        public ReminderViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            imageView=itemView.findViewById(R.id.image);
            name=itemView.findViewById(R.id.name);
            initials=itemView.findViewById(R.id.initials);
            note=itemView.findViewById(R.id.note);
            note=itemView.findViewById(R.id.note);
            time=itemView.findViewById(R.id.time);
            date=itemView.findViewById(R.id.date);
            done=itemView.findViewById(R.id.done);
            done.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ChatSync chatSync=new ChatSync(chat_entity.getMessage_id(),((MyApplication)context.getApplicationContext()).getDatabase(),UPDATE_PENDING);
            chatSync.execute();
            done.setImageResource(R.drawable.ic_selected);
            deleteItem(getAdapterPosition());
        }
    }

}
