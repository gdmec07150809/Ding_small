package com.example.administrator.ding_small;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.Adapter.DeviceListAdapter;
import com.example.administrator.ding_small.HelpTool.MD5Utils;
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

import static com.example.administrator.ding_small.R.id.device_listview;
import static com.example.administrator.ding_small.R.id.device_type;


/**
 * Created by CZK on 2017/12/19.
 */

public class SelectDeviceActivity extends Activity implements View.OnClickListener {
    private JSONArray jsonArray;
    private ArrayList<String> device_names;
    private ArrayList<String> device_locations;
    private ArrayList<String> device_ssids;
    private ArrayList<String> device_staus;
    private ArrayList<String> device_date;
    private ListView select_device_list;
    private EditText search_edittext;
    private Dialog mCameraDialog;
    private ImageView date_img, selling_img, device_img, uuid_img;

    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    private Handler handler;
    private LoadingLayout loading;
    JSONArray sortedJsonArray = null;
    String memid, token, sign, oldPass, newPass, ts, c_newPass;
    public static final int SHOW_RESPONSE = 0;
    //更改语言所要更改的控件 销售日期、售点名称、设备名称、UUID、返回、选择设备
    private TextView date_text, selling_text, device_text, uuid_text, back_text, select_device_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_new_device);
        init();//初始化控件
        //CreatJson();//构造jsonArray备用
        changeTextView();//更改语言
        getCache();
        loading.setStatus(LoadingLayout.Loading);
    }

    private void changeTextView() {
        if (Locale.getDefault().getLanguage().equals("en")) {
            //date_text,selling_text,device_text,uuid_text,back_text,select_device_text

            date_text.setText("Date");
            selling_text.setText("Selling");
            device_text.setText("Device");
            uuid_text.setText("UUID");
            back_text.setText("Back");
            select_device_text.setText("Select Device");
        }
    }

    private void getCache() {
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

    private void init() {
        select_device_list = findViewById(R.id.select_device_list);//设备列表
        findViewById(R.id.start_layout).setOnClickListener(this);//启用日期
        findViewById(R.id.selling_layout).setOnClickListener(this);//售点名称
        findViewById(R.id.device_layout).setOnClickListener(this);//设备名称
        findViewById(R.id.uuid_layout).setOnClickListener(this);//SSID
        findViewById(R.id.add_device).setOnClickListener(this);//新增设备
        findViewById(R.id.back).setOnClickListener(this);//返回
        findViewById(R.id.search_img).setOnClickListener(this);//搜索
//        findViewById(R.id.clean_text).setOnClickListener(this);//清空搜索框
//        findViewById(R.id.search_btn).setOnClickListener(this);//查询

        date_text = findViewById(R.id.date_text);
        selling_text = findViewById(R.id.selling_text);
        device_text = findViewById(R.id.device_text);
        uuid_text = findViewById(R.id.uuid_text);
        back_text = findViewById(R.id.back_text);
        select_device_text = findViewById(R.id.select_device_text);
//        search_edittext=findViewById(R.id.search_edittext);

        date_img = findViewById(R.id.date_img);
        selling_img = findViewById(R.id.selling_img);
        device_img = findViewById(R.id.device_img);
        uuid_img = findViewById(R.id.uuid_img);
        loading = findViewById(R.id.loading_layout);
    }

    private void CreatJson() {
        jsonArray = new JSONArray();
        device_names = new ArrayList<String>();
        device_locations = new ArrayList<String>();
        device_ssids = new ArrayList<String>();
        device_staus = new ArrayList<String>();
        device_date = new ArrayList<String>();

        device_names.add("桌台灯-1644");
        device_names.add("桌台灯-1620");
        device_names.add("桌台灯-1614");
        device_names.add("桌台灯-1514");
        device_names.add("桌台灯-1254");
        device_names.add("桌台灯-1255");
        device_names.add("桌台灯-1205");

        device_locations.add("大山百乐门酒吧");
        device_locations.add("广州广州塔");
        device_locations.add("广州广百百货");
        device_locations.add("广州正佳广场");
        device_locations.add("广州上下九步行街");
        device_locations.add("广州天河城");
        device_locations.add("广州番禺广场");


        device_ssids.add("201715464848465");
        device_ssids.add("221054546544614");
        device_ssids.add("546546165454645");
        device_ssids.add("465454651846515");
        device_ssids.add("156451518454841");
        device_ssids.add("156848184654564");
        device_ssids.add("574575762425112");

        device_staus.add("1");
        device_staus.add("1");
        device_staus.add("0");
        device_staus.add("0");
        device_staus.add("1");
        device_staus.add("0");
        device_staus.add("1");

        device_date.add("2017/12/5");
        device_date.add("2017/5/6");
        device_date.add("2017/6/8");
        device_date.add("2017/6/8");
        device_date.add("2017/6/4");
        device_date.add("2017/2/6");
        device_date.add("2017/6/9");
        try {
            for (int i = 0; i < device_names.size(); i++) {
                JSONObject jsonObject;
                jsonObject = new JSONObject();
                jsonObject.put("device_name", device_names.get(i));
                jsonObject.put("device_location", device_locations.get(i));
                jsonObject.put("device_ssid", device_ssids.get(i));
                jsonObject.put("device_state", device_staus.get(i));
                jsonObject.put("device_date", device_date.get(i));
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*date_text,selling_text,device_text,ssid_text*/
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.start_layout://启用日期
//                date_text.setTextColor(ContextCompat.getColor(this,R.color.orange));
//                selling_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
//                device_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
//                ssid_text.setTextColor(ContextCompat.getColor(this,R.color.blank));

                date_img.setImageResource(R.mipmap.icon_common_sort_down);
                selling_img.setImageResource(R.mipmap.icon_common_sort_up);
                device_img.setImageResource(R.mipmap.icon_common_sort_up);
                uuid_img.setImageResource(R.mipmap.icon_common_sort_up);


                if (jsonArray.length() > 0) {
                    loading.setStatus(LoadingLayout.Success);

                                /*按销售日期排序 */
                    sortedJsonArray = new JSONArray();
                    List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            jsonValues.add(jsonArray.getJSONObject(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Collections.sort(jsonValues, new Comparator<JSONObject>() {
                        //You can change "Name" with "ID" if you want to sort by ID
                        private static final String KEY_NAME = "insert_date";

                        @Override
                        public int compare(JSONObject a, JSONObject b) {
                            String valA = new String();
                            String valB = new String();

                            try {
                                valA = (String) a.getString(KEY_NAME);
                                valB = (String) b.getString(KEY_NAME);
                            } catch (JSONException e) {
                                //do something
                            }

                            return valA.compareTo(valB);
                            //if you want to change the sort order, simply use the following:
                            //return -valA.compareTo(valB);
                        }
                    });
                    for (int i = 0; i < jsonArray.length(); i++) {
                        sortedJsonArray.put(jsonValues.get(i));
                    }
                }
                select_device_list.setAdapter(new DeviceListAdapter(SelectDeviceActivity.this, sortedJsonArray));//设置适配器
                break;
            case R.id.selling_layout://售点名称
//                date_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
//                selling_text.setTextColor(ContextCompat.getColor(this,R.color.orange));
//                device_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
//                ssid_text.setTextColor(ContextCompat.getColor(this,R.color.blank));

                date_img.setImageResource(R.mipmap.icon_common_sort_up);
                selling_img.setImageResource(R.mipmap.icon_common_sort_down);
                device_img.setImageResource(R.mipmap.icon_common_sort_up);
                uuid_img.setImageResource(R.mipmap.icon_common_sort_up);


                if (jsonArray.length() > 0) {
                    loading.setStatus(LoadingLayout.Success);

                    /*按售点名称排序 */
                    sortedJsonArray = new JSONArray();
                    List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            jsonValues.add(jsonArray.getJSONObject(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Collections.sort(jsonValues, new Comparator<JSONObject>() {
                        //You can change "Name" with "ID" if you want to sort by ID
                        private static final String KEY_NAME = "eqpAddressJson";

                        @Override
                        public int compare(JSONObject a, JSONObject b) {
                            String valA = new String();
                            String valB = new String();

                            try {
                                JSONObject jsonObject = new JSONObject(a.getString(KEY_NAME));
                                JSONObject jsonObject1 = new JSONObject(b.getString(KEY_NAME));
                                valA = (String) jsonObject.getString("fa");
                                valB = (String) jsonObject1.getString("fa");
                            } catch (JSONException e) {
                                //do something
                            }
                            return valB.compareTo(valA);
                            //if you want to change the sort order, simply use the following:
                            //return -valA.compareTo(valB);
                        }
                    });
                    for (int i = 0; i < jsonArray.length(); i++) {
                        sortedJsonArray.put(jsonValues.get(i));
                    }
                }
                select_device_list.setAdapter(new DeviceListAdapter(SelectDeviceActivity.this, sortedJsonArray));//设置适配器
                break;
            case R.id.device_layout://设备名称
//                date_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
//                selling_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
//                device_text.setTextColor(ContextCompat.getColor(this,R.color.orange));
//                ssid_text.setTextColor(ContextCompat.getColor(this,R.color.blank));

                date_img.setImageResource(R.mipmap.icon_common_sort_up);
                selling_img.setImageResource(R.mipmap.icon_common_sort_up);
                device_img.setImageResource(R.mipmap.icon_common_sort_down);
                uuid_img.setImageResource(R.mipmap.icon_common_sort_up);

                if (jsonArray.length() > 0) {
                    loading.setStatus(LoadingLayout.Success);

                                /*按设备名称排序 */
                    sortedJsonArray = new JSONArray();
                    List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            jsonValues.add(jsonArray.getJSONObject(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Collections.sort(jsonValues, new Comparator<JSONObject>() {
                        //You can change "Name" with "ID" if you want to sort by ID
                        private static final String KEY_NAME = "eqpName";

                        @Override
                        public int compare(JSONObject a, JSONObject b) {
                            String valA = new String();
                            String valB = new String();

                            try {
                                valA = (String) a.getString(KEY_NAME);
                                valB = (String) b.getString(KEY_NAME);
                            } catch (JSONException e) {
                                //do something
                            }

                            return valA.compareTo(valB);
                            //if you want to change the sort order, simply use the following:
                            //return -valA.compareTo(valB);
                        }
                    });
                    for (int i = 0; i < jsonArray.length(); i++) {
                        sortedJsonArray.put(jsonValues.get(i));
                    }
                }
                select_device_list.setAdapter(new DeviceListAdapter(SelectDeviceActivity.this, sortedJsonArray));//设置适配器
                break;
            case R.id.uuid_layout://UUID
//                date_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
//                selling_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
//                device_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
//                ssid_text.setTextColor(ContextCompat.getColor(this,R.color.orange));

                date_img.setImageResource(R.mipmap.icon_common_sort_up);
                selling_img.setImageResource(R.mipmap.icon_common_sort_up);
                device_img.setImageResource(R.mipmap.icon_common_sort_up);
                uuid_img.setImageResource(R.mipmap.icon_common_sort_down);


                if (jsonArray.length() > 0) {
                    loading.setStatus(LoadingLayout.Success);

                                /*按UUID排序 */
                    sortedJsonArray = new JSONArray();
                    List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            jsonValues.add(jsonArray.getJSONObject(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Collections.sort(jsonValues, new Comparator<JSONObject>() {
                        //You can change "Name" with "ID" if you want to sort by ID
                        private static final String KEY_NAME = "macNo";

                        @Override
                        public int compare(JSONObject a, JSONObject b) {
                            String valA = new String();
                            String valB = new String();

                            try {
                                valA = (String) a.getString(KEY_NAME);
                                valB = (String) b.getString(KEY_NAME);
                            } catch (JSONException e) {
                                //do something
                            }

                            return valA.compareTo(valB);
                            //if you want to change the sort order, simply use the following:
                            //return -valA.compareTo(valB);
                        }
                    });
                    for (int i = 0; i < jsonArray.length(); i++) {
                        sortedJsonArray.put(jsonValues.get(i));
                    }
                }
                select_device_list.setAdapter(new DeviceListAdapter(SelectDeviceActivity.this, sortedJsonArray));//设置适配器
                break;
            case R.id.add_device://新增
//                intent=new Intent(SelectDeviceActivity.this,MainLayoutActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                setDialog();
                break;
            case R.id.search_layout://搜索添加
                intent = new Intent(SelectDeviceActivity.this, DeviceSearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.cancel_layout://取消
                mCameraDialog.dismiss();
                break;
            case R.id.scan_layout:
                IntentIntegrator integrator = new IntentIntegrator(SelectDeviceActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("扫描二维码/条形码");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.initiateScan();
                break;
            case R.id.search_img:
                intent = new Intent(SelectDeviceActivity.this, SearchBoxActiivty.class);
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

    //底部弹出菜单
    private void setDialog() {
        LinearLayout root = null;
        mCameraDialog = new Dialog(this, R.style.BottomDialog);
        root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.device_style, null);
        //初始化视图
        root.findViewById(R.id.search_layout).setOnClickListener(this);
        root.findViewById(R.id.scan_layout).setOnClickListener(this);
        root.findViewById(R.id.cancel_layout).setOnClickListener(this);

        TextView search_text = root.findViewById(R.id.search_text);
        TextView scan_text = root.findViewById(R.id.scan_text);
        TextView cancel_text = root.findViewById(R.id.cancel_text);

        if (Locale.getDefault().getLanguage().equals("en")) {
            search_text.setText("Add By Search");
            scan_text.setText("Add By Scan");
            cancel_text.setText("Cancel");
        }

//        default_img=root.findViewById(R.id.default_img);
//        five_img=root.findViewById(R.id.five_img);
//        one_minute_img=root.findViewById(R.id.one_minute_img);

        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.DialogAnimation); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

    //接收扫描结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result.getContents() == null) {
            Toast.makeText(this, "扫描失败", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println("扫描结果：" + result.getContents());
            String mac_str = null;
            try {
                mac_str = result.getContents().substring(result.getContents().indexOf("=") + 1, result.getContents().indexOf("&"));
                System.out.println("截取结果：" + result.getContents().substring(result.getContents().indexOf("=") + 1, result.getContents().indexOf("&")));
            } catch (Exception e) {
                Toast.makeText(this, "该设备不存在", Toast.LENGTH_SHORT).show();
            }
            if (mac_str != null) {
                Intent intent = new Intent(SelectDeviceActivity.this, PerfectDeviceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("device_mac", mac_str);
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


                                /*按销售日期排序 */
                                sortedJsonArray = new JSONArray();
                                List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonValues.add(jsonArray.getJSONObject(i));
                                }
                                Collections.sort(jsonValues, new Comparator<JSONObject>() {
                                    //You can change "Name" with "ID" if you want to sort by ID
                                    private static final String KEY_NAME = "insert_date";

                                    @Override
                                    public int compare(JSONObject a, JSONObject b) {
                                        String valA = new String();
                                        String valB = new String();

                                        try {
                                            valA = (String) a.getString(KEY_NAME);
                                            valB = (String) b.getString(KEY_NAME);
                                        } catch (JSONException e) {
                                            //do something
                                        }

                                        return valA.compareTo(valB);
                                        //if you want to change the sort order, simply use the following:
                                        //return -valA.compareTo(valB);
                                    }
                                });
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    sortedJsonArray.put(jsonValues.get(i));
                                }

                            }
                            if (sortedJsonArray != null) {
                                select_device_list.setAdapter(new DeviceListAdapter(SelectDeviceActivity.this, sortedJsonArray));//设置适配器
                                loading.setStatus(LoadingLayout.Success);
                            }
                            select_device_list.setOnItemClickListener(new OnItemClickListener() {//列表item事件
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(SelectDeviceActivity.this, DeviceDetailActivity.class);
                                    Bundle bundle = new Bundle();
                                    try {
                                        JSONObject object = new JSONObject(sortedJsonArray.get(i).toString());

                                        bundle.putString("device_mac", object.getString("macNo"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });

                            // setListViewHeightBasedOnChildren(device_listview);//计算listview的item个数,并完整显示

                        } else {
                            new AlertDialog.Builder(SelectDeviceActivity.this).setTitle("网络提示").setMessage("请检查网络是否畅通").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
