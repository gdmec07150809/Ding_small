package com.example.administrator.ding_small.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private String date=null;
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
            contentView = LayoutInflater.from(context).inflate(R.layout.select_new_device_item,viewGroup,false);
            holder.device_name = contentView.findViewById(R.id.device_name);
            holder.location =contentView.findViewById(R.id.location);
            holder.ssid = contentView.findViewById(R.id.uuid);
            holder.stauts = contentView.findViewById(R.id.stauts);
            holder.device_date=contentView.findViewById(R.id.device_date);
            holder.date_layout=contentView.findViewById(R.id.date_layout);
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
            String type=obj.getString("device_date");
            if(stauts.equals("0")){
                holder.stauts.setImageResource(R.mipmap.icon_maintain_connected);
            }else{
                holder.stauts.setImageResource(R.mipmap.icon_maintain_unconnect);
            }
            holder.device_date.setText(type);
            if(i==0){
                    date=type;
            }else{
                    if(date.equals(type)){
                        holder.date_layout.setVisibility(View.GONE);
                        date=type;
                    }else{
                        date=type;
                    }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentView;
    }
    private class ViewHolder{
        TextView  device_name,location,ssid,device_date;
        ImageView stauts;
        LinearLayout date_layout;
    }
}
