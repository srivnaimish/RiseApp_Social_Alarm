package com.riseapps.riseapp.executor.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.riseapps.riseapp.view.fragment.PersonalFragment;
import com.riseapps.riseapp.view.fragment.Settings;
import com.riseapps.riseapp.view.fragment.SharedFragment;
import com.riseapps.riseapp.view.fragment.WeatherFragment;

/**
 * Created by naimish on 31/10/17.
 */

public class SectionPagerAdapter extends FragmentPagerAdapter {

   // String tabTitles[] = new String[]{"Personal", "Shared", "Weather","Settings"};

    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PersonalFragment.newInstance();
            case 1:
                return SharedFragment.newInstance();
            case 2:
                return WeatherFragment.newInstance();
            case 3:
                return Settings.newInstance();


        }
        return PersonalFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

}
