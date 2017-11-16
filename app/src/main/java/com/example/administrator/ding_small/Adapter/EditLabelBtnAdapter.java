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
 * Created by Administrator on7/10/28.
 */



public class EditLabelBtnAdapter extends BaseAdapter implements OnClickListener{
    private Context context;
    private ViewHolder holder;
    private JSONArray list;
    private Callback mCallback;

    public interface  Callback{
        public void click(View v);
    }
    public EditLabelBtnAdapter(Context context, JSONArray list,Callback callback) {
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
            contentView = LayoutInflater.from(context).inflate(R.layout.edit_label_btn_list_item,viewGroup,false);
            holder.label_text = contentView.findViewById(R.id.label_text);
            holder.label_img = contentView.findViewById(R.id.label_img);
            holder.up_img = contentView.findViewById(R.id.up_img);
            holder.edit_img = contentView.findViewById(R.id.edit_img);
            contentView.setTag(holder);
        }else{
            holder = (ViewHolder) contentView.getTag();
        }
        int index=0;
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(String.valueOf(list.get(i)));
            holder.label_text.setText(jsonObject.getString("name"));
            holder.edit_img.setImageResource(R.drawable.edit);
            holder.up_img.setImageResource(R.drawable.up);
            holder.up_img.setOnClickListener(this);
            holder.up_img.setTag(i);

           if(i>0){
               holder.label_img.setImageResource(R.drawable.title_yes);
           }
            holder.label_img.setOnClickListener(this);
            holder.label_img.setTag(i);

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
        TextView label_text;
        ImageView label_img,up_img,edit_img;
    }
}
