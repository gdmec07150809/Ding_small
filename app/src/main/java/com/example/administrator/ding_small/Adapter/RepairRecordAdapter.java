package com.example.administrator.ding_small.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

/**
 * Created by youyou000 on 2018/start1/29.
 */

public class RepairRecordAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private ViewHolder holder;
    private JSONArray list=null;
    Bitmap bitmap=null;
    JSONArray picArray=null;
    int listIndex=0;
    private Dialog mCameraDialog;

    private RequestQueue queue;
    private ImageLoader imageLoader;
    private ArrayList<String> arrayList = new ArrayList<String>();

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
            JSONObject jsonObject= null;
            try {
                jsonObject = new JSONObject(list.get(i)+"");
                picArray=new JSONArray(jsonObject.getString("picJson"));
                if(picArray.length()==0||picArray==null){
                    contentView = LayoutInflater.from(context).inflate(R.layout.new_repair_recoed_no_img,viewGroup,false);
                    holder.repair_name=contentView.findViewById(R.id.repair_name);
                    holder.repqir_record_time=contentView.findViewById(R.id.repqir_record_time);
                    holder.repair_content=contentView.findViewById(R.id.repair_content);
                }else{
                    contentView = LayoutInflater.from(context).inflate(R.layout.new_repair_record,viewGroup,false);
                    holder.repair_name=contentView.findViewById(R.id.repair_name);
                    holder.repqir_record_time=contentView.findViewById(R.id.repqir_record_time);
                    holder.repair_content=contentView.findViewById(R.id.repair_content);
                    holder.pic_layout=contentView.findViewById(R.id.pic_layout);
                    holder.img1=contentView.findViewById(R.id.img1);
                    holder.img1.setOnClickListener(this);
                    holder.img2=contentView.findViewById(R.id.img2);
                    holder.img2.setOnClickListener(this);
                    holder.img3=contentView.findViewById(R.id.img3);
                    holder.img3.setOnClickListener(this);
                    holder.img_layout=contentView.findViewById(R.id.img_layout);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



            contentView.setTag(holder);
        }else{
            holder = (ViewHolder) contentView.getTag();
        }

        try {
            JSONObject jsonObject=new JSONObject(list.get(i)+"");
            picArray=new JSONArray(jsonObject.getString("picJson"));
            if(picArray!=null&&picArray.length()>0){

                if(picArray.length()>0){
                    for(int t=0;t<picArray.length();t++) {
                        if(t==0){
                            holder.img1.setDefaultImageResId(R.drawable.defult_no_img);
                            holder.img1.setErrorImageResId(R.drawable.defult_no_img);
                            holder.img1.setImageUrl((String) picArray.get(t), imageLoader);
                            holder.img1.setTag((String) picArray.get(t));
                        }else if(t==1){
                            holder.img2.setDefaultImageResId(R.drawable.defult_no_img);
                            holder.img2.setErrorImageResId(R.drawable.defult_no_img);
                            holder.img2.setImageUrl((String) picArray.get(t), imageLoader);
                            holder.img2.setTag((String) picArray.get(t));
                        }else{
                            holder.img3.setDefaultImageResId(R.drawable.defult_no_img);
                            holder.img3.setErrorImageResId(R.drawable.defult_no_img);
                            holder.img3.setImageUrl((String) picArray.get(t), imageLoader);
                            holder.img3.setTag((String) picArray.get(t));
                        }

                        if(!arrayList.contains((String)picArray.get(t))){
                            arrayList.add((String)picArray.get(t));
                        }
                    }
                }else{
                    //  holder.img_layout.setVisibility(View.GONE);
                }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img1:
               //Toast.makeText(context,""+v.getTag(),Toast.LENGTH_SHORT).show();
                for(int i=0;i<arrayList.size();i++){
                    if (v.getTag()!=null&&v.getTag().equals(arrayList.get(i))){
                        System.out.println(v.getTag());
                        returnBitMap(arrayList.get(i));
                    }
                }

                break;
            case R.id.img2:
                //Toast.makeText(context,""+v.getTag(),Toast.LENGTH_SHORT).show();
                for(int i=0;i<arrayList.size();i++){
                    if (v.getTag()!=null&&v.getTag().equals(arrayList.get(i))){
                        System.out.println(v.getTag());
                        returnBitMap(arrayList.get(i));
                    }
                }
                break;
            case R.id.img3:
               //Toast.makeText(context,""+v.getTag(),Toast.LENGTH_SHORT).show();
                for(int i=0;i<arrayList.size();i++){
                    if (v.getTag()!=null&&v.getTag().equals(arrayList.get(i))){
                        System.out.println(v.getTag());
                        returnBitMap(arrayList.get(i));
                    }
                }
                break;
        }
    }

    //昵称底部弹出菜单
    private void setNichNameDialog(Bitmap img) {
        RelativeLayout root = null;
        mCameraDialog = new Dialog(context, R.style.Dialog);
        root = (RelativeLayout) LayoutInflater.from(context).inflate(
                R.layout.image_dialog, null);
        holder.imgs=root.findViewById(R.id.big_img);
        Drawable drawable1 = new BitmapDrawable(img);
        holder.imgs.setBackground(drawable1);
        holder.imgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraDialog.dismiss();
            }
        });
        //初始化视图
        mCameraDialog.setContentView(root);
        mCameraDialog.getWindow().setDimAmount(1f);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setWindowAnimations(R.style.imgAnimation); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) context.getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 10f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

    private class ViewHolder{
        TextView repair_name,repqir_record_time,repair_content;
        NetworkImageView img1,img2,img3;
        ImageView imgs;
        RelativeLayout pic_layout;
        LinearLayout img_layout;
    }


    public void returnBitMap(final String url){

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

                    Message message=new Message();
                    message.what=0;
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
                        setNichNameDialog(bitmap);
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
