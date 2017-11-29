package com.example.administrator.ding_small.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.administrator.ding_small.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/2.
 */

public class ContactAccountAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> list=new ArrayList<String>();
    private ViewHolder holder;
    public ContactAccountAdapter(Context context, ArrayList<String> list) {
        this.context=context;
        this.list=list;
        holder = new ViewHolder();
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
            contentView = LayoutInflater.from(context).inflate(R.layout.account_item,viewGroup,false);
            contentView.setTag(holder);
        }else{
            holder = (ViewHolder) contentView.getTag();
        }
        return contentView;
    }
    private class ViewHolder{
    }
}
