package com.example.administrator.ding_small.Title;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.ding_small.R;

/**
 * Created by CZK on 2017/10/28.
 */

public class AddTitleActivity extends Activity implements View.OnClickListener {
    private TextView title_img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_title);
        title_img = findViewById(R.id.title_img);
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
        findViewById(R.id.confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.c1:
                title_img.setBackgroundResource(R.drawable.c1_bg);
                break;
            case R.id.c2:
                title_img.setBackgroundResource(R.drawable.c2_bg);
                break;
            case R.id.c3:
                title_img.setBackgroundResource(R.drawable.c3_bg);
                break;
            case R.id.c4:
                title_img.setBackgroundResource(R.drawable.c4_bg);
                break;
            case R.id.c5:
                title_img.setBackgroundResource(R.drawable.c5_bg);
                break;
            case R.id.c6:
                title_img.setBackgroundResource(R.drawable.c6_bg);
                break;
            case R.id.c7:
                title_img.setBackgroundResource(R.drawable.c7_bg);
                break;
            case R.id.c8:
                title_img.setBackgroundResource(R.drawable.c8_bg);
                break;
            case R.id.c9:
                title_img.setBackgroundResource(R.drawable.c9_bg);
                break;
            case R.id.c10:
                title_img.setBackgroundResource(R.drawable.c10_bg);
                break;
            case R.id.c11:
                title_img.setBackgroundResource(R.drawable.c11_bg);
                break;
            case R.id.c12:
                title_img.setBackgroundResource(R.drawable.c12_bg);
                break;
            case R.id.c13:
                title_img.setBackgroundResource(R.drawable.c13_bg);
                break;
            case R.id.c14:
                title_img.setBackgroundResource(R.drawable.c14_bg);
                break;
            case R.id.c15:
                title_img.setBackgroundResource(R.drawable.c15_bg);
                break;
            case R.id.c16:
                title_img.setBackgroundResource(R.drawable.c16_bg);
                break;
            case R.id.c17:
                title_img.setBackgroundResource(R.drawable.c17_bg);
                break;
            case R.id.c18:
                title_img.setBackgroundResource(R.drawable.c18_bg);
                break;

            case R.id.confirm:
                intent = new Intent(AddTitleActivity.this, EditTitleActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
