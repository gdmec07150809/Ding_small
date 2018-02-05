package com.example.administrator.ding_small;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.Adapter.AdvertisementGvAdapter;
import com.example.administrator.ding_small.Fragment.AdvertisementFragment;
import com.example.administrator.ding_small.HelpTool.LocationUtil;
import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity;
import com.example.administrator.ding_small.PersonalCenter.PersonalCenterActivity;
import com.example.administrator.ding_small.Utils.utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by CZK on 2017/12/11.
 */
public class MainLayoutActivity extends FragmentActivity implements View.OnClickListener {

    private int nowPosition = 0;
    private Timer timer;
    private TimerTask task;
    private ViewPager mPager;
    private AdvertisementGvAdapter advertisementGvAdapter;
    @ViewInject(R.id.pager_position_gv)
    private GridView pager_position_gv;
    private long clickTime = 0;
    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    String memid, token, UserSign, oldPass, newPass, ts,sign,imgUrl;
    String nameStr="";
    //变化语言所改变的控件 登陆、客服、消息、名称、记事日历、记账日历、记事本、记账本、设备表、联系人,扫码、搜索、报修,首页,口碑服务,特供商城,我的
    private TextView login_text, custom_service_text, news_text, name_text, notepad_calendar_text, account_calendar_text,
            notepad_text, account_text, device_text, contacts_text, scan_text, search_text, repair_text, home_text, reputation_service_text, mall_text, my_text;
    private EditText user_name;
    public static final int SHOW_RESPONSE = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置contentFeature,可使用切换动画
        setContentView(R.layout.main_layout);
        init();//初始化控
        getLocation();//获取地址
        getCacheUser();//获取用户信息
        // ifApm();//判断上下午
        initPager();//轮播图


