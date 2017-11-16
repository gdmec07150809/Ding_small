package com.example.administrator.ding_small.Label;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.example.administrator.ding_small.Adapter.EditLabelAdapter.Callback;
import com.example.administrator.ding_small.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/31.
 */

public class EditLabelActivity extends Activity  implements View.OnClickListener,Callback {
    private ListView list;
    private ArrayList<String> lists;
    public JSONArray jsonArray;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_label);
        //初始化控件
        list=findViewById(R.id.title_list);
        findViewById(R.id.add_label).setOnClickListener(this);
        findViewById(R.id.edit_label).setOnClickListener(this);
        jsonArray=new JSONArray();
        lists=new ArrayList<String>();
        lists.add("通用");
        lists.add("住房");
        lists.add("逛街");
        lists.add("买菜");
        lists.add("奖金");
        lists.add("学费");
        lists.add("工资");
        lists.add("房租");
        lists.add("零食");
        lists.add("夜宵");

        try {
            for (int i=0;i<lists.size();i++){
                JSONObject jsonObject;
                jsonObject=new JSONObject();
                jsonObject.put("name",lists.get(i));
                jsonObject.put("number","0");
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        list.setAdapter(new com.example.administrator.ding_small.Adapter.EditLabelAdapter(EditLabelActivity.this,jsonArray,this));
    }

    @Override
    public void onClick(View view) {
        Intent intent;
            switch (view.getId()){
                case R.id.add_label:
                    intent=new Intent(EditLabelActivity.this,AddLabelActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                case R.id.edit_label:
                    intent=new Intent(EditLabelActivity.this,EditLabelBtnActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
            }
    }

    @Override
    public void click(View v) {

    }
}
