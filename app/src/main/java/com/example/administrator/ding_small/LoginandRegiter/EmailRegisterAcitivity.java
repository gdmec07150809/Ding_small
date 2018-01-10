package com.example.administrator.ding_small.LoginandRegiter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.ding_small.HelpTool.CountDownTimerUtils;
import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.example.administrator.ding_small.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by CZK on 2017/12/29.
 */

public class EmailRegisterAcitivity extends Activity implements View.OnClickListener{
    private EditText p_code,phone;
    private EditText password,c_password;
    private String p1_str,p2_str,phone_str,code_str;
    public static final int SHOW_RESPONSE = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_mail_register);
        findViewById(R.id.send_text).setOnClickListener(this);
        findViewById(R.id.next).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        p_code = findViewById(R.id.p_code);
        phone = findViewById(R.id.phone);
        password=findViewById(R.id.password1);
        c_password=findViewById(R.id.c_password);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.send_text://发送验证码
                phone_str = phone.getText().toString();
                //判断信息
                if (phone_str.equals("")) {
                    new AlertDialog.Builder(EmailRegisterAcitivity.this).setTitle("注册提示").setMessage("邮箱不能为空").setPositiveButton("确定", null).show();
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
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }
    private  void comfirPassword(){
        p1_str=password.getText().toString();
        p2_str=c_password.getText().toString();
        code_str=p_code.getText().toString();
        //判断信息
        if(p1_str.equals("")|| p2_str.equals("")||code_str.equals("")){
            new AlertDialog.Builder(EmailRegisterAcitivity.this).setTitle("注册提示").setMessage("密码不能为空").setPositiveButton("确定",null).show();
        }else{
            if(!p1_str.equals(p2_str)){
                new AlertDialog.Builder(EmailRegisterAcitivity.this).setTitle("注册提示").setMessage("两次密码不相等").setPositiveButton("确定",null).show();
            }else{
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
            OkHttpClient okHttpClient = new OkHttpClient();
            String b= "{\"memPhone\":\""+phone_str+"\",\"msgType\":\"1\",\"msgLen\":\"4\"}";//json字符串
            System.out.println("邮箱："+b);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), b);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            System.out.println(request.headers());
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                System.out.println("结果："+response.body().string()+"状态码："+ response.code());
            } catch (IOException e) {
                e.printStackTrace();
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
            String url = "http://120.76.188.131:8080/a10/api/user/register.do";
            OkHttpClient okHttpClient = new OkHttpClient();
            String pass= MD5Utils.md5(p1_str);
            String b= "{\"memPhone\":\""+phone_str+"\",\"memPwd1\":\""+pass+"\",\"smsVerifCode\":\""+code_str+"\",\"pid\":\"BKF-5b405a7d-5fb7-4278-a931-e45a3afe8e55\",\"rid\":\"f8c2d197098440e3909b0782400874d2\",\"cpFlag\":\"0\"}";//json字符串
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
                String result=response.body().string();
                if(response!=null){
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
                        JSONObject object=new JSONObject(response);
                        JSONObject object1=new JSONObject(object.getString("meta"));

                        switch (object1.getString("res")){
                            case "00000"://成功
                                new AlertDialog.Builder(EmailRegisterAcitivity.this).setTitle("返回登陆").setMessage("注册成功").setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent =new Intent(EmailRegisterAcitivity.this,LoginAcitivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                }).show();
                                break;
                            case "21001"://手机号已存在
                                new AlertDialog.Builder(EmailRegisterAcitivity.this).setTitle("返回注册").setMessage("手机号 "+phone_str+" 已存在.").setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                                break;
                            case "21003"://验证码错误
                                new AlertDialog.Builder(EmailRegisterAcitivity.this).setTitle("返回注册").setMessage("短信验证码错误, 请核对!").setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                                break;
                            case "21004"://短信验证码已失效
                                new AlertDialog.Builder(EmailRegisterAcitivity.this).setTitle("返回注册").setMessage("短信验证码已失效, 请重新发送!").setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
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
