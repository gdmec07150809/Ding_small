package com.example.administrator.ding_small;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.Adapter.DeviceListAdapter;
import com.example.administrator.ding_small.Adapter.ManufacturerAdapter;
import com.example.administrator.ding_small.HelpTool.CustomDialog;
import com.example.administrator.ding_small.HelpTool.LocationUtil;
import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.weavey.loading.lib.LoadingLayout;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.administrator.ding_small.HelpTool.LocationUtil.getAddress;
import static com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity.SHOW_RESPONSE;

/**
 * Created by CZK on 2017/12/19.
 */

public class DeviceDetailActivity extends Activity implements View.OnClickListener {
    private TextView device_name;
    private boolean isCallRecord = true;
    private boolean isManagement = true;
    private boolean isManufacturer = true;
    private boolean isParameter = true;
    private boolean isSellingPoint = true;
    private boolean isRecord = true;
    private boolean isEdit = false;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 100;

    private LinearLayout call_record_layout, management_layout, manufacturer_layout, parameter_layout, selling_point_layout, record_layout, base_layout, phone_record_layout;
    private ImageView management_img, selling_point_img, parameter_img, manufacturer_img, record_img, call_record_img, repair_img, base_img, eqpFlag, phone_record_img;
    private EditText selling_point_name, selling_point_number, selling_point_location, selling_point_phone, selling_point_user, parameter_location;
    //fa_location,ssid,macNo,eqpBrand,eqpStyle,startDate,protectDateTo
    //详细地址,uuid,MAC,品牌名称,品类,销售日期,保修期

    //{"poc": "", "deptName": "", "companyName": "", "managementName": "", "managementPhone": ""}
    private TextView latitude, longitude, temperature, loaction, location_detail, base_text, management_text, selling_text, phone_record_text,
            parameter_text, manufacturer_text, fa_location, ssid, macNo, eqpBrand, eqpStyle, startDate, protectDateTo,
            poc, deptName, companyName, managementName, managementPhone, repair_text, selling_name, selling_num, selling_location,
            selling_phone, selling_user_name, lo_la_text, auto_text, location_detail_text, temperature_text, no_data_text;

    private String str_location = null;
    private String latitude_str, longitude_str, province_str, temperature_str;

    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    String memid, token, sign, oldPass, newPass, ts, c_newPass, device_mac;
    public static final int SHOW_RESPONSE = 0;
    private ScrollView scrollview;
    private JSONObject DataObject;//设备数据
    private LoadingLayout loading;

    private ListView manufacturer_list;//制造商与报修

    //更改语言所要更改的控件 品牌名称、品类、销售日期、保质期、返回、设备信息、一键报修，管理公司名、部门、管理联系人电话、管理联系人名称、售点名称、售点编号、售点地址、售点联系人电话、售点联系人名称
    private TextView brand_name_text, category_text, selling_date, shelf_life_text, back_text, device_dateil_text, next_text,
            company_text, deptName_text, managementPhone_text, managementName_text, selling_name_text, selling_num_text, selling_location_text, selling_phone_text, selling_user_name_text;

