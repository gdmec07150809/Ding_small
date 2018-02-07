package com.example.administrator.ding_small;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.HelpTool.FlowLayout;
import com.example.administrator.ding_small.HelpTool.LocationUtil;
import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.example.administrator.ding_small.HelpTool.UploadUtil;
import com.example.administrator.ding_small.Label.EditLabelActivity;
import com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity;
import com.example.administrator.ding_small.PersonalCenter.PersonalCenterPerfectActivity;
import com.example.administrator.ding_small.Utils.utils;
import com.weavey.loading.lib.LoadingLayout;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.feezu.liuli.timeselector.TimeSelector;
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
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static android.R.attr.path;
import static com.example.administrator.ding_small.HelpTool.LocationUtil.getAddress;
import static com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity.SHOW_RESPONSE;

/**
 * Created by CZK on 2017/12/20.
 */

public class CreatRepairRemarksActivity extends Activity implements View.OnClickListener {
    private TextView dateT, location_text, photo_text, reimbursement_text, parameter_text, management_text, at_action, latitude, longitude, location, temperature, information_text, remarks_text, adress_text;
    private ImageView photo1, photo2, photo3, photo4, new_information_img, new_photo_img, new_remarks_img;
    private String atTime, title;
    InputMethodManager imm;
    private Dialog mCameraDialog;
    private int color;
    private RelativeLayout title_layout;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 100;
    private String str_location = null;
    private String latitude_str, longitude_str, province_str, temperature_str,location_str;
    private FlowLayout found_activity_fyt;
    private ArrayList<Bitmap> arrayList = new ArrayList<Bitmap>();
    private ArrayList<String> photoPath=new ArrayList<String>();

    private EditText repair_user,repair_phone,remark_text;

    public static final int SHOW_RESPONSE = 0;
    //private static final String repairFile = "repairFile";//定义保存的文件的名称

    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    String memid,token,ts,upPhotoSign,path;
    String user_str,phone_str,remark_str;
    private LoadingLayout loading;

    private ArrayList<String> photoList=null;

    private TextView repair_information_text,repair_user_text,repair_phone_text,remarks_information_text,update_photo_text,confirm_text;
    @RequiresApi(api = VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_repair_layout);
        init();//初始化控件
        changeTextView();//更换语言
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        //getStringValue();//获取前页传来的数据
        //getLocation();//获取当前经纬度
        //getTemperature();//获取当前温度
        getString();//获取页面传递数据

