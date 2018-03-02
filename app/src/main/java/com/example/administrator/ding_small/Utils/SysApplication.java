package com.example.administrator.ding_small.Utils;

/**
 * Created by youyou000 on 2018/2/26.
 */

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;
public class SysApplication extends Application {
    private List<Activity> mList = new LinkedList<Activity>();
    private static SysApplication instance;
    private SysApplication() {
    }
    public synchronized static SysApplication getInstance() {
        if (null == instance) {
            instance = new SysApplication();
        }
        return instance;
    }
    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
// System.exit(0);//去掉这个
        }
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}