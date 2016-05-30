package com.fruit.client.activity;

import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import com.fruit.client.MyApplication;
import com.fruit.client.R;
import com.fruit.client.object.RoutePlan;
import com.fruit.client.object.RoutePoint;
import com.fruit.client.object.event.Event;
import com.fruit.client.util.Constant;
import com.fruit.client.util.Urls;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.fruit.common.network.NetWorkUtil;
import com.fruit.common.ui.ToastUtil;
import com.fruit.core.activity.FruitActivity;
import com.fruit.core.http.VolleyManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by John on 2016/5/12.
 */
public class RouteNavigationActivity extends FruitActivity implements MyApplication.OnReceiveData {
    private static final int TASK_QUERY_POINT = 1;
    private static final int TASK_QUERY_EVENT = 2;
    private TextureMapView mapView;
    private BaiduMap baiduMap;
    private RoutePlan routePlan;
    private ArrayList<RoutePoint> routePoints = new ArrayList<>();
    private ArrayList<Event> events = new ArrayList<>();
    private MyApplication myApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent!=null && intent.hasExtra("route_plan")){
            routePlan = intent.getParcelableExtra("route_plan");
        }else {
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_navigation);
        mapView = (TextureMapView)findViewById(R.id.map_view);
        baiduMap = mapView.getMap();

        Application application = getApplication();
        if (application instanceof MyApplication){
            myApplication = (MyApplication)application;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
//        showDialog("正在准备巡查信息...");
//        getPlanDetail(routePlan.getPlanPk());
//        myApplication.setReceiveDataListener(this);
//        Locationor.getInstance(getApplication()).startLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void getPlanDetail(String planpk){
        Map<String, String> params =new HashMap<>();
        params.put("type", "queryPoint");
        params.put("planpk", planpk);
        String s = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", s, params, Urls.ROOT+Urls.INSPECT_TASK,
                Object.class, this, TASK_QUERY_POINT);
    }

    private void getEventList(){
        Map<String, String> params = new HashMap<>();
        params.put("type", "queryEvent");
        params.put("planpk", routePlan.getPlanPk());
        String s = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", s, params, Urls.ROOT+Urls.INSPECT_TASK,
                Object.class, this, TASK_QUERY_EVENT);
    }

    @Override
    public void dealFailureResult(VolleyError returnObject, int taskid) {
        super.dealFailureResult(returnObject, taskid);
        switch(taskid){
            case TASK_QUERY_POINT:
                hideProgressDialog();
                ToastUtil.showShort(this, "查询巡查计划失败");
                break;
            case TASK_QUERY_EVENT:
                hideProgressDialog();
                ToastUtil.showShort(this, "查询事件列表失败");
                break;
            default:
                break;
        }
    }

    @Override
    public void dealSuccessResult(Object returnObject, int taskid) {
        super.dealSuccessResult(returnObject, taskid);
        switch(taskid){
            case TASK_QUERY_POINT:
                if (returnObject!=null){
                    JSONObject jsonObject = (JSONObject)returnObject;
                    String flag = jsonObject.getString("flag");
                    if (flag.equals("0000")){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObject1.getJSONArray("list");
                        for (int i=0; i<jsonArray.size(); i++){
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            RoutePoint routePoint = new RoutePoint();
                            routePoint.setPlanPk(jsonObject2.getString("PlanPk"));
                            routePoint.setId(jsonObject2.getString("id"));
                            routePoint.setLat(jsonObject2.getString("Lat"));
                            routePoint.setLon(jsonObject2.getString("Lon"));
                            routePoint.setOrderId(jsonObject2.getString("Orderid"));
                            routePoint.setRouteCode(jsonObject2.getString("RouteCode"));
                            routePoints.add(routePoint);
                        }
                        drawLineOnMap();
                        getEventList();
                    }else if (flag.equals("0001")){
                        hideProgressDialog();
                        ToastUtil.showShort(this, "没有查询到巡查点");
                    }else {
                        hideProgressDialog();
                        ToastUtil.showShort(this, "查询巡查点失败");
                    }
                }
                break;
            case TASK_QUERY_EVENT:
                if (returnObject!=null){
                    JSONObject jsonObject = (JSONObject)returnObject;
                    String flag = jsonObject.getString("flag");
                    if (flag.equals("0000")){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObject1.getJSONArray("list");
                        for (int i=0; i<jsonArray.size(); i++){
                            JSONObject json = jsonArray.getJSONObject(i);
                            Event event= new Event();
                            event.setBillDate(json.getString("BillDate"));
                            event.setBillNo(json.getString("BillNo"));
                            event.setBillPk(json.getString("BillPk"));
                            event.setCarNo(json.getString("CarNo"));
                            event.setDirection(json.getString("Direction"));
                            event.setFacilityPk(json.getString("FacilityPk"));
                            event.setGrade(json.getString("Grade"));
                            event.setIsProcess(json.getString("IsProcess"));
                            event.setLat(json.getString("Lat"));
                            event.setLon(json.getString("Lon"));
                            event.setMemo(json.getString("Memo"));
                            event.setNumber(json.getString("Number"));
                            event.setPileNo(json.getString("PileNo"));
                            event.setProcessDept(json.getString("ProcessDept"));
                            event.setProcessType(json.getString("ProcessType"));
                            event.setRequEndTime(json.getString("RequEndTime"));
                            event.setRouteNo(json.getString("RouteNo"));
                            event.setState(json.getString("State"));
                            event.setType(json.getString("Type"));
                            event.setType1(json.getString("Type1"));
                            event.setType2(json.getString("Type2"));
                            event.setType3(json.getString("Type3"));
                            event.setUnit(json.getString("unit"));
                            event.setUserName(json.getString("userName"));
                            event.setUserPhone(json.getString("userPhone"));
                            events.add(event);
                        }
                        drawEventMarkOnMap();
                    }else if (flag.equals("0001")){
                        hideProgressDialog();
                        ToastUtil.showShort(this, "没有查询到事件");
                    }else {
                        hideProgressDialog();
                        ToastUtil.showShort(this, "查询事件失败");
                    }
                }
                break;
            default:
                break;
        }
    }

    private void drawEventMarkOnMap() {
        for (int i=0; i<events.size(); i++){
            Event event = events.get(i);
            double lat = Double.parseDouble(event.getLat());
            double lon = Double.parseDouble(event.getLon());
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.fd_blue_point);
            OverlayOptions overlayOptions = new MarkerOptions().icon(bitmapDescriptor)
                    .position(new LatLng(lat, lon));
            baiduMap.addOverlay(overlayOptions);
        }
    }

    private void drawLineOnMap() {
        // 构造折线点坐标
        List<LatLng> points = new ArrayList<LatLng>();
        for (int i=0; i<routePoints.size(); i++){
            double lat = Double.parseDouble(routePoints.get(i).getLat());
            double lon = Double.parseDouble(routePoints.get(i).getLon());
            points.add(new LatLng(lat, lon));
        }
        //构建分段颜色索引数组
        List<Integer> colors = new ArrayList<>();
        for (int i=0; i<points.size(); i++){
            colors.add(Integer.valueOf(Color.RED));
        }
        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .colorsValues(colors).points(points);
        //添加在地图中
        Polyline mPolyline = (Polyline) baiduMap.addOverlay(ooPolyline);
    }

    @Override
    public void onReceive(double longitude, double latitude) {

    }
}
