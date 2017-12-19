package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ding_small.Adapter.AccountAnalysisLabelAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/11.
 */

public class AccountAnalysisLabelStatisticsActivity extends Activity implements View.OnClickListener{
    private ArrayList<String> labelLists;//标签
    private ArrayList<String> numberLists;//笔数
    private ArrayList<String> outComeLists;//支出
    private ArrayList<String> inComeLists;//收入
    public JSONArray jsonArray;
    private ListView listView;
    private TextView summary_text,label_text,income_text,expenditure_text,outtime_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_analysis_label_statistics);
        listView=findViewById(R.id.account_analysis_label_listview);
        summary_text=findViewById(R.id.summary_text);
        label_text=findViewById(R.id.label_text);
        income_text=findViewById(R.id.income_text);
        expenditure_text=findViewById(R.id.expenditure_text);
        outtime_text=findViewById(R.id.outtime_text);

        findViewById(R.id.f_summary).setOnClickListener(this);
        findViewById(R.id.f_label).setOnClickListener(this);
        findViewById(R.id.f_income).setOnClickListener(this);
        findViewById(R.id.f_expenditure).setOnClickListener(this);
        findViewById(R.id.f_outtime).setOnClickListener(this);

        summary_text.setTextColor(getResources().getColor(R.color.blank));
        income_text.setTextColor(getResources().getColor(R.color.blank));
        label_text.setTextColor(getResources().getColor(R.color.green));
        outtime_text.setTextColor(getResources().getColor(R.color.blank));
        expenditure_text.setTextColor(getResources().getColor(R.color.blank));
        CreatJson();//构造json备用
        listView.setAdapter(new AccountAnalysisLabelAdapter(AccountAnalysisLabelStatisticsActivity.this,jsonArray));
    }
    private void CreatJson(){
        jsonArray=new JSONArray();
        labelLists=new ArrayList<String>();
        numberLists=new ArrayList<String>();
        outComeLists=new ArrayList<String>();
        inComeLists=new ArrayList<String>();

        numberLists.add("12");
        numberLists.add("50");
        numberLists.add("23");
        numberLists.add("89");
        numberLists.add("45");

        outComeLists.add("112");
        outComeLists.add("10");
        outComeLists.add("33");
        outComeLists.add("19");
        outComeLists.add("55");

        inComeLists.add("1112");
        inComeLists.add("-101");
        inComeLists.add("323");
        inComeLists.add("129");
        inComeLists.add("-515");


        labelLists.add("上课");
        labelLists.add("旅游");
        labelLists.add("逛街");
        labelLists.add("背诵");
        labelLists.add("开会");
        try {
            for (int i=0;i<labelLists.size();i++){
                JSONObject jsonObject;
                jsonObject=new JSONObject();
                jsonObject.put("label",labelLists.get(i));
                jsonObject.put("number",numberLists.get(i));
                jsonObject.put("outCome",outComeLists.get(i));
                jsonObject.put("inCome",inComeLists.get(i));
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.f_income:
                intent=new Intent(AccountAnalysisLabelStatisticsActivity.this,InComeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_summary:
                intent=new Intent(AccountAnalysisLabelStatisticsActivity.this,AccountAnalysisLabelStatisticsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_expenditure:
                intent=new Intent(AccountAnalysisLabelStatisticsActivity.this,ExpenditureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_outtime:
                intent=new Intent(AccountAnalysisLabelStatisticsActivity.this,AccountOutTimeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
