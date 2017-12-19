package com.example.administrator.ding_small;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.example.administrator.ding_small.Adapter.DeviceListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



/**
 * Created by Administrator on 2017/12/19.
 */

public class SelectDeviceActivity extends Activity {
    private JSONArray jsonArray;
    private ArrayList<String> device_names;
    private ArrayList<String> device_locations;
    private ArrayList<String> device_ssids;
    private ArrayList<String> device_staus;
    private ArrayList<String> device_type;
    private ListView device_listview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_device);
        init();//初始化控件
        CreatJson();//构造jsonArray备用
        device_listview.setAdapter(new DeviceListAdapter(SelectDeviceActivity.this,jsonArray));//设置适配器
    }
    private void init(){
        device_listview=findViewById(R.id.select_device_list);
    }
    private void CreatJson(){
        jsonArray=new JSONArray();
        device_names=new ArrayList<String>();
        device_locations=new ArrayList<String>();
        device_ssids=new ArrayList<String>();
        device_staus=new ArrayList<String>();
        device_type=new ArrayList<String>();

        device_names.add("桌台灯-1644");
        device_names.add("桌台灯-1620");
        device_names.add("桌台灯-1614");
        device_names.add("桌台灯-1514");
        device_names.add("桌台灯-1254");
        device_names.add("桌台灯-1255");
        device_names.add("桌台灯-1205");

        device_locations.add("大山百乐门酒吧");
        device_locations.add("广州广州塔");
        device_locations.add("广州广百百货");
        device_locations.add("广州正佳广场");
        device_locations.add("广州上下九步行街");
        device_locations.add("广州天河城");
        device_locations.add("广州番禺广场");


        device_ssids.add("201715464848465");
        device_ssids.add("221054546544614");
        device_ssids.add("546546165454645");
        device_ssids.add("465454651846515");
        device_ssids.add("156451518454841");
        device_ssids.add("156848184654564");
        device_ssids.add("574575762425112");

        device_staus.add("1");
        device_staus.add("1");
        device_staus.add("0");
        device_staus.add("0");
        device_staus.add("1");
        device_staus.add("0");
        device_staus.add("1");

        device_type.add("bluetooth");
        device_type.add("wifi");
        device_type.add("scan");
        device_type.add("scan");
        device_type.add("wifi");
        device_type.add("bluetooth");
        device_type.add("wifi");
        try {
            for (int i=0;i<device_names.size();i++){
                JSONObject jsonObject;
                jsonObject=new JSONObject();
                jsonObject.put("device_name",device_names.get(i));
                jsonObject.put("device_location",device_locations.get(i));
                jsonObject.put("device_ssid",device_ssids.get(i));
                jsonObject.put("device_state",device_staus.get(i));
                jsonObject.put("device_type",device_type.get(i));
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