        getCache();
        Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.icon_fix_addimg);
        arrayList.add(icon);

    }

    private void changeTextView() {

        if (Locale.getDefault().getLanguage().equals("en")) {
            repair_information_text.setText("Repair Information (Required)");
            repair_user_text.setText("Repair User");
            repair_phone_text.setText("Repair Phone");
            remarks_text.setText("Remarks");
            photo_text.setText("Photo");
            information_text.setText("Information");
            location_text.setText("Repair Location");
            remark_text.setHint("Fill in the note information");

            //remarks_information_text,update_photo_text
            remarks_information_text.setText("Note information");
            update_photo_text.setText("Upload a picture (Optional)");
            //  //repair_user,repair_phone,
            repair_user.setHint("Enter");
            repair_phone.setHint("Enter");
            adress_text.setHint("Unselected");
            confirm_text.setText("Comfirm");

        }
    }
    private void  getCache(){
        /*    editor.putString("opName", user_str);
                    editor.putString("memPhone", phone_str);
                    editor.putString("repireDescription", remark_str);*/
        //repair_user,repair_phone,remark_text
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        String url = utils.url+"/app/ppt7000/memberImgUpload.do";
        //String url = "http://192.168.1.103:8080/api/user/logout.do";
        ts = String.valueOf(new Date().getTime());
        System.out.println("上传图片：" + memid + "  ts:" + ts + "  token:" + token);
        String Sign = url + memid + token + ts;
        System.out.println("upPhotoSign:" + Sign);
        upPhotoSign = MD5Utils.md5(Sign);


        repair_user.setText(sp.getString("opName", ""));
        repair_phone.setText(sp.getString("memPhone", ""));
        remark_text.setText(sp.getString("repireDescription", ""));

        /*
        *  remarks_text = findViewById(R.id.remarks_text);
        photo_text = findViewById(R.id.photo_text);
        information_text = findViewById(R.id.information_text);*/
        //adress_text.setText(sp.getString("cacheAdress",""));

    }
    private void getString() {
        if (getIntent().getStringExtra("adress") != null) {
            adress_text.setText(getIntent().getStringExtra("adress"));
            String locationValue=getIntent().getStringExtra("adress")+getIntent().getStringExtra("location");
            location_str=locationValue;
        }
        Bundle bundle=this.getIntent().getExtras();
        photoList= (ArrayList<String>) bundle.getSerializable("pathList");
        if(photoList!=null&&photoList.size()>0){
            for(int i=0;i<photoList.size();i++){
                photoPath.add(photoList.get(i));
                arrayList.add(BitmapFactory.decodeFile(photoList.get(i)));
            }
        }
        ;
    }

    private void getTemperature() {
        try {
            String str = getAddress(LocationUtil.location, getApplicationContext());

        } catch (IOException e) {
            e.printStackTrace();
        }
        province_str = LocationUtil.province;
        System.out.println("市：" + province_str);
        String url = "https://api.seniverse.com/v3/weather/now.json?key=hifwkocphbol8biw&location=" + province_str + "&language=zh-Hans&unit=c";
        sendRequestWithHttpClient(this, url);//获取温度的方法
    }

    @RequiresApi(api = VERSION_CODES.M)
    private void getLocation() {
        //获取地址
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        } else {
            LocationUtil.initLocation(CreatRepairRemarksActivity.this);
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

    private void getStringValue() {
        Bundle bundle = this.getIntent().getExtras();
        atTime = bundle.getString("date");
        title = bundle.getString("at_action");
        color = bundle.getInt("drawable");
        dateT.setText(atTime);
        title_layout.setBackgroundColor(color);
        at_action.setText(title);
    }

    private void init() {
//        dateT=findViewById(R.id.date);
//        dateT.setOnClickListener(this);
        findViewById(R.id.new_information_layout).setOnClickListener(this);//信息
        findViewById(R.id.new_remarks_layout).setOnClickListener(this);//备注
        findViewById(R.id.new_photo_layout).setOnClickListener(this);//图片
//        findViewById(R.id.reimbursement_layout).setOnClickListener(this);
//        findViewById(R.id.parameter_layout).setOnClickListener(this);
//        findViewById(R.id.management_layout).setOnClickListener(this);
        findViewById(R.id.confrim_btn).setOnClickListener(this);//确定
        findViewById(R.id.back).setOnClickListener(this);//返回
        findViewById(R.id.new_select_layout).setOnClickListener(this);//选择地址

        remarks_text = findViewById(R.id.remarks_text);
        photo_text = findViewById(R.id.photo_text);
        information_text = findViewById(R.id.information_text);

        //repair_user,repair_phone,remark_text
        repair_user=findViewById(R.id.repair_user);
        repair_phone=findViewById(R.id.repair_phone);
        remark_text=findViewById(R.id.remark_text);

        // information_img,photo_img,location_img;
        new_information_img = findViewById(R.id.new_information_img);
        new_photo_img = findViewById(R.id.new_photo_img);
        new_remarks_img = findViewById(R.id.new_remarks_img);

        adress_text = findViewById(R.id.adress_text);


        loading = findViewById(R.id.loading_layout);

        //repair_information_text,repair_user_text,repair_phone_text
        repair_information_text=findViewById(R.id.repair_information_text);
        repair_user_text=findViewById(R.id.repair_user_text);
        repair_phone_text=findViewById(R.id.repair_phone_text);
        location_text=findViewById(R.id.location_text);
        //remarks_information_text,update_photo_text
        remarks_information_text=findViewById(R.id.remarks_information_text);
        update_photo_text=findViewById(R.id.update_photo_text);
        confirm_text=findViewById(R.id.confirm_text);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.date://选择时间事件
                TimeSelector timeSelector = new TimeSelector(CreatRepairRemarksActivity.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        atTime = time;
                        dateT.setText(time);
                    }

                }, atTime, "2500-12-31 23:59:59");
                timeSelector.setIsLoop(false);//设置不循环,true循环
                timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.show();
                break;
            case R.id.new_information_layout:
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInputFromInputMethod(view.getWindowToken(), 0); //强制显示键盘

                findViewById(R.id.new_repair_photo_lay).setVisibility(View.GONE);
                findViewById(R.id.new_repair_remarks_lay).setVisibility(View.GONE);
                findViewById(R.id.repair_information_lay).setVisibility(View.VISIBLE);


                remarks_text.setTextColor(ContextCompat.getColor(this, R.color.list_item_color));
                photo_text.setTextColor(ContextCompat.getColor(this, R.color.list_item_color));
                information_text.setTextColor(ContextCompat.getColor(this, R.color.theme_color));

                new_photo_img.setBackgroundResource(R.mipmap.icon_fix_img_normal);
                new_remarks_img.setBackgroundResource(R.mipmap.icon_fix_remark_normal);
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
                new_remarks_img.setBackgroundResource(R.mipmap.icon_fix_remark_active);
                new_information_img.setBackgroundResource(R.mipmap.icon_fix_info_normal);

                break;
            case R.id.new_photo_layout://备注图片事件
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInputFromInputMethod(view.getWindowToken(), 0); //强制显示键盘
                findViewById(R.id.new_repair_photo_lay).setVisibility(View.VISIBLE);
                findViewById(R.id.new_repair_remarks_lay).setVisibility(View.GONE);
                findViewById(R.id.repair_information_lay).setVisibility(View.GONE);

                remarks_text.setTextColor(ContextCompat.getColor(this, R.color.list_item_color));
                photo_text.setTextColor(ContextCompat.getColor(this, R.color.theme_color));
                information_text.setTextColor(ContextCompat.getColor(this, R.color.list_item_color));

                new_photo_img.setBackgroundResource(R.mipmap.icon_fix_img_active);
                new_remarks_img.setBackgroundResource(R.mipmap.icon_fix_remark_normal);
                new_information_img.setBackgroundResource(R.mipmap.icon_fix_info_normal);

                labelFlowLayout(arrayList);
                break;

            case R.id.new_select_layout:
                sp = CreatRepairRemarksActivity.this.getSharedPreferences(tokeFile, MODE_PRIVATE);//实例化
                SharedPreferences.Editor editor = sp.edit(); //使处于可编辑状态

                user_str=repair_user.getText().toString().trim();
                phone_str=repair_phone.getText().toString().trim();
                editor.putString("opName", user_str);
                editor.putString("memPhone", phone_str);
                editor.commit();
                intent = new Intent(CreatRepairRemarksActivity.this, SelectLocationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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

                location_text.setTextColor(ContextCompat.getColor(this, R.color.green));
                photo_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                reimbursement_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                parameter_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                management_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
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

                location_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                photo_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                reimbursement_text.setTextColor(ContextCompat.getColor(this, R.color.green));
                parameter_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                management_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                break;
            case R.id.parameter_layout://备注参数事件
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

                longitude = findViewById(R.id.longitude);
                latitude = findViewById(R.id.latitude);
                location = findViewById(R.id.location);
                temperature = findViewById(R.id.temperature);
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

                location_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                photo_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                reimbursement_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                parameter_text.setTextColor(ContextCompat.getColor(this, R.color.green));
                management_text.setTextColor(ContextCompat.getColor(this, R.color.blank));

                //获取地址
                if (str_location == null || str_location.isEmpty()) {
                    Toast.makeText(this, "请打开网络,重新进入", Toast.LENGTH_SHORT).show();
                } else {
                    longitude.setText(longitude_str);
                    latitude.setText(latitude_str);
                    location.setText(str_location);
                }
                temperature.setText(temperature_str + "℃");
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

                location_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                photo_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                reimbursement_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                parameter_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                management_text.setTextColor(ContextCompat.getColor(this, R.color.green));
                break;
            case R.id.confrim_btn:
                intent = new Intent(CreatRepairRemarksActivity.this, CreatRepairActivity.class);
                //repair_user,repair_phone,remark_text
                 user_str=repair_user.getText().toString().trim();
                phone_str=repair_phone.getText().toString().trim();
                remark_str=remark_text.getText().toString().trim();
                if(user_str.equals("")||phone_str.equals("")||remark_str.equals("")){
                    Toast.makeText(this, "请检查信息是否为空", Toast.LENGTH_SHORT).show();
                }else{
//                    if(photoPath!=null&&photoPath.size()>0){
//                        loading.setStatus(LoadingLayout.Loading);
//                        new Thread(upPhotoTask).start();
//
//                    }else{
                        Bundle bundle = new Bundle();
                        String jsonString="{\"opName\":\""+user_str+"\",\"memPhone\":\""+phone_str+"\",\"repireDescription\":\""+remark_str+"\",\"otherInfoJson\": {\"dt\": \"\",\"lo\": \"\",\"da\": \"dsagvx\",\"sq\": \"\",\"pc\": \"\",\"la\": \"\",\"fa\": \"" + location_str + "\",\"ar\": \"\",\"pv\": \"\",\"te\": \"\",\"ct\": \"\"}}";
                        String otherInfoJson_str="{\"dt\": \"\",\"lo\": \"\",\"da\": \"dsagvx\",\"sq\": \"\",\"pc\": \"\",\"la\": \"\",\"fa\": \"\" + location_str + \"\",\"ar\": \"\",\"pv\": \"\",\"te\": \"\",\"ct\": \"\"}";
                        System.out.println("报修  "+jsonString);


                        sp = CreatRepairRemarksActivity.this.getSharedPreferences(tokeFile, MODE_PRIVATE);//实例化
                        SharedPreferences.Editor editor1 = sp.edit(); //使处于可编辑状态
                        editor1.putString("opName", user_str);
                        editor1.putString("memPhone", phone_str);
                        editor1.putString("repireDescription", remark_str);
                        editor1.commit();    //提交数据保存

                        bundle.putString("opName", user_str);
                        bundle.putString("memPhone", phone_str);
                        bundle.putString("repireDescription", remark_str);
                        bundle.putString("fa", location_str);
                       // bundle.putString("path", path);
                        bundle.putString("explain","repair");
                        bundle.putSerializable("pathList",photoPath);
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                //    }

                }

                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    //删除图片事件 //获取,处理拍照事件
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
                        Toast.makeText(CreatRepairRemarksActivity.this, "sd卡不可用！！！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new DateFormat();
                    String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".png";
                    System.out.println("路径：" + name);
                    // Toast.makeText(this, name, Toast.LENGTH_LONG).show();
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

                    arrayList.add(bitmap);
                    if (arrayList.size() == 5) {
                        arrayList.remove(0);
                    }
                    /*更新图片布局*/
                    found_activity_fyt.removeAllViews();
                    //加载搜索记录
                    for (int i = arrayList.size() - 1; i >= 0; i--) {
                        final ImageView imageView = new ImageView(CreatRepairRemarksActivity.this);
                        System.out.println("数组：" + arrayList.size());
                        imageView.setImageBitmap(arrayList.get(i));
                        imageView.setPadding(15, 10, 15, 10);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);//设置宽高,第一个参数是宽,第二个参数是高
                        //设置边距
                        params.topMargin = 30;
                        params.bottomMargin = 5;
                        params.leftMargin = 0;
                        params.rightMargin = 8;
                        imageView.setLayoutParams(params);
                        found_activity_fyt = findViewById(R.id.found_activity_fyt);
                        found_activity_fyt.addView(imageView);//将内容添加到布局中

                            System.out.println("图片数2："+ found_activity_fyt.getChildCount()+":"+i);
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {//添加点击事件
                                    System.out.println("图片数：" + found_activity_fyt.getChildCount());
                                    //判断图片少于9张时,去除（除最后一张）的点击事件;等于9张时,去除所有图片的点击事件
                                    if (found_activity_fyt.getChildCount() < 5) {
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent, 11);
                                    }
                                }
                            });
                             if(found_activity_fyt.getChildCount()<4){
                                 found_activity_fyt.getChildAt(0).setLongClickable(false);
                            }else {
                                 imageView.setOnLongClickListener(new View.OnLongClickListener() {
                                     @Override
                                     public boolean onLongClick(View view) {
                                        found_activity_fyt.removeView(view);
                                         return true;
                                     }
                                 });
                             }
                    }

                    FileOutputStream b = null;
                    File file = new File("/sdcard/Image/");
                    file.mkdirs();// 创建文件夹
                    String fileName = "/sdcard/Image/" + name;

                    photoPath.add(fileName);

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
                }
        }

    }

    /**
     * 网络操作相关的子线程okhttp框架 上传多图片
     */
    Runnable upPhotoTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            String url=utils.url+"/app/ppt7000/memberImgUpload.do?memId="+memid+"&ts="+ts;
            List<File> fileList=new ArrayList<>();
            for(int i=0;i<photoPath.size();i++){
              File file = new File(photoPath.get(i));
                fileList.add(file);
              System.out.println("路径"+i+"："+photoPath.get(i)+"  :  "+file+" : "+upPhotoSign+" : "+url);
            }
            ArrayList<String> name=new ArrayList<>();
            name.add("file10");
            name.add("file11");
            name.add("file12");
            name.add("file13");

           uploadCrepairFile(fileList,url,upPhotoSign,name,"",CreatRepairRemarksActivity.this);
        }
    };

    private void showDetelePhotoDialog(final int number) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(CreatRepairRemarksActivity.this);
        normalDialog.setTitle("删除");
        normalDialog.setMessage("是否删除该图片？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //判断那个图片删除
                        switch (number) {
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
                        Toast.makeText(context, "访问失败!!!请检查服务器...", Toast.LENGTH_LONG).show();
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

    //图片布局方法
    private void labelFlowLayout(final ArrayList<Bitmap> arrayList) {
        if (found_activity_fyt == null) {
            //加载搜索记录
            for (int i = 0; i < arrayList.size(); i++) {
                final ImageView imageView = new ImageView(CreatRepairRemarksActivity.this);
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
                      for(int i=0;i<found_activity_fyt.getChildCount();i++){
                          found_activity_fyt.getChildAt(i).setClickable(false);
                      }
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 11);

                    }
                });
            }
        }
    }

    /**
     * android上传文件到服务器
     * @param file  需要上传的文件
     * @param RequestURL  请求的rul
     * @param sign  签名
     * @param name  文件名
     * @return  返回响应的内容
     */
    public  String uploadCrepairFile(List<File> file, String RequestURL, String sign, ArrayList<String> name, String id, Context context){
        String result = null;
        String  BOUNDARY =  UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--" , LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";   //内容类型

        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(60000);
            conn.setConnectTimeout(60000);
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
            // sendMessage(UPLOAD_SERVER_ERROR_CODE,"上传失败：error=" + e.getMessage());
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
                    try {
                        JSONObject res=new JSONObject(response);
                        JSONObject meta=new JSONObject(res.getString("meta"));
                        System.out.println("图片路径："+meta.getString("msg"));
                        path=meta.getString("msg");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    loading.setStatus(LoadingLayout.Success);//状态取消
                    Toast.makeText(CreatRepairRemarksActivity.this,"图片上传成功",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CreatRepairRemarksActivity.this, CreatRepairActivity.class);
                    Bundle bundle = new Bundle();
                    String jsonString="{\"opName\":\""+user_str+"\",\"memPhone\":\""+phone_str+"\",\"repireDescription\":\""+remark_str+"\",\"otherInfoJson\": {\"dt\": \"\",\"lo\": \"\",\"da\": \"dsagvx\",\"sq\": \"\",\"pc\": \"\",\"la\": \"\",\"fa\": \"" + location_str + "\",\"ar\": \"\",\"pv\": \"\",\"te\": \"\",\"ct\": \"\"}}";
                    String otherInfoJson_str="{\"dt\": \"\",\"lo\": \"\",\"da\": \"dsagvx\",\"sq\": \"\",\"pc\": \"\",\"la\": \"\",\"fa\": \"\" + location_str + \"\",\"ar\": \"\",\"pv\": \"\",\"te\": \"\",\"ct\": \"\"}";
                    System.out.println("报修  "+jsonString);


                    sp = CreatRepairRemarksActivity.this.getSharedPreferences(tokeFile, MODE_PRIVATE);//实例化
                    SharedPreferences.Editor editor = sp.edit(); //使处于可编辑状态
                    String cacheAdress=adress_text.getText().toString().trim();
                    editor.putString("opName", user_str);
                    editor.putString("memPhone", phone_str);
                    editor.putString("repireDescription", remark_str);
                    editor.putString("adress", cacheAdress);
                    editor.commit();    //提交数据保存

                    bundle.putString("opName", user_str);
                    bundle.putString("memPhone", phone_str);
                    bundle.putString("repireDescription", remark_str);
                    bundle.putString("fa", location_str);
                    bundle.putString("path", path);
                    bundle.putString("explain","repair");
                    bundle.putSerializable("pathList",photoPath);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case 1:
                    loading.setStatus(LoadingLayout.Success);//状态取消
                    Toast.makeText(CreatRepairRemarksActivity.this,"图片上传失败",Toast.LENGTH_SHORT).show();
                    break;
                default:break;
            }
        }
    };
}
