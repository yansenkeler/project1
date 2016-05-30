package com.fruit.client.activity;

import android.app.Application;
import android.content.Intent;
import com.fruit.client.MyApplication;
import com.fruit.client.R;
import com.fruit.client.object.SelectTypeAlertObj;
import com.fruit.client.util.Constant;
import com.fruit.client.util.Locationor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.fruit.baiduapi.util.BaiduGeocodeManager;
import com.fruit.common.ui.ToastUtil;
import com.fruit.core.activity.FruitActivity;
import com.fruit.core.db.DBUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user on 2016/3/24.
 */
public class LocationActivity extends FruitActivity implements MyApplication.OnReceiveData, BaiduMap.OnMapStatusChangeListener, BaiduGeocodeManager.DealResponseData, BaiduMap.OnMapTouchListener {
    private static final int REQUESTCODE = 0;
    private static final String TAG = LocationActivity.class.getSimpleName();
    private TextureMapView mMapView;
    private TextView mFinish;
    private TextView currentAddress;
    private ImageView mLocation;
    private LinearLayout floatingProgress;
    private BaiduMap mBaiduMap;
    private BaiduGeocodeManager mGeocodeManager;
    private ArrayList<SelectTypeAlertObj> mAlertObjs = new ArrayList<>();
    private double lat, lon;
    private double tmpLat, tmpLon;
    private String[] types = new String[]{"路桩", "标志设施", "其他设施"};
    private int addType = 0;
    private String currentAddressString;
    private MyApplication mApplication;
//    private Timer mTimer;
//    private LocationTimerTask mTask;
    private ImageView mPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent!=null && intent.hasExtra("type")){
            addType = intent.getIntExtra("type", 0);
        }else {
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        initData();
        mMapView = (TextureMapView)findViewById(R.id.mapview);
        mFinish = (TextView)findViewById(R.id.finish);
        currentAddress = (TextView)findViewById(R.id.currentAddress);
        mLocation = (ImageView)findViewById(R.id.location);
        floatingProgress = (LinearLayout)findViewById(R.id.progress);
        mPin = (ImageView)findViewById(R.id.pin);

        if (addType==Constant.AddType.TYPE_PILE){
            currentAddress.setText("选择你要添加路桩的位置");
        }else if (addType==Constant.AddType.TYPE_MARK){
            currentAddress.setText("选择你要添加标志设施的位置");
        }else if (addType==Constant.AddType.TYPE_FENCE){
            currentAddress.setText("选择你要添加护栏设施的位置");
        }else if (addType==Constant.AddType.TYPE_OTHER){
            currentAddress.setText("选择你要添加其他设施的位置");
        }else if(addType==Constant.AddType.TYPE_EVENT){
            currentAddress.setText("选择你要添加事件的位置");
        }
        mBaiduMap = mMapView.getMap();
        mMapView.removeViewAt(1);
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(true);
        mGeocodeManager = BaiduGeocodeManager.getInstance();
        mFinish.setOnClickListener(this);
        mLocation.setOnClickListener(this);
        mBaiduMap.setOnMapStatusChangeListener(this);
        mBaiduMap.setOnMapTouchListener(this);
        Application mApplication1= getApplication();
        if (mApplication1 instanceof MyApplication){
            mApplication = (MyApplication)mApplication1;
        }
        String tmpLatitude = DBUtil.getConfigValue("last_location_latitude");
        String tmpLongitude = DBUtil.getConfigValue("last_location_longitude");
        if (tmpLatitude!=null && tmpLatitude.length()>0
                && tmpLongitude!=null && tmpLongitude.length()>0){
            double lastLatitude = Double.parseDouble(tmpLatitude);
            double lastLongitude = Double.parseDouble(tmpLongitude);
            Locationor.getInstance(getApplication()).
                    updateLocation(mMapView, new LatLng(lastLatitude, lastLongitude), 16);
        }
//        mTimer = new Timer();
//        mTask = new LocationTimerTask();
//        mTimer.schedule(mTask, 0, 3000);
        Log.d(TAG, "at first schedule a timer task which will be executed immediately");

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        mApplication.setReceiveDataListener(LocationActivity.this);
        Locationor.getInstance(getApplication()).startLocation();
    }

    private void initData(){
        mAlertObjs.add(new SelectTypeAlertObj(R.drawable.ic_launcher, types[0]));
        mAlertObjs.add(new SelectTypeAlertObj(R.drawable.ic_launcher, types[1]));
        mAlertObjs.add(new SelectTypeAlertObj(R.drawable.ic_launcher, types[2]));
    }

    @Override
    public void onFruitActivityClick(View view) {
        super.onFruitActivityClick(view);
        switch (view.getId()){
            case R.id.finish:
                if (lat==0 || lon==0 || currentAddressString==null){
                    ToastUtil.showShort(this, "无法定位当前位置，点击左下角按钮重新定位");
                }else {
                    if (addType== Constant.AddType.TYPE_PILE){
                        Intent intent = new Intent(LocationActivity.this, AddPileActivity.class);
                        intent.putExtra("latitude", lat);
                        intent.putExtra("longitude", lon);
                        intent.putExtra("currentAddress", currentAddressString);
                        startActivityForResult(intent, REQUESTCODE);
                    }else if (addType==Constant.AddType.TYPE_MARK){
                        Intent intent = new Intent(LocationActivity.this, AddMarkActivity.class);
                        intent.putExtra("latitude", lat);
                        intent.putExtra("longitude", lon);
                        intent.putExtra("currentAddress", currentAddressString);
                        startActivityForResult(intent, REQUESTCODE);
                    }else if (addType==Constant.AddType.TYPE_OTHER){
                        Intent intent = new Intent(LocationActivity.this, AddOtherActivity.class);
                        intent.putExtra("latitude", lat);
                        intent.putExtra("longitude", lon);
                        intent.putExtra("currentAddress", currentAddressString);
                        startActivityForResult(intent, REQUESTCODE);
                    }else if (addType==Constant.AddType.TYPE_FENCE){
                        Intent intent = new Intent(LocationActivity.this, AddFenceActivity.class);
                        intent.putExtra("latitude", lat);
                        intent.putExtra("longitude", lon);
                        intent.putExtra("start_address", currentAddressString);
                        startActivityForResult(intent, REQUESTCODE);
                    }else if(addType==Constant.AddType.TYPE_EVENT){
                        Intent intent = new Intent(LocationActivity.this, AddEventActivity.class);
                        intent.putExtra("latitude", lat);
                        intent.putExtra("longitude", lon);
                        intent.putExtra("start_address", currentAddressString);
                        startActivityForResult(intent, REQUESTCODE);
                    }
                }
                break;
            case R.id.location:
                mApplication.setReceiveDataListener(LocationActivity.this);
                Locationor.getInstance(getApplication()).startLocation();
                floatingProgress.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
//        mTask.cancel();
//        mTimer.cancel();
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

    private void addMarker(double lat, double lon){
        mBaiduMap.clear();
        LatLng mLatLng = new LatLng(lat, lon);
        BitmapDescriptor mDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.large_point);
        OverlayOptions mOptions = new MarkerOptions()
                .position(mLatLng)
                .icon(mDescriptor);
        mBaiduMap.addOverlay(mOptions);
    }

    @Override
    public void onReceive(double longitude, double latitude) {
        floatingProgress.setVisibility(View.GONE);
        lat = latitude;
        lon = longitude;
        tmpLat = lat;
        tmpLon = lon;
        addMarker(lat, lon);
        Locationor.getInstance(getApplication()).updateLocation(mMapView, new LatLng(lat, lon), 16);
        mGeocodeManager.getAddressByLatAndLon(lat, lon, this);
        Log.d(TAG, "receive location data in location activity");
    }

    @Override
    public void onTouch(MotionEvent mMotionEvent) {
        switch (mMotionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
//                mTask.cancel();
                Log.d(TAG, "moving map cancel the timer task");
                break;
            case MotionEvent.ACTION_UP:
//                mTask = new LocationTimerTask();
//                mTimer.schedule(mTask, 30000, 3000);
                Log.d(TAG, "schedule a timer task which will be executed after 10s");
                break;
            default:
                break;
        }
    }

    class LocationTimerTask extends TimerTask{

        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
        }
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mApplication.setReceiveDataListener(LocationActivity.this);
            Locationor.getInstance(getApplication()).startLocation();
        }
    };
}
