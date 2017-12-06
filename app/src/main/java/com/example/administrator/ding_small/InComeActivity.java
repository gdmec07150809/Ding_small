package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.ding_small.Adapter.InComeAdapter;
import com.example.administrator.ding_small.HelpTool.FlowLayout;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/12/6.
 */

public class InComeActivity extends Activity implements View.OnClickListener{
    @ViewInject(R.id.found_activity_fyt)
    private FlowLayout found_activity_fyt;
    @ViewInject(R.id.found_history_ryt)
    private LinearLayout found_history_ryt;
    private String[] strs = {"美的", "格力", "飞利浦", "方太", "西门子", "A.O.史密斯", "爱马仕", "奔腾", "TCL", "小天鹅", "三洋"};
    private int[] number={12,54,50,20,40,45,16,56,78,123,12};
    private int[] money={12,54,-50,20,-40,45,-16,56,78,-123,12};
    private int [] color={R.color.bg1,R.color.bg2,R.color.bg3,R.color.bg4,R.color.bg5,R.color.bg6,R.color.bg7,R.color.bg8,R.color.bg9,R.color.bg10,R.color.bg11};
    private int sum;
    private ListView analasis_list;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    int viewWidth;
    private TextView summary_text,income_text;
    private RelativeLayout f_summary,f_income;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_income);
        found_history_ryt=findViewById(R.id.found_history_ryt);
        analasis_list=findViewById(R.id.income_list);
        findViewById(R.id.f_summary).setOnClickListener(this);
        summary_text=findViewById(R.id.summary_text);
        income_text=findViewById(R.id.income_text);

        summary_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
        income_text.setTextColor(ContextCompat.getColor(this, R.color.green));

        ViewUtils.inject(this);
        /*获取屏幕宽度*/
        WindowManager  windowManager=getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        viewWidth  = dm.widthPixels-20 ;
        System.out.println("手机宽度："+viewWidth);
        //计算总笔数
        for (int i:number){
            sum+=i;
        }
        CreatJson();//组合一个jsonArray备用
        //添加比例布局
        for (int i = 0; i < strs.length; i++) {
            final TextView text = new TextView(InComeActivity.this);
            text.setBackgroundResource(color[i]);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(viewWidth*number[i]/sum, LayoutParams.MATCH_PARENT);//设置宽高,第一个参数是宽,第二个参数是高
            //设置边距
            params.topMargin = 0;
            params.bottomMargin = 0;
            params.leftMargin = 0;
            params.rightMargin = 0;
            text.setLayoutParams(params);
            found_activity_fyt.addView(text);//将内容添加到布局中
        }
        //给布局添加动画
        Animation myAnimation = AnimationUtils.loadAnimation(this, R.anim.income_anim);
        found_history_ryt.startAnimation(myAnimation);

        analasis_list.setAdapter(new InComeAdapter(InComeActivity.this,jsonArray));
    }
    private void CreatJson(){
        jsonArray=new JSONArray();
        for(int i=0;i<color.length;i++){
            jsonObject=new JSONObject();
            try {
                jsonObject.put("color",color[i]);
                jsonObject.put("title",strs[i]);
                jsonObject.put("number",number[i]);
                jsonObject.put("money",money[i]);
                jsonObject.put("sum",sum);
                jsonObject.put("width",viewWidth-80);
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.f_summary:
                intent=new Intent(InComeActivity.this,AccountBookReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}
