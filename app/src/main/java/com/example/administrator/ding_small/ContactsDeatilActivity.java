package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ding_small.Adapter.ContactAccountAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/2.
 */

public class ContactsDeatilActivity extends Activity implements View.OnClickListener{
    private ListView listView,listView2;
    private ArrayList<String> arrayList;
    private TextView nametext,count_number,count_text,l_money,R_money,received_text,payable_text;
    String  name="";
    private Button account_btn,notepad_btn;
    private LinearLayout account_layout,notepad_layout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_details);
        listView=findViewById(R.id.contacts_account_list);
        nametext=findViewById(R.id.name);
        nametext.setOnClickListener(this);
        count_number=findViewById(R.id.count_number);
        count_text=findViewById(R.id.count_text);
        l_money=findViewById(R.id.l_money);
        R_money=findViewById(R.id.R_money);
        received_text=findViewById(R.id.received_text);
        payable_text=findViewById(R.id.payable_text);
        notepad_btn=findViewById(R.id.notepad_btn);
        account_btn=findViewById(R.id.account_btn);
        notepad_btn.setOnClickListener(this);
        account_btn.setOnClickListener(this);
        account_layout=(LinearLayout) findViewById(R.id.account_layout);
        notepad_layout=(LinearLayout) findViewById(R.id.notepad_layout);

        name=getIntent().getStringExtra("name");
        nametext.setText(name);
        arrayList=new ArrayList<String>();
        for (int i=0;i<10;i++){
            arrayList.add(i+"");
        }
        listView.setAdapter(new ContactAccountAdapter(ContactsDeatilActivity.this,arrayList));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.name:
                Intent intent=new Intent(ContactsDeatilActivity.this,ContactsDeatilsToActivity.class);
                intent.putExtra("name",name);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.notepad_btn:
                count_number.setText("31");
                count_text.setText("合计记事");
                l_money.setText("21");
                received_text.setText("已完成");
                R_money.setText("10");
                payable_text.setText("待完成");
                account_btn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                account_btn.setTextColor(Color.parseColor("#6AB845"));
                notepad_btn.setBackgroundColor(Color.parseColor("#6AB845"));
                notepad_btn.setTextColor(Color.parseColor("#FFFFFF"));
                account_layout.setVisibility(View.GONE);
                notepad_layout.setVisibility(View.VISIBLE);
                listView2=findViewById(R.id.contacts_notepad_list);
                listView2.setAdapter(new ContactAccountAdapter(ContactsDeatilActivity.this,arrayList));
                break;
            case R.id.account_btn:
                count_number.setText("3200");
                count_text.setText("合计结算余额");
                l_money.setText("21200");
                received_text.setText("已收/待收");
                R_money.setText("3200");
                payable_text.setText("已付/待付");
                account_btn.setBackgroundColor(Color.parseColor("#6AB845"));
                account_btn.setTextColor(Color.parseColor("#FFFFFF"));
                notepad_btn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                notepad_btn.setTextColor(Color.parseColor("#6AB845"));
                account_layout.setVisibility(View.VISIBLE);
                notepad_layout.setVisibility(View.GONE);
                break;
        }
    }
}
