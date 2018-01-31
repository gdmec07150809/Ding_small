package com.example.administrator.ding_small;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Observable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.ding_small.HelpTool.MD5Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.administrator.ding_small.LoginandRegiter.LoginAcitivity.SHOW_RESPONSE;
import static org.xutils.common.util.IOUtil.copy;

/**
 * Created by youyou000 on 2018/1/27.
 */

public class TestActivity extends Activity {
    private TextView test_text;

    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    String memid, token, UserSign, oldPass, newPass, ts, nameStr,sign;
    public static final int SHOW_RESPONSE = 0;
    private ImageView photo,photo1,photo2;
    Bitmap bitmap;
    private ArrayList<Bitmap> arrayList = new ArrayList<Bitmap>();
    private ArrayList<Bitmap> newarrayList=new ArrayList<>();

    List<File> fileList=new ArrayList<File>();
    int index=0;
    ExecutorService exe = Executors.newFixedThreadPool(50);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        test_text=findViewById(R.id.test_text);
        photo=findViewById(R.id.photo);
        photo1=findViewById(R.id.photo1);
        photo2=findViewById(R.id.photo2);
        //getCache();
        sendMultipart();
       new Thread(getPhoto).start();//加载网络图片,并设置

       // getBitmap.getBitmapPhoto("https://avatar.csdn.net/C/3/5/1_hmyang314.jpg");
    }
    private void getCache() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        memid = sp.getString("memId", "null");
        token = sp.getString("tokEn", "null");

        String url = "http://192.168.1.105:8080/app/ppt7000/statisticsSummarySecr9000.do";
        ts = String.valueOf(new Date().getTime());
        System.out.println("首页：memId" + memid + "  ts:" + ts + "  token:" + token);
        String Sign = url + memid + token + ts;
        System.out.println("Sign:" + Sign);
        sign = MD5Utils.md5(Sign);
        System.out.println("加密sign:" + sign);
        new Thread(networkTask).start();//获取轮播图
    }
    /**
     * 网络操作相关的子线程okhttp框架  获取图片
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作

            //System.out.println("图片："+getBitmap.getBitmapPhoto("https://avatar.csdn.net/C/3/5/1_hmyang314.jpg"));
            // String url = "http://192.168.1.108:8080/app/invs6002/lisSecr6002.do?memId=" + memid + "&ts=" + ts ;轮播图
            String url = "http://192.168.1.105:8080/app/secr9000/statisticsSummarySecr9000.do?memId=" + memid + "&ts=" + ts ;
            OkHttpClient okHttpClient = new OkHttpClient();

            String b = "{\"dateFm\":\"2017-12-01\",\"dateTo\":\"2018-03-01\",\"memId\":\""+memid+"\"}";//json字符串
            System.out.println("验证：" + sign +" "+b);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), b);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("sIgn", sign)
                    .build();

            System.out.println(request.headers());
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                String result = response.body().string();
                if (response != null) {
                    //在子线程中将Message对象发出去
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    message.obj = result.toString();
                    // getDeviceListHandler.sendMessage(message);
                }
              //  test_text.setText("结果：" + result + "状态码：" + response.code());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    Runnable getPhoto=new Runnable() {
        @Override
        public void run() {
            String[] url={"https://avatar.csdn.net/C/3/5/1_hmyang314.jpg","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1517057253649&di=322b0a2d78c713c71f53a434f4ebbdd4&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dpixel_huitu%252C0%252C0%252C294%252C40%2Fsign%3Decfe83b9042442a7ba03f5e5b83bc827%2F728da9773912b31bc2fe74138d18367adab4e17e.jpg","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1517057253648&di=59d9a1e4d8da7441d593f4a4f6b86ce4&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fe1fe9925bc315c600dce09d386b1cb13495477b6.jpg"};
            for(int i=0;i<url.length;i++){
                    returnBitMap(url[i]);//获取网络图片，并转化为Bitmap格式
            }
            //sendMultipart();
        }
    };

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
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    Message message = new Message();
                    message.what = index;
                    handler.sendMessage(message);
                    index++;
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
            arrayList.add(bitmap);
            switch (msg.what){
                case 0:
                    photo.setImageBitmap(bitmap);
                    break;
                case 1:
                    photo1.setImageBitmap(bitmap);
                    break;
                case 2:
                    photo2.setImageBitmap(bitmap);
                    break;
                default:
                    break;
            }
            System.out.println("bitmap:"+bitmap);
           //photo.setImageBitmap(bitmap);//更新UI
        }

    };

    /**
     * 上传文件及参数
     */
    private void sendMultipart(){
        File sdcache = getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        //设置超时时间及缓存
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));


        OkHttpClient mOkHttpClient=builder.build();

        MultipartBody.Builder mbody=new MultipartBody.Builder().setType(MultipartBody.FORM);

        List<File> fileList=new ArrayList<File>();
        File img1=new File("https://avatar.csdn.net/C/3/5/1_hmyang314.jpg");
        fileList.add(img1);
        int i=0;
        for(File file:fileList){
            if(file.exists()){
                Log.i("imageName:",file.getName());//经过测试，此处的名称不能相同，如果相同，只能保存最后一个图片，不知道那些同名的大神是怎么成功保存图片的。
                mbody.addFormDataPart("image"+i,file.getName(),RequestBody.create(MediaType.parse("image/jpeg"),file));
                i++;
            }
        }
        String url="http://192.168.1.110:8080/app/ppt7000/memberImgUpload.do?memId="+memid+"&ts="+ts;
        RequestBody requestBody =mbody.build();
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + "...")
                .url(url)
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("InfoMSG", response.body().string());
            }
        });
    }
}
