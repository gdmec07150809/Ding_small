package com.example.administrator.ding_small;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.content.Intent;



public class MainActivity extends Activity implements  View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.left).setOnClickListener(this);
        findViewById(R.id.right).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);

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
                Intent intent=new Intent(MainActivity.this,ContactsActivity.class);
                startActivity(intent);
        }
    }
}
