package com.example.administrator.ding_small.PersonalCenter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.ding_small.HelpTool.MD5Utils;
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

import static android.R.attr.data;

/**
 * Created by Administrator on 2017/12/27.
 */

public class EditPassWordActivity extends Activity implements View.OnClickListener{
    private EditText password1,c_password;
    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp=null;//定义储存源，备用
    String memid,token,sign,oldPass,newPass,ts;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_password);
        password1=findViewById(R.id.password1);
        c_password=findViewById(R.id.c_password);
        findViewById(R.id.edit_password).setOnClickListener(this);
        getCache();
    }
    private  void getCache() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        String url = "http://192.168.1.101:8080/a10/api/user/changePwd.do";
         ts = String.valueOf(new Date().getTime());
        System.out.println("首页：" + memid + "  ts:" + ts+ "  token:" + token);
        String Sign = url + memid + token + ts;
        System.out.println("Sign:"+Sign);
        sign = MD5Utils.md5(Sign);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_password:
                 oldPass=password1.getText().toString().trim();
                newPass=c_password.getText().toString().trim();
                new Thread(networkTask).start();//发送验证码
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
            String url = "http://192.168.1.101:8080/a10/api/user/changePwd.do?memId=" + memid + "&ts=" + ts;
            OkHttpClient okHttpClient = new OkHttpClient();
            String oldpass=MD5Utils.md5(oldPass);
            String newpass=MD5Utils.md5(newPass);
            System.out.println("验证："+sign);
            String b= "{\"pwdOrg\":\""+oldpass+"\",\"pwdNew\":\""+newpass+"\"}";//json字符串
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), b);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("sIgn",sign)
                    .build();
            System.out.println(request.headers());
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                String result=response.body().string();
                System.out.println("结果："+result+"状态码："+ response.code());
                //Toast.makeText(EditPassWordActivity.this,result,Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


}
