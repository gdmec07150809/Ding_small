package com.example.administrator.ding_small;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

/**
 * Created by CZK on 2017/11/13.
 */

public class SearchTimeActivity extends Activity {
    private TextView start_date, end_date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_time);
        start_date = findViewById(R.id.start_time);
        end_date = findViewById(R.id.end_time);
        Button btn = findViewById(R.id.confirm);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePicker datePicker = (DatePicker) findViewById(R.id.start_picker);
                final DatePicker end_datePicker = (DatePicker) findViewById(R.id.end_picker);
                //日期格式
                String start_date_str = "";
                String end_date_str = "";
                start_date_str = String.valueOf(datePicker.getYear() + "年" + (datePicker.getMonth() + 1) + "月" + datePicker.getDayOfMonth() + "日");
                end_date_str = String.valueOf(end_datePicker.getYear() + "年" + (end_datePicker.getMonth() + 1) + "月" + end_datePicker.getDayOfMonth() + "日");
                start_date.setText(start_date_str);
                end_date.setText(end_date_str);
            }
        });
    }
}
