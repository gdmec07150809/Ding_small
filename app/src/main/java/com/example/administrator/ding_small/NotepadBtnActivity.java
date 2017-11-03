package com.example.administrator.ding_small;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.example.administrator.ding_small.Adapter.NotepadBtnAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/3.
 */

public class NotepadBtnActivity extends Activity {
    private ListView listView;
    private ArrayList<String> arrayList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepad_btn_activity);
        listView=findViewById(R.id.notepad_list);
        arrayList=new ArrayList<String>();
        for (int i=0;i<10;i++){
            arrayList.add(i+"");
        }
        listView.setAdapter(new NotepadBtnAdapter(NotepadBtnActivity.this,arrayList));
    }
}
