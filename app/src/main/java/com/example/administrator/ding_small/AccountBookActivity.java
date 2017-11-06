package com.example.administrator.ding_small;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.example.administrator.ding_small.Adapter.AccountBookAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/6.
 */

public class AccountBookActivity extends Activity {
    private ListView account_book_list;
    private ArrayList<String> arrayList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_book);
        account_book_list=findViewById(R.id.contacts_account_list);
       arrayList=new ArrayList<String>();
        for (int i=0;i<50;i++){
            arrayList.add(i+"");
        }
        account_book_list.setAdapter(new AccountBookAdapter(AccountBookActivity.this,arrayList));
    }
}
