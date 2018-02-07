package com.example.administrator.ding_small.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.ding_small.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CZK on 2018/start1/5.
 */

public class SearchBoxAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder holder;
    private ArrayList<String> list=null;
    public SearchBoxAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        holder = new ViewHolder();
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
            contentView = LayoutInflater.from(context).inflate(R.layout.search_record_item,viewGroup,false);
            holder.record_str=contentView.findViewById(R.id.record_str);
            contentView.setTag(holder);
        }else{
            holder = (ViewHolder) contentView.getTag();
        }
        holder.record_str.setText(list.get(i));
        return contentView;
    }
    private class ViewHolder{
        TextView record_str;
    }
}
