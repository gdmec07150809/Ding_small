package com.example.administrator.ding_small;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.Adapter.MFragmentPagerAdapter;
import com.example.administrator.ding_small.Fragment.Fragment1;
import com.example.administrator.ding_small.Fragment.Fragment2;
import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity;
import com.example.administrator.ding_small.PersonalCenter.PersonalCenterPerfectActivity;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by CZK on 2017/12/20.
 */

public class CreatRepairActivity extends FragmentActivity implements View.OnClickListener {
    LinearLayout ll, two;
    Intent intent;
    RelativeLayout action;
    private TextView day, time, action_text;
    private String atTime, at_action;
    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;
    //图标
    int icno[] = {R.mipmap.fix_icon_noele_normal, R.mipmap.fix_icon_nocharge_normal, R.mipmap.fix_icon_nolight_normal,
            R.mipmap.fix_icon_nocold_normal, R.mipmap.fix_icon_paiqi_normal, R.mipmap.fix_icon_umbrella_normal,R.mipmap.fix_icon_paishui_normal,
            R.mipmap.fix_icon_loushui_normal, R.mipmap.fix_icon_loudian_normal, R.mipmap.fix_icon_louqi_normal,
            R.mipmap.fix_icon_noise_normal, R.mipmap.fix_icon_surface_normal, R.mipmap.fix_icon_print_normal, R.mipmap.fix_icon_others_normal};
    //图标下的文字
    String name[] = {"不通电", "不充电", "不发光", "不制冷", "不排气", "不防水", "不排气",
            "漏水", "漏电", "漏气", "噪音大", "外观破损","印刷错误", "其它"};

    //实现Tab滑动效果
    private ViewPager mViewPager;
    private  String jsonString;
    String date_str;

    //当前页卡编号
    private int currIndex = 0;

    //存放Fragment
    private ArrayList<Fragment> fragmentArrayList;

    //管理Fragment
    private FragmentManager fragmentManager;
    private TextView confirmation;
    private TextView repair_user;

