package com.example.administrator.ding_small.Label;

import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.administrator.ding_small.Adapter.EditLabelBtnAdapter.Callback;
import com.example.administrator.ding_small.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/16.
 */

public class EditLabelBtnActivity extends Activity implements View.OnClickListener,Callback {
    public ListView list;
    private ArrayList<String> lists;
    public JSONArray jsonArray;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_label_btn);
        list=findViewById(R.id.label_list);
        findViewById(R.id.add_label).setOnClickListener(this);
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

        list.setAdapter(new com.example.administrator.ding_small.Adapter.EditLabelBtnAdapter(EditLabelBtnActivity.this,jsonArray,this));

        list.setOnItemClickListener(new OnItemClickListener() {
            @RequiresApi(api = VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.add_label:
                intent=new Intent(EditLabelBtnActivity.this,AddLabelActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void click(View v) {
        switch (v.getId()){
            case R.id.up_img:
                try {
                    JSONObject ob1= new JSONObject(String.valueOf(jsonArray.get((Integer) v.getTag())));
                    JSONObject ob2= new JSONObject(String.valueOf(jsonArray.get(1)));
                    ob1.put("number","1");
                    jsonArray.put(1,ob1);
                    jsonArray.put((Integer) v.getTag(),ob2);
                    System.out.println("更改后："+ob1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                list.setAdapter(new com.example.administrator.ding_small.Adapter.EditLabelBtnAdapter(EditLabelBtnActivity.this,jsonArray,this));
                break;
            case R.id.edit_img:
                Intent intent=new Intent(EditLabelBtnActivity.this,EditLabelItemBtnActivity.class);
                String label= String.valueOf(v.getTag(R.id.tag_first));
                int index= (int) v.getTag(R.id.tag_second);
                Bundle bundle=new Bundle();
                bundle.putString("index",index+"");
                bundle.putString("label",label);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.label_img:

                break;
        }
    }
}
