package com.riseapps.riseapp.view.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.riseapps.riseapp.view.ui.fragment.FeedsFragment;
import com.riseapps.riseapp.view.ui.fragment.PersonalFragment;

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