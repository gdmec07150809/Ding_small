package com.example.administrator.ding_small.LoginandRegiter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.content.Intent;

import android.widget.EditText;


import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.example.administrator.ding_small.MainLayoutActivity;
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
import static com.example.administrator.ding_small.R.*;



public class LoginAcitivity extends Activity implements  View.OnClickListener{
    private EditText user_name,user_password,p_code,phone,password1,c_password;
    public static final int SHOW_RESPONSE = 0;
    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp=null;//定义储存源，备用
    String memId,tokEn;
    String code_str,phone_str,p1_str,p2_str,login_user,login_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_login);
//        findViewById(R.id.left).setOnClickListener(this);
//        findViewById(R.id.right).setOnClickListener(this);
        findViewById(R.id.new_login).setOnClickListener(this);
//        findViewById(R.id.send_text).setOnClickListener(this);
        findViewById(R.id.register).setOnClickListener(this);
        findViewById(R.id.forgot_password).setOnClickListener(this);
        findViewById(id.back).setOnClickListener(this);
        user_name=findViewById(R.id.user_name);
        user_password=findViewById(R.id.user_password);

    }


    @Override
    public void onClick(View v) {
        Intent  intent;
        switch(v.getId()){
//            case R.id.left:
//                findViewById(R.id.l_bottom).setBackgroundColor(getResources().getColor(color.green));
//                findViewById(R.id.r_bottom).setBackgroundColor(getResources().getColor(color.white));
//                findViewById(R.id.login_xml).setVisibility(View.VISIBLE);
//                findViewById(R.id.register_xml).setVisibility(View.GONE);
//                break;
//            case R.id.right:
//                findViewById(R.id.l_bottom).setBackgroundColor(getResources().getColor(color.white));
//                findViewById(R.id.r_bottom).setBackgroundColor(getResources().getColor(color.green));
//                findViewById(R.id.login_xml).setVisibility(View.GONE);
//                findViewById(R.id.register_xml).setVisibility(View.VISIBLE);
//                break;
            case R.id.new_login:
                login_user=user_name.getText().toString().trim();
                login_pass=user_password.getText().toString().trim();
                System.out.println("用户密码："+login_user+":"+login_pass);
                //判断是否为空
                if(login_user.equals("")|| login_pass.equals("")){
                    new AlertDialog.Builder(LoginAcitivity.this).setTitle("登录提示").setMessage("用户名或密码不能为空！！！").setPositiveButton("确定",null).show();
                }else {
                   // Intent  intent=new Intent(LoginAcitivity.this,AccountBookActivity.class);
//                    Intent  intent=new Intent(LoginAcitivity.this,MainLayoutActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    //跳转
//                    startActivity(intent);
//                    finish();
                    //访问服务器
                    new Thread(loginRun).start();//登录
                }
                break;
//            case R.id.send_text://发送验证码
//                p_code=findViewById(R.id.p_code);
//                phone=findViewById(R.id.phone);
//                password1=findViewById(R.id.password1);
//                c_password=findViewById(R.id.c_password);
//
//                code_str=p_code.getText().toString();
//                phone_str=phone.getText().toString();
//                p1_str=password1.getText().toString();
//                p2_str=c_password.getText().toString();
//                //判断信息
//                if(p1_str.equals("")|| p2_str.equals("")||phone_str.equals("")){
//                    new AlertDialog.Builder(LoginAcitivity.this).setTitle("注册提示").setMessage("信息不能为空").setPositiveButton("确定",null).show();
//                }else{
//                    if(!p1_str.equals(p2_str)){
//                        new AlertDialog.Builder(LoginAcitivity.this).setTitle("注册提示").setMessage("两次密码不相等").setPositiveButton("确定",null).show();
//                    }else{
//                       TextView send_text=findViewById(id.send_text);
//                        CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(send_text, 60000, 1000);
//                        mCountDownTimerUtils.start();
//                        //getCode(phone_str,"1","4");
//                       new Thread(networkTask).start();//发送验证码
//                    }
//                }
//                break;
            case R.id.register://注册
//                code_str=p_code.getText().toString();
//                phone_str=phone.getText().toString();
//                p1_str=password1.getText().toString();
//                p2_str=c_password.getText().toString();
//                //判断信息
//                if(p1_str.equals("")|| p2_str.equals("")||phone_str.equals("")){
//                    new AlertDialog.Builder(LoginAcitivity.this).setTitle("注册提示").setMessage("信息不能为空").setPositiveButton("确定",null).show();
//                }else{
//                    if(!p1_str.equals(p2_str)){
//                        new AlertDialog.Builder(LoginAcitivity.this).setTitle("注册提示").setMessage("两次密码不相等").setPositiveButton("确定",null).show();
//                    }else{
//                      new Thread(sendRegister).start();//注册
//                    }
//                }
                    intent=new Intent(LoginAcitivity.this,RegisterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //跳转
                    startActivity(intent);
                break;
            case R.id.forgot_password://忘记密码
                intent=new Intent(LoginAcitivity.this,ForgotPassWordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //跳转
                startActivity(intent);
                break;
            case id.back:
                finish();
                break;
            default:
                break;

        }
    }

    /**
     * 网络操作相关的子线程okhttp框架  登录
     */
    Runnable loginRun = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String url = "http://192.168.1.101:8080/a10/api/user/login.do";
            OkHttpClient okHttpClient = new OkHttpClient();
            String pass=MD5Utils.md5(login_pass);

            String b= "{\"loginType\":\"2\",\"loginPwd\":\""+pass+"\",\"loginAccount\":\""+login_user+"\",\"pid\":\"BKF-5b405a7d-5fb7-4278-a931-e45a3afe8e55\"}";//json字符串
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
                        JSONObject object=new JSONObject(response);
                        JSONObject object1=new JSONObject(object.getString("meta"));
                        JSONObject jsonObject=new JSONObject(object.getString("data"));
                        memId=jsonObject.getString("memId");
                        tokEn=jsonObject.getString("tokEn");

                        if(memId!=null&&tokEn!=null){
                            if(memId.length()>0&&tokEn.length()>0){
                                System.out.println("缓存："+memId+":"+tokEn);
                                //储存token,备用
//                                String b= "{\"memId\":"+memId+",\"token\":"+tokEn+"}";//json字符串
//                                System.out.println("json字符串："+b);
                                sp=LoginAcitivity.this.getSharedPreferences(tokeFile, MODE_PRIVATE);//实例化
                                SharedPreferences.Editor editor = sp.edit(); //使处于可编辑状态
                                editor.putString("tokEn", tokEn);
                                editor.putString("memId", memId);
                                editor.commit();    //提交数据保存
                            }
                        }
                        switch (object1.getString("res")){
                            case "00000"://登录成功
                               Intent intent=new Intent(LoginAcitivity.this,MainLayoutActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case "99999"://登录失败
                                new AlertDialog.Builder(LoginAcitivity.this).setTitle("重新登录").setMessage(object1.getString("msg")).setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                                break;
                            default:
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
     * 网络操作相关的子线程okhttp框架  发送验证码
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String url = "http://192.168.1.101:8080/a10/api/user/getSmsMsg.do";
            OkHttpClient okHttpClient = new OkHttpClient();
            String b= "{\"memPhone\":"+phone_str+",\"msgType\":\"1\",\"msgLen\":\"4\"}";//json字符串
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
            String url = "http://192.168.1.101:8080/a10/api/user/register.do";
            OkHttpClient okHttpClient = new OkHttpClient();
            String pass=MD5Utils.md5(p1_str);
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
                                new AlertDialog.Builder(LoginAcitivity.this).setTitle("返回登陆").setMessage("注册成功").setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        findViewById(R.id.l_bottom).setBackgroundColor(getResources().getColor(color.green));
                                        findViewById(R.id.r_bottom).setBackgroundColor(getResources().getColor(color.white));
                                        findViewById(R.id.login_xml).setVisibility(View.VISIBLE);
                                        findViewById(R.id.register_xml).setVisibility(View.GONE);
                                    }
                                }).show();
                                break;
                            case "21001"://手机号已存在
                                new AlertDialog.Builder(LoginAcitivity.this).setTitle("返回注册").setMessage("手机号 "+phone_str+" 已存在.").setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                                break;
                            case "21003"://验证码错误
                                new AlertDialog.Builder(LoginAcitivity.this).setTitle("返回注册").setMessage("短信验证码错误, 请核对!").setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                                break;
                            case "21004"://短信验证码已失效
                                new AlertDialog.Builder(LoginAcitivity.this).setTitle("返回注册").setMessage("短信验证码已失效, 请重新发送!").setPositiveButton("确定",new DialogInterface.OnClickListener() {
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