    public Context context;
    private String opName,memPhone,repireDescription,fa;

    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    String memid, token, sign, oldPass, newPass, ts, c_newPass, device_mac,device_id;
    public static final int SHOW_RESPONSE = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creat_repair);
        InitFragment();//初始化流式布局
        InitViewPager();//初始化viewPager
        day = findViewById(R.id.day);
        day.setOnClickListener(this);
        action_text = findViewById(R.id.action_text);
        action = findViewById(R.id.action);
        findViewById(R.id.footer).setOnClickListener(this);
        findViewById(R.id.remarks_layout).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        confirmation=findViewById(R.id.confirmation);
        repair_user=findViewById(R.id.repair_user);
        getCache();//获取缓存
        getBundleString();
        //获取当前年月日时分
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month;
        int date = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        //给时分赋值
        if (minute < 10) {
            String minute_text = "0" + minute;
            atTime = year + "-" + (month + 1) + "-" + (date+5)+ "  " + hour + ":" + minute_text;
            day.setText(atTime);
        } else {
            atTime = year + "-" + (month + 1) + "-" + (date+5) + "  " + hour + ":" + minute;
            day.setText(atTime);
        }

    }

    private void getCache() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        String url = "http://192.168.1.113:8080/app/ppt7000/intsertPpt7000.do";//报修
        ts = String.valueOf(new Date().getTime());
        System.out.println("提交报修：" + memid + "  ts:" + ts + "  token:" + token);
        String Sign = url + memid + token + ts;
        System.out.println("Sign:" + Sign);
        sign = MD5Utils.md5(Sign);
        //new Thread(repairTask).start();//提交报修
    }
    private void getBundleString() {
        Bundle getStringValue = this.getIntent().getExtras();
        if (getStringValue.getString("explain") != null) {
          if(getStringValue.getString("explain").equals("repair")){
                /* opName,memPhone,repireDescription,fa;*/
              opName=getStringValue.getString("opName");
              memPhone=getStringValue.getString("memPhone");
              repireDescription=getStringValue.getString("repireDescription");
              fa=getStringValue.getString("fa");

              sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
              device_id = sp.getString("device_id", "");
              confirmation.setText("已填写");
              repair_user.setText(opName);
          }
        }
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.day:
                TimeSelector timeSelector1 = new TimeSelector(CreatRepairActivity.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        atTime = time;
                        day.setText(time);
                    }

                }, atTime, "2500-12-31 23:59:59");
                timeSelector1.setIsLoop(false);//设置不循环,true循环
                timeSelector1.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector1.show();
                break;
            case R.id.footer:
                //action_text.getText().toString(); repair_user date_str
                String setValue=confirmation.getText().toString().trim();
                if(setValue.equals("未填写")){
                    Toast.makeText(CreatRepairActivity.this,"请填写信息",Toast.LENGTH_SHORT).show();
                }else{
                    new Thread(repairTask).start();
                }
                break;
            case R.id.remarks_layout:
                date_str = day.getText().toString();
                intent = new Intent(CreatRepairActivity.this, CreatRepairRemarksActivity.class);
                at_action = action_text.getText().toString();//获取标题
                //获取标题背景颜色
                Drawable background = action.getBackground();
                ColorDrawable colorDrawable = (ColorDrawable) background;
                int color = colorDrawable.getColor();
                Bundle bundle = new Bundle();
                bundle.putString("at_action", at_action);
                bundle.putString("date", date_str);
                bundle.putInt("drawable", color);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 网络操作相关的子线程okhttp框架  获取设备
     */
    Runnable repairTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作

            String title=action_text.getText().toString();
            String url = "http://192.168.1.113:8080/app/ppt7000/intsertPpt7000.do?memId=" + memid + "&ts=" + ts ;
            OkHttpClient okHttpClient = new OkHttpClient();
            System.out.println("验证：" + sign);
            String b="{\"repireDescription\": \""+repireDescription+"\",\"opName\": \""+opName+"\",\"parentId\": \""+memid+"\"," +
                    "\"handleDate\": \""+atTime+"\",\"repireTitle\": \""+title+"\",\"eqpId\": \""+device_id+"\"," +
                    "\"memPhone\": \""+memPhone+"\",\"otherInfoJson\": {\"lo\": \"\"," +
                    "\"dt\": \"\",\"da\": \"\",\"pc\": \"\",\"sq\": \"\",\"la\": \"\",\"fa\": \""+fa+"\",\"te\": \"\",\"pv\": \"\",\"ar\": \"\",\"ct\": \"\"}}";

            System.out.println("提交："+b);
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
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    message.obj = result.toString();
                    getReqirHandler.sendMessage(message);
                }
                System.out.println("结果：" + result + "状态码：" + response.code());
                //Toast.makeText(EditPassWordActivity.this,result,Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private Handler getReqirHandler = new Handler() {

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
                            new AlertDialog.Builder(CreatRepairActivity.this).setTitle("报修提示").setMessage("已报修").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent=new Intent(CreatRepairActivity.this,DeviceListActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }).show();
                        } else {
                            new AlertDialog.Builder(CreatRepairActivity.this).setTitle("报修提示").setMessage(object1.getString("msg")).setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
    /**
     * 初始化页卡内容区
     */
    private void InitViewPager() {

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(new MFragmentPagerAdapter(fragmentManager, fragmentArrayList));

        //让ViewPager缓存2个页面
        mViewPager.setOffscreenPageLimit(1);

        //设置默认打开第一页
        mViewPager.setCurrentItem(0);
        //设置viewpager页面滑动监听事件
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 初始化Fragment，并添加到ArrayList中
     */
    private void InitFragment() {
        fragmentArrayList = new ArrayList<Fragment>();
        Fragment1 fragment1 = new Fragment1();
        Bundle bundle = new Bundle();
        bundle.putIntArray("icon", icno);
        bundle.putStringArray("name", name);
        fragment1.setArguments(bundle);
        fragmentArrayList.add(fragment1);
        fragmentManager = getSupportFragmentManager();

    }

    /**
     * 页卡切换监听
     *
     * @author CZK
     * @version 1.0
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            switch (position) {
                case 0:
                    Fragment1 fragment1 = new Fragment1();
                    Bundle bundle = new Bundle();
                    bundle.putIntArray("icon", icno);
                    bundle.putStringArray("name", name);
                    fragment1.setArguments(bundle);
                    ft.replace(android.R.id.content, fragment1);
                    //Toast.makeText(NotepadActivity.this,"页面"+(position+1),Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < ll.getChildCount(); i++) {
                        ll.getChildAt(i).setSelected(false);
                    }
                    ll.getChildAt(position).setSelected(true);
                    break;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    ;
}
