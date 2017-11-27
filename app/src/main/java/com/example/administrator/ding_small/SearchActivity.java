package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ding_small.Adapter.SearchAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/3.
 */

public class SearchActivity extends Activity implements View.OnClickListener{
    private ListView listView;
    private ArrayList<String> arrayList;
    private Button finished_btn,no_finish_btn;
    private boolean isTitle=false;
    private TextView time,title;
    private ImageView butoom_jiantou,or_butoom_jiantou;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        listView=findViewById(R.id.search_list);
        finished_btn=findViewById(R.id.finished_btn);
        no_finish_btn=findViewById(R.id.no_finish_btn);
        butoom_jiantou=findViewById(R.id.butoom_jiantou);
        or_butoom_jiantou=findViewById(R.id.or_butoom_jiantou);
        time=findViewById(R.id.time);
        title=findViewById(R.id.title);
        time.setOnClickListener(this);
        title.setOnClickListener(this);
        finished_btn.setOnClickListener(this);
        no_finish_btn.setOnClickListener(this);
        arrayList=new ArrayList<String>();
        for (int i=0;i<10;i++){
            arrayList.add(i+"");
        }
        listView.setAdapter(new SearchAdapter(SearchActivity.this,arrayList,isTitle));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finished_btn:
                finished_btn.setBackgroundColor(Color.parseColor("#6AB845"));
                finished_btn.setTextColor(Color.parseColor("#FFFFFF"));
                no_finish_btn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                no_finish_btn.setTextColor(Color.parseColor("#6AB845"));
                listView.setAdapter(new SearchAdapter(SearchActivity.this,arrayList,isTitle));
                break;
            case R.id.no_finish_btn:
                finished_btn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                finished_btn.setTextColor(Color.parseColor("#6AB845"));
                no_finish_btn.setBackgroundColor(Color.parseColor("#6AB845"));
                no_finish_btn.setTextColor(Color.parseColor("#FFFFFF"));
                listView.setAdapter(new SearchAdapter(SearchActivity.this,arrayList,isTitle));
                break;
            case R.id.time:
                isTitle=false;
                time.setTextColor(Color.parseColor("#F36F24"));
                title.setTextColor(Color.parseColor("#000000"));
                or_butoom_jiantou.setImageResource(R.drawable.or_butoom_jiaotou);
                butoom_jiantou.setImageResource(R.drawable.butoom_jiantou);
                listView.setAdapter(new SearchAdapter(SearchActivity.this,arrayList,isTitle));
                break;
            case R.id.title:
                isTitle=true;
                time.setTextColor(Color.parseColor("#000000"));
                title.setTextColor(Color.parseColor("#F36F24"));
                or_butoom_jiantou.setImageResource(R.drawable.butoom_jiantou);
                butoom_jiantou.setImageResource(R.drawable.or_butoom_jiaotou);
                listView.setAdapter(new SearchAdapter(SearchActivity.this,arrayList,isTitle));
                break;

        }
    }
}
