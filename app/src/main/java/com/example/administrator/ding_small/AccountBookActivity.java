package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.ding_small.Adapter.AccountBookAdapter;
import com.example.administrator.ding_small.PersonalCenter.PersonalCenterActivity;

import java.util.ArrayList;

import static com.example.administrator.ding_small.R.id.f_account;
import static com.example.administrator.ding_small.R.id.f_center;
import static com.example.administrator.ding_small.R.id.f_contacts;
import static com.example.administrator.ding_small.R.id.f_notepad;

/**
 * Created by Administrator on 2017/11/6.
 */

public class AccountBookActivity extends Activity implements View.OnClickListener{
    private ListView account_book_list;
    private ArrayList<String> arrayList;
    private ImageView f_account,f_contacts,f_center,f_notepad;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_book);
        findViewById(R.id.add).setOnClickListener(this);

        f_account=findViewById(R.id.f_account);
        f_contacts=findViewById(R.id.f_contacts);
        f_center=findViewById(R.id.f_center);
        f_notepad=findViewById(R.id.f_notepad);
        f_account.setImageResource(R.drawable.account_book_yes);
        f_contacts.setImageResource(R.drawable.contacts_no);

        f_contacts.setOnClickListener(this);
        f_center.setOnClickListener(this);
        f_notepad.setOnClickListener(this);
        account_book_list=findViewById(R.id.contacts_account_list);
       arrayList=new ArrayList<String>();
        for (int i=0;i<50;i++){
            arrayList.add(i+"");
        }
        account_book_list.setAdapter(new AccountBookAdapter(AccountBookActivity.this,arrayList));
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
        }
    }
}
