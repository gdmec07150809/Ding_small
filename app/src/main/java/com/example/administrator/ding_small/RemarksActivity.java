package com.example.administrator.ding_small;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
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

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


import static com.example.administrator.ding_small.HelpTool.getContactInfo.getContactInfo;
import static com.example.administrator.ding_small.R.id.date;
import static com.example.administrator.ding_small.R.id.money;


/**
 * Created by Administrator on 2017/11/9.
 */

public class RemarksActivity extends Activity implements View.OnClickListener{
    private TextView contacts_text,label_text,repeat_text,location_text,photo_text,dateT,week,title,reimbursement_text,loan_text,privacy_text,at_action;
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
    private EditText search_text,remarks_text;//搜索框
    boolean isFlag=true;//用哪个JsonObject响应listVIEW点击事件
    private Button search_btn;
    private ImageView clean_text;
    private Calendar cal = Calendar.getInstance();
    boolean isInfinite=false;//是否无限次重复
    private String atTime,at_action_text,money;
    private  ImageView photo1,photo2,photo3,photo4;
    private Dialog mCameraDialog;
    InputMethodManager imm;
    private LinearLayout action;

    @RequiresApi(api = VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setContentView(R.layout.remarks);
        findViewById(R.id.remarks_contacts).setOnClickListener(this);
        findViewById(R.id.remarks_label).setOnClickListener(this);
        findViewById(R.id.remarks_repeat).setOnClickListener(this);
        findViewById(R.id.remarks_location).setOnClickListener(this);
        findViewById(R.id.remarks_photo).setOnClickListener(this);
        findViewById(R.id.remarks_reimbursement).setOnClickListener(this);
        findViewById(R.id.remarks_privacy).setOnClickListener(this);
        findViewById(R.id.remarks_loan).setOnClickListener(this);
        findViewById(R.id.infinite).setOnClickListener(this);

        contacts_text= findViewById(R.id.contacts_text);
        label_text=findViewById(R.id.label_text);
        repeat_text=findViewById(R.id.repeat_text);
        location_text=findViewById(R.id.location_text);
        photo_text=findViewById(R.id.photo_text);
        reimbursement_text=findViewById(R.id.reimbursement_text);
        loan_text=findViewById(R.id.loan_text);
        privacy_text=findViewById(R.id.privacy_text);

        dateT=findViewById(R.id.date);
        dateT.setOnClickListener(this);
        week=findViewById(R.id.week);
        title=findViewById(R.id.title);
        remarks_text=findViewById(R.id.remarks_text);
        at_action=findViewById(R.id.at_action);
        action=findViewById(R.id.action);
        //获取传过来的值
        Bundle bundle=getIntent().getExtras();
        String t1=bundle.getString("title");
        int  bg_number= Integer.parseInt(bundle.getString("bg_number"));
        setBackgroundRemarks(bg_number);//设置标题背景
        atTime=bundle.getString("atTime");
        at_action_text=bundle.getString("at_action");
        week.setText("周"+getWeek(atTime));
        at_action.setText(at_action_text);
//        if(t1.equals("已收")||t1.equals("待收")){
//            findViewById(R.id.loan_layout).setVisibility(View.GONE);
//            findViewById(R.id.reimbursement_layout).setVisibility(View.VISIBLE);
//        }else if(t1.equals("已付")||t1.equals("待付")){
//            findViewById(R.id.loan_layout).setVisibility(View.VISIBLE);
//            findViewById(R.id.reimbursement_layout).setVisibility(View.GONE);
//        }else {
//            findViewById(R.id.loan_layout).setVisibility(View.GONE);
//            findViewById(R.id.reimbursement_layout).setVisibility(View.GONE);
//        }


        //记事时报销、借出功能不显示
        if(t1.equals("记事")){
            findViewById(R.id.loan_layout).setVisibility(View.GONE);
            findViewById(R.id.reimbursement_layout).setVisibility(View.GONE);
            findViewById(R.id.money).setVisibility(View.INVISIBLE);
        }else{
            money=bundle.getString("money");
            TextView number=findViewById(R.id.money);
            number.setText(money);
        }
        title.setText(t1);
        dateT.setText(atTime);
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
        Intent intent;
        switch (view.getId()){
            case R.id.remarks_contacts:
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                //设置对应按钮背景样式
                findViewById(R.id.remarks_contacts).setBackgroundResource(R.drawable.c6_bg);
                findViewById(R.id.remarks_label).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_repeat).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_reimbursement).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_loan).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_privacy).setBackgroundResource(R.drawable.hui_bg);

                //设置对应文本颜色
                privacy_text.setTextColor(getResources().getColor(R.color.blank));
                loan_text.setTextColor(getResources().getColor(R.color.blank));
                reimbursement_text.setTextColor(getResources().getColor(R.color.blank));
                contacts_text.setTextColor(this.getResources().getColor(R.color.green));
                label_text.setTextColor(getResources().getColor(R.color.blank));
                repeat_text.setTextColor(getResources().getColor(R.color.blank));
                location_text.setTextColor(getResources().getColor(R.color.blank));
                photo_text.setTextColor(getResources().getColor(R.color.blank));

                //设置对应布局显隐
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
                //搜索联系人事件
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
                //清除搜索框内容
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
                //联系人列表子事件
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

                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

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
                //绘制自定义流式布局
                labelFlowLayout();
                break;
            case R.id.remarks_repeat:
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

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

                final TextView infinite= findViewById(R.id.infinite);
                final LinearLayout year= findViewById(R.id.year);
                final LinearLayout month= findViewById(R.id.month);
                final LinearLayout week= findViewById(R.id.week_layout);
                final LinearLayout day= findViewById(R.id.day);

                infinite.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isInfinite){
                            TextView infinite1= findViewById(R.id.infinite);
                            infinite1.setTextColor(getResources().getColor(R.color.blank));
                            EditText edit_repeat_number_text=findViewById(R.id.edit_repeat_number_text);
                            edit_repeat_number_text.setEnabled(true);
                            isInfinite=!isInfinite;
                        }else{
                            TextView infinite1= findViewById(R.id.infinite);
                            infinite1.setTextColor(getResources().getColor(R.color.orange));
                            isInfinite=!isInfinite;
                            EditText edit_repeat_number_text=findViewById(R.id.edit_repeat_number_text);
                            edit_repeat_number_text.setEnabled(false);
                        }

                    }
                });
                year.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final TextView year_text= findViewById(R.id.year_text);
                        final TextView month_text= findViewById(R.id.month_text);
                        final TextView week_text= findViewById(R.id.week_text);
                        final TextView day_text= findViewById(R.id.day_text);
                        year_text.setTextColor(getResources().getColor(R.color.orange));
                        month_text.setTextColor(getResources().getColor(R.color.blank));
                        week_text.setTextColor(getResources().getColor(R.color.blank));
                        day_text.setTextColor(getResources().getColor(R.color.blank));
                    }
                });
                week.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final TextView year_text= findViewById(R.id.year_text);
                        final TextView month_text= findViewById(R.id.month_text);
                        final TextView week_text= findViewById(R.id.week_text);
                        final TextView day_text= findViewById(R.id.day_text);
                        year_text.setTextColor(getResources().getColor(R.color.blank));
                        month_text.setTextColor(getResources().getColor(R.color.blank));
                        week_text.setTextColor(getResources().getColor(R.color.orange));
                        day_text.setTextColor(getResources().getColor(R.color.blank));
                    }
                });
                month.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final TextView year_text= findViewById(R.id.year_text);
                        final TextView month_text= findViewById(R.id.month_text);
                        final TextView week_text= findViewById(R.id.week_text);
                        final TextView day_text= findViewById(R.id.day_text);
                        year_text.setTextColor(getResources().getColor(R.color.blank));
                        month_text.setTextColor(getResources().getColor(R.color.orange));
                        week_text.setTextColor(getResources().getColor(R.color.blank));
                        day_text.setTextColor(getResources().getColor(R.color.blank));
                    }
                });
                day.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final TextView year_text= findViewById(R.id.year_text);
                        final TextView month_text= findViewById(R.id.month_text);
                        final TextView week_text= findViewById(R.id.week_text);
                        final TextView day_text= findViewById(R.id.day_text);
                        year_text.setTextColor(getResources().getColor(R.color.blank));
                        month_text.setTextColor(getResources().getColor(R.color.blank));
                        week_text.setTextColor(getResources().getColor(R.color.blank));
                        day_text.setTextColor(getResources().getColor(R.color.orange));
                    }
                });
                break;
            case R.id.remarks_photo:
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

                findViewById(R.id.remarks_contacts).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_label).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_repeat).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.c6_bg);
                findViewById(R.id.remarks_reimbursement).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_loan).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_privacy).setBackgroundResource(R.drawable.hui_bg);

                findViewById(R.id.remarks_repeat_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_photo_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.remarks_location_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_label_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_contacts_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_reimbursement_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_privacy_layout).setVisibility(View.GONE);
                findViewById(R.id.remarks_loan_layout).setVisibility(View.GONE);

                privacy_text.setTextColor(getResources().getColor(R.color.blank));
                loan_text.setTextColor(getResources().getColor(R.color.blank));
                reimbursement_text.setTextColor(getResources().getColor(R.color.blank));
                contacts_text.setTextColor(this.getResources().getColor(R.color.blank));
                label_text.setTextColor(getResources().getColor(R.color.blank));
                repeat_text.setTextColor(getResources().getColor(R.color.blank));
                location_text.setTextColor(getResources().getColor(R.color.blank));
                photo_text.setTextColor(getResources().getColor(R.color.green));

                photo1=findViewById(R.id.photo1);
                photo2=findViewById(R.id.photo2);
                photo3=findViewById(R.id.photo3);
                photo4=findViewById(R.id.photo4);
                photo1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDialog(1);
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(intent, 0);
                    }
                });
                photo2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDialog(2);
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(intent, 0);
                    }
                });
                photo3.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDialog(3);
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(intent, 0);
                    }
                });
                photo4.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDialog(4);
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(intent, 0);
                    }
                });
                photo1.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        showDetelePhotoDialog(1);
                        return true;
                    }
                });
                photo2.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        showDetelePhotoDialog(2);
                        return true;
                    }
                });
                photo3.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        showDetelePhotoDialog(3);
                        return true;
                    }
                });
                photo4.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        showDetelePhotoDialog(4);
                        return true;
                    }
                });
                break;
            case R.id.remarks_location:
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

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

                //获取地址
                if(str_location ==null || str_location.isEmpty()){
                    location.setText("请打开移动网络,重试");
                }else{
                    location.setText(str_location);
                }
                break;
            case R.id.remarks_reimbursement:
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

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
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

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
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

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
            case R.id.date:
                TimeSelector timeSelector = new TimeSelector(RemarksActivity.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        TextView week=findViewById(R.id.week);
                        atTime=time;
                        dateT.setText(time);
                        week.setText("周"+getWeek(atTime));
                    }

                }, atTime, "2500-12-31 23:59:59");
                timeSelector.setIsLoop(false);//设置不循环,true循环
                timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.show();
                break;

            case R.id.btn_open_camera:
                     intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 11);
                break;
            case R.id.btn_open_camera2:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,12);
                break;
            case R.id.btn_open_camera3:
               intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 13);
                break;
            case R.id.btn_open_camera4:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 14);
                break;
            case R.id.btn_cancel:
                mCameraDialog.dismiss();
                break;
            case R.id.btn_choose_img:
               intent= new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 21);
                break;
            case R.id.btn_choose_img2:
                intent= new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 22);
                break;
            case R.id.btn_choose_img3:
                intent= new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,23 );
                break;
            case R.id.btn_choose_img4:
                intent= new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 24);
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
//    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
//        editText.setFocusable(true);
//        editText.setFocusableInTouchMode(true);
//        editText.requestFocus();
//        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//    }
    //日期选择器监听
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        }
    };

