package com.example.administrator.ding_small.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.administrator.ding_small.R;

/**
 * Created by Administrator on 2016/9/29.
 */
//轮播图适配器
public class AdvertisementGvAdapter extends BaseAdapter {
    private Context context;
    private int size = 0;
    private ViewHolder holder;
    private int position = 0;

    public AdvertisementGvAdapter(int size, Context context) {
        this.size = size;
        holder = new ViewHolder();
        this.context = context;//获取上下文
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View contentView = null;
        if (contentView == null) {
            contentView = LayoutInflater.from(context).inflate(R.layout.fragment_item, viewGroup, false);
            holder.position_img = (ImageView) contentView.findViewById(R.id.position_img);

            contentView.setTag(holder);//设置事件触发源
        } else {
            holder = (ViewHolder) contentView.getTag();//获取事件触发源
        }
        if (i == position) {
            holder.position_img.setBackgroundResource(R.mipmap.position_press);//设置轮播图图标图片
        } else {
            holder.position_img.setBackgroundResource(R.mipmap.position);
        }
        return contentView;
    }

    private class ViewHolder {
        ImageView position_img;
    }

    public void setPosition(int position) {
        this.position = position;
        notifyDataSetChanged();//更新UI
    }
}
