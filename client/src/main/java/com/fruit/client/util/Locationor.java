package com.fruit.client.util;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by user on 2016/4/27.
 */
public class Locationor {
    private static Locationor instance;
    private LocationClient mLocationClient;
    private LocationClientOption mClientOption;
    private BDLocationListener mBDLocationListener;

    private boolean isOpenLocation;

    public Locationor(Context mContext){
        if (mLocationClient==null){
            mLocationClient = new LocationClient(mContext);
        }
        if (mClientOption==null){
            mClientOption = new LocationClientOption();
        }
    }

    public static synchronized Locationor getInstance(Context mContext){
        if (instance==null){
            instance = new Locationor(mContext);
        }
        return instance;
    }

    public void setLocation(BDLocationListener mLocationListener){
        if (mBDLocationListener==null){

        }
        mBDLocationListener = mLocationListener;
        if (!isOpenLocation){
            mClientOption.setIsNeedAddress(true);
            mClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
            mClientOption.setCoorType("bd09ll");
            mClientOption.setPriority(LocationClientOption.GpsFirst);
            mClientOption.setProdName("client");
            mClientOption.setOpenGps(true);
            mLocationClient.setLocOption(mClientOption);
            mLocationClient.registerLocationListener(mBDLocationListener);
        }
    }

    public void updateLocation(TextureMapView mBaiduMapView,  LatLng cenpt ,int zoom){
        try {
            //定义地图状态
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(cenpt)
                    .zoom(zoom)
                    .build();
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            BaiduMap mBaiduMap = mBaiduMapView.getMap();
            mBaiduMap.animateMapStatus(mMapStatusUpdate);
//            mBaiduMap.setMapStatus(mMapStatusUpdate);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void startLocation(){
        mLocationClient.start();
        mLocationClient.requestLocation();
    }

    public void destroyLocation(){
        mLocationClient.unRegisterLocationListener(mBDLocationListener);
    }
}
