package com.example.administrator.ding_small;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.CarouselTool.MZModeBannerFragment;
import com.example.administrator.ding_small.HelpTool.LocationUtil;
import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity;
import com.example.administrator.ding_small.PersonalCenter.PersonalCenterActivity;
import com.example.administrator.ding_small.Utils.utils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.lidroid.xutils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.administrator.ding_small.HelpTool.DateUtils.changeweek;
import static com.example.administrator.ding_small.HelpTool.DateUtils.changeweekOne;
import static com.example.administrator.ding_small.NotepadActivity.TAG;


/**
 * Created by youyou000 on 2018/2/6.
 */

public class NewMainLayoutActivity extends FragmentActivity implements View.OnClickListener{
    private TextView name_text,time_text,home_text,my_text,wellcome_text,device_list_text,repair_text,search_btn,search_text;
    private ImageView home_img,my_img;
    private long clickTime = 0;
    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    String memid, token, UserSign, oldPass, newPass, ts,sign,imgUrl;
    String nameStr="";//用户
    private Dialog mCameraDialog;
    private LinearLayout search_lay;

    private static final String fristFile = "fristFile";//定义保存的文件的名称
    //重写onKeyDown方法,实现双击退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //两秒内点击两次退出,则退出
    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            if (Locale.getDefault().getLanguage().equals("en")) {
                Toast.makeText(getApplicationContext(), "Click out again", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "再次点击退出", Toast.LENGTH_SHORT).show();
            }
            clickTime = System.currentTimeMillis();
        } else {
            Log.e(TAG, "exit application");
            System.exit(0);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_main_layout);
        /*判断是否第一次安装*/
        if(getIntent().getStringExtra("loginCache")!=null&&!getIntent().getStringExtra("loginCache").equals("")){
            firstLoginDialog();
        }
        init();//初始化控
        getLocation();//获取位置权限
        getCacheUser();//获取用户信息
        //轮播图绑定
        Fragment fragment = MZModeBannerFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.home_container,fragment).commit();

        //获取当前年月日时分
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month;
        int date = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int ss=t.second;
        String dateStr=year+"年"+(month + 1)+"月"+date+"日";
        String time_str=dateStr+hour+"时"+minute+"分"+ss+"秒";
        //String ts1 = String.valueOf(new Date().getTime());
        System.out.println("时间："+week(time_str));
        time_text.setText((month+1)+"月"+date+"日  "+week(time_str));

        changeTextView();//更换语言
    }

    private void changeTextView(){
        if (Locale.getDefault().getLanguage().equals("en")){
            wellcome_text.setText("Hello, welcome to use");
            repair_text.setText("Repair");
            device_list_text.setText("Equipment table");
            search_text.setText("Enter a keyword search");
            search_btn.setText("Search");
            home_text.setText("Home");
            my_text.setText("My");
            if (!"null".equals(memid) && memid != null) {
            }else{
                name_text.setText("Please login");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocation() {
        if(Build.VERSION.SDK_INT==23) {
            System.out.println(Build.VERSION.SDK_INT);
            int checkCallPhonePermission =
                    ContextCompat.checkSelfPermission(NewMainLayoutActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(NewMainLayoutActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);
                System.out.println("开始授权");
            }else{
                LocationUtil.initLocation(NewMainLayoutActivity.this);
                System.out.println("主经度:" + Double.toString(LocationUtil.longitude) + "主纬度：" + Double.toString(LocationUtil.latitude));
                String   longitude_str=Double.toString(LocationUtil.longitude);
                if("0.0".equals(longitude_str)){
                    new AlertDialog.Builder(NewMainLayoutActivity.this).setTitle("权限提示").setMessage("请手动打开定位权限").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
                    ContextCompat.checkSelfPermission(NewMainLayoutActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //ActivityCompat.requestPermissions(MainLayoutActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
            }
            int checkCallPhonePermission1 =
                    ContextCompat.checkSelfPermission(NewMainLayoutActivity.this, Manifest.permission.CAMERA);
            if (checkCallPhonePermission1 != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(NewMainLayoutActivity.this, new String[]{Manifest.permission.CAMERA}, 222);
                IntentIntegrator integrator = new IntentIntegrator(NewMainLayoutActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            }
        }else{
            System.out.println("版本号："+Build.VERSION.SDK_INT);
        }
    }

    private void init() {
        name_text= (TextView) findViewById(R.id.name_text);
        time_text= (TextView) findViewById(R.id.time_text);
        home_text= (TextView) findViewById(R.id.home_text);
        my_text= (TextView) findViewById(R.id.my_text);
        home_img= (ImageView) findViewById(R.id.home_img);
        my_img= (ImageView) findViewById(R.id.my_img);
        wellcome_text=findViewById(R.id.wellcome_text);
        repair_text=findViewById(R.id.repair_text);
        device_list_text=findViewById(R.id.device_list_text);
        search_btn=findViewById(R.id.search_btn);
        search_text=findViewById(R.id.search_text);

        findViewById(R.id.repair_lay).setOnClickListener(this);
        findViewById(R.id.device_list_lay).setOnClickListener(this);
        findViewById(R.id.my_lay).setOnClickListener(this);
        findViewById(R.id.search_lay).setOnClickListener(this);


        home_img.setImageResource(R.mipmap.icon_home_active);
        home_text.setTextColor(ContextCompat.getColor(this,R.color.theme_color));
        my_img.setImageResource(R.mipmap.icon_my_normal);
        my_text.setTextColor(ContextCompat.getColor(this,R.color.time_color));
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
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.repair_lay://报修
                if (!memid.equals("null") && memid != null) {
                    intent = new Intent(NewMainLayoutActivity.this, SelectDeviceActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    intent = new Intent(NewMainLayoutActivity.this, LoginAcitivity.class);
                    intent.putExtra("back","in");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_anim_in, R.anim.activity_anim_out);
                }
                break;

            case R.id.device_list_lay://设备表
                if (!memid.equals("null") && memid != null) {
                    intent = new Intent(NewMainLayoutActivity.this, DeviceListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    intent = new Intent(NewMainLayoutActivity.this, LoginAcitivity.class);
                    intent.putExtra("back","in");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_anim_in, R.anim.activity_anim_out);
                }
                break;

            case R.id.my_lay://我的
                if (!"null".equals(memid) && memid != null) {
                    intent = new Intent(NewMainLayoutActivity.this, PersonalCenterActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("nameStr", nameStr);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    intent = new Intent(NewMainLayoutActivity.this, LoginAcitivity.class);
                    intent.putExtra("back","in");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_anim_in, R.anim.activity_anim_out);
                }
                break;

            case R.id.search_lay://搜索
                if (!"null".equals(memid) && memid != null) {
                    intent = new Intent(NewMainLayoutActivity.this, SearchBoxActiivty.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    intent = new Intent(NewMainLayoutActivity.this, LoginAcitivity.class);
                    intent.putExtra("back","in");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_anim_in, R.anim.activity_anim_out);
                }

                break;
        }
    }


    /**
     * 网络操作相关的子线程okhttp框架  获取用户信息
     */
    Runnable getUserTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String url = utils.url+"/api/secr/user/getPersonalInfo.do?memId=" + memid + "&ts=" + ts;
            System.out.println("用户："+url);
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

    /*处理获取用户信息*/
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

                            if(!nameStr.equals("")&&nameStr!=null){
                                name_text.setText(nameStr);
                            }else{
                                name_text.setVisibility(View.GONE);
                            }
                            //储存token,备用
                            sp = NewMainLayoutActivity.this.getSharedPreferences(tokeFile, MODE_PRIVATE);//实例化
                            SharedPreferences.Editor editor = sp.edit(); //使处于可编辑状态
                            editor.putString("nameStr", nameStr);
                            editor.putString("imgUrl", imgUrl);
                            editor.commit();    //提交数据保存
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
     * 输入日期如（2014年06月14日16时09分00秒）返回（星期数）
     *
     * @param time
     * @return
     */
    public String week(String time) {
        Date date = null;
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        int mydate = 0;
        String week = null;
        try {
            date = sdr.parse(time);
            Calendar cd = Calendar.getInstance();
            cd.setTime(date);
            mydate = cd.get(Calendar.DAY_OF_WEEK);
            // 获取指定日期转换成星期几
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (mydate == 1) {
            week = "星期日";
        } else if (mydate == 2) {
            week = "星期一";
        } else if (mydate == 3) {
            week = "星期二";
        } else if (mydate == 4) {
            week = "星期三";
        } else if (mydate == 5) {
            week = "星期四";
        } else if (mydate == 6) {
            week = "星期五";
        } else if (mydate == 7) {
            week = "星期六";
        }
        return week;
    }


    /*指示弹出窗*/
    private void firstLoginDialog() {
        RelativeLayout root = null;
        mCameraDialog = new Dialog(this, R.style.logindialog);
        root = (RelativeLayout) LayoutInflater.from(this).inflate(
                R.layout.first_login_dialog, null);
        //初始化视图
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
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
}
