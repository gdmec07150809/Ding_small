package com.example.administrator.ding_small.PersonalCenter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.test.ServiceTestCase;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.example.administrator.ding_small.CreatRepairRemarksActivity;
import com.example.administrator.ding_small.HelpTool.LocationUtil;
import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.example.administrator.ding_small.JsonClass.JsonBean;
import com.example.administrator.ding_small.JsonClass.JsonFileReader;
import com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity;
import com.example.administrator.ding_small.MainLayoutActivity;
import com.example.administrator.ding_small.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.administrator.ding_small.HelpTool.LocationUtil.getAddress;

/**
 * Created by Administrator on 2017/11/6.
 */

public class PersonalCenterPerfectActivity extends Activity implements View.OnClickListener{
    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp=null;//定义储存源，备用
    String memid,token,sign,oldPass,newPass,ts,c_newPass;
    public static final int SHOW_RESPONSE = 0;
    private static  final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION  = 100;

    //更换语言所要更改的控件
    private TextView back_text,perfct_personal_text,change_avatar_text,nickname_text,sex_text,region_text,address_text,signature_text,switch_account_text,next;

    private ImageView head_img;
    private Dialog mCameraDialog;
    private TextView sex_value,location_value,nickname_value_text,address_value,signature_value;

    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private String adress,str_location;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfect_personal);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.head3).setOnClickListener(this);
        head_img=findViewById(R.id.head_img);
        head_img.setOnClickListener(this);
        findViewById(R.id.sex_layout).setOnClickListener(this);
        findViewById(R.id.location_layout).setOnClickListener(this);
        findViewById(R.id.nickname_layout).setOnClickListener(this);
        findViewById(R.id.address_layout).setOnClickListener(this);
        findViewById(R.id.signature_layout).setOnClickListener(this);
        findViewById(R.id.save_layout).setOnClickListener(this);
