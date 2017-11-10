package com.example.administrator.ding_small;

/**
 * Created by Administrator on 2017/10/20.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;


public class NotepadActivity  extends Activity implements View.OnClickListener{
    LinearLayout ll,two;
    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notepad);
        ll=findViewById(R.id.main_ll);
        two = this.findViewById(R.id.two);
        findViewById(R.id.received).setOnClickListener(this);
        findViewById(R.id.payable).setOnClickListener(this);
        findViewById(R.id.click_btn).setOnClickListener(this);
        findViewById(R.id.remarks).setOnClickListener(this);
        initPoints();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.click_btn:
                intent=new Intent(NotepadActivity.this,TitleActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case R.id.received:
                intent=new Intent(NotepadActivity.this,ReceivedActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case R.id.payable:
                intent=new Intent(NotepadActivity.this,PayableActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
            case R.id.remarks:
                intent=new Intent(NotepadActivity.this,RemarksActivity.class);
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
