package com.example.administrator.ding_small;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.ding_small.HelpTool.MD5Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static android.R.attr.password;
import static android.os.Build.VERSION_CODES.M;


public class MainActivity extends Activity implements  View.OnClickListener{
    private EditText user_name,user_password;
    public static final int SHOW_RESPONSE = 0;
    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    String Msg="";
    String resultStr="";
    String code;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.left).setOnClickListener(this);
        findViewById(R.id.right).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        user_name=findViewById(R.id.user_name);
        user_password=findViewById(R.id.user_password);

    }


    @Override
    protected void onDestroy() {
        if(token!=null){
            if(token.length()>0){
                System.out.println("缓存："+token);
                //储存token,备用
                SharedPreferences share = super.getSharedPreferences(tokeFile, MODE_PRIVATE);//实例化
                SharedPreferences.Editor editor = share.edit(); //使处于可编辑状态
                editor.putString("token", token);
                editor.commit();    //提交数据保存
            }
        }
        super.onDestroy();
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.left:
                findViewById(R.id.l_bottom).setBackgroundColor(getResources().getColor(R.color.green));
                findViewById(R.id.r_bottom).setBackgroundColor(getResources().getColor(R.color.white));
                findViewById(R.id.login_xml).setVisibility(View.VISIBLE);
                findViewById(R.id.register_xml).setVisibility(View.GONE);
                break;
            case R.id.right:
                findViewById(R.id.l_bottom).setBackgroundColor(getResources().getColor(R.color.white));
                findViewById(R.id.r_bottom).setBackgroundColor(getResources().getColor(R.color.green));
                findViewById(R.id.login_xml).setVisibility(View.GONE);
                findViewById(R.id.register_xml).setVisibility(View.VISIBLE);
                break;
            case R.id.login:
                String name=user_name.getText().toString().trim();
                String password=user_password.getText().toString().trim();
                System.out.println("用户密码："+name+":"+password);
                //判断是否为空
                if(name.equals("")|| password.equals("")){
                    new AlertDialog.Builder(MainActivity.this).setTitle("登录提示").setMessage("用户名或密码不能为空！！！").setPositiveButton("确定",null).show();
                }else {
//                    Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
                    //访问服务器
                    sendRequestWithHttpClient(name,MD5Utils.Md5(name,password));
                }
        }
    }
    //201701108登录验证类，获取手机号和密码
    //方法：发送网络请求，获取用户数据。在里面开启线程
    private void sendRequestWithHttpClient(final String name1, final String pass1) {
        System.out.println("加密密码："+MD5Utils.md5(pass1));
        new Thread(new Runnable() {

            @Override
            public void run() {
                //用HttpClient发送请求，分为五步
                //第一步：创建HttpClient对象
                 HttpClient httpCient = new DefaultHttpClient();
                //第二步：创建代表请求的对象,参数是访问的服务器地址blMac=12:34:56:78:9A:BC&userId=1001&userName=%E5%BC%A0%E4%B8%89
                HttpGet httpGet = new HttpGet("http://192.168.1.101:8080/appUser/appUserLogin.do?loginType=1&loginAccount="+name1+"&loginPwd="+pass1);
                try {
                    //第三步：执行请求，获取服务器发还的相应对象
                    HttpResponse httpResponse = httpCient.execute(httpGet);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    System.out.println("状态码："+httpResponse.getStatusLine().getStatusCode());
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
                        System.out.println("返回结果："+response);
                        if(response!=null){
                            //在子线程中将Message对象发出去
                            Message message = new Message();
                            message.what = SHOW_RESPONSE;
                            message.obj = response.toString();
                            handler.sendMessage(message);
                        }
                    }else{
                        Toast.makeText(MainActivity.this,"访问失败!!!请检查服务器...",Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println("访问失败！！！");
                    e.printStackTrace();
                }

            }
        }).start();//这个start()方法不要忘记了

    }
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    try {
                        JSONObject responseObject=new JSONObject(response);
                        System.out.println("返回："+responseObject);
                        Msg=responseObject.getString("msg");
                        code=responseObject.getString("resCode");
                        if(code.equals("00000")){
                            resultStr= responseObject.getString("result");
                            JSONObject resultObject=new JSONObject(resultStr);
                            token=resultObject.getString("token");
                            System.out.println("token:"+token);
                            Intent  intent=new Intent(MainActivity.this,AccountBookActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            //跳转
                            startActivity(intent);
                            finish();
                        }else{
                            new AlertDialog.Builder(MainActivity.this).setTitle("登录异常").setMessage(Msg).setPositiveButton("确定",null).show();
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
