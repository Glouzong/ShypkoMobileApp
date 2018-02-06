package com.example.glouz.shypkoapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private static final int COUNT_PAGE = 4;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return WelcomePageFragments.newInstance(position);
    }

    @Override
    public int getCount() {
        return COUNT_PAGE;
    }
}
