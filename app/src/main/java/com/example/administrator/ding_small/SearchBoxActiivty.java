package com.example.administrator.ding_small;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ding_small.Adapter.DeviceListAdapter;
import com.example.administrator.ding_small.Adapter.SearchBoxAdapter;
import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.weavey.loading.lib.LoadingLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by CZK on 2018/1/5.
 */

public class SearchBoxActiivty extends Activity implements View.OnClickListener {
    private ArrayList<String> search_record_lists = null;
    private EditText search_text;
    private ListView search_record_list, select_device_list;
    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    private Handler handler;
    private LoadingLayout loading;
    JSONArray sortedJsonArray = null;
    String memid, token, sign, oldPass, newPass, ts, c_newPass, search_str;
    public static final int SHOW_RESPONSE = 0;

    private JSONArray jsonArray;

    //更改语言所要更改的控件
    private TextView cancel_text, recent_search_record_text, clean_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_search_list);
        init();//初始化控件
        changeTextView();
        search_record_lists = new ArrayList<String>();
        getCache();//获取缓存


    }

    private void changeTextView() {
        if (Locale.getDefault().getLanguage().equals("en")) {
            recent_search_record_text.setText("Recent Search Record");
            clean_text.setText("Clean");
            cancel_text.setText("Cancel");
            search_text.setHint("Search");
        }
    }

    private void getMemId() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        String url = "http://192.168.1.104:8080/app/ppt6000/dateList.do";
        ts = String.valueOf(new Date().getTime());
        System.out.println("首页：" + memid + "  ts:" + ts + "  token:" + token);
        String Sign = url + memid + token + ts;
        System.out.println("Sign:" + Sign);
        sign = MD5Utils.md5(Sign);

        new Thread(networkTask).start();//获取设备列表
    }

    private void getCache() {
        final ArrayList<String> environmentList = new ArrayList<String>();
        SharedPreferences preferDataList = getSharedPreferences("SearchDataList", MODE_PRIVATE);
        int environNums = preferDataList.getInt("SearchNums", 0);
        for (int i = 0; i < environNums; i++) {
            String environItem = preferDataList.getString("item_" + i, null);
            System.out.println("获取：" + environItem);
            environmentList.add(environItem);
        }
        search_record_list.setAdapter(new SearchBoxAdapter(this, environmentList));
        search_record_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {//列表item事件
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                search_text.setText(environmentList.get(i));
            }
        });
        setListViewHeightBasedOnChildren(search_record_list);
    }

    private void setCache() {
        search_str = search_text.getText().toString().trim();
        System.out.println(search_str);
        search_record_lists.add(search_str);

        SharedPreferences.Editor editor = getSharedPreferences("SearchDataList", MODE_PRIVATE).edit();
        editor.putInt("SearchNums", search_record_lists.size());
        for (int i = 0; i < search_record_lists.size(); i++) {
            editor.putString("item_" + i, search_record_lists.get(i));
        }
        editor.commit();
    }

    private void init() {
        findViewById(R.id.search_btn).setOnClickListener(this);
        //findViewById(R.id.cancel_btn).setOnClickListener(this);
        findViewById(R.id.remove_cache).setOnClickListener(this);
        search_text = findViewById(R.id.searh_box);
        search_record_list = findViewById(R.id.search_record_list);
        select_device_list = findViewById(R.id.select_device_list);
        loading = findViewById(R.id.loading_layout);
        // cancel_text,recent_search_record_text,clean_text;
        recent_search_record_text = findViewById(R.id.recent_record_text);
        clean_text = findViewById(R.id.clean_text);
        cancel_text = findViewById(R.id.cancel_btn);
        cancel_text.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_btn:
                setCache();//设置缓存
                loading.setStatus(LoadingLayout.Loading);
                getMemId();//查询
                break;
            case R.id.remove_cache://清除搜索记录
                SharedPreferences userSettings = getSharedPreferences("SearchDataList", 0);
                SharedPreferences.Editor editor = userSettings.edit();
                editor.clear();
                editor.commit();
                getCache();
                break;
            case R.id.cancel_btn://取消
                finish();
                break;
            default:
                break;
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 网络操作相关的子线程okhttp框架  获取设备
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String url = "http://192.168.1.104:8080/app/ppt6000/dateList.do?memId=" + memid + "&ts=" + ts;
            OkHttpClient okHttpClient = new OkHttpClient();
            System.out.println("验证：" + sign);
            String b = "{}";//json字符串
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), b);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("sIgn", sign)
                    .build();
            System.out.println(request.headers());
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                String result = response.body().string();
                if (response != null) {
                    //在子线程中将Message对象发出去
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    message.obj = result.toString();
                    getDeviceListHandler.sendMessage(message);
                }
                System.out.println("结果：" + result + "状态码：" + response.code());
                //Toast.makeText(EditPassWordActivity.this,result,Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    private Handler getDeviceListHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    System.out.println(response);
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject object1 = new JSONObject(object.getString("meta"));
                        //{"meta":{"res":"99999","msg":"用户名或密码有误"},"data":null}状态码：200
                        if (object1.getString("res").equals("00000")) {
//                            new AlertDialog.Builder(DeviceListActivity.this).setTitle("修改密码").setMessage("修改成功,返回首页").setPositiveButton("确定",new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent intent=new Intent(DeviceListActivity.this,MainLayoutActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            }).show();
                            // JSONObject jsonObject=new JSONObject(object.getString("data"));
                            System.out.println("数据：" + object.getString("data"));
                            jsonArray = new JSONArray(object.getString("data"));
                            if (jsonArray.length() > 0) {

                                /*模糊查询*/
                                sortedJsonArray = new JSONArray();
                                //List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    if ((jsonArray.getJSONObject(i).getString("eqpName")).indexOf(search_str) > -1) {
                                        sortedJsonArray.put(jsonArray.getJSONObject(i));
                                    }
                                }
                            }
                            if (sortedJsonArray != null) {
                                select_device_list.setAdapter(new DeviceListAdapter(SearchBoxActiivty.this, sortedJsonArray));//设置适配器
                                loading.setStatus(LoadingLayout.Success);
                            }
                            select_device_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {//列表item事件
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(SearchBoxActiivty.this, DeviceDetailActivity.class);
                                    Bundle bundle = new Bundle();
                                    try {
                                        JSONObject object = new JSONObject(sortedJsonArray.get(i).toString());
                                        System.out.println(object.getString("macNo"));
                                        bundle.putString("device_mac", object.getString("macNo"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });

                            setListViewHeightBasedOnChildren(select_device_list);//计算listview的item个数,并完整显示

                        } else {
                            new AlertDialog.Builder(SearchBoxActiivty.this).setTitle("网络提示").setMessage("请检查网络是否畅通").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

    };
}
