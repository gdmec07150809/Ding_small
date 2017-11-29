package com.example.administrator.ding_small.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ding_small.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.LogManager;

/**
 * Created by Administrator on 2017/11/28.
 */

    public class NotepadRemarkDetailsAdapter extends BaseAdapter {
        private Context context;
        private ViewHolder holder;
        private JSONArray list;
        public NotepadRemarkDetailsAdapter(Context context, JSONArray list) {
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
                contentView = LayoutInflater.from(context).inflate(R.layout.notepad_record_item, viewGroup, false);
                holder.date = contentView.findViewById(R.id.date);
                holder.record = contentView.findViewById(R.id.record);
                holder.money = contentView.findViewById(R.id.money);
                holder.img=contentView.findViewById(R.id.img);
                contentView.setTag(holder);
            } else {
                holder = (ViewHolder) contentView.getTag();
            }
            try {
                JSONObject jsonObject = new JSONObject(String.valueOf(list.get(i)));
                holder.date.setText(jsonObject.getString("date"));
                holder.record.setText(jsonObject.getString("explain"));
                holder.money.setText(jsonObject.getString("money"));
                if(i==0){
                    holder.img.setVisibility(View.GONE);
                }else if(i/2!=0){
                    holder.img.setImageResource(R.drawable.weixin);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return contentView;
        }

        private class ViewHolder{
            TextView date,record,money;
            ImageView img;
        }
    }


