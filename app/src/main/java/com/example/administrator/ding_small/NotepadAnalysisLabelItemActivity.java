package com.example.administrator.ding_small;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ding_small.Adapter.NotepadBtnAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.administrator.ding_small.HelpTool.JsonArrayBySort.sort;

/**
 * Created by CZK on 2017/11/29.
 */

public class NotepadAnalysisLabelItemActivity extends Activity {
    private ArrayList<String> date;//时间
    private ArrayList<String> explain;//详情
    private ArrayList<String> name;//执行人
    private ArrayList<String> title;//标题
    private ArrayList<String> label;//标签
    private ArrayList<String> time;//标签
    private int titleColor[];
    public JSONArray jsonArray;
    private ListView listView;
    private TextView acount_number, outtime_number, label_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepad_analysis_label_item);
        listView = findViewById(R.id.notepad_analysis_label_listview);
        acount_number = findViewById(R.id.acount_number);
        outtime_number = findViewById(R.id.outtime_number);
        label_text = findViewById(R.id.label_text);
        date = new ArrayList<String>();
        explain = new ArrayList<String>();
        name = new ArrayList<String>();
        title = new ArrayList<String>();
        label = new ArrayList<String>();
        titleColor = new int[]{R.color.green, R.color.fen, R.color.orange, R.color.green, R.color.fen, R.color.orange, R.color.bg1, R.color.bg2, R.color.bg3, R.color.bg4, R.color.bg5};
        time = new ArrayList<String>();
        date.add("2016-start4-5");
        date.add("2016-start4-6");
        date.add("2016-start4-7");
        date.add("2016-start4-7");
        date.add("2016-start4-8");
        date.add("2016-start4-9");
        date.add("2016-start4-6");
        date.add("2016-start4-7");
        date.add("2016-start4-7");
        date.add("2016-start4-8");
        date.add("2016-start4-9");
        explain.add("21元,20170922,到105告拉进来");
        explain.add("21元,20170922,到105告拉进来");
        explain.add("21元,20170922,到105告拉进来");
        explain.add("21元,20170922,到105告拉进来");
        explain.add("21元,20170922,到105告拉进来");
        explain.add("21元,20170922,到105告拉进来");
        explain.add("21元,20170922,到105告拉进来");
        explain.add("21元,20170922,到105告拉进来");
        explain.add("21元,20170922,到105告拉进来");
        explain.add("21元,20170922,到105告拉进来");
        explain.add("21元,20170922,到105告拉进来");
        name.add("张先生");
        name.add("张先生");
        name.add("张先生");
        name.add("张先生");
        name.add("张先生");
        name.add("张先生");
        name.add("张先生");
        name.add("张先生");
        name.add("张先生");
        name.add("张先生");
        name.add("张先生");
        title.add("上门维修");
        title.add("上门维修");
        title.add("工单指令");
        title.add("上门维修");
        title.add("工单指令");
        title.add("吃饭");
        title.add("逛街");
        title.add("打游戏");
        title.add("上门维修");
        title.add("上学");
        title.add("工作");
        label.add("2118-乐堡吸塑灯箱");
        label.add("2118-乐堡吸塑灯箱");
        label.add("2118-乐堡吸塑灯箱");
        label.add("2118-乐堡吸塑灯箱");
        label.add("2118-乐堡吸塑灯箱");
        label.add("2118-乐堡吸塑灯箱");
        label.add("2118-乐堡吸塑灯箱");
        label.add("2118-乐堡吸塑灯箱");
        label.add("2118-乐堡吸塑灯箱");
        label.add("2118-乐堡吸塑灯箱");
        label.add("2118-乐堡吸塑灯箱");
        time.add("10:30");
        time.add("start1:30");
        time.add("12:30");
        time.add("9:30");
        time.add("start4:30");
        time.add("19:30");
        time.add("22:30");
        time.add("11:30");
        time.add("8:30");
        time.add("23:30");
        time.add("15:30");
        jsonArray = new JSONArray();
        try {
            for (int i = 0; i < date.size(); i++) {
                JSONObject jsonObject;
                jsonObject = new JSONObject();
                jsonObject.put("date", date.get(i));
                jsonObject.put("explain", explain.get(i));
                jsonObject.put("name", name.get(i));
                jsonObject.put("title", title.get(i));
                jsonObject.put("label", label.get(i));
                jsonObject.put("time", time.get(i));
                jsonObject.put("titleColor", titleColor[i]);
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String label_str = getIntent().getStringExtra("label");
        label_text.setText(label_str);
        acount_number.setText("共" + jsonArray.length() + "条");
        listView.setAdapter(new NotepadBtnAdapter(NotepadAnalysisLabelItemActivity.this, sort(jsonArray, "date", true)));
    }
}
