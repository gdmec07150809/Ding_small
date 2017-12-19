package com.example.administrator.ding_small.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ding_small.ContactsActivity;
import com.example.administrator.ding_small.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Administrator on 2017/10/19.
 */

public class DeviceListAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder holder;
    private JSONArray list=null;
    public DeviceListAdapter(Context context, JSONArray list) {
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
            contentView = LayoutInflater.from(context).inflate(R.layout.device_list_item,viewGroup,false);
            holder.device_name = contentView.findViewById(R.id.device_name);
            holder.location =contentView.findViewById(R.id.device_location);
            holder.ssid = contentView.findViewById(R.id.ssid);
            holder.stauts = contentView.findViewById(R.id.stauts);
            holder.device_type=contentView.findViewById(R.id.device_type);
            contentView.setTag(holder);
        }else{
            holder = (ViewHolder) contentView.getTag();
        }
        try {
            JSONObject obj=new JSONObject(String.valueOf(list.get(i)));
            holder.device_name.setText( obj.getString("device_name"));
            holder.location.setText( obj.getString("device_location"));
            holder.ssid.setText( obj.getString("device_ssid"));
            String stauts=obj.getString("device_state");
            String type=obj.getString("device_type");
            if(stauts.equals("0")){
                holder.stauts.setImageResource(R.drawable.weixiu_ing_img);
            }else{
                holder.stauts.setImageResource(R.drawable.weixiu_ed_img);
            }
            switch (type){
                case "bluetooth":
                    holder.device_type.setImageResource(R.drawable.bluetooth_img);
                    break;
                case "wifi":
                    holder.device_type.setImageResource(R.drawable.wifi_img);
                    break;
                case "scan":
                    holder.device_type.setImageResource(R.drawable.home_scan);
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentView;
    }
    private class ViewHolder{
        TextView  device_name,location,ssid;
        ImageView stauts,device_type;
    }
}
