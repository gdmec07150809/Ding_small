package com.example.administrator.ding_small.PersonalCenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity;
import com.example.administrator.ding_small.MainLayoutActivity;
import com.example.administrator.ding_small.R;

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

import static android.R.attr.data;

/**
 * Created by CZK on 2017/12/27.
 */

public class EditPassWordActivity extends Activity implements View.OnClickListener{
    private EditText password1,c_password,c_new_password;
    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp=null;//定义储存源，备用
    String memid,token,sign,oldPass,newPass,ts,c_newPass;
    public static final int SHOW_RESPONSE = 0;

    //更改语言所要更改的控件
    private TextView back_text,edit_password_text,old_password_text,new_password_text,confirm_text,edit_password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_edit_password);
        password1=findViewById(R.id.password1);
        c_password=findViewById(R.id.c_password);
        c_new_password=findViewById(R.id.c_new_password);
        edit_password=findViewById(R.id.edit_password);
        edit_password.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);

        back_text=findViewById(R.id.back_text);
        edit_password_text=findViewById(R.id.edit_password_text);
        old_password_text=findViewById(R.id.old_password_text);
        new_password_text=findViewById(R.id.new_password_text);
        confirm_text=findViewById(R.id.confirm_text);

        changeTextView();//更换语言
        getCache();
    }
    private void changeTextView(){
        if(Locale.getDefault().getLanguage().equals("en")){
            back_text.setText("Back");
            edit_password_text.setText("Edit PassWord");

            old_password_text.setText("Old");
            new_password_text.setText("New");
            confirm_text.setText("Confirm");

            password1.setHint("Enter Old PassWord");
            c_password.setHint("Enter New PassWord");
            c_new_password.setHint("Please Reconfirm The PassWord");
            edit_password.setText("Save");
        }
    }
    private  void getCache() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        String url = "http://120.76.188.131:8080/a10/api/user/changePwd.do";
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
                c_newPass=c_new_password.getText().toString().trim();
                if(oldPass.equals("")||newPass.equals("")||c_newPass.equals("")){
                    new AlertDialog.Builder(EditPassWordActivity.this).setTitle("修改提示").setMessage("信息不能为空！！").setPositiveButton("确定",null).show();
                }else if(oldPass.equals(newPass)){
                    new AlertDialog.Builder(EditPassWordActivity.this).setTitle("修改提示").setMessage("新密码不能跟旧密码一样！！").setPositiveButton("确定",null).show();
                }else if(!newPass.equals(c_newPass)){
                    new AlertDialog.Builder(EditPassWordActivity.this).setTitle("修改提示").setMessage("新密码不相等,请重新输入！！").setPositiveButton("确定",null).show();
                }else{
                    new Thread(networkTask).start();//发送验证码
                }
                break;
            case R.id.back:
                finish();
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
            String url = "http://120.76.188.131:8080/a10/api/user/changePwd.do?memId=" + memid + "&ts=" + ts;
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
                if(response!=null){
                    //在子线程中将Message对象发出去
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    message.obj = result.toString();
                    EditPasswordHandler.sendMessage(message);
                }
                System.out.println("结果："+result+"状态码："+ response.code());
                //Toast.makeText(EditPassWordActivity.this,result,Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    private Handler EditPasswordHandler = new Handler() {

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
                            //{"meta":{"res":"99999","msg":"用户名或密码有误"},"data":null}状态码：200
                                if(object1.getString("res").equals("00000")){
                                    new AlertDialog.Builder(EditPassWordActivity.this).setTitle("修改密码").setMessage("修改成功,返回首页").setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent=new Intent(EditPassWordActivity.this,MainLayoutActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).show();
                                }else{
                                    new AlertDialog.Builder(EditPassWordActivity.this).setTitle("修改密码").setMessage(object1.getString("msg")).setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
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

}
