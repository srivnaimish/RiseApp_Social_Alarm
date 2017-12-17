package com.riseapps.riseapp.view.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.riseapps.riseapp.Components.AppConstants;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.ChatSync;
import com.riseapps.riseapp.executor.Tasks;
import com.riseapps.riseapp.model.DB.ChatSummary;
import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.MyApplication;
import com.riseapps.riseapp.view.ui.activity.ChatActivity;

import java.util.ArrayList;
import java.util.List;

import static com.riseapps.riseapp.Components.AppConstants.DELETE_CHAT;
import static com.riseapps.riseapp.Components.AppConstants.UPDATE_SUMMARY;

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

        Glide.with(context)
                .load(AppConstants.getProfileImage(chatSummary.getChat_contact_number()))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .error(R.drawable.default_user)
                .placeholder(R.drawable.default_user)
                .centerCrop()
                .into(feedsViewHolder.pic);

        feedsViewHolder.mssg.setText(chatSummary.getChat_last_message());
        feedsViewHolder.chatSummary=chatSummary;

        if(position==summaryList.size()-1){
            feedsViewHolder.view.setVisibility(View.GONE);
        }
    }

    public void deleteItem(int position){
        summaryList.remove(position);
        notifyDataSetChanged();
    }

    public void addItems(List<ChatSummary> summaryList) {
        this.summaryList = (ArrayList<ChatSummary>) summaryList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return summaryList.size();
    }

    class FeedsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

        private ChatSummary chatSummary;
        private CardView cardView;
        private ImageView badge,pic;
        private TextView name,mssg, initials;/*, image*/
        private View view;

        public FeedsViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contact_name);
            initials = itemView.findViewById(R.id.initials);
            mssg=itemView.findViewById(R.id.contact_mssg);
            badge=itemView.findViewById(R.id.badge);
            cardView=itemView.findViewById(R.id.feed_card);
            cardView.setOnClickListener(this);
            cardView.setOnLongClickListener(this);
            view=itemView.findViewById(R.id.divider);
            pic=itemView.findViewById(R.id.pic);
            //image = itemView.findViewById(R.id.image);
        }

        @Override
        public void onClick(View v) {
            ChatSync chatSync=new ChatSync(chatSummary.getChat_contact_number(),((MyApplication)context.getApplicationContext()).getDatabase(),UPDATE_SUMMARY);
            chatSync.execute();
            if(badge.getVisibility()==View.VISIBLE){
                badge.setVisibility(View.GONE);
                name.setTypeface(name.getTypeface(), Typeface.NORMAL);
            }
            Intent intent=new Intent(context, ChatActivity.class);
            intent.putExtra("chat_id",chatSummary.getChat_contact_number());
            intent.putExtra("contact_name",chatSummary.getChat_contact_name());
            context.startActivity(intent);

        }

        @Override
        public boolean onLongClick(View view) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setMessage("Delete Chat with "+chatSummary.getChat_contact_name()+"?");
            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // continue with delete
                    ChatSync chatSync=new ChatSync(chatSummary.getChat_contact_number(),((MyApplication)context.getApplicationContext()).getDatabase(),DELETE_CHAT);
                    chatSync.execute();
                    //deleteItem(getAdapterPosition());
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
    }

}