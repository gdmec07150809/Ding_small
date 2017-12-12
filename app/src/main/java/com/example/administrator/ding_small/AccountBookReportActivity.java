package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.ding_small.Adapter.AccountBookReportAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.administrator.ding_small.R.id.finished_text;
import static com.example.administrator.ding_small.R.id.outtime_text;
import static com.example.administrator.ding_small.R.id.title_text;

/**
 * Created by Administrator on 2017/12/4.
 */

public class AccountBookReportActivity extends Activity implements View.OnClickListener {
    private ArrayList<String> inComeLists;
    private ArrayList<String> expenditTureList;
    private ArrayList<String> yearLists;
    public JSONArray jsonArray;
    private ListView listView;
    private TextView summary_text,label_text,income_text,expenditure_text,outtime_text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountbook_select_report);
        listView=findViewById(R.id.account_report_listview);
        findViewById(R.id.f_summary).setOnClickListener(this);
        findViewById(R.id.f_label).setOnClickListener(this);
        findViewById(R.id.f_income).setOnClickListener(this);
        findViewById(R.id.f_expenditure).setOnClickListener(this);
        findViewById(R.id.f_outtime).setOnClickListener(this);
        summary_text=findViewById(R.id.summary_text);
        label_text=findViewById(R.id.label_text);
        income_text=findViewById(R.id.income_text);
        expenditure_text=findViewById(R.id.expenditure_text);
        outtime_text=findViewById(R.id.outtime_text);

        summary_text.setTextColor(getResources().getColor(R.color.green));
        label_text.setTextColor(getResources().getColor(R.color.blank));
        income_text.setTextColor(getResources().getColor(R.color.blank));
        outtime_text.setTextColor(getResources().getColor(R.color.blank));
        expenditure_text.setTextColor(getResources().getColor(R.color.blank));

        jsonArray=new JSONArray();
        expenditTureList=new ArrayList<String>();
        yearLists=new ArrayList<String>();
        inComeLists=new ArrayList<String>();
        expenditTureList.add("12");
        expenditTureList.add("50");
        expenditTureList.add("23");
        expenditTureList.add("89");
        expenditTureList.add("45");

        inComeLists.add("112");
        inComeLists.add("10");
        inComeLists.add("33");
        inComeLists.add("19");
        inComeLists.add("55");


        yearLists.add("2017");
        yearLists.add("2016");
        yearLists.add("2015");
        yearLists.add("2014");
        yearLists.add("2013");
        try {
            for (int i=0;i<expenditTureList.size();i++){
                JSONObject jsonObject;
                jsonObject=new JSONObject();
                jsonObject.put("expenditTure",expenditTureList.get(i));
                jsonObject.put("inCome",inComeLists.get(i));
                jsonObject.put("year",yearLists.get(i));
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView.setAdapter(new AccountBookReportAdapter(AccountBookReportActivity.this,jsonArray));
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(AccountBookReportActivity.this,AccountBookAnalysisActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.f_income:
                intent=new Intent(AccountBookReportActivity.this,InComeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_label:
                intent=new Intent(AccountBookReportActivity.this,AccountAnalysisLabelStatisticsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_expenditure:
                intent=new Intent(AccountBookReportActivity.this,ExpenditureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_outtime:
                intent=new Intent(AccountBookReportActivity.this,AccountOutTimeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}
