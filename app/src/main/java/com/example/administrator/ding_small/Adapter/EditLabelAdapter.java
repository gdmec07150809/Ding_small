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

/**
 * Created by CZK on 2017/10/31.
 */

public class EditLabelAdapter extends BaseAdapter implements OnClickListener {
    private Context context;
    private ViewHolder holder;
    private JSONArray list;
    private Callback mCallback;

    public interface  Callback{
        public void click(View v);
    }
    public EditLabelAdapter(Context context, JSONArray list,Callback callback) {
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
        if (contentView == null) {
            contentView = LayoutInflater.from(context).inflate(R.layout.edit_label_list_item, viewGroup, false);
            holder.label_text = contentView.findViewById(R.id.label_text);
            holder.label_img = contentView.findViewById(R.id.label_img);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        try {
            JSONObject jsonObject = new JSONObject(String.valueOf(list.get(i)));
            holder.label_text.setText(jsonObject.getString("name"));
            if(i>0){
                holder.label_img.setImageResource(R.drawable.title_yes);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.label_img.setOnClickListener(this);
        holder.label_img.setTag(i);
        return contentView;
    }

    @Override
    public void onClick(View v) {
        mCallback.click(v);
    }


    private class ViewHolder{
        TextView label_text;
        ImageView label_img;
    }
}
