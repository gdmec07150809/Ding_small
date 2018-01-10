package com.example.administrator.ding_small;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.ding_small.Adapter.AccountReturnByMonthAdapter;
import com.example.administrator.ding_small.Adapter.SearchAdapter;

import java.util.ArrayList;



/**
 * Created by CZK on 2017/12/5.
 */

public class AccountReturnByMonthActivity extends Activity implements View.OnClickListener{
    private ListView listView;
    private boolean isTitle=false;
    private ArrayList<String> arrayList;
    private RelativeLayout date_layout,title_layout,money_layout;
    private TextView time,title,money;
    private ImageView date_down,title_down,money_down;
    private Button received_btn,yet_btn,all_btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_return_by_month);
        findViewById(R.id.date_layout).setOnClickListener(this);
        findViewById(R.id.title_layout).setOnClickListener(this);
        findViewById(R.id.money_layout).setOnClickListener(this);

        received_btn= findViewById(R.id.received_btn);
        yet_btn= findViewById(R.id.yet_btn);
        all_btn= findViewById(R.id.all_btn);
        received_btn.setOnClickListener(this);
        yet_btn.setOnClickListener(this);
        all_btn.setOnClickListener(this);
        time=findViewById(R.id.time);
        title=findViewById(R.id.title);
        money=findViewById(R.id.money);
        date_down=findViewById(R.id.date_down);
        title_down=findViewById(R.id.title_down);
        money_down=findViewById(R.id.money_down);
        listView=findViewById(R.id.search_list);
        arrayList=new ArrayList<String>();
        for (int i=0;i<10;i++){
            arrayList.add(i+"");
        }
        listView.setAdapter(new AccountReturnByMonthAdapter(AccountReturnByMonthActivity.this,arrayList,isTitle));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.date_layout:
                time.setTextColor(getResources().getColor(R.color.orange));
                date_down.setImageResource(R.drawable.or_butoom_jiaotou);

                title.setTextColor(getResources().getColor(R.color.blank));
                title_down.setImageResource(R.drawable.butoom_jiantou);

                money.setTextColor(getResources().getColor(R.color.blank));
                money_down.setImageResource(R.drawable.butoom_jiantou);

                listView.setAdapter(new SearchAdapter(AccountReturnByMonthActivity.this,arrayList,false));
                break;
            case R.id.title_layout:
                title.setTextColor(getResources().getColor(R.color.orange));
                title_down.setImageResource(R.drawable.or_butoom_jiaotou);

                time.setTextColor(getResources().getColor(R.color.blank));
                date_down.setImageResource(R.drawable.butoom_jiantou);

                money.setTextColor(getResources().getColor(R.color.blank));
                money_down.setImageResource(R.drawable.butoom_jiantou);

                listView.setAdapter(new SearchAdapter(AccountReturnByMonthActivity.this,arrayList,true));
                break;
            case R.id.money_layout:
                money.setTextColor(getResources().getColor(R.color.orange));
                money_down.setImageResource(R.drawable.or_butoom_jiaotou);

                title.setTextColor(getResources().getColor(R.color.blank));
                title_down.setImageResource(R.drawable.butoom_jiantou);

                time.setTextColor(getResources().getColor(R.color.blank));
               date_down.setImageResource(R.drawable.butoom_jiantou);

                listView.setAdapter(new SearchAdapter(AccountReturnByMonthActivity.this,arrayList,true));
                break;
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
            default:
                break;
        }
    }
}
