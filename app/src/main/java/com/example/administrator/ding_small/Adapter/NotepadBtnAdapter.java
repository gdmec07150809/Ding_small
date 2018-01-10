package com.example.administrator.ding_small.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.ding_small.NotepadBtnActivity;
import com.example.administrator.ding_small.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.R.id.list;

/**
 * Created by CZK on 2017/11/3.
 */

public class NotepadBtnAdapter extends BaseAdapter {
    private Context context;
    private JSONArray list;
    private ViewHolder holder;
    private String saveTime="";
    //private int[] titleColor= new int[]{R.color.green, R.color.fen,R.color.blank,R.color.green, R.color.fen,R.color.orange};
    public NotepadBtnAdapter(Context context, JSONArray list) {
        this.context=context;
        this.list=list;
        holder = new ViewHolder();
    }

    @Override
    public int getCount() {
        return list.length() ;
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
            contentView = LayoutInflater.from(context).inflate(R.layout.notepad_item,viewGroup,false);
            holder.date=contentView.findViewById(R.id.date);
            holder.explain=contentView.findViewById(R.id.explain);
            holder.name=contentView.findViewById(R.id.name);
            holder.label=contentView.findViewById(R.id.label);
            holder.time=contentView.findViewById(R.id.time);
            holder.head_date=contentView.findViewById(R.id.head_date);
            holder.title=contentView.findViewById(R.id.title);
            contentView.setTag(holder);
        }else{
            holder = (ViewHolder) contentView.getTag();
        }
        try {
            JSONObject jsonObject = new JSONObject(String.valueOf(list.get(i)));

            if(i==0){
                holder.date.setText(jsonObject.getString("date"));
                saveTime=jsonObject.getString("date").toString();
            }else{
              if(saveTime.equals(jsonObject.getString("date").toString())){
                  holder.head_date.setVisibility(View.GONE);
                 // saveTime=jsonObject.getString("date").toString();
              }else{
                  holder.date.setText(jsonObject.getString("date"));
                  saveTime=jsonObject.getString("date").toString();
                  holder.head_date.setVisibility(View.VISIBLE);
              }
            }

            holder.explain.setText(jsonObject.getString("explain"));
            holder.name.setText(jsonObject.getString("name"));
            holder.label.setText(jsonObject.getString("label"));
            holder.time.setText(jsonObject.getString("time"));
            holder.title.setText(jsonObject.getString("title"));
            holder.title.setBackgroundResource(jsonObject.getInt("titleColor"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentView;
    }
    private class ViewHolder{
        TextView date,explain,name,label,time;
        Button title;
        LinearLayout head_date;
    }
}
