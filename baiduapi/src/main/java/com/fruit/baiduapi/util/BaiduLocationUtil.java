package com.fruit.baiduapi.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.fruit.baiduapi.model.AddressInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenliang on 15/7/23.
 */
public class BaiduLocationUtil {
    private static LocationClient locationClient;
    private static BDLocationListener l;
    private static BaiduLocationUtil util;

    public static BaiduLocationUtil getInstance(Context c, BDLocationListener listener){
        util = new BaiduLocationUtil();
        locationClient = new LocationClient(c);
        l = listener;
        return util;
    }

    public static void updateLocation(MapView mBaiduMapView,  LatLng cenpt ,int zoom){
        try {
            //定义地图状态
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(cenpt)
                    .zoom(zoom)
                    .build();
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            BaiduMap mBaiduMap = mBaiduMapView.getMap();
            mBaiduMap.setMapStatus(mMapStatusUpdate);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void updateLocation(TextureMapView mBaiduMapView,  LatLng cenpt ,int zoom){
        try {
            //定义地图状态
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(cenpt)
                    .zoom(zoom)
                    .build();
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            BaiduMap mBaiduMap = mBaiduMapView.getMap();
            mBaiduMap.setMapStatus(mMapStatusUpdate);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void setLocationDefault(BDLocation location,BaiduMap mBaiduMap,int drawable){
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(0)//定位光圈去掉
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(0).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);

        if (drawable==0){
            MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null);
            mBaiduMap.setMyLocationConfigeration(config);
        }else{
            // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                    .fromResource(drawable);
            MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker);
            mBaiduMap.setMyLocationConfigeration(config);
        }
    }

    public void startLBServer(){
        LocationClientOption locationClientOption = new LocationClientOption();
        locationClientOption.setIsNeedAddress(true);
        locationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationClientOption.setCoorType("bd09ll");
        locationClientOption.setPriority(LocationClientOption.GpsFirst);
        locationClientOption.setProdName("client");
        locationClientOption.setOpenGps(true);
//        locationClientOption.setScanSpan(1000);
        locationClient.setLocOption(locationClientOption);
        locationClient.registerLocationListener(l);
        locationClient.start();
        locationClient.requestLocation();
    }

    public void closeLBServer(){
        locationClient.stop();
        locationClient.unRegisterLocationListener(l);
    }

    public static void setToLocation(BaiduMap baiduMap, double lat, double lng){
        baiduMap.setMyLocationEnabled(true);
        MyLocationData data = new MyLocationData.Builder().latitude(lat).longitude(lng).build();
        baiduMap.setMyLocationData(data);
        MyLocationConfiguration.LocationMode mode = MyLocationConfiguration.LocationMode.NORMAL;
        MyLocationConfiguration configuration = new MyLocationConfiguration(mode, true, null);
        baiduMap.setMyLocationConfigeration(configuration);
        LatLng latLng = new LatLng(lat, lng);
        MapStatus mapStatus = new MapStatus.Builder().target(latLng).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        baiduMap.setMapStatus(mapStatusUpdate);
    }
}
