package com.riseapps.riseapp.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.executor.Interface.FabListener;
import com.riseapps.riseapp.executor.Interface.ToggleShareDialog;
import com.riseapps.riseapp.view.activity.MainActivity;


public class SharedFragment extends Fragment implements View.OnClickListener{

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

        ((MainActivity)getActivity()).setFabListener2(new FabListener() {
            @Override
            public void onFabClick() {
                toggleShareDialog.toggleVisibility();
            }
        });
        return view;
    }

    public static SharedFragment newInstance() {
        return new SharedFragment();
    }

    @Override
    public void onClick(View view) {

    }

   /* public void openSharedDialog(){

    }*/

}
