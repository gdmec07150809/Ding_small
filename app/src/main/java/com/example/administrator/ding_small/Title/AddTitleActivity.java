package com.example.administrator.ding_small.Title;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.example.administrator.ding_small.R;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by CZK on 2017/10/28.
 */

public class AddTitleActivity extends Activity implements View.OnClickListener {
    private TextView title_img;

    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    String memid, token, sign, oldPass, newPass, ts, c_newPass;
    public static final int SHOW_RESPONSE = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_title);
        title_img = findViewById(R.id.title_img);

        getCache();
        findViewById(R.id.c1).setOnClickListener(this);
        findViewById(R.id.c2).setOnClickListener(this);
        findViewById(R.id.c3).setOnClickListener(this);
        findViewById(R.id.c4).setOnClickListener(this);
        findViewById(R.id.c5).setOnClickListener(this);
        findViewById(R.id.c6).setOnClickListener(this);
        findViewById(R.id.c7).setOnClickListener(this);
        findViewById(R.id.c8).setOnClickListener(this);
        findViewById(R.id.c9).setOnClickListener(this);
        findViewById(R.id.c10).setOnClickListener(this);
        findViewById(R.id.c11).setOnClickListener(this);
        findViewById(R.id.c12).setOnClickListener(this);
        findViewById(R.id.c13).setOnClickListener(this);
        findViewById(R.id.c14).setOnClickListener(this);
        findViewById(R.id.c15).setOnClickListener(this);
        findViewById(R.id.c16).setOnClickListener(this);
        findViewById(R.id.c17).setOnClickListener(this);
        findViewById(R.id.c18).setOnClickListener(this);
        findViewById(R.id.confirm).setOnClickListener(this);
    }

    private void getCache() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        String url = "http://192.168.1.103:8080/app/secr6000/insertSecr6000.do";
        ///app/secr9000lisSecr9000
        ts = String.valueOf(new Date().getTime());
        System.out.println("首页：" + memid + "  ts:" + ts + "  token:" + token);
        String Sign = url + memid + token + ts;
        System.out.println("Sign:" + Sign);
        sign = MD5Utils.md5(Sign);
        System.out.println("加密Sign:"+sign);
        new Thread(networkTask).start();//添加标题
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.c1:
                title_img.setBackgroundResource(R.drawable.c1_bg);
                break;
            case R.id.c2:
                title_img.setBackgroundResource(R.drawable.c2_bg);
                break;
            case R.id.c3:
                title_img.setBackgroundResource(R.drawable.c3_bg);
                break;
            case R.id.c4:
                title_img.setBackgroundResource(R.drawable.c4_bg);
                break;
            case R.id.c5:
                title_img.setBackgroundResource(R.drawable.c5_bg);
                break;
            case R.id.c6:
                title_img.setBackgroundResource(R.drawable.c6_bg);
                break;
            case R.id.c7:
                title_img.setBackgroundResource(R.drawable.c7_bg);
                break;
            case R.id.c8:
                title_img.setBackgroundResource(R.drawable.c8_bg);
                break;
            case R.id.c9:
                title_img.setBackgroundResource(R.drawable.c9_bg);
                break;
            case R.id.c10:
                title_img.setBackgroundResource(R.drawable.c10_bg);
                break;
            case R.id.c11:
                title_img.setBackgroundResource(R.drawable.c11_bg);
                break;
            case R.id.c12:
                title_img.setBackgroundResource(R.drawable.c12_bg);
                break;
            case R.id.c13:
                title_img.setBackgroundResource(R.drawable.c13_bg);
                break;
            case R.id.c14:
                title_img.setBackgroundResource(R.drawable.c14_bg);
                break;
            case R.id.c15:
                title_img.setBackgroundResource(R.drawable.c15_bg);
                break;
            case R.id.c16:
                title_img.setBackgroundResource(R.drawable.c16_bg);
                break;
            case R.id.c17:
                title_img.setBackgroundResource(R.drawable.c17_bg);
                break;
            case R.id.c18:
                title_img.setBackgroundResource(R.drawable.c18_bg);
                break;

            case R.id.confirm:
                intent = new Intent(AddTitleActivity.this, EditTitleActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    /**
     * 网络操作相关的子线程okhttp框架  添加标题
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String url = "http://192.168.1.103:8080/app/secr6000/insertSecr6000.do?memId=" + memid + "&ts=" + ts;
            OkHttpClient okHttpClient = new OkHttpClient();
            System.out.println("验证：" + sign);
            String b = "{\"proType\":\"已收\",\"opId\":\"34e6af429fff444f911611fa2c9f5ecd\",\"attType\":\"标题\",\"name\":\"早餐\",\"sort\":\"1\"}";//json字符串
            System.out.println(b);
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
                    // getDeviceListHandler.sendMessage(message);
                }
                System.out.println("结果：" + result + "状态码：" + response.code());
                //Toast.makeText(EditPassWordActivity.this,result,Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}
