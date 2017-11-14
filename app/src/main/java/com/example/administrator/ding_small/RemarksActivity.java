package com.example.administrator.ding_small;

import android.app.Activity;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.format.Time;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.ding_small.HelpTool.LunarCalendar;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Administrator on 2017/11/9.
 */

public class RemarksActivity extends Activity implements View.OnClickListener{
    private TextView contacts_text,label_text,repeat_text,location_text,photo_text,dateT,timeT,week,nong;
    @RequiresApi(api = VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remarks);
        findViewById(R.id.remarks_contacts).setOnClickListener(this);
        findViewById(R.id.remarks_label).setOnClickListener(this);
        findViewById(R.id.remarks_repeat).setOnClickListener(this);
        findViewById(R.id.remarks_location).setOnClickListener(this);
        findViewById(R.id.remarks_photo).setOnClickListener(this);
        findViewById(R.id.remarks_bind).setOnClickListener(this);
        contacts_text= findViewById(R.id.contacts_text);
        label_text=findViewById(R.id.label_text);
        repeat_text=findViewById(R.id.repeat_text);
        location_text=findViewById(R.id.location_text);
        photo_text=findViewById(R.id.photo_text);
        timeT=findViewById(R.id.time);
        dateT=findViewById(R.id.date);
        week=findViewById(R.id.week);
        nong=findViewById(R.id.nong);
       //获取当前年月日时分
        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month;
        int date = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        //判断周几
        if("1".equals(mWay)){
            mWay ="周日";
        }else if("2".equals(mWay)){
            mWay ="周一";
        }else if("3".equals(mWay)){
            mWay ="周二";
        }else if("4".equals(mWay)){
            mWay ="周三";
        }else if("5".equals(mWay)){
            mWay ="周四";
        }else if("6".equals(mWay)){
            mWay ="周五";
        }else if("7".equals(mWay)){
            mWay ="周六";
        }
        //给时分赋值
        if(minute<10){
            String minute_text="0"+minute;
            timeT.setText(hour+":"+minute_text);
        }else{
            timeT.setText(hour+":"+minute);
        }
        //给星期赋值
        week.setText(mWay);
        //给年月日赋值
        dateT.setText(year+"-"+(month+1)+"-"+date);
        //给农历赋值
        System.out.println("农历："+ new LunarCalendar().getChinaDayString(new LunarCalendar().getLunarDateINT(year,month+1,date)));
        nong.setText(new LunarCalendar().getChinaDayString(new LunarCalendar().getLunarDateINT(year,month+1,date)));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.remarks_contacts:
                findViewById(R.id.remarks_contacts).setBackgroundResource(R.drawable.c6_bg);
                findViewById(R.id.remarks_label).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_repeat).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.hui_bg);

                contacts_text.setTextColor(this.getResources().getColor(R.color.green));
                label_text.setTextColor(getResources().getColor(R.color.blank));
                repeat_text.setTextColor(getResources().getColor(R.color.blank));
                location_text.setTextColor(getResources().getColor(R.color.blank));
                photo_text.setTextColor(getResources().getColor(R.color.blank));
                break;
            case R.id.remarks_label:
                findViewById(R.id.remarks_contacts).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_label).setBackgroundResource(R.drawable.c6_bg);
                findViewById(R.id.remarks_repeat).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.hui_bg);

                contacts_text.setTextColor(this.getResources().getColor(R.color.blank));
                label_text.setTextColor(getResources().getColor(R.color.green));
                repeat_text.setTextColor(getResources().getColor(R.color.blank));
                location_text.setTextColor(getResources().getColor(R.color.blank));
                photo_text.setTextColor(getResources().getColor(R.color.blank));
                break;
            case R.id.remarks_repeat:
                findViewById(R.id.remarks_contacts).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_label).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_repeat).setBackgroundResource(R.drawable.c6_bg);
                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_repeat_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.remarks_photo_layout).setVisibility(View.GONE);

                contacts_text.setTextColor(this.getResources().getColor(R.color.blank));
                label_text.setTextColor(getResources().getColor(R.color.blank));
                repeat_text.setTextColor(getResources().getColor(R.color.green));
                location_text.setTextColor(getResources().getColor(R.color.blank));
                photo_text.setTextColor(getResources().getColor(R.color.blank));
                break;
            case R.id.remarks_photo:
                findViewById(R.id.remarks_contacts).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_label).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_repeat).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.c6_bg);
                findViewById(R.id.remarks_repeat_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_photo_layout).setVisibility(View.VISIBLE);

                contacts_text.setTextColor(this.getResources().getColor(R.color.blank));
                label_text.setTextColor(getResources().getColor(R.color.blank));
                repeat_text.setTextColor(getResources().getColor(R.color.blank));
                location_text.setTextColor(getResources().getColor(R.color.blank));
                photo_text.setTextColor(getResources().getColor(R.color.green));
                break;
            case R.id.remarks_location:
                findViewById(R.id.remarks_contacts).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_label).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_repeat).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.c6_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.hui_bg);

                contacts_text.setTextColor(this.getResources().getColor(R.color.blank));
                label_text.setTextColor(getResources().getColor(R.color.blank));
                repeat_text.setTextColor(getResources().getColor(R.color.blank));
                location_text.setTextColor(getResources().getColor(R.color.green));
                photo_text.setTextColor(getResources().getColor(R.color.blank));
                break;

        }
    }
}