        changeLanguage();//设置语言
        //getCache();//获取轮播图


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocation() {
//        if(Build.VERSION.SDK_INT>=23){
//            System.out.println("版本号："+Build.VERSION.SDK_INT);
//            //获取地址
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
//                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)){
//                    //如果没勾选“不再询问”，向用户发起权限请求
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
//                }
//                if("0.0".equals(Double.toString(LocationUtil.longitude))){
//                    new AlertDialog.Builder(MainLayoutActivity.this).setTitle("权限提示").setMessage("请手动打开定位权限").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            startActivityForResult(intent, 10);
//                        }
//                    }).show();
//
//                }
//
//
////            // 转到手机设置界面，用户设置GPS
////            Intent intent = new Intent(
////                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////            startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
//            }
//        }else{
//            System.out.println("版本号："+Build.VERSION.SDK_INT);
//            LocationUtil.initLocation(MainLayoutActivity.this);
//            System.out.println("主经度:" + Double.toString(LocationUtil.longitude) + "主纬度：" + Double.toString(LocationUtil.latitude));
////            longitude_str=Double.toString(LocationUtil.longitude);
////            latitude_str=Double.toString(LocationUtil.latitude);
//            if("0.0".equals(Double.toString(LocationUtil.longitude))){
//                new AlertDialog.Builder(MainLayoutActivity.this).setTitle("权限提示").setMessage("请手动打开定位权限").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        startActivityForResult(intent, 10);
//                    }
//                }).show();
//
//            }
//
//        }
        if(Build.VERSION.SDK_INT==23){
            System.out.println("版本号："+Build.VERSION.SDK_INT);
            int checkCallPhonePermission =
                    ContextCompat.checkSelfPermission(MainLayoutActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //ActivityCompat.requestPermissions(MainLayoutActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 10);


            }else {
                if ("0.0".equals(Double.toString(LocationUtil.longitude))) {
                    new AlertDialog.Builder(MainLayoutActivity.this).setTitle("权限提示").setMessage("请手动打开定位权限").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 10);
                        }
                    }).show();
                }
            }
        }else if (Build.VERSION.SDK_INT > 23) {
            System.out.println("版本号："+Build.VERSION.SDK_INT);
            int checkCallPhonePermission =
                    ContextCompat.checkSelfPermission(MainLayoutActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //ActivityCompat.requestPermissions(MainLayoutActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 10);



            }
        }else{
            System.out.println("版本号："+Build.VERSION.SDK_INT);
        }
    }
    private void changeLanguage() {
        // Toast.makeText(MainLayoutActivity.this,"当前系统语言："+Locale.getDefault().getLanguage(),Toast.LENGTH_SHORT).show();
        if (Locale.getDefault().getLanguage().equals("en")) {
            /*login_text,custom_service_text,news_text,name_text,notepad_calendar_text,account_calendar_text,notepad_text,account_text,
            device_text,contacts_text,scan_text,search_text,repair_text,home_text,reputation_service_text,mall_text,my_text*/
            login_text.setText("Login");
            custom_service_text.setText("Service");
            news_text.setText("News");
            notepad_calendar_text.setText("Notepad_Calendar");
            account_calendar_text.setText("Account_Calendar");
            notepad_text.setText("Notepad_Book");
            account_text.setText("Account_Book");
            device_text.setText("Device_List");
            contacts_text.setText("Contacts");
            scan_text.setText("Scan");
            search_text.setText("Search");
            repair_text.setText("Repair");
            home_text.setText("Home");
            reputation_service_text.setText("Rreputation Service");
            mall_text.setText("Mall");
            my_text.setText("My");
            user_name.setHint("Search");
        }
    }

    private void getCacheUser() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        String url = utils.url+"/api/secr/user/getPersonalInfo.do";
        ts = String.valueOf(new Date().getTime());
        System.out.println("首页：" + memid + "  ts:" + ts + "  token:" + token);
        String Sign = url + memid + token + ts;
        System.out.println("UserSign:" + Sign);
        UserSign = MD5Utils.md5(Sign);
        if(memid!=null&&!memid.equals("null")){
            new Thread(getUserTask).start();//获取用户信息,启动
        }else{
            Handler handler1 = new Handler();
            handler1.postDelayed(runnable, 100);
        }
    }

    private void getCache() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
       // String url = "http://192.168.1.108:8080/app/invs6002/lisSecr6002.do";轮播图
