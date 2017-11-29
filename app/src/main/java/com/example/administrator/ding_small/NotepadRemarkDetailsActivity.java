package com.example.administrator.ding_small;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.administrator.ding_small.Adapter.NotepadRemarkDetailsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/28.
 */

public class NotepadRemarkDetailsActivity extends Activity{
    private ListView listView;
    private ArrayList<String> date;
    private ArrayList<String> explain;
    private ArrayList<String> money;
    public JSONArray jsonArray;
    private ScrollView scrollview;
    private Handler handler;
    private String inName,inDate,inTime,inTitle,inLabel,inExplain,inTitleColor;
    //private int inTitleColor;
    private TextView name1_view,title_view,name2_view,label_view,date_view,explain_view,name3_view,time_view;
    private LinearLayout finishing_layout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepad_item_details);
        listView=findViewById(R.id.record_list);
        scrollview= findViewById(R.id.scrollview);
        name1_view=findViewById(R.id.name);
        title_view=findViewById(R.id.title);
        name2_view=findViewById(R.id.user_name);
        label_view=findViewById(R.id.label);
        date_view=findViewById(R.id.date);
        explain_view=findViewById(R.id.explain);
        name3_view=findViewById(R.id.max_name);
        time_view=findViewById(R.id.time);
        finishing_layout=findViewById(R.id.finishing_layout);

        handler = new Handler();
        handler.postDelayed(runnable, 100);

        jsonArray=new JSONArray();
        date=new ArrayList<String>();
        explain=new ArrayList<String>();
        money=new ArrayList<String>();
        date.add("2016-10-02  10:02");
        date.add("2016-10-21  14:02");
        date.add("2016-11-01  9:02");
        date.add("2016-11-15  10:02");
        date.add("2016-11-20  13:12");
        date.add("2016-11-30  15:02");
        date.add("2016-12-02  7:02");
        date.add("2016-12-30  13:02");

        explain.add("录入初始值：");
        explain.add("收款：");
        explain.add("付款：");
        explain.add("付款：");
        explain.add("付款：");
        explain.add("收款：");
        explain.add("付款：");
        explain.add("收款：");

        money.add("200");
        money.add("10");
        money.add("20");
        money.add("120");
        money.add("25");
        money.add("210");
        money.add("15");
        money.add("45");

        try {
            for (int i=0;i<date.size();i++){
                JSONObject jsonObject;
                jsonObject=new JSONObject();
                jsonObject.put("date",date.get(i));
                jsonObject.put("explain",explain.get(i));
                jsonObject.put("money",money.get(i));
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView.setAdapter(new NotepadRemarkDetailsAdapter(NotepadRemarkDetailsActivity.this,jsonArray));
        setListViewHeightBasedOnChildren(listView);
        getBundlevalue();
    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            scrollview.fullScroll(ScrollView.FOCUS_UP);
        }
    };


    /*获取页面传过来的值*/
    private void getBundlevalue(){
        Bundle getStringValue=this.getIntent().getExtras();
        if(getStringValue.getString("date")!=null&&getStringValue.getString("time")!=null){
            inDate=getStringValue.getString("date");
            inTime=getStringValue.getString("time");
            date_view.setText(inDate+"  "+inTime);
            time_view.setText(inDate+"  "+inTime);
        }
        if(getStringValue.getString("title")!=null){
            inTitle=getStringValue.getString("title");
            title_view.setText(inTitle);
        }
       if(getStringValue.getString("label")!=null){
           inLabel=getStringValue.getString("label");
           label_view.setText(inLabel);
       }
        if(getStringValue.getString("explain")!=null){
            inExplain=getStringValue.getString("explain");
            label_view.setText(inLabel);
            explain_view.setText(inExplain);
        }
        if(getStringValue.getString("name")!=null){
            inName=getStringValue.getString("name");
            name1_view.setText(inName);
            name2_view.setText(inName);
            name3_view.setText(inName);
        }
      if(getStringValue.getString("titleColor")!=null){
          inTitleColor= getStringValue.getString("titleColor");
          finishing_layout.setBackgroundResource(Integer.parseInt(inTitleColor));
      }
    }
}
