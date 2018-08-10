package com.example.roshan.rahiapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by darthvader on 8/8/18.
 */

public class PagerAdapter extends FragmentPagerAdapter {
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                    RouteFragment fragment = new RouteFragment();
                    return fragment;
            case 1:
                    MapFragment fragment1 = new MapFragment();
                    return fragment1;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Route";
            case 1:
                return "Map";
        }

        return super.getPageTitle(position);
    }
}
