package com.example.administrator.ding_small.Label;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.administrator.ding_small.Adapter.EditLabelBtnAdapter.Callback;
import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.example.administrator.ding_small.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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

public class EditLabelBtnActivity extends Activity implements View.OnClickListener, Callback {
    public ListView list;
    private ArrayList<String> lists;
    public JSONArray jsonArray;

    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    String memid, token, sign, oldPass, newPass, ts, c_newPass;
    public static final int SHOW_RESPONSE = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_label_btn);
        list = findViewById(R.id.label_list);
        findViewById(R.id.add_label).setOnClickListener(this);

        getCache();
        jsonArray = new JSONArray();
        lists = new ArrayList<String>();
        lists.add("通用");
        lists.add("住房");
        lists.add("逛街");
        lists.add("买菜");
        lists.add("奖金");
        lists.add("学费");
        lists.add("工资");
        lists.add("房租");
        lists.add("零食");
        lists.add("夜宵");
        try {
            for (int i = 0; i < lists.size(); i++) {
                JSONObject jsonObject;
                jsonObject = new JSONObject();
                jsonObject.put("name", lists.get(i));
                jsonObject.put("number", "0");
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        list.setAdapter(new com.example.administrator.ding_small.Adapter.EditLabelBtnAdapter(EditLabelBtnActivity.this, jsonArray, this));

        list.setOnItemClickListener(new OnItemClickListener() {
            @RequiresApi(api = VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }


    private void getCache() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        String url = "http://192.168.start1.103:8080/app/secr6000/deleteSecr6000ByKey.do";
        ///app/secr9000lisSecr9000
        ts = String.valueOf(new Date().getTime());
        System.out.println("首页：" + memid + "  ts:" + ts + "  token:" + token);
        String Sign = url + memid + token + ts;
        System.out.println("Sign:" + Sign);
        sign = MD5Utils.md5(Sign);
        System.out.println("加密Sign:"+sign);
        new Thread(networkTask).start();//删除标签
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.add_label:
                intent = new Intent(EditLabelBtnActivity.this, AddLabelActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void click(View v) {
        switch (v.getId()) {
            case R.id.up_img:
                try {
                    JSONObject ob1 = new JSONObject(String.valueOf(jsonArray.get((Integer) v.getTag())));
                    JSONObject ob2 = new JSONObject(String.valueOf(jsonArray.get(1)));
                    ob1.put("number", "start1");
                    jsonArray.put(1, ob1);
                    jsonArray.put((Integer) v.getTag(), ob2);
                    System.out.println("更改后：" + ob1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                list.setAdapter(new com.example.administrator.ding_small.Adapter.EditLabelBtnAdapter(EditLabelBtnActivity.this, jsonArray, this));
                break;
            case R.id.edit_img:
                Intent intent = new Intent(EditLabelBtnActivity.this, EditLabelItemBtnActivity.class);
                String label = String.valueOf(v.getTag(R.id.tag_first));
                int index = (int) v.getTag(R.id.tag_second);
                Bundle bundle = new Bundle();
                bundle.putString("index", index + "");
                bundle.putString("label", label);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.label_img:

                break;
        }
    }

    /**
     * 网络操作相关的子线程okhttp框架 删除标签
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String url = "http://192.168.start1.103:8080/app/secr6000/deleteSecr6000ByKey.do?memId=" + memid + "&ts=" + ts;
            OkHttpClient okHttpClient = new OkHttpClient();
            System.out.println("验证：" + sign);
            String b = "{\"onlyId\" : \"start1\"}";//json字符串
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
