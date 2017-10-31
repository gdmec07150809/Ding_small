package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.administrator.ding_small.Adapter.EditTitleAdapter.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.media.CamcorderProfile.get;

/**
 * Created by Administrator on 2017/10/28.
 */

public class EditTitleActivity extends Activity implements View.OnClickListener,Callback{
    public ListView list;
    private ArrayList<String> lists;
    public JSONArray jsonArray;

    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_title);
        list=findViewById(R.id.title_list);
        findViewById(R.id.received).setOnClickListener(this);
        findViewById(R.id.notepad).setOnClickListener(this);
        findViewById(R.id.payable).setOnClickListener(this);
        findViewById(R.id.add_title).setOnClickListener(this);
        jsonArray=new JSONArray();
        lists=new ArrayList<String>();
        lists.add("通用");
        lists.add("住房");
        lists.add("逛街");
        lists.add("买菜");
        lists.add("奖金");
        lists.add("学费");
        lists.add("工资");
        lists.add("房租");
        lists.add("零食");
        lists.add("夜宵");
        try {
            for (int i=0;i<lists.size();i++){
                JSONObject jsonObject;
                jsonObject=new JSONObject();
                jsonObject.put("name",lists.get(i));
                jsonObject.put("number","0");
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        list.setAdapter(new com.example.administrator.ding_small.Adapter.EditTitleAdapter(EditTitleActivity.this,jsonArray,this));

        list.setOnItemClickListener(new OnItemClickListener() {
            @RequiresApi(api = VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }



    //    private JSONArray sort(JSONArray ja,final String field, boolean isAsc){
//       List<JSONObject> sortList=new ArrayList<>();
//
//        for (int i=0;i<ja.length();i++){
//            try {
//                sortList.add((JSONObject) ja.get(i));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        Collections.sort(sortList, new Comparator<JSONObject>() {
//            @Override
//            public int compare(JSONObject o1, JSONObject o2) {
//                Object f1 = null;
//                Object f2 = null;
//                try {
//
//                    f1 = o1.getString(field);
//                    f2 = o2.getString(field);
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                if (f1 instanceof Number && f2 instanceof Number) {
//                    return ((Number) f1).intValue() - ((Number) f2).intValue();
//                } else {
//                    return f1.toString().compareTo(f2.toString());
//                }
//            }
//        });
//        if(!isAsc){
//            Collections.reverse(sortList);
//        }
//        JSONArray result=new JSONArray();
//        for(int i=0;i<sortList.size();i++){
//            result.put(sortList.get(i));
//        }
//       return result;
//    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.received:
                intent=new Intent(EditTitleActivity.this,ReceivedActivity.class);
                startActivity(intent);
                break;
            case R.id.payable:
                intent=new Intent(EditTitleActivity.this,PayableActivity.class);
                startActivity(intent);
                break;
            case R.id.notepad:
                intent=new Intent(EditTitleActivity.this,NotepadActivity.class);
                startActivity(intent);
                break;
            case R.id.add_title:
                intent=new Intent(EditTitleActivity.this,AddTitleActivity.class);
                startActivity(intent);
                break;

        }
    }


    @Override
    public void click(View v) {
            switch (v.getId()){
                case R.id.up_img:
                    try {
                        JSONObject ob1= new JSONObject(String.valueOf(jsonArray.get((Integer) v.getTag())));
                        JSONObject ob2= new JSONObject(String.valueOf(jsonArray.get(0)));
                        ob1.put("number","1");
                        jsonArray.put(0,ob1);
                        jsonArray.put((Integer) v.getTag(),ob2);
                        System.out.println("更改后："+ob1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    list.setAdapter(new com.example.administrator.ding_small.Adapter.EditTitleAdapter(EditTitleActivity.this,jsonArray,this));
                    break;
                case R.id.edit_img:
                        Intent intent=new Intent(EditTitleActivity.this,EditTitleItemBtnActivity.class);
                        String title= String.valueOf(v.getTag(R.id.tag_first));
                        int index= (int) v.getTag(R.id.tag_second);
                        Bundle bundle=new Bundle();
                        bundle.putString("index",index+"");
                        bundle.putString("title",title);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    break;
            }
    }
}
