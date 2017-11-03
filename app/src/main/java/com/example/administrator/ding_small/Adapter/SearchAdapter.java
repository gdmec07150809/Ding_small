package com.example.administrator.ding_small.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.ding_small.R;
import com.example.administrator.ding_small.SearchActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/3.
 */

public class SearchAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder holder;
    private ArrayList<String> list=null;
    private boolean isTitle=false;
    public SearchAdapter(Context context, ArrayList<String> list,boolean is) {
        this.context = context;
        holder = new ViewHolder();
        this.list = list;
        this.isTitle=is;
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
            if(isTitle==false){
                contentView = LayoutInflater.from(context).inflate(R.layout.notepad_item,viewGroup,false);
            }else {
                contentView = LayoutInflater.from(context).inflate(R.layout.notepad_small_item, viewGroup, false);
            }
            contentView.setTag(holder);
        }else{
            holder = (ViewHolder) contentView.getTag();
        }
        return contentView;
    }
    private class ViewHolder{

    }
}
