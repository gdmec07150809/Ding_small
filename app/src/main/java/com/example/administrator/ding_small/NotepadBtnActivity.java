package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.ding_small.Adapter.NotepadBtnAdapter;
import com.example.administrator.ding_small.PersonalCenter.PersonalCenterActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/3.
 */

public class NotepadBtnActivity extends Activity implements View.OnClickListener{
    private ListView listView;
    private ArrayList<String> arrayList;
    private ImageView f_account,f_contacts,f_center,f_notepad;
    private Button finished_btn,no_finish_btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepad_btn_activity);
        listView=findViewById(R.id.notepad_list);
        findViewById(R.id.add).setOnClickListener(this);

        f_account=findViewById(R.id.f_account);
        f_contacts=findViewById(R.id.f_contacts);
        f_center=findViewById(R.id.f_center);
        f_notepad=findViewById(R.id.f_notepad);
        f_notepad.setImageResource(R.drawable.work_order_yes);
        f_contacts.setImageResource(R.drawable.contacts_no);
        finished_btn=findViewById(R.id.finished_btn);
        no_finish_btn=findViewById(R.id.no_finish_btn);

        f_account.setOnClickListener(this);
        f_contacts.setOnClickListener(this);
        f_center.setOnClickListener(this);
        f_notepad.setOnClickListener(this);
        finished_btn.setOnClickListener(this);
        no_finish_btn.setOnClickListener(this);

        findViewById(R.id.search).setOnClickListener(this);
        findViewById(R.id.calendar).setOnClickListener(this);


        arrayList=new ArrayList<String>();
        for (int i=0;i<10;i++){
            arrayList.add(i+"");
        }
        listView.setAdapter(new NotepadBtnAdapter(NotepadBtnActivity.this,arrayList));
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.add:
                intent=new Intent(NotepadBtnActivity.this,NotepadActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.search:
                intent=new Intent(NotepadBtnActivity.this,SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_account:
                Intent intent2=new Intent(NotepadBtnActivity.this,AccountBookActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                break;
            case R.id.f_center:
                intent=new Intent(NotepadBtnActivity.this,PersonalCenterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_contacts:
                intent=new Intent(NotepadBtnActivity.this,ContactsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.calendar:
                intent=new Intent(NotepadBtnActivity.this,NotepadSearchByCalendarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.finished_btn:
                finished_btn.setBackgroundColor(Color.parseColor("#6AB845"));
                finished_btn.setTextColor(Color.parseColor("#FFFFFF"));
                no_finish_btn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                no_finish_btn.setTextColor(Color.parseColor("#6AB845"));
                break;
            case R.id.no_finish_btn:
                finished_btn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                finished_btn.setTextColor(Color.parseColor("#6AB845"));
                no_finish_btn.setBackgroundColor(Color.parseColor("#6AB845"));
                no_finish_btn.setTextColor(Color.parseColor("#FFFFFF"));
                break;
        }
    }
}