//        String url = "http://192.168.1.108:8080/app/ppt7000/memberImgUpload.do";
//       // ts = String.valueOf(new Date().getTime());
//        System.out.println("首页：memId" + memid + "  ts:" + ts + "  token:" + token);
//        String Sign = url + memid + token + ts;
//        System.out.println("Sign:" + Sign);
//        sign = MD5Utils.md5(Sign);
//        System.out.println("加密sign:" + sign);
      // new Thread(networkTask).start();//获取轮播图
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            ifApm(Locale.getDefault().getLanguage());//判断上下午
            System.out.println("判断上下午");
            handler.postDelayed(this, 1000 * 60 * 30);
        }
    };

    private void init() {
        mPager = findViewById(R.id.home_activity_pager);
        findViewById(R.id.notepad_layout).setOnClickListener(this);
        findViewById(R.id.account_layout).setOnClickListener(this);
        findViewById(R.id.device_layout).setOnClickListener(this);
        findViewById(R.id.contacts_layout).setOnClickListener(this);
        findViewById(R.id.notepad_calendar).setOnClickListener(this);
        findViewById(R.id.account_calendar).setOnClickListener(this);
        findViewById(R.id.login_layout).setOnClickListener(this);
        findViewById(R.id.repair_layout).setOnClickListener(this);
        findViewById(R.id.search_layout).setOnClickListener(this);
        findViewById(R.id.more_layout).setOnClickListener(this);
        findViewById(R.id.personalcenter_layout).setOnClickListener(this);

        findViewById(R.id.scan_layout).setOnClickListener(this);
        /*login_text,custom_service_text,news_text,name_text,notepad_calendar_text,account_calendar_text,notepad_text,account_text,
        device_text,contacts_text,scan_text,search_text,repair_text,home_text,reputation_service_text,mall_text,my_text*/
        login_text = findViewById(R.id.login_text);
        custom_service_text = findViewById(R.id.custom_service_text);
        news_text = findViewById(R.id.news_text);
        name_text = findViewById(R.id.name_text);
        notepad_calendar_text = findViewById(R.id.notepad_calendar_text);
        account_calendar_text = findViewById(R.id.account_calendar_text);
        notepad_text = findViewById(R.id.notepad_text);
        account_text = findViewById(R.id.account_text);
        device_text = findViewById(R.id.device_text);
        contacts_text = findViewById(R.id.contacts_text);
        scan_text = findViewById(R.id.scan_text);
        search_text = findViewById(R.id.search_text);
        repair_text = findViewById(R.id.repair_text);
        home_text = findViewById(R.id.home_text);
        reputation_service_text = findViewById(R.id.reputation_service_text);
        mall_text = findViewById(R.id.mall_text);
        my_text = findViewById(R.id.my_text);
        user_name = findViewById(R.id.user_name);


        ViewUtils.inject(this);
    }

    private int[] images = {R.mipmap.banner, R.mipmap.banner2, R.mipmap.banner3, R.mipmap.banner4};//创建图片整形数组

    private void initPager() {//轮播图界面操作
        List<android.support.v4.app.Fragment> fragmentList = new ArrayList<Fragment>();

        //填充轮播图数据
        for (int i = 0; i < images.length; i++) {
            android.support.v4.app.Fragment advertisementFragment = new AdvertisementFragment();
            Bundle bundle = new Bundle();//Bundle类就是用键值对储存数据
            bundle.putInt("image", images[i]);
            advertisementFragment.setArguments(bundle);
            fragmentList.add(advertisementFragment);
        }

        mPager.setOffscreenPageLimit(images.length);//设置图片切换个数
        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyPageChangeListener());

        advertisementGvAdapter = new AdvertisementGvAdapter(images.length, MainLayoutActivity.this);
        pager_position_gv.setAdapter(advertisementGvAdapter);
        pager_position_gv.setNumColumns(images.length);//设置小圆按钮个数
        utils.setGvwidth(advertisementGvAdapter, pager_position_gv);//设置小圆按钮宽度
        //小圆按钮点击事件
        pager_position_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPager.setCurrentItem(i);
            }
        });
        task = new TimerTask() {
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        timer = new Timer(true);
        timer.schedule(task, 1000, 1000);//设置定时器,进入页面1秒开始滚动,间隔1秒
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mPager != null) {
                if (nowPosition == images.length) {
                    nowPosition = 0;
                } else {
                    nowPosition++;
                }
                mPager.setCurrentItem(nowPosition);
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        task = new TimerTask() {
            @Override
            public void run() {
                if (mPager != null && nowPosition >= 0) {
                    mPager.setCurrentItem(nowPosition);
                }
            }
        };
        timer = new Timer(true);
        timer.schedule(task, 1000, 1000);
    }

    //事件触发
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.notepad_layout://记事本
                Toast.makeText(getApplicationContext(), "该功能暂未对外开放！！！", Toast.LENGTH_SHORT).show();
//                intent=new Intent(MainLayoutActivity.this,NotepadBtnActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                break;
            case R.id.account_layout://记账本
                Toast.makeText(getApplicationContext(), "该功能暂未对外开放！！！", Toast.LENGTH_SHORT).show();
//                intent=new Intent(MainLayoutActivity.this,AccountBookActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                break;
            case R.id.device_layout://设备表
                getCache();
                System.out.println(memid);
                if (!memid.equals("null") && memid != null) {
                    intent = new Intent(MainLayoutActivity.this, DeviceListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    new AlertDialog.Builder(this).setTitle("请登陆")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MainLayoutActivity.this, LoginAcitivity.class);
                                    intent.putExtra("back","in");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.activity_anim_in, R.anim.activity_anim_out);
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
            case R.id.contacts_layout://联系人
                Toast.makeText(getApplicationContext(), "该功能暂未对外开放！！！", Toast.LENGTH_SHORT).show();
