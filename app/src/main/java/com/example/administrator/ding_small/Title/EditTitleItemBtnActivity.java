package com.example.administrator.ding_small.Title;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.ding_small.R;

import static java.nio.file.Paths.get;

/**
 * Created by CZK on 2017/10/31.
 */

public class EditTitleItemBtnActivity extends Activity implements View.OnClickListener{
    private EditText title;
    private TextView title_img;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_title_item_btn);
        //初始化控件
        findViewById(R.id.confirm).setOnClickListener(this);
        title=findViewById(R.id.title);
        title_img=findViewById(R.id.title_img);
        findViewById(R.id.c1).setOnClickListener(this);
        findViewById(R.id.c2).setOnClickListener(this);
        findViewById(R.id.c3).setOnClickListener(this);
        findViewById(R.id.c4).setOnClickListener(this);
        findViewById(R.id.c5).setOnClickListener(this);
        findViewById(R.id.c6).setOnClickListener(this);
        findViewById(R.id.c7).setOnClickListener(this);
        findViewById(R.id.c8).setOnClickListener(this);
        findViewById(R.id.c9).setOnClickListener(this);
        findViewById(R.id.c10).setOnClickListener(this);
        findViewById(R.id.c11).setOnClickListener(this);
        findViewById(R.id.c12).setOnClickListener(this);
        findViewById(R.id.c13).setOnClickListener(this);
        findViewById(R.id.c14).setOnClickListener(this);
        findViewById(R.id.c15).setOnClickListener(this);
        findViewById(R.id.c16).setOnClickListener(this);
        findViewById(R.id.c17).setOnClickListener(this);
        findViewById(R.id.c18).setOnClickListener(this);

        //获取传过来的值
        Bundle bundle=getIntent().getExtras();
        String t1=bundle.getString("title");
        int index= Integer.parseInt(bundle.getString("index"));
        title.setText(t1);

        //设置对应背景
        switch (index){
            case 1:
                title_img.setBackgroundResource(R.drawable.c1_bg);
                break;
            case 2:
                title_img.setBackgroundResource(R.drawable.c2_bg);
                break;
            case 3:
                title_img.setBackgroundResource(R.drawable.c3_bg);
                break;
            case 4:
                title_img.setBackgroundResource(R.drawable.c4_bg);
                break;
            case 5:
                title_img.setBackgroundResource(R.drawable.c5_bg);
                break;
            case 6:
                title_img.setBackgroundResource(R.drawable.c6_bg);
                break;
            case 7:
                title_img.setBackgroundResource(R.drawable.c7_bg);
                break;
            case 8:
                title_img.setBackgroundResource(R.drawable.c8_bg);
                break;
            case 9:
                title_img.setBackgroundResource(R.drawable.c9_bg);
                break;
            case 10:
                title_img.setBackgroundResource(R.drawable.c10_bg);
                break;
        }

    }

    //添加控件点击事件
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.c1:title_img.setBackgroundResource(R.drawable.c1_bg);break;
            case R.id.c2: title_img.setBackgroundResource(R.drawable.c2_bg);break;
            case R.id.c3: title_img.setBackgroundResource(R.drawable.c3_bg);break;
            case R.id.c4: title_img.setBackgroundResource(R.drawable.c4_bg);break;
            case R.id.c5: title_img.setBackgroundResource(R.drawable.c5_bg);break;
            case R.id.c6: title_img.setBackgroundResource(R.drawable.c6_bg);break;
            case R.id.c7: title_img.setBackgroundResource(R.drawable.c7_bg);break;
            case R.id.c8: title_img.setBackgroundResource(R.drawable.c8_bg);break;
            case R.id.c9: title_img.setBackgroundResource(R.drawable.c9_bg);break;
            case R.id.c10: title_img.setBackgroundResource(R.drawable.c10_bg);break;
            case R.id.c11: title_img.setBackgroundResource(R.drawable.c11_bg);break;
            case R.id.c12: title_img.setBackgroundResource(R.drawable.c12_bg);break;
            case R.id.c13: title_img.setBackgroundResource(R.drawable.c13_bg);break;
            case R.id.c14: title_img.setBackgroundResource(R.drawable.c14_bg);break;
            case R.id.c15: title_img.setBackgroundResource(R.drawable.c3_bg);break;
            case R.id.c16: title_img.setBackgroundResource(R.drawable.c4_bg);break;
            case R.id.c17: title_img.setBackgroundResource(R.drawable.c5_bg);break;
            case R.id.c18: title_img.setBackgroundResource(R.drawable.c6_bg);break;

            case R.id.confirm:
                intent=new Intent(EditTitleItemBtnActivity.this,EditTitleActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
