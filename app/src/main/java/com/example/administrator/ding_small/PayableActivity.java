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
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.Adapter.MFragmentPagerAdapter;
import com.example.administrator.ding_small.Fragment.Fragment1;
import com.example.administrator.ding_small.Fragment.Fragment2;
import com.example.administrator.ding_small.Title.TitleActivity;

import org.feezu.liuli.timeselector.TimeSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.fragment;
import static com.example.administrator.ding_small.R.id.at_action;
import static com.example.administrator.ding_small.R.id.calendar;

/**
 * Created by CZK on 2017/10/27.
 */

public class PayableActivity extends FragmentActivity implements View.OnClickListener {
    LinearLayout ll;
    RelativeLayout action;
    private TextView number, number_enter, action_text, day;
    private int index = 0;
    private int small_index = 0;
    private ArrayList<String> arrays = null;
    private String addNumber = "", at_action, atTime, bg_number;
    private double sum = 0;
    private boolean isAdd = true;
    private boolean isFrist = true;
    Intent intent;
    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;

    //实现Tab滑动效果
    private ViewPager mViewPager;


    //当前页卡编号
    private int currIndex = 0;

    //存放Fragment
    private ArrayList<Fragment> fragmentArrayList;

    //管理Fragment
    private FragmentManager fragmentManager;

    public Context context;
    //图标
    int icno[] = {R.drawable.c1_bg, R.drawable.c2_bg, R.drawable.c3_bg, R.drawable.c4_bg, R.drawable.c5_bg, R.drawable.c6_bg,
            R.drawable.c7_bg, R.drawable.c8_bg, R.drawable.c9_bg, R.drawable.c10_bg, R.drawable.c11_bg, R.drawable.c12_bg,
            R.drawable.c13_bg, R.drawable.c14_bg, R.drawable.c1_bg, R.drawable.c2_bg, R.drawable.c3_bg, R.drawable.edit_add};
    //图标下的文字
    String name[] = {"待办事项", "常用数据", "一般数据", "生日", "身份证", "银行资料",
            "待办事项", "常用数据", "一般数据", "生日", "身份证", "银行资料",
            "待办事项", "常用数据", "一般数据", "生日", "身份证", "编辑"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creat_payable);
        findViewById(R.id.number_0).setOnClickListener(this);
        findViewById(R.id.number_1).setOnClickListener(this);
        findViewById(R.id.number_2).setOnClickListener(this);
        findViewById(R.id.number_3).setOnClickListener(this);
        findViewById(R.id.number_4).setOnClickListener(this);
        findViewById(R.id.number_5).setOnClickListener(this);
        findViewById(R.id.number_6).setOnClickListener(this);
        findViewById(R.id.number_7).setOnClickListener(this);
        findViewById(R.id.number_8).setOnClickListener(this);
        findViewById(R.id.number_9).setOnClickListener(this);
        findViewById(R.id.number_add).setOnClickListener(this);
        findViewById(R.id.number_remove).setOnClickListener(this);
        findViewById(R.id.number_clear_last).setOnClickListener(this);
        findViewById(R.id.number_small).setOnClickListener(this);
        findViewById(R.id.notepad).setOnClickListener(this);//记事
        findViewById(R.id.received).setOnClickListener(this);//已收
        findViewById(R.id.remark).setOnClickListener(this);//备注
        findViewById(R.id.payed).setOnClickListener(this);//已付
        findViewById(R.id.receivables).setOnClickListener(this);//待收
        findViewById(R.id.back).setOnClickListener(this);//返回
        number = (TextView) findViewById(R.id.number);
        number_enter = findViewById(R.id.number_enter);
        action_text = findViewById(R.id.action_text);
        number_enter.setOnClickListener(this);
        action = findViewById(R.id.action);
        day = findViewById(R.id.day);
        day.setOnClickListener(this);

        arrays = new ArrayList<String>();
        InitFragment();//添加fragment页面
        InitViewPager();//初始化控件
        initPoints();
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
            atTime = year + "-" + (month + 1) + "-" + date + "  " + hour + ":" + minute_text;
            day.setText(atTime);
        } else {
            atTime = year + "-" + (month + 1) + "-" + date + "  " + hour + ":" + minute;
            day.setText(atTime);
        }