//                intent=new Intent(MainLayoutActivity.this,ContactsActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                break;
            case R.id.notepad_calendar://记事本日历
                Toast.makeText(getApplicationContext(), "该功能暂未对外开放！！！", Toast.LENGTH_SHORT).show();
//                intent=new Intent(MainLayoutActivity.this,NotepadSearchByCalendarActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                break;
            case R.id.account_calendar://记账本日历
                Toast.makeText(getApplicationContext(), "该功能暂未对外开放！！！", Toast.LENGTH_SHORT).show();
//                intent=new Intent(MainLayoutActivity.this,AccountsSearchByCalendarActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                break;
            case R.id.login_layout://登陆
                intent = new Intent(MainLayoutActivity.this, LoginAcitivity.class);
                intent.putExtra("back","in");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_anim_in, R.anim.activity_anim_out);
                break;
            case R.id.repair_layout://报修
                getCache();
                System.out.println(memid);
                if (!memid.equals("null") && memid != null) {
                    intent = new Intent(MainLayoutActivity.this, SelectDeviceActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    new AlertDialog.Builder(this).setTitle("请登陆")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MainLayoutActivity.this, LoginAcitivity.class);
                                    intent.putExtra("back","in");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.activity_anim_in, R.anim.activity_anim_out);
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
            case R.id.search_layout://搜索
                getCache();
                System.out.println(memid);
                if (!memid.equals("null") && memid != null) {
                    intent = new Intent(MainLayoutActivity.this, DeviceSearchActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    new AlertDialog.Builder(this).setTitle("请登陆")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MainLayoutActivity.this, LoginAcitivity.class);
                                    intent.putExtra("back","in");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.activity_anim_in, R.anim.activity_anim_out);
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
            case R.id.more_layout://更多功能暂未开发
                Toast.makeText(getApplicationContext(), "该功能暂未对外开放！！！", Toast.LENGTH_SHORT).show();
                //System.out.println("32px:"+DensityUtil.px2sp(MainLayoutActivity.this,32));
//                intent = new Intent(MainLayoutActivity.this, TestActivity.class);//测试接口类
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);

                break;
            case R.id.scan_layout://扫码功能

                getCache();
                System.out.println(memid);
                if (!"null".equals(memid) && memid != null) {

                    if (Build.VERSION.SDK_INT >= 23) {
                        int checkCallPhonePermission =
                                ContextCompat.checkSelfPermission(MainLayoutActivity.this,Manifest.permission.CAMERA);
                        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainLayoutActivity.this, new String[]{Manifest.permission.CAMERA}, 222);
                            IntentIntegrator integrator = new IntentIntegrator(MainLayoutActivity.this);
                            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                            integrator.setPrompt("扫描二维码/条形码");
                            integrator.setCameraId(0);
                            integrator.setBeepEnabled(true);
                            integrator.initiateScan();
                            return;
                        } else {
                            IntentIntegrator integrator = new IntentIntegrator(MainLayoutActivity.this);
                            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                            integrator.setPrompt("扫描二维码/条形码");
                            integrator.setCameraId(0);
                            integrator.setBeepEnabled(true);
                            integrator.initiateScan();
                        }
                    } else {
                        IntentIntegrator integrator = new IntentIntegrator(MainLayoutActivity.this);
                        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                        integrator.setPrompt("扫描二维码/条形码");
                        integrator.setCameraId(0);
                        integrator.setBeepEnabled(true);
                        integrator.initiateScan();
                    }


                } else {
                    new AlertDialog.Builder(this).setTitle("请登陆")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MainLayoutActivity.this, LoginAcitivity.class);
                                    intent.putExtra("back","in");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.activity_anim_in, R.anim.activity_anim_out);
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

