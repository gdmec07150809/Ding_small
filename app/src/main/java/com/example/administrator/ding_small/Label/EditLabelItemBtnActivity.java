package com.example.administrator.ding_small.Label;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.ding_small.R;

/**
 * Created by CZK on 2017/11/16.
 */

public class EditLabelItemBtnActivity extends Activity implements View.OnClickListener{
    private TextView label;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_label_activity);
        label=findViewById(R.id.label);
        findViewById(R.id.confirm).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        //获取传过来的值
        Bundle bundle=getIntent().getExtras();
        String t1=bundle.getString("label");
        label.setText(t1);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.confirm:
                intent=new Intent(EditLabelItemBtnActivity.this,EditLabelBtnActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.back:
                intent=new Intent(EditLabelItemBtnActivity.this,EditLabelBtnActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
