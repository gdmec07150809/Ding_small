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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
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

public class ExpenditureActivity extends Activity implements View.OnClickListener{
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
    private TextView summary_text,label_text,income_text,expenditure_text,outtime_text;
    private RelativeLayout f_summary,f_income;
    private Button received_btn,all_btn,yet_btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_expenditure);
        found_history_ryt=findViewById(R.id.found_history_ryt);
        analasis_list=findViewById(R.id.income_list);

        findViewById(R.id.f_summary).setOnClickListener(this);
        findViewById(R.id.f_label).setOnClickListener(this);
        findViewById(R.id.f_income).setOnClickListener(this);
        findViewById(R.id.f_expenditure).setOnClickListener(this);
        findViewById(R.id.f_outtime).setOnClickListener(this);

        summary_text=findViewById(R.id.summary_text);
        label_text=findViewById(R.id.label_text);
        income_text=findViewById(R.id.income_text);
        expenditure_text=findViewById(R.id.expenditure_text);
        outtime_text=findViewById(R.id.outtime_text);
        all_btn= findViewById(R.id.all_btn);
        received_btn= findViewById(R.id.received_btn);
        yet_btn= findViewById(R.id.yet_btn);
        all_btn.setOnClickListener(this);
        received_btn.setOnClickListener(this);
        yet_btn.setOnClickListener(this);

        label_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
        summary_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
        income_text.setTextColor(ContextCompat.getColor(this, R.color.blank));
        expenditure_text.setTextColor(ContextCompat.getColor(this, R.color.green));
        outtime_text.setTextColor(ContextCompat.getColor(this, R.color.blank));

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
            final TextView text = new TextView(ExpenditureActivity.this);
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

        analasis_list.setAdapter(new InComeAdapter(ExpenditureActivity.this,jsonArray));

        analasis_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("点击");
                Intent intent=new Intent(ExpenditureActivity.this,AccountInComeItemDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title",strs[i]);
                bundle.putInt("number",number[i]);
                bundle.putInt("color",color[i]);
                bundle.putString("name","支出");
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
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
                intent=new Intent(ExpenditureActivity.this,AccountBookReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_label:
                intent=new Intent(ExpenditureActivity.this,AccountAnalysisLabelStatisticsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.f_income:
                intent=new Intent(ExpenditureActivity.this,InComeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.received_btn:
                received_btn.setTextColor(getResources().getColor(R.color.white));
                all_btn.setTextColor(getResources().getColor(R.color.green));
                yet_btn.setTextColor(getResources().getColor(R.color.green));

                received_btn.setBackgroundColor(getResources().getColor(R.color.green));
                all_btn.setBackgroundResource(R.drawable.bg_gray);
                yet_btn.setBackgroundResource(R.drawable.bg_gray);
                break;
            case R.id.all_btn:
                received_btn.setTextColor(getResources().getColor(R.color.green));
                all_btn.setTextColor(getResources().getColor(R.color.white));
                yet_btn.setTextColor(getResources().getColor(R.color.green));

                received_btn.setBackgroundResource(R.drawable.bg_gray);
                all_btn.setBackgroundColor(getResources().getColor(R.color.green));
                yet_btn.setBackgroundResource(R.drawable.bg_gray);
                break;
            case R.id.yet_btn:
                received_btn.setTextColor(getResources().getColor(R.color.green));
                all_btn.setTextColor(getResources().getColor(R.color.green));
                yet_btn.setTextColor(getResources().getColor(R.color.white));

                received_btn.setBackgroundResource(R.drawable.bg_gray);
                all_btn.setBackgroundResource(R.drawable.bg_gray);
                yet_btn.setBackgroundColor(getResources().getColor(R.color.green));
                break;
            case R.id.f_outtime:
                intent=new Intent(ExpenditureActivity.this,AccountOutTimeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
