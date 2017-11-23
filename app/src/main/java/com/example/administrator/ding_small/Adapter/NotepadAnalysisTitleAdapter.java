package com.example.administrator.ding_small.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.ding_small.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/10/31.
 */

public class NotepadAnalysisTitleAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder holder;
    private JSONArray list;
    public NotepadAnalysisTitleAdapter(Context context, JSONArray list) {
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
        if (contentView == null) {
            contentView = LayoutInflater.from(context).inflate(R.layout.notepad_analysis_title_list_item, viewGroup, false);
            holder.title = contentView.findViewById(R.id.title);
            holder.nofinish = contentView.findViewById(R.id.nofinish);
            holder.finished = contentView.findViewById(R.id.finished);
            holder.acount = contentView.findViewById(R.id.acount);

            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        try {
            JSONObject jsonObject = new JSONObject(String.valueOf(list.get(i)));
            holder.nofinish.setText(jsonObject.getString("nofinish"));
            holder.finished.setText(jsonObject.getString("finished"));
            holder.title.setText(jsonObject.getString("title"));
            int out= Integer.parseInt(jsonObject.getString("nofinish"));
            int on= Integer.parseInt(jsonObject.getString("finished"));
            int acount=out+on;
            holder.acount.setText(acount+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentView;
    }

    private class ViewHolder{
        TextView title,nofinish,finished,acount;
    }
}