//                 intent=new Intent(MainLayoutActivity.this,PerfectDeviceActivity.class);
//                startActivity(intent);
                break;
            case R.id.personalcenter_layout://我的
                getCache();
                if (!"null".equals(memid) && memid != null) {
                    intent = new Intent(MainLayoutActivity.this, PersonalCenterActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("nameStr", nameStr);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    new AlertDialog.Builder(this).setTitle("请登陆")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MainLayoutActivity.this, LoginAcitivity.class);
                                    intent.putExtra("back","in");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.activity_anim_in, R.anim.activity_anim_out);
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
            default:
                break;
        }
    }

    //viewpager适配器
    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> list;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            // TODO Auto-generated constructor stub
            this.list = list;
        }

        @Override
        public Fragment getItem(int index) {
            // TODO Auto-generated method stub
            return list.get(index);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public void finishUpdate(View container) {
            // TODO Auto-generated method stub
            super.finishUpdate(container);

        }
    }

    //viewpager状态监听
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        //viewpager页面变更监听
        @Override
        public void onPageSelected(int position) {
            PageHandler.sendEmptyMessage(position);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }
    }

    private Handler PageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            advertisementGvAdapter.setPosition(msg.what);
            nowPosition = msg.what;
        }
    };


    //重写onKeyDown方法,实现双击退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //退出事件
    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再次点击退出", Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            this.finish();
            System.exit(0);
        }
    }

    //判断是上午还是下午
    private void ifApm(String lang) {
        Time localTime = new Time("Asia/Hong_Kong");
        localTime.setToNow();
        int time = Integer.parseInt(localTime.format("%H"));
        System.out.println(localTime.format("%H"));
        if (lang.equals("en")) {
            if (time > 0 && time <= 6) {
                if(nameStr!=null&&!nameStr.equals("")){
                    name_text.setText(nameStr+" Good Morning");
                }else{
                    name_text.setText("Good Morning");
                }
            } else if (time > 6 && time <= 12) {
                if(nameStr!=null&&!nameStr.equals("")){
                    name_text.setText(nameStr+" Good Morning");
                }else{
                    name_text.setText("Good Morning");
                }
            } else if (time > 12 && time < 18) {
                if(nameStr!=null&&!nameStr.equals("")){
                    name_text.setText(nameStr+" Good Afternoon");
                }else{
                    name_text.setText("Good Afternoon");
                }
            } else {
                if(nameStr!=null&&!nameStr.equals("")){
                    name_text.setText(nameStr+" Good Evening");
                }else{
                    name_text.setText("Good Evening");
                }

            }
        } else {
            if (time > 0 && time <= 6) {
                if(nameStr!=null&&!nameStr.equals("")){
                    name_text.setText(nameStr+" 凌晨好");
                }else {
                    name_text.setText("凌晨好");
                }
            } else if (time > 6 && time <= 12) {
                if(nameStr!=null&&!nameStr.equals("")){
                    name_text.setText(nameStr+" 上午好");
                }else {
                    name_text.setText("上午好");
                }
            } else if (time > 12 && time < 18) {
                if(nameStr!=null&&!nameStr.equals("")){
                    name_text.setText(nameStr+" 下午好");
                }else {
                    name_text.setText("下午好");
                }
            } else {
                if(nameStr!=null&&!nameStr.equals("")){
                    name_text.setText(nameStr+" 晚上好");
                }else {
                    name_text.setText("晚上好");
                }
            }
        }

    }

    //接收扫描结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(resultCode==10){
            Toast.makeText(this, "返回", Toast.LENGTH_SHORT).show();
        }
        if (result== null) {
            Toast.makeText(this, "扫描失败", Toast.LENGTH_SHORT).show();
        } else {
            //http://www.ding-new.com/pptappdwnld.do?p1=16402&p2=03&p3=2w&p4=yy  试用链接
            System.out.println("扫描结果：" + result.getContents());
            // result.getContents().substring(result.getContents().indexOf("="), result.getContents().indexOf("&"));
            String mac_str = null;
            try {
                mac_str = result.getContents().substring(result.getContents().indexOf("=") + 1, result.getContents().indexOf("&"));
                System.out.println("截取结果：" + result.getContents().substring(result.getContents().indexOf("=") + 1, result.getContents().indexOf("&")));
            } catch (Exception e) {
                Toast.makeText(this, "该设备不存在", Toast.LENGTH_SHORT).show();
            }
            if (mac_str != null) {
                Intent intent = new Intent(MainLayoutActivity.this, PerfectDeviceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("device_mac", mac_str);
                bundle.putString("activity", "1");
                intent.putExtras(bundle);
                startActivity(intent);
            }
            //resultNew.setText("扫描结果："+result.getContents());
        }
    }

    /**
     * 网络操作相关的子线程okhttp框架  获取图片
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作

            //System.out.println("图片："+getBitmap.getBitmapPhoto("https://avatar.csdn.net/C/3/5/1_hmyang314.jpg"));
           // String url = "http://192.168.1.108:8080/app/invs6002/lisSecr6002.do?memId=" + memid + "&ts=" + ts ;轮播图
            String url = "http://192.168.1.117:8080/app/secr9000/statisticsSummarySecr9000.do?memId=" + memid + "&ts=" + ts ;
            OkHttpClient okHttpClient = new OkHttpClient();
          //  String b = "{\"advType\":\"0\",\"dateNow \":\""+ts+"\"}";//轮播图json字符串
            String b = "{\"dateFm\":\"2017-12-01\",\"dateTo\":\"2018-03-01\",\"memId\":\""+memid+"\"}";//json字符串
            System.out.println("验证：" + sign +" "+b);
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
                   // getDeviceListHandler.sendMessage(message);
                }
                System.out.println("结果：" + result + "状态码：" + response.code());
                //Toast.makeText(EditPassWordActivity.this,result,Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 网络操作相关的子线程okhttp框架  获取用户信息
     */
    Runnable getUserTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String url = utils.url+"/api/secr/user/getPersonalInfo.do?memId=" + memid + "&ts=" + ts;
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(10,TimeUnit.SECONDS).build();

            System.out.println("验证：" + UserSign);
            String b = "{}";//json字符串
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), b);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("sIgn", UserSign)
                    .build();
            System.out.println(request.headers());
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                String result = response.body().string();
                if (response != null) {
                    //在子线程中将Message对象发出去
                    Message message = new Message();
                    message.what = 0;
                    message.obj = result.toString();
                    getUserHandler.sendMessage(message);
                }
                System.out.println("结果：" + result + "状态码：" + response.code());
                //Toast.makeText(EditPassWordActivity.this,result,Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private Handler getUserHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String response = (String) msg.obj;
                    System.out.println(response);

                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject object1 = new JSONObject(object.getString("meta"));
                        JSONObject objectData = new JSONObject(object.getString("data"));
                        //{"meta":{"res":"99999","msg":"用户名或密码有误"},"data":null}状态码：200

                            if (object1.getString("res").equals("00000")) {
                                nameStr=objectData.getString("nick");
                                imgUrl=objectData.getString("imgUrl");
                                Handler handler = new Handler();
                                handler.postDelayed(runnable, 100);
                                //储存token,备用
                                sp = MainLayoutActivity.this.getSharedPreferences(tokeFile, MODE_PRIVATE);//实例化
                                SharedPreferences.Editor editor = sp.edit(); //使处于可编辑状态
                                editor.putString("nameStr", nameStr);
                                editor.putString("imgUrl", imgUrl);
                                editor.commit();    //提交数据保存
                            } else {
                                new AlertDialog.Builder(MainLayoutActivity.this).setTitle("网络提示").setMessage("请检查网络").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
