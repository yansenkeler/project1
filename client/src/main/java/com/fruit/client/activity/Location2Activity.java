package com.fruit.client.activity;

import android.app.Application;
import android.content.Intent;
import com.fruit.client.MyApplication;
import com.fruit.client.R;
import com.fruit.client.util.Locationor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.fruit.baiduapi.util.BaiduGeocodeManager;
import com.fruit.common.ui.ToastUtil;
import com.fruit.core.activity.FruitActivity;
import com.fruit.core.db.DBUtil;

/**
 * Created by user on 2016/3/24.
 */
public class Location2Activity extends FruitActivity implements BaiduMap.OnMapStatusChangeListener, BaiduGeocodeManager.DealResponseData, MyApplication.OnReceiveData {
    private TextureMapView mMapView;
    private TextView mFinish;
    private TextView currentAddress;
    private ImageView mLocation;
    private BaiduMap mBaiduMap;
    private BaiduGeocodeManager mGeocodeManager;
    private MyApplication mMyApplication;
    private LinearLayout progress;

    private double lat, lon;
    private String currentAddressString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mMapView = (TextureMapView)findViewById(R.id.mapview);
        mFinish = (TextView)findViewById(R.id.finish);
        currentAddress = (TextView)findViewById(R.id.currentAddress);
        mLocation = (ImageView)findViewById(R.id.location);
        progress = (LinearLayout)findViewById(R.id.progress);
        currentAddress.setText("选择护栏的终点位置");
        mBaiduMap = mMapView.getMap();
        mMapView.removeViewAt(1);
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(true);
        Application mApplication = getApplication();
        if (mApplication instanceof MyApplication){
            mMyApplication = (MyApplication)mApplication;
        }
        mGeocodeManager = BaiduGeocodeManager.getInstance();
        mFinish.setOnClickListener(this);
        mLocation.setOnClickListener(this);
        mBaiduMap.setOnMapStatusChangeListener(this);
        String tmpLatitude = DBUtil.getConfigValue("last_location_latitude");
        String tmpLongitude = DBUtil.getConfigValue("last_location_longitude");
        if (tmpLatitude!=null && tmpLatitude.length()>0
                && tmpLongitude!=null && tmpLongitude.length()>0){
            double lastLatitude = Double.parseDouble(tmpLatitude);
            double lastLongitude = Double.parseDouble(tmpLongitude);
            Locationor.getInstance(getApplication()).
                    updateLocation(mMapView, new LatLng(lastLatitude, lastLongitude), 16);
        }
    }

    @Override
    public void onFruitActivityClick(View view) {
        super.onFruitActivityClick(view);
        switch (view.getId()){
            case R.id.finish:
                if (lat==0 || lon==0 || currentAddressString==null){
                    ToastUtil.showShort(this, "无法定位当前位置，点击左下角按钮重新定位");
                }else {
                    Intent intent = new Intent();
                    intent.putExtra("latitude", lat);
                    intent.putExtra("longitude", lon);
                    intent.putExtra("end_address", currentAddressString);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case R.id.location:
                progress.setVisibility(View.VISIBLE);
                mMyApplication.setReceiveDataListener(this);
                Locationor.getInstance(getApplication()).startLocation();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        mMyApplication.setReceiveDataListener(this);
        Locationor.getInstance(getApplication()).startLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mMapStatus) {

    }

    @Override
    public void onMapStatusChange(MapStatus mMapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mMapStatus) {
        lat = mMapStatus.target.latitude;
        lon = mMapStatus.target.longitude;
        mGeocodeManager.getAddressByLatAndLon(lat, lon, this);
    }

    @Override
    public void onReceiveData(String data, int taskid) {
        if (taskid==BaiduGeocodeManager.TASK_GET_ADDR){
            JSONObject mJSONObject = JSONObject.parseObject(data);
            int status = mJSONObject.getInteger("status");
            if (status==0){
                JSONObject result = mJSONObject.getJSONObject("result");
                String address = result.getString("formatted_address");
                currentAddressString = address;
            }
        }else {
            ToastUtil.showShort(this, "无法定位");
        }
    }

    @Override
    public void onReceive(double longitude, double latitude) {
        progress.setVisibility(View.GONE);
        lat = latitude;
        lon = longitude;
        Locationor.getInstance(getApplication()).updateLocation(mMapView, new LatLng(latitude, longitude), 16);
        mGeocodeManager.getAddressByLatAndLon(lat, lon, this);
    }
}
