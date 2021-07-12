package com.example.aliatnetwork;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ShopsPagerAdapter extends FragmentPagerAdapter {
    private int numOfTabs;
    public ShopsPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs=numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new ShopInfoFragment();
            case 1: return new ShopImageFragment();
            case 2: return new AssToShopsFragment();
            default:return null;


        }

    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
