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
import com.example.administrator.ding_small.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by CZK on 2018/1/9.
 */

public class ChangePhoneActivity extends Activity implements View.OnClickListener {
    private TextView back, phone;
    private Dialog mCameraDialog;//弹出窗初始化

    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    String memid, token, sign, newPass, ts, phone_str,save_phone;
    private TextView send_text;
    public static final int SHOW_RESPONSE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_phone_layout);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.confirm_change).setOnClickListener(this);
        phone = findViewById(R.id.phone);
        send_text = findViewById(R.id.send_text);
        send_text.setOnClickListener(this);
        getCache();


    }

    private void getCache() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        save_phone=sp.getString("phone","null");
        // String url = "http://192.168.1.104:8080/app/ppt6000/dateList.do";
        ///app/secr9000lisSecr9000
        String url = "http://192.168.1.107:8080/app/secr9000/deleteSecr9000ByKey.do";
        ts = String.valueOf(new Date().getTime());
        System.out.println("首页：" + memid + "  ts:" + ts + "  token:" + token);
        String Sign = url + memid + token + ts;
        System.out.println("Sign:" + Sign);
        sign = MD5Utils.md5(Sign);
        System.out.println("加密Sign:"+sign);
        new Thread(networkTask).start();//获取设备列表
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
                String send_str=send_text.getText().toString().trim();
                if(phone_str.equals("")||send_str.equals("")){
                    new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("注册提示").setMessage("信息不能为空").setPositiveButton("确定", null).show();
                }else{
                    setPhoneDialog();
                }
                break;
            case R.id.send_text://发送验证码
                phone_str = phone.getText().toString();
                //判断信息
                if (phone_str.equals("")) {
                    new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("注册提示").setMessage("手机号不能为空").setPositiveButton("确定", null).show();
                } else {
                    if(!phone_str.equals(save_phone)){
                        new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("注册提示").setMessage("必须输入旧手机").setPositiveButton("确定", null).show();
                    }else{
                        TextView send_text = findViewById(R.id.send_text);
                        CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(send_text, 60000, 1000);
                        mCountDownTimerUtils.start();
                        new Thread(networkTask).start();//发送验证码
                    }
                }
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
            //String url = "http://120.76.188.131:8080/a10/api/user/getSmsMsg.do";
            String url = "http://192.168.1.107:8080/api/user/getSmsMsg.do";
            OkHttpClient okHttpClient = new OkHttpClient();
            String b = "{\"memPhone\":" + phone_str + ",\"msgType\":\"3\",\"msgLen\":\"4\"}";//json字符串
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
                                Toast.makeText(ChangePhoneActivity.this,"验证码已发送",Toast.LENGTH_SHORT).show();
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

    //更换手机底部弹出菜单
    private void setPhoneDialog() {
        LinearLayout root = null;
        mCameraDialog = new Dialog(this, R.style.Dialog);
        root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.change_phone_dialog, null);
        Button new_login = root.findViewById(R.id.new_login);
        final EditText editText = root.findViewById(R.id.newPhone_value);
        new_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断输入的手机号是否和上次一样
                if (save_phone.equals(editText.getText().toString())) {
                    new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("更换手机").setMessage("不能跟上次手机一样").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                } else {
                    Toast.makeText(ChangePhoneActivity.this, "更换成功", Toast.LENGTH_SHORT).show();
                    mCameraDialog.dismiss();
                    finish();
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
}
