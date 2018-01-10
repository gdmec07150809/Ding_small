package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ding_small.Adapter.AccountInComeItemDetailAdapter;

import java.util.ArrayList;


/**
 * Created by CZK on 2017/12/9.
 */

public class AccountInComeItemDetailActivity extends Activity implements View.OnClickListener {
    private ListView acount_income_item_detail_list;
    private ArrayList<String> arrayList;
    private String title;
    private int color, number;
    private TextView number_text, big_title, collect, paid;
    private TextView summary_text, label_text, income_text, expenditure_text, outtime_text, time, money;
    private Button received_btn, all_btn, yet_btn;
    private ImageView date_down, money_down;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_income_item_detail);
        acount_income_item_detail_list = findViewById(R.id.acount_income_item_detail_list);
        number_text = findViewById(R.id.number);
        big_title = findViewById(R.id.big_title);
        all_btn = findViewById(R.id.all_btn);
        received_btn = findViewById(R.id.received_btn);
        summary_text = findViewById(R.id.summary_text);
        label_text = findViewById(R.id.label_text);
        income_text = findViewById(R.id.income_text);
        expenditure_text = findViewById(R.id.expenditure_text);
        outtime_text = findViewById(R.id.outtime_text);
        yet_btn = findViewById(R.id.yet_btn);

        all_btn.setOnClickListener(this);
        received_btn.setOnClickListener(this);
        yet_btn.setOnClickListener(this);
        findViewById(R.id.date_layout).setOnClickListener(this);
        findViewById(R.id.money_layout).setOnClickListener(this);
        findViewById(R.id.f_summary).setOnClickListener(this);
        findViewById(R.id.f_label).setOnClickListener(this);
        findViewById(R.id.f_income).setOnClickListener(this);
        findViewById(R.id.f_expenditure).setOnClickListener(this);
        findViewById(R.id.f_outtime).setOnClickListener(this);

        time = findViewById(R.id.time);
        money = findViewById(R.id.money);
        date_down = findViewById(R.id.date_down);
        money_down = findViewById(R.id.money_down);
        collect = findViewById(R.id.collect);
        paid = findViewById(R.id.paid);

        getString();//获取传送数据
        number_text.setText("共" + number + "条");
        big_title.setText(title);
        arrayList = new ArrayList<String>();
        for (int i = 0; i < 50; i++) {
            arrayList.add(i + "");
        }
        acount_income_item_detail_list.setAdapter(new AccountInComeItemDetailAdapter(AccountInComeItemDetailActivity.this, arrayList, title, color, false));
    }

    private void getString() {
        Bundle bundle = this.getIntent().getExtras();
        title = bundle.getString("title");
        color = bundle.getInt("color");
        number = bundle.getInt("number");
        if (bundle.getString("name").equals("收入")) {
            label_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
            summary_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
            income_text.setTextColor(ContextCompat.getColor(this, R.color.green));
            expenditure_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
            outtime_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
        } else {
            label_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
            summary_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
            income_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
            expenditure_text.setTextColor(ContextCompat.getColor(this, R.color.green));
            outtime_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.f_summary:
                intent = new Intent(AccountInComeItemDetailActivity.this, AccountBookReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_label:
                intent = new Intent(AccountInComeItemDetailActivity.this, AccountAnalysisLabelStatisticsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_expenditure:
                intent = new Intent(AccountInComeItemDetailActivity.this, ExpenditureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_income:
                intent = new Intent(AccountInComeItemDetailActivity.this, InComeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_outtime:
                intent = new Intent(AccountInComeItemDetailActivity.this, AccountOutTimeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.received_btn:
                received_btn.setTextColor(getResources().getColor(R.color.white));
                all_btn.setTextColor(getResources().getColor(R.color.green));
                yet_btn.setTextColor(getResources().getColor(R.color.green));

                received_btn.setBackgroundColor(getResources().getColor(R.color.green));
                all_btn.setBackgroundResource(R.drawable.bg_gray);
                yet_btn.setBackgroundResource(R.drawable.bg_gray);
                collect.setText("已收 2300");
                paid.setText("已付 800");
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
                collect.setText("待收 2300");
                paid.setText("待付 800");
                break;
            case R.id.date_layout:
                time.setTextColor(ContextCompat.getColor(this, R.color.orange));
                money.setTextColor(ContextCompat.getColor(this, R.color.blank));

                date_down.setImageResource(R.drawable.or_butoom_jiaotou);
                money_down.setImageResource(R.drawable.butoom_jiantou);
                acount_income_item_detail_list.setAdapter(new AccountInComeItemDetailAdapter(AccountInComeItemDetailActivity.this, arrayList, title, color, false));
                break;
            case R.id.money_layout:
                time.setTextColor(ContextCompat.getColor(this, R.color.blank));
                money.setTextColor(ContextCompat.getColor(this, R.color.orange));

                date_down.setImageResource(R.drawable.butoom_jiantou);
                money_down.setImageResource(R.drawable.or_butoom_jiaotou);
                acount_income_item_detail_list.setAdapter(new AccountInComeItemDetailAdapter(AccountInComeItemDetailActivity.this, arrayList, title, color, true));
                break;
            default:
                break;
        }
    }
}
