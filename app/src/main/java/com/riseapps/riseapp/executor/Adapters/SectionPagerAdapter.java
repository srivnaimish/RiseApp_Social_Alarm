package com.riseapps.riseapp.executor.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.riseapps.riseapp.R;
import com.riseapps.riseapp.view.fragment.FeedsFragment;
import com.riseapps.riseapp.view.fragment.PersonalFragment;
import com.riseapps.riseapp.view.fragment.Settings;

/**
 * Created by naimish on 31/10/17.
 */

public class SectionPagerAdapter extends FragmentPagerAdapter {

     private String tabTitles[] = new String[]{"Messages", "Personal"};

    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FeedsFragment.newInstance();
            case 1:
                return PersonalFragment.newInstance();
        }
        return PersonalFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}
