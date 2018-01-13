package com.example.administrator.ding_small.LoginandRegiter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.content.Intent;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.example.administrator.ding_small.MainLayoutActivity;
import com.example.administrator.ding_small.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;


import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.administrator.ding_small.NotepadActivity.TAG;
import static com.example.administrator.ding_small.R.*;


public class LoginAcitivity extends Activity implements View.OnClickListener {
    private EditText user_name, user_password, p_code, phone, password1, c_password;
    public static final int SHOW_RESPONSE = 0;
    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    String memId, tokEn;
    String code_str, phone_str, p1_str, p2_str, login_user, login_pass;


    private long clickTime = 0;
    //更改语言所要更改的控件
    private TextView phone_text, password_text, forget_password, register;
    private Button new_login;

    //重写onKeyDown方法,实现双击退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再次点击退出", Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            Log.e(TAG, "exit application");
            this.finish();
            System.exit(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_login);
//        findViewById(R.id.left).setOnClickListener(this);
//        findViewById(R.id.right).setOnClickListener(this);
        new_login = findViewById(R.id.new_login);
        new_login.setOnClickListener(this);
        register = findViewById(R.id.register);
        register.setOnClickListener(this);
        forget_password = findViewById(R.id.forgot_password);
        forget_password.setOnClickListener(this);
        //findViewById(R.id.new_login).setOnClickListener(this);
//        findViewById(R.id.send_text).setOnClickListener(this);
//        findViewById(R.id.register).setOnClickListener(this);
//        findViewById(R.id.forgot_password).setOnClickListener(this);
        findViewById(id.back).setOnClickListener(this);

        user_name = findViewById(R.id.user_name);
        user_password = findViewById(R.id.user_password);
        phone_text = findViewById(R.id.phone_text);
        password_text = findViewById(R.id.password_text);

        changeTextView();//更改语言
    }

    private void changeTextView() {
        if (Locale.getDefault().getLanguage().equals("en")) {
            phone_text.setText("Phone");
            password_text.setText("PassWord");
            user_name.setHint("Enter Phone");
            user_password.setHint("Enter PassWord");
            forget_password.setText("Forgot?");
            register.setText("Register");
            new_login.setText("Login");
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.new_login:
                login_user = user_name.getText().toString().trim();
                login_pass = user_password.getText().toString().trim();
                System.out.println("用户密码：" + login_user + ":" + login_pass);
                //判断是否为空
                if (login_user.equals("") || login_pass.equals("")) {
                    new AlertDialog.Builder(LoginAcitivity.this).setTitle("登录提示").setMessage("用户名或密码不能为空！！！").setPositiveButton("确定", null).show();
                } else {
                    //访问服务器
                    new Thread(loginRun).start();//登录
                }
                break;

            case R.id.register://注册
                //判断当前系统语言,进入对应注册页面
                if (Locale.getDefault().getLanguage().equals("en")){
                    intent = new Intent(LoginAcitivity.this, EmailRegisterAcitivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //跳转
                    startActivity(intent);
                }else {
                    intent = new Intent(LoginAcitivity.this, RegisterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //跳转
                    startActivity(intent);
                }

                break;
            case R.id.forgot_password://忘记密码
                intent = new Intent(LoginAcitivity.this, ForgotPassWordActivity.class);
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
            // String url = "http://120.76.188.131:8080/a10/api/user/login.do";192.168.1.105
            String url = "http://192.168.1.103:8080/api/user/login.do";
            OkHttpClient okHttpClient = new OkHttpClient();
            String pass = MD5Utils.md5(login_pass);

            String b = "{\"loginType\":\"2\",\"loginPwd\":\"" + pass + "\",\"loginAccount\":\"" + login_user + "\",\"pid\":\"BKF-5b405a7d-5fb7-4278-a931-e45a3afe8e55\",\"rid\":\"f8c2d197098440e3909b0782400874d2\",\"cpFlag\":\"0\"}";//json字符串
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
                                new AlertDialog.Builder(LoginAcitivity.this).setTitle("重新登录").setMessage(object1.getString("msg")).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                                break;
                            case "00000"://登录成功
                                if(object.getString("data")!=null||object.getString("data").equals("null")){
                                    JSONObject jsonObject = new JSONObject(object.getString("data"));
                                    memId = jsonObject.getString("memId");
                                    tokEn = jsonObject.getString("tokEn");

                                    if (memId != null && tokEn != null) {
                                        if (memId.length() > 0 && tokEn.length() > 0) {
                                            System.out.println("缓存：" + memId + ":" + tokEn);
                                            //储存token,备用
//                                String b= "{\"memId\":"+memId+",\"token\":"+tokEn+"}";//json字符串
//                                System.out.println("json字符串："+b);
                                            sp = LoginAcitivity.this.getSharedPreferences(tokeFile, MODE_PRIVATE);//实例化
                                            SharedPreferences.Editor editor = sp.edit(); //使处于可编辑状态
                                            editor.putString("tokEn", tokEn);
                                            editor.putString("memId", memId);
                                            editor.putString("phone", login_user);
                                            editor.commit();    //提交数据保存
                                        }
                                    }
                                }

                                Toast.makeText(LoginAcitivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginAcitivity.this, MainLayoutActivity.class);
                                startActivity(intent);
                                finish();
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

}
