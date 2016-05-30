package com.fruit.client.fragment;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.fruit.baiduapi.util.BaiduLocationUtil;
import com.fruit.client.MyApplication;
import com.fruit.client.R;
import com.fruit.client.activity.EventDetailEditActivity;
import com.fruit.client.object.event.Event;
import com.fruit.client.object.event.EventListResponse;
import com.fruit.client.util.Constant;
import com.fruit.client.util.Locationor;
import com.fruit.client.util.Urls;
import com.fruit.common.network.NetWorkUtil;
import com.fruit.common.ui.ToastUtil;
import com.fruit.core.db.DBUtil;
import com.fruit.core.fragment.FruitFragment;
import com.fruit.core.http.VolleyManager;
import com.fruit.widget.MultiStateView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * Created by user on 2016/4/23.
 */
public class HomeFragment extends FruitFragment implements BaiduMap.OnMapStatusChangeListener, MyApplication.OnReceiveData, BaiduMap.OnMarkerClickListener {
    private static final int TASK_GET_EVENTS = 0;
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private BaiduLocationUtil mLocationUtil;
//    private Timer mTimer;
//    private LocationTimerTask timerTask;
    private MyApplication mMyApplication;
    private FrameLayout locationBtn;
    private ArrayList<Event> events = new ArrayList<>();

    private double lat, lon;
    private String roleName;
    private String deptName;
    private String userName;
    private boolean requestTwice = false;

    public HomeFragment(){}

    public static HomeFragment getInstance(){
        HomeFragment mFragment = new HomeFragment();
        Bundle mBundle = new Bundle();
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = getArguments();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, null, false);
        mMapView = (TextureMapView)v.findViewById(R.id.map_view);
        locationBtn = (FrameLayout)v.findViewById(R.id.location);

