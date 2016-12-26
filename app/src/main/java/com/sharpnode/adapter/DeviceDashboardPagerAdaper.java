package com.sharpnode.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sharpnode.DeviceDashboardFragment;

/**
 * Created by admin on 12/8/2016.
 */
public class DeviceDashboardPagerAdaper extends FragmentPagerAdapter {

    private Context _context;
    public static int totalPage = 1;
    public DeviceDashboardPagerAdaper(Context context, FragmentManager fm) {
        super(fm);
        _context = context;
    }
    @Override
    public DeviceDashboardFragment getItem(int position) {
        DeviceDashboardFragment f = new DeviceDashboardFragment();
        switch (position) {
            case 0:
                f = new DeviceDashboardFragment();
                break;
            case 1:
                f = new DeviceDashboardFragment();
                break;
        }
        return f;
    }
    @Override
    public int getCount() {
        return totalPage;
    }
}