package com.example.administrator.ding_small.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.ding_small.HelpTool.FlowLayout;
import com.example.administrator.ding_small.InComeActivity;
import com.example.administrator.ding_small.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import static android.R.id.list;
import static com.example.administrator.ding_small.R.id.color;
import static com.example.administrator.ding_small.R.id.flow_layout;
import static com.example.administrator.ding_small.R.id.found_activity_fyt;
import static com.example.administrator.ding_small.R.id.found_history_ryt;
import static com.example.administrator.ding_small.R.id.number;

/**
 * Created by Administrator on 2017/12/6.
 */

public class InComeAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder holder;
    private JSONArray list=null;
    public InComeAdapter(Context context, JSONArray list) {
        this.context = context;
        holder = new ViewHolder();
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.length();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View contentView = null;
        if (contentView == null ){
            contentView = LayoutInflater.from(context).inflate(R.layout.account_income_item,viewGroup,false);
            holder.title=contentView.findViewById(R.id.title);
            holder.explain=contentView.findViewById(R.id.explain);
            holder.money=contentView.findViewById(R.id.money);
            holder.flow_layout=contentView.findViewById(flow_layout);
            contentView.setTag(holder);
        }else{
            holder = (ViewHolder) contentView.getTag();
        }
        try {
            JSONObject object=new JSONObject(String.valueOf(list.get(i)));
            holder.title.setText( object.getString("title"));
            holder.title.setBackgroundResource(object.getInt("color"));
            Double number= Double.valueOf(Integer.parseInt(object.getString("number")));
            if(number>0){
                holder.money.setText(number+"");
                holder.money.setTextColor(ContextCompat.getColor(context, R.color.green));
            }else{
                holder.money.setText(number+"");
                holder.money.setTextColor(ContextCompat.getColor(context, R.color.orange));
            }
            int color= Integer.parseInt(object.getString("color"));
            int width= Integer.parseInt(object.getString("width"));
            Double sum= Double.valueOf(Integer.parseInt(object.getString("sum")));
            final TextView text = new TextView(context);
            text.setBackgroundResource(color);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (width*number/sum), LayoutParams.MATCH_PARENT);//设置宽高,第一个参数是宽,第二个参数是高
            //设置边距
            params.topMargin = 0;
            params.bottomMargin = 0;
            params.leftMargin = 0;
            params.rightMargin = 0;
            text.setLayoutParams(params);
            holder.flow_layout.addView(text);//将内容添加到布局中
            BigDecimal   b   =   new BigDecimal((number/sum)*100);
            String str=number+"笔,"+(b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue())+"%";
            holder.explain.setText(str);

            Animation myAnimation = AnimationUtils.loadAnimation(context, R.anim.income_anim);
            holder.flow_layout.startAnimation(myAnimation);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentView;
    }
    private class ViewHolder{
        TextView  title,explain,money;
        FlowLayout flow_layout;
    }
}
