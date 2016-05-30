package com.fruit.baiduapi.util;

import android.content.Context;
import android.location.Location;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;

/**
 * Created by user on 2016/3/11.
 */
public class BaiduClientFactory {
    private ArrayList<LocationClient> mClients = new ArrayList<>();
    private  ArrayList<OnLatLngResponse> mLatLngResponses = new ArrayList<>();
    private static BaiduClientFactory mClientFactory;
    private ArrayList<Integer> mIntegers = new ArrayList<>();

    public static synchronized BaiduClientFactory getInstance(){
        if (mClientFactory==null){
            mClientFactory = new BaiduClientFactory();
        }
        return mClientFactory;
    }

    public void addClient(final int index, final OnLatLngResponse mLatLngResponse, Context mContext){
        LocationClient mClient = new LocationClient(mContext);
        LocationClientOption locationClientOption = new LocationClientOption();
        locationClientOption.setIsNeedAddress(true);
        locationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationClientOption.setCoorType("bd09ll");
        locationClientOption.setPriority(LocationClientOption.NetWorkFirst);
        locationClientOption.setProdName("client");
        locationClientOption.setOpenGps(false);
        mClient.setLocOption(locationClientOption);
        mClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation mBDLocation) {
                mLatLngResponse.onLatLngReceive(mBDLocation, index);
            }
        });
        mIntegers.add(new Integer(index));
        mLatLngResponses.add(mLatLngResponse);
        mClients.add(mClient);
    }

    public void startLocation(int index, Context mContext, OnLatLngResponse mLatLngResponse){
        if (!mIntegers.contains(new Integer(index))){
            addClient(index, mLatLngResponse, mContext);
        }
        int wz = mIntegers.indexOf(new Integer(index));
        mClients.get(wz).start();
        mClients.get(wz).requestLocation();
    }

    public void stopAll(){
        for (int i=0; i<mClients.size(); i++){
            mClients.get(i).stop();
        }
        mClients.clear();
        mIntegers.clear();
        mLatLngResponses.clear();
    }

    public interface OnLatLngResponse{
        void onLatLngReceive(BDLocation mBDLocation, int index);
    }
}
