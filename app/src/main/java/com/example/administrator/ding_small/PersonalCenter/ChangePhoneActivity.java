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
    String memid, token, sign, newPass, ts, phone_str,save_phone;
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
                String send_str=p_code.getText().toString().trim();
                if(phone_str.equals("")||send_str.equals("")){
                    if (Locale.getDefault().getLanguage().equals("en")){
                        new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("The replacement of mobile phone prompt").setMessage("Information can not be empty").setPositiveButton("confirm", null).show();
                    }else{
                        new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("更换手机提示").setMessage("信息不能为空").setPositiveButton("确定", null).show();
                    }

                }else{
                    setPhoneDialog();
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
                    if(!phone_str.equals(save_phone)){
                        if (Locale.getDefault().getLanguage().equals("en")){
                            new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("The replacement of mobile phone prompt").setMessage("You have to enter the old cell phone").setPositiveButton("confirm", null).show();
                        }else{
                            new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("更换手机提示").setMessage("必须输入旧手机").setPositiveButton("确定", null).show();
                        }
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
            String url = utils.url+"/api/user/getSmsMsg.do";
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

    //更换手机底部弹出菜单
    private void setPhoneDialog() {

        mCameraDialog = new Dialog(this, R.style.Dialog);
        root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.change_phone_dialog, null);
        Button new_login = root.findViewById(R.id.new_login);
        TextView send_text=root.findViewById(R.id.send_text);
        EditText newPhone_value=root.findViewById(R.id.newPhone_value);
        if(Locale.getDefault().getLanguage().equals("en")){
            newPhone_value.setHint("Please enter a new mobile phone");
            new_login.setText("binding");
        }
        final EditText editText = root.findViewById(R.id.newPhone_value);

        send_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (save_phone.equals(editText.getText().toString())) {
                    if (Locale.getDefault().getLanguage().equals("en")){
                        new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("Change the phone").setMessage("Can't be the same as the last cell phone").setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }else{
                        new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("更换手机").setMessage("不能跟上次绑定的手机一样").setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }

                }else{
                    TextView send_text=root.findViewById(R.id.send_text);
                    CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(send_text, 60000, 1000);
                    mCountDownTimerUtils.start();
                    new Thread(networkTask).start();//发送验证码
                }
            }
        });
        new_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断输入的手机号是否和上次一样
                if (save_phone.equals(editText.getText().toString())) {
                    if (Locale.getDefault().getLanguage().equals("en")){
                        new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("Change the phone").setMessage("Can't be the same as the last cell phone").setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }else{
                        new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("更换手机").setMessage("不能跟上次绑定的手机一样").setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }

                } else {
                    if (Locale.getDefault().getLanguage().equals("en")){
                        Toast.makeText(ChangePhoneActivity.this, "success", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ChangePhoneActivity.this, "更换成功", Toast.LENGTH_SHORT).show();
                    }

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
