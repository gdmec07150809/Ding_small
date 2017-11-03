package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.example.administrator.ding_small.Adapter.NotepadBtnAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/3.
 */

public class NotepadBtnActivity extends Activity implements View.OnClickListener{
    private ListView listView;
    private ArrayList<String> arrayList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepad_btn_activity);
        listView=findViewById(R.id.notepad_list);
        findViewById(R.id.add).setOnClickListener(this);
        findViewById(R.id.search).setOnClickListener(this);
        arrayList=new ArrayList<String>();
        for (int i=0;i<10;i++){
            arrayList.add(i+"");
        }
        listView.setAdapter(new NotepadBtnAdapter(NotepadBtnActivity.this,arrayList));
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.add:
                intent=new Intent(NotepadBtnActivity.this,NotepadActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.search:
                intent=new Intent(NotepadBtnActivity.this,SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}
