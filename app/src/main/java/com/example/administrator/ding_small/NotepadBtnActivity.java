package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.ding_small.Adapter.NotepadBtnAdapter;
import com.example.administrator.ding_small.Adapter.NotepadRemarkDetailsAdapter;
import com.example.administrator.ding_small.PersonalCenter.PersonalCenterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.media.CamcorderProfile.get;
import static com.example.administrator.ding_small.R.id.money;
import static com.example.administrator.ding_small.R.id.sort;
import static com.example.administrator.ding_small.HelpTool.JsonArrayBySort.sort;
/**
 * Created by Administrator on 2017/11/3.
 */

public class NotepadBtnActivity extends Activity implements View.OnClickListener {
    private ListView listView;
    private ArrayList<String> arrayList;
    private ImageView f_account, f_contacts, f_center, f_notepad;
    private Button finished_btn, no_finish_btn;
    private ArrayList<String> date;//时间
    private ArrayList<String> explain;//详情
    private ArrayList<String> name;//执行人
    private ArrayList<String> title;//标题
    private ArrayList<String> label;//标签
    private ArrayList<String> time;//标签
    private int titleColor[];
    public JSONArray jsonArray;
    private Spinner Right;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepad_btn_activity);
        listView = findViewById(R.id.notepad_list);
        findViewById(R.id.add).setOnClickListener(this);

        f_account = findViewById(R.id.f_account);
        f_contacts = findViewById(R.id.f_contacts);
        f_center = findViewById(R.id.f_center);
        f_notepad = findViewById(R.id.f_notepad);
        f_notepad.setImageResource(R.drawable.notepad_yes_img);
        f_contacts.setImageResource(R.drawable.contacts_no);
        finished_btn = findViewById(R.id.finished_btn);
        no_finish_btn = findViewById(R.id.no_finish_btn);

        Right = findViewById(R.id.Right);
        f_account.setOnClickListener(this);
        f_contacts.setOnClickListener(this);
        f_center.setOnClickListener(this);
        f_notepad.setOnClickListener(this);
        finished_btn.setOnClickListener(this);
        no_finish_btn.setOnClickListener(this);

        findViewById(R.id.analysis).setOnClickListener(this);
        findViewById(R.id.search).setOnClickListener(this);
        findViewById(R.id.calendar).setOnClickListener(this);
        findViewById(R.id.select).setOnClickListener(this);

        date = new ArrayList<String>();
        explain = new ArrayList<String>();
        name = new ArrayList<String>();
        title = new ArrayList<String>();
        label = new ArrayList<String>();
        titleColor = new int[]{R.color.green, R.color.fen, R.color.orange, R.color.green, R.color.fen, R.color.orange, R.color.bg1, R.color.bg2, R.color.bg3, R.color.bg4, R.color.bg5};
        time = new ArrayList<String>();

        date.add("2016-4-5");
        date.add("2016-4-6");
        date.add("2016-4-7");
        date.add("2016-4-7");
        date.add("2016-4-8");
        date.add("2016-4-9");
        date.add("2016-4-6");
        date.add("2016-4-7");
        date.add("2016-4-7");
        date.add("2016-4-8");
        date.add("2016-4-9");
        explain.add("21元,20170922,到105告拉进来");
        explain.add("21元,20170922,到105告拉进来");
        explain.add("21元,20170922,到105告拉进来");
        explain.add("21元,20170922,到105告拉进来");
        explain.add("21元,20170922,到105告拉进来");
        explain.add("21元,20170922,到105告拉进来");
        explain.add("21元,20170922,到105告拉进来");
        explain.add("21元,20170922,到105告拉进来");
        explain.add("21元,20170922,到105告拉进来");
        explain.add("21元,20170922,到105告拉进来");
        explain.add("21元,20170922,到105告拉进来");
        name.add("张先生");
        name.add("张先生");
        name.add("张先生");
        name.add("张先生");
        name.add("张先生");
        name.add("张先生");
        name.add("张先生");
        name.add("张先生");
        name.add("张先生");
        name.add("张先生");
        name.add("张先生");
        title.add("上门维修");
        title.add("上门维修");
        title.add("工单指令");
        title.add("上门维修");
        title.add("工单指令");
        title.add("吃饭");
        title.add("逛街");
        title.add("打游戏");
        title.add("上门维修");
        title.add("上学");
        title.add("工作");
        label.add("2118-乐堡吸塑灯箱");
        label.add("2118-乐堡吸塑灯箱");
        label.add("2118-乐堡吸塑灯箱");
        label.add("2118-乐堡吸塑灯箱");
        label.add("2118-乐堡吸塑灯箱");
        label.add("2118-乐堡吸塑灯箱");
        label.add("2118-乐堡吸塑灯箱");
        label.add("2118-乐堡吸塑灯箱");
        label.add("2118-乐堡吸塑灯箱");
        label.add("2118-乐堡吸塑灯箱");
        label.add("2118-乐堡吸塑灯箱");
        time.add("10:30");
        time.add("1:30");
        time.add("12:30");
        time.add("9:30");
        time.add("4:30");
        time.add("19:30");
        time.add("22:30");
        time.add("11:30");
        time.add("8:30");
        time.add("23:30");
        time.add("15:30");
        jsonArray = new JSONArray();
        try {
            for (int i = 0; i < date.size(); i++) {
                JSONObject jsonObject;
                jsonObject = new JSONObject();
                jsonObject.put("date", date.get(i));
                jsonObject.put("explain", explain.get(i));
                jsonObject.put("name", name.get(i));
                jsonObject.put("title", title.get(i));
                jsonObject.put("label", label.get(i));
                jsonObject.put("time", time.get(i));
                jsonObject.put("titleColor", titleColor[i]);
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView.setAdapter(new NotepadBtnAdapter(NotepadBtnActivity.this, sort(jsonArray, "date", true)));
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(titleColor[i] + "");
                Intent intent = new Intent(NotepadBtnActivity.this, NotepadRemarkDetailsActivity.class);
                Bundle bundle = new Bundle();

                try {
                    JSONObject object = new JSONObject(sort(jsonArray, "date", true).get(i) + "");
                    bundle.putString("name", object.getString("name"));
                    bundle.putString("explain", object.getString("explain"));
                    bundle.putString("title", object.getString("title"));
                    System.out.println("before:" + object.getString("titleColor"));
                    bundle.putString("titleColor", object.getString("titleColor"));
                    bundle.putString("date", object.getString("date"));
                    bundle.putString("time", object.getString("time"));
                    bundle.putString("label", object.getString("label"));
                    bundle.putString("activityName", "1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        getSpnner();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.add:
                intent = new Intent(NotepadBtnActivity.this, NotepadActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.search:
                intent = new Intent(NotepadBtnActivity.this, SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_account:
                Intent intent2 = new Intent(NotepadBtnActivity.this, AccountBookActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                break;
            case R.id.f_center:
                intent = new Intent(NotepadBtnActivity.this, PersonalCenterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_contacts:
                intent = new Intent(NotepadBtnActivity.this, ContactsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.calendar:
                intent = new Intent(NotepadBtnActivity.this, NotepadSearchByCalendarActivity.class);
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
            case R.id.analysis:
                intent = new Intent(NotepadBtnActivity.this, NotepadReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.select:
                System.out.println("小图标");
                Right.performClick();
                break;
            default:
                break;
        }
    }

    private void getSpnner() {
        data_list = new ArrayList<String>();
        data_list.add("个人");
        data_list.add("公司");
        //适配器
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        //加载适配器
        Right.setAdapter(arr_adapter);
        Right.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        TextView tv1=(TextView) view;
                        tv1.setTextSize(14.0f);
                        tv1.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                    case 1:
                        TextView tv2=(TextView) view;
                        tv2.setTextSize(14.0f);
                        tv2.setTextColor(Color.parseColor("#FFFFFF"));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
