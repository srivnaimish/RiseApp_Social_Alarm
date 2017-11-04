package com.riseapps.riseapp.view.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Interface.ToggleShareDialog;


public class SharedFragment extends Fragment implements View.OnClickListener{

    FloatingActionButton floatingActionButton;
    ToggleShareDialog toggleShareDialog;
    public SharedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_shared, container, false);
        toggleShareDialog= (ToggleShareDialog) getActivity();

        floatingActionButton=view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(this);
        return view;
    }

    public static SharedFragment newInstance() {
        return new SharedFragment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.floatingActionButton:
                toggleShareDialog.toggleVisibility();
            break;
        }
    }
}
