package com.example.administrator.ding_small.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.administrator.ding_small.R;

import java.util.ArrayList;

/**
 * Created by CZK on 2017/11/6.
 */

public class AccountsSearchByCalendarAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder holder;
    private ArrayList<String> list = null;

    public AccountsSearchByCalendarAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        holder = new ViewHolder();
        this.list = arrayList;
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
        if (contentView == null) {
            if(i==0){
                contentView = LayoutInflater.from(context).inflate(R.layout.accounts_calendar_search_item, viewGroup, false);
            }else {
                contentView = LayoutInflater.from(context).inflate(R.layout.accounts_calendar_search_small_item, viewGroup, false);
            }

            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        return contentView;

    }

    private class ViewHolder {

    }
}