package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.example.administrator.ding_small.Adapter.AdvertisementGvAdapter;
import com.example.administrator.ding_small.Fragment.AdvertisementFragment;
import com.example.administrator.ding_small.R;
import com.example.administrator.ding_small.Utils.utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.id;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mPager=findViewById(R.id.home_activity_pager);
        findViewById(R.id.notepad_layout).setOnClickListener(this);
        findViewById(R.id.account_layout).setOnClickListener(this);
        findViewById(R.id.device_layout).setOnClickListener(this);
        findViewById(R.id.contacts_layout).setOnClickListener(this);
        findViewById(R.id.notepad_calendar).setOnClickListener(this);
        findViewById(R.id.account_calendar).setOnClickListener(this);
        ViewUtils.inject(this);
        initPager();//轮播图
    }
    private int[] images = {R.mipmap.banner, R.mipmap.banner2, R.mipmap.banner3,R.mipmap.banner4};//创建图片整形数组
    private void initPager() {//轮播图界面操作
        List<android.support.v4.app.Fragment> fragmentList = new ArrayList<Fragment>();

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
        pager_position_gv.setNumColumns(images.length);
        utils.setGvwidth(advertisementGvAdapter, pager_position_gv);
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
        timer.schedule(task, 1000, 1000);
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
                if (mPager != null) {
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
            case R.id.notepad_layout:
                intent=new Intent(MainLayoutActivity.this,NotepadBtnActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.account_layout:
                intent=new Intent(MainLayoutActivity.this,AccountBookActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.device_layout:
                break;
            case R.id.contacts_layout:
                intent=new Intent(MainLayoutActivity.this,ContactsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.notepad_calendar:
                intent=new Intent(MainLayoutActivity.this,NotepadSearchByCalendarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.account_calendar:
                intent=new Intent(MainLayoutActivity.this,AccountsSearchByCalendarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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

}
