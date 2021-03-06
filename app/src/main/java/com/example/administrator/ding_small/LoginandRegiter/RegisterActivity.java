package com.example.administrator.ding_small.LoginandRegiter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.HelpTool.CountDownTimerUtils;
import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.example.administrator.ding_small.MainLayoutActivity;
import com.example.administrator.ding_small.NewMainLayoutActivity;
import com.example.administrator.ding_small.R;
import com.example.administrator.ding_small.Utils.SysApplication;
import com.example.administrator.ding_small.Utils.utils;
import com.weavey.loading.lib.LoadingLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.password;
import static com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity.SHOW_RESPONSE;
import static com.example.administrator.ding_small.R.id.c_password;
import static com.example.administrator.ding_small.R.id.password1;

/**
 * Created by CZK on 2017/12/28.
 */

public class RegisterActivity extends Activity implements View.OnClickListener {
    private EditText p_code, phone;
    private EditText password, c_password, memName;
    private String p1_str, p2_str, phone_str, code_str, memName_str;

    public static final int SHOW_RESPONSE = 0;
    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    String memId, tokEn;
    private LoadingLayout loading;
    //更改语言所要更改的控件
    private TextView back_text, register_text, email_text, phone_text, code_text, nickname_text, password_text, confirm_text, send_text;
    private Button next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_phone_register);
        SysApplication.getInstance().addActivity(this);
        send_text = findViewById(R.id.send_text);
        send_text.setOnClickListener(this);
        next = findViewById(R.id.next);
        next.setOnClickListener(this);
        findViewById(R.id.email).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        password = findViewById(R.id.password1);
        c_password = findViewById(R.id.c_password);
        p_code = findViewById(R.id.p_code);
        phone = findViewById(R.id.phone);
        memName = findViewById(R.id.memName);

        back_text = findViewById(R.id.back_text);
        register_text = findViewById(R.id.register_text);
        email_text = findViewById(R.id.email_text);
        phone_text = findViewById(R.id.phone_text);
        code_text = findViewById(R.id.code_text);
        nickname_text = findViewById(R.id.nickname_text);
        password_text = findViewById(R.id.password_text);
        confirm_text = findViewById(R.id.confirm_text);
        loading = findViewById(R.id.loading_layout);
        changeTextView();
    }

    private void changeTextView() {
        if (Locale.getDefault().getLanguage().equals("en")) {
            phone_text.setText("Phone");
            register_text.setText("Register");
            back_text.setText("Back");
            code_text.setText("Code");
            email_text.setText("Email");
            nickname_text.setText("NickName");

            confirm_text.setText("Confirm");
            phone.setHint("Enter phone");
            p_code.setHint("Enter Code");
            password_text.setText("PassWord");
            c_password.setHint("Enter The PassWord Again");
            memName.setHint("Enter NickName");
            password.setHint("Enter PassWord");
            send_text.setText("Get");
            next.setText("Finish Register");
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.send_text://发送验证码
                phone_str = phone.getText().toString();
                //判断信息
                if (phone_str.equals("")) {
                    if (Locale.getDefault().getLanguage().equals("en")){
                        new AlertDialog.Builder(RegisterActivity.this).setTitle("Registration prompts").setMessage("Cell phone number can not be empty").setPositiveButton("confirm", null).show();
                    }else{
                        new AlertDialog.Builder(RegisterActivity.this).setTitle("注册提示").setMessage("手机号不能为空").setPositiveButton("确定", null).show();
                    }

                } else {
                    TextView send_text = findViewById(R.id.send_text);
                    CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(send_text, 60000, 1000);
                    mCountDownTimerUtils.start();
                    new Thread(networkTask).start();//发送验证码
                }
                break;
            case R.id.next://完成注册
                comfirPassword();
                break;
            case R.id.email://邮箱注册
                intent = new Intent(RegisterActivity.this, EmailRegisterAcitivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.back://返回
                finish();
                break;
            default:
                break;
        }
    }

    private void comfirPassword() {
        p1_str = password.getText().toString();
        p2_str = c_password.getText().toString();
        code_str = p_code.getText().toString();
        memName_str = memName.getText().toString();
        //判断信息
        if (p1_str.equals("") || p2_str.equals("") || code_str.equals("") || memName_str.equals("")) {
            if (Locale.getDefault().getLanguage().equals("en")){
                new AlertDialog.Builder(RegisterActivity.this).setTitle("Registration prompts").setMessage("Information can not be empty").setPositiveButton("confirm", null).show();
            }else{
                new AlertDialog.Builder(RegisterActivity.this).setTitle("注册提示").setMessage("信息不能为空").setPositiveButton("确定", null).show();
            }

        } else {
            if (!p1_str.equals(p2_str)) {
                if (Locale.getDefault().getLanguage().equals("en")){
                    new AlertDialog.Builder(RegisterActivity.this).setTitle("Registration prompts").setMessage("Two passwords are not equal").setPositiveButton("confirm", null).show();
                }else{
                    new AlertDialog.Builder(RegisterActivity.this).setTitle("注册提示").setMessage("两次密码不相等").setPositiveButton("确定", null).show();
                }

            } else {
                new Thread(sendRegister).start();//注册
            }
        }
    }

    /**
     * 网络操作相关的子线程okhttp框架  发送验证码
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String url = "http://120.76.188.131:8080/a10/api/user/getSmsMsg.do";
           // String url = "http://192.168.1.103:8080/api/user/getSmsMsg.do";
            OkHttpClient okHttpClient = new OkHttpClient();
            String b = "{\"memPhone\":" + phone_str + ",\"msgType\":\"1\",\"msgLen\":\"4\"}";//json字符串
            System.out.println(b);
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
                                    Toast.makeText(RegisterActivity.this,"Verifying code has been sent",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(RegisterActivity.this,"验证码已发送",Toast.LENGTH_SHORT).show();
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
     * 网络操作相关的子线程okhttp框架  注册
     */
    Runnable sendRegister = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            // String url = "http://120.76.188.131:8080/a10/api/user/register.do";
            String url = utils.url+"/api/user/register.do";
            OkHttpClient okHttpClient = new OkHttpClient();
            String pass = MD5Utils.md5(p1_str);
            String b = "{\"memPhone\":\"" + phone_str + "\",\"memPwd1\":\"" + pass + "\",\"memName\":\"" + memName_str + "\",\"smsVerifCode\":\"" + code_str + "\",\"pid\":\"BKF-5b405a7d-5fb7-4278-a931-e45a3afe8e55\",\"rid\":\"f8c2d197098440e3909b0782400874d2\",\"cpFlag\":\"0\"}";//json字符串
            System.out.println(b);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), b);//请求体
            Request request = new Request.Builder()//发送请求
                    .url(url)
                    .post(requestBody)
                    .build();
            System.out.println(request.headers());
            Call call = okHttpClient.newCall(request);//创建回调
            try {
                Response response = call.execute();//获取请求结果
                String result = response.body().string();
                if (response != null) {
                    //在子线程中将Message对象发出去
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    message.obj = result.toString();
                    sendRegisterHandler.sendMessage(message);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    //接收注册返回结果，并处理
    private Handler sendRegisterHandler = new Handler() {

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
//                                new AlertDialog.Builder(RegisterActivity.this).setTitle("返回登陆").setMessage("注册成功").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        Intent intent = new Intent(RegisterActivity.this, LoginAcitivity.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        startActivity(intent);
//                                    }
//                                }).show();
                                new Thread(loginRun).start();//登录
                                break;
                            case "21001"://手机号已存在
                                if (Locale.getDefault().getLanguage().equals("en")){
                                    new AlertDialog.Builder(RegisterActivity.this).setTitle("return").setMessage("phone " + phone_str + " Already existed.").setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                                }else{
                                    new AlertDialog.Builder(RegisterActivity.this).setTitle("返回注册").setMessage("手机号 " + phone_str + " 已存在.").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                                }

                                break;
                            case "21003"://验证码错误
                                if (Locale.getDefault().getLanguage().equals("en")){
                                    new AlertDialog.Builder(RegisterActivity.this).setTitle("return").setMessage("SMS verification code error, please check!").setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                                }else {
                                    new AlertDialog.Builder(RegisterActivity.this).setTitle("返回注册").setMessage("短信验证码错误, 请核对!").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                                }
                                break;
                            case "21004"://短信验证码已失效
                                if (Locale.getDefault().getLanguage().equals("en")){
                                    new AlertDialog.Builder(RegisterActivity.this).setTitle("return").setMessage("SMS authentication code has failed, please send it again!").setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                                }else {
                                    new AlertDialog.Builder(RegisterActivity.this).setTitle("返回注册").setMessage("短信验证码已失效, 请重新发送!").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                                    break;
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

    /**
     * 网络操作相关的子线程okhttp框架  登录
     */
    Runnable loginRun = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String url = utils.url+"/api/user/login.do";
            //String url = "http://192.168.1.105:8080/api/user/login.do";
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(20, TimeUnit.SECONDS).build();
            String pass = MD5Utils.md5(p2_str);

            String b = "{\"loginType\":\"2\",\"loginPwd\":\"" + pass + "\",\"loginAccount\":\"" + phone_str + "\",\"pid\":\"BKF-5b405a7d-5fb7-4278-a931-e45a3afe8e55\",\"rid\":\"f8c2d197098440e3909b0782400874d2\",\"cpFlag\":\"0\"}";//json字符串
            System.out.println(b);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), b);//请求体
            Request request = new Request.Builder()//发送请求
                    .url(url)
                    .post(requestBody)
                    .build();
            System.out.println(request.headers());
            Call call = okHttpClient.newCall(request);//创建回调
            try {
                Response response = call.execute();//获取请求结果
                String result = response.body().string();
                if (response != null) {
                    //在子线程中将Message对象发出去
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    message.obj = result.toString();
                    loginHandler.sendMessage(message);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    //接收注册返回结果，并处理
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
                        switch (object1.getString("res")) {
                            case "99999"://登录失败
                                loading.setStatus(LoadingLayout.Success);
                                if (Locale.getDefault().getLanguage().equals("en")){
                                    new AlertDialog.Builder(RegisterActivity.this).setTitle("Login again").setMessage(object1.getString("msg")).setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                                }else{
                                    new AlertDialog.Builder(RegisterActivity.this).setTitle("重新登录").setMessage(object1.getString("msg")).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                                }

                                break;
                            case "00000"://登录成功
                                loading.setStatus(LoadingLayout.Success);
                                if(object.getString("data")!=null||object.getString("data").equals("null")){
                                    JSONObject jsonObject = new JSONObject(object.getString("data"));
                                    System.out.println(jsonObject);
                                    memId = jsonObject.getString("memId");
                                    tokEn = jsonObject.getString("tokEn");

                                    if (memId != null && tokEn != null) {
                                        if (memId.length() > 0 && tokEn.length() > 0) {
                                            System.out.println("缓存：" + memId + ":" + tokEn);
                                            //储存token,备用
                                            sp = RegisterActivity.this.getSharedPreferences(tokeFile, MODE_PRIVATE);//实例化
                                            SharedPreferences.Editor editor = sp.edit(); //使处于可编辑状态
                                            editor.putString("tokEn", tokEn);
                                            editor.putString("memId", memId);
                                            editor.putString("phone", phone_str);
                                            editor.commit();    //提交数据保存
                                        }
                                    }
                                }
                                if (Locale.getDefault().getLanguage().equals("en")){
                                    Toast.makeText(RegisterActivity.this,"success",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(RegisterActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                                }

                                Intent intent = new Intent(RegisterActivity.this, NewMainLayoutActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            default:
                                loading.setStatus(LoadingLayout.Success);
                                if (Locale.getDefault().getLanguage().equals("en")){
                                    new AlertDialog.Builder(RegisterActivity.this).setTitle("Login again").setMessage(object1.getString("msg")).setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                                }else{
                                    new AlertDialog.Builder(RegisterActivity.this).setTitle("重新登录").setMessage(object1.getString("msg")).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
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
}
