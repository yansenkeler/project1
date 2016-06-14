package com.fruit.client.fragment;

import android.app.Application;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fruit.baiduapi.util.BaiduLocationUtil;
import com.fruit.client.MyApplication;
import com.fruit.client.R;
import com.fruit.client.activity.MonitorActivity;
import com.fruit.client.object.Monitor;
import com.fruit.client.util.Constant;
import com.fruit.client.util.Locationor;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.fruit.client.util.Urls;
import com.fruit.common.network.NetWorkUtil;
import com.fruit.common.phone.PhoneHelp;
import com.fruit.common.phone.Service;
import com.fruit.common.res.DensityUtil;
import com.fruit.common.ui.ToastUtil;
import com.fruit.core.db.DBUtil;
import com.fruit.core.fragment.FruitFragment;
import com.fruit.core.http.VolleyManager;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2016/4/23.
 */
public class MonitorFragment extends FruitFragment implements MyApplication.OnReceiveData, BaiduMap.OnMarkerClickListener {
    private static final String TAG = MonitorFragment.class.getSimpleName();
    private static final int TASK_GET_MONITORS = 0;
    private TextureMapView mMapView;

    private BaiduMap mBaiduMap;
    private FrameLayout location;
    private MyApplication mApplication;
    private double latitude;
    private double longitude;
    private ArrayList<Monitor> monitors = new ArrayList<>();

    private AlertDialog ad;
    private boolean isLocation = false;

    public MonitorFragment(){}

    public MonitorFragment getInstance(){
        MonitorFragment mMonitorFragment = new MonitorFragment();
        Bundle mBundle = new Bundle();
        mMonitorFragment.setArguments(mBundle);
        return mMonitorFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_monitor, null, false);
        mMapView = (TextureMapView)v.findViewById(R.id.map_view);
        location = (FrameLayout)v.findViewById(R.id.location);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApplication.setReceiveDataListener(MonitorFragment.this);
                Locationor.getInstance(getActivity().getApplication()).startLocation();
                isLocation = true;
                showDialog("定位中...", getActivity());
            }
        });

        mBaiduMap = mMapView.getMap();
        mMapView.removeViewAt(1);
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(true);
        Locationor.getInstance(getActivity().getApplication()).updateLocation(mMapView, new LatLng(120.552644,31.874786), 16);

        Application mApplication1 = getActivity().getApplication();
        if (mApplication1 instanceof MyApplication){
            mApplication = (MyApplication)mApplication1;
        }
        String tmpLatitude = DBUtil.getConfigValue("last_location_latitude");
        String tmpLongitude = DBUtil.getConfigValue("last_location_longitude");
        if (tmpLatitude!=null && tmpLatitude.length()>0
                && tmpLongitude!=null && tmpLongitude.length()>0){
            double lastLatitude = Double.parseDouble(tmpLatitude);
            double lastLongitude = Double.parseDouble(tmpLongitude);
            Locationor.getInstance(getActivity().getApplication()).
                    updateLocation(mMapView, new LatLng(lastLatitude, lastLongitude), 16);
//            BaiduLocationUtil.updateLocation(mMapView, new LatLng(lastLatitude, lastLongitude), 16);
        }
        mBaiduMap.setOnMarkerClickListener(this);
        ad = new AlertDialog.Builder(getActivity())
                .create();
        return v;
    }

    private void getMonitors(){
        Map<String, String> params = new HashMap<>();
        params.put("type", "query");
        params.put("facilityname", "监控点");
        String s = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(getActivity()).JsonPostRequest(null, "utf-8", s, params,
                Urls.ROOT + Urls.ADD_OTHER, Object.class, this, TASK_GET_MONITORS);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mApplication.setReceiveDataListener(this);
        Locationor.getInstance(getActivity().getApplication()).startLocation();
        getMonitors();
        showDialog("正在获取监控点", getActivity());
        mMapView.onResume();
    }

    @Override
    public void onFruitClick(int id) {
    }

    @Override
    public void dealFailResult(VolleyError returnString, int taskid) {
        super.dealFailResult(returnString, taskid);
        switch(taskid){
            case TASK_GET_MONITORS:
                hideProgressDialog();
                ToastUtil.showShort(getActivity(), "获取监控点失败");
                break;
            default:
                break;
        }
    }

    @Override
    public void dealSuccessResult(Object data, int taskid) {
        super.dealSuccessResult(data, taskid);
        switch (taskid){
            case TASK_GET_MONITORS:
                if (data!=null){
                    JSONObject jsonObject = (JSONObject)data;
                    String flag = jsonObject.getString("flag");
                    if (flag.equals("0000")){
                        monitors.clear();
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObject1.getJSONArray("list");
                        for (int i=0; i<jsonArray.size(); i++){
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            Monitor monitor = new Monitor();
                            monitor.setLatitude(jsonObject2.getString("Latitude"));
                            monitor.setLongitude(jsonObject2.getString("Longitude"));
                            monitor.setFacilityPK(jsonObject2.getString("FacilityPK"));
                            monitor.setDescription(jsonObject2.getString("Description"));
                            monitor.setLocation(jsonObject2.getString("Location"));
                            monitors.add(monitor);
                            addMark(monitor);
                        }
                        hideProgressDialog();
                    }else if(flag.equals("0001")){
                        hideProgressDialog();
                        ToastUtil.showShort(getActivity(), "暂无监控点");
                    }else {
                        hideProgressDialog();
                        ToastUtil.showShort(getActivity(), "获取监控点失败");
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onReceive(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        Locationor.getInstance(getActivity().getApplication())
                .updateLocation(mMapView, new LatLng(this.latitude, this.longitude), 16);
//      BaiduLocationUtil.updateLocation(mMapView, new LatLng(this.latitude, this.longitude), 16);
        DBUtil.setConfigValue("last_location_latitude", this.latitude + "");
        DBUtil.setConfigValue("last_location_longitude", this.longitude + "");
        if (isLocation){
            hideProgressDialog();
            isLocation = false;
        }
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
        mBaiduMap.addOverlay(option);
    }

    @Override
    public boolean onMarkerClick(Marker mMarker) {
        Intent intent = new Intent(getActivity(), MonitorActivity.class);
        intent.putParcelableArrayListExtra("monitors", monitors);
        startActivity(intent);
        return true;
    }

    private void toggleVideoData(Marker mMarker) {
        LatLng latLng = mMarker.getPosition();
        Log.d("toggle camera", "经纬度是："+latLng.longitude+" "+latLng.latitude);
        BaiduLocationUtil.updateLocation(mMapView, latLng, 16);
    }

    private void showVideoDialog(){
        ad.show();
        ad.setContentView(R.layout.layout_monitor_view);
        Window dialogWindow = ad.getWindow();
        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogWindow.setGravity(Gravity.TOP);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.dimAmount = 0f;
        dialogWindow.setAttributes(lp);
        VideoView videoView = (VideoView)ad.findViewById(R.id.videoview);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
        layoutParams.width = PhoneHelp.getPhoneWidth(getActivity());
        layoutParams.height = DensityUtil.dip2px(getActivity(), 200);
        String uri = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.mp;
        videoView.setVideoURI(Uri.parse(uri));
        videoView.start();
    }

}
