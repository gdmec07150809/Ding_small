package com.example.administrator.ding_small;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.HelpTool.FlowLayout;
import com.example.administrator.ding_small.HelpTool.LocationUtil;
import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.example.administrator.ding_small.HelpTool.UploadUtil;
import com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity;
import com.example.administrator.ding_small.PersonalCenter.PersonalCenterPerfectActivity;
import com.example.administrator.ding_small.Utils.utils;
import com.weavey.loading.lib.LoadingLayout;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.administrator.ding_small.HelpTool.LocationUtil.getAddress;
import static com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity.SHOW_RESPONSE;
import static com.example.administrator.ding_small.R.id.add;
import static com.example.administrator.ding_small.R.id.content;
import static com.example.administrator.ding_small.R.id.date;
import static com.example.administrator.ding_small.R.id.perfect;

/**
 * Created by CZK on 2017/12/21.
 */

public class PerfectDeviceActivity extends Activity implements View.OnClickListener ,View.OnLongClickListener{
    //private TextView location_text,photo_text,reimbursement_text,parameter_text,management_text,date_text,latitude,longitude,location,temperature,location_detail;
    private TextView dateT, location_text, photo_text, reimbursement_text, parameter, management_text, at_action, latitude, longitude, location, temperature, information_text, remarks_text, adress_text;
    private ImageView new_information_img, new_photo_img, new_remarks_img;
    InputMethodManager imm;
    private Dialog mCameraDialog;
    private ImageView photo1, photo2, photo3, photo4;
    int nowYear, nowMonth, nowDay;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 100;
    private String str_location = null;
    private String latitude_str, longitude_str, province_str, temperature_str, result, device_mac,device_id;
    private FlowLayout found_activity_fyt;
    private ArrayList<Bitmap> arrayList = new ArrayList<Bitmap>();
    private EditText repair_user;
    //更改语言所要更改的控件 返回、完善设备信息、自定义名称、售点名称、售点地址、售点联系人、联系人电话、当前位置参数、具体地址,确定
    private TextView back_text, perfect_device_information, custom_name_text, selling_name_text, selling_location_text,
            selling_user_name_text, selling_phone_text, location_parameters_text, address_text, comfir_text,leapfrog,temperature_text;
    private EditText selling_edit_text, selling_location_edit_text, selling_user_edit_text, selling_phone_edit_text, address_edit_text;

    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    String memid, token, getSign, oldPass, newPass,upSign,ts, c_newPass,upPhotoSign;
    // longitude_str,latitude_str,str_location,temperature_str
    private JSONObject DataObject;
    String repair_user_str, selling_edit_str, selling_location_edit_str, selling_user_edit_str, selling_phone_edit_str, address_edit_str;
    private ArrayList<String>  upPhotoPath=new ArrayList<String>();
    private LoadingLayout loading;
    List<File> fileList=null;
    String fileName,act;
    private ImageView addimg;
    LinearLayout perfect,delect;
    @RequiresApi(api = VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_perfect_device);
        init();//初始化控件
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        changeTextView();//更改语言
        getBundleString();//获取页面传递数据
        //getTimeNow();//获取当前时间
        getLocation();//获取当前经纬度
        getTemperature();//获取当前温度
//        Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.icon_fix_addimg);
//        arrayList.add(icon);
        getCache1();//获取设备eqpId
        loading.setStatus(LoadingLayout.Loading);

