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
 * Created by CZK on 2017/12/6.
 */

public class AccountScreenActiivity extends Activity implements View.OnClickListener {
    private FlowLayout inCome_flowlayout;
    private FlowLayout outCome_flowlayout;
    private FlowLayout label_flowlayout;
    private String[] inComeStrs = new String[]{"通用", "住房", "逛街", "买菜", "奖金", "学费", "工资", "房租", "零食", "夜宵"};
    private String[] labelStrs = new String[]{"通用", "住房", "逛街", "买菜", "奖金", "学费", "工资", "房租", "零食", "夜宵"};
    private String[] outComeStrs = new String[]{"通用", "住房", "逛街", "买菜", "奖金", "学费", "工资", "房租", "零食", "夜宵"};
    private ArrayList<String> inComeList = new ArrayList<String>();
    private ArrayList<String> outComeList = new ArrayList<String>();
    private ArrayList<String> labelList = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_screen);
        findViewById(R.id.reset).setOnClickListener(this);
        inComeFlowLayout();
        outComeflowlayout();
        labelFlowLayout();
    }

    //支出布局方法
    private void inComeFlowLayout() {
        if (inCome_flowlayout == null) {
            //加载搜索记录
            for (int i = 0; i < inComeStrs.length; i++) {
                final TextView text = new TextView(AccountScreenActiivity.this);
                System.out.println("数组：" + inComeStrs[i]);
                text.setText(inComeStrs[i]);//添加内容
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
                inCome_flowlayout = findViewById(R.id.income_flowlayout);
                inCome_flowlayout.addView(text);//将内容添加到布局中
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//添加点击事件
                        // search_text.setText(text.getText().toString());

                        if (inComeList.contains(text.getText().toString())) {
                            inComeList.remove(text.getText().toString());
                            view.setBackgroundResource(R.drawable.light_button_back);
                            text.setTextColor(getResources().getColor(R.color.blank));
                        } else {
                            view.setBackgroundResource(R.drawable.green_button_back);
                            text.setTextColor(getResources().getColor(R.color.white));
                            inComeList.add(text.getText().toString());
                        }
                        //Toast.makeText(NotepadScreenActivity.this, text.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    //收入布局方法
    private void outComeflowlayout() {
        if (outCome_flowlayout == null) {
            //加载搜索记录
            for (int i = 0; i < outComeStrs.length; i++) {
                final TextView text = new TextView(AccountScreenActiivity.this);
                System.out.println("数组：" + outComeStrs[i]);
                text.setText(outComeStrs[i]);//添加内容
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
                outCome_flowlayout = findViewById(R.id.outcome_flowlayout);
                outCome_flowlayout.addView(text);//将内容添加到布局中
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//添加点击事件

                        if (outComeList.contains(text.getText().toString())) {
                            outComeList.remove(text.getText().toString());
                            view.setBackgroundResource(R.drawable.light_button_back);
                            text.setTextColor(getResources().getColor(R.color.blank));
                        } else {
                            view.setBackgroundResource(R.drawable.green_button_back);
                            text.setTextColor(getResources().getColor(R.color.white));
                            outComeList.add(text.getText().toString());
                        }
                        // Toast.makeText(NotepadScreenActivity.this, text.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }

    //标签布局方法
    private void labelFlowLayout() {
        if (label_flowlayout == null) {
            //加载搜索记录
            for (int i = 0; i < labelStrs.length; i++) {
                final TextView text = new TextView(AccountScreenActiivity.this);
                System.out.println("数组：" + labelStrs[i]);
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

                        if (labelList.contains(text.getText().toString())) {
                            labelList.remove(text.getText().toString());
                            view.setBackgroundResource(R.drawable.light_button_back);
                            text.setTextColor(getResources().getColor(R.color.blank));
                        } else {
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
        switch (view.getId()) {
            case R.id.reset:
                inCome_flowlayout.removeAllViews();
                outCome_flowlayout.removeAllViews();
                label_flowlayout.removeAllViews();
                inCome_flowlayout = null;
                outCome_flowlayout = null;
                label_flowlayout = null;
                outComeList = new ArrayList<String>();
                inComeList = new ArrayList<String>();
                labelList = new ArrayList<String>();
                inComeFlowLayout();
                outComeflowlayout();
                labelFlowLayout();
                break;
            default:
                break;
        }
    }
}
