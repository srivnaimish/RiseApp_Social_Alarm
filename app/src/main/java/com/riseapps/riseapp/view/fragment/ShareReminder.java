package com.riseapps.riseapp.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.riseapps.riseapp.Components.AppConstants;
import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Interface.ToggleShareDialog;
import com.riseapps.riseapp.widgets.TextStrips;

import java.util.ArrayList;

/**
 * Created by naimish on 4/11/17.
 */

public class ShareReminder extends Fragment implements View.OnClickListener, TextWatcher {

    ToggleShareDialog toggleShareDialog;
    ImageView closeFragment;
    FlexboxLayout linearLayout;
    EditText editText;
    ArrayList<String> emails=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_send, container, false);

        toggleShareDialog= (ToggleShareDialog) getActivity();
        closeFragment=view.findViewById(R.id.closeFragment);
        closeFragment.setOnClickListener(this);
        linearLayout=view.findViewById(R.id.linearLayout);
        editText=view.findViewById(R.id.edit_email);
        editText.addTextChangedListener(this);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    addTextStrip(editText.getText().toString(),true);
                }

                return handled;
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.closeFragment:
                toggleShareDialog.toggleVisibility();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(charSequence.length()!=0) {
            char c = charSequence.charAt(charSequence.length() - 1);
            if (c == ' ' || c == ',') {
                addTextStrip(editText.getText().toString(),false);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public void addTextStrip(String charSequence,boolean enterpress){

        if(AppConstants.isValidEmail(charSequence)) {

            TextStrips textStrips = new TextStrips(getContext());

            TextView textView = (TextView) textStrips.getChildAt(0);
            String email;
            if(!enterpress)
            email = charSequence.substring(0, charSequence.length() - 1);
            else
                email = charSequence.substring(0, charSequence.length());
            emails.add(email);
            textView.setText(email);
            linearLayout.addView(textStrips);

            ImageButton imageButton = (ImageButton) textStrips.getChildAt(1);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextStrips textStrips1 = (TextStrips) view.getParent();
                    emails.remove(linearLayout.indexOfChild(textStrips1));
                    linearLayout.removeViewAt(linearLayout.indexOfChild(textStrips1));
                }
            });
        }else {
            Toast.makeText(getContext(), "Enter Valid Email", Toast.LENGTH_SHORT).show();
        }
        editText.setText("");
    }
}
