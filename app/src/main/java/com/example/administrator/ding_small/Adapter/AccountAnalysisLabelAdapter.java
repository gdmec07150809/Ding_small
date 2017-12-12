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

import static com.example.administrator.ding_small.R.id.label_text;

/**
 * Created by Administrator on 2017/12/11.
 */

public class AccountAnalysisLabelAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder holder;
    private JSONArray list;
    public AccountAnalysisLabelAdapter(Context context, JSONArray list) {
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
            contentView = LayoutInflater.from(context).inflate(R.layout.account_analysis_label_item, viewGroup, false);
            holder.label=contentView.findViewById(R.id.label);
            holder.number=contentView.findViewById(R.id.number);
            holder.outCome=contentView.findViewById(R.id.outCome);
            holder.inCome=contentView.findViewById(R.id.inCome);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        try {
            JSONObject jsonObject = new JSONObject(String.valueOf(list.get(i)));
            holder.label.setText(jsonObject.getString("label"));
            holder.number.setText(jsonObject.getString("number"));
            holder.outCome.setText(jsonObject.getString("outCome"));
            Double d_number= Double.valueOf(jsonObject.getString("inCome"));
            if(d_number>0){
                holder.inCome.setText(jsonObject.getString("inCome"));
                holder.inCome.setTextColor(ContextCompat.getColor(context, R.color.green));
            }else{
                holder.inCome.setText(jsonObject.getString("inCome"));
                holder.inCome.setTextColor(ContextCompat.getColor(context, R.color.orange));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentView;
    }
    private class ViewHolder{
        TextView label,number,outCome,inCome;
    }
}
