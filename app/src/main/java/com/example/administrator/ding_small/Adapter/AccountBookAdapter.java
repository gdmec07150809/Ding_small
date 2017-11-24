package com.example.administrator.ding_small.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
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
    private String saveTime;
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
            holder.time=contentView.findViewById(R.id.time);
            holder.time_layout=contentView.findViewById(R.id.time_layout);
            if(i%2!=0){
                Button repair=contentView.findViewById(R.id.repair);
                repair.setBackgroundResource(R.drawable.no_repair_bg);
            }
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        if(i>0){
            //System.out.println("保存值："+saveTime);
            if(saveTime.equals(holder.time.getText().toString())){
                holder.time_layout.setVisibility(View.GONE);
            }
        }
        saveTime=holder.time.getText().toString();
        return contentView;
    }
    private class ViewHolder{
        private TextView time;
        private RelativeLayout time_layout;
    }
}
