package com.fruit.baiduapi.util;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 2016/3/11.
 */
public class BaiduGeocodeManager {
    private static final String geocoderUrl = "http://api.map.baidu.com/geocoder/v2/";
    private static final String ak = "4bcbi0rj9wm19Nww4i87modf";
    private static final String mcode = "45:8C:8B:BB:DF:4D:48:14:F9:B1:62:52:45:B5:1A:3E:D0:9C:FB:F0;com.fruit.client";
    public static final int TASK_GET_ADDR = 0;
    public static final int TASK_GET_LATLNG = 1;
    private DealResponseData mDealResponseData;
    private static BaiduGeocodeManager mGeocodeManager;

    private BaiduGeocodeManager(){}

    public static synchronized BaiduGeocodeManager getInstance(){
        if (mGeocodeManager==null){
            mGeocodeManager = new BaiduGeocodeManager();
        }
        return mGeocodeManager;
    }

    final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String response = (String) msg.obj;
            int taskid = msg.arg1;
            mDealResponseData.onReceiveData((String) msg.obj, taskid);
        }
    };

    public void getAddressByLatAndLon(double latitude, double longitude, DealResponseData mInterface){
        mDealResponseData = mInterface;
        final String url = String.format(geocoderUrl+"?location=%f,%f&output=json&ak=%s&pos=0&mcode=%s", latitude, longitude, ak, mcode);
        HttpThread mThread = new HttpThread(url, TASK_GET_ADDR);
        mThread.start();
    }

    public void getLatLonByAddressString(String address, final DealResponseData latLonReturnInterface){
        String addr = null;
        try {
            addr = URLEncoder.encode(address, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = String.format(geocoderUrl + "?address=%s&output=json&ak=%s&mcode=%s", addr, ak, mcode);
        HttpThread mThread = new HttpThread(url, TASK_GET_LATLNG);
        mThread.start();
    }

    class HttpThread extends Thread{
        private String requestString;
        private int taskid;

        public HttpThread(String mRequestString, int mTaskid) {
            requestString = mRequestString;
            taskid = mTaskid;
        }

        @Override
        public void run() {
            super.run();
            URL url1 = null;
            URLConnection connection = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            try {
                url1 = new URL(requestString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                connection = url1.openConnection();
                isr = new InputStreamReader(connection.getInputStream(), "utf-8");
                br = new BufferedReader(isr);
                String data = null;
                final StringBuffer sb = new StringBuffer();
                int count = 1;
                while((data=br.readLine())!=null){
                    sb.append(data);
                }
                Message mMessage = new Message();
                mMessage.obj = sb.toString();
                mMessage.arg1 = taskid;
                mHandler.sendMessage(mMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(isr!=null){
                    try {
                        isr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(br!=null){
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public interface DealResponseData{
        void onReceiveData(String data, int taskid);
    }
}
