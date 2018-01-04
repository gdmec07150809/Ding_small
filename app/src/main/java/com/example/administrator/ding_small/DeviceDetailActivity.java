package com.example.administrator.ding_small;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.HelpTool.CustomDialog;
import com.example.administrator.ding_small.HelpTool.LocationUtil;

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

import static com.example.administrator.ding_small.HelpTool.LocationUtil.getAddress;
import static com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity.SHOW_RESPONSE;

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
    private boolean isEdit=false;
    private static  final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION  = 100;

    private LinearLayout call_record_layout,management_layout,manufacturer_layout,parameter_layout,selling_point_layout,record_layout,base_layout;
    private ImageView management_img,selling_point_img,parameter_img,manufacturer_img,record_img,call_record_img,repair_img,base_img;
    private EditText selling_point_name,selling_point_number,selling_point_location,selling_point_phone,selling_point_user,parameter_location;
    private TextView latitude,longitude,temperature,location_detail,base_text,management_text,selling_text,parameter_text,manufacturer_text;

    private String str_location=null;
    private  String latitude_str,longitude_str,province_str,temperature_str;
    @RequiresApi(api = VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_device_detail);
        init();//初始化控件，绑定监听
        getBundleString();//获取bundle数据
        getLocation();//获取当前地址
        getTemperature();//获取当前温度
    }
    private  void getBundleString(){
        Bundle getStringValue=this.getIntent().getExtras();
        if(getStringValue.getString("device_name")!=null){
            device_name.setText( getStringValue.getString("device_name"));
        }
    }

    private void init(){
        device_name=findViewById(R.id.device_name);
        //findViewById(R.id.call_record_up).setOnClickListener(this);//调拨记录
        findViewById(R.id.management_layout).setOnClickListener(this);//管理信息
        findViewById(R.id.new_manufacturer_layout).setOnClickListener(this);//制造商
        findViewById(R.id.new_parameter_layout).setOnClickListener(this);//设备参数
        findViewById(R.id.new_selling_layout).setOnClickListener(this);//售点信息
        findViewById(R.id.base_layout).setOnClickListener(this);//基本信息
        findViewById(R.id.repair_btn).setOnClickListener(this);//一键报修
        //findViewById(R.id.record_up).setOnClickListener(this);//维修记录
        findViewById(R.id.back).setOnClickListener(this);//返回;
        //findViewById(R.id.edit).setOnClickListener(this);//编辑
        //findViewById(R.id.device_del).setOnClickListener(this);//删除

       // call_record_layout=findViewById(R.id.call_record_layout);

        base_layout=findViewById(R.id.base_down_layout);
        management_layout=findViewById(R.id.management_down_layout);
        manufacturer_layout=findViewById(R.id.manufacturer_down_layout);
        parameter_layout=findViewById(R.id.parameter_down_layout);
        selling_point_layout=findViewById(R.id.selling_down_layout);
        //record_layout=findViewById(R.id.record_layout);
        /* management_img,selling_point_img,parameter_img,manufacturer_img,record_img,call_record_img;  箭头图片的初始化*/

        management_img=findViewById(R.id.management_img);
        selling_point_img=findViewById(R.id.selling_img);
        parameter_img=findViewById(R.id.parameter_img);
        manufacturer_img=findViewById(R.id.manufacturer_img);
        base_img=findViewById(R.id.base_img);
//        record_img=findViewById(R.id.record_img);
//        call_record_img=findViewById(R.id.call_record_img);
     //   findViewById(R.id.repair_img).setOnClickListener(this);

        //base_text,management_text,selling_text,parameter_text,manufacturer_text;信息标题
        base_text=findViewById(R.id.base_text);
        management_text=findViewById(R.id.management_text);
        selling_text=findViewById(R.id.selling_text);
        parameter_text=findViewById(R.id.parameter_text);
        manufacturer_text=findViewById(R.id.manufacturer_text);
    }
    //按钮点击事件
    @Override
    public void onClick(View view) {
        final Intent intent;
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
            case R.id.management_layout://管理信息
                management_layout.setVisibility(View.VISIBLE);
                base_layout.setVisibility(View.GONE);
                manufacturer_layout.setVisibility(View.GONE);
                parameter_layout.setVisibility(View.GONE);
                selling_point_layout.setVisibility(View.GONE);

                management_img.setImageResource(R.mipmap.icon_info_up);
                selling_point_img.setImageResource(R.mipmap.icon_info_down);
                parameter_img.setImageResource(R.mipmap.icon_info_down);
                manufacturer_img.setImageResource(R.mipmap.icon_info_down);
                base_img.setImageResource(R.mipmap.icon_info_down);

                //base_text,management_text,selling_text,parameter_text,manufacturer_text
                base_text.setTextColor(ContextCompat.getColor(this,R.color.title_color));
                management_text.setTextColor(ContextCompat.getColor(this,R.color.theme_color));
                selling_text.setTextColor(ContextCompat.getColor(this,R.color.title_color));
                parameter_text.setTextColor(ContextCompat.getColor(this,R.color.title_color));
                manufacturer_text.setTextColor(ContextCompat.getColor(this,R.color.title_color));
                break;
            case R.id.new_manufacturer_layout://制造商
                management_layout.setVisibility(View.GONE);
                base_layout.setVisibility(View.GONE);
                manufacturer_layout.setVisibility(View.VISIBLE);
                parameter_layout.setVisibility(View.GONE);
                selling_point_layout.setVisibility(View.GONE);

                management_img.setImageResource(R.mipmap.icon_info_down);
                selling_point_img.setImageResource(R.mipmap.icon_info_down);
                parameter_img.setImageResource(R.mipmap.icon_info_down);
                manufacturer_img.setImageResource(R.mipmap.icon_info_up);
                base_img.setImageResource(R.mipmap.icon_info_down);

                base_text.setTextColor(ContextCompat.getColor(this,R.color.title_color));
                management_text.setTextColor(ContextCompat.getColor(this,R.color.title_color));
                selling_text.setTextColor(ContextCompat.getColor(this,R.color.title_color));
                parameter_text.setTextColor(ContextCompat.getColor(this,R.color.title_color));
                manufacturer_text.setTextColor(ContextCompat.getColor(this,R.color.theme_color));
                break;
            case R.id.new_parameter_layout://参数信息
                management_layout.setVisibility(View.GONE);
                base_layout.setVisibility(View.GONE);
                manufacturer_layout.setVisibility(View.GONE);
                parameter_layout.setVisibility(View.VISIBLE);
                selling_point_layout.setVisibility(View.GONE);

                management_img.setImageResource(R.mipmap.icon_info_down);
                selling_point_img.setImageResource(R.mipmap.icon_info_down);
                parameter_img.setImageResource(R.mipmap.icon_info_up);
                manufacturer_img.setImageResource(R.mipmap.icon_info_down);
                base_img.setImageResource(R.mipmap.icon_info_down);

                base_text.setTextColor(ContextCompat.getColor(this,R.color.title_color));
                management_text.setTextColor(ContextCompat.getColor(this,R.color.title_color));
                selling_text.setTextColor(ContextCompat.getColor(this,R.color.title_color));
                parameter_text.setTextColor(ContextCompat.getColor(this,R.color.theme_color));
                manufacturer_text.setTextColor(ContextCompat.getColor(this,R.color.title_color));
                break;
            case R.id.new_selling_layout://售点信息
                management_layout.setVisibility(View.GONE);
                base_layout.setVisibility(View.GONE);
                manufacturer_layout.setVisibility(View.GONE);
                parameter_layout.setVisibility(View.GONE);
                selling_point_layout.setVisibility(View.VISIBLE);

                management_img.setImageResource(R.mipmap.icon_info_down);
                selling_point_img.setImageResource(R.mipmap.icon_info_up);
                parameter_img.setImageResource(R.mipmap.icon_info_down);
                manufacturer_img.setImageResource(R.mipmap.icon_info_down);
                base_img.setImageResource(R.mipmap.icon_info_down);

                base_text.setTextColor(ContextCompat.getColor(this,R.color.title_color));
                management_text.setTextColor(ContextCompat.getColor(this,R.color.title_color));
                selling_text.setTextColor(ContextCompat.getColor(this,R.color.theme_color));
                parameter_text.setTextColor(ContextCompat.getColor(this,R.color.title_color));
                manufacturer_text.setTextColor(ContextCompat.getColor(this,R.color.title_color));
                break;
            case R.id.base_layout://基本信息
                management_layout.setVisibility(View.GONE);
                base_layout.setVisibility(View.VISIBLE);
                manufacturer_layout.setVisibility(View.GONE);
                parameter_layout.setVisibility(View.GONE);
                selling_point_layout.setVisibility(View.GONE);

                management_img.setImageResource(R.mipmap.icon_info_down);
                selling_point_img.setImageResource(R.mipmap.icon_info_down);
                parameter_img.setImageResource(R.mipmap.icon_info_down);
                manufacturer_img.setImageResource(R.mipmap.icon_info_down);
                base_img.setImageResource(R.mipmap.icon_info_up);

                base_text.setTextColor(ContextCompat.getColor(this,R.color.theme_color));
                management_text.setTextColor(ContextCompat.getColor(this,R.color.title_color));
                selling_text.setTextColor(ContextCompat.getColor(this,R.color.title_color));
                parameter_text.setTextColor(ContextCompat.getColor(this,R.color.title_color));
                manufacturer_text.setTextColor(ContextCompat.getColor(this,R.color.title_color));
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
            case R.id.repair_btn://报修按钮
                intent=new Intent(DeviceDetailActivity.this,CreatRepairActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.edit://编辑
                isEdit=!isEdit;
                //selling_point_name,selling_point_number,selling_point_location,selling_point_phone,selling_point_user
                selling_point_name=findViewById(R.id.selling_point_name);//售点名称
                selling_point_number=findViewById(R.id.selling_point_number);//售点编号
                selling_point_location=findViewById(R.id.selling_point_location);//售点地址
                selling_point_phone=findViewById(R.id.selling_point_phone);//售点人号码
                selling_point_user=findViewById(R.id.selling_point_user);//售点人姓名
                parameter_location=findViewById(R.id.parameter_detail_location);//参数详细地址

                //latitude,longitude,temperature,location_detail
                longitude=findViewById(R.id.longitude);
                latitude=findViewById(R.id.latitude);
                temperature=findViewById(R.id.temperature);
                location_detail=findViewById(R.id.parameter_location);


                if(isEdit){//判断可否编辑
                    ImageView img=findViewById(R.id.edit);
                    img.setImageResource(R.drawable.yes);

                    selling_point_name.setEnabled(true);
                    selling_point_number.setEnabled(true);
                    selling_point_location.setEnabled(true);
                    selling_point_phone.setEnabled(true);
                    selling_point_user.setEnabled(true);
                    parameter_location.setEnabled(true);
                }else{
                    ImageView img=findViewById(R.id.edit);
                    img.setImageResource(R.drawable.edit_img);

                    selling_point_name.setEnabled(false);
                    selling_point_number.setEnabled(false);
                    selling_point_location.setEnabled(false);
                    selling_point_phone.setEnabled(false);
                    selling_point_user.setEnabled(false);
                    parameter_location.setEnabled(false);
                }

                //获取地址
                if(str_location ==null || str_location.isEmpty()){
                    Toast.makeText(this,"请打开网络,重新进入",Toast.LENGTH_SHORT).show();
                }else{
                    if(longitude_str.length()>14||latitude_str.length()>14){
                        longitude_str=longitude_str.substring(0,15);
                        latitude_str=latitude_str.substring(0,15);
                    }
                    longitude.setText(longitude_str);
                    latitude.setText( latitude_str);
                    location_detail.setText(str_location);
                }
                temperature.setText(temperature_str+"℃");
                break;
            case R.id.device_del:
                CustomDialog.Builder builder = new CustomDialog.Builder(this);
                builder.setMessage("是否删除该设备");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent1=new Intent(DeviceDetailActivity.this,DeviceListActivity.class);
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
    private void getLocation(){
        //获取地址
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        } else {
            LocationUtil.initLocation(DeviceDetailActivity.this);
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

    public  void   sendRequestWithHttpClient(final Context context, final String url) {
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
                    System.out.println("状态码："+httpResponse.getStatusLine().getStatusCode());
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity = httpResponse.getEntity();
                        String result = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串


                        if(result!=null){
                            //在子线程中将Message对象发出去
                            Message message = new Message();
                            message.what = SHOW_RESPONSE;
                            message.obj = result.toString();
                            System.out.println("返回结果："+result);
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
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    try {
                        JSONObject responseObject=new JSONObject(response);
                        System.out.println("返回："+responseObject);
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
    private void getTemperature(){
        try {
            String str=LocationUtil.getAddress(LocationUtil.location,getApplicationContext());

        } catch (IOException e) {
            e.printStackTrace();
        }
        province_str=LocationUtil.province;
        System.out.println("市："+province_str);
        String url="https://api.seniverse.com/v3/weather/now.json?key=hifwkocphbol8biw&location="+province_str+"&language=zh-Hans&unit=c";
        sendRequestWithHttpClient(this,url);//获取温度的方法

    }
}
