package com.example.administrator.ding_small.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.ding_small.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.R.id.list;
import static com.example.administrator.ding_small.R.id.flow_layout;

/**
 * Created by CZK on 2017/12/9.
 */

public class AccountOutTimeAdapter extends BaseAdapter {
    private Context context;
    private JSONArray list;
    private ViewHolder holder;
    private boolean isFlag;
    public AccountOutTimeAdapter(Context context, JSONArray list, boolean isFlag) {
        this.context = context;
        holder = new ViewHolder();
        this.list = list;
        this.isFlag=isFlag;
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
            if(isFlag){
                contentView = LayoutInflater.from(context).inflate(R.layout.account_return_by_month_small_item,viewGroup,false);
            }else{
                contentView = LayoutInflater.from(context).inflate(R.layout.account_return_by_month_item,viewGroup,false);
            }
            holder.title=contentView.findViewById(R.id.title);
            holder.time=contentView.findViewById(R.id.time);
            holder.dai=contentView.findViewById(R.id.dai);
            contentView.setTag(holder);
        }else{
            holder = (ViewHolder) contentView.getTag();
        }
        JSONObject object= null;
        try {
            object = new JSONObject(String.valueOf(list.get(i)));
            holder.title.setText(object.getString("title"));
            holder.title.setBackgroundResource(object.getInt("color"));
            Double money= Double.valueOf(object.getString("money"));
            if(money<0){
                holder.time.setText(money+"");
                holder.time.setTextColor(ContextCompat.getColor(context, R.color.orange));
            }else{
                holder.time.setText(money+"");
            }
            holder.time.setText(money+"");
            holder.dai.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return contentView;
    }
    private class ViewHolder{
        private TextView title,time,dai;
    }
}
