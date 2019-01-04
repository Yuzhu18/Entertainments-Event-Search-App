package com.example.yuzhujiang.eventsearch;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetailPageAdapter extends FragmentPagerAdapter{

    private final List<Fragment> myFragmentList = new ArrayList<>();
    private final List<String> myFragmentTitleList = new ArrayList<>();

    public void addFragment(Fragment fragment, String title){
        myFragmentList.add(fragment);
        myFragmentTitleList.add(title);
    }

    public DetailPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return myFragmentTitleList.get(position);
    }

    @Override
    public Fragment getItem(int i) {
        return myFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return myFragmentList.size();
    }
}