//        gridView = (GridView) findViewById(R.id.gridview);
//        //初始化数据
//        initData();
//
//        String[] from={"img","text"};
//
//        int[] to={R.id.img,R.id.text};
//
//        adapter=new SimpleAdapter(this, dataList, R.layout.gride_view_item, from, to);
//
//        gridView.setAdapter(adapter);
//
//        gridView.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                    long arg3) {
////                AlertDialog.Builder builder= new AlertDialog.Builder(NotepadActivity.this);
////                System.out.println("选择背景："+icno[arg2]);
////                arg1.setDrawingCacheEnabled(true);
////                System.out.println("路径:"+ arg1.getDrawingCache());
////                arg1.setDrawingCacheEnabled(false);
////                builder.setTitle("提示").setMessage(dataList.get(arg2).get("text").toString()).create().show();
//                Drawable drawable=arg1.getBackground();
//                if(dataList.get(arg2).get("text").toString().equals("编辑")){
//                    intent=new Intent(PayableActivity.this,TitleActivity.class);
//                    startActivity(intent);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                }else{
//                    at_action=dataList.get(arg2).get("text").toString();
//                    action_text.setText(at_action);
//                    switch (arg2){
//                        case 0:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg1));
//                            break;
//                        case start1:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg2));
//
//                            break;
//                        case start2:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg3));
//                            break;
//                        case start3:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg4));
//
//                            break;
//                        case start4:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg5));
//
//                            break;
//                        case 5:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg6));
//
//                            break;
//                        case 6:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg7));
//
//                            break;
//                        case 7:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg8));
//
//                            break;
//                        case 8:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg9));
//                            break;
//                        case 9:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg10));
//                            break;
//                        case 10:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg11));
//                            break;
//                        case 11:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg12));
//                            break;
//                        case 12:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg13));
//                            break;
//                        case 13:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg14));
//                            break;
//                        case 14:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg1));
//                            break;
//                        case 15:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg2));
//                            break;
//                        case 16:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg3));
//                            break;
//                        case 17:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg4));
//                            break;
//                        case 18:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg5));
//                            break;
//                        case 19:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg6));
//                            break;
//                        case 20:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg7));
//                            break;
//                        case 21:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg8));
//                            break;
//                        case 22:
//                            action.setBackgroundColor(getResources().getColor(R.color.bg9));
//                            break;
//
//                    }
//                    bg_number=(arg2)+"";
//                }
//
//            }
//        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.number_0:
                if (index == 0) {
                    if (number.getText().toString().substring(0, 1).equals("0")) {
                        if (number.getText().toString().length() > 1) {
                            if (number.getText().toString().substring(1, 2).equals(".")) {
                                number.setText(number.getText().toString().substring(0) + "0");
                                index++;
                                break;
                            } else {
                                number.setText(number.getText().toString().substring(0) + "0");
                                index = 0;
                                break;
                            }
                        } else if (number.getText().toString().length() == 1) {
                            number.setText("0");
                            index = 0;
                            break;
                        }
                    }
                } else {
                    number.setText(number.getText().toString().substring(0) + "0");
                }
                index++;
                break;
            case R.id.number_1:
                if (index == 0) {
                    if (number.getText().toString().substring(0, 1).equals("0")) {
                        number.setText(number.getText().toString().substring(1) + "start1");
                    }
                } else {
                    number.setText(number.getText().toString().substring(0) + "start1");
                }
                index++;
                break;
            case R.id.number_2:
                if (index == 0) {
                    if (number.getText().toString().substring(0, 1).equals("0")) {
                        number.setText(number.getText().toString().substring(1) + "start2");
                    }
                } else {
                    number.setText(number.getText().toString().substring(0) + "start2");
                }
                index++;
                break;
            case R.id.number_3:
                if (index == 0) {
                    if (number.getText().toString().substring(0, 1).equals("0")) {
                        number.setText(number.getText().toString().substring(1) + "start3");
                    }
                } else {
                    number.setText(number.getText().toString().substring(0) + "start3");
                }
                index++;
                break;
            case R.id.number_4:
                if (index == 0) {
                    if (number.getText().toString().substring(0, 1).equals("0")) {
                        number.setText(number.getText().toString().substring(1) + "start4");
                    }
                } else {
                    number.setText(number.getText().toString().substring(0) + "start4");
                }
                index++;
                break;
            case R.id.number_5:
                if (index == 0) {
                    if (number.getText().toString().substring(0, 1).equals("0")) {
                        number.setText(number.getText().toString().substring(1) + "5");
                    }
                } else {
                    number.setText(number.getText().toString().substring(0) + "5");
                }
                index++;
                break;
            case R.id.number_6:
                if (index == 0) {
                    if (number.getText().toString().substring(0, 1).equals("0")) {
                        number.setText(number.getText().toString().substring(1) + "6");
                    }
                } else {
                    number.setText(number.getText().toString().substring(0) + "6");
                }
                index++;
                break;
            case R.id.number_7:
                if (index == 0) {
                    if (number.getText().toString().substring(0, 1).equals("0")) {
                        number.setText(number.getText().toString().substring(1) + "7");
                    }
                } else {
                    number.setText(number.getText().toString().substring(0) + "7");
                }
                index++;
                break;
            case R.id.number_8:
                if (index == 0) {
                    if (number.getText().toString().substring(0, 1).equals("0")) {
                        number.setText(number.getText().toString().substring(1) + "8");
                    }
                } else {
                    number.setText(number.getText().toString().substring(0) + "8");
                }
                index++;
                break;
            case R.id.number_9:
                if (index == 0) {
                    if (number.getText().toString().substring(0, 1).equals("0")) {
                        number.setText(number.getText().toString().substring(1) + "9");
                    }
                } else {
                    number.setText(number.getText().toString().substring(0) + "9");
                }
                index++;
                break;
            case R.id.number_small:
                if (small_index == 0) {
                    number.setText(number.getText().toString().substring(0) + ".");
                    small_index++;
                    index++;

                } else {
                    small_index++;
                    index++;
                }
                break;
            case R.id.number_clear_last:
                arrays.clear();
                number.setText("0");
                index = 0;
                small_index = 0;
                break;

            case R.id.number_add:
                sum += Double.valueOf(number.getText().toString());
                number.setText("0");
                index = 0;
                isAdd = true;
                small_index = 0;
                number_enter.setText("=");
                break;
            case R.id.number_remove:
                if (isFrist) {
                    sum += Double.valueOf(number.getText().toString());
                } else {
                    sum -= Double.valueOf(number.getText().toString());
                }
                number.setText("0");
                index = 0;
                small_index = 0;
                isAdd = false;
                number_enter.setText("=");
                break;
            case R.id.number_enter:
                if (isAdd == true) {
                    sum += Double.valueOf(number.getText().toString());
                } else {
                    sum -= Double.valueOf(number.getText().toString());
                }
                number.setText(sum + "");
                sum = 0;
                number_enter.setText("ok");
                arrays.clear();
                break;
            case R.id.received:
                intent = new Intent(PayableActivity.this, ReceivedActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.notepad:
                intent = new Intent(PayableActivity.this, NotepadActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.payed:
                intent = new Intent(PayableActivity.this, PayActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.receivables:
                intent = new Intent(PayableActivity.this, ReceivablesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.remark:
                intent = new Intent(PayableActivity.this, RemarksActivity.class);
                String money = number.getText().toString();
                Drawable background = action.getBackground();
                ColorDrawable colorDrawable = (ColorDrawable) background;
                at_action = action_text.getText().toString();
                int color = colorDrawable.getColor();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("title", "待付");
                intent.putExtra("atTime", atTime);
                intent.putExtra("drawable", color);
                intent.putExtra("at_action", at_action);
                //   intent.putExtra("bg_number",bg_number);
                intent.putExtra("money", money);
                startActivity(intent);
                break;
            case R.id.day:
                TimeSelector timeSelector = new TimeSelector(PayableActivity.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        atTime = time;
                        day.setText(time);
                    }

                }, atTime, "2500-12-31 23:59:59");
                timeSelector.setIsLoop(false);//设置不循环,true循环
                timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.show();
                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 实现小圆点的添加，
     * 找到线性布局动态的向线性布局内添加小圆，并添加drawable选择的效果
     */
    private void initPoints() {
        //实例化线性布局
        ll = (LinearLayout) findViewById(R.id.main_ll);
        //绘制和图片对应的圆点的数量
        for (int i = 0; i < fragmentArrayList.size(); i++) {
            View view = new View(this);
            //设置圆点的大小
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
            //设置间距
            params.setMargins(10, 10, 10, 10);
            //设置图片的自定义效果
            view.setBackgroundResource(R.drawable.start_selsect);
            //把设置好的视图属性设置到View中
            view.setLayoutParams(params);
            //把创建好的View添加到线性布局中
            ll.addView(view);
        }
        Log.e("TAG", ":" + ll.getChildCount());
        //设置选中线性布局中的第一个
        ll.getChildAt(0).setSelected(true);
    }

    /**
     * 初始化页卡内容区
     */
    private void InitViewPager() {

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(new MFragmentPagerAdapter(fragmentManager, fragmentArrayList));

        //让ViewPager缓存2个页面
        mViewPager.setOffscreenPageLimit(2);

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
        Fragment2 fragment2 = new Fragment2();
        Bundle bundle1 = new Bundle();
        bundle1.putIntArray("icon", icno);
        bundle1.putStringArray("name", name);
        fragment2.setArguments(bundle1);
        fragmentArrayList.add(fragment2);

        fragmentManager = getSupportFragmentManager();

    }

    /**
     * 页卡切换监听
     *
     * @author CZK
     * @version start1.0
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
                    // Toast.makeText(PayableActivity.this,"页面"+(position+start1), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < ll.getChildCount(); i++) {
                        ll.getChildAt(i).setSelected(false);
                    }
                    ll.getChildAt(position).setSelected(true);
                    break;
                case 1:
                    Fragment2 fragment2 = new Fragment2();
                    Bundle bundle1 = new Bundle();
                    bundle1.putIntArray("icon", icno);
                    bundle1.putStringArray("name", name);
                    fragment2.setArguments(bundle1);
                    ft.replace(android.R.id.content, fragment2);
                    //   Toast.makeText(PayableActivity.this,"页面"+(position+start1),Toast.LENGTH_SHORT).show();
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
