package com.example.administrator.ding_small;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.ding_small.HelpTool.FlowLayout;
import com.example.administrator.ding_small.HelpTool.LocationUtil;
import com.example.administrator.ding_small.HelpTool.LunarCalendar;
import com.example.administrator.ding_small.Label.EditLabelActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import static com.example.administrator.ding_small.HelpTool.getContactInfo.getContactInfo;
import static com.example.administrator.ding_small.R.id.found_activity_lay;
import static com.example.administrator.ding_small.R.id.search_btn;


/**
 * Created by Administrator on 2017/11/9.
 */

public class RemarksActivity extends Activity implements View.OnClickListener{
    private TextView contacts_text,label_text,repeat_text,location_text,photo_text,dateT,timeT,week,nong,title,reimbursement_text,loan_text,privacy_text;
    private static  final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION  = 100;
    private String str_location=null;
    private InputMethodManager inputMethodManager;
    private String[] strs=null;
    private String[] ContactsStrs=null;
    private ArrayList<String> labelList=new ArrayList<String>();
    private FlowLayout found_activity_fyt;
    private FlowLayout found_activity_lay;

    private ListView list;
    private JSONObject contactData;//储存第一手信息
    private JSONObject contactDatas;//储存搜索结果
    private JSONObject jsonObject;//为contactData提供对象
    private EditText search_text;//搜索框
    boolean isFlag=true;//用哪个JsonObject响应listVIEW点击事件
    private Button search_btn;
    private ImageView clean_text;
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
        findViewById(R.id.remarks_reimbursement).setOnClickListener(this);
        findViewById(R.id.remarks_privacy).setOnClickListener(this);
        findViewById(R.id.remarks_loan).setOnClickListener(this);

        contacts_text= findViewById(R.id.contacts_text);
        label_text=findViewById(R.id.label_text);
        repeat_text=findViewById(R.id.repeat_text);
        location_text=findViewById(R.id.location_text);
        photo_text=findViewById(R.id.photo_text);
        reimbursement_text=findViewById(R.id.reimbursement_text);
        loan_text=findViewById(R.id.loan_text);
        privacy_text=findViewById(R.id.privacy_text);