        addimg.setClickable(true);
        addimg.setLongClickable(false);
    }

    private void getCache1() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        String url = utils.url+"/app/ppt6000/dataPpt6000Is.do";
        ts = String.valueOf(new Date().getTime());
        System.out.println("首页：" + memid + "  ts:" + ts + "  token:" + token);
        String Sign = url + memid + token + ts;
        System.out.println("Sign:" + Sign+" "+url);
        getSign = MD5Utils.md5(Sign);

        address_edit_text.setText(sp.getString("perfect_addres",""));
        new Thread(getDeta).start();//获取该设备详情
    }

    private void UpPhotoCache(){
        String url = utils.url+"/app/ppt6000/ppt6000MemberImgUpload.do";
        ts = String.valueOf(new Date().getTime());
        System.out.println("上传图片：" + memid + "  ts:" + ts + "  token:" + token);
        String Sign = url + memid + token + ts;
        System.out.println("upPhotoSign:" + Sign);
        upPhotoSign = MD5Utils.md5(Sign);
        System.out.println("加密upPhotoSign:" + upPhotoSign);
    }
    private void changeTextView() {
        // back_text,perfect_device_information,custom_name_text,selling_name_text,selling_location_text,selling_user_name_text,selling_phone_text,location_parameters_text,address_text
        if (Locale.getDefault().getLanguage().equals("en")) {
            back_text.setText("Back");
            perfect_device_information.setText("Perfect Device Information");
            custom_name_text.setText("Custom Name");
            selling_name_text.setText("Selling Name");
            selling_location_text.setText("Selling Location");
            selling_user_name_text.setText("Selling User Name");
            selling_phone_text.setText("Selling Phone");
            location_parameters_text.setText("Location");
            address_text.setText("Address");
            leapfrog.setText("Skip");
            information_text.setText("Remarks");
            photo_text.setText("Photo");
            remarks_text.setText("Location");
            comfir_text.setText("Comfir");
            temperature_text.setText("Temperature");

            //selling_edit_text,selling_location_edit_text,selling_user_edit_text,selling_phone_edit_text;
            selling_edit_text.setHint("Enter");
            selling_location_edit_text.setHint("Enter");
            selling_user_edit_text.setHint("Enter");
            selling_phone_edit_text.setHint("Enter");
            address_edit_text.setHint("Enter");
        }
    }

    private void getBundleString() {
        Bundle getStringValue = this.getIntent().getExtras();
        if (getStringValue.getString("device_mac") != null) {
            if(getStringValue.getString("activity").equals("start1")){

                delect.setVisibility(View.VISIBLE);
                perfect.setVisibility(View.GONE);
            }else{
                leapfrog.setVisibility(View.GONE);
                delect.setVisibility(View.GONE);
                perfect.setVisibility(View.VISIBLE);
            }
            device_mac = getStringValue.getString("device_mac");
            device_id = getStringValue.getString("device_id");
            act=getStringValue.getString("act");
        }

    }

    private void getTimeNow() {
        //获取当前年月日时分
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        nowYear = t.year;
        nowMonth = t.month;
        nowDay = t.monthDay;
        // date_text.setText(nowYear+"-"+(nowMonth+start1)+"-"+nowDay);
        System.out.println("日期：" + nowYear + "-" + nowMonth + "-" + nowDay);
    }

    @RequiresApi(api = VERSION_CODES.M)
    private void getLocation() {
        //获取地址
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        } else {
            LocationUtil.initLocation(PerfectDeviceActivity.this);
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

    private void init() {
        findViewById(R.id.confrim_btn).setOnClickListener(this);
        leapfrog = findViewById(R.id.leapfrog);
        findViewById(R.id.back).setOnClickListener(this);

        findViewById(R.id.new_information_layout).setOnClickListener(this);//信息
        findViewById(R.id.new_remarks_layout).setOnClickListener(this);//备注
        findViewById(R.id.new_photo_layout).setOnClickListener(this);//图片
        findViewById(R.id.icon_equiplist_boxdelete).setOnClickListener(this);//清空自定义名称

        remarks_text = findViewById(R.id.remarks_text);
        photo_text = findViewById(R.id.photo_text);
        information_text = findViewById(R.id.information_text);

        repair_user = findViewById(R.id.repair_user);

        new_information_img = findViewById(R.id.new_information_img);
        new_photo_img = findViewById(R.id.new_photo_img);
        new_remarks_img = findViewById(R.id.new_remarks_img);
        // back_text,perfect_device_information,custom_name_text,selling_name_text,selling_location_text,selling_user_name_text,selling_phone_text,location_parameters_text,address_text
        back_text = findViewById(R.id.back_text);
        perfect_device_information = findViewById(R.id.perfect_device_information);
        custom_name_text = findViewById(R.id.custom_name_text);
        selling_name_text = findViewById(R.id.selling_name_text);
        selling_location_text = findViewById(R.id.selling_location_text);
        selling_user_name_text = findViewById(R.id.selling_user_name_text);
        selling_phone_text = findViewById(R.id.selling_phone_text);
        location_parameters_text = findViewById(R.id.location_parameters_text);
        address_text = findViewById(R.id.address_text);
        comfir_text = findViewById(R.id.comfir_text);

        //selling_edit_text,selling_location_edit_text,selling_user_edit_text,selling_phone_edit_text;
        selling_edit_text = findViewById(R.id.selling_edit_text);
        selling_location_edit_text = findViewById(R.id.selling_location_edit_text);
        selling_user_edit_text = findViewById(R.id.selling_user_edit_text);
        selling_phone_edit_text = findViewById(R.id.selling_phone_edit_text);
        temperature_text=findViewById(R.id.temperature_text);

        address_edit_text = findViewById(R.id.address_edit_text);
        temperature = findViewById(R.id.temperature);
        addimg=findViewById(R.id.addimg);
        addimg.setOnClickListener(this);
        addimg.setOnLongClickListener((OnLongClickListener) this);

        perfect=findViewById(R.id.perfct_img);
        delect=findViewById(R.id.delect);
        delect.setOnClickListener(this);
        loading = findViewById(R.id.loading_layout);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.delect:
                if (Locale.getDefault().getLanguage().equals("en")) {
                    new AlertDialog.Builder(this).setTitle("Back？")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("confirm", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton("back", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 点击“返回”后的操作,这里不设置没有任何操作
                                    dialog.dismiss();
                                }
                            }).show();
                }else{
                    new AlertDialog.Builder(this).setTitle("确认返回？")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 点击“返回”后的操作,这里不设置没有任何操作
                                    dialog.dismiss();
                                }
                            }).show();
                }



                break;
            case R.id.addimg:

                int checkCallPhonePermission = ContextCompat.checkSelfPermission(PerfectDeviceActivity.this, Manifest.permission.CAMERA);
                if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(PerfectDeviceActivity.this,new String[]{Manifest.permission.CAMERA},222);

                }else{
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 11);
                }

                break;
            case R.id.confrim_btn://确定
                if (Locale.getDefault().getLanguage().equals("en")){
                    new AlertDialog.Builder(this).setTitle("Submit？")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 点击“确认”后的操作
                                    repair_user_str = repair_user.getText().toString().trim();//自定义名称
                                    selling_edit_str = selling_edit_text.getText().toString().trim();//售点名称
                                    selling_location_edit_str = selling_location_edit_text.getText().toString().trim();//售点地址
                                    selling_user_edit_str = selling_user_edit_text.getText().toString().trim();//售点联系人
                                    selling_phone_edit_str = selling_phone_edit_text.getText().toString().trim();//联系人电话
                                    address_edit_str = address_edit_text.getText().toString().trim();//详细地址


                                    // longitude_str,latitude_str,str_location,temperature_str
                                    if (selling_edit_str.equals("") || selling_location_edit_str.equals("") || selling_user_edit_str.equals("") || selling_phone_edit_str.equals("") || address_edit_str.equals("")) {
                                        Toast.makeText(PerfectDeviceActivity.this, "Please check whether the information is empty", Toast.LENGTH_SHORT).show();
                                    } else {
                                        sp = PerfectDeviceActivity.this.getSharedPreferences(tokeFile, MODE_PRIVATE);//实例化
                                        SharedPreferences.Editor editor = sp.edit(); //使处于可编辑状态
                                        editor.putString("perfect_addres", address_edit_str);
                                        editor.commit();    //提交数据保存
                                        UpPhotoCache();//获取签名,id,ts,token
                                        if(fileList!=null&&fileList.size()>0){
                                            loading.setStatus(LoadingLayout.Loading);//状态取消
                                            new Thread(upPhotoTask).start();
                                        }else{
                                            getCache();
                                            System.out.println("没图片");
                                        }

                                    }

                                }
                            })
                            .setNegativeButton("back", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 点击“返回”后的操作,这里不设置没有任何操作
                                    dialog.dismiss();
                                }
                            }).show();

                }else{
                    new AlertDialog.Builder(this).setTitle("确认提交？")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 点击“确认”后的操作
                                    repair_user_str = repair_user.getText().toString().trim();//自定义名称
                                    selling_edit_str = selling_edit_text.getText().toString().trim();//售点名称
                                    selling_location_edit_str = selling_location_edit_text.getText().toString().trim();//售点地址
                                    selling_user_edit_str = selling_user_edit_text.getText().toString().trim();//售点联系人
                                    selling_phone_edit_str = selling_phone_edit_text.getText().toString().trim();//联系人电话
                                    address_edit_str = address_edit_text.getText().toString().trim();//详细地址

                                    // longitude_str,latitude_str,str_location,temperature_str
                                    if (selling_edit_str.equals("") || selling_location_edit_str.equals("") || selling_user_edit_str.equals("") || selling_phone_edit_str.equals("") || address_edit_str.equals("")) {
                                        Toast.makeText(PerfectDeviceActivity.this, "请检查信息是否为空", Toast.LENGTH_SHORT).show();
                                    } else {
                                        sp = PerfectDeviceActivity.this.getSharedPreferences(tokeFile, MODE_PRIVATE);//实例化
                                        SharedPreferences.Editor editor = sp.edit(); //使处于可编辑状态
                                        editor.putString("perfect_addres", address_edit_str);
                                        editor.commit();    //提交数据保存
                                        UpPhotoCache();//获取签名,id,ts,token
                                        if(fileList!=null&&fileList.size()>0){
                                            loading.setStatus(LoadingLayout.Loading);//状态取消
                                            new Thread(upPhotoTask).start();
                                        }else{
                                            getCache();
                                            System.out.println("没图片");
                                        }

                                    }

                                }
                            })
                            .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 点击“返回”后的操作,这里不设置没有任何操作
                                    dialog.dismiss();
                                }
                            }).show();
                }





                break;
            case R.id.icon_equiplist_boxdelete:
                repair_user.setText("");
                break;
