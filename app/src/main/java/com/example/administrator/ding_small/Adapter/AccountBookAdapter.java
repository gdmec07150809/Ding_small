package com.example.administrator.ding_small.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.ding_small.AccountBookActivity;
import com.example.administrator.ding_small.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/6.
 */

public class AccountBookAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<String> list;
    private ViewHolder holder;
    public AccountBookAdapter(Context context, ArrayList<String> arrayList) {
        this.context=context;
        holder=new ViewHolder();this.list=arrayList;
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
            contentView = LayoutInflater.from(context).inflate(R.layout.account_book_item, viewGroup, false);
            if(i%2!=0){
                Button repair=contentView.findViewById(R.id.repair);
                repair.setBackgroundResource(R.drawable.no_repair_bg);
            }
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        return contentView;
    }
    private class ViewHolder{

    }
}
