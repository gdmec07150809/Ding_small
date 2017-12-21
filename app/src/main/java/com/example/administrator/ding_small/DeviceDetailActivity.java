package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/12/19.
 */

public class DeviceDetailActivity extends Activity implements View.OnClickListener{
    private TextView device_name;
    private boolean isCallRecord=true;
    private boolean isManagement=true;
    private boolean isManufacturer=true;
    private boolean isParameter=true;
    private boolean isSellingPoint=true;
    private boolean isRecord=true;
    private LinearLayout call_record_layout,management_layout,manufacturer_layout,parameter_layout,selling_point_layout,record_layout;
    private ImageView management_img,selling_point_img,parameter_img,manufacturer_img,record_img,call_record_img,repair_img;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_detail);
        init();//初始化控件，绑定监听
        getBundleString();//获取bundle数据
    }
    private  void getBundleString(){
        Bundle getStringValue=this.getIntent().getExtras();
        if(getStringValue.getString("device_name")!=null){
            device_name.setText( getStringValue.getString("device_name"));
        }
    }

    private void init(){
        device_name=findViewById(R.id.device_name);
        findViewById(R.id.call_record_up).setOnClickListener(this);//调拨记录
        findViewById(R.id.management_up).setOnClickListener(this);//管理信息
        findViewById(R.id.manufacturer_up).setOnClickListener(this);//制造商
        findViewById(R.id.parameter_up).setOnClickListener(this);//设备参数
        findViewById(R.id.selling_point_up).setOnClickListener(this);//售点信息
        findViewById(R.id.record_up).setOnClickListener(this);//维修记录
        findViewById(R.id.back).setOnClickListener(this);//返回;
        call_record_layout=findViewById(R.id.call_record_layout);
        management_layout=findViewById(R.id.management_layout);
        manufacturer_layout=findViewById(R.id.manufacturer_layout);
        parameter_layout=findViewById(R.id.parameter_layout);
        selling_point_layout=findViewById(R.id.selling_point_layout);
        record_layout=findViewById(R.id.record_layout);
        /* management_img,selling_point_img,parameter_img,manufacturer_img,record_img,call_record_img;  箭头图片的初始化*/
        management_img=findViewById(R.id.management_img);
        selling_point_img=findViewById(R.id.selling_point_img);
        parameter_img=findViewById(R.id.parameter_img);
        manufacturer_img=findViewById(R.id.manufacturer_img);
        record_img=findViewById(R.id.record_img);
        call_record_img=findViewById(R.id.call_record_img);
        findViewById(R.id.repair_img).setOnClickListener(this);
    }
    //按钮点击事件
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.call_record_up://调拨记录
                if(isCallRecord){
                    call_record_layout.setVisibility(View.VISIBLE);
                    isCallRecord=!isCallRecord;
                    call_record_img.setImageResource(R.drawable.up_jiantou);
                }else{
                    call_record_layout.setVisibility(View.GONE);
                    isCallRecord=!isCallRecord;
                    call_record_img.setImageResource(R.drawable.butoom_jiantou);
                }
                break;
            case R.id.management_up://管理信息
                if(isManagement){
                    management_layout.setVisibility(View.VISIBLE);
                    isManagement=!isManagement;
                    management_img.setImageResource(R.drawable.up_jiantou);
                }else{
                    management_layout.setVisibility(View.GONE);
                    isManagement=!isManagement;
                    management_img.setImageResource(R.drawable.butoom_jiantou);
                }
                break;
            case R.id.manufacturer_up://制造商
                if(isManufacturer){
                    manufacturer_layout.setVisibility(View.VISIBLE);
                    isManufacturer=!isManufacturer;
                    manufacturer_img.setImageResource(R.drawable.up_jiantou);
                }else{
                    manufacturer_layout.setVisibility(View.GONE);
                    isManufacturer=!isManufacturer;
                    manufacturer_img.setImageResource(R.drawable.butoom_jiantou);
                }
                break;
            case R.id.parameter_up://参数信息
                if(isParameter){
                    parameter_layout.setVisibility(View.VISIBLE);
                    isParameter=!isParameter;
                    parameter_img.setImageResource(R.drawable.up_jiantou);
                }else{
                    parameter_layout.setVisibility(View.GONE);
                    isParameter=!isParameter;
                    parameter_img.setImageResource(R.drawable.butoom_jiantou);
                }
                break;
            case R.id.selling_point_up://售点信息
                if(isSellingPoint){
                    selling_point_layout.setVisibility(View.VISIBLE);
                    isSellingPoint=!isSellingPoint;
                    selling_point_img.setImageResource(R.drawable.up_jiantou);
                }else{
                    selling_point_layout.setVisibility(View.GONE);
                    isSellingPoint=!isSellingPoint;
                    selling_point_img.setImageResource(R.drawable.butoom_jiantou);
                }
                break;
            case R.id.record_up://维修记录
                if(isRecord){
                    record_layout.setVisibility(View.VISIBLE);
                    isRecord=!isRecord;
                    record_img.setImageResource(R.drawable.up_jiantou);
                }else{
                    record_layout.setVisibility(View.GONE);
                    isRecord=!isRecord;
                    record_img.setImageResource(R.drawable.butoom_jiantou);
                }
                break;
            case R.id.repair_img://报修按钮
                intent=new Intent(DeviceDetailActivity.this,CreatRepairActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.back://返回
                finish();
                break;
            default:
                break;
        }
    }
}
