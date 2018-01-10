package com.example.administrator.ding_small.PersonalCenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity;
import com.example.administrator.ding_small.R;

import java.util.Date;

/**
 * Created by CZK on 2018/1/9.
 */

public class ChangePhoneActivity extends Activity implements View.OnClickListener {
    private TextView back, phone;
    private Dialog mCameraDialog;

    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    String memid, token, sign, newPass, ts, c_newPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_phone_layout);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.confirm_change).setOnClickListener(this);
        phone = findViewById(R.id.phone);


    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.confirm_change:
                setPhoneDialog();
                break;
        }
    }

    //更换手机底部弹出菜单
    private void setPhoneDialog() {
        LinearLayout root = null;
        mCameraDialog = new Dialog(this, R.style.Dialog);
        root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.change_phone_dialog, null);
        Button new_login = root.findViewById(R.id.new_login);
        final EditText editText = root.findViewById(R.id.newPhone_value);
        new_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //判断输入的手机号是否和上次一样
                if (phone.getText().toString().equals(editText.getText().toString())) {
                    new AlertDialog.Builder(ChangePhoneActivity.this).setTitle("更换手机").setMessage("不能跟上次手机一样").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                } else {
                    Toast.makeText(ChangePhoneActivity.this, "更换成功", Toast.LENGTH_SHORT).show();
                    mCameraDialog.dismiss();
                    finish();
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
}