//        findViewById(R.id.confirm).setOnClickListener(this);
        back_text=findViewById(R.id.back_text);
        perfct_personal_text=findViewById(R.id.perfct_personal_text);
        change_avatar_text=findViewById(R.id.change_avatar_text);
        nickname_text=findViewById(R.id.nickname_text);
        sex_text=findViewById(R.id.sex_text);
        region_text=findViewById(R.id.region_text);
        address_text=findViewById(R.id.address_text);
        signature_text=findViewById(R.id.signature_text);
        switch_account_text=findViewById(R.id.switch_account_text);
        next=findViewById(R.id.next);
        sex_value=findViewById(R.id.sex_value);
        location_value=findViewById(R.id.location_value);
        nickname_value_text=findViewById(R.id.nickname_value_text);
        address_value=findViewById(R.id.address_value);
        signature_value=findViewById(R.id.signature_value);
        getLocation();//获取地址
        changeTextView();//更改语言
        getCache();
        initJsonData();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocation(){
        //获取地址
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

        } else {
            LocationUtil.initLocation(PersonalCenterPerfectActivity.this);
            System.out.println("主经度:"+Double.toString(LocationUtil.longitude)+"主纬度："+Double.toString(LocationUtil.latitude));
//            longitude_str=Double.toString(LocationUtil.longitude);
//            latitude_str=Double.toString(LocationUtil.latitude);
            try {
                str_location= getAddress(LocationUtil.location,getApplicationContext());
                location_value.setText(str_location);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void changeTextView(){
        if(Locale.getDefault().getLanguage().equals("en")){
            back_text.setText("Back");
            perfct_personal_text.setText("Perfct Personal");
            change_avatar_text.setText("Change Avatar");
            nickname_text.setText("NickName");
            sex_text.setText("Sex");
            region_text.setText("Region");
            address_text.setText("Address");
            signature_text.setText("Signature");
            switch_account_text.setText("Switch Account");
            next.setText("Save");
        }
    }

    private  void getCache() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");
        String url = "http://120.76.188.131:8080/a10/api/user/logout.do";
        ts = String.valueOf(new Date().getTime());
        System.out.println("首页：" + memid + "  ts:" + ts+ "  token:" + token);
        String Sign = url + memid + token + ts;
        System.out.println("Sign:"+Sign);
        sign = MD5Utils.md5(Sign);
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.confirm:
                intent=new Intent(PersonalCenterPerfectActivity.this,PersonalCenterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.head_img:
                intent= new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 21);
                break;
            case R.id.sex_layout:
                setDialog();
                break;
            case R.id.nickname_layout:
                setNichNameDialog();
                break;
            case R.id.location_layout:
                showPickerView();
                break;
            case R.id.address_layout:
                setAdressDialog();
                break;
            case R.id.signature_layout:
                setSignatureDialog();
                break;
            case R.id.save_layout:
                Toast.makeText(this,"保存",Toast.LENGTH_SHORT).show();
                break;
            case R.id.head3:
                new Thread(networkTask).start();//登出
                break;
        }
    }

    /**
     * 网络操作相关的子线程okhttp框架  登出
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            String url = "http://120.76.188.131:8080/a10/api/user/logout.do?memId=" + memid + "&ts=" + ts;
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
                    loginHandler.sendMessage(message);
                }
                System.out.println("结果："+result+"状态码："+ response.code());
                //Toast.makeText(EditPassWordActivity.this,result,Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private Handler loginHandler = new Handler() {

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
                            new AlertDialog.Builder(PersonalCenterPerfectActivity.this).setTitle("登出提示").setMessage("登出成功,返回登录").setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(PersonalCenterPerfectActivity.this,LoginAcitivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).show();
                        }else{
                            new AlertDialog.Builder(PersonalCenterPerfectActivity.this).setTitle("登出提示").setMessage("请先登陆").setPositiveButton("确定",new DialogInterface.OnClickListener() {
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

    //获取,处理拍照事件
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //判断那个相机回调
        switch (requestCode){
            case 21:
                //打开相册并选择照片，这个方式选择单张
                // 获取返回的数据，这里是android自定义的Uri地址
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    // 获取选择照片的数据视图
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    // 从数据视图中获取已选择图片的路径
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                   Toast.makeText(this,picturePath,Toast.LENGTH_SHORT).show();
                    System.out.println(picturePath);
                    cursor.close();
                    // 将图片显示到界面上
                    head_img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                }
                break;
            default:
                break;
        }

    }

    //性别底部弹出菜单
    private void setDialog() {
        LinearLayout root=null;
        mCameraDialog = new Dialog(this, R.style.Dialog);
        root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.select_sex_layout, null);

        RadioGroup radioGroup=root.findViewById(R.id.group);


        if(sex_value.getText().toString().equals("男")){
            RadioButton radioButton=root.findViewById(R.id.button1);
            radioButton.setChecked(true);
        }else{
            RadioButton radioButton=root.findViewById(R.id.button2);
            radioButton.setChecked(true);
        }

        // 单选按钮组监听事件
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 根据ID判断选择的按钮
                if (checkedId == R.id.button1) {
                    sex_value.setText("男");
                    mCameraDialog.dismiss();
                }else {
                    sex_value.setText("女");
                    mCameraDialog.dismiss();
                }
            }
        });

        //初始化视图
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
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

    //昵称底部弹出菜单
    private void setNichNameDialog() {
        LinearLayout root=null;
        mCameraDialog = new Dialog(this, R.style.Dialog);
        root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.change_name_layout, null);
        Button new_login=root.findViewById(R.id.new_login);

        final EditText editText=root.findViewById(R.id.nickname_value);
        editText.setText(nickname_value_text.getText().toString());
        new_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    nickname_value_text.setText(editText.getText().toString());
                    mCameraDialog.dismiss();
            }
        });

        //初始化视图
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
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

    //详细地址底部弹出菜单
    private void setAdressDialog() {
        LinearLayout root=null;
        mCameraDialog = new Dialog(this, R.style.Dialog);
        root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.change_name_layout, null);
        Button new_login=root.findViewById(R.id.new_login);
        final EditText editText=root.findViewById(R.id.nickname_value);
        editText.setText(address_value.getText().toString());
        new_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address_value.setText(editText.getText().toString());
                mCameraDialog.dismiss();
            }
        });

        //初始化视图
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
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

    //个性签名底部弹出菜单
    private void setSignatureDialog() {
        LinearLayout root=null;
        mCameraDialog = new Dialog(this, R.style.Dialog);
        root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.change_name_layout, null);
        Button new_login=root.findViewById(R.id.new_login);
        final EditText editText=root.findViewById(R.id.nickname_value);
        new_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signature_value.setText(editText.getText().toString());
                mCameraDialog.dismiss();
            }
        });

        //初始化视图
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
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

    private void showPickerView() {
        OptionsPickerView pvOptions=new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String text = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                adress=text;
               location_value.setText(text);
            }
        }).setTitleText("")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.GRAY)
                .setContentTextSize(13)
                .setOutSideCancelable(false)
                .setContentTextSize(18)//滚轮文字大小
                .setSubmitColor(ContextCompat.getColor(this,R.color.theme_color))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(this,R.color.theme_color))//取消按钮文字颜色
                .setTitleBgColor(ContextCompat.getColor(this,R.color.time_bg_color))//标题背景颜色 Night mode
                .setBgColor(ContextCompat.getColor(this,R.color.time_bg_color))//滚轮背景颜色 Night mode
                .build();
          /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
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
}
