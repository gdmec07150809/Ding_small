package com.example.administrator.ding_small.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.ding_small.ContactsActivity;
import com.example.administrator.ding_small.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;


/**
 * Created by CZK on 2017/10/19.
 */

public class DeviceListAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder holder;
    private JSONArray list=null;
    private String date;
    Bitmap bitmap=null;
    public DeviceListAdapter(Context context, JSONArray list) {
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
        if (contentView == null ){
            contentView = LayoutInflater.from(context).inflate(R.layout.select_new_device_item,viewGroup,false);
            holder.device_name = contentView.findViewById(R.id.device_name);
            holder.location =contentView.findViewById(R.id.location);
            holder.ssid = contentView.findViewById(R.id.uuid);
            holder.stauts = contentView.findViewById(R.id.stauts);
            holder.stauts_img = contentView.findViewById(R.id.stauts_img);
            holder.device_date=contentView.findViewById(R.id.device_date);
            holder.date_layout=contentView.findViewById(R.id.date_layout);
            holder.device_img=contentView.findViewById(R.id.device_head_img);
            contentView.setTag(holder);
        }else{
            holder = (ViewHolder) contentView.getTag();
        }
        try {
            JSONObject obj=new JSONObject(String.valueOf(list.get(i)));
            System.out.println("adapter:"+String.valueOf(list.get(i)));
            holder.device_name.setText( obj.getString("eqpName"));

            if(obj.getString("imsPci")!=null){
                System.out.println("路劲:"+obj.getString("imsPci"));
                //String imgUrl="https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1517382173&di=26a2bf5e76ab8b80075729896093b7ac&src=http://image.tianjimedia.com/uploadImages/2015/215/41/M68709LC8O6L.jpg";
                returnBitMap(obj.getString("imsPci"));//获取网络图片，并转化为Bitmap格式  设备图片
            }
            if(bitmap!=null){
                holder.device_img.setImageBitmap(bitmap);
            }
            if(obj.getString("eqpAddressJson")==null||obj.getString("eqpAddressJson").equals("null")){
                holder.location.setText("");
            }else{
                JSONObject jsonObject=new JSONObject(obj.getString("eqpAddressJson"));
                holder.location.setText(jsonObject.getString("fa"));
            }
            holder.ssid.setText( obj.getString("macNo"));
            String stauts=obj.getString("eqpStatus");
            String type=obj.getString("insertDate");
           // long date1= Long.parseLong(type);

            if(stauts.equals("1")){
                holder.stauts_img.setImageResource(R.mipmap.icon_list_fixing);
            }else{
                holder.stauts_img.setVisibility(View.GONE);
            }
            //holder.device_date.setText( getFormatedDateTime("yyyy-MM-dd HH:mm:ss",date1));
            holder.device_date.setText(type);
            if(i==0){
                    date=type;
            }else{
                    if(date.equals(type)){
                        holder.date_layout.setVisibility(View.GONE);
                        date=type;
                    }else{
                        date=type;
                    }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentView;
    }
    private class ViewHolder{
        TextView  device_name,location,ssid,device_date;
        ImageView stauts,stauts_img,device_img;
        LinearLayout date_layout;
    }
    public static String getFormatedDateTime(String pattern, long dateTime) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new Date(dateTime + 0));
    }

    public Bitmap returnBitMap(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;
                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection)imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.setUseCaches(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        return bitmap;
    }
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
           switch (msg.what){
               case 0:
                   System.out.println("图片bitmap:"+bitmap);
                   holder.device_img.setImageBitmap(bitmap);
                   break;
               default:break;
           }
        }
    };

}
