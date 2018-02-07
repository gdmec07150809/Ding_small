package com.example.administrator.ding_small.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.ding_small.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by CZK on 2018/1/5.
 */

public class ManufacturerAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder holder;
    private ArrayList<String> list=null;
    public ManufacturerAdapter(Context context, ArrayList<String> list) {
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
            contentView = LayoutInflater.from(context).inflate(R.layout.new_detail_manufacturer,viewGroup,false);
           // holder.manufacturer_name=contentView.findViewById(R.id.manufacturer_name);
            holder.company_name=contentView.findViewById(R.id.company_name);
            holder.selling_phone=contentView.findViewById(R.id.selling_phone);
            holder.repair_phone=contentView.findViewById(R.id.repair_phone);
            //after_phone_text,repair_phone_text
            holder.after_phone_text=contentView.findViewById(R.id.after_phone_text);
            holder.repair_phone_text=contentView.findViewById(R.id.repair_phone_text);
            holder.manufacturer_location=contentView.findViewById(R.id.manufacturer_location);
            contentView.setTag(holder);
        }else{
            holder = (ViewHolder) contentView.getTag();
        }

        if (Locale.getDefault().getLanguage().equals("en")) {
            holder.after_phone_text.setText("  (After sale telephone)");
            holder.repair_phone_text.setText("  (Repair telephone)");
        }
        try {
            JSONObject jsonObject=new JSONObject(list.get(i));
         //  holder.manufacturer_name.setText(jsonObject.getString("deptName"));
            holder.company_name.setText(jsonObject.getString("deptName"));
            holder.selling_phone.setText(jsonObject.getString("wnerPhone"));
            holder.repair_phone.setText(jsonObject.getString("repairPhone"));
            JSONObject jsonObject1=new JSONObject(jsonObject.getString("wnerAddress"));
            holder.manufacturer_location.setText(jsonObject1.getString("fa"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentView;
    }
    private class ViewHolder{
        TextView manufacturer_name,company_name,selling_phone,repair_phone,manufacturer_location,after_phone_text,repair_phone_text;
    }
}
