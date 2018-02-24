package com.example.administrator.ding_small.PersonalCenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.HelpTool.CountDownTimerUtils;
import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity;
import com.example.administrator.ding_small.LoginandRegiter.RegisterActivity;
import com.example.administrator.ding_small.NewMainLayoutActivity;
import com.example.administrator.ding_small.R;
import com.example.administrator.ding_small.Utils.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by CZK on 2018/start1/9.
 */

public class ChangePhoneActivity extends Activity implements View.OnClickListener {
    private TextView back, phone;
    private Dialog mCameraDialog;//弹出窗初始化
    private TextView back_text,reset_password_text,phone_text,code_text;

    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    String memid, token, sign, newPass, ts, phone_str,save_phone,changeTs,changeSign;
    private TextView send_text;
    public static final int SHOW_RESPONSE = 0;
    private EditText p_code;
    private Button confirm_change;
    LinearLayout root = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_phone_layout);
        findViewById(R.id.back).setOnClickListener(this);
        confirm_change=findViewById(R.id.confirm_change);
        confirm_change.setOnClickListener(this);
        phone = findViewById(R.id.phone);
        send_text = findViewById(R.id.send_text);
        send_text.setOnClickListener(this);
        // back_text,reset_password_text,phone_text,code_text;
        back_text=findViewById(R.id.back_text);
        reset_password_text=findViewById(R.id.reset_password_text);
        phone_text=findViewById(R.id.phone_text);
        code_text=findViewById(R.id.code_text);
        p_code=findViewById(R.id.p_code);
        getCache();
        getChangeCache();//修改账号
        changeTextView();//更换语言


    }

    private void changeTextView(){
        if (Locale.getDefault().getLanguage().equals("en")) {
            back_text.setText("Back");
            reset_password_text.setText("Change Phone");
            phone_text.setText("Phone");
            code_text.setText("Verify Code");
            send_text.setText("gain");
            phone.setHint("Enter");
            p_code.setHint("Enter");
            confirm_change.setText("confirm");

        }
    }
    private void getCache() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        save_phone=sp.getString("phone","null");
        // String url = "http://192.168.1.104:8080/app/ppt6000/dateList.do";
        ///app/secr9000lisSecr9000
        String url = utils.url+"/app/secr9000/deleteSecr9000ByKey.do";
        ts = String.valueOf(new Date().getTime());
        System.out.println("首页：" + memid + "  ts:" + ts + "  token:" + token);
        String Sign = url + memid + token + ts;
        System.out.println("Sign:" + Sign);
        sign = MD5Utils.md5(Sign);
        System.out.println("加密Sign:"+sign);
    }
    private void getChangeCache() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        // String url = "http://192.168.1.104:8080/app/ppt6000/dateList.do";
        ///app/secr9000lisSecr9000
        String url = utils.url+"/api/user/changeMobile.do";
        changeTs = String.valueOf(new Date().getTime());
        System.out.println("首页：" + memid + "  ts:" + changeTs + "  token:" + token);
        String Sign = url + memid + token + changeTs;
        System.out.println("Sign:" + Sign);
        changeSign = MD5Utils.md5(Sign);
        System.out.println("手机加密Sign:"+changeSign);
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back://返回
                finish();
                break;
            case R.id.confirm_change://确认修改
                phone_str=phone.getText().toString().trim();
                String send_str=p_code.getText().toString().trim();
                if(phone_str.equals("")||send_str.equals("")){
                    if (Locale.getDefault().getLanguage().equals("en")){
                        new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("The replacement of mobile phone prompt").setMessage("Information can not be empty").setPositiveButton("confirm", null).show();
                    }else{
                        new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("更换手机提示").setMessage("信息不能为空").setPositiveButton("确定", null).show();
                    }

                }else{
                  /*修改*/
                    new Thread(ChangePhoneTask).start();//发送验证码
                }
                break;
            case R.id.send_text://发送验证码
                phone_str = phone.getText().toString();
                //判断信息
                if (phone_str.equals("")) {
                    if (Locale.getDefault().getLanguage().equals("en")){
                        new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("The replacement of mobile phone prompt").setMessage("Mobile phone number can not be empty").setPositiveButton("confirm", null).show();
                    }else{
                        new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("更换手机提示").setMessage("手机号不能为空").setPositiveButton("确定", null).show();
                    }

                } else {
                    if(phone_str.equals(save_phone)){
                        if (Locale.getDefault().getLanguage().equals("en")){
                            new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("The replacement of mobile phone prompt").setMessage("You have to enter the old cell phone").setPositiveButton("confirm", null).show();
                        }else{
                            new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("更换手机提示").setMessage("必须输入新手机").setPositiveButton("确定", null).show();
                        }
                    }else{
                        TextView send_text = findViewById(R.id.send_text);
                        CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(send_text, 60000, 1000);//1秒后，一分钟倒计时
                        mCountDownTimerUtils.start();
                        new Thread(networkTask).start();//发送验证码
                    }
                }
                break;
        }
    }


    /**
     * 网络操作相关的子线程okhttp框架  验证码
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            //String url = "http://120.76.188.131:8080/a10/api/user/getSmsMsg.do";
            String url = utils.url+"/api/user/getSmsMsg.do";
            OkHttpClient okHttpClient = new OkHttpClient();
            String b = "{\"memPhone\":" + phone_str + ",\"msgType\":\"4\",\"msgLen\":\"4\"}";//json字符串
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), b);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            System.out.println(request.headers());
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                if (response != null) {
                    //在子线程中将Message对象发出去
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    message.obj = response.toString();
                    codeHandler.sendMessage(message);
                }
                System.out.println("结果：" + response.body().string() + "状态码：" + response.code());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    /*验证码处理类*/
    private Handler codeHandler = new Handler() {

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
                        switch (object1.getString("res")) {
                            case "00000"://成功
                                if (Locale.getDefault().getLanguage().equals("en")){
                                    Toast.makeText(ChangePhoneActivity.this,"Verifying code has been sent",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(ChangePhoneActivity.this,"验证码已发送",Toast.LENGTH_SHORT).show();
                                }

                                break;
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
     * 网络操作相关的子线程okhttp框架  修改手机
     */
    Runnable ChangePhoneTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String url = utils.url+"/api/user/changeMobile.do?memId=" + memid + "&ts=" + changeTs;
            //String url = "http://120.76.188.131:8080/a10/api/user/changePwd.do?memId=" + memid + "&ts=" + ts;
            String send_str=p_code.getText().toString().trim();
            OkHttpClient okHttpClient = new OkHttpClient();
            System.out.println("验证：" + changeSign);
            String b = "{\"mobileNew\":\""+phone_str+"\",\"smsVerifCode\":\""+send_str+"\",\"pid\":\"BKF-5b405a7d-5fb7-4278-a931-e45a3afe8e55\",\"rid\":\"f8c2d197098440e3909b0782400874d2\",\"cpFlag\":\"0\"}";//json字符串
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), b);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("sIgn", changeSign)
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
                    ChangePhoneHandler.sendMessage(message);
                }
                System.out.println("修改结果：" + result + "状态码：" + response.code());
                //Toast.makeText(EditPassWordActivity.this,result,Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    private Handler ChangePhoneHandler = new Handler() {

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
                            if (Locale.getDefault().getLanguage().equals("en")){
                                new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("Modify the password").setMessage("Revise successfully and return to the home page").setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(ChangePhoneActivity.this, NewMainLayoutActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).show();
                            }else{
                                new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("修改密码").setMessage("修改成功,返回首页").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(ChangePhoneActivity.this, NewMainLayoutActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).show();
                            }
                        } else {
                            if (Locale.getDefault().getLanguage().equals("en")){
                                new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("Modify the password").setMessage(object1.getString("msg")).setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                            }else{
                                new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("修改密码").setMessage(object1.getString("msg")).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                            }
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

}
