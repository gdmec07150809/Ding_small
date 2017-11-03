package com.example.administrator.ding_small;

import android.app.AlertDialog;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;


public class MainActivity extends Activity implements  View.OnClickListener{
    private EditText user_name,user_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.left).setOnClickListener(this);
        findViewById(R.id.right).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        user_name=findViewById(R.id.user_name);
        user_password=findViewById(R.id.user_password);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.left:
                findViewById(R.id.l_bottom).setBackgroundColor(getResources().getColor(R.color.green));
                findViewById(R.id.r_bottom).setBackgroundColor(getResources().getColor(R.color.white));
                findViewById(R.id.login_xml).setVisibility(View.VISIBLE);
                findViewById(R.id.register_xml).setVisibility(View.GONE);
                break;
            case R.id.right:
                findViewById(R.id.l_bottom).setBackgroundColor(getResources().getColor(R.color.white));
                findViewById(R.id.r_bottom).setBackgroundColor(getResources().getColor(R.color.green));
                findViewById(R.id.login_xml).setVisibility(View.GONE);
                findViewById(R.id.register_xml).setVisibility(View.VISIBLE);
                break;
            case R.id.login:
                String name=user_name.getText().toString();
                String password=user_password.getText().toString();
                System.out.println("用户密码："+name+":"+password);
                //判断是否为空
                if(name.equals("")|| password.equals("")){
                    new AlertDialog.Builder(MainActivity.this).setTitle("登录提示").setMessage("用户名或密码不能为空！！！").setPositiveButton("确定",null).show();
                }else {
                    Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
        }
    }
}
