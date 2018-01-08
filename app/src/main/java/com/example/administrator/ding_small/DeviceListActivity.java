package com.example.administrator.ding_small;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.Adapter.DeviceListAdapter;
import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.example.administrator.ding_small.PersonalCenter.EditPassWordActivity;
import com.example.administrator.ding_small.PersonalCenter.PersonalCenterActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
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

import static android.media.CamcorderProfile.get;
import static com.example.administrator.ding_small.R.id.Right;

/**
 * Created by Administrator on 2017/12/18.
 */

public class DeviceListActivity extends Activity implements View.OnClickListener{
    private Spinner Right;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private ListView device_listview;
    private JSONArray jsonArray;

    JSONArray sortedJsonArray=null;
    private ArrayList<String> device_names;
    private ArrayList<String> device_locations;
    private ArrayList<String> device_ssids;
    private ArrayList<String> device_staus;
    private ArrayList<String> device_type;

    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp=null;//定义储存源，备用
    String memid,token,sign,oldPass,newPass,ts,c_newPass;
    int using_num=0;
    int maintenancing_num=0;
    public static final int SHOW_RESPONSE = 0;
    private ScrollView scrollview;
    private Handler handler;
    private TextView count_number,using,maintenancing;
    private LoadingLayout loading;
    //更改语言所要更改的控件 返回、设备列表、设备总数、使用中、维修中、扫码添加、搜索添加、手工输入、历史设备
    private  TextView back_text,device_list_text,device_num_text,using_text,maintenancing_text,add_by_scan_text,add_by_search_text,enter_by_manually_text,device_history_text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_device_list);
        init();//初始化控件
        //getSpnner();//下拉列表
        changeTextView();//更改语言
        /*滚至顶部*/
        handler = new Handler();
        handler.postDelayed(runnable, 10);

       // CreatJson();//构造jsonArray备用
        getCache();

        System.out.println("json数据："+jsonArray);

        loading.setStatus(LoadingLayout.Loading);
    }

    private void changeTextView(){
        if(Locale.getDefault().getLanguage().equals("en")){
            // back_text,device_list_text,device_num_text,using_text,maintenancing_text,add_by_scan_text,add_by_search_text,enter_by_manually_text,device_history_text;
            back_text.setText("Back");
            device_list_text.setText("Device List");
            device_num_text.setText("Device Number");
            using_text.setText("Using:");
            maintenancing_text.setText("Maintenancing:");
            add_by_scan_text.setText("Scan");
            add_by_search_text.setText("Search");
            enter_by_manually_text.setText("Manually");
            device_history_text.setText("Device History");
        }
    }
    private  void getCache() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        String url = "http://192.168.1.114:8080/app/ppt6000/dateList.do";
        ts = String.valueOf(new Date().getTime());
        System.out.println("首页：" + memid + "  ts:" + ts+ "  token:" + token);
        String Sign = url + memid + token + ts;
        System.out.println("Sign:"+Sign);
        sign = MD5Utils.md5(Sign);
        new Thread(networkTask).start();//获取设备列表
    }
    private void init(){
        //Right = findViewById(R.id.Right);
        device_listview=findViewById(R.id.select_device_list);
        //findViewById(R.id.select).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.scan_list_layout).setOnClickListener(this);
        scrollview=findViewById(R.id.scrollview_layout);
        loading=findViewById(R.id.loading_layout);
        findViewById(R.id.search_img).setOnClickListener(this);//搜索
        count_number=findViewById(R.id.count_number);//总数
        findViewById(R.id.refresh_img).setOnClickListener(this);//刷新
        findViewById(R.id.add_by_search).setOnClickListener(this);//搜索添加
        // findViewById(R.id.personalcenter_layout).setOnClickListener(this);
        using=findViewById(R.id.using);//使用中
        maintenancing=findViewById(R.id.maintenancing);//维修中


        // back_text,device_list_text,device_num_text,using_text,maintenancing_text,add_by_scan_text,add_by_search_text,enter_by_manually_text,device_history_text;
        back_text=findViewById(R.id.back_text);
        device_list_text=findViewById(R.id.device_list_text);
        device_num_text=findViewById(R.id.device_num_text);
        using_text=findViewById(R.id.using_text);
        maintenancing_text=findViewById(R.id.maintenancing_text);
        add_by_scan_text=findViewById(R.id.add_by_scan_text);
        add_by_search_text=findViewById(R.id.add_by_search_text);
        enter_by_manually_text=findViewById(R.id.enter_by_manually_text);
        device_history_text=findViewById(R.id.device_history_text);
    }
    private void CreatJson(){
        jsonArray=new JSONArray();
        device_names=new ArrayList<String>();
        device_locations=new ArrayList<String>();
        device_ssids=new ArrayList<String>();
        device_staus=new ArrayList<String>();
        device_type=new ArrayList<String>();

        device_names.add("桌台灯-1644");
        device_names.add("桌台灯-1620");
        device_names.add("桌台灯-1614");
        device_names.add("桌台灯-1514");
        device_names.add("桌台灯-1254");

        device_locations.add("大山百乐门酒吧");
        device_locations.add("广州广州塔");
        device_locations.add("广州广百百货");
        device_locations.add("广州正佳广场");
        device_locations.add("广州上下九步行街");

        device_ssids.add("201715464848465");
        device_ssids.add("221054546544614");
        device_ssids.add("546546165454645");
        device_ssids.add("465454651846515");
        device_ssids.add("156451518454841");

        device_staus.add("1");
        device_staus.add("1");
        device_staus.add("0");
        device_staus.add("0");
        device_staus.add("1");

        device_type.add("2017/5/9");
        device_type.add("2017/6/2");
        device_type.add("2017/8/1");
        device_type.add("2017/9/2");
        device_type.add("2017/10/12");
        try {
            for (int i=0;i<device_names.size();i++){
                JSONObject jsonObject;
                jsonObject=new JSONObject();
                jsonObject.put("device_name",device_names.get(i));
                jsonObject.put("device_location",device_locations.get(i));
                jsonObject.put("device_ssid",device_ssids.get(i));
                jsonObject.put("device_state",device_staus.get(i));
                jsonObject.put("device_date",device_type.get(i));
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            scrollview.fullScroll(ScrollView.FOCUS_UP);
        }
    };

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
//            case R.id.select:
//                System.out.println("小图标");
//                Right.performClick();
//                break;
            case R.id.scan_list_layout:
                System.out.println("扫码");
                IntentIntegrator integrator=new IntentIntegrator(DeviceListActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("扫描二维码/条形码");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.initiateScan();
                break;
//            case R.id.personalcenter_layout:
//                intent=new Intent(DeviceListActivity.this,PersonalCenterActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                break;
            case R.id.search_img:
                intent=new Intent(DeviceListActivity.this,SearchBoxActiivty.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.refresh_img:
                loading.setStatus(LoadingLayout.Loading);
                getCache();
                break;
            case R.id.add_by_search:
                intent=new Intent(DeviceListActivity.this,DeviceSearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }
    //接收扫描结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result.getContents()==null){
            Toast.makeText(this,"扫描失败",Toast.LENGTH_SHORT).show();
        }else{
            System.out.println("扫描结果："+result.getContents());
            String mac_str=null;
            try{
                mac_str=result.getContents().substring(result.getContents().indexOf("=")+1, result.getContents().indexOf("&"));
                System.out.println("截取结果："+result.getContents().substring(result.getContents().indexOf("=")+1, result.getContents().indexOf("&")));
            }catch (Exception e){
                Toast.makeText(this,"该设备不存在",Toast.LENGTH_SHORT).show();
            }
            if(mac_str!=null){
                Intent intent=new Intent(DeviceListActivity.this,PerfectDeviceActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("device_mac",mac_str);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            //resultNew.setText("扫描结果："+result.getContents());
        }
    }


    /**
     * 网络操作相关的子线程okhttp框架  获取设备
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String url = "http://192.168.1.114:8080/app/ppt6000/dateList.do?memId=" + memid + "&ts=" + ts;
            OkHttpClient okHttpClient = new OkHttpClient();
            System.out.println("验证："+sign);
            String b= "{}";//json字符串
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), b);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("sIgn",sign)
                    .build();
            System.out.println(request.headers());
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                String result=response.body().string();
                if(response!=null){
                    //在子线程中将Message对象发出去
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    message.obj = result.toString();
                    getDeviceListHandler.sendMessage(message);
                }
                System.out.println("结果："+result+"状态码："+ response.code());
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
                        JSONObject object=new JSONObject(response);
                        JSONObject object1=new JSONObject(object.getString("meta"));
                        //{"meta":{"res":"99999","msg":"用户名或密码有误"},"data":null}状态码：200
                        if(object1.getString("res").equals("00000")){
//                            new AlertDialog.Builder(DeviceListActivity.this).setTitle("修改密码").setMessage("修改成功,返回首页").setPositiveButton("确定",new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent intent=new Intent(DeviceListActivity.this,MainLayoutActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            }).show();
                           // JSONObject jsonObject=new JSONObject(object.getString("data"));
                            System.out.println("数据："+object.getString("data"));
                            jsonArray=new JSONArray(object.getString("data"));
                            if(jsonArray.length()>0){

                                System.out.println(jsonArray.length());
                                count_number.setText(jsonArray.length()+"");
                                /*按销售日期排序 */
                                sortedJsonArray = new JSONArray();
                                List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonValues.add(jsonArray.getJSONObject(i));
                                }
                                Collections.sort( jsonValues, new Comparator<JSONObject>() {
                                    //You can change "Name" with "ID" if you want to sort by ID
                                    private static final String KEY_NAME = "insert_date";

                                    @Override
                                    public int compare(JSONObject a, JSONObject b) {
                                        String valA = new String();
                                        String valB = new String();

                                        try {
                                            valA = (String) a.getString(KEY_NAME);
                                            valB = (String) b.getString(KEY_NAME);
                                        }
                                        catch (JSONException e) {
                                            //do something
                                        }

                                        return valA.compareTo(valB);
                                        //if you want to change the sort order, simply use the following:
                                        //return -valA.compareTo(valB);
                                    }
                                });
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    sortedJsonArray.put(jsonValues.get(i));
                                    if(jsonValues.get(i).getString("eqpStatus").equals("1")){
                                        maintenancing_num+=1;
                                    }else   if(jsonValues.get(i).getString("eqpStatus").equals("2")){
                                        using_num+=1;
                                    }
                                }
                            }
                            using.setText(using_num+"");
                            maintenancing.setText(maintenancing_num+"");
                            device_listview.setAdapter(new DeviceListAdapter(DeviceListActivity.this,sortedJsonArray));//设置适配器
                            loading.setStatus(LoadingLayout.Success);
                            device_listview.setOnItemClickListener(new OnItemClickListener() {//列表item事件
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent=new Intent(DeviceListActivity.this,DeviceDetailActivity.class);
                                    Bundle bundle=new Bundle();
                                    try {
                                        JSONObject object=new JSONObject(sortedJsonArray.get(i).toString());
                                        bundle.putString("device_mac",object.getString("macNo"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                            setListViewHeightBasedOnChildren(device_listview);//计算listview的item个数,并完整显示
                        }else{
                            new AlertDialog.Builder(DeviceListActivity.this).setTitle("网络提示").setMessage("请检查网络是否畅通").setPositiveButton("确定",new DialogInterface.OnClickListener() {
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
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}
