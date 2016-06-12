package com.fruit.client.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import com.fruit.client.R;
import com.fruit.client.activity.InspectNavigationActivity;
import com.fruit.client.activity.PermissionsActivity;
import com.fruit.client.adapter.InspectAdapter;
import com.fruit.client.adapter.RoutePlanAdapter;
import com.fruit.client.object.RoutePlan;
import com.fruit.client.object.RoutePoint;
import com.fruit.client.object.event.Event;
import com.fruit.client.util.Constant;
import com.fruit.client.util.PermissionsChecker;
import com.fruit.client.util.Urls;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.fruit.common.network.NetWorkUtil;
import com.fruit.common.ui.ToastUtil;
import com.fruit.core.db.DBUtil;
import com.fruit.core.fragment.FruitFragment;
import com.fruit.core.http.VolleyManager;
import com.fruit.widget.MultiStateView;

import java.io.File;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/4/23.
 */
public class InspectFragment extends FruitFragment {
    public static final String ROUTE_PLAN_NODE = "route_plan_node";
    private static final String APP_FOLDER_NAME = "Client";
    private static final int TASK_QUERY_LIST = 0;
    private static final int TASK_QUERY_POINT = 1;
    private static final int TASK_QUERY_EVENT = 2;
    private static final int TASK_TASK_FINSIH = 3;
    private static final int REQUEST_NAVIGATE = 0;
    private static final int REQUEST_CODE = 1;

    public static List<Activity> activityList = new ArrayList<>();

    private MultiStateView multiStateView;
    private SwipeRefreshLayout mRefreshLayout;
    private ListView planList;
    private Button retry;

//    private Button mStartInspect;
//    private TextView taskName, taskDate, taskRoute;
////    private TextView timeText;
////    private ImageView chooseTimeImage;
//    private Spinner routeSpinner;
    private String mSDCardPath;
    private String authinfo;

    private ArrayList<RoutePlan> routePlen = new ArrayList<>();
    private InspectAdapter adapter;

    private double startLatitude, startLongitude, endLatitude, endLongitude;

    private ArrayList<BNRoutePlanNode> bnRoutePlanNodes = new ArrayList<>();
    private ArrayList<Event> events = new ArrayList<>();
    private int taskIndex = 0;
    private boolean isTimeSelected = false;
    private GregorianCalendar calendar;

    private PermissionsChecker checker;
    private String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public InspectFragment(){}