        timeT=findViewById(R.id.time);
        dateT=findViewById(R.id.date);
        week=findViewById(R.id.week);
        nong=findViewById(R.id.nong);
        title=findViewById(R.id.title);
        //获取传过来的值
        Bundle bundle=getIntent().getExtras();
        String t1=bundle.getString("title");
        if(t1.equals("已收")||t1.equals("待收")){
            findViewById(R.id.loan_layout).setVisibility(View.GONE);
            findViewById(R.id.reimbursement_layout).setVisibility(View.VISIBLE);
        }else if(t1.equals("已付")||t1.equals("待付")){
            findViewById(R.id.loan_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.reimbursement_layout).setVisibility(View.GONE);
        }else {
            findViewById(R.id.loan_layout).setVisibility(View.GONE);
            findViewById(R.id.reimbursement_layout).setVisibility(View.GONE);
        }
        title.setText(t1);
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
        //获取地址
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        } else {
            LocationUtil.initLocation(RemarksActivity.this);
            System.out.println("主经度:"+Double.toString(LocationUtil.longitude)+"主纬度："+Double.toString(LocationUtil.latitude));
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    str_location= LocationUtil.getAddress(LocationUtil.location,getApplicationContext());
                    //位置信息-----一个字符串
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @TargetApi(VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.remarks_contacts:
                findViewById(R.id.remarks_contacts).setBackgroundResource(R.drawable.c6_bg);
                findViewById(R.id.remarks_label).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_repeat).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_reimbursement).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_loan).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_privacy).setBackgroundResource(R.drawable.hui_bg);

                privacy_text.setTextColor(getResources().getColor(R.color.blank));
                loan_text.setTextColor(getResources().getColor(R.color.blank));
                reimbursement_text.setTextColor(getResources().getColor(R.color.blank));
                contacts_text.setTextColor(this.getResources().getColor(R.color.green));
                label_text.setTextColor(getResources().getColor(R.color.blank));
                repeat_text.setTextColor(getResources().getColor(R.color.blank));
                location_text.setTextColor(getResources().getColor(R.color.blank));
                photo_text.setTextColor(getResources().getColor(R.color.blank));

                findViewById(R.id.remarks_repeat_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_photo_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_location_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_label_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_contacts_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.remarks_reimbursement_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_privacy_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_loan_layout).setVisibility(View.GONE);
                ContactsStrs = new String[]{"lily/youyou", "张先生/优游", "李小龙", "郭德纲", "李维嘉", "何炅", "谢娜", "黄晓明", "张艺兴"};
                contactsFlowLayout();
                //获取手机联系人列表
                try {
                    contactData=getContactInfo(RemarksActivity.this);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                list=findViewById(R.id.contacts_list);
                search_btn=findViewById(R.id.search_btn);
                clean_text=findViewById(R.id.clean_text);
                search_text=findViewById(R.id.search_edittext);
                search_btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        findViewById(R.id.found_history_lay).setVisibility(View.GONE);
                        findViewById(R.id.contacts_list).setVisibility(View.VISIBLE);
                        isFlag=false;
                        String search_val=search_text.getText().toString();
                        String ss= null;
                        contactDatas=new JSONObject();;
                        String name="";
                        int index=0;
                        try {
                            for(int i=0;i<contactData.length();i++){
                                ss = contactData.getString("contact"+i);
                                JSONObject obj=new JSONObject(ss);
                                name=obj.getString("lastname").substring(obj.getString("lastname").length()-1,obj.getString("lastname").length()).toString();
                                if(search_val.equals(name)){
                                    System.out.println("键："+name);
                                    System.out.println("值："+search_val);
                                    contactDatas.put("contact"+index, obj);
                                    index++;
                                }else if(search_val.equals(obj.getString("mobile"))){
                                    contactDatas.put("contact"+index, obj);
                                    index++;
                                }
                            }

                            list.setAdapter(new com.example.administrator.ding_small.Adapter.ContactAdapter(RemarksActivity.this, contactDatas));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

                clean_text.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        findViewById(R.id.found_history_lay).setVisibility(View.GONE);
                        findViewById(R.id.contacts_list).setVisibility(View.VISIBLE);

                        isFlag=true;
                        search_text.setText("");
                        list.setAdapter(new com.example.administrator.ding_small.Adapter.ContactAdapter(RemarksActivity.this, contactData));
                    }
                });
                list.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String ss= null;
                        String name="";
                        try {
                            if(isFlag){
                                ss = contactData.getString("contact"+i);
                            }else {
                                ss = contactDatas.getString("contact"+i);
                            }
                            JSONObject obj=new JSONObject(ss);
                            name=obj.getString("lastname").substring(obj.getString("lastname").length()-1,obj.getString("lastname").length()).toString();
                            contacts_text.setText(name);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                break;
            case R.id.remarks_label:
                findViewById(R.id.remarks_contacts).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_label).setBackgroundResource(R.drawable.c6_bg);
                findViewById(R.id.remarks_repeat).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_reimbursement).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_loan).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_privacy).setBackgroundResource(R.drawable.hui_bg);

                privacy_text.setTextColor(getResources().getColor(R.color.blank));
                loan_text.setTextColor(getResources().getColor(R.color.blank));
                reimbursement_text.setTextColor(getResources().getColor(R.color.blank));
                contacts_text.setTextColor(this.getResources().getColor(R.color.blank));
                label_text.setTextColor(getResources().getColor(R.color.green));
                repeat_text.setTextColor(getResources().getColor(R.color.blank));
                location_text.setTextColor(getResources().getColor(R.color.blank));
                photo_text.setTextColor(getResources().getColor(R.color.blank));

                findViewById(R.id.remarks_repeat_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_photo_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_location_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_label_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.remarks_contacts_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_reimbursement_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_privacy_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_loan_layout).setVisibility(View.GONE);
                strs = new String[]{"通用", "住房", "逛街", "买菜", "奖金", "学费", "工资", "房租", "零食", "夜宵", "+"};
                labelFlowLayout();
                break;
            case R.id.remarks_repeat:
                findViewById(R.id.remarks_contacts).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_label).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_repeat).setBackgroundResource(R.drawable.c6_bg);
                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_reimbursement).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_loan).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_privacy).setBackgroundResource(R.drawable.hui_bg);

                privacy_text.setTextColor(getResources().getColor(R.color.blank));
                loan_text.setTextColor(getResources().getColor(R.color.blank));
                reimbursement_text.setTextColor(getResources().getColor(R.color.blank));
                findViewById(R.id.remarks_repeat_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.remarks_photo_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_location_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_label_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_contacts_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_reimbursement_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_privacy_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_loan_layout).setVisibility(View.GONE);

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
                findViewById(R.id.remarks_reimbursement).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_loan).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_privacy).setBackgroundResource(R.drawable.hui_bg);

                privacy_text.setTextColor(getResources().getColor(R.color.blank));
                loan_text.setTextColor(getResources().getColor(R.color.blank));
                reimbursement_text.setTextColor(getResources().getColor(R.color.blank));
                findViewById(R.id.remarks_repeat_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_photo_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.remarks_location_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_label_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_contacts_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_reimbursement_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_privacy_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_loan_layout).setVisibility(View.GONE);

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
                findViewById(R.id.remarks_reimbursement).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_loan).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_privacy).setBackgroundResource(R.drawable.hui_bg);

                privacy_text.setTextColor(getResources().getColor(R.color.blank));
                loan_text.setTextColor(getResources().getColor(R.color.blank));
                reimbursement_text.setTextColor(getResources().getColor(R.color.blank));
                contacts_text.setTextColor(this.getResources().getColor(R.color.blank));
                label_text.setTextColor(getResources().getColor(R.color.blank));
                repeat_text.setTextColor(getResources().getColor(R.color.blank));
                location_text.setTextColor(getResources().getColor(R.color.green));
                photo_text.setTextColor(getResources().getColor(R.color.blank));

                findViewById(R.id.remarks_repeat_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_photo_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_location_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.remarks_label_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_contacts_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_reimbursement_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_privacy_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_loan_layout).setVisibility(View.GONE);

                TextView location=findViewById(R.id.location);
                if(str_location ==null || str_location.isEmpty()){
                    location.setText("请打开移动网络,重试");
                }else{
                    location.setText(str_location);
                }
                break;
            case R.id.remarks_reimbursement:
                findViewById(R.id.remarks_contacts).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_label).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_repeat).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_reimbursement).setBackgroundResource(R.drawable.c6_bg);
                findViewById(R.id.remarks_loan).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_privacy).setBackgroundResource(R.drawable.hui_bg);



                findViewById(R.id.remarks_repeat_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_photo_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_location_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_label_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_contacts_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_reimbursement_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.remarks_privacy_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_loan_layout).setVisibility(View.GONE);

                privacy_text.setTextColor(getResources().getColor(R.color.blank));
                loan_text.setTextColor(getResources().getColor(R.color.blank));
                reimbursement_text.setTextColor(getResources().getColor(R.color.green));
                contacts_text.setTextColor(this.getResources().getColor(R.color.blank));
                label_text.setTextColor(getResources().getColor(R.color.blank));
                repeat_text.setTextColor(getResources().getColor(R.color.blank));
                location_text.setTextColor(getResources().getColor(R.color.blank));
                photo_text.setTextColor(getResources().getColor(R.color.blank));
                break;
            case R.id.remarks_privacy:
                findViewById(R.id.remarks_contacts).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_label).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_repeat).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_reimbursement).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_loan).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_privacy).setBackgroundResource(R.drawable.c6_bg);



                findViewById(R.id.remarks_repeat_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_photo_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_location_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_label_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_contacts_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_reimbursement_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_privacy_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.remarks_loan_layout).setVisibility(View.GONE);

                privacy_text.setTextColor(getResources().getColor(R.color.green));
                loan_text.setTextColor(getResources().getColor(R.color.blank));
                reimbursement_text.setTextColor(getResources().getColor(R.color.blank));
                contacts_text.setTextColor(this.getResources().getColor(R.color.blank));
                label_text.setTextColor(getResources().getColor(R.color.blank));
                repeat_text.setTextColor(getResources().getColor(R.color.blank));
                location_text.setTextColor(getResources().getColor(R.color.blank));
                photo_text.setTextColor(getResources().getColor(R.color.blank));
                break;
            case R.id.remarks_loan:
                findViewById(R.id.remarks_contacts).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_label).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_repeat).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_reimbursement).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_loan).setBackgroundResource(R.drawable.c6_bg);
                findViewById(R.id.remarks_privacy).setBackgroundResource(R.drawable.hui_bg);



                findViewById(R.id.remarks_repeat_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_photo_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_location_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_label_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_contacts_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_reimbursement_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_privacy_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_loan_layout).setVisibility(View.VISIBLE);

                privacy_text.setTextColor(getResources().getColor(R.color.blank));
                loan_text.setTextColor(getResources().getColor(R.color.green));
                reimbursement_text.setTextColor(getResources().getColor(R.color.blank));
                contacts_text.setTextColor(this.getResources().getColor(R.color.blank));
                label_text.setTextColor(getResources().getColor(R.color.blank));
                repeat_text.setTextColor(getResources().getColor(R.color.blank));
                location_text.setTextColor(getResources().getColor(R.color.blank));
                photo_text.setTextColor(getResources().getColor(R.color.blank));
                break;
        }
    }
    //标签布局方法
    private void labelFlowLayout() {
        if(found_activity_fyt==null){
            //加载搜索记录
            for (int i = 0; i < strs.length; i++) {
                final TextView text = new TextView(RemarksActivity.this);
                System.out.println("数组："+strs[i]);
                if(i<strs.length-1){
                    text.setText(strs[i]);//添加内容
                    text.setTextSize(12);
                    text.setTextColor(Color.rgb(102, 102, 102));
                    text.setBackgroundResource(R.drawable.light_button_back);
                    text.setPadding(15, 10, 15, 10);
                }else {
                    text.setText(strs[i]);//添加内容
                    text.setTextSize(12);
                    text.setTextColor(getResources().getColor(R.color.orange));
                    text.setBackgroundResource(R.drawable.light_button_back);
                    text.setPadding(15, 10, 15, 10);
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);//设置宽高,第一个参数是宽,第二个参数是高
                //设置边距
                params.topMargin = 5;
                params.bottomMargin = 5;
                params.leftMargin = 8;
                params.rightMargin = 8;
                text.setLayoutParams(params);
                found_activity_fyt = findViewById(R.id.found_activity_fyt);
                found_activity_fyt.addView(text);//将内容添加到布局中
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//添加点击事件
                        if(text.getText().toString().equals("+")){
                            Intent intent=new Intent(RemarksActivity.this,EditLabelActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else{
                            if(labelList.contains(text.getText().toString())){
                                labelList.remove(text.getText().toString());
                                view.setBackgroundResource(R.drawable.light_button_back);
                                text.setTextColor(getResources().getColor(R.color.blank));
                            }else {
                                view.setBackgroundResource(R.drawable.green_button_back);
                                text.setTextColor(getResources().getColor(R.color.white));
                                labelList.add(text.getText().toString());
                            }
                        }

                        Toast.makeText(RemarksActivity.this, text.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }

    //标签布局方法
    private void contactsFlowLayout() {
        if(found_activity_lay==null){
            //加载搜索记录
            for (int i = 0; i < ContactsStrs.length; i++) {
                final TextView text = new TextView(RemarksActivity.this);
                System.out.println("数组："+ContactsStrs[i]);
                text.setText(ContactsStrs[i]);//添加内容
                text.setTextSize(12);
                text.setTextColor(getResources().getColor(R.color.green));
                text.setBackgroundResource(R.drawable.light_button_back);
                text.setPadding(15, 10, 15, 10);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);//设置宽高,第一个参数是宽,第二个参数是高
                //设置边距
                params.topMargin = 5;
                params.bottomMargin = 5;
                params.leftMargin = 8;
                params.rightMargin = 8;
                text.setLayoutParams(params);
                found_activity_lay = findViewById(R.id.found_activity_lay);
                found_activity_lay.addView(text);//将内容添加到布局中
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//添加点击事件
                        search_text.setText(text.getText().toString());
                        Toast.makeText(RemarksActivity.this, text.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }


    }
}
