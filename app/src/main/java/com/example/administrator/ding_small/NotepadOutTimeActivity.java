package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ding_small.Adapter.SearchAdapter;

import java.util.ArrayList;



/**
 * Created by Administrator on 2017/11/27.
 */

public class NotepadOutTimeActivity extends Activity implements View.OnClickListener{
    private ListView listView;
    private boolean isTitle=false;
    private ArrayList<String> arrayList;
    private TextView date_text,title_text,f_label_text,f_title_text,f_finished_text,f_outtime_text;
    private ImageView date_img,title_img;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepad_return_by_month);
        findViewById(R.id.title_btn).setOnClickListener(this);
        findViewById(R.id.date_btn).setOnClickListener(this);
        findViewById(R.id.f_title).setOnClickListener(this);
        findViewById(R.id.f_label).setOnClickListener(this);

        findViewById(R.id.f_title).setOnClickListener(this);
        findViewById(R.id.f_label).setOnClickListener(this);
        findViewById(R.id.f_finished).setOnClickListener(this);
        findViewById(R.id.f_outtime).setOnClickListener(this);
        f_title_text=findViewById(R.id.title_text);
        f_label_text=findViewById(R.id.label_text);
        f_finished_text=findViewById(R.id.finished_text);
        f_outtime_text=findViewById(R.id.outtime_text);

        f_title_text.setTextColor(getResources().getColor(R.color.blank));
        f_label_text.setTextColor(getResources().getColor(R.color.blank));
        f_finished_text.setTextColor(getResources().getColor(R.color.blank));
        f_outtime_text.setTextColor(getResources().getColor(R.color.green));

        date_text=findViewById(R.id.time);
        title_text=findViewById(R.id.title);
        listView=findViewById(R.id.search_list);

        date_img=findViewById(R.id.or_butoom_jiantou);
        title_img=findViewById(R.id.butoom_jiantou);

        arrayList=new ArrayList<String>();
        for (int i=0;i<10;i++){
            arrayList.add(i+"");
        }
        listView.setAdapter(new SearchAdapter(NotepadOutTimeActivity.this,arrayList,isTitle));
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.date_btn:
                date_text.setTextColor(getResources().getColor(R.color.orange));
                date_img.setImageResource(R.drawable.or_butoom_jiaotou);

                title_text.setTextColor(getResources().getColor(R.color.blank));
                title_img.setImageResource(R.drawable.butoom_jiantou);
                listView.setAdapter(new SearchAdapter(NotepadOutTimeActivity.this,arrayList,false));
                break;
            case R.id.title_btn:
                date_text.setTextColor(getResources().getColor(R.color.blank));
                date_img.setImageResource(R.drawable.butoom_jiantou);

                title_text.setTextColor(getResources().getColor(R.color.orange));
                title_img.setImageResource(R.drawable.or_butoom_jiaotou);
                listView.setAdapter(new SearchAdapter(NotepadOutTimeActivity.this,arrayList,true));
                break;
            case R.id.f_title:
                intent=new Intent(NotepadOutTimeActivity.this, NotepadAnalysisTitleStatisticsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_label:
                intent=new Intent(NotepadOutTimeActivity.this, NotepadAnalysisLabelStatisticsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_finished:
                intent=new Intent(NotepadOutTimeActivity.this,NotepadReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}