    public static FruitFragment getInstance(){
        InspectFragment mInspectFragment = new InspectFragment();
        Bundle mBundle = new Bundle();
        mInspectFragment.setArguments(mBundle);
        return mInspectFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = getArguments();
//        RoutePlan routePlan = new RoutePlan();
//        routePlan.setTaskNo("空");
//        routePlen.add(routePlan);
        adapter = new InspectAdapter(routePlen, getActivity());
        calendar = new GregorianCalendar();

        checker = new PermissionsChecker(getActivity());
//        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
//            if (checker.lacksPermissions(PERMISSIONS)) {
//                startPermissionsActivity();
//            }else {
//                ToastUtil.showShort(getActivity(), "所需权限已全部获取");
//            }
//        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event, null, false);
        multiStateView = (MultiStateView)v.findViewById(R.id.multistateview);
        multiStateView.setViewForState(R.layout.layout_content_list, MultiStateView.ViewState.CONTENT);
        multiStateView.setViewForState(R.layout.layout_loading, MultiStateView.ViewState.LOADING);
        multiStateView.setViewForState(R.layout.layout_error, MultiStateView.ViewState.ERROR);
        multiStateView.setViewForState(R.layout.layout_empty, MultiStateView.ViewState.EMPTY);

        mRefreshLayout = (SwipeRefreshLayout)multiStateView.getView(MultiStateView.ViewState.CONTENT).findViewById(R.id.refresh_container);
        planList = (ListView)multiStateView.getView(MultiStateView.ViewState.CONTENT).findViewById(R.id.list_view);
        retry = (Button)multiStateView.getView(MultiStateView.ViewState.ERROR).findViewById(R.id.retry);

        planList.setAdapter(adapter);
        if (initDirs()){
            initNavi();
        }
        planList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog("正在获取巡查信息...", getActivity());
                getPlanDetail(routePlen.get(position).getPlanPk());
                taskIndex = position;
            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTaskList();
            }
        });
        return v;
    }

    @Override
    public void onFruitClick(int id) {
        switch (id){
//            case R.id.start_inspect:
//                int index = routeSpinner.getSelectedItemPosition();
//                if (index>0){
//                    showDialog("正在获取巡查信息...", getActivity());
//                    getPlanDetail(routePlen.get(index).getPlanPk());
//                    taskIndex = index;
//                }else {
//                    ToastUtil.showShort(getActivity(), "选择一条巡查路线");
//                }
//                break;
//            case R.id.choose_time:
//                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        timeText.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
//                        isTimeSelected = true;
//                    }
//                }, calendar.get(GregorianCalendar.YEAR), calendar.get(GregorianCalendar.MONTH),
//                        calendar.get(GregorianCalendar.DAY_OF_MONTH)).show();
//                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getTaskList();
    }

    @Override
    public void dealSuccessResult(Object data, int taskid) {
        super.dealSuccessResult(data, taskid);
        switch (taskid){
            case TASK_QUERY_LIST:
                if (data!=null){
                    JSONObject jsonObject = (JSONObject)data;
                    String flag = jsonObject.getString("flag");
                    String msg = jsonObject.getString("msg");
                    if (flag.equals("0000")){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObject1.getJSONArray("list");
                        routePlen.clear();
//                        RoutePlan routePlan0 = new RoutePlan();
//                        routePlan0.setPlanName("空");
//                        routePlen.add(routePlan0);
                        for (int i=0; i<jsonArray.size(); i++){
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            RoutePlan routePlan = new RoutePlan();
                            routePlan.setDateInfo(jsonObject2.getString("dateinfo"));
                            routePlan.setDeptName(jsonObject2.getString("deptName"));
                            routePlan.setDeptPk(jsonObject2.getString("deptPk"));
                            routePlan.setEndTime(jsonObject2.getString("endTime"));
                            routePlan.setUserNo(jsonObject2.getString("userNo"));
                            routePlan.setUserName(jsonObject2.getString("userName"));
                            routePlan.setState(jsonObject2.getString("state"));
                            routePlan.setPlanPk(jsonObject2.getString("PlanPk"));
                            routePlan.setTaskNo(jsonObject2.getString("TaskNo"));
                            routePlan.setPlanName(jsonObject2.getString("PlanName"));
                            routePlan.setPlanDescr(jsonObject2.getString("PlanDescr"));
                            routePlen.add(routePlan);
                        }
                        adapter.notifyDataSetChanged();
                    }else if (flag.equals("0001")){
                        routePlen.clear();
                        multiStateView.setViewState(MultiStateView.ViewState.EMPTY);
                    }else {
                        routePlen.clear();
                        multiStateView.setViewState(MultiStateView.ViewState.ERROR);
                    }
                }
                break;
            case TASK_QUERY_POINT:
                if (data!=null){
                    JSONObject jsonObject = (JSONObject)data;
                    String flag = jsonObject.getString("flag");
                    if (flag.equals("0000")){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObject1.getJSONArray("list");
                        bnRoutePlanNodes.clear();
                        for (int i=0; i<jsonArray.size(); i++){
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            RoutePoint routePoint = new RoutePoint();
                            routePoint.setPlanPk(jsonObject2.getString("PlanPk"));
                            routePoint.setId(jsonObject2.getString("id"));
                            routePoint.setLat(jsonObject2.getString("Lat"));
                            routePoint.setLon(jsonObject2.getString("Lon"));
                            routePoint.setOrderId(jsonObject2.getString("Orderid"));
                            routePoint.setRouteCode(jsonObject2.getString("RouteCode"));
                            double lat = Double.parseDouble(jsonObject2.getString("Lat"));
                            double lon = Double.parseDouble(jsonObject2.getString("Lon"));
                            BNRoutePlanNode bnRoutePlanNode = new BNRoutePlanNode(lon, lat, "", null, BNRoutePlanNode.CoordinateType.BD09LL);
                            bnRoutePlanNodes.add(bnRoutePlanNode);
                        }
//                        int index = routeSpinner.getSelectedItemPosition();
                        getEventList(routePlen.get(taskIndex).getPlanPk());
                    }else if (flag.equals("0001")){
                        hideProgressDialog();
                        ToastUtil.showShort(getActivity(), "没有查询到巡查点");
                    }else {
                        hideProgressDialog();
                        ToastUtil.showShort(getActivity(), "查询巡查点失败");
                    }
                }
                break;
            case TASK_QUERY_EVENT:
                if (data!=null){
                    JSONObject jsonObject = (JSONObject)data;
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
//                        ToastUtil.showShort(getActivity(), "成功获取到事件列表");
                        if (BaiduNaviManager.isNaviInited()) {
                            routeplanToNavi(BNRoutePlanNode.CoordinateType.BD09LL);
                        }else {
                            ToastUtil.showShort(getActivity(), "初始化失败，无法跳转到导航界面");
                        }
                    }else if (flag.equals("0001")){
                        hideProgressDialog();
                        ToastUtil.showShort(getActivity(), "没有查询到事件");
                        if (BaiduNaviManager.isNaviInited()) {
                            routeplanToNavi(BNRoutePlanNode.CoordinateType.BD09LL);
                        }else {
                            ToastUtil.showShort(getActivity(), "初始化失败，无法跳转到导航界面");
                        }
                    }else {
                        hideProgressDialog();
                        ToastUtil.showShort(getActivity(), "查询事件失败");
                    }
                }
                break;
            case TASK_TASK_FINSIH:
                if (data!=null){
                    JSONObject jsonObject = (JSONObject)data;
                    String flag = jsonObject.getString("flag");
                    String msg = jsonObject.getString("msg");
                    if (flag.equals("0000")){
                        ToastUtil.showShort(getActivity(), "完成巡查任务提交成功");
                        getTaskList();
                    }else {
                        ToastUtil.showShort(getActivity(), "完成巡查任务提交失败");
                    }
                }
                break;
            default:
                break;
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(getActivity(), REQUEST_CODE, PERMISSIONS);
    }

    @Override
    public void dealFailResult(VolleyError returnString, int taskid) {
        super.dealFailResult(returnString, taskid);
        switch (taskid){
            case TASK_QUERY_LIST:
                ToastUtil.showShort(getActivity(), "查询失败");
                break;
            case TASK_QUERY_POINT:
                hideProgressDialog();
                ToastUtil.showShort(getActivity(), "查询巡查点失败");
                break;
            case TASK_QUERY_EVENT:
                hideProgressDialog();
                ToastUtil.showShort(getActivity(), "查询事件列表失败");
                break;
            case TASK_TASK_FINSIH:
                ToastUtil.showShort(getActivity(), "完成巡查任务提交失败");
                break;
            default:
                break;
        }
    }

    private void getEventList(String planpk){
        Map<String, String> params = new HashMap<>();
        params.put("type", "queryEvent");
        params.put("planpk", planpk);
        String s = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(getActivity()).JsonPostRequest(null, "utf-8", s, params, Urls.ROOT+Urls.INSPECT_TASK,
                Object.class, this, TASK_QUERY_EVENT);
    }

    private void getPlanDetail(String planpk){
        Map<String, String> params =new HashMap<>();
        params.put("type", "queryPoint");
        params.put("planpk", planpk);
        String s = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(getActivity()).JsonPostRequest(null, "utf-8", s, params, Urls.ROOT+Urls.INSPECT_TASK,
                Object.class, this, TASK_QUERY_POINT);
    }

    private void getTaskList(){
        Map<String, String> params = new HashMap<>();
        params.put("type", "query");
        String deptpk = DBUtil.getConfigValue("dept_pk");
        params.put("deptpk", deptpk);
//        if (isTimeSelected){
//            params.put("dateinfo", timeText.getText().toString().replace("-", ""));
//        }
        String s = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(getActivity()).JsonPostRequest(null, "utf-8", s, params, Urls.ROOT+Urls.INSPECT_TASK,
                Object.class, this, TASK_QUERY_LIST);
    }

    private void initNavi() {
        BaiduNaviManager.getInstance().init(getActivity(), mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
//                        Toast.makeText(getActivity(), authinfo, Toast.LENGTH_LONG).show();
                        }
                    });
                }
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
//                        Toast.makeText(getActivity(), authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            public void initSuccess() {
//                Toast.makeText(getActivity(), "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                initSetting();
            }

            public void initStart() {
//                Toast.makeText(getActivity(), "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
            }

            public void initFailed() {
//                Toast.makeText(getActivity(), "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }

        },  null, ttsHandler, null);

    }

    private void notifyTaskFinished(){
        Map<String, String> params = new HashMap<>();
        params.put("type", "end");
        params.put("taskno", routePlen.get(taskIndex).getTaskNo());
        params.put("userno", routePlen.get(taskIndex).getUserNo());
        String s = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(getActivity()).JsonPostRequest(null, "utf-8", s, params, Urls.ROOT+Urls.INSPECT_TASK,
                Object.class, this, TASK_TASK_FINSIH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Activity.RESULT_OK){
            switch (requestCode){
                case REQUEST_NAVIGATE:
                    notifyTaskFinished();
                    break;
                case REQUEST_CODE:
                    if (resultCode== PermissionsActivity.PERMISSIONS_DENIED){
                        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
                        new AlertDialog.Builder(getActivity())
                                .setTitle("注意")
                                .setMessage("缺少主要权限, 应用无法运行，即将退出")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getActivity().finish();
                                    }
                                })
                                .create()
                                .show();
                    }else if (resultCode==PermissionsActivity.PERMISSIONS_GRANTED){
                        if (BaiduNaviManager.isNaviInited()) {
                            routeplanToNavi(BNRoutePlanNode.CoordinateType.BD09LL);
                        }
                    }
                default:
                    break;
            }
        }else {

        }
    }

    private void initSetting(){
        BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_AUTO);
        BNaviSettingManager.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.AUTO_MODE);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private void routeplanToNavi(BNRoutePlanNode.CoordinateType coType) {
//        BNRoutePlanNode sNode = null;
//        BNRoutePlanNode eNode = null;
//        switch (coType) {
//            case BD09LL: {
//                sNode = new BNRoutePlanNode(120.5619990000, 31.8204040000, "", null, coType);
//                eNode = new BNRoutePlanNode(120.6681050000, 31.8549950000, "", null, coType);
//                break;
//            }
//            default:
//                break;
//        }
//        if (sNode != null && eNode != null) {
//            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
//            list.add(sNode);
//            list.add(eNode);
//            BaiduNaviManager.getInstance().launchNavigator(getActivity(), list, 1, true, new InspectRoutePlanListener(sNode));
//        }
//        sort(bnRoutePlanNodes);
        BaiduNaviManager.getInstance().launchNavigator(getActivity(), bnRoutePlanNodes, 1, true, new InspectRoutePlanListener(bnRoutePlanNodes.get(0)));
    }

    private void sort(ArrayList<BNRoutePlanNode> bnRoutePlanNodes) {

    }

    public class InspectRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public InspectRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            /**
             * 设置途径点以及resetEndNode会回调该接口
             */
            for (Activity ac : activityList) {
                if (ac.getClass().getName().endsWith("InspectNavigationActivity")) {
                    return;
                }
            }
            Intent intent = new Intent(getActivity(), InspectNavigationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            bundle.putParcelableArrayList("event_list", events);
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_NAVIGATE);
            hideProgressDialog();
        }

        @Override
        public void onRoutePlanFailed() {
            Toast.makeText(getActivity(), "算路失败", Toast.LENGTH_SHORT).show();
        }
    }

    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
//                    showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
//                    showToastMsg("Handler : TTS play end");
                    break;
                }
                default:
                    break;
            }
        }
    };

    public void showToastMsg(final String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    BNOuterTTSPlayerCallback ttsCallback = new BNOuterTTSPlayerCallback() {
        @Override
        public int getTTSState() {
            return 0;
        }

        @Override
        public int playTTSText(String s, int i) {
            return 0;
        }

        @Override
        public void phoneCalling() {

        }

        @Override
        public void phoneHangUp() {

        }

        @Override
        public void initTTSPlayer() {

        }

        @Override
        public void releaseTTSPlayer() {

        }

        @Override
        public void stopTTS() {

        }

        @Override
        public void resumeTTS() {

        }

        @Override
        public void pauseTTS() {

        }
    };
}
