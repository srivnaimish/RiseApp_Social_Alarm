package com.riseapps.riseapp.executor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.model.DB.ChatSummary;
import com.riseapps.riseapp.view.activity.ChatActivity;

import java.util.ArrayList;

public class FeedsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int PERSONAL_ALARM = 0;
    private Context context;
    private ArrayList<ChatSummary> summaryList;
    private Tasks tasks = new Tasks();

    public FeedsAdapter(Context context, ArrayList<ChatSummary> summaryList) {
        this.context = context;
        this.summaryList = summaryList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_row, parent, false);
        return new FeedsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FeedsViewHolder feedsViewHolder = (FeedsViewHolder) holder;
        ChatSummary chatSummary=summaryList.get(position);
        String name=chatSummary.getChat_contact_name();
        feedsViewHolder.name.setText(name);
        feedsViewHolder.initials.setText(tasks.getInitial(name));
        if(!chatSummary.getRead()){
            feedsViewHolder.badge.setVisibility(View.VISIBLE);
            feedsViewHolder.name.setTypeface(feedsViewHolder.name.getTypeface(), Typeface.BOLD);
        }
        feedsViewHolder.chatSummary=chatSummary;
    }

    @Override
    public int getItemCount() {
        return summaryList.size();
    }

    class FeedsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ChatSummary chatSummary;
        private CardView cardView;
        private ImageView badge;
        private TextView name, initials;/*, image*/

        public FeedsViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contact_name);
            initials = itemView.findViewById(R.id.initials);
            badge=itemView.findViewById(R.id.badge);
            cardView=itemView.findViewById(R.id.feed_card);
            cardView.setOnClickListener(this);
            //image = itemView.findViewById(R.id.image);
        }

        @Override
        public void onClick(View v) {
            Intent intent=new Intent(context, ChatActivity.class);
            intent.putExtra("contact_id",chatSummary.getChat_contact_id());
            context.startActivity(intent);
        }
    }

}