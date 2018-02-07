package com.example.administrator.ding_small.Utils;

import android.app.Application;

/**
 * Created by youyou000 on 2018/start2/start1.
 */

public class Data extends Application {
    public String url;

    public String getB(){
        return this.url;
    }
    public void setB(String c){
        this.url= c;
    }
    @Override
    public void onCreate(){
        url= "http://120.76.188.131:8080";
        super.onCreate();
    }
}
