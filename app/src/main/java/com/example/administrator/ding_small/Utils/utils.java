package com.example.administrator.ding_small.Utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/29.
 */
public class utils {

    /**
     * 设置gridview固定宽度
     */
    public static void setGvwidth(BaseAdapter comAdapter, GridView gridView) {
        int listViewWidth = 0;
        int adaptCount = comAdapter.getCount();
        for (int i = 0; i < adaptCount; i++) {
            View temp = comAdapter.getView(i, null, gridView);
            temp.measure(0, 0);
            listViewWidth += temp.getMeasuredWidth();
        }
        ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
        layoutParams.width = listViewWidth;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        gridView.setLayoutParams(layoutParams);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return 屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }



    /**
     * 设置listview固定高度
     */
    public static void setLvHeight(BaseAdapter comAdapter, ListView listView) {
        int listViewHeight = 0;
        int adaptCount = comAdapter.getCount();
        for (int i = 0; i < adaptCount; i++) {
            View temp = comAdapter.getView(i, null, listView);
            temp.measure(0, 0);
            listViewHeight += temp.getMeasuredHeight();
        }

        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.FILL_PARENT;
        layoutParams.height = listViewHeight;
        listView.setLayoutParams(layoutParams);
    }


    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }



    /**
     * soap网络请求
     *
     * @param methodName  web service 方法名称
     * @param soapAction  web service 方法参数
     * @param params      web service 请求params
     * @param SERVICE_URL web service 请求url后缀
     * @return soap             web service 请求结果
     */
    public static String getRefreshSoapObject(String methodName, String soapAction, HashMap<String, Object> params, String SERVICE_URL) {
        String URL = "";
        String NAMESPACE = "";// 名称空间，服务器端生成的namespace属性值
        String METHOD_NAME = methodName;
        String SOAP_ACTION = soapAction;

        SoapObject soap = null;
        String str = null;
        try {
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            if (params != null && params.size() > 0) {
                for (Map.Entry<String, Object> item : params.entrySet()) {
                    rpc.addProperty(item.getKey(), item.getValue().toString());
                }
            }
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = rpc;
            envelope.dotNet = false;// true--net; false--java;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE ht = new HttpTransportSE(URL);
            ht.debug = true;
            ht.call(SOAP_ACTION, envelope);
            try {
                soap = (SoapObject) envelope.getResponse();
            } catch (Exception e) {
                soap = (SoapObject) envelope.bodyIn;
            }
            str = soap.getProperty("return").toString();

        } catch (ConnectException e) {
            //网络异常
            Log.e(methodName, e.toString());
            return "{\"msg\":\"webError\"}";
        } catch (SocketTimeoutException e) {
            //网络异常
            return "{\"msg\":\"webError\"}";
        } catch (XmlPullParserException e) {
            //数据解析异常
            Log.e(methodName, e.toString());
            return "{\"msg\":\"error\"}";
        } catch (IOException e) {
            //数据读取异常
            Log.e(methodName, e.toString());
            return "{\"msg\":\"error\"}";
        } catch (NullPointerException e) {
            //数据读取异常
            Log.e(methodName, e.toString());
            return "{\"msg\":\"error\"}";
        }

        return str;
    }

}
