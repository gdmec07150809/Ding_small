package com.example.administrator.ding_small.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.administrator.ding_small.HelpTool.BitmapCache;
import com.example.administrator.ding_small.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by youyou000 on 2018/start1/29.
 */

public class RepairRecordAdapter extends BaseAdapter {
    private Context context;
    private ViewHolder holder;
    private JSONArray list=null;
    Bitmap bitmap=null;
    JSONArray picArray=null;
    int listIndex=0;

    private RequestQueue queue;
    private ImageLoader imageLoader;
    private ArrayList<Bitmap> arrayList = new ArrayList<Bitmap>();

    public RepairRecordAdapter(Context context, JSONArray list) {
        this.context = context;
        holder = new ViewHolder();
        this.list = list;
        queue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(queue, new BitmapCache());
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
            contentView = LayoutInflater.from(context).inflate(R.layout.new_repair_record,viewGroup,false);
            holder.repair_name=contentView.findViewById(R.id.repair_name);
            holder.repqir_record_time=contentView.findViewById(R.id.repqir_record_time);
            holder.repair_content=contentView.findViewById(R.id.repair_content);
            holder.pic_layout=contentView.findViewById(R.id.pic_layout);
            holder.img1=contentView.findViewById(R.id.img1);
            holder.img2=contentView.findViewById(R.id.img2);
            holder.img3=contentView.findViewById(R.id.img3);
            holder.img_layout=contentView.findViewById(R.id.img_layout);
            contentView.setTag(holder);
        }else{
            holder = (ViewHolder) contentView.getTag();
        }

        try {
            JSONObject jsonObject=new JSONObject(list.get(i)+"");
            picArray=new JSONArray(jsonObject.getString("picJson"));
            holder.img1.setDefaultImageResId(R.drawable.defult_no_img);
            holder.img2.setDefaultImageResId(R.drawable.defult_no_img);
            holder.img3.setDefaultImageResId(R.drawable.defult_no_img);
            //String[] picArray={"http://a.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=161e332af4246b607b5bba70dec8367a/8326cffc1e178a82111612d9f403738da977e8a5.jpg"};
           if(picArray.length()>0){
               for(int t=0;t<picArray.length();t++) {
                   if(t==0){
                       holder.img1.setDefaultImageResId(R.drawable.defult_no_img);
                       holder.img1.setErrorImageResId(R.drawable.defult_no_img);
                       holder.img1.setImageUrl((String) picArray.get(t), imageLoader);
                   }else if(t==1){
                       holder.img2.setDefaultImageResId(R.drawable.defult_no_img);
                       holder.img2.setErrorImageResId(R.drawable.defult_no_img);
                       holder.img2.setImageUrl((String) picArray.get(t), imageLoader);
                   }else{
                       holder.img3.setDefaultImageResId(R.drawable.defult_no_img);
                       holder.img3.setErrorImageResId(R.drawable.defult_no_img);
                       holder.img3.setImageUrl((String) picArray.get(t), imageLoader);
                   }
//                   System.out.println("图片路径："+picArray.get(t));
//                   if(picArray.get(t)!=null&&!picArray.get(t).equals("null")){
//                       returnBitMap(String.valueOf(picArray.get(t)),t);
//                   }
               }
           }else{
             //  holder.img_layout.setVisibility(View.GONE);
           }
            if(jsonObject.getString("equipmenUserName")!=null&&!jsonObject.getString("equipmenUserName").equals("null")){
                holder.repair_name.setText(jsonObject.getString("equipmenUserName"));
                holder.repqir_record_time.setText(jsonObject.getString("equipmenDate"));
                holder.repair_content.setText(jsonObject.getString("equipmenRes"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentView;
    }
    private class ViewHolder{
        TextView repair_name,repqir_record_time,repair_content;
        NetworkImageView img1,img2,img3;
        RelativeLayout pic_layout;
        LinearLayout img_layout;
    }

    public void returnBitMap(final String url,final int t){

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
                    conn.setConnectTimeout(10000);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    Message message = new Message();
                    message.what = t;
                    handler.sendMessage(message);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //arrayList.add(bitmap);
            if(bitmap!=null){
                switch (msg.what){
                    case 0:
                        holder.img1.setImageBitmap(bitmap);
                        break;
case 1:
        holder.img2.setImageBitmap(bitmap);
        break;
        case 2:
        holder.img3.setImageBitmap(bitmap);
        break;
default:
        break;
        }
        }
        System.out.println("bitmap:"+bitmap);
        //photo.setImageBitmap(bitmap);//更新UI
        }

        };
        }
