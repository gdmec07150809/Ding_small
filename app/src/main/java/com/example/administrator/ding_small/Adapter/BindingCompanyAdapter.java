package com.example.administrator.ding_small.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.example.administrator.ding_small.PersonalCenter.BindingCompanyActivity;
import com.example.administrator.ding_small.R;

import java.util.ArrayList;

/**
 * Created by CZK on 2017/11/7.
 */

public class BindingCompanyAdapter extends BaseAdapter implements OnClickListener {
    private Context context;
    private ArrayList<String> list;
    private ViewHolder holder;
    private Callback mCallback;
    public interface  Callback{
        public void click(View v);
    }
    public BindingCompanyAdapter(Context context, ArrayList<String> list,Callback callback) {
        this.list=list;
        this.context=context;
        holder=new ViewHolder();
        mCallback=callback;
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
            contentView = LayoutInflater.from(context).inflate(R.layout.company_item,viewGroup,false);
            holder.company_name=contentView.findViewById(R.id.company_name);
            holder.switchbtn=contentView.findViewById(R.id.switchbtn);
            contentView.setTag(holder);
        }else{
            holder = (ViewHolder) contentView.getTag();
        }
        holder.company_name.setText(list.get(i));
        holder.switchbtn.setOnClickListener(this);
        holder.switchbtn.setTag(i);
        return contentView;
    }
    @Override
    public void onClick(View v) {
        mCallback.click(v);
    }
    private class ViewHolder{
        private TextView company_name;
        private Switch switchbtn;
    }
}
