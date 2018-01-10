package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.ding_small.HelpTool.FlowLayout;

import java.util.ArrayList;


/**
 * Created by CZK on 2017/11/22.
 */

public class NotepadScreenActivity extends Activity implements View.OnClickListener{
    private FlowLayout title_flowlayout;
    private FlowLayout label_flowlayout;
    private String[] titleStrs = new String[]{"通用", "住房", "逛街", "买菜", "奖金", "学费", "工资", "房租", "零食", "夜宵"};
    private String[] labelStrs = new String[]{"通用", "住房", "逛街", "买菜", "奖金", "学费", "工资", "房租", "零食", "夜宵"};
    private ArrayList<String> labelList=new ArrayList<String>();
    private ArrayList<String> titleList=new ArrayList<String>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepad_screen);
        findViewById(R.id.reset).setOnClickListener(this);
        titleFlowLayout();
        labelFlowLayout();
    }
    //标题布局方法
    private void titleFlowLayout() {
        if(title_flowlayout==null){
            //加载搜索记录
            for (int i = 0; i < titleStrs.length; i++) {
                final TextView text = new TextView(NotepadScreenActivity.this);
                System.out.println("数组："+titleStrs[i]);
                text.setText(titleStrs[i]);//添加内容
                text.setTextSize(12);
                text.setTextColor(getResources().getColor(R.color.blank));
                text.setBackgroundResource(R.drawable.light_button_back);
                text.setPadding(15, 10, 15, 10);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);//设置宽高,第一个参数是宽,第二个参数是高
                //设置边距
                params.topMargin = 5;
                params.bottomMargin = 5;
                params.leftMargin = 8;
                params.rightMargin = 8;
                text.setLayoutParams(params);
                title_flowlayout = findViewById(R.id.title_flowlayout);
                title_flowlayout.addView(text);//将内容添加到布局中
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//添加点击事件
                       // search_text.setText(text.getText().toString());

                        if(titleList.contains(text.getText().toString())){
                            titleList.remove(text.getText().toString());
                            view.setBackgroundResource(R.drawable.light_button_back);
                            text.setTextColor(getResources().getColor(R.color.blank));
                        }else {
                            view.setBackgroundResource(R.drawable.green_button_back);
                            text.setTextColor(getResources().getColor(R.color.white));
                            titleList.add(text.getText().toString());
                        }
                       //Toast.makeText(NotepadScreenActivity.this, text.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    //标签布局方法
    private void labelFlowLayout() {
        if(label_flowlayout==null){
            //加载搜索记录
            for (int i = 0; i < labelStrs.length; i++) {
                final TextView text = new TextView(NotepadScreenActivity.this);
                System.out.println("数组："+labelStrs[i]);
                    text.setText(labelStrs[i]);//添加内容
                    text.setTextSize(12);
                    text.setTextColor(getResources().getColor(R.color.blank));
                    text.setBackgroundResource(R.drawable.light_button_back);
                    text.setPadding(15, 10, 15, 10);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);//设置宽高,第一个参数是宽,第二个参数是高
                //设置边距
                params.topMargin = 5;
                params.bottomMargin = 5;
                params.leftMargin = 8;
                params.rightMargin = 8;
                text.setLayoutParams(params);
                label_flowlayout = findViewById(R.id.label_flowlayout);
                label_flowlayout.addView(text);//将内容添加到布局中
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//添加点击事件

                            if(labelList.contains(text.getText().toString())){
                                labelList.remove(text.getText().toString());
                                view.setBackgroundResource(R.drawable.light_button_back);
                                text.setTextColor(getResources().getColor(R.color.blank));
                            }else {
                                view.setBackgroundResource(R.drawable.green_button_back);
                                text.setTextColor(getResources().getColor(R.color.white));
                                labelList.add(text.getText().toString());
                            }
                      // Toast.makeText(NotepadScreenActivity.this, text.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.reset:
                    label_flowlayout.removeAllViews();
                    title_flowlayout.removeAllViews();
                    label_flowlayout=null;
                    title_flowlayout=null;
                    labelList=new ArrayList<String>();
                    titleList=new ArrayList<String>();
                    labelFlowLayout();
                    titleFlowLayout();
                break;
            default:
                break;
        }
    }
}
