package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.Adapter.AdvertisementGvAdapter;
import com.example.administrator.ding_small.Fragment.AdvertisementFragment;
import com.example.administrator.ding_small.R;
import com.example.administrator.ding_small.Utils.utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.id;
import static android.R.string.no;

/**
 * Created by Administrator on 2017/12/11.
 */
public class MainLayoutActivity extends FragmentActivity implements View.OnClickListener{

    private int nowPosition = 0;
    private Timer timer;
    private TimerTask task;
    private ViewPager mPager;
    private AdvertisementGvAdapter advertisementGvAdapter;
    @ViewInject(R.id.pager_position_gv)
    private GridView pager_position_gv;
    private long clickTime=0;
    private TextView name_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        init();//初始化控件
        ifApm();//判断上下午
        initPager();//轮播图
    }
    private void init(){
        mPager=findViewById(R.id.home_activity_pager);
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

        findViewById(R.id.scan_layout).setOnClickListener(this);
        name_text=findViewById(R.id.name_text);
        ViewUtils.inject(this);
    }
    private int[] images = {R.mipmap.banner, R.mipmap.banner2, R.mipmap.banner3,R.mipmap.banner4};//创建图片整形数组
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
                if (nowPosition == images.length){
                    nowPosition = 0;
                }else{
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
            public void run() {
              if (mPager != null&&nowPosition>=0) {
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
        switch (view.getId()){
            case R.id.notepad_layout://记事本
                intent=new Intent(MainLayoutActivity.this,NotepadBtnActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.account_layout://记账本
                intent=new Intent(MainLayoutActivity.this,AccountBookActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.device_layout://设备表
                intent=new Intent(MainLayoutActivity.this,DeviceListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.contacts_layout://联系人
                intent=new Intent(MainLayoutActivity.this,ContactsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.notepad_calendar://记事本日历
                intent=new Intent(MainLayoutActivity.this,NotepadSearchByCalendarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.account_calendar://记账本日历
                intent=new Intent(MainLayoutActivity.this,AccountsSearchByCalendarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.login_layout://登陆
                intent=new Intent(MainLayoutActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.repair_layout://报修
                intent=new Intent(MainLayoutActivity.this,SelectDeviceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.search_layout://搜索
                intent=new Intent(MainLayoutActivity.this,DeviceSearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.more_layout://更多功能暂不开发
                Toast.makeText(this,"该功能暂不开发",Toast.LENGTH_SHORT).show();
                break;
            case R.id.scan_layout://扫码功能
                    IntentIntegrator integrator=new IntentIntegrator(MainLayoutActivity.this);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrator.setPrompt("扫描二维码/条形码");
                    integrator.setCameraId(0);
                    integrator.setBeepEnabled(true);
                    integrator.initiateScan();
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

    private Handler PageHandler = new Handler(){
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
            Toast.makeText(getApplicationContext(), "再次点击退出",  Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            this.finish();
            System.exit(0);
        }
    }
    //判断是上午还是下午
    private void ifApm(){
        Time localTime = new Time("Asia/Hong_Kong");
        localTime.setToNow();
        int time= Integer.parseInt(localTime.format("%H"));
        System.out.println(localTime.format("%H"));
        if(time>0&&time<=6){
            name_text.setText("凌晨好");
        }else if(time>6&&time<=12){
            name_text.setText("上午好");
        }else if(time>12&&time<=18){
            name_text.setText("下午好");
        }else {
            name_text.setText("晚上好");
        }
    }
    //接受扫描结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result.getContents()==null){
            Toast.makeText(this,"扫描失败",Toast.LENGTH_SHORT).show();
        }else{
            System.out.println("扫描结果："+result.getContents());
           // Intent intent=new Intent(MainLayoutActivity.this,CreatRepairActivity.class);
            //startActivity(intent);
            //resultNew.setText("扫描结果："+result.getContents());
        }
    }
}
