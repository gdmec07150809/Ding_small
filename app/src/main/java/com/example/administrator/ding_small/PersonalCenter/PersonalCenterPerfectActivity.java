package com.example.administrator.ding_small.PersonalCenter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.test.ServiceTestCase;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.example.administrator.ding_small.CreatRepairRemarksActivity;
import com.example.administrator.ding_small.DeviceDetailActivity;
import com.example.administrator.ding_small.DeviceListActivity;
import com.example.administrator.ding_small.HelpTool.CustomDialog;
import com.example.administrator.ding_small.HelpTool.LocationUtil;
import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.example.administrator.ding_small.HelpTool.UploadUtil;
import com.example.administrator.ding_small.JsonClass.JsonBean;
import com.example.administrator.ding_small.JsonClass.JsonFileReader;
import com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity;
import com.example.administrator.ding_small.MainLayoutActivity;
import com.example.administrator.ding_small.PerfectDeviceActivity;
import com.example.administrator.ding_small.R;
import com.example.administrator.ding_small.Utils.utils;
import com.google.gson.Gson;
import com.weavey.loading.lib.LoadingLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.administrator.ding_small.HelpTool.LocationUtil.getAddress;

/**
 * Created by CZK on 2017/11/6.
 */

public class PersonalCenterPerfectActivity extends Activity implements View.OnClickListener {
    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    String memid, token, UserSign, setUserSign,oldPass, newPass, ts,getTs, photoSign,c_newPass,sign,nameStr,setTs,photoTs;
    public static final int SHOW_RESPONSE = 0;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 100;

    //更换语言所要更改的控件
    private TextView back_text, perfct_personal_text, change_avatar_text, nickname_text, sex_text, region_text, address_text, signature_text, switch_account_text, next;

    private ImageView head_img;
    private Dialog mCameraDialog;
    private TextView sex_value, location_value, nickname_value_text, address_value, signature_value;

    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private String adress, str_location,path;
    Bitmap bitmap;
    private LoadingLayout loading;

    List<File> fileList=null;//图片集合

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfect_personal);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.head3).setOnClickListener(this);
        head_img = findViewById(R.id.head_img);
        head_img.setOnClickListener(this);
        findViewById(R.id.sex_layout).setOnClickListener(this);
        findViewById(R.id.location_layout).setOnClickListener(this);
        findViewById(R.id.nickname_layout).setOnClickListener(this);
        findViewById(R.id.address_layout).setOnClickListener(this);
        findViewById(R.id.signature_layout).setOnClickListener(this);
        findViewById(R.id.save_layout).setOnClickListener(this);
