package com.example.administrator.ding_small;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Administrator on 2017/11/17.
 */

public class AddContactsActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contacts);
        findViewById(R.id.addName).setOnClickListener(this);
        findViewById(R.id.addPhone).setOnClickListener(this);
    }
    private void showNameSetDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.dailog_set_name, null);
        // dialog.setView(view);// 将自定义的布局文件设置给dialog
        dialog.setView(view, 0, 0, 0, 0);// 设置边距为0,保证在2.x的版本上运行没问题

        final EditText set_name = (EditText) view
                .findViewById(R.id.set_name);

        Button btnOK = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        btnOK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = set_name.getText().toString();
                // password!=null && !password.equals("")
                if (!TextUtils.isEmpty(name)) {
                    findViewById(R.id.addName_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.addName).setVisibility(View.GONE);
                    dialog.dismiss();
                } else {
                    Toast.makeText(AddContactsActivity.this,"不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();// 隐藏dialog
            }
        });
        dialog.show();
    }
    private void showPhoneSetDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        View view = View.inflate(this, R.layout.dailog_set_phone, null);
        // dialog.setView(view);// 将自定义的布局文件设置给dialog
        dialog.setView(view, 0, 0, 0, 0);// 设置边距为0,保证在2.x的版本上运行没问题

        final EditText set_name = (EditText) view
                .findViewById(R.id.set_phone);

        Button btnOK = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        btnOK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = set_name.getText().toString();
                // password!=null && !password.equals("")
                if (!TextUtils.isEmpty(name)) {
                    findViewById(R.id.addPhone_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.addPhone).setVisibility(View.GONE);
                    dialog.dismiss();
                } else {
                    Toast.makeText(AddContactsActivity.this,"不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();// 隐藏dialog
            }
        });
        dialog.show();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addName:
                showNameSetDailog();
                break;
            case R.id.addPhone:
                showPhoneSetDailog();
                break;
        }
    }
}
