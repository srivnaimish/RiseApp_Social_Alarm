package com.riseapps.riseapp.view.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.ChatSync;
import com.riseapps.riseapp.executor.Interface.ContactSelection;
import com.riseapps.riseapp.executor.Utils;
import com.riseapps.riseapp.model.DB.Contact_Entity;
import com.riseapps.riseapp.model.MyApplication;
import com.riseapps.riseapp.view.ui.activity.ChatActivity;
import com.riseapps.riseapp.view.ui.activity.PickContacts;

import java.util.ArrayList;
import java.util.List;

import static com.riseapps.riseapp.Components.AppConstants.UPDATE_SUMMARY;


public class ContactsAdapter extends RecyclerView.Adapter {

    private ArrayList<Contact_Entity> contacts;
    private Context c;
    private ContactSelection contactSelection;
    private int count=0;
    private Utils utils =new Utils();
    private int colorSelected = Color.LTGRAY;
    private int colorNormal = Color.WHITE;


    public ContactsAdapter(Context context, ArrayList<Contact_Entity> contacts) {
        this.contacts = contacts;
        c = context;
        contactSelection = (ContactSelection) c;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Contact_Entity contact = contacts.get(position);
        ContactsViewHolder contactsViewHolder=((ContactsViewHolder) holder);
        String name=contact.getName();
        contactsViewHolder.name.setText(name);
        contactsViewHolder.phone.setText(contact.getNumber());
        contactsViewHolder.initials.setText(utils.getInitial(name));
        if (contact.isSelection())
            contactsViewHolder.status.setVisibility(View.VISIBLE);
        else
            contactsViewHolder.status.setVisibility(View.GONE);

        /*Glide.with(c)
                .load(AppConstants.getProfileImage(contact.getNumber()))
                .dontAnimate()
                .error(R.drawable.default_user)
                .placeholder(R.drawable.default_user)
                .centerCrop()
                .into(((ContactsViewHolder) holder).pic);*/
        contactsViewHolder.cardView.setCardBackgroundColor(contact.isSelection() ? colorSelected : colorNormal);

        ((ContactsViewHolder) holder).contact = contact;
    }

    public void addItems(List<Contact_Entity> contactList) {
        this.contacts = (ArrayList<Contact_Entity>) contactList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return contacts.size();
    }

    private class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        Contact_Entity contact;
        CardView cardView;
        TextView  name,initials, phone;
        ImageButton status;
        ImageView pic;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.contact_card);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            initials=itemView.findViewById(R.id.initials);
            status = itemView.findViewById(R.id.selected_state);
            pic=itemView.findViewById(R.id.pic);
            cardView.setOnClickListener(this);
            cardView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == cardView.getId()) {
                if(count>0) {
                    if (contact.isSelection()) {
                        contact.setSelection(false);
                        status.setVisibility(View.GONE);
                        contactSelection.onContactSelected(false, getAdapterPosition());
                        count--;
                    } else {
                        contact.setSelection(true);
                        status.setVisibility(View.VISIBLE);
                        status.startAnimation(AnimationUtils.loadAnimation(c, R.anim.selection));
                        contactSelection.onContactSelected(true, getAdapterPosition());
                        count++;
                    }
                    cardView.setCardBackgroundColor(contact.isSelection() ? colorSelected : colorNormal);
                }else {
                    ChatSync chatSync=new ChatSync(phone.getText().toString(),((MyApplication)c.getApplicationContext()).getDatabase(),UPDATE_SUMMARY);
                    chatSync.execute();
                    Intent intent=new Intent(c, ChatActivity.class);
                    intent.putExtra("chat_id",phone.getText().toString());
                    intent.putExtra("contact_name",name.getText().toString());
                    c.startActivity(intent);
                    ((PickContacts) c).finish();

                }
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (contact.isSelection()) {
                contact.setSelection(false);
                status.setVisibility(View.GONE);
                contactSelection.onContactSelected(false, getAdapterPosition());
                count--;
            } else {
                contact.setSelection(true);
                status.setVisibility(View.VISIBLE);
                status.startAnimation(AnimationUtils.loadAnimation(c, R.anim.selection));
                contactSelection.onContactSelected(true, getAdapterPosition());
                count++;
            }
            cardView.setCardBackgroundColor(contact.isSelection() ? colorSelected : colorNormal);
            return true;
        }
    }

}
