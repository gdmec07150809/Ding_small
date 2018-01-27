package com.example.administrator.ding_small.PersonalCenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity;
import com.example.administrator.ding_small.R;

import java.util.Locale;

/**
 * Created by CZK on 2017/11/7.
 */

public class SettingActivity extends Activity implements View.OnClickListener {

    //更换语言所要更改的控件
    private TextView lang_text, back_text, setting_text, search_interval_text, language_text, next,time_text;
    private Dialog mCameraDialog;
    public static final int SHOW_RESPONSE = 0;
    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    String memId, tokEn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
//        findViewById(R.id.come_statistics).setOnClickListener(this);
//        findViewById(R.id.come_budget).setOnClickListener(this);
//        findViewById(R.id.come_data_lock).setOnClickListener(this);
        lang_text = findViewById(R.id.lang_text);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.comfir_btn).setOnClickListener(this);
        back_text = findViewById(R.id.back_text);
        setting_text = findViewById(R.id.setting_text);
        search_interval_text = findViewById(R.id.search_interval_text);
        language_text = findViewById(R.id.language_text);
        time_text=findViewById(R.id.time_text);
        time_text.setOnClickListener(this);
        next = findViewById(R.id.next);
        getTimeCache();//获取时间间隔缓存
        if (Locale.getDefault().getLanguage().equals("en")) {
            lang_text.setText("English");
            back_text.setText("Back");
            setting_text.setText("Setting");
            search_interval_text.setText("Search Interval");
            language_text.setText("Language");
            next.setText("Save");
        }
    }


    private void getTimeCache() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        time_text.setText(sp.getString("time", "30"));
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
//            case R.id.come_statistics:
//                intent=new Intent(SettingActivity.this,SatisticsSettingActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                break;
//            case R.id.come_budget:
//                intent=new Intent(SettingActivity.this,BudgetSettingActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                break;
//            case R.id.come_data_lock:
//                intent=new Intent(SettingActivity.this,DataLockActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                break;
            case R.id.time_text:
                setNichNameDialog();//弹出时间间隔
                break;
            case R.id.back:
                finish();
                break;
            case R.id.comfir_btn:
                String time_str=time_text.getText().toString().trim();

                sp = SettingActivity.this.getSharedPreferences(tokeFile, MODE_PRIVATE);//实例化
                SharedPreferences.Editor editor = sp.edit(); //使处于可编辑状态
                editor.putString("time", time_str);
                editor.commit();    //提交数据保存

                intent = new Intent(SettingActivity.this, PersonalCenterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    //时间间隔底部弹出菜单
    private void setNichNameDialog() {
        LinearLayout root = null;
        mCameraDialog = new Dialog(this, R.style.Dialog);
        root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.change_name_layout, null);
        Button new_login = root.findViewById(R.id.new_login);

        final EditText editText = root.findViewById(R.id.nickname_value);
        editText.setText(time_text.getText().toString());
        new_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time_text.setText(editText.getText().toString());
                mCameraDialog.dismiss();
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
