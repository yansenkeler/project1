package com.fruit.client.activity;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.fruit.client.MyApplication;
import com.fruit.client.R;

import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.widget.TabHost;

import com.android.volley.VolleyError;
import com.fruit.client.util.Locationor;
import com.fruit.core.activity.FruitActivity;

/**
 * Created by user on 2016/3/3.
 */
public class TestActivity extends FruitActivity implements BDLocationListener {

    private TextureMapView mMapView;
    private TextureMapView mMapView2;
    private BaiduMap mBaiduMap;
    private BaiduMap mBaiduMap2;
    private ViewPager pager = null;
    private MyApplication mMyApplication;
    private Locationor locationor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        mMapView = (TextureMapView) findViewById(R.id.mTexturemap);
        mMapView2 = (TextureMapView) findViewById(R.id.mTexturemap2);
        //获取BaiduMap，用法与原MapView相同
        mBaiduMap = mMapView.getMap();
        mBaiduMap2 = mMapView2.getMap();

        Application mApplication = getApplication();
        if (mApplication instanceof MyApplication){
            mMyApplication = (MyApplication)mApplication;
        }

        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        // 如果没有继承TabActivity时，通过该种方法加载启动tabHost
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("texturehint").setIndicator("功能说明",
                null).setContent(
                R.id.texturehint));

        tabHost.addTab(tabHost.newTabSpec("mTexturemap").setIndicator("地图")
                .setContent(R.id.mTexturemap));

        tabHost.addTab(tabHost.newTabSpec("textdesc").setIndicator("Scrollview页")
                .setContent(R.id.textdesc));
        locationor = new Locationor(this);
        locationor.setLocation(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mMapView.onResume();
        locationor.startLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // activity 销毁时同时销毁地图控件
        mMapView.onDestroy();
        locationor.destroyLocation();
    }

    @Override
    public void dealFailureResult(VolleyError returnObject, int taskid) {
        super.dealFailureResult(returnObject, taskid);
    }

    @Override
    public void dealSuccessResult(Object returnObject, int taskid) {
        super.dealSuccessResult(returnObject, taskid);
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
//        locationor.updateLocation(mMapView, new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()), 16);
    }
}
