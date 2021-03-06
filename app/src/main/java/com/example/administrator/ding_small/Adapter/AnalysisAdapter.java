package com.example.administrator.ding_small.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.ding_small.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.administrator.ding_small.R.id.ge;

/**
 * Created by CZK on 2017/10/31.
 */

public class AnalysisAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder holder;
    private JSONArray list;
    public AnalysisAdapter(Context context, JSONArray list) {
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
        if (contentView == null) {
            contentView = LayoutInflater.from(context).inflate(R.layout.notpad_analysis_list_item, viewGroup, false);
            holder.yue = contentView.findViewById(R.id.yue);
            holder.OutTime = contentView.findViewById(R.id.OutTime);
            holder.OnTime = contentView.findViewById(R.id.OnTime);
            holder.acount = contentView.findViewById(R.id.acount);

            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        try {
            JSONObject jsonObject = new JSONObject(String.valueOf(list.get(i)));
            holder.OutTime.setText(jsonObject.getString("OutTime"));
            holder.OutTime.setTextColor(ContextCompat.getColor(context, R.color.blank));
            holder.OnTime.setText(jsonObject.getString("OnTime"));
            holder.OnTime.setTextColor(ContextCompat.getColor(context, R.color.blank));
            holder.yue.setText(i+1+"月");
            Double out= Double.valueOf(Integer.parseInt(jsonObject.getString("OutTime")));
            Double on= Double.valueOf(Integer.parseInt(jsonObject.getString("OnTime")));
            Double acount=out+on;
            if(acount>0){
                holder.acount.setText(acount+"");
                holder.acount.setTextColor(ContextCompat.getColor(context, R.color.green));
            }else{
                holder.acount.setText(acount+"");
                holder.acount.setTextColor(ContextCompat.getColor(context, R.color.blank));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentView;
    }

    private class ViewHolder{
        TextView yue,OutTime,OnTime,acount;
    }
}
