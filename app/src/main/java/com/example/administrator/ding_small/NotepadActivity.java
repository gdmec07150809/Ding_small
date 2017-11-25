package com.example.administrator.ding_small;

/**
 * Created by Administrator on 2017/10/20.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.Title.TitleActivity;

import org.feezu.liuli.timeselector.TimeSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.administrator.ding_small.R.id.color;
import static com.example.administrator.ding_small.R.id.ge;


public class NotepadActivity  extends Activity implements View.OnClickListener{
    LinearLayout ll,two;
    Intent intent;
    RelativeLayout action;
    private TextView day,time,action_text;
    private String atTime,at_action,bg_number;
    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;
    //图标
    int icno[] = { R.drawable.c1_bg, R.drawable.c2_bg, R.drawable.c3_bg, R.drawable.c4_bg, R.drawable.c5_bg, R.drawable.c6_bg,
            R.drawable.c7_bg,R.drawable.c8_bg, R.drawable.c9_bg, R.drawable.c10_bg, R.drawable.c11_bg, R.drawable.c12_bg ,
            R.drawable.c13_bg,R.drawable.c14_bg, R.drawable.c1_bg, R.drawable.c2_bg, R.drawable.c3_bg, R.drawable.c4_bg,
            R.drawable.c5_bg, R.drawable.c6_bg,R.drawable.c7_bg,R.drawable.c8_bg, R.drawable.c9_bg, R.drawable.edit_add};
    //图标下的文字
    String name[]={"待办事项","常用数据","一般数据","生日","身份证","银行资料",
            "待办事项", "常用数据","一般数据","生日","身份证","银行资料",
            "待办事项", "常用数据","一般数据","生日","身份证","杂项",
            "待办事项", "常用数据","一般数据","生日","身份证","编辑"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepad);
        ll=findViewById(R.id.main_ll);
        two = this.findViewById(R.id.two);
        findViewById(R.id.received).setOnClickListener(this);
        findViewById(R.id.payable).setOnClickListener(this);
        //findViewById(R.id.click_btn).setOnClickListener(this);
        findViewById(R.id.remarks).setOnClickListener(this);
        initPoints();
        day=findViewById(R.id.day);
        day.setOnClickListener(this);
        action_text=findViewById(R.id.action_text);
        action=findViewById(R.id.action);
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
            atTime=year+"-"+(month+1)+"-"+date+"  "+hour+":"+minute_text;
            day.setText(atTime);
        }else{
            atTime=year+"-"+(month+1)+"-"+date+"  "+hour+":"+minute;
            day.setText(atTime);
        }
        gridView = (GridView) findViewById(R.id.gridview);
        //初始化数据
        initData();

        String[] from={"img","text"};

        int[] to={R.id.img,R.id.text};

        adapter=new SimpleAdapter(this, dataList, R.layout.gride_view_item, from, to);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {
            @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
//                AlertDialog.Builder builder= new AlertDialog.Builder(NotepadActivity.this);
               System.out.println(dataList.get(arg2));
//                arg1.setDrawingCacheEnabled(true);
//                System.out.println("路径:"+ arg1.getDrawingCache());
//                arg1.setDrawingCacheEnabled(false);
//                builder.setTitle("提示").setMessage(dataList.get(arg2).get("text").toStrin)).create().show();
                Resources drawable=arg1.getResources();
                System.out.println("路径："+drawable);
                if(dataList.get(arg2).get("text").toString().equals("编辑")){
                    intent=new Intent(NotepadActivity.this,TitleActivity.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }else{
                    at_action=dataList.get(arg2).get("text").toString();
                    action_text.setText(at_action);
                switch (arg2){
                    case 0:
                        action.setBackgroundColor(getResources().getColor(R.color.bg1));
                        break;
                    case 1:
                        action.setBackgroundColor(getResources().getColor(R.color.bg2));

                        break;
                    case 2:
                        action.setBackgroundColor(getResources().getColor(R.color.bg3));
                        break;
                    case 3:
                        action.setBackgroundColor(getResources().getColor(R.color.bg4));

                        break;
                    case 4:
                        action.setBackgroundColor(getResources().getColor(R.color.bg5));

                        break;
                    case 5:
                        action.setBackgroundColor(getResources().getColor(R.color.bg6));

                        break;
                    case 6:
                        action.setBackgroundColor(getResources().getColor(R.color.bg7));

                        break;
                    case 7:
                        action.setBackgroundColor(getResources().getColor(R.color.bg8));

                        break;
                    case 8:
                        action.setBackgroundColor(getResources().getColor(R.color.bg9));
                        break;
                    case 9:
                        action.setBackgroundColor(getResources().getColor(R.color.bg10));
                        break;
                    case 10:
                        action.setBackgroundColor(getResources().getColor(R.color.bg11));
                        break;
                    case 11:
                        action.setBackgroundColor(getResources().getColor(R.color.bg12));
                        break;
                    case 12:
                        action.setBackgroundColor(getResources().getColor(R.color.bg13));
                        break;
                    case 13:
                        action.setBackgroundColor(getResources().getColor(R.color.bg14));
                        break;
                    case 14:
                        action.setBackgroundColor(getResources().getColor(R.color.bg1));
                        break;
                    case 15:
                        action.setBackgroundColor(getResources().getColor(R.color.bg2));
                        break;
                    case 16:
                        action.setBackgroundColor(getResources().getColor(R.color.bg3));
                        break;
                    case 17:
                        action.setBackgroundColor(getResources().getColor(R.color.bg4));
                        break;
                    case 18:
                        action.setBackgroundColor(getResources().getColor(R.color.bg5));
                        break;
                    case 19:
                        action.setBackgroundColor(getResources().getColor(R.color.bg6));
                        break;
                    case 20:
                        action.setBackgroundColor(getResources().getColor(R.color.bg7));
                        break;
                    case 21:
                        action.setBackgroundColor(getResources().getColor(R.color.bg8));
                        break;
                    case 22:
                        action.setBackgroundColor(getResources().getColor(R.color.bg9));
                        break;

                }
                bg_number=(arg2)+"";
                }
            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.received:
                intent=new Intent(NotepadActivity.this,ReceivedActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case R.id.payable:
                intent=new Intent(NotepadActivity.this,PayableActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case R.id.remarks:
                intent=new Intent(NotepadActivity.this,RemarksActivity.class);
                intent.putExtra("title","记事");
                intent.putExtra("atTime",atTime);
                intent.putExtra("at_action",at_action);
                intent.putExtra("bg_number",bg_number);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case R.id.day:
                TimeSelector timeSelector = new TimeSelector(NotepadActivity.this, new TimeSelector.ResultHandler() {
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
        for (int i = 0; i < 4; i++) {
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

    void initData() {
        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i <icno.length; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("img", icno[i]);
            map.put("text",name[i]);
            dataList.add(map);
        }
    }
}
