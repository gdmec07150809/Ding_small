package com.example.administrator.ding_small;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.HelpTool.LocationUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import static com.example.administrator.ding_small.HelpTool.LocationUtil.getAddress;
import static com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity.SHOW_RESPONSE;
import static com.example.administrator.ding_small.R.id.date;

/**
 * Created by Administrator on 2017/12/21.
 */

public class PerfectDeviceActivity extends Activity implements View.OnClickListener{
    private TextView location_text,photo_text,reimbursement_text,parameter_text,management_text,date_text,latitude,longitude,location,temperature,location_detail;
    InputMethodManager imm;
    private Dialog mCameraDialog;
    private ImageView photo1,photo2,photo3,photo4;
    int nowYear,nowMonth,nowDay;
    private static  final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION  = 100;
    private String str_location=null;
    private  String latitude_str,longitude_str,province_str,temperature_str,result;
    @RequiresApi(api = VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfect_device);
        init();//初始化控件
        getTimeNow();//获取当前时间
        getLocation();//获取当前经纬度
        getTemperature();//获取当前温度
    }
    private void getTemperature(){
        try {
            String str=LocationUtil.getAddress(LocationUtil.location,getApplicationContext());

        } catch (IOException e) {
            e.printStackTrace();
        }
        province_str=LocationUtil.province;
        System.out.println("市："+province_str);
        String url="https://api.seniverse.com/v3/weather/now.json?key=hifwkocphbol8biw&location="+province_str+"&language=zh-Hans&unit=c";
        System.out.println(url);
        sendRequestWithHttpClient(this,url);//获取温度的方法

//        String result=SendUrlUtils.sendRequestHttpClient(this,url);
//        System.out.println("结果22："+result);

    }
    private void getTimeNow(){
        //获取当前年月日时分
        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        nowYear = t.year;
        nowMonth= t.month;
        nowDay= t.monthDay;
        date_text.setText(nowYear+"-"+(nowMonth+1)+"-"+nowDay);
        System.out.println("日期："+nowYear+"-"+nowMonth+"-"+nowDay);
    }
    @RequiresApi(api = VERSION_CODES.M)
    private void getLocation(){
        //获取地址
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        } else {
            LocationUtil.initLocation(PerfectDeviceActivity.this);
            System.out.println("主经度:"+Double.toString(LocationUtil.longitude)+"主纬度："+Double.toString(LocationUtil.latitude));
            longitude_str=Double.toString(LocationUtil.longitude);
            latitude_str=Double.toString(LocationUtil.latitude);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    str_location= getAddress(LocationUtil.location,getApplicationContext());
                    //位置信息-----一个字符串
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void init(){
        findViewById(R.id.photo_layout).setOnClickListener(this);
        findViewById(R.id.location_layout).setOnClickListener(this);
        findViewById(R.id.reimbursement_layout).setOnClickListener(this);
        findViewById(R.id.parameter_layout).setOnClickListener(this);
        findViewById(R.id.management_layout).setOnClickListener(this);
        findViewById(R.id.more_device_name).setOnClickListener(this);
        findViewById(R.id.more_mac).setOnClickListener(this);
        findViewById(R.id.more_poc).setOnClickListener(this);
        findViewById(R.id.confrim_btn).setOnClickListener(this);

        date_text=findViewById(date);
        date_text.setOnClickListener(this);
        location_text=findViewById(R.id.location_text);
        photo_text=findViewById(R.id.photo_text);
        reimbursement_text=findViewById(R.id.reimbursement_text);
        parameter_text=findViewById(R.id.parameter_text);
        management_text=findViewById(R.id.management_text);
        location_detail=findViewById(R.id.location_detail);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.photo_layout://备注图片事件
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.c6_bg);
                findViewById(R.id.remarks_reimbursement).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_parameter).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_management).setBackgroundResource(R.drawable.hui_bg);

                findViewById(R.id.remarks_photo_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.repair_location_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_user_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_parameter_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_management_layout).setVisibility(View.GONE);

                location_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                photo_text.setTextColor(ContextCompat.getColor(this,R.color.green));
                reimbursement_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                parameter_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                management_text.setTextColor(ContextCompat.getColor(this,R.color.blank));

                photo1=findViewById(R.id.photo1);
                photo2=findViewById(R.id.photo2);
                photo3=findViewById(R.id.photo3);
                photo4=findViewById(R.id.photo4);
                photo1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDialog(1);
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(intent, 0);
                    }
                });
                photo2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDialog(2);
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(intent, 0);
                    }
                });
                photo3.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDialog(3);
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(intent, 0);
                    }
                });
                photo4.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDialog(4);
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(intent, 0);
                    }
                });
                photo1.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        showDetelePhotoDialog(1);
                        return true;
                    }
                });
                photo2.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        showDetelePhotoDialog(2);
                        return true;
                    }
                });
                photo3.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        showDetelePhotoDialog(3);
                        return true;
                    }
                });
                photo4.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        showDetelePhotoDialog(4);
                        return true;
                    }
                });
                break;
            case R.id.btn_open_camera://打开相机1
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 11);
                break;
            case R.id.btn_open_camera2://打开相机2
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,12);
                break;
            case R.id.btn_open_camera3://打开相机3
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 13);
                break;
            case R.id.btn_open_camera4://打开相机4
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 14);
                break;
            case R.id.btn_cancel://上弹菜单取消事件
                mCameraDialog.dismiss();
                break;
            case R.id.btn_choose_img://选择相册1
                intent= new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 21);
                break;
            case R.id.btn_choose_img2://选择相册2
                intent= new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 22);
                break;
            case R.id.btn_choose_img3://选择相册3
                intent= new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,23 );
                break;
            case R.id.btn_choose_img4://选择相册4
                intent= new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 24);
                break;
            case R.id.location_layout://备注地址事件
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.c6_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_reimbursement).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_parameter).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_management).setBackgroundResource(R.drawable.hui_bg);

                findViewById(R.id.remarks_photo_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_location_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.repair_user_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_parameter_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_management_layout).setVisibility(View.GONE);

                location_text.setTextColor(ContextCompat.getColor(this,R.color.green));
                photo_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                reimbursement_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                parameter_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                management_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                break;
            case R.id.reimbursement_layout://备注维修人事件
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_reimbursement).setBackgroundResource(R.drawable.c6_bg);
                findViewById(R.id.remarks_parameter).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_management).setBackgroundResource(R.drawable.hui_bg);

                findViewById(R.id.remarks_photo_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_location_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_user_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.repair_parameter_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_management_layout).setVisibility(View.GONE);

                location_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                photo_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                reimbursement_text.setTextColor(ContextCompat.getColor(this,R.color.green));
                parameter_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                management_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                break;
            case R.id.parameter_layout://备注参数事件
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

                longitude=findViewById(R.id.longitude);
                latitude=findViewById(R.id.latitude);
                location=findViewById(R.id.location);
                temperature=findViewById(R.id.temperature);
                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_reimbursement).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_parameter).setBackgroundResource(R.drawable.c6_bg);
                findViewById(R.id.remarks_management).setBackgroundResource(R.drawable.hui_bg);

                findViewById(R.id.remarks_photo_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_location_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_user_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_parameter_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.repair_management_layout).setVisibility(View.GONE);

                location_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                photo_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                reimbursement_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                parameter_text.setTextColor(ContextCompat.getColor(this,R.color.green));
                management_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                //获取地址
                if(str_location ==null || str_location.isEmpty()){
                    Toast.makeText(this,"请打开网络,重新进入",Toast.LENGTH_SHORT).show();
                }else{
                    longitude.setText(longitude_str);
                    latitude.setText(latitude_str);
                    location.setText(str_location);
                }
                temperature.setText(temperature_str+"℃");
                break;
            case R.id.management_layout://备注管理人事件
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_reimbursement).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_parameter).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_management).setBackgroundResource(R.drawable.c6_bg);

                findViewById(R.id.remarks_photo_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_location_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_user_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_parameter_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_management_layout).setVisibility(View.VISIBLE);

                location_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                photo_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                reimbursement_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                parameter_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                management_text.setTextColor(ContextCompat.getColor(this,R.color.green));
                break;
            case R.id.more_device_name://设备更多信息
                showMoreInformation(1);
                break;
            case R.id.more_mac:
                showMoreInformation(2);//mac
                break;
            case R.id.more_poc://运营中心列表
                showMoreInformation(3);
                break;
            case R.id.confrim_btn:
                String location_detail_str=location_detail.getText().toString();
                if (location_detail_str==null||location_detail_str.equals("")){
                    Toast.makeText(this,"请输入详细地址",Toast.LENGTH_SHORT).show();
                }else {
                    intent=new Intent(PerfectDeviceActivity.this,DeviceListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;
            case date:
                new DatePickerDialog(PerfectDeviceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date_text.setText(String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth));
                    }
                },nowYear,nowMonth,nowDay).show();
                break;
            default:
                break;
        }
    }
    //获取,处理拍照事件
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        mCameraDialog.dismiss();
        findViewById(R.id.remarks_photo_layout).setVisibility(View.VISIBLE);
        //判断那个相机回调
        switch (requestCode){
            case 11:
                if (resultCode == Activity.RESULT_OK) {
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                        Log.i("TestFile",
                                "SD card is not avaiable/writeable right now.");
                        Toast.makeText(PerfectDeviceActivity.this,"sd卡不可用！！！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new DateFormat();
                    String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".png";
                    System.out.println("路径："+name);
                    Toast.makeText(this, name, Toast.LENGTH_LONG).show();
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

                    FileOutputStream b = null;
                    File file = new File("/sdcard/Image/");
                    file.mkdirs();// 创建文件夹
                    String fileName = "/sdcard/Image/"+name;

                    try {
                        b = new FileOutputStream(fileName);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if(b!=null){
                                b.flush();
                                b.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try
                    {
                        photo1.setImageBitmap(bitmap);// 将图片显示在ImageView里
                    }catch(Exception e)
                    {
                        Log.e("error", e.getMessage());
                    }

                }
                break;
            case 12:
                if (resultCode == Activity.RESULT_OK) {
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                        Log.i("TestFile",
                                "SD card is not avaiable/writeable right now.");
                        Toast.makeText(PerfectDeviceActivity.this,"sd卡不可用！！！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new DateFormat();
                    String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".png";
                    System.out.println("路径："+name);
                    Toast.makeText(this, name, Toast.LENGTH_LONG).show();
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

                    FileOutputStream b = null;
                    File file = new File("/sdcard/Image/");
                    file.mkdirs();// 创建文件夹
                    String fileName = "/sdcard/Image/"+name;

                    try {
                        b = new FileOutputStream(fileName);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if(b!=null){
                                b.flush();
                                b.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try
                    {
                        photo2.setImageBitmap(bitmap);// 将图片显示在ImageView里
                    }catch(Exception e)
                    {
                        Log.e("error", e.getMessage());
                    }

                }
                break;
            case 13:
                if (resultCode == Activity.RESULT_OK) {
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                        Log.i("TestFile",
                                "SD card is not avaiable/writeable right now.");
                        Toast.makeText(PerfectDeviceActivity.this,"sd卡不可用！！！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new DateFormat();
                    String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".png";
                    System.out.println("路径："+name);
                    Toast.makeText(this, name, Toast.LENGTH_LONG).show();
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

                    FileOutputStream b = null;
                    File file = new File("/sdcard/Image/");
                    file.mkdirs();// 创建文件夹
                    String fileName = "/sdcard/Image/"+name;

                    try {
                        b = new FileOutputStream(fileName);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if(b!=null){
                                b.flush();
                                b.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try
                    {
                        photo3.setImageBitmap(bitmap);// 将图片显示在ImageView里
                    }catch(Exception e)
                    {
                        Log.e("error", e.getMessage());
                    }

                }
                break;
            case 14:
                if (resultCode == Activity.RESULT_OK) {
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                        Log.i("TestFile",
                                "SD card is not avaiable/writeable right now.");
                        Toast.makeText(PerfectDeviceActivity.this,"sd卡不可用！！！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new DateFormat();
                    String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".png";
                    System.out.println("路径："+name);
                    Toast.makeText(this, name, Toast.LENGTH_LONG).show();
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

                    FileOutputStream b = null;
                    File file = new File("/sdcard/Image/");
                    file.mkdirs();// 创建文件夹
                    String fileName = "/sdcard/Image/"+name;

                    try {
                        b = new FileOutputStream(fileName);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if(b!=null){
                                b.flush();
                                b.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try
                    {
                        photo4.setImageBitmap(bitmap);// 将图片显示在ImageView里
                    }catch(Exception e)
                    {
                        Log.e("error", e.getMessage());
                    }
                }
                break;
            case 21:
                //打开相册并选择照片，这个方式选择单张
                // 获取返回的数据，这里是android自定义的Uri地址
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    // 获取选择照片的数据视图
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    // 从数据视图中获取已选择图片的路径
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    // 将图片显示到界面上
                    photo1.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }
                break;
            case 22:
                //打开相册并选择照片，这个方式选择单张
                // 获取返回的数据，这里是android自定义的Uri地址
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    // 获取选择照片的数据视图
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    // 从数据视图中获取已选择图片的路径
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    // 将图片显示到界面上
                    photo2.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }
                break;
            case 23:
                //打开相册并选择照片，这个方式选择单张
                // 获取返回的数据，这里是android自定义的Uri地址
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    // 获取选择照片的数据视图
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    // 从数据视图中获取已选择图片的路径
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    // 将图片显示到界面上
                    photo3.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }
                break;
            case 24:
                //打开相册并选择照片，这个方式选择单张
                // 获取返回的数据，这里是android自定义的Uri地址
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    // 获取选择照片的数据视图
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    // 从数据视图中获取已选择图片的路径
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    // 将图片显示到界面上
                    photo4.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }
                break;
        }

    }
    private void showDetelePhotoDialog(final int number){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(PerfectDeviceActivity.this);
        normalDialog.setTitle("删除");
        normalDialog.setMessage("是否删除该图片？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //判断那个图片删除
                        switch (number){
                            case 1:
                                photo1.setImageResource(R.drawable.no_photo);
                                break;
                            case 2:
                                photo2.setImageResource(R.drawable.no_photo);
                                break;
                            case 3:
                                photo3.setImageResource(R.drawable.no_photo);
                                break;
                            case 4:
                                photo4.setImageResource(R.drawable.no_photo);
                                break;
                            default:
                                break;
                        }

                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
        // 显示
        normalDialog.show();
    }

    //底部弹出菜单
    private void setDialog(int number) {
        LinearLayout root=null;
        mCameraDialog = new Dialog(this, R.style.BottomDialog);
        //判断那个相机弹出
        switch (number){
            case 1:
                root = (LinearLayout) LayoutInflater.from(this).inflate(
                        R.layout.photo_menu, null);
                //初始化视图
                root.findViewById(R.id.btn_choose_img).setOnClickListener(this);
                root.findViewById(R.id.btn_open_camera).setOnClickListener(this);
                root.findViewById(R.id.btn_cancel).setOnClickListener(this);
                break;
            case 2:
                root = (LinearLayout) LayoutInflater.from(this).inflate(
                        R.layout.photo2_menu, null);
                //初始化视图
                root.findViewById(R.id.btn_choose_img2).setOnClickListener(this);
                root.findViewById(R.id.btn_open_camera2).setOnClickListener(this);
                root.findViewById(R.id.btn_cancel).setOnClickListener(this);
                break;
            case 3:
                root = (LinearLayout) LayoutInflater.from(this).inflate(
                        R.layout.photo3_menu, null);
                //初始化视图
                root.findViewById(R.id.btn_choose_img3).setOnClickListener(this);
                root.findViewById(R.id.btn_open_camera3).setOnClickListener(this);
                root.findViewById(R.id.btn_cancel).setOnClickListener(this);
                break;
            case 4:
                root = (LinearLayout) LayoutInflater.from(this).inflate(
                        R.layout.photo4_menu, null);
                //初始化视图
                root.findViewById(R.id.btn_choose_img4).setOnClickListener(this);
                root.findViewById(R.id.btn_open_camera4).setOnClickListener(this);
                root.findViewById(R.id.btn_cancel).setOnClickListener(this);
                break;
            default:
                break;
        }

        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.DialogAnimation); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

    //底部弹出下拉菜单
    private void showMoreInformation(int number) {
        LinearLayout root=null;//弹出布局
        mCameraDialog = new Dialog(this, R.style.BottomDialog);
        //判断需要哪个弹窗
        switch (number){
            case 1:
                root = (LinearLayout) LayoutInflater.from(this).inflate(
                        R.layout.device_name_bottom, null);
                break;
            case 2:
                root = (LinearLayout) LayoutInflater.from(this).inflate(
                        R.layout.device_mac, null);
                break;
            case 3:
                root = (LinearLayout) LayoutInflater.from(this).inflate(
                        R.layout.device_operation_center, null);
                break;
            default:
                break;
        }

        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.DialogAnimation); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }
    public  void   sendRequestWithHttpClient(final Context context, final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //用HttpClient发送请求，分为五步
                //第一步：创建HttpClient对象
                HttpClient httpCient = new DefaultHttpClient();
                //第二步：创建代表请求的对象,参数是访问的服务器地址blMac=12:34:56:78:9A:BC&userId=1001&userName=%E5%BC%A0%E4%B8%89
                //HttpGet httpGet = new HttpGet("http://192.168.1.101:8080/appUser/appUserLogin.do?loginType=1&loginAccount="+name1+"&loginPwd="+pass1);
                HttpPost httpPost = new HttpPost(url);//测试链接
                try {
                    //第三步：执行请求，获取服务器发还的相应对象
                    HttpResponse httpResponse = httpCient.execute(httpPost);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    System.out.println("状态码："+httpResponse.getStatusLine().getStatusCode());
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity = httpResponse.getEntity();
                        result = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串

                        if(result!=null){
                            //在子线程中将Message对象发出去
                            Message message = new Message();
                            message.what = SHOW_RESPONSE;
                            message.obj = result.toString();
                            System.out.println("返回结果1："+result);
                            handler.sendMessage(message);

                        }
                    }else{
                        Toast.makeText(context,"访问失败!!!请检查服务器...",Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println("访问失败！！！");
                    e.printStackTrace();
                }

            }
        }).start();//这个start()方法不要忘记了

    }
    //解析天气
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    try {
                        JSONObject responseObject=new JSONObject(response);
                        System.out.println("返回1："+responseObject);
                        JSONArray jsonArray=new JSONArray(responseObject.getString("results"));
                        JSONObject jsonObject=new JSONObject(jsonArray.get(0).toString());
                        JSONObject jsonObject1=new JSONObject(jsonObject.getString("now"));
                        System.out.println("温度："+jsonObject1.getString("temperature"));
                        temperature_str=jsonObject1.getString("temperature");
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
