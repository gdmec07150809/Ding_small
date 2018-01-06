package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.administrator.ding_small.Adapter.MFragmentPagerAdapter;
import com.example.administrator.ding_small.Fragment.Fragment1;
import com.example.administrator.ding_small.Fragment.Fragment2;

import org.feezu.liuli.timeselector.TimeSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/20.
 */

public class CreatRepairActivity extends FragmentActivity implements View.OnClickListener{
    LinearLayout ll,two;
    Intent intent;
    RelativeLayout action;
    private TextView day,time,action_text;
    private String atTime,at_action;
    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;
    //图标
    int icno[] = { R.drawable.c1_bg, R.drawable.c2_bg, R.drawable.c3_bg, R.drawable.c4_bg, R.drawable.c5_bg, R.drawable.c6_bg,
                    R.drawable.c7_bg,R.drawable.c8_bg, R.drawable.c9_bg, R.drawable.c10_bg, R.drawable.c11_bg, R.drawable.c12_bg };
    //图标下的文字
    String name[]={"不通电","不充电","不亮灯","不制冷","不排气","不防水",
            "漏水", "漏电","漏气","噪音大","外观破损","其它",
    };

    //实现Tab滑动效果
    private ViewPager mViewPager;

    //当前页卡编号
    private int currIndex = 0;

    //存放Fragment
    private ArrayList<Fragment> fragmentArrayList;

    //管理Fragment
    private FragmentManager fragmentManager;

    public Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creat_repair);
        InitFragment();//初始化流式布局
        InitViewPager();//初始化viewPager

        day=findViewById(R.id.day);
        day.setOnClickListener(this);
        action_text=findViewById(R.id.action_text);
        action=findViewById(R.id.action);
        findViewById(R.id.footer).setOnClickListener(this);
        findViewById(R.id.remarks_layout).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        //获取当前年月日时分
        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month;
        int date = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        //给时分赋值
        if(minute<10){
            String minute_text="0"+minute;
            atTime=year+"-"+(month+1)+"-"+(date+5)+"  "+hour+":"+minute_text;
            day.setText(atTime);
        }else{
            atTime=year+"-"+(month+1)+"-"+(date+5)+"  "+hour+":"+minute;
            day.setText(atTime);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.day:
                TimeSelector timeSelector = new TimeSelector(CreatRepairActivity.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        atTime=time;
                        day.setText(time);
                    }

                }, atTime, "2500-12-31 23:59:59");
                timeSelector.setIsLoop(false);//设置不循环,true循环
                timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.show();
                break;
            case R.id.footer:
                    intent=new Intent(CreatRepairActivity.this,DeviceListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                break;
            case R.id.remarks_layout:
                String date_str=day.getText().toString();
                intent=new Intent(CreatRepairActivity.this,CreatRepairRemarksActivity.class);
                at_action=action_text.getText().toString();//获取标题
                //获取标题背景颜色
                Drawable background = action.getBackground();
                ColorDrawable colorDrawable = (ColorDrawable) background;
                int color = colorDrawable.getColor();
                Bundle bundle=new Bundle();
                bundle.putString("at_action",at_action);
                bundle.putString("date",date_str);
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
    private void InitFragment(){
        fragmentArrayList = new ArrayList<Fragment>();
        Fragment1 fragment1=new Fragment1();
        Bundle bundle=new Bundle();
        bundle.putIntArray("icon",icno);
        bundle.putStringArray("name",name);
        fragment1.setArguments(bundle);
        fragmentArrayList.add(fragment1);
        fragmentManager = getSupportFragmentManager();

    }

    /**
     * 页卡切换监听
     * @author CZK
     * @version 1.0
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageSelected(int position) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            switch (position){
                case 0:
                    Fragment1 fragment1=new Fragment1();
                    Bundle bundle=new Bundle();
                    bundle.putIntArray("icon",icno);
                    bundle.putStringArray("name",name);
                    fragment1.setArguments(bundle);
                    ft.replace(android.R.id.content, fragment1);
                    //Toast.makeText(NotepadActivity.this,"页面"+(position+1),Toast.LENGTH_SHORT).show();
                    for (int i=0;i<ll.getChildCount();i++){
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
    };
}
