package com.example.administrator.ding_small.Label;

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
 * Created by CZK on 2017/11/16.
 */

public class EditLabelItemBtnActivity extends Activity implements View.OnClickListener {
    private TextView label;
    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    String memid, token, sign, oldPass, newPass, ts, c_newPass;
    public static final int SHOW_RESPONSE = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_label_activity);
        label = findViewById(R.id.label);
        findViewById(R.id.confirm).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);

        getCache();
        //获取传过来的值
        Bundle bundle = getIntent().getExtras();
        String t1 = bundle.getString("label");
        label.setText(t1);
    }

    private void getCache() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        String url = "http://192.168.1.103:8080/app/secr6000/updateSecr6000.do";
        ///app/secr9000lisSecr9000
        ts = String.valueOf(new Date().getTime());
        System.out.println("首页：" + memid + "  ts:" + ts + "  token:" + token);
        String Sign = url + memid + token + ts;
        System.out.println("Sign:" + Sign);
        sign = MD5Utils.md5(Sign);
        System.out.println("加密Sign:"+sign);
        new Thread(networkTask).start();//修改标题
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.confirm:
                intent = new Intent(EditLabelItemBtnActivity.this, EditLabelBtnActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.back:
                intent = new Intent(EditLabelItemBtnActivity.this, EditLabelBtnActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    /**
     * 网络操作相关的子线程okhttp框架  获取标题
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String url = "http://192.168.1.103:8080/app/secr6000/updateSecr6000.do?memId=" + memid + "&ts=" + ts;
            OkHttpClient okHttpClient = new OkHttpClient();
            System.out.println("验证：" + sign);
            String b = "{\"onlyId\":\"2\",\"proType\" : \"已收\",\"attType\" : \"标题\", \"name\" : \"早餐\",\"sort\" : \"1\"}";//json字符串
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
