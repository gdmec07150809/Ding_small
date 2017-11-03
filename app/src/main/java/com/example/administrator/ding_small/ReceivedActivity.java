package com.example.administrator.ding_small;

/**
 * Created by Administrator on 2017/10/21.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReceivedActivity extends Activity implements View.OnClickListener{
    LinearLayout ll;
    private TextView number,number_enter;
    private int index=0;
    private int small_index=0;
    private ArrayList<String> arrays=null;
    private String addNumber="";
    private double sum=0;
    private boolean isAdd=true;
    private boolean isFrist=true;
    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creat_received);
        findViewById(R.id.number_0).setOnClickListener(this);
        findViewById(R.id.number_1).setOnClickListener(this);
        findViewById(R.id.number_2).setOnClickListener(this);
        findViewById(R.id.number_3).setOnClickListener(this);
        findViewById(R.id.number_4).setOnClickListener(this);
        findViewById(R.id.number_5).setOnClickListener(this);
        findViewById(R.id.number_6).setOnClickListener(this);
        findViewById(R.id.number_7).setOnClickListener(this);
        findViewById(R.id.number_8).setOnClickListener(this);
        findViewById(R.id.number_9).setOnClickListener(this);
        findViewById(R.id.number_add).setOnClickListener(this);
        findViewById(R.id.number_remove).setOnClickListener(this);
        findViewById(R.id.number_clear_last).setOnClickListener(this);
        findViewById(R.id.number_small).setOnClickListener(this);
        findViewById(R.id.payable).setOnClickListener(this);
        findViewById(R.id.notepad).setOnClickListener(this);
        number= (TextView) findViewById(R.id.number);
        number_enter=findViewById(R.id.number_enter);
        number_enter.setOnClickListener(this);
        arrays=new ArrayList<String>();
        initPoints();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.number_0:
                if(index==0){
                    if(number.getText().toString().substring(0,1).equals("0")){
                        if(number.getText().toString().length()>1){
                            if(number.getText().toString().substring(1,2).equals(".")) {
                                number.setText(number.getText().toString().substring(0) + "0");
                                index++;
                                break;
                            }else{
                                number.setText(number.getText().toString().substring(0) + "0");
                                index=0;
                                break;
                            }
                        }else if(number.getText().toString().length()==1){
                            number.setText("0");
                            index=0;
                            break;
                        }
                    }
                }else{
                    number.setText(number.getText().toString().substring(0)+"0");
                }
                index++;
                break;
            case R.id.number_1:
                if(index==0){
                    if(number.getText().toString().substring(0,1).equals("0")){
                        number.setText(number.getText().toString().substring(1)+"1");
                    }
                }else{
                    number.setText(number.getText().toString().substring(0)+"1");
                }
                index++;
                break;
            case R.id.number_2:
                if(index==0){
                    if(number.getText().toString().substring(0,1).equals("0")){
                        number.setText(number.getText().toString().substring(1)+"2");
                    }
                }else{
                    number.setText(number.getText().toString().substring(0)+"2");
                }
                index++;
                break;
            case R.id.number_3:
                if(index==0){
                    if(number.getText().toString().substring(0,1).equals("0")){
                        number.setText(number.getText().toString().substring(1)+"3");
                    }
                }else{
                    number.setText(number.getText().toString().substring(0)+"3");
                }
                index++;
                break;
            case R.id.number_4:
                if(index==0){
                    if(number.getText().toString().substring(0,1).equals("0")){
                        number.setText(number.getText().toString().substring(1)+"4");
                    }
                }else{
                    number.setText(number.getText().toString().substring(0)+"4");
                }
                index++;
                break;
            case R.id.number_5:
                if(index==0){
                    if(number.getText().toString().substring(0,1).equals("0")){
                        number.setText(number.getText().toString().substring(1)+"5");
                    }
                }else{
                    number.setText(number.getText().toString().substring(0)+"5");
                }
                index++;
                break;
            case R.id.number_6:
                if(index==0){
                    if(number.getText().toString().substring(0,1).equals("0")){
                        number.setText(number.getText().toString().substring(1)+"6");
                    }
                }else{
                    number.setText(number.getText().toString().substring(0)+"6");
                }
                index++;
                break;
            case R.id.number_7:
                if(index==0){
                    if(number.getText().toString().substring(0,1).equals("0")){
                        number.setText(number.getText().toString().substring(1)+"7");
                    }
                }else{
                    number.setText(number.getText().toString().substring(0)+"7");
                }
                index++;
                break;
            case R.id.number_8:
                if(index==0){
                    if(number.getText().toString().substring(0,1).equals("0")){
                        number.setText(number.getText().toString().substring(1)+"8");
                    }
                }else{
                    number.setText(number.getText().toString().substring(0)+"8");
                }
                index++;
                break;
            case R.id.number_9:
                if(index==0){
                    if(number.getText().toString().substring(0,1).equals("0")){
                        number.setText(number.getText().toString().substring(1)+"9");
                    }
                }else{
                    number.setText(number.getText().toString().substring(0)+"9");
                }
                index++;
                break;
            case R.id.number_small:
                if(small_index==0){
                    number.setText(number.getText().toString().substring(0)+".");
                    small_index++;
                    index++;

                }else{
                    small_index++;
                    index++;
                }
                break;
            case R.id.number_clear_last:
                arrays.clear();
                number.setText("0");
                index=0;
                small_index=0;
                break;

            case R.id.number_add:
                sum+= Double.valueOf(number.getText().toString());
                number.setText("0");
                index=0;
                isAdd=true;
                small_index=0;
                number_enter.setText("=");
                break;
            case R.id.number_remove:
                if(isFrist){
                    sum+= Double.valueOf(number.getText().toString());
                }else {
                    sum-= Double.valueOf(number.getText().toString());
                }
                number.setText("0");
                index=0;
                small_index=0;
                isAdd=false;
                number_enter.setText("=");
                break;
            case R.id.number_enter:
                if(isAdd==true){
                    sum+= Double.valueOf(number.getText().toString());
                }else{
                    sum-= Double.valueOf(number.getText().toString());
                }
                number.setText(sum+"");
                sum=0;
                number_enter.setText("ok");
                arrays.clear();
                break;
            case R.id.notepad:
                intent=new Intent(ReceivedActivity.this,NotepadActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case R.id.payable:
                intent=new Intent(ReceivedActivity.this,PayableActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
        }
    }
    /**
     * 实现小圆点的添加，
     * 找到线性布局动态的向线性布局内添加小圆，并添加drawable选择的效果
     */
    private void initPoints() {
        //实例化线性布局
        ll = (LinearLayout) findViewById(R.id.main_ll);
        //绘制和图片对应的圆点的数量
        for (int i = 0; i < 4; i++) {
            View view = new View(this);
            //设置圆点的大小
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
            //设置间距
            params.setMargins(10, 10, 10, 10);
            //设置图片的自定义效果
            view.setBackgroundResource(R.drawable.start_selsect);
            //把设置好的视图属性设置到View中
            view.setLayoutParams(params);
            //把创建好的View添加到线性布局中
            ll.addView(view);
        }
        Log.e("TAG", ":" + ll.getChildCount());
        //设置选中线性布局中的第一个
        ll.getChildAt(0).setSelected(true);
    }
}
