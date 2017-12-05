package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.example.administrator.ding_small.Adapter.AnalysisAdapter;
import com.example.administrator.ding_small.Adapter.NotepadAnalysisTitleAdapter;
import com.example.administrator.ding_small.Adapter.NotepadReturnByMonthAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by Administrator on 2017/11/21.
 */

public class AccountBookAnalysisActivity extends Activity implements View.OnClickListener{
    private ColumnChartView mColumnChartCc;
    private ColumnChartData data;
    private ArrayList<String> OutTimeLists;
    private ArrayList<String> lists;
    public JSONArray jsonArray;
    private ListView listView;
    private Button received_btn,yet_btn,all_btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_analysis);
        listView=findViewById(R.id.analasis_list);
        received_btn= findViewById(R.id.received_btn);
        yet_btn= findViewById(R.id.yet_btn);
        all_btn= findViewById(R.id.all_btn);
        received_btn.setOnClickListener(this);
        yet_btn.setOnClickListener(this);
        all_btn.setOnClickListener(this);

        mColumnChartCc = (ColumnChartView) findViewById(R.id.column_chart_cc);
        mColumnChartCc.setOnValueTouchListener(new ValueTouchListener());
        generateStackedData();

        jsonArray=new JSONArray();
        lists=new ArrayList<String>();
        OutTimeLists=new ArrayList<String>();
        lists.add("12");
        lists.add("50");
        lists.add("23");
        lists.add("89");
        lists.add("45");
        lists.add("1");
        lists.add("21");
        lists.add("15");
        lists.add("16");
        lists.add("45");
        lists.add("116");
        lists.add("40");

        OutTimeLists.add("112");
        OutTimeLists.add("10");
        OutTimeLists.add("33");
        OutTimeLists.add("19");
        OutTimeLists.add("55");
        OutTimeLists.add("12");
        OutTimeLists.add("81");
        OutTimeLists.add("16");
        OutTimeLists.add("12");
        OutTimeLists.add("44");
        OutTimeLists.add("56");
        OutTimeLists.add("78");

        try {
            for (int i=0;i<lists.size();i++){
                JSONObject jsonObject;
                jsonObject=new JSONObject();
                jsonObject.put("OnTime",lists.get(i));
                jsonObject.put("OutTime",OutTimeLists.get(i));
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView.setAdapter(new AnalysisAdapter(AccountBookAnalysisActivity.this,jsonArray));
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(AccountBookAnalysisActivity.this, AccountReturnByMonthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    //绘制柱形图
    private void generateStackedData() {

        int numSubcolumns = 1;
        int numColumns = 12;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        Float[] floats = {320f, 105f, 150f, 115f,520f, 15f, 50f, 125f,720f, 195f, 520f, 115f};//总数数据
        Float[] lists = {420f, 10f, 50f, 15f,20f, 150f, 500f, 25f,120f, 95f, 220f, 115f};//按期数据
        Float[] outTimes = {20f, 110f, 510f, 105f,210f, 10f, 100f, 215f,12f, 915f, 205f, 15f};//逾期数据
        String[] selecedNames = {"1月", "2月", "3月", "4月","5月","6月","7月","8月","9月","10月","11月","12月"};

        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<SubcolumnValue>();
            values.add(new SubcolumnValue(outTimes[i], Color.parseColor("#F36F24")));
            values.add(new SubcolumnValue(floats[i], Color.parseColor("#303F9F")));
            values.add(new SubcolumnValue(lists[i], Color.parseColor("#6AB845")));
            axisValues.add(new AxisValue(i).setLabel(selecedNames[i]));
            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }
        data = new ColumnChartData(columns);
        data.setAxisXBottom(new Axis(axisValues));
        data.setAxisYLeft(new Axis());
        mColumnChartCc.setColumnChartData(data);

        // Set value touch listener that will trigger changes for chartTop.
//        mColumnChartCc.setOnValueTouchListener(new ValueTouchListener());

        // Set selection mode to keep selected month column highlighted.
        mColumnChartCc.setValueSelectionEnabled(true);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.received_btn:
                received_btn.setTextColor(getResources().getColor(R.color.white));
                all_btn.setTextColor(getResources().getColor(R.color.green));
                yet_btn.setTextColor(getResources().getColor(R.color.green));

                received_btn.setBackgroundColor(getResources().getColor(R.color.green));
                all_btn.setBackgroundResource(R.drawable.bg_gray);
                yet_btn.setBackgroundResource(R.drawable.bg_gray);
                break;
            case R.id.all_btn:
                received_btn.setTextColor(getResources().getColor(R.color.green));
                all_btn.setTextColor(getResources().getColor(R.color.white));
                yet_btn.setTextColor(getResources().getColor(R.color.green));

                received_btn.setBackgroundResource(R.drawable.bg_gray);
                all_btn.setBackgroundColor(getResources().getColor(R.color.green));
                yet_btn.setBackgroundResource(R.drawable.bg_gray);
                break;
            case R.id.yet_btn:
                received_btn.setTextColor(getResources().getColor(R.color.green));
                all_btn.setTextColor(getResources().getColor(R.color.green));
                yet_btn.setTextColor(getResources().getColor(R.color.white));

                received_btn.setBackgroundResource(R.drawable.bg_gray);
                all_btn.setBackgroundResource(R.drawable.bg_gray);
                yet_btn.setBackgroundColor(getResources().getColor(R.color.green));
                break;
        }
    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {
        @Override
        public void onValueSelected(int i, int i1, SubcolumnValue value) {
            // Toast.makeText(ColumnChartActivity.this, "Selected: " + value, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onValueDeselected() {

        }
    }
}
