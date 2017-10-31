package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/27.
 */

public class TitleActivity extends Activity implements View.OnClickListener{
    private ListView list;
    private ArrayList<String> lists;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title);
        list=findViewById(R.id.title_list);
        findViewById(R.id.received).setOnClickListener(this);
        findViewById(R.id.edit_title).setOnClickListener(this);
        findViewById(R.id.notepad).setOnClickListener(this);
        findViewById(R.id.payable).setOnClickListener(this);
        findViewById(R.id.add_title).setOnClickListener(this);
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
        list.setAdapter(new com.example.administrator.ding_small.Adapter.TitleAdapter(TitleActivity.this,lists));
    }

    @Override
    public void onClick(View view) {
    switch (view.getId()){
        case R.id.received:
            intent=new Intent(TitleActivity.this,ReceivedActivity.class);
            startActivity(intent);
            break;
        case R.id.payable:
            intent=new Intent(TitleActivity.this,PayableActivity.class);
            startActivity(intent);
            break;
        case R.id.notepad:
            intent=new Intent(TitleActivity.this,NotepadActivity.class);
            startActivity(intent);
            break;
        case R.id.add_title:
            intent=new Intent(TitleActivity.this,AddTitleActivity.class);
            startActivity(intent);
            break;
        case R.id.edit_title:
            intent=new Intent(TitleActivity.this,EditTitleActivity.class);
            startActivity(intent);
            break;
    }
    }
}
