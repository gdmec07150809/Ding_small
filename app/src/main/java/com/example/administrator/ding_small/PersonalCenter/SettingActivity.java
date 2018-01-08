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
 * Created by Administrator on 2017/11/7.
 */

public class SettingActivity extends Activity implements View.OnClickListener{
    private TextView lang_text;
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

        if(Locale.getDefault().getLanguage().equals("en")){
            lang_text.setText("English");
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
