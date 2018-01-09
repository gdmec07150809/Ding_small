package com.example.administrator.ding_small.PersonalCenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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

import com.example.administrator.ding_small.R;

/**
 * Created by youyou000 on 2018/1/9.
 */

public class ChangePhoneActivity extends Activity implements View.OnClickListener{
    private TextView back;
    private Dialog mCameraDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_phone_layout);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.confirm_change).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
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
        LinearLayout root=null;
        mCameraDialog = new Dialog(this, R.style.Dialog);
        root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.change_phone_dialog, null);
        Button new_login=root.findViewById(R.id.new_login);
        final EditText editText=root.findViewById(R.id.newPhone_value);
        new_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
}
