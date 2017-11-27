package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ding_small.Adapter.NotepadAnalysisTitleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.administrator.ding_small.R.id.ge;

/**
 * Created by Administrator on 2017/11/23.
 */

public class NotepadAnalysisTitleStatisticsActivity extends Activity implements View.OnClickListener{
    private ArrayList<String> finishedLists;
    private ArrayList<String> nofinishlists;
    private ArrayList<String> titleLists;
    public JSONArray jsonArray;
    private ListView listView;
    private TextView title_text,label_text,finished_text,outtime_text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepad_analysis_title_statistics);
        listView=findViewById(R.id.notepad_analysis_title_listview);

        findViewById(R.id.f_title).setOnClickListener(this);
        findViewById(R.id.f_label).setOnClickListener(this);
        findViewById(R.id.f_finished).setOnClickListener(this);
        findViewById(R.id.f_outtime).setOnClickListener(this);
        title_text=findViewById(R.id.title_text);
        label_text=findViewById(R.id.label_text);
        finished_text=findViewById(R.id.finished_text);
        outtime_text=findViewById(R.id.outtime_text);

        title_text.setTextColor(getResources().getColor(R.color.green));
        label_text.setTextColor(getResources().getColor(R.color.blank));
        finished_text.setTextColor(getResources().getColor(R.color.blank));
        outtime_text.setTextColor(getResources().getColor(R.color.blank));


        jsonArray=new JSONArray();
        nofinishlists=new ArrayList<String>();
        finishedLists=new ArrayList<String>();
        titleLists=new ArrayList<String>();

        nofinishlists.add("12");
        nofinishlists.add("50");
        nofinishlists.add("23");
        nofinishlists.add("89");
        nofinishlists.add("45");

        finishedLists.add("112");
        finishedLists.add("10");
        finishedLists.add("33");
        finishedLists.add("19");
        finishedLists.add("55");


        titleLists.add("上课");
        titleLists.add("旅游");
        titleLists.add("逛街");
        titleLists.add("背诵");
        titleLists.add("开会");
        try {
            for (int i=0;i<titleLists.size();i++){
                JSONObject jsonObject;
                jsonObject=new JSONObject();
                jsonObject.put("nofinish",nofinishlists.get(i));
                jsonObject.put("finished",finishedLists.get(i));
                jsonObject.put("title",titleLists.get(i));
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView.setAdapter(new NotepadAnalysisTitleAdapter(NotepadAnalysisTitleStatisticsActivity.this,jsonArray));
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.f_finished:
                intent=new Intent(NotepadAnalysisTitleStatisticsActivity.this,NotepadReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_label:
                intent=new Intent(NotepadAnalysisTitleStatisticsActivity.this,NotepadAnalysisLabelStatisticsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_outtime:
                intent=new Intent(NotepadAnalysisTitleStatisticsActivity.this,NotepadOutTimeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}
