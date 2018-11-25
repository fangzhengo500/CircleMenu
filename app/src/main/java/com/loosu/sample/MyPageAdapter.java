package com.loosu.sample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPageAdapter extends FragmentPagerAdapter {
    private Fragment[] mFragments = {
            TestFragment.createInstance("Page 1", 0xffff6666),
            TestFragment.createInstance("Page 2", 0xff66ff66),
            TestFragment.createInstance("Page 3", 0xff6666ff),
    };

    public MyPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments[i];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }
}
