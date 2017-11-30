package com.riseapps.riseapp.executor.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Interface.ContactSelection;
import com.riseapps.riseapp.model.DB.Contact_Entity;

import java.util.ArrayList;


public class ContactsAdapter extends RecyclerView.Adapter {

    private ArrayList<Contact_Entity> contacts;
    private Context c;
    private ContactSelection contactSelection;


    public ContactsAdapter(Context context, ArrayList<Contact_Entity> contacts) {
        this.contacts = contacts;
        c = context;
        contactSelection = (ContactSelection) c;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);
        return new ContactsViewHolder(view, c);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Contact_Entity contact = contacts.get(position);
        ((ContactsViewHolder) holder).name.setText(contact.getName());
        ((ContactsViewHolder) holder).phone.setText(contact.getNumber());
        ((ContactsViewHolder) holder).initials.setText(contact.getInitials());
        if (contact.isSelection())
            ((ContactsViewHolder) holder).status.setVisibility(View.VISIBLE);
        else
            ((ContactsViewHolder) holder).status.setVisibility(View.GONE);
        ((ContactsViewHolder) holder).contact = contact;

    }


    @Override
    public int getItemCount() {
        return contacts.size();
    }

    private class ContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Contact_Entity contact;
        CardView cardView;
        TextView initials, name, phone;
        ImageButton status;

        public ContactsViewHolder(View itemView, Context c) {
            super(itemView);
            cardView = itemView.findViewById(R.id.contact_card);
            initials = itemView.findViewById(R.id.initials);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            status = itemView.findViewById(R.id.selected_state);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == cardView.getId()) {
                if (contact.isSelection()) {
                    contact.setSelection(false);
                    status.setVisibility(View.GONE);
                    contactSelection.onContactSelected(false, getAdapterPosition());
                } else {
                    contact.setSelection(true);
                    status.setVisibility(View.VISIBLE);
                    status.startAnimation(AnimationUtils.loadAnimation(c, R.anim.selection));
                    contactSelection.onContactSelected(true, getAdapterPosition());
                }
            }
        }
    }

}
