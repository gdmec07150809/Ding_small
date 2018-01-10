package com.example.administrator.ding_small.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ding_small.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by CZK on 2017/10/27.
 */

public class TitleAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder holder;
    private ArrayList<String> list;
    public TitleAdapter(Context context, ArrayList list) {
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
            contentView = LayoutInflater.from(context).inflate(R.layout.title_list_item,viewGroup,false);
            holder.color =contentView.findViewById(R.id.color);
            holder.title_text = contentView.findViewById(R.id.title_text);
            holder.title_img = contentView.findViewById(R.id.title_img);
            contentView.setTag(holder);
        }else{
            holder = (ViewHolder) contentView.getTag();
        }
        holder.title_text.setText(list.get(i));
        if(list.get(i).equals("住房")){
            holder.color.setBackgroundResource(R.drawable.c2_bg);
            holder.title_img.setImageResource(R.drawable.title_c);
        }else if(list.get(i).equals("学费")){
            holder.color.setBackgroundResource(R.drawable.c3_bg);
            holder.title_img.setImageResource(R.drawable.title_yes);
        }else if(list.get(i).equals("工资")){
            holder.color.setBackgroundResource(R.drawable.c4_bg);
        }else if(list.get(i).equals("房租")){
            holder.color.setBackgroundResource(R.drawable.c5_bg);
            holder.title_img.setImageResource(R.drawable.title_yes);
        }else if(list.get(i).equals("逛街")){
            holder.color.setBackgroundResource(R.drawable.c6_bg);
            holder.title_img.setImageResource(R.drawable.title_yes);
        }else if(list.get(i).equals("零食")){
            holder.color.setBackgroundResource(R.drawable.c1_bg);
            holder.title_img.setImageResource(R.drawable.title_yes);
        }else if(list.get(i).equals("买菜")){
            holder.color.setBackgroundResource(R.drawable.c7_bg);
            holder.title_img.setImageResource(R.drawable.title_yes);
        }else if(list.get(i).equals("奖金")){
            holder.color.setBackgroundResource(R.drawable.c8_bg);
            holder.title_img.setImageResource(R.drawable.title_yes);
        }else if(list.get(i).equals("零食")){
            holder.color.setBackgroundResource(R.drawable.c9_bg);
            holder.title_img.setImageResource(R.drawable.title_yes);
        }else if(list.get(i).equals("夜宵")){
            holder.color.setBackgroundResource(R.drawable.c10_bg);
            holder.title_img.setImageResource(R.drawable.title_yes);
        }
        return contentView;
    }
    private class ViewHolder{
        TextView color,title_text;
        ImageView title_img;
    }
}