    //private Handler handler;
    @RequiresApi(api = VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_device_detail);
        init();//初始化控件，绑定监听
        changeTextView();//更改语言
        loading.setStatus(LoadingLayout.Loading);
        getBundleString();//获取bundle数据
        getCache();//获取缓存
        getLocation();//获取当前地址
        getTemperature();//获取当前温度

    }

    private void changeTextView() {
        //base_text,management_text,selling_text,phone_record_text,parameter_text,manufacturer_text,company_text,deptName_text,managementPhone_text,managementName_text
        // selling_name_text,selling_num_text,selling_location_text,selling_phone_text,selling_user_name_text
        //lo_la_text,auto_text,location_detail_text,temperature_text
        if (Locale.getDefault().getLanguage().equals("en")) {
            brand_name_text.setText("Brand Name");
            category_text.setText("Category");
            selling_date.setText("Selling Date");
            shelf_life_text.setText("Shelf Life");
            base_text.setText("Basic Information");
            management_text.setText("Management Information");
            selling_text.setText("Selling Information");
            parameter_text.setText("Parameter Information");
            manufacturer_text.setText("Manufacturer and Warranty");
            repair_text.setText("Repair Record");
            phone_record_text.setText("Call Record");

            back_text.setText("Back");
            device_dateil_text.setText("Device dateil");
            next_text.setText("Repair");

            company_text.setText("Company Name");
            deptName_text.setText("Department");
            managementPhone_text.setText("Management Phone");
            managementName_text.setText("Management Name");

            selling_name_text.setText("Selling Name");
            selling_num_text.setText("Selling Number");
            selling_location_text.setText("Selling Location");
            selling_phone_text.setText("Selling Phone");
            selling_user_name_text.setText("Selling User Name");

            lo_la_text.setText("La&lo");
            auto_text.setText("Automatic positioning");
            location_detail_text.setText("Address");
            temperature_text.setText("Temperature");
            no_data_text.setText("No Data");
        }
    }

    private void getBundleString() {
        Bundle getStringValue = this.getIntent().getExtras();
        if (getStringValue.getString("device_mac") != null) {
            device_mac = getStringValue.getString("device_mac");
        }
    }

    private void getCache() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        String url = "http://192.168.1.104:8080/app/ppt6000/dataPpt6000Is.do";
        ts = String.valueOf(new Date().getTime());
        System.out.println("首页：" + memid + "  ts:" + ts + "  token:" + token);
        String Sign = url + memid + token + ts;
        System.out.println("Sign:" + Sign);
        sign = MD5Utils.md5(Sign);

        new Thread(networkTask).start();//获取该设备详情
    }

    private void init() {
        device_name = findViewById(R.id.device_name);//设备名
        fa_location = findViewById(R.id.fa_location);//详细地址
        findViewById(R.id.management_layout).setOnClickListener(this);//管理信息
        findViewById(R.id.new_manufacturer_layout).setOnClickListener(this);//制造商
        findViewById(R.id.new_parameter_layout).setOnClickListener(this);//设备参数
        findViewById(R.id.new_selling_layout).setOnClickListener(this);//售点信息
        findViewById(R.id.base_layout).setOnClickListener(this);//基本信息
        findViewById(R.id.repair_btn).setOnClickListener(this);//一键报修
        findViewById(R.id.repair_record_layout).setOnClickListener(this);//维修记录
        findViewById(R.id.phone_record_lay).setOnClickListener(this);//调拨记录

        findViewById(R.id.back).setOnClickListener(this);//返回;
        findViewById(R.id.edit_img).setOnClickListener(this);//修改
        findViewById(R.id.delete_img).setOnClickListener(this);//删除

        loading = findViewById(R.id.loading_layout);

        base_layout = findViewById(R.id.base_down_layout);
        management_layout = findViewById(R.id.management_down_layout);
        manufacturer_layout = findViewById(R.id.manufacturer_down_layout);
        parameter_layout = findViewById(R.id.parameter_down_layout);
        selling_point_layout = findViewById(R.id.selling_down_layout);
        record_layout = findViewById(R.id.new_repair_record_layout);
        phone_record_layout = findViewById(R.id.phone_record_layout);
        //record_layout=findViewById(R.id.record_layout);
        /* management_img,selling_point_img,parameter_img,manufacturer_img,record_img,call_record_img;  箭头图片的初始化*/

        management_img = findViewById(R.id.management_img);
        selling_point_img = findViewById(R.id.selling_img);
        parameter_img = findViewById(R.id.parameter_img);
        manufacturer_img = findViewById(R.id.manufacturer_img);
        base_img = findViewById(R.id.base_img);
        repair_img = findViewById(R.id.repair_img);
        eqpFlag = findViewById(R.id.eqpFlag);
        phone_record_img = findViewById(R.id.phone_record_img);
//        record_img=findViewById(R.id.record_img);
//        call_record_img=findViewById(R.id.call_record_img);
        //   findViewById(R.id.repair_img).setOnClickListener(this);

        //base_text,management_text,selling_text,parameter_text,manufacturer_text;信息标题
        base_text = findViewById(R.id.base_text);
        management_text = findViewById(R.id.management_text);
        selling_text = findViewById(R.id.selling_text);
        parameter_text = findViewById(R.id.parameter_text);
        manufacturer_text = findViewById(R.id.manufacturer_text);
        repair_text = findViewById(R.id.repair_text);
        phone_record_text = findViewById(R.id.phone_record_text);

        //selling_name,selling_num,selling_location,selling_phone,selling_user_name
        selling_name = findViewById(R.id.selling_name);
        selling_num = findViewById(R.id.selling_num);
        selling_location = findViewById(R.id.selling_location);
        selling_phone = findViewById(R.id.selling_phone);
        selling_user_name = findViewById(R.id.selling_user_name);


        //brand_name_text,category_text,selling_date,shelf_life_text,company_text,deptName_text,managementPhone_text,managementName_text,
        // selling_name_text,selling_num_text,selling_location_text,selling_phone_text,selling_user_name_text
        //lo_la_text,auto_text,location_detail_text,temperature_text
        brand_name_text = findViewById(R.id.brand_name_text);
        category_text = findViewById(R.id.category_text);
        selling_date = findViewById(R.id.selling_date);
        shelf_life_text = findViewById(R.id.shelf_life_text);

        back_text = findViewById(R.id.back_text);
        device_dateil_text = findViewById(R.id.device_detail);
        next_text = findViewById(R.id.next);

        company_text = findViewById(R.id.company_text);
        deptName_text = findViewById(R.id.deptName_text);
        managementPhone_text = findViewById(R.id.managementPhone_text);
        managementName_text = findViewById(R.id.managementName_text);
        selling_name_text = findViewById(R.id.selling_name_text);
        selling_num_text = findViewById(R.id.selling_num_text);
        selling_location_text = findViewById(R.id.selling_location_text);
        selling_phone_text = findViewById(R.id.selling_phone_text);
        selling_user_name_text = findViewById(R.id.selling_user_name_text);
        lo_la_text = findViewById(R.id.lo_la_text);
        auto_text = findViewById(R.id.auto_text);
        location_detail_text = findViewById(R.id.location_detail_text);
        temperature_text = findViewById(R.id.temperature_text);
        no_data_text = findViewById(R.id.no_data_text);
    }

    //按钮点击事件
    @Override
    public void onClick(View view) {
        final Intent intent;
        switch (view.getId()) {
            case R.id.phone_record_lay://调拨记录
                management_layout.setVisibility(View.GONE);
                base_layout.setVisibility(View.GONE);
                manufacturer_layout.setVisibility(View.GONE);
                parameter_layout.setVisibility(View.GONE);
                selling_point_layout.setVisibility(View.GONE);
                record_layout.setVisibility(View.GONE);
                phone_record_layout.setVisibility(View.VISIBLE);

                management_img.setImageResource(R.mipmap.icon_info_down);
                selling_point_img.setImageResource(R.mipmap.icon_info_down);
                parameter_img.setImageResource(R.mipmap.icon_info_down);
                manufacturer_img.setImageResource(R.mipmap.icon_info_down);
                base_img.setImageResource(R.mipmap.icon_info_down);
                repair_img.setImageResource(R.mipmap.icon_info_down);
                phone_record_img.setImageResource(R.mipmap.icon_info_up);

                //base_text,management_text,selling_text,parameter_text,manufacturer_text
                base_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                management_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                selling_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                parameter_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                manufacturer_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                repair_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                phone_record_text.setTextColor(ContextCompat.getColor(this, R.color.theme_color));

                break;
            case R.id.management_layout://管理信息
                management_layout.setVisibility(View.VISIBLE);
                base_layout.setVisibility(View.GONE);
                manufacturer_layout.setVisibility(View.GONE);
                parameter_layout.setVisibility(View.GONE);
                selling_point_layout.setVisibility(View.GONE);
                record_layout.setVisibility(View.GONE);
                phone_record_layout.setVisibility(View.GONE);

                management_img.setImageResource(R.mipmap.icon_info_up);
                selling_point_img.setImageResource(R.mipmap.icon_info_down);
                parameter_img.setImageResource(R.mipmap.icon_info_down);
                manufacturer_img.setImageResource(R.mipmap.icon_info_down);
                base_img.setImageResource(R.mipmap.icon_info_down);
                repair_img.setImageResource(R.mipmap.icon_info_down);
                phone_record_img.setImageResource(R.mipmap.icon_info_down);

                //base_text,management_text,selling_text,parameter_text,manufacturer_text
                base_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                management_text.setTextColor(ContextCompat.getColor(this, R.color.theme_color));
                selling_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                parameter_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                manufacturer_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                repair_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                phone_record_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));

                //poc,deptName,companyName,managementName,managementPhone
                poc = findViewById(R.id.poc);
                deptName = findViewById(R.id.deptName);
                companyName = findViewById(R.id.companyName);
                managementName = findViewById(R.id.managementName);
                managementPhone = findViewById(R.id.managementPhone);

                try {
                    if (DataObject.getString("managerInfoJson") != null) {
                        System.out.println("管理信息：" + DataObject.getString("managerInfoJson"));
                        JSONObject jsonObject = new JSONObject(DataObject.getString("managerInfoJson"));
                        poc.setText(jsonObject.getString("poc"));
                        deptName.setText(jsonObject.getString("deptName"));
                        companyName.setText(jsonObject.getString("companyName"));
                        managementName.setText(jsonObject.getString("managementName"));
                        managementPhone.setText(jsonObject.getString("managementPhone"));
                    } else {
                        System.out.println("管理无信息");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.new_manufacturer_layout://制造商
                management_layout.setVisibility(View.GONE);
                base_layout.setVisibility(View.GONE);
                manufacturer_layout.setVisibility(View.VISIBLE);
                parameter_layout.setVisibility(View.GONE);
                selling_point_layout.setVisibility(View.GONE);
                record_layout.setVisibility(View.GONE);
                phone_record_layout.setVisibility(View.GONE);

                management_img.setImageResource(R.mipmap.icon_info_down);
                selling_point_img.setImageResource(R.mipmap.icon_info_down);
                parameter_img.setImageResource(R.mipmap.icon_info_down);
                manufacturer_img.setImageResource(R.mipmap.icon_info_up);
                base_img.setImageResource(R.mipmap.icon_info_down);
                repair_img.setImageResource(R.mipmap.icon_info_down);
                phone_record_img.setImageResource(R.mipmap.icon_info_down);

                base_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                management_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                selling_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                parameter_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                manufacturer_text.setTextColor(ContextCompat.getColor(this, R.color.theme_color));
                repair_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                phone_record_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));

                manufacturer_list = findViewById(R.id.manufacturer_list);

                try {
                    if (DataObject.getString("ownerInfoJson") != null) {
                        System.out.println("制造商信息：" + DataObject.getString("ownerInfoJson"));
                        ArrayList<String> ownerInfoJson_list = new ArrayList<String>();
                        ownerInfoJson_list.add(DataObject.getString("ownerInfoJson"));
                        manufacturer_list.setAdapter(new ManufacturerAdapter(this, ownerInfoJson_list));

                    } else {
                        System.out.println("制造商无信息");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.new_parameter_layout://参数信息
                management_layout.setVisibility(View.GONE);
                base_layout.setVisibility(View.GONE);
                manufacturer_layout.setVisibility(View.GONE);
                parameter_layout.setVisibility(View.VISIBLE);
                selling_point_layout.setVisibility(View.GONE);
                record_layout.setVisibility(View.GONE);
                phone_record_layout.setVisibility(View.GONE);

                management_img.setImageResource(R.mipmap.icon_info_down);
                selling_point_img.setImageResource(R.mipmap.icon_info_down);
                parameter_img.setImageResource(R.mipmap.icon_info_up);
                manufacturer_img.setImageResource(R.mipmap.icon_info_down);
                base_img.setImageResource(R.mipmap.icon_info_down);
                repair_img.setImageResource(R.mipmap.icon_info_down);
                phone_record_img.setImageResource(R.mipmap.icon_info_down);


                base_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                management_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                selling_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                parameter_text.setTextColor(ContextCompat.getColor(this, R.color.theme_color));
                manufacturer_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                repair_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                phone_record_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));

                //latitude,longitude,temperature,loaction,location_detail
                latitude = findViewById(R.id.latitude);
                longitude = findViewById(R.id.longitude);
                temperature = findViewById(R.id.temperature);
                loaction = findViewById(R.id.auto_location);
                location_detail = findViewById(R.id.location_detail);

                try {
                    if (DataObject.getString("eqpAddressJson") != null) {
                        JSONObject jsonObject = new JSONObject(DataObject.getString("eqpAddressJson"));
                        location_detail.setText(jsonObject.getString("fa"));
                        latitude.setText(jsonObject.getString("la"));
                        longitude.setText(jsonObject.getString("lo"));
                        loaction.setText(jsonObject.getString("pv") + jsonObject.getString("ct"));
                        temperature.setText(jsonObject.getString("te"));
                        //location_detail.setText(jsonObject.getString("fa"));
                        System.out.println("参数信息：" + DataObject.getString("eqpAddressJson"));
                    } else {
                        System.out.println("参数无信息");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.new_selling_layout://售点信息
                management_layout.setVisibility(View.GONE);
                base_layout.setVisibility(View.GONE);
                manufacturer_layout.setVisibility(View.GONE);
                parameter_layout.setVisibility(View.GONE);
                selling_point_layout.setVisibility(View.VISIBLE);
                record_layout.setVisibility(View.GONE);
                phone_record_layout.setVisibility(View.GONE);

                management_img.setImageResource(R.mipmap.icon_info_down);
                selling_point_img.setImageResource(R.mipmap.icon_info_up);
                parameter_img.setImageResource(R.mipmap.icon_info_down);
                manufacturer_img.setImageResource(R.mipmap.icon_info_down);
                base_img.setImageResource(R.mipmap.icon_info_down);
                repair_img.setImageResource(R.mipmap.icon_info_down);
                phone_record_img.setImageResource(R.mipmap.icon_info_down);

                base_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                management_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                selling_text.setTextColor(ContextCompat.getColor(this, R.color.theme_color));
                parameter_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                manufacturer_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                repair_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                phone_record_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));

                try {
                    if (DataObject.getString("userInfoJson") != null) {
                        System.out.println("售点信息：" + DataObject.getString("userInfoJson"));
                        JSONObject jsonObject = new JSONObject(DataObject.getString("userInfoJson"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("addressInfoJson"));
                        selling_name.setText(jsonObject.getString("pointOfSaleName"));
                        selling_num.setText(jsonObject.getString("userId"));
                        selling_phone.setText(jsonObject.getString("pointOfSalePhone"));
                        selling_location.setText(jsonObject1.getString("fa"));
                        selling_user_name.setText(jsonObject.getString("username"));

                    } else {
                        System.out.println("售点无信息");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.base_layout://基本信息
                management_layout.setVisibility(View.GONE);
                base_layout.setVisibility(View.VISIBLE);
                manufacturer_layout.setVisibility(View.GONE);
                parameter_layout.setVisibility(View.GONE);
                selling_point_layout.setVisibility(View.GONE);
                record_layout.setVisibility(View.GONE);
                phone_record_layout.setVisibility(View.GONE);

                management_img.setImageResource(R.mipmap.icon_info_down);
                selling_point_img.setImageResource(R.mipmap.icon_info_down);
                parameter_img.setImageResource(R.mipmap.icon_info_down);
                manufacturer_img.setImageResource(R.mipmap.icon_info_down);
                base_img.setImageResource(R.mipmap.icon_info_up);
                repair_img.setImageResource(R.mipmap.icon_info_down);
                phone_record_img.setImageResource(R.mipmap.icon_info_down);

                base_text.setTextColor(ContextCompat.getColor(this, R.color.theme_color));
                management_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                selling_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                parameter_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                manufacturer_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                repair_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                phone_record_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));

                //ssid,macNo,eqpBrand,eqpStyle,startDate,protectDateTo
                ssid = findViewById(R.id.ssid);
                macNo = findViewById(R.id.macNo);
                eqpBrand = findViewById(R.id.eqpBrand);
                eqpStyle = findViewById(R.id.eqpStyle);
                startDate = findViewById(R.id.startDate);
                protectDateTo = findViewById(R.id.protectDateTo);

                try {
                    ssid.setText(DataObject.getString("ssid"));
                    macNo.setText(DataObject.getString("macNo"));
                    eqpBrand.setText(DataObject.getString("eqpBrand"));
                    eqpStyle.setText(DataObject.getString("eqpStyle"));
                    startDate.setText(DataObject.getString("startDate"));
                    protectDateTo.setText(DataObject.getString("protectDateTo"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.repair_record_layout://维修记录

                management_layout.setVisibility(View.GONE);
                base_layout.setVisibility(View.GONE);
                manufacturer_layout.setVisibility(View.GONE);
                parameter_layout.setVisibility(View.GONE);
                selling_point_layout.setVisibility(View.GONE);
                record_layout.setVisibility(View.VISIBLE);
                phone_record_layout.setVisibility(View.GONE);


                management_img.setImageResource(R.mipmap.icon_info_down);
                selling_point_img.setImageResource(R.mipmap.icon_info_down);
                parameter_img.setImageResource(R.mipmap.icon_info_down);
                manufacturer_img.setImageResource(R.mipmap.icon_info_down);
                base_img.setImageResource(R.mipmap.icon_info_down);
                repair_img.setImageResource(R.mipmap.icon_info_up);
                phone_record_img.setImageResource(R.mipmap.icon_info_down);


                base_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                management_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                selling_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                parameter_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                manufacturer_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));
                repair_text.setTextColor(ContextCompat.getColor(this, R.color.theme_color));
                phone_record_text.setTextColor(ContextCompat.getColor(this, R.color.title_color));

//                if(isRecord){
//                    record_layout.setVisibility(View.VISIBLE);
//                    isRecord=!isRecord;
//                    record_img.setImageResource(R.drawable.up_jiantou);
//                }else{
//                    record_layout.setVisibility(View.GONE);
//                    isRecord=!isRecord;
//                    record_img.setImageResource(R.drawable.butoom_jiantou);
//                }
                break;
            case R.id.repair_btn://报修按钮
                //selling_name,selling_num,selling_location,selling_phone,selling_user_name
                String selling_name_str = selling_name.getText().toString().trim();
                String selling_num_str = selling_name.getText().toString().trim();
                String selling_location_str = selling_name.getText().toString().trim();
                String selling_phone_str = selling_name.getText().toString().trim();
                String selling_user_name_str = selling_name.getText().toString().trim();

                if (selling_name_str.equals("") || selling_num_str.equals("") || selling_location_str.equals("") || selling_phone_str.equals("") || selling_user_name_str.equals("")) {
                    new AlertDialog.Builder(DeviceDetailActivity.this).setTitle("报修提示").setMessage("请完善设备信息,再报修！！！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
                } else {
                    intent = new Intent(DeviceDetailActivity.this, CreatRepairActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;
            case R.id.edit_img://编辑
//                isEdit=!isEdit;
//                //selling_point_name,selling_point_number,selling_point_location,selling_point_phone,selling_point_user
//                selling_point_name=findViewById(R.id.selling_point_name);//售点名称
//                selling_point_number=findViewById(R.id.selling_point_number);//售点编号
//                selling_point_location=findViewById(R.id.selling_point_location);//售点地址
//                selling_point_phone=findViewById(R.id.selling_point_phone);//售点人号码
//                selling_point_user=findViewById(R.id.selling_point_user);//售点人姓名
//                parameter_location=findViewById(R.id.parameter_detail_location);//参数详细地址
//
//                //latitude,longitude,temperature,location_detail
//                longitude=findViewById(R.id.longitude);
//                latitude=findViewById(R.id.latitude);
//                temperature=findViewById(R.id.temperature);
//                location_detail=findViewById(R.id.parameter_location);
//
//                if(isEdit){//判断可否编辑
//                    ImageView img=findViewById(R.id.edit);
//                    img.setImageResource(R.drawable.yes);
//
//                    selling_point_name.setEnabled(true);
//                    selling_point_number.setEnabled(true);
//                    selling_point_location.setEnabled(true);
//                    selling_point_phone.setEnabled(true);
//                    selling_point_user.setEnabled(true);
//                    parameter_location.setEnabled(true);
//                }else{
//                    ImageView img=findViewById(R.id.edit);
//                    img.setImageResource(R.drawable.edit_img);
//
//                    selling_point_name.setEnabled(false);
//                    selling_point_number.setEnabled(false);
//                    selling_point_location.setEnabled(false);
//                    selling_point_phone.setEnabled(false);
//                    selling_point_user.setEnabled(false);
//                    parameter_location.setEnabled(false);
//                }
//
//                //获取地址
//                if(str_location ==null || str_location.isEmpty()){
//                    Toast.makeText(this,"请打开网络,重新进入",Toast.LENGTH_SHORT).show();
//                }else{
//                    if(longitude_str.length()>14||latitude_str.length()>14){
//                        longitude_str=longitude_str.substring(0,15);
//                        latitude_str=latitude_str.substring(0,15);
//                    }
//                    longitude.setText(longitude_str);
//                    latitude.setText( latitude_str);
//                    location_detail.setText(str_location);
//                }
//                temperature.setText(temperature_str+"℃");

                intent = new Intent(DeviceDetailActivity.this, PerfectDeviceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("device_mac", device_mac);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.delete_img:
                CustomDialog.Builder builder = new CustomDialog.Builder(this);
                builder.setMessage("是否删除该设备");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent1 = new Intent(DeviceDetailActivity.this, DeviceListActivity.class);
                        startActivity(intent1);
                    }
                });

                builder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
                break;
            case R.id.back://返回
                finish();
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = VERSION_CODES.M)
    private void getLocation() {
        //获取地址
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        } else {
            LocationUtil.initLocation(DeviceDetailActivity.this);
            System.out.println("主经度:" + Double.toString(LocationUtil.longitude) + "主纬度：" + Double.toString(LocationUtil.latitude));
            longitude_str = Double.toString(LocationUtil.longitude);
            latitude_str = Double.toString(LocationUtil.latitude);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    str_location = getAddress(LocationUtil.location, getApplicationContext());
                    //位置信息-----一个字符串
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendRequestWithHttpClient(final Context context, final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //用HttpClient发送请求，分为五步
                //第一步：创建HttpClient对象
                HttpClient httpCient = new DefaultHttpClient();
                //第二步：创建代表请求的对象,参数是访问的服务器地址blMac=12:34:56:78:9A:BC&userId=1001&userName=%E5%BC%A0%E4%B8%89
                //HttpGet httpGet = new HttpGet("http://192.168.1.101:8080/appUser/appUserLogin.do?loginType=1&loginAccount="+name1+"&loginPwd="+pass1);
                HttpGet httpGet = new HttpGet(url);//测试链接
                try {
                    //第三步：执行请求，获取服务器发还的相应对象
                    HttpResponse httpResponse = httpCient.execute(httpGet);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    System.out.println("状态码：" + httpResponse.getStatusLine().getStatusCode());
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity = httpResponse.getEntity();
                        String result = EntityUtils.toString(entity, "utf-8");//将entity当中的数据转换为字符串
                        if (result != null) {
                            //在子线程中将Message对象发出去
                            Message message = new Message();
                            message.what = SHOW_RESPONSE;
                            message.obj = result.toString();
                            System.out.println("返回结果：" + result);
                            handler.sendMessage(message);
                        }
                    } else {
                        System.out.println("访问失败！！！");
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println("访问失败！！！");
                    e.printStackTrace();
                }
            }
        }).start();//这个start()方法不要忘记了
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        System.out.println("返回：" + responseObject);
                        JSONArray jsonArray = new JSONArray(responseObject.getString("results"));
                        JSONObject jsonObject = new JSONObject(jsonArray.get(0).toString());
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("now"));
                        System.out.println("温度：" + jsonObject1.getString("temperature"));
                        temperature_str = jsonObject1.getString("temperature");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

    };

    private void getTemperature() {
        try {
            String str = LocationUtil.getAddress(LocationUtil.location, getApplicationContext());

        } catch (IOException e) {
            e.printStackTrace();
        }
        province_str = LocationUtil.province;
        System.out.println("市：" + province_str);
        String url = "https://api.seniverse.com/v3/weather/now.json?key=hifwkocphbol8biw&location=" + province_str + "&language=zh-Hans&unit=c";
        sendRequestWithHttpClient(this, url);//获取温度的方法

    }

    /**
     * 网络操作相关的子线程okhttp框架  获取设备详情
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String url = "http://192.168.1.104:8080/app/ppt6000/dataPpt6000Is.do?memId=" + memid + "&ts=" + ts + "&macNo=" + device_mac;
            OkHttpClient okHttpClient = new OkHttpClient();
            System.out.println("验证：" + sign);
            String b = "{\"memId\":\"" + memid + "\",\"macNo\":\"" + device_mac + "\"}";//json字符串
            System.out.println("设备详情参数：" + b);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), b);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("sIgn", sign)
                    .build();
            System.out.println(request.headers());
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                String result = response.body().string();
                if (response != null) {
                    //在子线程中将Message对象发出去
                    if (response.code() == 200) {
                        Message message = new Message();
                        message.what = SHOW_RESPONSE;
                        message.obj = result.toString();
                        getDeviceListHandler.sendMessage(message);
                    } else {
                        System.out.println("结果：" + result);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    private Handler getDeviceListHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    System.out.println(response);
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject object1 = new JSONObject(object.getString("meta"));
                        //{"meta":{"res":"99999","msg":"用户名或密码有误"},"data":null}状态码：200
                        if (object1.getString("res").equals("00000")) {
                            if (object.getString("data") != null && !object.getString("data").equals("null")) {
                                DataObject = new JSONObject(object.getString("data"));//该设备数据
                                loading.setStatus(LoadingLayout.Success);
                                JSONObject FAObject = new JSONObject(DataObject.getString("eqpAddressJson"));//该设备数据
                                System.out.println("data:  " + DataObject);
                                device_name.setText(DataObject.getString("eqpName"));
                                if (DataObject.getString("eqpFlag").equals("0")) {
                                    eqpFlag.setImageResource(R.mipmap.icon_equiplist_scan);
                                } else if (DataObject.getString("eqpFlag").equals("1")) {
                                    eqpFlag.setImageResource(R.mipmap.icon_info_wifi_active);
                                } else {
                                    eqpFlag.setImageResource(R.mipmap.icon_info_bluetooth_active);
                                }

                                fa_location.setText(FAObject.getString("fa"));
                            } else {
                                new AlertDialog.Builder(DeviceDetailActivity.this).setTitle("设备提示").setMessage("无此设备,请重新选择").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).show();
                            }
                        } else {
                            new AlertDialog.Builder(DeviceDetailActivity.this).setTitle("网络提示").setMessage("请检查网络是否畅通").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

    };
}