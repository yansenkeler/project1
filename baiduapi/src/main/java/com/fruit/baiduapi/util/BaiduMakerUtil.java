package com.fruit.baiduapi.util;

import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.fruit.baiduapi.model.AddressInfo;

import java.util.List;

/**
 * Created by liangchen on 15/7/12.
 * 覆盖物添加
 */
public class BaiduMakerUtil {

    private static BaiduMakerUtil baiduMakerUtil;

    private BaiduMakerUtil (){}

    public static BaiduMakerUtil getInstance(){

        if (baiduMakerUtil ==null){
            baiduMakerUtil = new BaiduMakerUtil();
        }
        return baiduMakerUtil;
    }

    /**
     * 初始化图层
     */
    public void addAddressInfosOverlay(BaiduMap mBaiduMap,List<AddressInfo> infos,BitmapDescriptor bitmapDescriptor)
    {
        mBaiduMap.clear();
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        for (AddressInfo info : infos)
        {
            // 位置
            latLng = new LatLng(info.getLatitude(), info.getLongitude());
            // 图标
            overlayOptions = new MarkerOptions().position(latLng)
                    .icon(bitmapDescriptor).zIndex(5);
            marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);
        }
        // 将地图移到到最后一个经纬度位置
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(u);
    }


}
