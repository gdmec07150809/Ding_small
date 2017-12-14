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

import com.example.administrator.ding_small.Adapter.AccountBookAdapter;
import com.example.administrator.ding_small.PersonalCenter.PersonalCenterActivity;

import java.util.ArrayList;

import static com.example.administrator.ding_small.R.id.date_down;
import static com.example.administrator.ding_small.R.id.finished_btn;
import static com.example.administrator.ding_small.R.id.money;
import static com.example.administrator.ding_small.R.id.money_down;
import static com.example.administrator.ding_small.R.id.no_finish_btn;

/**
 * Created by Administrator on 2017/11/6.
 */

public class AccountBookActivity extends Activity implements View.OnClickListener{
    private ListView account_book_list;
    private ArrayList<String> arrayList;
    private ImageView f_account,f_contacts,f_center,f_notepad;
    private Button already_btn,not_btn;
    private TextView date_text,title_text,money_text;
    private ImageView date_jiantou,title_jiantou,money_jiantou;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_book);
        findViewById(R.id.add).setOnClickListener(this);
        findViewById(R.id.account_analysis).setOnClickListener(this);

        f_account=findViewById(R.id.f_account);
        f_contacts=findViewById(R.id.f_contacts);
        f_center=findViewById(R.id.f_center);
        f_notepad=findViewById(R.id.f_notepad);
        f_account.setImageResource(R.drawable.account_book_yes);
        f_contacts.setImageResource(R.drawable.contacts_no);
        already_btn=findViewById(R.id.already_btn);
        not_btn=findViewById(R.id.not_btn);

        date_text=findViewById(R.id.time);
        title_text=findViewById(R.id.account_title);
        money_text=findViewById(R.id.money);
        date_jiantou=findViewById(R.id.date_jiantou);
        title_jiantou=findViewById(R.id.title_jiantou);
        money_jiantou=findViewById(R.id.money_jiantou);

        already_btn.setOnClickListener(this);
        not_btn.setOnClickListener(this);
        f_contacts.setOnClickListener(this);
        f_center.setOnClickListener(this);
        f_notepad.setOnClickListener(this);
        findViewById(R.id.calendar).setOnClickListener(this);
        findViewById(R.id.search).setOnClickListener(this);

        findViewById(R.id.date_layout).setOnClickListener(this);
        findViewById(R.id.title_layout).setOnClickListener(this);
        findViewById(R.id.money_layout).setOnClickListener(this);

        account_book_list=findViewById(R.id.contacts_account_list);
       arrayList=new ArrayList<String>();
        for (int i=0;i<50;i++){
            arrayList.add(i+"");
        }
        account_book_list.setAdapter(new AccountBookAdapter(AccountBookActivity.this,arrayList,true));
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.add:
                Intent intent1=new Intent(AccountBookActivity.this,NotepadActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                break;
            case R.id.f_notepad:
                Intent intent2=new Intent(AccountBookActivity.this,NotepadBtnActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                break;
            case R.id.f_center:
                intent=new Intent(AccountBookActivity.this,PersonalCenterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_contacts:
                intent=new Intent(AccountBookActivity.this,ContactsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.calendar:
                intent=new Intent(AccountBookActivity.this,AccountsSearchByCalendarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.search:
                intent=new Intent(AccountBookActivity.this,SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.already_btn:
                already_btn.setBackgroundColor(Color.parseColor("#6AB845"));
                already_btn.setTextColor(Color.parseColor("#FFFFFF"));
                not_btn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                not_btn.setTextColor(Color.parseColor("#6AB845"));
                break;
            case R.id.not_btn:
                already_btn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                already_btn.setTextColor(Color.parseColor("#6AB845"));
                not_btn.setBackgroundColor(Color.parseColor("#6AB845"));
                not_btn.setTextColor(Color.parseColor("#FFFFFF"));
                break;
            case R.id.account_analysis:
                intent=new Intent(AccountBookActivity.this,AccountBookReportActivity.class);
                startActivity(intent);
                break;
            case R.id.date_layout:
                date_text.setTextColor(ContextCompat.getColor(this, R.color.orange));
                money_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                title_text.setTextColor(ContextCompat.getColor(this, R.color.blank));

                date_jiantou.setImageResource(R.drawable.or_butoom_jiaotou);
                title_jiantou.setImageResource(R.drawable.butoom_jiantou);
                money_jiantou.setImageResource(R.drawable.butoom_jiantou);

                account_book_list.setAdapter(new AccountBookAdapter(AccountBookActivity.this,arrayList,true));
                break;
            case R.id.title_layout:
                date_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                money_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                title_text.setTextColor(ContextCompat.getColor(this, R.color.orange));

                date_jiantou.setImageResource(R.drawable.butoom_jiantou);
                title_jiantou.setImageResource(R.drawable.or_butoom_jiaotou);
                money_jiantou.setImageResource(R.drawable.butoom_jiantou);

                account_book_list.setAdapter(new AccountBookAdapter(AccountBookActivity.this,arrayList,false));
                break;
            case R.id.money_layout:
                date_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
                money_text.setTextColor(ContextCompat.getColor(this, R.color.orange));
                title_text.setTextColor(ContextCompat.getColor(this, R.color.blank));

                date_jiantou.setImageResource(R.drawable.butoom_jiantou);
                title_jiantou.setImageResource(R.drawable.butoom_jiantou);
                money_jiantou.setImageResource(R.drawable.or_butoom_jiaotou);

                account_book_list.setAdapter(new AccountBookAdapter(AccountBookActivity.this,arrayList,false));
                break;
        }
    }
}
