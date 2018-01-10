package com.example.administrator.ding_small;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

/**
 * Created by CZK on 2017/11/3.
 */

public class ContactsDetailsToActivity extends Activity {
    private TextView nameView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_details_activity);
        String name = getIntent().getStringExtra("name");
        nameView = findViewById(R.id.name);
        nameView.setText(name);
    }
}
