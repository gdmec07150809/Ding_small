package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ding_small.Adapter.AccountInComeItemDetailAdapter;
import com.example.administrator.ding_small.Adapter.AccountOutTimeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.id;

/**
 * Created by CZK on 2017/12/11.
 */

public class AccountOutTimeActivity extends Activity implements View.OnClickListener{
    private TextView date_text,title_text,money_text,r_wait;
    private ImageView date_jiantou,title_jiantou,money_jiantou;
    private Button already_btn,not_btn;
    private ListView listView;
    private TextView summary_text,label_text,income_text,expenditure_text,outtime_text;

    private String[] strs = {"美的", "格力", "飞利浦", "方太", "西门子", "A.O.史密斯", "爱马仕", "奔腾", "TCL", "小天鹅", "三洋"};

    private int[] money={12,54,-50,20,-40,45,-16,56,78,-123,12};
    private int [] color={R.color.bg1,R.color.bg2,R.color.bg3,R.color.bg4,R.color.bg5,R.color.bg6,R.color.bg7,R.color.bg8,R.color.bg9,R.color.bg10,R.color.bg11};
    private int sum;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_outtime);
        date_text=findViewById(R.id.time);
        title_text=findViewById(R.id.title);
        money_text=findViewById(R.id.money);
        date_jiantou=findViewById(R.id.date_down);
        title_jiantou=findViewById(R.id.title_down);
        money_jiantou=findViewById(R.id.money_down);
        r_wait=findViewById(R.id.r_wait);

        listView=findViewById(R.id.accout_outtime_list);

        already_btn=findViewById(R.id.already_btn);
        not_btn=findViewById(R.id.not_btn);
        already_btn.setOnClickListener(this);
        not_btn.setOnClickListener(this);

        findViewById(R.id.date_layout).setOnClickListener(this);
        findViewById(R.id.title_layout).setOnClickListener(this);
        findViewById(R.id.money_layout).setOnClickListener(this);

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

        label_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
        summary_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
        income_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
        expenditure_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
        outtime_text.setTextColor(ContextCompat.getColor(this, R.color.green));
        CreatJson();//组合一个jsonArray备用
        listView.setAdapter(new AccountOutTimeAdapter(AccountOutTimeActivity.this,jsonArray,false));
    }
    private void CreatJson(){
        jsonArray=new JSONArray();
        for(int i=0;i<color.length;i++){
            jsonObject=new JSONObject();
            try {
                jsonObject.put("color",color[i]);
                jsonObject.put("title",strs[i]);
                jsonObject.put("money",money[i]);
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.date_layout:
                date_text.setTextColor(ContextCompat.getColor(this, R.color.orange));
                money_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                title_text.setTextColor(ContextCompat.getColor(this, R.color.blank));

                date_jiantou.setImageResource(R.drawable.or_butoom_jiaotou);
                title_jiantou.setImageResource(R.drawable.butoom_jiantou);
                money_jiantou.setImageResource(R.drawable.butoom_jiantou);
                listView.setAdapter(new AccountOutTimeAdapter(AccountOutTimeActivity.this,jsonArray,false));
                break;
            case R.id.title_layout:
                date_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                money_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                title_text.setTextColor(ContextCompat.getColor(this, R.color.orange));

                date_jiantou.setImageResource(R.drawable.butoom_jiantou);
                title_jiantou.setImageResource(R.drawable.or_butoom_jiaotou);
                money_jiantou.setImageResource(R.drawable.butoom_jiantou);
                listView.setAdapter(new AccountOutTimeAdapter(AccountOutTimeActivity.this,jsonArray,true));
                break;
            case R.id.money_layout:
                date_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                money_text.setTextColor(ContextCompat.getColor(this, R.color.orange));
                title_text.setTextColor(ContextCompat.getColor(this, R.color.blank));

                date_jiantou.setImageResource(R.drawable.butoom_jiantou);
                title_jiantou.setImageResource(R.drawable.butoom_jiantou);
                money_jiantou.setImageResource(R.drawable.or_butoom_jiaotou);

                listView.setAdapter(new AccountOutTimeAdapter(AccountOutTimeActivity.this,jsonArray,true));
                break;
            case R.id.already_btn:
                already_btn.setBackgroundColor(Color.parseColor("#6AB845"));
                already_btn.setTextColor(Color.parseColor("#FFFFFF"));
                not_btn.setBackgroundResource(R.drawable.bg_gray);
                not_btn.setTextColor(Color.parseColor("#6AB845"));
                r_wait.setText("待收 800");
                break;
            case R.id.not_btn:
                already_btn.setBackgroundResource(R.drawable.bg_gray);
                already_btn.setTextColor(Color.parseColor("#6AB845"));
                not_btn.setBackgroundColor(Color.parseColor("#6AB845"));
                not_btn.setTextColor(Color.parseColor("#FFFFFF"));
                r_wait.setText("待付 800");
                break;
            case R.id.f_summary:
                intent=new Intent(AccountOutTimeActivity.this,AccountBookReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_label:
                intent=new Intent(AccountOutTimeActivity.this,AccountAnalysisLabelStatisticsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_expenditure:
                intent=new Intent(AccountOutTimeActivity.this,ExpenditureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_income:
                intent=new Intent(AccountOutTimeActivity.this,InComeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
