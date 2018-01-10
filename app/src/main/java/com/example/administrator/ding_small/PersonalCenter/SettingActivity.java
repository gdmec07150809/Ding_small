package com.example.administrator.ding_small.PersonalCenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.ding_small.R;

import java.util.Locale;

/**
 * Created by CZK on 2017/11/7.
 */

public class SettingActivity extends Activity implements View.OnClickListener{

    //更换语言所要更改的控件
    private TextView lang_text,back_text,setting_text,search_interval_text,language_text,next;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
//        findViewById(R.id.come_statistics).setOnClickListener(this);
//        findViewById(R.id.come_budget).setOnClickListener(this);
//        findViewById(R.id.come_data_lock).setOnClickListener(this);
        lang_text=findViewById(R.id.lang_text);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.comfir_btn).setOnClickListener(this);
        back_text=findViewById(R.id.back_text);
        setting_text=findViewById(R.id.setting_text);
        search_interval_text=findViewById(R.id.search_interval_text);
        language_text=findViewById(R.id.language_text);
        next=findViewById(R.id.next);
        if(Locale.getDefault().getLanguage().equals("en")){
            lang_text.setText("English");
            back_text.setText("Back");
            setting_text.setText("Setting");
            search_interval_text.setText("Search Interval");
            language_text.setText("Language");
            next.setText("Save");
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
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
            case R.id.back:
                finish();
                break;
            case R.id.comfir_btn:
                intent=new Intent(SettingActivity.this,PersonalCenterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}
