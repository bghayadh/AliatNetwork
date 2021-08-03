package com.example.aliatnetwork;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TicketPagerAdapter extends FragmentPagerAdapter {

            private int numOfTabs;


    public TicketPagerAdapter (FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs=numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new TicketInfoFragment();
            case 1: return new TicketActivityFrag();
            case 2: return new TicketHistory();
            default:  return null;
        }

    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
