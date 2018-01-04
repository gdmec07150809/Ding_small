package com.example.administrator.ding_small;

/**
 * Created by Administrator on 2017/10/19.
 */
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.content.Intent;

import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.example.administrator.ding_small.PersonalCenter.PersonalCenterActivity;

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

import static com.example.administrator.ding_small.HelpTool.getContactInfo.getContactInfo;

public class ContactsActivity extends Activity implements ListView.OnClickListener{
    private ListView list;
    private static final String TAG = "ContactsActivity";
    private JSONObject contactData;//储存第一手信息
    private JSONObject contactDatas;//储存搜索结果
    private EditText search_text;//搜索框
    boolean isFlag=true;//用哪个JsonObject响应listVIEW点击事件
    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp=null;//定义储存源，备用
    private String memid,token,sign,ts;
    @Override
    protected void onCreate(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);
        list=findViewById(R.id.contacts_list);
        search_text=findViewById(R.id.search_edittext);
        findViewById(R.id.f_notepad).setOnClickListener(this);
        findViewById(R.id.f_account).setOnClickListener(this);
        findViewById(R.id.clean_text).setOnClickListener(this);
        findViewById(R.id.add).setOnClickListener(this);
        findViewById(R.id.search_btn).setOnClickListener(this);
        findViewById(R.id.f_center).setOnClickListener(this);
        findViewById(R.id.add_contacts).setOnClickListener(this);

        //获取手机联系人列表
        try {
            contactData=getContactInfo(ContactsActivity.this);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        list.setAdapter(new com.example.administrator.ding_small.Adapter.ContactAdapter(ContactsActivity.this, contactData));
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {//联系人列表点击事件
                Intent intent=new Intent(ContactsActivity.this,ContactsDetailActivity.class);
                String ss= null;
                String name="";
                try {
                    if(isFlag){
                        ss = contactData.getString("contact"+i);
                    }else {
                        ss = contactDatas.getString("contact"+i);
                    }
                    JSONObject obj=new JSONObject(ss);
                    name=obj.getString("lastname").substring(obj.getString("lastname").length()-1,obj.getString("lastname").length()).toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtra("name",name);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        getCache();//获取缓存值
    }

    private  void getCache(){
        //  new Thread(networkTask).start();
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        String url1 = "http://192.168.1.103:8080/a10/app/ppt6000/dataList.do";//测试链接,未通
        ts = String.valueOf(new Date().getTime());
        System.out.println("首页：" + memid + "  ts:" + ts +"  token:"+token);
        String Sign = url1 + memid + token + ts;
        sign = MD5Utils.md5(Sign);
        if(memid!=null){
            new Thread(networkTask).start();
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add://新建
                Intent intent=new Intent(ContactsActivity.this,NotepadActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.clean_text://清除输入框内容
                isFlag=true;
                search_text.setText("");
                list.setAdapter(new com.example.administrator.ding_small.Adapter.ContactAdapter(ContactsActivity.this, contactData));
                break;
            case R.id.search_btn://查询
                isFlag=false;
               String search_val=search_text.getText().toString();
                String ss= null;
                contactDatas=new JSONObject();;
                String name="";
                int index=0;
                try {
                    for(int i=0;i<contactData.length();i++){
                        ss = contactData.getString("contact"+i);
                        JSONObject obj=new JSONObject(ss);
                        name=obj.getString("lastname").substring(obj.getString("lastname").length()-1,obj.getString("lastname").length()).toString();
                        if(search_val.equals(name)){
                            System.out.println("键："+name);
                            System.out.println("值："+search_val);
                            contactDatas.put("contact"+index, obj);
                            index++;
                        }else if(search_val.equals(obj.getString("mobile"))){
                            contactDatas.put("contact"+index, obj);
                            index++;
                        }
                    }
                        list.setAdapter(new com.example.administrator.ding_small.Adapter.ContactAdapter(ContactsActivity.this, contactDatas));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.f_notepad://记事本
                Intent intent1=new Intent(ContactsActivity.this,NotepadBtnActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                break;
            case R.id.f_account://记账本
                Intent intent2=new Intent(ContactsActivity.this,AccountBookActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                break;
            case R.id.f_center://个人中心
                Intent intent3=new Intent(ContactsActivity.this,PersonalCenterActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent3);
                break;
            case R.id.add_contacts://添加联系人
                Intent intent4=new Intent(ContactsActivity.this,AddContactsActivity.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent4);
                break;
            default:
                break;
        }
    }

    /**
     * 网络操作相关的子线程okhttp框架  测试实例
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String url = "http://192.168.1.103:8080/a10/app/ppt6000/dataList.do?memId=" + memid + "&ts=" + ts;
            System.out.println("URL:"+url);
            OkHttpClient okHttpClient = new OkHttpClient();
            String b= "{\"eqpId\":\"198dbe33682548f4a92a864dca3ac5a9\"}";//json字符串
            System.out.println("json:"+b);
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
                System.out.println("结果："+response.body().string()+"状态码："+ response.code());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}
