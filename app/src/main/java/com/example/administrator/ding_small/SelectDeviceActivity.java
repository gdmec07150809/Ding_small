package com.example.administrator.ding_small;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.administrator.ding_small.R.id.device_listview;
import static com.example.administrator.ding_small.R.id.device_type;


/**
 * Created by Administrator on 2017/12/19.
 */

public class SelectDeviceActivity extends Activity implements View.OnClickListener{
    private JSONArray jsonArray;
    private ArrayList<String> device_names;
    private ArrayList<String> device_locations;
    private ArrayList<String> device_ssids;
    private ArrayList<String> device_staus;
    private ArrayList<String> device_date;
    private ListView select_device_list;
    private TextView date_text,selling_text,device_text,ssid_text;
    private EditText search_edittext;
    private Dialog mCameraDialog;
    private ImageView date_img,selling_img,device_img,uuid_img;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_new_device);
        init();//初始化控件
        CreatJson();//构造jsonArray备用
        select_device_list.setAdapter(new DeviceListAdapter(SelectDeviceActivity.this,jsonArray));//设置适配器
        select_device_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(SelectDeviceActivity.this,DeviceDetailActivity.class);
                Bundle bundle=new Bundle();
                try {
                    JSONObject object=new JSONObject(jsonArray.get(i).toString());
                    bundle.putString("device_name",object.getString("device_name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    private void init(){
        select_device_list=findViewById(R.id.select_device_list);//设备列表
        findViewById(R.id.start_layout).setOnClickListener(this);//启用日期
        findViewById(R.id.selling_layout).setOnClickListener(this);//售点名称
        findViewById(R.id.device_layout).setOnClickListener(this);//设备名称
        findViewById(R.id.uuid_layout).setOnClickListener(this);//SSID
        findViewById(R.id.add_device).setOnClickListener(this);//新增设备
        findViewById(R.id.back).setOnClickListener(this);//返回
//        findViewById(R.id.clean_text).setOnClickListener(this);//清空搜索框
//        findViewById(R.id.search_btn).setOnClickListener(this);//查询

//        date_text=findViewById(R.id.date_text);
//        selling_text=findViewById(R.id.selling_text);
//        device_text=findViewById(R.id.device_text);
//        ssid_text=findViewById(R.id.uuid_text);
//        search_edittext=findViewById(R.id.search_edittext);

        date_img=findViewById(R.id.date_img);
        selling_img=findViewById(R.id.selling_img);
        device_img=findViewById(R.id.device_img);
        uuid_img=findViewById(R.id.uuid_img);
    }
    private void CreatJson(){
        jsonArray=new JSONArray();
        device_names=new ArrayList<String>();
        device_locations=new ArrayList<String>();
        device_ssids=new ArrayList<String>();
        device_staus=new ArrayList<String>();
        device_date=new ArrayList<String>();

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
            for (int i=0;i<device_names.size();i++){
                JSONObject jsonObject;
                jsonObject=new JSONObject();
                jsonObject.put("device_name",device_names.get(i));
                jsonObject.put("device_location",device_locations.get(i));
                jsonObject.put("device_ssid",device_ssids.get(i));
                jsonObject.put("device_state",device_staus.get(i));
                jsonObject.put("device_date",device_date.get(i));
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
        switch (view.getId()){
            case R.id.start_layout://启用日期
//                date_text.setTextColor(ContextCompat.getColor(this,R.color.orange));
//                selling_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
//                device_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
//                ssid_text.setTextColor(ContextCompat.getColor(this,R.color.blank));

                date_img.setImageResource(R.mipmap.icon_common_sort_down);
                selling_img.setImageResource(R.mipmap.icon_common_sort_up);
                device_img.setImageResource(R.mipmap.icon_common_sort_up);
                uuid_img.setImageResource(R.mipmap.icon_common_sort_up);
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
                break;
            case R.id.uuid_layout://SSID
//                date_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
//                selling_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
//                device_text.setTextColor(ContextCompat.getColor(this,R.color.blank));
//                ssid_text.setTextColor(ContextCompat.getColor(this,R.color.orange));

                date_img.setImageResource(R.mipmap.icon_common_sort_up);
                selling_img.setImageResource(R.mipmap.icon_common_sort_up);
                device_img.setImageResource(R.mipmap.icon_common_sort_up);
                uuid_img.setImageResource(R.mipmap.icon_common_sort_down);
                break;
            case R.id.add_device://新增
//                intent=new Intent(SelectDeviceActivity.this,MainLayoutActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                setDialog();
                break;
            case R.id.search_layout://搜索添加
                intent=new Intent(SelectDeviceActivity.this,DeviceSearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.cancel_layout://取消
                mCameraDialog.dismiss();
                break;
            case R.id.scan_layout:
                IntentIntegrator integrator=new IntentIntegrator(SelectDeviceActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("扫描二维码/条形码");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.initiateScan();
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
        LinearLayout root=null;
        mCameraDialog = new Dialog(this, R.style.BottomDialog);
        root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.device_style, null);
        //初始化视图
        root.findViewById(R.id.search_layout).setOnClickListener(this);
        root.findViewById(R.id.scan_layout).setOnClickListener(this);
        root.findViewById(R.id.cancel_layout).setOnClickListener(this);

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
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result.getContents()==null){
            Toast.makeText(this,"扫描失败",Toast.LENGTH_SHORT).show();
        }else{
            System.out.println("扫描结果："+result.getContents());
            Intent intent=new Intent(SelectDeviceActivity.this,PerfectDeviceActivity.class);
            startActivity(intent);
            //resultNew.setText("扫描结果："+result.getContents());
        }
    }
}
