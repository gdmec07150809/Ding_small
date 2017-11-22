package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.administrator.ding_small.Adapter.AnalysisAdapter;
import com.example.administrator.ding_small.Adapter.NotepadReportAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/22.
 */

public class NotepadReportActivity extends Activity {
    private ArrayList<String> OutTimeLists;
    private ArrayList<String> lists;
    private ArrayList<String> yearLists;
    public JSONArray jsonArray;
    private ListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepad_select_report);
        listView=findViewById(R.id.notepad_report_listview);
        jsonArray=new JSONArray();
        lists=new ArrayList<String>();
        yearLists=new ArrayList<String>();
        OutTimeLists=new ArrayList<String>();
        lists.add("12");
        lists.add("50");
        lists.add("23");
        lists.add("89");
        lists.add("45");

        OutTimeLists.add("112");
        OutTimeLists.add("10");
        OutTimeLists.add("33");
        OutTimeLists.add("19");
        OutTimeLists.add("55");


        yearLists.add("2017");
        yearLists.add("2016");
        yearLists.add("2015");
        yearLists.add("2014");
        yearLists.add("2013");
        try {
            for (int i=0;i<lists.size();i++){
                JSONObject jsonObject;
                jsonObject=new JSONObject();
                jsonObject.put("OnTime",lists.get(i));
                jsonObject.put("OutTime",OutTimeLists.get(i));
                jsonObject.put("year",yearLists.get(i));
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView.setAdapter(new NotepadReportAdapter(NotepadReportActivity.this,jsonArray));
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(NotepadReportActivity.this,AnalysisActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