//        findViewById(R.id.confirm).setOnClickListener(this);
        back_text = findViewById(R.id.back_text);
        perfct_personal_text = findViewById(R.id.perfct_personal_text);
        change_avatar_text = findViewById(R.id.change_avatar_text);
        nickname_text = findViewById(R.id.nickname_text);
        sex_text = findViewById(R.id.sex_text);
        region_text = findViewById(R.id.region_text);
        address_text = findViewById(R.id.address_text);
        signature_text = findViewById(R.id.signature_text);
        switch_account_text = findViewById(R.id.switch_account_text);
        next = findViewById(R.id.next);
        sex_value = findViewById(R.id.sex_value);
        location_value = findViewById(R.id.location_value);
        nickname_value_text = findViewById(R.id.nickname_value_text);
        address_value = findViewById(R.id.address_value);
        signature_value = findViewById(R.id.signature_value);
        loading = findViewById(R.id.loading_layout);

        getCacheUser();//获取用户信息
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        getCachePhoto();//上传图片
        //upPhoto();
        getLocation();//获取地址
        changeTextView();//更改语言
        initJsonData();//地址数据
    }

    private void getCacheUser() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        String url = utils.url+"/api/secr/user/getPersonalInfo.do";
        getTs = String.valueOf(new Date().getTime());
        System.out.println("首页：" + memid + "  ts:" + getTs + "  token:" + token);
        String Sign = url + memid + token + getTs;
        System.out.println("UserSign:" + Sign);
        UserSign = MD5Utils.md5(Sign);
        new Thread(getUserTask).start();//获取用户信息,启动
    }
    private void setUser() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        String url = utils.url+"/api/secr/user/amendPersonalInfo.do";
        //String url = "http://192.168.1.103:8080/api/user/logout.do";
        setTs = String.valueOf(new Date().getTime());
        System.out.println("首页：" + memid + "  ts:" + setTs + "  token:" + token);
        String Sign = url + memid + token + setTs;
        System.out.println("setUserSign:" + Sign);
        setUserSign = MD5Utils.md5(Sign);
        new Thread(setUserTask).start();//获取用户信息,启动
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocation() {

        //获取地址
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        } else {
            LocationUtil.initLocation(PersonalCenterPerfectActivity.this);
            System.out.println("主经度:" + Double.toString(LocationUtil.longitude) + "主纬度：" + Double.toString(LocationUtil.latitude));
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    str_location = getAddress(LocationUtil.location, getApplicationContext());
                    Message message = new Message();
                    message.what = 0;

                    locationHandler.sendMessage(message);

                    //位置信息-----一个字符串
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private Handler locationHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if(str_location!=null&&!str_location.equals("")){
                        location_value.setText(str_location);
                    }
                    break;
                default:break;
            }
        }
    };
    private void changeTextView() {
        if (Locale.getDefault().getLanguage().equals("en")) {
            back_text.setText("Back");
            perfct_personal_text.setText("Perfct Personal");
            change_avatar_text.setText("Change Avatar");
            nickname_text.setText("NickName");
            sex_text.setText("Sex");
            region_text.setText("Region");
            address_text.setText("Address");
            signature_text.setText("Signature");
            switch_account_text.setText("Switch Account");
            next.setText("Save");
        }
    }

    private void getCache() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        String url = utils.url+"/api/user/logout.do";
       // String url = "http://192.168.1.103:8080/api/user/logout.do";

        ts = String.valueOf(new Date().getTime());
        System.out.println("登出：" + memid + "  ts:" + ts + "  token:" + token);
        String Sign = url + memid + token + ts;
        System.out.println("Sign:" + Sign);
        sign = MD5Utils.md5(Sign);
    }

    private void getCachePhoto() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        // String url = "http://192.168.1.108:8080/app/invs6002/lisSecr6002.do";轮播图
        String url = utils.url+"/api/user/userAvatarUpload.do";
        photoTs = String.valueOf(new Date().getTime());
        System.out.println("上传图片：memId" + memid + "  ts:" + photoTs + "  token:" + token);
        String Sign = url + memid + token + photoTs;
        System.out.println("Sign:" + Sign);
        photoSign = MD5Utils.md5(Sign);
        System.out.println("加密sign:" + photoSign);
        // new Thread(networkTask).start();//获取轮播图
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
//            case R.id.confirm:
//                intent = new Intent(PersonalCenterPerfectActivity.this, PersonalCenterActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                break;
            case R.id.head_img://头像

                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 21);
                break;
            case R.id.sex_layout:
                setDialog();//性别弹出框
                break;
            case R.id.nickname_layout:
                setNichNameDialog();//昵称弹出框
                break;
            case R.id.location_layout:
                showPickerView();//地址弹出框
                break;
            case R.id.address_layout:
                setAdressDialog();//具体地址弹出框
                break;
            case R.id.signature_layout:
                setSignatureDialog();//个性签名弹出框
                break;
            case R.id.save_layout:
                new AlertDialog.Builder(this).setTitle("确认保存？")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(fileList!=null&&fileList.size()>0){
                                    loading.setStatus(LoadingLayout.Loading);//状态取消
                                    new Thread(run).start();
                                }else{
                                    setUser();//修改用户信息
                                }
                            }
                        })
                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“返回”后的操作,这里不设置没有任何操作
                                dialog.dismiss();
                            }
                        }).show();

                break;
            case R.id.head3://退出
                getCache();//退出
                new AlertDialog.Builder(this).setTitle("确认退出？")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(networkTask).start();//登出
                            }
                        })
                        .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“返回”后的操作,这里不设置没有任何操作
                                dialog.dismiss();
                            }
                        }).show();
                break;
        }
    }

    /**
     * 网络操作相关的子线程okhttp框架  登出
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String url = utils.url+"/api/user/logout.do?memId=" + memid + "&ts=" + ts;
            OkHttpClient okHttpClient = new OkHttpClient();

            System.out.println("验证：" + sign);
            String b = "{}";//json字符串
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), b);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("sIgn", sign)
                    .build();
            System.out.println(request.headers());
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                String result = response.body().string();
                if (response != null) {
                    //在子线程中将Message对象发出去
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    message.obj = result.toString();
                    loginHandler.sendMessage(message);
                }
                System.out.println("结果：" + result + "状态码：" + response.code());
                //Toast.makeText(EditPassWordActivity.this,result,Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private Handler loginHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    System.out.println(response);
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject object1 = new JSONObject(object.getString("meta"));
                        //{"meta":{"res":"99999","msg":"用户名或密码有误"},"data":null}状态码：200
                        if (object1.getString("res").equals("00000")) {
                            new AlertDialog.Builder(PersonalCenterPerfectActivity.this).setTitle("登出提示").setMessage("登出成功,返回登录").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //清除本地缓存
                                    SharedPreferences userSettings= getSharedPreferences(tokeFile, 0);
                                    SharedPreferences.Editor editor = userSettings.edit();
                                    editor.clear();
                                    editor.commit();

                                    Intent intent = new Intent(PersonalCenterPerfectActivity.this, LoginAcitivity.class);
                                    intent.putExtra("back","out");
                                    startActivity(intent);
                                    finish();
                                }
                            }).show();
                        } else {
                            new AlertDialog.Builder(PersonalCenterPerfectActivity.this).setTitle("登出提示").setMessage("请先登陆").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
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



    //获取手机相册选择的图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //判断那个相机回调
        switch (requestCode) {
            case 21:
                //打开相册并选择照片，这个方式选择单张
                // 获取返回的数据，这里是android自定义的Uri地址
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    // 获取选择照片的数据视图
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    // 从数据视图中获取已选择图片的路径
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    //Toast.makeText(this, picturePath, Toast.LENGTH_SHORT).show();

                    System.out.println(picturePath);
                    cursor.close();
                    // 将图片显示到界面上
                    File file=new File(picturePath);
                    fileList=new ArrayList<File>();
                    fileList.add(file);
                  //  path=picturePath;
                    Drawable drawable = new BitmapDrawable(BitmapFactory.decodeFile(picturePath));
                    head_img.setBackground(drawable);

                }
                break;
            case 10:
                    Toast.makeText(this,"返回",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }


    /**
     * 网络操作相关的子线程okhttp框架 上传头像
     */
    Runnable run = new Runnable() {
        @Override
        public void run() {
            // TODO

                //System.out.println("图片："+file);
                String url=utils.url+"/api/user/userAvatarUpload.do?memId="+memid+"&ts="+photoTs;
                System.out.println("路径："+path+"  :  "+fileList+" : "+photoSign+" : "+url);
                String name="file1";
                uploadFile(fileList,url,photoSign,name,"",PersonalCenterPerfectActivity.this);

         ;
        }
    };
    //性别底部弹出菜单
    private void setDialog() {
        LinearLayout root = null;
        mCameraDialog = new Dialog(this, R.style.Dialog);
        root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.select_sex_layout, null);
             RadioGroup radioGroup = root.findViewById(R.id.group);
        if (sex_value.getText().toString().equals("男")) {
            RadioButton radioButton = root.findViewById(R.id.button1);
            radioButton.setChecked(true);
        } else {
            RadioButton radioButton = root.findViewById(R.id.button2);
            radioButton.setChecked(true);
        }

        // 单选按钮组监听事件
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 根据ID判断选择的按钮
                if (checkedId == R.id.button1) {
                    sex_value.setText("男");
                    mCameraDialog.dismiss();
                } else {
                    sex_value.setText("女");
                    mCameraDialog.dismiss();
                }
            }
        });

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

    //昵称底部弹出菜单
    private void setNichNameDialog() {
        LinearLayout root = null;
        mCameraDialog = new Dialog(this, R.style.Dialog);
        root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.change_name_layout, null);
        Button new_login = root.findViewById(R.id.new_login);

        final EditText editText = root.findViewById(R.id.nickname_value);
        editText.setText(nickname_value_text.getText().toString());
        new_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickname_value_text.setText(editText.getText().toString());
                mCameraDialog.dismiss();
            }
        });

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

    //详细地址底部弹出菜单
    private void setAdressDialog() {
        LinearLayout root = null;
        mCameraDialog = new Dialog(this, R.style.Dialog);
        root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.change_name_layout, null);
        Button new_login = root.findViewById(R.id.new_login);
        final EditText editText = root.findViewById(R.id.nickname_value);
        editText.setText(address_value.getText().toString());
        new_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address_value.setText(editText.getText().toString());
                mCameraDialog.dismiss();
            }
        });

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

    //个性签名底部弹出菜单
    private void setSignatureDialog() {
        LinearLayout root = null;
        mCameraDialog = new Dialog(this, R.style.Dialog);
        root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.change_name_layout, null);
        Button new_login = root.findViewById(R.id.new_login);
        final EditText editText = root.findViewById(R.id.nickname_value);
        new_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signature_value.setText(editText.getText().toString());
                mCameraDialog.dismiss();
            }
        });

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

    private void showPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String text = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                adress = text;
                location_value.setText(text);
            }
        }).setTitleText("")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.GRAY)
                .setContentTextSize(13)
                .setOutSideCancelable(false)
                .setContentTextSize(18)//滚轮文字大小
                .setSubmitColor(ContextCompat.getColor(this, R.color.theme_color))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(this, R.color.theme_color))//取消按钮文字颜色
                .setTitleBgColor(ContextCompat.getColor(this, R.color.time_bg_color))//标题背景颜色 Night mode
                .setBgColor(ContextCompat.getColor(this, R.color.time_bg_color))//滚轮背景颜色 Night mode
                .build();
          /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData() {   //解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        //  获取json数据
        String JsonData = JsonFileReader.getJson(this, "province_data.json");
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    /**
     * 网络操作相关的子线程okhttp框架  获取用户信息
     */
    Runnable getUserTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String url = utils.url+"/api/secr/user/getPersonalInfo.do?memId=" + memid + "&ts=" + getTs;
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder().readTimeout(10, TimeUnit.SECONDS).connectTimeout(10,TimeUnit.SECONDS).build();
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
                            nickname_value_text.setText(nameStr);
                            String img=objectData.getString("imgUrl");
                            //String img="https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1517382173&di=26a2bf5e76ab8b80075729896093b7ac&src=http://image.tianjimedia.com/uploadImages/2015/215/41/M68709LC8O6L.jpg";
                            if(img!=null&&!img.equals("null")){
                                returnBitMap(img);//获取网络图片，并转化为Bitmap格式  设备图片
                            }

                            if(objectData.getString("sex")==null||objectData.getString("sex").equals("")||objectData.getString("sex").equals("null")){
                                sex_value.setText("");
                            }else{
                                sex_value.setText(objectData.getString("sex"));
                            }

                        } else {
                            new AlertDialog.Builder(PersonalCenterPerfectActivity.this).setTitle("网络提示").setMessage("请检查网络").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
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
     * 网络操作相关的子线程okhttp框架  修改用户信息
     */
    Runnable setUserTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String url = utils.url+"/api/secr/user/amendPersonalInfo.do?memId=" + memid + "&ts=" + setTs;
            OkHttpClient okHttpClient = new OkHttpClient();
            String nickValue=nickname_value_text.getText().toString();
            String sexValue=sex_value.getText().toString();
            System.out.println("验证：" + setUserSign);
            String b = "{\"nick\":\""+nickValue+"\",\"sex\":\""+sexValue+"\"}";//json字符串
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), b);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("sIgn", setUserSign)
                    .build();
            System.out.println(request.headers());
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                String result = response.body().string();
                if (response != null) {
                    //在子线程中将Message对象发出去
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    message.obj = result.toString();
                    setUserHandler.sendMessage(message);
                }
                System.out.println("结果：" + result + "状态码：" + response.code());
                //Toast.makeText(EditPassWordActivity.this,result,Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    private Handler setUserHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    System.out.println(response);
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject object1 = new JSONObject(object.getString("meta"));
                        //{"meta":{"res":"99999","msg":"用户名或密码有误"},"data":null}状态码：200
                        if (object1.getString("res").equals("00000")) {
                            loading.setStatus(LoadingLayout.Loading);//状态取消
                            Toast.makeText(PersonalCenterPerfectActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            Intent   intent = new Intent(PersonalCenterPerfectActivity.this, MainLayoutActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            loading.setStatus(LoadingLayout.Error);
                            LoadingLayout.getConfig()
                                    .setErrorText("出错啦~请稍后重试！");
                            new AlertDialog.Builder(PersonalCenterPerfectActivity.this).setTitle("网络提示").setMessage("请检查网络").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
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

    public Bitmap returnBitMap(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;

                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection)imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.setConnectTimeout(10000);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    Message message = new Message();
                    message.what = 0;
                    DeviceImgHandler.sendMessage(message);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        return bitmap;
    }
    private Handler DeviceImgHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Drawable drawable = new BitmapDrawable(bitmap);
                    head_img.setBackground(drawable);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * android上传文件到服务器
     * @param file  需要上传的文件
     * @param RequestURL  请求的rul
     * @param sign  签名
     * @param name  文件名
     * @return  返回响应的内容
     */
    public  String uploadFile(List<File> file, String RequestURL, String sign, String name, String id, Context context){
        String result = null;
        String  BOUNDARY =  UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--" , LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";   //内容类型

        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(100000);
            conn.setConnectTimeout(100000);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", "utf-8");  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("sign", sign);//签名
            // conn.setRequestProperty("eqpId", id);//id
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            conn.connect();

            if(file!=null){
                /**
                 * 当文件不为空，把文件包装并且上传
                 */

                for(File file1:file){
                    System.out.println("图片上传："+file1);
                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                    StringBuffer sb = new StringBuffer();
                    sb.append(PREFIX);
                    sb.append(BOUNDARY);
                    sb.append(LINE_END);
                    /**
                     * 这里重点注意：
                     * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
                     * filename是文件的名字，包含后缀名的   比如:abc.png
                     */

                    System.out.println("文件名："+file1.getName()+" : "+name);
                    sb.append("Content-Disposition: form-data; name=\""+name+"\"; filename=\""+file1.getName()+"\""+"; eqpId=\""+id+"\""+LINE_END);
                    sb.append("Content-Type:image/pjpeg; charset=utf-8"+LINE_END);
                    sb.append(LINE_END);
                    dos.write(sb.toString().getBytes());
                    InputStream is = new FileInputStream(file1);
                    byte[] bytes = new byte[1024];
                    int len = 0;
                    while((len=is.read(bytes))!=-1){
                        dos.write(bytes, 0, len);
                    }

                    is.close();
                    dos.write(LINE_END.getBytes());
                    byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
                    dos.write(end_data);
                    dos.flush();
                    /**
                     * 获取响应码  200=成功
                     * 当响应成功，获取响应的流
                     */
                    //conn.getResponseMessage();
                    int res = conn.getResponseCode();
                    if(res==200){
                        InputStream input =  conn.getInputStream();
                        StringBuffer sb1= new StringBuffer();
                        int ss ;
                        while((ss=input.read())!=-1){
                            sb1.append((char)ss);
                        }
                        result = sb1.toString();
                        Message message = new Message();
                        message.what = 0;
                        upPhotoHandler1.sendMessage(message);
                        message.obj = result;
                        System.out.println("上传图片"+URLDecoder.decode(result, "utf-8"));
                        // Toast.makeText(context,"图片上传成功",Toast.LENGTH_SHORT).show();
                    }else{
                        //Toast.makeText(context,"图片上传失败",Toast.LENGTH_SHORT).show();
                        Message message = new Message();
                        message.what = 1;

                        upPhotoHandler1.sendMessage(message);
                        System.out.println("res"+conn.getResponseMessage()+res);
                    }
                }
            }
        } catch (MalformedURLException e) {
            // sendMessage(UPLOAD_SERVER_ERROR_CODE,"上传失败：error=" + e.getMessage());
            System.out.println("上传失败");
            e.printStackTrace();
        } catch (IOException e) {
            //sendMessage(UPLOAD_SERVER_ERROR_CODE,"上传失败：error=" + e.getMessage());
            System.out.println("上传失败");
            e.printStackTrace();
        }
        return result;
    }

    private Handler upPhotoHandler1 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    String response = (String) msg.obj;
                    JSONObject object = null;
                    try {
                        if(response!=null){
                            object = new JSONObject(response);
                            JSONObject object1 = new JSONObject(object.getString("meta"));
                            if(object1.getString("res").equals("00000")){
                                System.out.println(object1.getString("msg"));
                               // loading.setStatus(LoadingLayout.Success);//状态取消
                                setUser();//修改用户信息
                               // Toast.makeText(PersonalCenterPerfectActivity.this,"图片上传成功",Toast.LENGTH_SHORT).show();
                            }else{
                                System.out.println(object1.getString("msg"));
                                loading.setStatus(LoadingLayout.Success);//状态取消
                                Toast.makeText(PersonalCenterPerfectActivity.this,"图片上传失败,请重新上传",Toast.LENGTH_SHORT).show();
                                fileList=null;
                                Bitmap icon = BitmapFactory.decodeResource(PersonalCenterPerfectActivity.this.getResources(), R.drawable.tou);
                                Drawable drawable = new BitmapDrawable(icon);
                                head_img.setBackground(drawable);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;
                case 1:
                    loading.setStatus(LoadingLayout.Success);//状态取消
                    fileList=null;
                    Bitmap icon = BitmapFactory.decodeResource(PersonalCenterPerfectActivity.this.getResources(), R.drawable.tou);
                    Drawable drawable = new BitmapDrawable(icon);
                    head_img.setBackground(drawable);
                    Toast.makeText(PersonalCenterPerfectActivity.this,"图片上传失败,请重新上传",Toast.LENGTH_SHORT).show();
                    break;
                default:break;
            }
        }
    };
}
