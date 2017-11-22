package com.example.administrator.ding_small.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ding_small.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/10/31.
 */

public class NotepadReportAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder holder;
    private JSONArray list;
    public NotepadReportAdapter(Context context, JSONArray list) {
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
            contentView = LayoutInflater.from(context).inflate(R.layout.notepad_report_listview_item, viewGroup, false);
            holder.year = contentView.findViewById(R.id.year);
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
            holder.OnTime.setText(jsonObject.getString("OnTime"));
            holder.year.setText(jsonObject.getString("year"));
            int out= Integer.parseInt(jsonObject.getString("OutTime"));
            int on= Integer.parseInt(jsonObject.getString("OnTime"));
            int acount=out+on;
            holder.acount.setText(acount+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentView;
    }

    private class ViewHolder{
        TextView year,OutTime,OnTime,acount;
    }
}
