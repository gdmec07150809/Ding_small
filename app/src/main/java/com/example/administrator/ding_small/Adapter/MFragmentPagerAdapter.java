package com.example.administrator.ding_small.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by CZK on 2017/11/30.
 */

public class MFragmentPagerAdapter extends FragmentPagerAdapter {
    //存放Fragment的数组
    private ArrayList<Fragment> fragmentsList;


    public MFragmentPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragmentArrayList) {
        super(fragmentManager);
        this.fragmentsList = fragmentArrayList;
    }


    @Override
    public Fragment getItem(int position) {

        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
