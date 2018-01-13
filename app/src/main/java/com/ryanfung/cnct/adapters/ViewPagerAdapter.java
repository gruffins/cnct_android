package com.ryanfung.cnct.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<ViewPagerItem> items;

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull List<ViewPagerItem> items) {
        super(fragmentManager);
        this.items = items;
    }

    @Override
    public Fragment getItem(int position) {
        return items.get(position).fragment;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return items.get(position).title;
    }

    public static class ViewPagerItem {
        public Fragment fragment;
        public String title;

        public ViewPagerItem(@NonNull Fragment fragment, @NonNull String title) {
            this.fragment = fragment;
            this.title = title;
        }
    }
}
