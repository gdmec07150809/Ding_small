package com.example.administrator.ding_small.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ding_small.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/28.
 */



public class EditTitleAdapter extends BaseAdapter implements OnClickListener{
    private Context context;
    private ViewHolder holder;
    private JSONArray list;
    private Callback mCallback;

    public interface  Callback{
        public void click(View v);
    }
    public EditTitleAdapter(Context context, JSONArray list,Callback callback) {
        this.context = context;
        holder = new ViewHolder();
        this.list = list;
        mCallback=callback;
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
            contentView = LayoutInflater.from(context).inflate(R.layout.edit_title_list_item,viewGroup,false);
            holder.color = contentView.findViewById(R.id.color);
            holder.title_text = contentView.findViewById(R.id.title_text);
            holder.title_img = contentView.findViewById(R.id.title_img);
            holder.up_img = contentView.findViewById(R.id.up_img);
            holder.edit_img = contentView.findViewById(R.id.edit_img);
            contentView.setTag(holder);
        }else{
            holder = (ViewHolder) contentView.getTag();
        }
        try {
            int index=0;
            JSONObject jsonObject=new JSONObject(String.valueOf(list.get(i)));
            holder.title_text.setText(jsonObject.getString("name"));
            holder.edit_img.setImageResource(R.drawable.edit);
            holder.up_img.setImageResource(R.drawable.up);
            if(jsonObject.getString("name").equals("住房")){
                index=2;
                holder.color.setBackgroundResource(R.drawable.c2_bg);
                holder.title_img.setImageResource(R.drawable.title_c);
            }else if(jsonObject.getString("name").equals("学费")){
                index=3;
                holder.color.setBackgroundResource(R.drawable.c3_bg);
                holder.title_img.setImageResource(R.drawable.title_yes);
            }else if(jsonObject.getString("name").equals("工资")){
                index=4;
                holder.color.setBackgroundResource(R.drawable.c4_bg);
            }else if(jsonObject.getString("name").equals("房租")){
                index=5;
                holder.color.setBackgroundResource(R.drawable.c5_bg);
                holder.title_img.setImageResource(R.drawable.title_yes);
            }else if(jsonObject.getString("name").equals("逛街")){
                index=6;
                holder.color.setBackgroundResource(R.drawable.c6_bg);
                holder.title_img.setImageResource(R.drawable.title_yes);
            }else if(jsonObject.getString("name").equals("零食")){
                index=1;
                holder.color.setBackgroundResource(R.drawable.c1_bg);
                holder.title_img.setImageResource(R.drawable.title_yes);
            }else if(jsonObject.getString("name").equals("买菜")){
                index=7;
                holder.color.setBackgroundResource(R.drawable.c7_bg);
                holder.title_img.setImageResource(R.drawable.title_yes);
            }else if(jsonObject.getString("name").equals("奖金")){
                index=8;
                holder.color.setBackgroundResource(R.drawable.c8_bg);
                holder.title_img.setImageResource(R.drawable.title_yes);
            }else if(jsonObject.getString("name").equals("零食")){
                index=9;
                holder.color.setBackgroundResource(R.drawable.c9_bg);
                holder.title_img.setImageResource(R.drawable.title_yes);
            }else if(jsonObject.getString("name").equals("夜宵")){
                index=10;
                holder.color.setBackgroundResource(R.drawable.c10_bg);
                holder.title_img.setImageResource(R.drawable.title_yes);
            }
            holder.up_img.setOnClickListener(this);
            holder.up_img.setTag(i);
            holder.edit_img.setOnClickListener(this);
            holder.edit_img.setTag(R.id.tag_first,jsonObject.getString("name"));
            holder.edit_img.setTag(R.id.tag_second,index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentView;
    }

    @Override
    public void onClick(View v) {
            mCallback.click(v);
    }


    private class ViewHolder{
        TextView color,title_text;
        ImageView title_img,up_img,edit_img;
    }
}
