package com.example.administrator.ding_small.HelpTool;

import android.content.Context;
import android.os.Message;
import android.widget.Toast;

import com.example.administrator.ding_small.MainActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import static com.example.administrator.ding_small.MainActivity.SHOW_RESPONSE;

/**
 * Created by Administrator on 2017/12/21.
 */

public class SendUrlUtils {
    public  static  String result;

//    public static String  sendRequestWithHttpClient(final Context context, final String url) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //用HttpClient发送请求，分为五步
//                //第一步：创建HttpClient对象
//                HttpClient httpCient = new DefaultHttpClient();
//                //第二步：创建代表请求的对象,参数是访问的服务器地址blMac=12:34:56:78:9A:BC&userId=1001&userName=%E5%BC%A0%E4%B8%89
//                //HttpGet httpGet = new HttpGet("http://192.168.1.101:8080/appUser/appUserLogin.do?loginType=1&loginAccount="+name1+"&loginPwd="+pass1);
//                HttpGet httpGet = new HttpGet(url);//测试链接
//                try {
//                    //第三步：执行请求，获取服务器发还的相应对象
//                    HttpResponse httpResponse = httpCient.execute(httpGet);
//                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
//                    System.out.println("状态码："+httpResponse.getStatusLine().getStatusCode());
//                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
//                        //第五步：从相应对象当中取出数据，放到entity当中
//                        HttpEntity entity = httpResponse.getEntity();
//                        String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
//                        if(response!=null){
//                            //在子线程中将Message对象发出去
//                            Message message = new Message();
//                            message.what = SHOW_RESPONSE;
//                            message.obj = response.toString();
//                            System.out.println("返回结果："+response);
//                            result=response.toString();
//
//                        }
//                    }else{
//                        Toast.makeText(context,"访问失败!!!请检查服务器...",Toast.LENGTH_LONG).show();
//                    }
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    System.out.println("访问失败！！！");
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();//这个start()方法不要忘记了
//        return result;
//
//    }
}
