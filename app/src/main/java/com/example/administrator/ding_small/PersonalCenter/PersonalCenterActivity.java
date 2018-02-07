package com.example.administrator.ding_small.PersonalCenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ding_small.AccountBookActivity;
import com.example.administrator.ding_small.ContactsActivity;
import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity;
import com.example.administrator.ding_small.MainLayoutActivity;
import com.example.administrator.ding_small.NewMainLayoutActivity;
import com.example.administrator.ding_small.NotepadActivity;
import com.example.administrator.ding_small.NotepadBtnActivity;
import com.example.administrator.ding_small.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by CZK on 2017/11/6.
 */

public class PersonalCenterActivity extends Activity implements View.OnClickListener {
    private ImageView f_account, f_contacts, f_center, f_notepad,head1;
    //更改语言所要更改的控件
    private TextView security_text, setting_text, about_text, custom_text, feedback_text, home_text, my_text,name_text;
    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    String memid, token, UserSign, oldPass, newPass, ts, c_newPass,sign,nick,imgUrl;
    public static final int SHOW_RESPONSE = 0;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 100;
    Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_personal_center);
        findViewById(R.id.perfect).setOnClickListener(this);
        //      findViewById(R.id.instructions).setOnClickListener(this);
        findViewById(R.id.about).setOnClickListener(this);
        findViewById(R.id.setting).setOnClickListener(this);
        findViewById(R.id.security).setOnClickListener(this);
        findViewById(R.id.home_layout).setOnClickListener(this);

        security_text = findViewById(R.id.security_text);
        setting_text = findViewById(R.id.setting_text);
        about_text = findViewById(R.id.about_text);
        custom_text = findViewById(R.id.custom_text);
        feedback_text = findViewById(R.id.feedback_text);
        home_text = findViewById(R.id.home_text);
        my_text = findViewById(R.id.my_text);
        name_text=findViewById(R.id.name_text);
        head1=findViewById(R.id.head1);
        changeTextView();//更改语言
        getCacheUser();//获取缓存
    }

    private void getCacheUser() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        nick = sp.getString("nameStr", "");
       imgUrl = sp.getString("imgUrl",null);
        //imgUrl="https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1517382173&di=26a2bf5e76ab8b80075729896093b7ac&src=http://image.tianjimedia.com/uploadImages/2015/215/41/M68709LC8O6L.jpg";
        if(imgUrl!=null&&!imgUrl.equals("")&&!imgUrl.equals("null")){
            returnBitMap(imgUrl);//获取网络图片，并转化为Bitmap格式  设备图片
        }
        name_text.setText(nick);

    }
    private void changeTextView() {
        if (Locale.getDefault().getLanguage().equals("en")) {
            security_text.setText("Account Security");
            setting_text.setText("Setting");
            about_text.setText("About Us");
            custom_text.setText("Contact Customer Service");
            feedback_text.setText("Feedback");
            home_text.setText("Home");
            my_text.setText("My");
        }
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.perfect:
                intent = new Intent(PersonalCenterActivity.this, PersonalCenterPerfectActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.setting:
                intent = new Intent(PersonalCenterActivity.this, SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.security:
                intent = new Intent(PersonalCenterActivity.this, AccountSecurityActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.home_layout:
                intent = new Intent(PersonalCenterActivity.this, NewMainLayoutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
    public Bitmap returnBitMap(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;
                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection)imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.setConnectTimeout(10000);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    Message message = new Message();
                    message.what = 0;
                    DeviceImgHandler.sendMessage(message);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        return bitmap;
    }
    private Handler DeviceImgHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Drawable drawable = new BitmapDrawable(bitmap);
                    head1.setBackground(drawable);
                    break;
                default:
                    break;
            }
        }
    };
}