        mBaiduMap = mMapView.getMap();
        mMapView.removeViewAt(1);
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(true);
        Application mApplication = getActivity().getApplication();
        if (mApplication instanceof MyApplication){
            mMyApplication = (MyApplication)mApplication;
        }
        String tmpLatitude = DBUtil.getConfigValue("last_location_latitude");
        String tmpLongitude = DBUtil.getConfigValue("last_location_longitude");
        if (tmpLatitude!=null && tmpLatitude.length()>0
                && tmpLongitude!=null && tmpLongitude.length()>0){
            double lastLatitude = Double.parseDouble(tmpLatitude);
            double lastLongitude = Double.parseDouble(tmpLongitude);
            Locationor.getInstance(getActivity().getApplication()).
                    updateLocation(mMapView, new LatLng(lastLatitude, lastLongitude), 16);
        }
        mBaiduMap.setOnMarkerClickListener(this);
        locationBtn.setOnClickListener(this);
//        mTimer = new Timer();
//        timerTask = new LocationTimerTask();
//        mTimer.schedule(timerTask, 0, 3000);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

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
//        mTimer.cancel();
//        timerTask.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mBaiduMap.clear();
        mMyApplication.setReceiveDataListener(this);
        Locationor.getInstance(getActivity().getApplication()).startLocation();
        getEventList();
    }

    private void getEventList(){
        Map<String, String> params = new HashMap<>();
        roleName = DBUtil.getConfigValue("role_name");
        deptName = DBUtil.getConfigValue("dept_name");
        userName = DBUtil.getConfigValue("user_name");
        //施工，路政确认只需要获取处理中状态的事件
        if (roleName.equals(Constant.RoleName.CONSTRUCTION) || roleName.equals(Constant.RoleName.LUZHENG_CONFIRM)){
            params.put("state", "处理中");
            params.put("rolename", roleName);
            params.put("deptname", deptName);
        }else {
            //系统管理员，路政，路网，养护需要先获取草稿状态的事件
            params.put("state", "草稿");
            params.put("rolename", roleName);
            params.put("deptname", deptName);
            params.put("username", userName);
        }
        String requestBody = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(getActivity()).JsonPostRequest(null, "utf-8", requestBody,
                params, Urls.ROOT+Urls.EVENT_QUERY, Object.class, this, TASK_GET_EVENTS);
    }

    @Override
    public void onFruitClick(int id) {
        switch (id){
            case R.id.location:
                mMyApplication.setReceiveDataListener(this);
                Locationor.getInstance(getActivity().getApplication()).startLocation();
                break;
            default:
                break;
        }
    }

    @Override
    public void dealFailResult(VolleyError returnString, int taskid) {
        super.dealFailResult(returnString, taskid);
        switch (taskid){
            case TASK_GET_EVENTS:
                ToastUtil.showShort(getActivity(), "获取事件失败");
                break;
            default:
                break;
        }
    }

    @Override
    public void dealSuccessResult(Object data, int taskid) {
        super.dealSuccessResult(data, taskid);
        switch (taskid){
            case TASK_GET_EVENTS:
                if (data!=null){
                    JSONObject jsonObject = (JSONObject)data;
                    String flag = jsonObject.getString("flag");
                    if (flag.equals("0000")){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObject1.getJSONArray("list");
                        for (int i=0; i<jsonArray.size(); i++){
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            Event event = new Event();
                            event.setBillNo(jsonObject2.getString("BillNo"));
                            event.setLat(jsonObject2.getString("Lat"));
                            event.setLon(jsonObject2.getString("Lon"));
                            event.setState(jsonObject2.getString("State"));
                            event.setUserName(jsonObject2.getString("userName"));
                            events.add(event);
                        }
                        if (roleName.equals(Constant.RoleName.CONSTRUCTION)||roleName.equals(Constant.RoleName.LUZHENG_CONFIRM)
                                ||roleName.equals(Constant.RoleName.SYSTEM_ADMINSTRATIONAR)||requestTwice){
                            if (events.size()>0){
                                dealReceivedEvents();
                            }
                        }else {
                            //继续获取处理中的事件
                            Map<String, String> params = new HashMap<>();
                            params.put("state", "处理中");
                            params.put("rolename", roleName);
                            params.put("deptname", deptName);
                            String requestBody = NetWorkUtil.appendParameter(Constant.url, params);
                            VolleyManager.newInstance(getActivity()).JsonPostRequest(null, "utf-8", requestBody,
                                    params, Urls.ROOT+Urls.EVENT_QUERY, Object.class, this, TASK_GET_EVENTS);
                            requestTwice = true;
                        }
                    }else if (flag.equals("0001")){
                        if (requestTwice){
                            if (events.size()>0){
                                dealReceivedEvents();
                            }
                        }else {
                            if (roleName.equals(Constant.RoleName.CONSTRUCTION)||roleName.equals(Constant.RoleName.LUZHENG_CONFIRM)
                                    ||roleName.equals(Constant.RoleName.SYSTEM_ADMINSTRATIONAR)){
                            }else {
                                Map<String, String> params = new HashMap<>();
                                params.put("state", "处理中");
                                params.put("rolename", roleName);
                                params.put("deptname", deptName);
                                String requestBody = NetWorkUtil.appendParameter(Constant.url, params);
                                VolleyManager.newInstance(getActivity()).JsonPostRequest(null, "utf-8", requestBody,
                                        params, Urls.ROOT+Urls.EVENT_QUERY, Object.class, this, TASK_GET_EVENTS);
                                requestTwice = true;
                            }
                        }
                    }else {
                        ToastUtil.showShort(getActivity(), "获取事件失败");
                    }
                }
                break;
            default:
                break;
        }
    }

    private void dealReceivedEvents() {
        for (Event event: events){
            addMarker(event);
        }
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
    }

    @Override
    public void onReceive(double longitude, double latitude) {
        lat = latitude;
        lon = longitude;
        Locationor.getInstance(getActivity().getApplication()).updateLocation(mMapView, new LatLng(lat, lon), 16);
        DBUtil.setConfigValue("last_location_latitude", lat+"");
        DBUtil.setConfigValue("last_location_longitude", lon+"");
        addMyMarker(longitude, latitude);
    }

    private void addMyMarker(double latitude, double longitude) {
        LatLng mLatLng = new LatLng(latitude, longitude);
        BitmapDescriptor mDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.point);
        OverlayOptions mOptions = new MarkerOptions()
                .position(mLatLng)
                .icon(mDescriptor);
        mBaiduMap.addOverlay(mOptions);
    }

    private void addMarker(Event event) {
        if (event.getState().equals("草稿")){
            double latitude = Double.parseDouble(event.getLat());
            double longitude = Double.parseDouble(event.getLon());
            LatLng mLatLng = new LatLng(latitude, longitude);
            Bundle bundle  = new Bundle();
            bundle.putString("bill_no", event.getBillNo());
            BitmapDescriptor mDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.mark_draft);
            OverlayOptions mOptions = new MarkerOptions()
                    .position(mLatLng)
                    .extraInfo(bundle)
                    .icon(mDescriptor);
            mBaiduMap.addOverlay(mOptions);
        }else if(event.getState().equals("业务部门确认")){
            double latitude = Double.parseDouble(event.getLat());
            double longitude = Double.parseDouble(event.getLon());
            LatLng mLatLng = new LatLng(latitude, longitude);
            Bundle bundle  = new Bundle();
            bundle.putString("bill_no", event.getBillNo());
            BitmapDescriptor mDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.mark_confirm1);
            OverlayOptions mOptions = new MarkerOptions()
                    .position(mLatLng)
                    .extraInfo(bundle)
                    .icon(mDescriptor);
            mBaiduMap.addOverlay(mOptions);
        }else if (event.getState().equals("施工确认")){
            double latitude = Double.parseDouble(event.getLat());
            double longitude = Double.parseDouble(event.getLon());
            LatLng mLatLng = new LatLng(latitude, longitude);
            Bundle bundle  = new Bundle();
            bundle.putString("bill_no", event.getBillNo());
            BitmapDescriptor mDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.mark_confirm2);
            OverlayOptions mOptions = new MarkerOptions()
                    .position(mLatLng)
                    .extraInfo(bundle)
                    .icon(mDescriptor);
            mBaiduMap.addOverlay(mOptions);
        }else if (event.getState().equals("施工")){
            double latitude = Double.parseDouble(event.getLat());
            double longitude = Double.parseDouble(event.getLon());
            LatLng mLatLng = new LatLng(latitude, longitude);
            Bundle bundle  = new Bundle();
            bundle.putString("bill_no", event.getBillNo());
            BitmapDescriptor mDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.mark_constr);
            OverlayOptions mOptions = new MarkerOptions()
                    .position(mLatLng)
                    .extraInfo(bundle)
                    .icon(mDescriptor);
            mBaiduMap.addOverlay(mOptions);
        }else if (event.getState().equals("竣工验收")){
            double latitude = Double.parseDouble(event.getLat());
            double longitude = Double.parseDouble(event.getLon());
            LatLng mLatLng = new LatLng(latitude, longitude);
            Bundle bundle  = new Bundle();
            bundle.putString("bill_no", event.getBillNo());
            BitmapDescriptor mDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.mark_finish);
            OverlayOptions mOptions = new MarkerOptions()
                    .position(mLatLng)
                    .extraInfo(bundle)
                    .icon(mDescriptor);
            mBaiduMap.addOverlay(mOptions);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (DBUtil.getConfigValue("initial").equals("1")){
            Bundle bundle = marker.getExtraInfo();
            String billno = bundle.getString("bill_no");
            Intent intent = new Intent(getActivity(), EventDetailEditActivity.class);
            intent.putExtra("bill_no", billno);
            startActivity(intent);
            return true;
        }else {
            ToastUtil.showShort(getActivity(), "请先初始化数据");
            return false;
        }
    }

    class LocationTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
        }
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mMyApplication.setReceiveDataListener(HomeFragment.this);
            Locationor.getInstance(getActivity().getApplication()).startLocation();
        }
    };
}
