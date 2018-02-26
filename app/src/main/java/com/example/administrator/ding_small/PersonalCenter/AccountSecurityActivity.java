package com.example.administrator.ding_small.PersonalCenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.ding_small.R;
import com.example.administrator.ding_small.Utils.SysApplication;

import java.util.Locale;

/**
 * Created by CZK on 2018/start1/start2.
 */

public class AccountSecurityActivity extends Activity implements View.OnClickListener {
    //更改语言所要更改的控件 返回、账户安全、修改密码、更换手机
    private TextView back_text, account_security_text, edit_password_text, change_phone_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_security);
        SysApplication.getInstance().addActivity(this);
        findViewById(R.id.edit_password_layout).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.change_layout).setOnClickListener(this);
        back_text = findViewById(R.id.back_text);
        account_security_text = findViewById(R.id.account_security_text);
        edit_password_text = findViewById(R.id.edit_password_text);
        change_phone_text = findViewById(R.id.change_phone_text);

        changeTextView();//更改语言
    }

    private void changeTextView() {
        if (Locale.getDefault().getLanguage().equals("en")) {
            back_text.setText("Back");
            account_security_text.setText("Account Security");
            edit_password_text.setText("Edit PassWord");
            change_phone_text.setText("Change Phone");
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.edit_password_layout:
                intent = new Intent(AccountSecurityActivity.this, EditPassWordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.change_layout:
                intent = new Intent(AccountSecurityActivity.this, ChangePhoneActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
