package com.example.administrator.ding_small.PersonalCenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Switch;

import com.example.administrator.ding_small.Adapter.BindingCompanyAdapter;
import com.example.administrator.ding_small.Adapter.BindingCompanyAdapter.Callback;
import com.example.administrator.ding_small.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/7.
 */

public class BindingCompanyActivity extends Activity implements View.OnClickListener,Callback {
    private ListView companyView;
    private ArrayList<String> list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bind_company);
        findViewById(R.id.back).setOnClickListener(this);
        list=new ArrayList<String>();
        list.add("广州顶牛信息科技有限公司");
        list.add("广州大华家电维修部");
        list.add("广州凌飞冷气工程有限公司");
        companyView=findViewById(R.id.company_list);
        companyView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        companyView.setAdapter(new BindingCompanyAdapter(BindingCompanyActivity.this,list,this));
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.back:
            finish();
                break;
        }
    }

    @Override
    public void click(View v) {
            switch ((int)v.getTag()){
                case 0:
                    break;
            }
    }
}