//弹出日期选择器
    public void show(View v){
        new DatePickerDialog(RemarksActivity.this,listener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    //更新日期
    private void updateDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        TextView date=findViewById(R.id.date_text);
        date.setText(simpleDateFormat.format(cal.getTime()));
    }


    /**
     * 判断当前日期是星期几
     *
     * @param  pTime     设置的需要判断的时间  //格式如2012-09-08
     *
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    private String getWeek(String pTime) {

        String Week = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            Week += "日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            Week += "六";
        }

        return Week;
    }
    //获取,处理拍照事件
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        mCameraDialog.dismiss();
        findViewById(R.id.remarks_photo_layout).setVisibility(View.VISIBLE);
        //判断那个相机回调
        switch (requestCode){
            case 11:
                if (resultCode == Activity.RESULT_OK) {
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                        Log.i("TestFile",
                                "SD card is not avaiable/writeable right now.");
                        Toast.makeText(RemarksActivity.this,"sd卡不可用！！！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new DateFormat();
                    String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".png";
                    System.out.println("路径："+name);
                    Toast.makeText(this, name, Toast.LENGTH_LONG).show();
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

                    FileOutputStream b = null;
                    File file = new File("/sdcard/Image/");
                    file.mkdirs();// 创建文件夹
                    String fileName = "/sdcard/Image/"+name;

                    try {
                        b = new FileOutputStream(fileName);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if(b!=null){
                                b.flush();
                                b.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try
                    {
                        photo1.setImageBitmap(bitmap);// 将图片显示在ImageView里
                    }catch(Exception e)
                    {
                        Log.e("error", e.getMessage());
                    }

                }
                break;
            case 12:
                if (resultCode == Activity.RESULT_OK) {
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                        Log.i("TestFile",
                                "SD card is not avaiable/writeable right now.");
                        Toast.makeText(RemarksActivity.this,"sd卡不可用！！！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new DateFormat();
                    String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".png";
                    System.out.println("路径："+name);
                    Toast.makeText(this, name, Toast.LENGTH_LONG).show();
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

                    FileOutputStream b = null;
                    File file = new File("/sdcard/Image/");
                    file.mkdirs();// 创建文件夹
                    String fileName = "/sdcard/Image/"+name;

                    try {
                        b = new FileOutputStream(fileName);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if(b!=null){
                                b.flush();
                                b.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try
                    {
                        photo2.setImageBitmap(bitmap);// 将图片显示在ImageView里
                    }catch(Exception e)
                    {
                        Log.e("error", e.getMessage());
                    }

                }
                break;
            case 13:
                if (resultCode == Activity.RESULT_OK) {
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                        Log.i("TestFile",
                                "SD card is not avaiable/writeable right now.");
                        Toast.makeText(RemarksActivity.this,"sd卡不可用！！！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new DateFormat();
                    String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".png";
                    System.out.println("路径："+name);
                    Toast.makeText(this, name, Toast.LENGTH_LONG).show();
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

                    FileOutputStream b = null;
                    File file = new File("/sdcard/Image/");
                    file.mkdirs();// 创建文件夹
                    String fileName = "/sdcard/Image/"+name;

                    try {
                        b = new FileOutputStream(fileName);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if(b!=null){
                                b.flush();
                                b.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try
                    {
                        photo3.setImageBitmap(bitmap);// 将图片显示在ImageView里
                    }catch(Exception e)
                    {
                        Log.e("error", e.getMessage());
                    }

                }
                break;
            case 14:
                if (resultCode == Activity.RESULT_OK) {
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                        Log.i("TestFile",
                                "SD card is not avaiable/writeable right now.");
                        Toast.makeText(RemarksActivity.this,"sd卡不可用！！！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new DateFormat();
                    String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".png";
                    System.out.println("路径："+name);
                    Toast.makeText(this, name, Toast.LENGTH_LONG).show();
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

                    FileOutputStream b = null;
                    File file = new File("/sdcard/Image/");
                    file.mkdirs();// 创建文件夹
                    String fileName = "/sdcard/Image/"+name;

                    try {
                        b = new FileOutputStream(fileName);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if(b!=null){
                                b.flush();
                                b.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try
                    {
                        photo4.setImageBitmap(bitmap);// 将图片显示在ImageView里
                    }catch(Exception e)
                    {
                        Log.e("error", e.getMessage());
                    }

                }
                break;
            case 21:
                //打开相册并选择照片，这个方式选择单张
                // 获取返回的数据，这里是android自定义的Uri地址
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    // 获取选择照片的数据视图
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    // 从数据视图中获取已选择图片的路径
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    // 将图片显示到界面上
                    photo1.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }
                break;
            case 22:
                //打开相册并选择照片，这个方式选择单张
                // 获取返回的数据，这里是android自定义的Uri地址
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    // 获取选择照片的数据视图
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    // 从数据视图中获取已选择图片的路径
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    // 将图片显示到界面上
                    photo2.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }
                break;
            case 23:
                //打开相册并选择照片，这个方式选择单张
                // 获取返回的数据，这里是android自定义的Uri地址
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    // 获取选择照片的数据视图
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    // 从数据视图中获取已选择图片的路径
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    // 将图片显示到界面上
                    photo3.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }
                break;
            case 24:
                //打开相册并选择照片，这个方式选择单张
                // 获取返回的数据，这里是android自定义的Uri地址
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    // 获取选择照片的数据视图
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    // 从数据视图中获取已选择图片的路径
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    // 将图片显示到界面上
                    photo4.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }
                break;
        }

    }

    //删除图片事件
    private void showDetelePhotoDialog(final int number){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(RemarksActivity.this);
        normalDialog.setTitle("删除");
        normalDialog.setMessage("是否删除该图片？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //判断那个图片删除
                      switch (number){
                          case 1:
                              photo1.setImageResource(R.drawable.no_photo);
                              break;
                          case 2:
                              photo2.setImageResource(R.drawable.no_photo);
                              break;
                          case 3:
                              photo3.setImageResource(R.drawable.no_photo);
                              break;
                          case 4:
                              photo4.setImageResource(R.drawable.no_photo);
                              break;
                      }

                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     return;
                    }
                });
        // 显示
        normalDialog.show();
    }

    //底部弹出菜单
    private void setDialog(int number) {
        LinearLayout root=null;
        mCameraDialog = new Dialog(this, R.style.BottomDialog);
        //判断那个相机弹出
        switch (number){
            case 1:
                root = (LinearLayout) LayoutInflater.from(this).inflate(
                        R.layout.photo_menu, null);
                //初始化视图
                root.findViewById(R.id.btn_choose_img).setOnClickListener(this);
                root.findViewById(R.id.btn_open_camera).setOnClickListener(this);
                root.findViewById(R.id.btn_cancel).setOnClickListener(this);
                break;
            case 2:
                root = (LinearLayout) LayoutInflater.from(this).inflate(
                        R.layout.photo2_menu, null);
                //初始化视图
                root.findViewById(R.id.btn_choose_img2).setOnClickListener(this);
                root.findViewById(R.id.btn_open_camera2).setOnClickListener(this);
                root.findViewById(R.id.btn_cancel).setOnClickListener(this);
                break;
            case 3:
                root = (LinearLayout) LayoutInflater.from(this).inflate(
                        R.layout.photo3_menu, null);
                //初始化视图
                root.findViewById(R.id.btn_choose_img3).setOnClickListener(this);
                root.findViewById(R.id.btn_open_camera3).setOnClickListener(this);
                root.findViewById(R.id.btn_cancel).setOnClickListener(this);
                break;
            case 4:
                root = (LinearLayout) LayoutInflater.from(this).inflate(
                        R.layout.photo4_menu, null);
                //初始化视图
                root.findViewById(R.id.btn_choose_img4).setOnClickListener(this);
                root.findViewById(R.id.btn_open_camera4).setOnClickListener(this);
                root.findViewById(R.id.btn_cancel).setOnClickListener(this);
                break;
        }

        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
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
    private  void setBackgroundRemarks(int number){
        switch (number){
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
    }
}
