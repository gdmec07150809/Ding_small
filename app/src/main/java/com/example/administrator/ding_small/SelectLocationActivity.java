package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.example.administrator.ding_small.JsonClass.JsonBean;
import com.example.administrator.ding_small.JsonClass.JsonFileReader;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by CZK on 2018/1/4.
 */

public class SelectLocationActivity extends Activity implements View.OnClickListener {
    private TextView mTvAddress;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private String adress;
    private EditText location;

    private static final String tokeFile = "selectAdressFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    private TextView select_location_text,select_text,unselect_text,confirm_btn;
    //重写onKeyDown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_location);
        initView();
        changeTextView();//更换语言
        initJsonData();
        getCache();
    }

    private void changeTextView() {

        if (Locale.getDefault().getLanguage().equals("en")) {
            //select_location_text,select_text,unselect_text
           select_location_text.setText("Select Location");
            select_text.setText("Select");
            unselect_text.setHint("Unselected");
            confirm_btn.setText("Confirm");
            location.setHint("Fill in the note information");

        }
    }
    private void getCache(){
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        unselect_text.setText(sp.getString("adress", ""));
        location.setText(sp.getString("location", ""));
    }
    private void showPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String text = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                adress = text;
                unselect_text.setText(text);
            }
        }).setTitleText("")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.GRAY)
                .setOutSideCancelable(false)
                .setContentTextSize(18)//滚轮文字大小
                .setSubmitColor(ContextCompat.getColor(this, R.color.theme_color))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(this, R.color.theme_color))//取消按钮文字颜色
                .setTitleBgColor(ContextCompat.getColor(this, R.color.time_bg_color))//标题背景颜色 Night mode
                .setBgColor(ContextCompat.getColor(this, R.color.time_bg_color))//滚轮背景颜色 Night mode
                .build();
            /*pvOptions.setPicker(options1Items);//一级选择器
            pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();

    }

    private void initView() {
        //mTvAddress = (TextView) findViewById(R.id.unselect_text);
        findViewById(R.id.select_layout).setOnClickListener(this);
        confirm_btn=findViewById(R.id.comfir_btn);
        confirm_btn.setOnClickListener(this);
        //select_location_text,select_text,unselect_text
        select_location_text=findViewById(R.id.select_location_text);
        select_text=findViewById(R.id.select_text);
        unselect_text=findViewById(R.id.unselect_text);
        location=findViewById(R.id.location);
    }


    private void initJsonData() {   //解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        //  获取json数据
        String JsonData = JsonFileReader.getJson(this, "province_data.json");
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.select_layout://选择地址提供器
                showPickerView();
                break;
            case R.id.comfir_btn://确定
                intent = new Intent(SelectLocationActivity.this, CreatRepairRemarksActivity.class);
                String location_str=location.getText().toString().trim();
                String adress=unselect_text.getText().toString().trim();
                if(adress.equals("")||adress==null||location_str.equals("")||location_str==null){
                    Toast.makeText(SelectLocationActivity.this,"地址不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    sp = SelectLocationActivity.this.getSharedPreferences(tokeFile, MODE_PRIVATE);//实例化
                    SharedPreferences.Editor editor = sp.edit(); //使处于可编辑状态

                    editor.putString("adress", adress);
                    editor.putString("location", location_str);
                    editor.commit();    //提交数据保存


                    String detailStr=adress+location_str;
                    intent.putExtra("adress", detailStr);
                    intent.putExtra("location",location_str);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }
}
