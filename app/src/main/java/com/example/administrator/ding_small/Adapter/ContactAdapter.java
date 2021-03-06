package com.example.administrator.ding_small.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.administrator.ding_small.ContactsActivity;
import com.example.administrator.ding_small.R;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by CZK on 2017/10/19.
 */

public class ContactAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder holder;
    private JSONObject list=null;
    public ContactAdapter(Context context, JSONObject list) {
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
        if (contentView == null ){
            contentView = LayoutInflater.from(context).inflate(R.layout.contacts_item,viewGroup,false);
            holder.name = (TextView) contentView.findViewById(R.id.name);
            holder.mobile = (TextView) contentView.findViewById(R.id.mobile);
            contentView.setTag(holder);
        }else{
            holder = (ViewHolder) contentView.getTag();
        }
        try {
            String ss=list.getString("contact"+i);
            JSONObject obj=new JSONObject(ss);
            holder.name.setText(obj.getString("lastname").substring(obj.getString("lastname").length()-1,obj.getString("lastname").length()).toString());
            holder.mobile.setText(obj.getString("mobile"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentView;
    }
    private class ViewHolder{
            TextView  name,mobile;
    }
}
