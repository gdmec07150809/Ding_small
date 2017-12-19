package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.ding_small.Adapter.DeviceListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;
import static com.example.administrator.ding_small.R.id.Right;

/**
 * Created by Administrator on 2017/12/18.
 */

public class DeviceListActivity extends Activity implements View.OnClickListener{
    private Spinner Right;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private ListView device_listview;
    private JSONArray jsonArray;
    private ArrayList<String> device_names;
    private ArrayList<String> device_locations;
    private ArrayList<String> device_ssids;
    private ArrayList<String> device_staus;
    private ArrayList<String> device_type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list);
        Right = findViewById(R.id.Right);
        device_listview=findViewById(R.id.device_listview);
        findViewById(R.id.select).setOnClickListener(this);
        getSpnner();//下拉列表
        CreatJson();//构造jsonArray备用
        device_listview.setAdapter(new DeviceListAdapter(DeviceListActivity.this,jsonArray));//设置适配器
        device_listview.setOnItemClickListener(new OnItemClickListener() {//列表item事件
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(DeviceListActivity.this,DeviceDetailActivity.class);
                Bundle bundle=new Bundle();
                try {
                   JSONObject object=new JSONObject(jsonArray.get(i).toString());

                    bundle.putString("device_name",object.getString("device_name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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

        device_locations.add("大山百乐门酒吧");
        device_locations.add("广州广州塔");
        device_locations.add("广州广百百货");
        device_locations.add("广州正佳广场");
        device_locations.add("广州上下九步行街");


        device_ssids.add("201715464848465");
        device_ssids.add("221054546544614");
        device_ssids.add("546546165454645");
        device_ssids.add("465454651846515");
        device_ssids.add("156451518454841");

        device_staus.add("1");
        device_staus.add("1");
        device_staus.add("0");
        device_staus.add("0");
        device_staus.add("1");

        device_type.add("bluetooth");
        device_type.add("wifi");
        device_type.add("scan");
        device_type.add("scan");
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
    private void getSpnner() {
        data_list = new ArrayList<String>();
        data_list.add("个人");
        data_list.add("公司");
        //适配器
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        //加载适配器
        Right.setAdapter(arr_adapter);
        Right.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        TextView tv1=(TextView) view;
                        tv1.setTextSize(14.0f);
                        tv1.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                    case 1:
                        TextView tv2=(TextView) view;
                        tv2.setTextSize(14.0f);
                        tv2.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.select:
                System.out.println("小图标");
                Right.performClick();
                break;
            default:
                break;
        }
    }
}