//            case R.id.leapfrog://跳过
//                // String location_detail_str=location_detail.getText().toString();
//                //String device_name_str=repair_user.getText().toString();
//                intent = new Intent(PerfectDeviceActivity.this, DeviceDetailActivity.class);
//                Bundle bundle1 = new Bundle();
//                bundle1.putString("device_mac", device_mac);
//                intent.putExtras(bundle1);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                break;
            case R.id.back:
                if (Locale.getDefault().getLanguage().equals("en")) {
                    new AlertDialog.Builder(this).setTitle("Back？")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("confirm", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton("back", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 点击“返回”后的操作,这里不设置没有任何操作
                                    dialog.dismiss();
                                }
                            }).show();
                }else{
                    new AlertDialog.Builder(this).setTitle("确认返回？")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 点击“返回”后的操作,这里不设置没有任何操作
                                    dialog.dismiss();
                                }
                            }).show();
                }

                break;
            case R.id.new_information_layout:
//                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInputFromInputMethod(view.getWindowToken(), 0); //强制显示键盘

                findViewById(R.id.new_repair_photo_lay).setVisibility(View.GONE);
                findViewById(R.id.new_repair_remarks_lay).setVisibility(View.GONE);
                findViewById(R.id.repair_information_lay).setVisibility(View.VISIBLE);


                remarks_text.setTextColor(ContextCompat.getColor(this, R.color.list_item_color));
                photo_text.setTextColor(ContextCompat.getColor(this, R.color.list_item_color));
                information_text.setTextColor(ContextCompat.getColor(this, R.color.theme_color));

                new_photo_img.setBackgroundResource(R.mipmap.icon_fix_img_normal);
                new_remarks_img.setBackgroundResource(R.mipmap.icon_equiplist_location_normal);
                new_information_img.setBackgroundResource(R.mipmap.icon_fix_info_active);

                break;
            case R.id.new_remarks_layout:
                findViewById(R.id.new_repair_photo_lay).setVisibility(View.GONE);
                findViewById(R.id.new_repair_remarks_lay).setVisibility(View.VISIBLE);
                findViewById(R.id.repair_information_lay).setVisibility(View.GONE);

                information_text.setTextColor(ContextCompat.getColor(this, R.color.list_item_color));
                photo_text.setTextColor(ContextCompat.getColor(this, R.color.list_item_color));
                remarks_text.setTextColor(ContextCompat.getColor(this, R.color.theme_color));

                new_photo_img.setBackgroundResource(R.mipmap.icon_fix_img_normal);
                new_remarks_img.setBackgroundResource(R.mipmap.icon_equiplist_location_active);
                new_information_img.setBackgroundResource(R.mipmap.icon_fix_info_normal);

                TextView location_text = findViewById(R.id.location_text);
                TextView location_str1 = findViewById(R.id.location_str);
                //获取地址
                if (str_location == null || str_location.isEmpty()) {
                    Toast.makeText(this, "请打开网络,重新进入", Toast.LENGTH_SHORT).show();
                } else {
                    location_text.setText(longitude_str + ",  " + latitude_str);
                    location_str1.setText(str_location);
                }

                break;
            case R.id.new_photo_layout://备注图片事件
                findViewById(R.id.new_repair_photo_lay).setVisibility(View.VISIBLE);
                findViewById(R.id.new_repair_remarks_lay).setVisibility(View.GONE);
                findViewById(R.id.repair_information_lay).setVisibility(View.GONE);

                remarks_text.setTextColor(ContextCompat.getColor(this, R.color.list_item_color));
                photo_text.setTextColor(ContextCompat.getColor(this, R.color.theme_color));
                information_text.setTextColor(ContextCompat.getColor(this, R.color.list_item_color));

                new_photo_img.setBackgroundResource(R.mipmap.icon_fix_img_active);
                new_remarks_img.setBackgroundResource(R.mipmap.icon_equiplist_location_normal);
                new_information_img.setBackgroundResource(R.mipmap.icon_fix_info_normal);
              //  labelFlowLayout(arrayList);
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
        findViewById(R.id.new_repair_photo_lay).setVisibility(View.VISIBLE);
        //判断那个相机回调
        switch (requestCode) {
            case 11:
                if (resultCode == Activity.RESULT_OK) {
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                        Log.i("TestFile",
                                "SD card is not avaiable/writeable right now.");
                        Toast.makeText(PerfectDeviceActivity.this, "sd卡不可用！！！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new DateFormat();
                    String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".png";
                    System.out.println("路径：" + name);
                    // Toast.makeText(this, name, Toast.LENGTH_LONG).show();
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                    fileList=new ArrayList<File>();
                    FileOutputStream b = null;
                    File file = new File("/sdcard/Image/");
                    file.mkdirs();// 创建文件夹
                    fileName = "/sdcard/Image/" + name;
                    addimg.setClickable(false);
                    addimg.setLongClickable(true);
                    addimg.setImageBitmap(bitmap);

                    //upPhotoPath.add(fileName);
                    File file1=new File(fileName);
                    fileList.add(file1);


                    try {
                        b = new FileOutputStream(fileName);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (b != null) {
                                b.flush();
                                b.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
//                    try
//                    {
//                        photo1.setImageBitmap(bitmap);// 将图片显示在ImageView里
//                    }catch(Exception e)
//                    {
//                        Log.e("error", e.getMessage());
//                    }
                }
                break;
        }

    }


    /**
     * 网络操作相关的子线程okhttp框架 上传多图片
     */
    Runnable upPhotoTask = new Runnable() {
        @Override
        public void run() {
            // TODO
                String url=utils.url+"/app/ppt6000/ppt6000MemberImgUpload.do?memId="+memid+"&ts="+ts+"&eqpId="+device_id;
                //String url="http://192.168.1.110:8080/app/ppt6000/ppt6000MemberImgUpload.do?memId="+memid+"&ts="+ts;
                System.out.println("路径"+upPhotoPath+" : "+upPhotoSign+" : "+url+" : "+device_id);

                ArrayList<String> name=new ArrayList<>();
                name.add("file14");
                uploadFile(fileList,url,upPhotoSign,name,device_id,PerfectDeviceActivity.this);
               // UploadUtil.uploadFile(fileList,url,upPhotoSign,name,device_id,PerfectDeviceActivity.this);


        }
    };
    private void showDetelePhotoDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.icon_fix_addimg);
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(PerfectDeviceActivity.this);
        normalDialog.setTitle("删除");
        normalDialog.setMessage("是否删除该图片？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //判断那个图片删除

                        addimg.setImageBitmap(icon);
                        addimg.setClickable(true);
                        addimg.setLongClickable(false);
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
        LinearLayout root = null;
        mCameraDialog = new Dialog(this, R.style.BottomDialog);
        //判断那个相机弹出
        switch (number) {
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
        LinearLayout root = null;//弹出布局
        mCameraDialog = new Dialog(this, R.style.BottomDialog);
        //判断需要哪个弹窗
        switch (number) {
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
                        temperature.setText(temperature_str + "℃");
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
        if(province_str!=null&&!"".equals(province_str)){
            System.out.println("市：" + province_str);
            String url = "https://api.seniverse.com/v3/weather/now.json?key=hifwkocphbol8biw&location=" + province_str + "&language=zh-Hans&unit=c";
            sendRequestWithHttpClient(this, url);//获取温度的方法
        }else{
            String url = "https://api.seniverse.com/v3/weather/now.json?key=hifwkocphbol8biw&location=广州市&language=zh-Hans&unit=c";
            sendRequestWithHttpClient(this, url);//获取温度的方法
        }


    }

    //图片布局方法
    private void labelFlowLayout(final ArrayList<Bitmap> arrayList) {
        if (found_activity_fyt == null) {
            //加载搜索记录
            for (int i = 0; i < arrayList.size(); i++) {
                final ImageView imageView = new ImageView(PerfectDeviceActivity.this);
                System.out.println("数组：" + arrayList.size());
                imageView.setImageBitmap(arrayList.get(i));
                imageView.setPadding(15, 10, 15, 10);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);//设置宽高,第一个参数是宽,第二个参数是高
                //设置边距
                params.topMargin = 5;
                params.bottomMargin = 5;
                params.leftMargin = 8;
                params.rightMargin = 8;
                imageView.setLayoutParams(params);
                found_activity_fyt = findViewById(R.id.found_activity_fyt);
                found_activity_fyt.addView(imageView);//将内容添加到布局中
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//添加点击事件
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 11);

                    }
                });
            }
        }
    }

    //获取缓存的memId,tokEn
    private void getCache() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        String url = utils.url+"/app/ppt6000/updateDate.do";
        ts = String.valueOf(new Date().getTime());
        System.out.println("首页：" + memid + "  ts:" + ts + "  token:" + token);
        String Sign = url + memid + token + ts;
        System.out.println("Sign:" + Sign);
        upSign = MD5Utils.md5(Sign);
        new Thread(networkTask).start();//更新设备信息
    }

    /**
     * 网络操作相关的子线程okhttp框架  获取设备详情
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // longitude_str,latitude_str,str_location,temperature_str
           /* repair_user_str=repair_user.getText().toString().trim();//自定义名称
                    selling_edit_str=selling_edit_text.getText().toString().trim();//售点名称
                    selling_location_edit_str=selling_location_edit_text.getText().toString().trim();//售点地址
                    selling_user_edit_str=selling_user_edit_text.getText().toString().trim();//售点联系人
                    selling_phone_edit_str=selling_phone_edit_text.getText().toString().trim();//联系人电话*/

            String pc[]=str_location.split("-");

            // 在这里进行 http request.网络请求相关操作
            String url = utils.url+"/app/ppt6000/updateDate.do?memId=" + memid + "&ts=" + ts;
            OkHttpClient okHttpClient = new OkHttpClient();
            System.out.println("验证：" + upSign);
            String b = "{\"macNo\": \"" + device_mac + "\",\"eqpId\": \"" + device_id + "\",\"memFullName\": \"" + repair_user_str + "\"," +
                    "\"Temperature\": \"" + temperature_str + "\",\"userInfoJson\": {\"username\": \"" + selling_user_edit_str + "\"," +
                    "\"pointOfSalePhone\": \"" + selling_phone_edit_str + "\",\"pointOfSaleName\": \"" + selling_edit_str + "\"," +
                    "\"addressInfoJson\": {\"dt\": \""+pc[2]+"\",\"lo\": \"\",\"da\": \"dsagvx\",\"sq\": \"\",\"pc\": \""+pc[0]+"\"," +
                    "\"la\": \"\",\"fa\": \"" + selling_location_edit_str + "\",\"ar\": \"\",\"pv\": \"山东\",\"te\": \"" + temperature_str + "\",\"ct\": \""+pc[1]+"\"}}}";//json字符串
            System.out.println("完善设备json:" + b);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), b);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("sIgn", upSign)
                    .build();
            System.out.println(request.headers());
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                String result = response.body().string();

                if (response != null) {
                    //在子线程中将Message对象发出去
                    if( response.code()==200){
                        Message message = new Message();
                        message.what = SHOW_RESPONSE;
                        message.obj = result.toString();
                        upDateHandler.sendMessage(message);
                    }else{
                        new AlertDialog.Builder(PerfectDeviceActivity.this).setTitle("完善设备提示").setMessage("更新失败,请检查网络").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).show();
                    }

                }
                System.out.println("结果：" + result + "状态码：" + response.code());
                //Toast.makeText(EditPassWordActivity.this,result,Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private Handler upDateHandler = new Handler() {

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
                            Toast.makeText(PerfectDeviceActivity.this,"信息完善成功",Toast.LENGTH_SHORT).show();
                            loading.setStatus(LoadingLayout.Success);//状态取消
                            Intent intent = new Intent(PerfectDeviceActivity.this, DeviceDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("device_mac", device_mac);
                            bundle.putString("act", act);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            } else {
                                new AlertDialog.Builder(PerfectDeviceActivity.this).setTitle("完善设备提示").setMessage(object1.getString("res")).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
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

    /**
     * 网络操作相关的子线程okhttp框架  获取设备详情
     */
    Runnable getDeta = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String url = utils.url+"/app/ppt6000/dataPpt6000Is.do?memId=" + memid + "&ts=" + ts;
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder().readTimeout(30, TimeUnit.SECONDS).connectTimeout(30,TimeUnit.SECONDS).build();
            System.out.println("验证：" + getSign);
            String b = "{\"parentId\":\"" + memid + "\",\"macNo\":\"" + device_mac + "\"}";//json字符串
            System.out.println("设备详情参数：" + b+"  "+url );
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), b);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("sIgn", getSign)
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
                        getDeviceHandler.sendMessage(message);
                    } else {
                        System.out.println("结果：" + result);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    private Handler getDeviceHandler = new Handler() {

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
                                //loading.setStatus(LoadingLayout.Success);

                                if(DataObject.getString("userInfoJson")!=null&!DataObject.getString("userInfoJson").equals("null")){
                                    JSONObject FAObject = new JSONObject(DataObject.getString("userInfoJson"));//该设备数据
                                    System.out.println("data数据:  " + FAObject);
                                    //"userInfoJson":{"username":"都在","userId":null,
                                    // "addressInfoJson":{"na":null,"ar":"","pc":"广东省","pv":"山东","ct":"广州市","dt":"番禺区","da":"dsagvx","fa":"笨猪","lo":"","la":"","te":"18"},
                                    // "pointOfSalePhone":"53666","pointOfSaleName":"哦哦YY"}
                                    //selling_edit_text, selling_location_edit_text, selling_user_edit_text, selling_phone_edit_text,;
                                    JSONObject faObject= new JSONObject(FAObject.getString("addressInfoJson"));

                                    selling_phone_edit_text.setText(FAObject.getString("pointOfSalePhone"));//售点手机号
                                    selling_location_edit_text.setText(faObject.getString("fa"));//售点地址
                                    repair_user.setHint(DataObject.getString("eqpName"));//设备名称
                                    selling_edit_text.setText(FAObject.getString("pointOfSaleName"));
                                    selling_user_edit_text.setText(FAObject.getString("username"));//售点联系人
                                }

                                //System.out.println(DataObject.getString("eqpName"));
                                device_id=DataObject.getString("eqpId");
                                loading.setStatus(LoadingLayout.Success);
                            } else {
                                new AlertDialog.Builder(PerfectDeviceActivity.this).setTitle("设备提示").setMessage("无此设备,请重新选择").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).show();
                            }
                        } else {
                            new AlertDialog.Builder(PerfectDeviceActivity.this).setTitle("设备提示").setMessage("无此设备,请重新选择").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
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

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()){
            case R.id.addimg:
                showDetelePhotoDialog();
                break;
        }
        return true;
    }


    /**
     * android上传文件到服务器
     * @param file  需要上传的文件
     * @param RequestURL  请求的rul
     * @param sign  签名
     * @param name  文件名
     * @return  返回响应的内容
     */
    public  String uploadFile(List<File> file, String RequestURL, String sign, ArrayList<String> name, String id, Context context){
        String result = null;
        String  BOUNDARY =  UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--" , LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";   //内容类型

        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(10000);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", "utf-8");  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("sign", sign);//签名
            // conn.setRequestProperty("eqpId", id);//id
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            conn.connect();

            if(file!=null){
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                for(int i=0;i<file.size();i++){
                    System.out.println("图片上传："+file.get(i));

                    StringBuffer sb = new StringBuffer();
                    sb.append(PREFIX);
                    sb.append(BOUNDARY);
                    sb.append(LINE_END);
                    /**
                     * 这里重点注意：
                     * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
                     * filename是文件的名字，包含后缀名的   比如:abc.png
                     */

                    System.out.println("文件名："+file.get(i).getName()+" : "+name);
                    sb.append("Content-Disposition: form-data; name=\""+name.get(i)+"\"; filename=\""+file.get(i).getName()+"\""+"; eqpId=\""+id+"\""+LINE_END);
                    sb.append("Content-Type:image/pjpeg; charset=utf-8"+LINE_END);
                    sb.append(LINE_END);
                    dos.write(sb.toString().getBytes());
                    InputStream is = new FileInputStream(file.get(i));
                    byte[] bytes = new byte[1024];
                    int len = 0;
                    while((len=is.read(bytes))!=-1){
                        dos.write(bytes, 0, len);
                    }

                    is.close();
                    dos.write(LINE_END.getBytes());
                    /**
                     * 获取响应码  200=成功
                     * 当响应成功，获取响应的流
                     */
                    //conn.getResponseMessage();
                }

                byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                int res = conn.getResponseCode();
                if(res==200){
                    InputStream input =  conn.getInputStream();
                    StringBuffer sb1= new StringBuffer();
                    int ss ;
                    while((ss=input.read())!=-1){
                        sb1.append((char)ss);
                    }
                    result = sb1.toString();
                    System.out.println("上传图片"+result);
                    Message message = new Message();
                    message.obj = result;
                    message.what = 0;
                    upPhotoHandler1.sendMessage(message);
                }else{
                    Message message = new Message();
                    message.what = 1;
                    upPhotoHandler1.sendMessage(message);
                    System.out.println("res"+conn.getResponseMessage()+res);
                }
            }
        } catch (MalformedURLException e) {
            System.out.println("上传失败");
            e.printStackTrace();
        } catch (IOException e) {
            //sendMessage(UPLOAD_SERVER_ERROR_CODE,"上传失败：error=" + e.getMessage());
            System.out.println("上传失败");
            e.printStackTrace();
        }
        return result;
    }
    private Handler upPhotoHandler1 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    String response = (String) msg.obj;
                    JSONObject object = null;
                    try {
                        object = new JSONObject(response);
                        JSONObject object1 = new JSONObject(object.getString("meta"));
                        if (object1.getString("res").equals("00000")) {
                            System.out.println(object1.getString("msg"));
                            loading.setStatus(LoadingLayout.Success);//状态取消
                            //Toast.makeText(PerfectDeviceActivity.this, "图片上传成功", Toast.LENGTH_SHORT).show();
                            getCache();
                        } else {
                            System.out.println(object1.getString("msg"));
                            loading.setStatus(LoadingLayout.Success);//状态取消
                            Toast.makeText(PerfectDeviceActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
                            fileList = null;
                            Bitmap icon = BitmapFactory.decodeResource(PerfectDeviceActivity.this.getResources(), R.mipmap.icon_fix_addimg);
                            addimg.setImageBitmap(icon);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    loading.setStatus(LoadingLayout.Success);//状态取消
                    fileList = null;
                    Bitmap icon = BitmapFactory.decodeResource(PerfectDeviceActivity.this.getResources(), R.mipmap.icon_fix_addimg);
                    addimg.setImageBitmap(icon);
                    Toast.makeText(PerfectDeviceActivity.this, "图片上传失败,请重新上传", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    };
}
