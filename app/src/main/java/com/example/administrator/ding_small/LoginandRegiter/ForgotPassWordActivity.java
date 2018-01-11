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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.HelpTool.CountDownTimerUtils;
import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.example.administrator.ding_small.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
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

import static android.icu.lang.UScript.getCode;
import static com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity.SHOW_RESPONSE;

/**
 * Created by CZK on 2017/11/16.
 */

public class ForgotPassWordActivity extends Activity implements View.OnClickListener {
    private EditText p_code, phone, password1, c_password;
    String code_str, phone_str, p1_str, p2_str, saveCode;
    String Msg = "";
    String resultStr = "";
    String code;
    //更改语言所要更改的控件
    private TextView back_text, reset_password_text, phone_text, code_text, send_text, new_password_text, confirm_text;
    private Button confirm_reset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_forgot_password);
        send_text = findViewById(R.id.send_text);
        send_text.setOnClickListener(this);
        confirm_reset = findViewById(R.id.confirm_reset);
        confirm_reset.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        back_text = findViewById(R.id.back_text);
        p_code = findViewById(R.id.p_code);
        phone = findViewById(R.id.phone);
        password1 = findViewById(R.id.new_password);
        c_password = findViewById(R.id.c_new_password);

        phone_text = findViewById(R.id.phone_text);
        code_text = findViewById(R.id.code_text);
        new_password_text = findViewById(R.id.new_password_text);
        confirm_text = findViewById(R.id.confirm_text);
        reset_password_text = findViewById(R.id.reset_password_text);
        changeTextView();//更改语言
    }

    private void changeTextView() {
        if (Locale.getDefault().getLanguage().equals("en")) {
            phone_text.setText("Phone");
            confirm_reset.setText("Confirm Reset");
            back_text.setText("Back");
            code_text.setText("Code");
            send_text.setText("Get");
            new_password_text.setText("New Password");
            reset_password_text.setText("Reset Password");
            confirm_text.setText("Confirm");
            phone.setHint("Enter phone");
            p_code.setHint("Enter Code");
            password1.setHint("Enter New PassWord");
            c_password.setHint("Enter The PassWord Again");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_text:
                phone_str = phone.getText().toString();
                //判断信息
                if (phone_str.equals("")) {
                    new AlertDialog.Builder(ForgotPassWordActivity.this).setTitle("重置提示").setMessage("信息不能为空").setPositiveButton("确定", null).show();
                } else {
                    TextView send_text = findViewById(R.id.send_text);
                    CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(send_text, 60000, 1000);
                    mCountDownTimerUtils.start();
                    // getCode(phone_str);
                    new Thread(networkTask).start();//发送验证码
                }
                break;
            case R.id.confirm_reset:
                code_str = p_code.getText().toString();
                phone_str = phone.getText().toString();
                p1_str = password1.getText().toString();
                p2_str = c_password.getText().toString();
                //判断信息
                if (p1_str.equals("") || p2_str.equals("") || phone_str.equals("") || code_str.equals("")) {
                    new AlertDialog.Builder(ForgotPassWordActivity.this).setTitle("重置提示").setMessage("信息不能为空").setPositiveButton("确定", null).show();
                } else {
                    if (!p1_str.equals(p2_str)) {
                        new AlertDialog.Builder(ForgotPassWordActivity.this).setTitle("重置提示").setMessage("两次密码不相等").setPositiveButton("确定", null).show();
                    } else {
                        //sendConfirmEdit(phone_str,p1_str,saveCode);
                        new Thread(comfirPassword).start();//重置密码
                    }
                }
                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
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
            String b = "{\"memPhone\":" + phone_str + ",\"msgType\":\"3\",\"msgLen\":\"4\"}";//json字符串
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
                              Toast.makeText(ForgotPassWordActivity.this,"验证码已发送",Toast.LENGTH_SHORT).show();
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

    //201701108登录验证类，获取手机号和密码
    private void sendConfirmEdit(final String phone, final String password, final String code) {
        // System.out.println("加密密码："+MD5Utils.md5(pass1));
        new Thread(new Runnable() {

            @Override
            public void run() {
                //用HttpClient发送请求，分为五步
                //第一步：创建HttpClient对象
                HttpClient httpCient = new DefaultHttpClient();
                //第二步：创建代表请求的对象,参数是访问的服务器地址blMac=12:34:56:78:9A:BC&userId=1001&userName=%E5%BC%A0%E4%B8%89
                HttpGet httpGet = new HttpGet("http://192.168.1.101:8080/appUser/appUserRegister.do?opCode=" + phone + "&opPwd=" + password + "&smsVerifCode=" + code);
                try {
                    //第三步：执行请求，获取服务器发还的相应对象
                    HttpResponse httpResponse = httpCient.execute(httpGet);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    System.out.println("状态码：" + httpResponse.getStatusLine().getStatusCode());
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "utf-8");//将entity当中的数据转换为字符串
                        System.out.println("返回结果：" + response);
                        if (response != null) {
                            //在子线程中将Message对象发出去
                            Message message = new Message();
                            message.what = SHOW_RESPONSE;
                            message.obj = response.toString();
                            handler1.sendMessage(message);
                        }
                    } else {
                        Toast.makeText(ForgotPassWordActivity.this, "访问失败!!!请检查服务器...", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println("访问失败！！！");
                    e.printStackTrace();
                }

            }
        }).start();//这个start()方法不要忘记了

    }

    private Handler handler1 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        System.out.println("返回：" + responseObject);
                        Msg = responseObject.getString("msg");
                        code = responseObject.getString("resCode");
                        if (code.equals("00000")) {
                            new AlertDialog.Builder(ForgotPassWordActivity.this).setTitle("返回登陆").setMessage(Msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(ForgotPassWordActivity.this, LoginAcitivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).show();
                        } else if (code.equals("21001")) {
                            new AlertDialog.Builder(ForgotPassWordActivity.this).setTitle("返回登陆").setMessage(Msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(ForgotPassWordActivity.this, LoginAcitivity.class);
                                    startActivity(intent);
                                    finish();
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
     * 网络操作相关的子线程okhttp框架 忘记密码
     */
    Runnable comfirPassword = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String url = "http://120.76.188.131:8080/a10/api/user/forgetPassword.do";
            OkHttpClient okHttpClient = new OkHttpClient();
            String pass = MD5Utils.md5(p1_str);
            String b = "{\"loginAccount\":\"" + phone_str + "\",\"pwdNew\":\"" + pass + "\",\"smsVerifCode\":\"" + code_str + "\"," +
                    "\"pid\":\"BKF-5b405a7d-5fb7-4278-a931-e45a3afe8e55\",\"rid\":\"f8c2d197098440e3909b0782400874d2\",\"cpFlag\":\"0\"}";//json字符串
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
                                new AlertDialog.Builder(ForgotPassWordActivity.this).setTitle("返回登陆").setMessage("重置成功").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(ForgotPassWordActivity.this, LoginAcitivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                }).show();
                                break;
                            case "21001"://手机号已存在
                                new AlertDialog.Builder(ForgotPassWordActivity.this).setTitle("返回重置").setMessage("手机号 " + phone_str + " 已存在.").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                                break;
                            case "21003"://验证码错误
                                new AlertDialog.Builder(ForgotPassWordActivity.this).setTitle("返回重置").setMessage("短信验证码错误, 请核对!").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                                break;
                            case "21004"://短信验证码已失效
                                new AlertDialog.Builder(ForgotPassWordActivity.this).setTitle("返回重置").setMessage("短信验证码已失效, 请重新发送!").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
