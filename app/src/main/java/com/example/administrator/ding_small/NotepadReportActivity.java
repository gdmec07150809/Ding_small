package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ding_small.Adapter.NotepadReportAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/11/22.
 */

public class NotepadReportActivity extends Activity implements View.OnClickListener{
    private ArrayList<String> OutTimeLists;

    private ArrayList<String> lists;

    private ArrayList<String> yearLists;

    public JSONArray jsonArray;

    private ListView listView;

    private TextView title_text,label_text,finished_text,outtime_text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepad_select_report);
        listView=findViewById(R.id.notepad_report_listview);
        findViewById(R.id.f_title).setOnClickListener(this);
        findViewById(R.id.f_label).setOnClickListener(this);
        findViewById(R.id.f_finished).setOnClickListener(this);
        findViewById(R.id.f_outtime).setOnClickListener(this);
        title_text=findViewById(R.id.title_text);
        label_text=findViewById(R.id.label_text);
        finished_text=findViewById(R.id.finished_text);
        outtime_text=findViewById(R.id.outtime_text);

        title_text.setTextColor(getResources().getColor(R.color.blank));
        label_text.setTextColor(getResources().getColor(R.color.blank));
        finished_text.setTextColor(getResources().getColor(R.color.green));
        outtime_text.setTextColor(getResources().getColor(R.color.blank));

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
                jsonObject = new JSONObject();
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
                Intent intent=new Intent(NotepadReportActivity.this,NotepadAnalysisActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.f_title:
                intent=new Intent(NotepadReportActivity.this,NotepadAnalysisTitleStatisticsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_label:
                intent=new Intent(NotepadReportActivity.this,NotepadAnalysisLabelStatisticsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_outtime:
                intent=new Intent(NotepadReportActivity.this,NotepadOutTimeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
