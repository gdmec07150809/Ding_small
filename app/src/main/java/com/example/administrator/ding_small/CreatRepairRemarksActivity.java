package com.example.administrator.ding_small;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.feezu.liuli.timeselector.TimeSelector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import static com.example.administrator.ding_small.R.id.contacts_text;
import static com.example.administrator.ding_small.R.id.label_text;
import static com.example.administrator.ding_small.R.id.loan_text;
import static com.example.administrator.ding_small.R.id.location_text;
import static com.example.administrator.ding_small.R.id.photo1;
import static com.example.administrator.ding_small.R.id.photo2;
import static com.example.administrator.ding_small.R.id.photo3;
import static com.example.administrator.ding_small.R.id.photo4;
import static com.example.administrator.ding_small.R.id.photo_text;
import static com.example.administrator.ding_small.R.id.privacy_text;
import static com.example.administrator.ding_small.R.id.reimbursement_text;
import static com.example.administrator.ding_small.R.id.repeat_text;

/**
 * Created by Administrator on 2017/12/20.
 */

public class CreatRepairRemarksActivity extends Activity implements View.OnClickListener{
    private TextView dateT,location_text,photo_text,reimbursement_text,parameter_text,management_text,at_action;
    private ImageView photo1,photo2,photo3,photo4;
    private String atTime,title;
    InputMethodManager imm;
    private Dialog mCameraDialog;
    private int color;
    private RelativeLayout title_layout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_remarks);
        init();//初始化控件
        getStringValue();//获取前页传来的数据

    }
    private void getStringValue(){
        Bundle bundle = this.getIntent().getExtras();
        atTime=bundle.getString("date");
        title=bundle.getString("at_action");
        color=bundle.getInt("drawable");
        dateT.setText(atTime);
        title_layout.setBackgroundColor(color);
        at_action.setText(title);
    }
    private void init(){
        dateT=findViewById(R.id.date);
        dateT.setOnClickListener(this);
        findViewById(R.id.remarks_photo).setOnClickListener(this);
        findViewById(R.id.remarks_location).setOnClickListener(this);
        findViewById(R.id.remarks_reimbursement).setOnClickListener(this);
        findViewById(R.id.remarks_parameter).setOnClickListener(this);
        findViewById(R.id.remarks_management).setOnClickListener(this);
        findViewById(R.id.confrim_btn).setOnClickListener(this);
        location_text=findViewById(R.id.location_text);
        photo_text=findViewById(R.id.photo_text);
        reimbursement_text=findViewById(R.id.reimbursement_text);
        parameter_text=findViewById(R.id.parameter_text);
        management_text=findViewById(R.id.management_text);
        title_layout=findViewById(R.id.action);
        at_action=findViewById(R.id.at_action);
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.date://选择时间事件
                TimeSelector timeSelector = new TimeSelector(CreatRepairRemarksActivity.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        atTime=time;
                        dateT.setText(time);
                    }

                }, atTime, "2500-12-31 23:59:59");
                timeSelector.setIsLoop(false);//设置不循环,true循环
                timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.show();
                break;
            case R.id.remarks_photo://备注图片事件
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.c6_bg);
                findViewById(R.id.remarks_reimbursement).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_parameter).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_management).setBackgroundResource(R.drawable.hui_bg);

                findViewById(R.id.remarks_photo_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.repair_location_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_user_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_parameter_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_management_layout).setVisibility(View.GONE);

                location_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                photo_text.setTextColor(ContextCompat.getColor(this,R.color.green));
                reimbursement_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                parameter_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                management_text.setTextColor(ContextCompat.getColor(this,R.color.blank));

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
            case R.id.btn_open_camera://打开相机1
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 11);
                break;
            case R.id.btn_open_camera2://打开相机2
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,12);
                break;
            case R.id.btn_open_camera3://打开相机3
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 13);
                break;
            case R.id.btn_open_camera4://打开相机4
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 14);
                break;
            case R.id.btn_cancel://上弹菜单取消事件
                mCameraDialog.dismiss();
                break;
            case R.id.btn_choose_img://选择相册1
                intent= new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 21);
                break;
            case R.id.btn_choose_img2://选择相册2
                intent= new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 22);
                break;
            case R.id.btn_choose_img3://选择相册3
                intent= new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,23 );
                break;
            case R.id.btn_choose_img4://选择相册4
                intent= new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 24);
                break;
            case R.id.remarks_location://备注地址事件
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.c6_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_reimbursement).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_parameter).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_management).setBackgroundResource(R.drawable.hui_bg);

                findViewById(R.id.remarks_photo_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_location_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.repair_user_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_parameter_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_management_layout).setVisibility(View.GONE);

                location_text.setTextColor(ContextCompat.getColor(this,R.color.green));
                photo_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                reimbursement_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                parameter_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                management_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                break;
            case R.id.remarks_reimbursement://备注维修人事件
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_reimbursement).setBackgroundResource(R.drawable.c6_bg);
                findViewById(R.id.remarks_parameter).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_management).setBackgroundResource(R.drawable.hui_bg);

                findViewById(R.id.remarks_photo_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_location_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_user_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.repair_parameter_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_management_layout).setVisibility(View.GONE);

                location_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                photo_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                reimbursement_text.setTextColor(ContextCompat.getColor(this,R.color.green));
                parameter_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                management_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                break;
            case R.id.remarks_parameter://备注参数事件
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_reimbursement).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_parameter).setBackgroundResource(R.drawable.c6_bg);
                findViewById(R.id.remarks_management).setBackgroundResource(R.drawable.hui_bg);

                findViewById(R.id.remarks_photo_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_location_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_user_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_parameter_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.repair_management_layout).setVisibility(View.GONE);

                location_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                photo_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                reimbursement_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                parameter_text.setTextColor(ContextCompat.getColor(this,R.color.green));
                management_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                break;
            case R.id.remarks_management://备注管理人事件
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

                findViewById(R.id.remarks_location).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_photo).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_reimbursement).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_parameter).setBackgroundResource(R.drawable.hui_bg);
                findViewById(R.id.remarks_management).setBackgroundResource(R.drawable.c6_bg);

                findViewById(R.id.remarks_photo_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_location_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_user_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_parameter_layout).setVisibility(View.GONE);
                findViewById(R.id.repair_management_layout).setVisibility(View.VISIBLE);

                location_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                photo_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                reimbursement_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                parameter_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
                management_text.setTextColor(ContextCompat.getColor(this,R.color.green));
                break;
            case R.id.confrim_btn:
                intent=new Intent(CreatRepairRemarksActivity.this,DeviceListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            default:
                break;
        }
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
                        Toast.makeText(CreatRepairRemarksActivity.this,"sd卡不可用！！！",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(CreatRepairRemarksActivity.this,"sd卡不可用！！！",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(CreatRepairRemarksActivity.this,"sd卡不可用！！！",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(CreatRepairRemarksActivity.this,"sd卡不可用！！！",Toast.LENGTH_SHORT).show();
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
                new AlertDialog.Builder(CreatRepairRemarksActivity.this);
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
                            default:
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
            default:
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
}
