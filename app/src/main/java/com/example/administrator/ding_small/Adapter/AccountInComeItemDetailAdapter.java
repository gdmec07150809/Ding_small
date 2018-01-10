package com.example.administrator.ding_small.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.ding_small.R;

import org.json.JSONArray;

import java.util.ArrayList;

import static com.example.administrator.ding_small.R.id.flow_layout;

/**
 * Created by CZK on 2017/12/9.
 */

public class AccountInComeItemDetailAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> list;
    private ViewHolder holder;
    private String title_str;
    private int color;
    private boolean isFlag;
    public AccountInComeItemDetailAdapter(Context context, ArrayList<String> list, String title, int color, boolean isFlag) {
        this.context = context;
        holder = new ViewHolder();
        this.list = list;
        this.title_str=title;
        this.color=color;
        this.isFlag=isFlag;
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
            if(isFlag){
                contentView = LayoutInflater.from(context).inflate(R.layout.account_return_by_month_small_item,viewGroup,false);
            }else{
                contentView = LayoutInflater.from(context).inflate(R.layout.account_return_by_month_item,viewGroup,false);
            }
            holder.title=contentView.findViewById(R.id.title);
            contentView.setTag(holder);
        }else{
            holder = (ViewHolder) contentView.getTag();
        }
        holder.title.setText(title_str);
        holder.title.setBackgroundResource(color);
        return contentView;
    }
    private class ViewHolder{
        private TextView title;
    }
}
