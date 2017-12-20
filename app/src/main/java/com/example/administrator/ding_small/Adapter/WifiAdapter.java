package com.example.administrator.ding_small.Adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ding_small.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/5.
 */
public class WifiAdapter extends BaseAdapter {

    private Context context;
    private ViewHolder holder;
    private List<ScanResult> scanResults = new ArrayList<>();

    public WifiAdapter(Context context, List<ScanResult> scanResults){
        this.context = context;
        holder = new ViewHolder();
        this.scanResults = scanResults;
    }

    @Override
    public int getCount() {
        return scanResults.size();
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
            contentView = LayoutInflater.from(context).inflate(R.layout.wifi_item,viewGroup,false);
            holder.number=contentView.findViewById(R.id.number);
            holder.mac=contentView.findViewById(R.id.mac);
            holder.ssid=contentView.findViewById(R.id.ssid);
            contentView.setTag(holder);
        }else{
            holder = (ViewHolder) contentView.getTag();
        }

        holder.number.setText(""+(i+1));
        holder.mac.setText(scanResults.get(i).BSSID);
        holder.ssid.setText(scanResults.get(i).SSID);
        return contentView;
    }

    private class ViewHolder{
        private TextView number,mac,ssid;
    }

}
