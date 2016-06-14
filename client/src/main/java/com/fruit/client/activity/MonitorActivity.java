package com.fruit.client.activity;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.VideoView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.fruit.baiduapi.util.BaiduLocationUtil;
import com.fruit.client.MyApplication;
import com.fruit.client.R;
import com.fruit.client.object.Monitor;
import com.fruit.client.util.Locationor;
import com.fruit.core.activity.FruitActivity;
import com.fruit.core.activity.templet.NaviActivity;

import java.util.ArrayList;

/**
 * Created by John on 2016/5/27.
 */
public class MonitorActivity extends FruitActivity implements MyApplication.OnReceiveData, BaiduMap.OnMarkerClickListener {
    private ArrayList<Monitor> monitors = new ArrayList<>();
    private VideoView videoView;
    private TextureMapView mapView;
    private FrameLayout location;
    private BaiduMap baiduMap;
    private MyApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent!=null && intent.hasExtra("monitors")){
            monitors = intent.getParcelableArrayListExtra("monitors");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        videoView = (VideoView)findViewById(R.id.video_view);
        mapView = (TextureMapView)findViewById(R.id.map_view);
        location = (FrameLayout)findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApplication.setReceiveDataListener(MonitorActivity.this);
                Locationor.getInstance(getApplication()).startLocation();
                showDialog("定位中...");
            }
        });
        baiduMap = mapView.getMap();
        mapView.removeViewAt(1);
        mapView.showScaleControl(false);
        mapView.showZoomControls(true);
        Locationor.getInstance(getApplication()).updateLocation(mapView, new LatLng(120.552644,31.874786), 16);

        Application mApplication1 = getApplication();
        if (mApplication1 instanceof MyApplication){
            mApplication = (MyApplication)mApplication1;
        }
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.mp;
        videoView.setVideoURI(Uri.parse(uri));
        for (Monitor monitor: monitors){
            addMark(monitor);
        }
        baiduMap.setOnMarkerClickListener(this);
    }

    private void addMark(Monitor monitor){
        double lon = Double.parseDouble(monitor.getLongitude());
        double lat = Double.parseDouble(monitor.getLatitude());
        LatLng mLatLng = new LatLng(lat, lon);
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.c4);
        OverlayOptions option = new MarkerOptions()
                .position(mLatLng)
                .icon(bitmap)
                .zIndex(20);
        baiduMap.addOverlay(option);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mApplication.setReceiveDataListener(this);
        Locationor.getInstance(getApplication()).startLocation();
        videoView.start();
    }

    @Override
    public void onReceive(double longitude, double latitude) {
        hideProgressDialog();
        Locationor.getInstance(getApplication())
                .updateLocation(mapView, new LatLng(latitude, longitude), 16);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        toggleVideoData(marker);
        return true;
    }

    private void toggleVideoData(Marker mMarker) {
        LatLng latLng = mMarker.getPosition();
        BaiduLocationUtil.updateLocation(mapView, latLng, 16);
    }
}
