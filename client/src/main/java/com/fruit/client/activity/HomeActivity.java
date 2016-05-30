package com.fruit.client.activity;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSONArray;
import com.fruit.client.R;
import com.fruit.client.fragment.EventFragment;
import com.fruit.client.fragment.HomeFragment;
import com.fruit.client.fragment.InspectFragment;
import com.fruit.client.fragment.MonitorFragment;
import com.fruit.client.fragment.PersonalFragment;
import com.fruit.client.object.query.Param;
import com.fruit.client.object.query.QueryParamsResponse;
import com.fruit.client.service.UpdateService;
import com.fruit.client.util.Constant;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fruit.client.util.Urls;
import com.fruit.common.file.FileUtil;
import com.fruit.common.network.NetWorkUtil;
import com.fruit.common.ui.ToastUtil;
import com.fruit.core.activity.FruitActivity;
import com.fruit.core.db.DBUtil;
import com.fruit.core.http.VolleyManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2016/4/23.
 */
public class HomeActivity extends FruitActivity implements TabHost.OnTabChangeListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String queryUrl = "AJAXReturnData/GetParameter.ashx";
    private static final int TASK_ROUTE = 0;
    private static final int TASK_FACILITY_NAME = 1;
    private static final int TASK_LOCATION = 2;
    private static final int TASK_SUPPORT_METHOD = 3;
    private static final int TASK_OTHER_FACILITY = 4;
    private static final int TASK_FENCE = 5;
    private static final int TASK_FENCE_TYPE = 6;
    private static final int TASK_UNIT = 7;
    private static final int TASK_DEAL_STATUS = 8;
    private static final int TASK_DEAL_TYPE = 9;
    private static final int TASK_CAR_NO = 10;
    private static final int TASK_HIGHWAY_PROJECT = 11;
    private static final int TASK_ROAD_NET_PROJECT = 12;
    private static final int TASK_CONSERVATION_PROJECT = 13;
    private static final int TASK_CONSERVATION_UNIT = 14;
    private static final int TASK_VERSION_UPDATE = 15;

    private String[] params = new String[]{"路线","标志","位置","支持方式", "其它", "护栏", "护栏类型",
            "计量单位", "处理情况", "处理类别", "车牌号", "路政项目", "路网项目", "养护项目", "养护单位"};
    private Map<String, ArrayList<Param>> mParams = new HashMap<>();
    private String curTag;
    private int num = 0;
    private String label = "loaddata";

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private ImageView mNavigationIcon;
    private ImageView mAddEvent;
    private TextView mTitle;
    private FragmentTabHost mFragmentTabHost;
    private FrameLayout mRealContent;

    private Class[] fragments = new Class[]{HomeFragment.class, EventFragment.class, InspectFragment.class,
    MonitorFragment.class, PersonalFragment.class};
    private int[] icons = new int[]{R.drawable.tab_home_btn, R.drawable.tab_event_btn, R.drawable.tab_inspect_btn,
    R.drawable.tab_monitor_btn, R.drawable.tab_personal_btn};
    private String[] labels = new String[]{"首页", "事件", "巡查", "监控", "个人"};
    private ArrayList<TextView> mTextViews = new ArrayList<>();
    private int mTabIndex = 0;
    private boolean isDrawerOpen = false;
    private String roleName;
    private boolean showAddEventBtn = true;
    private String hasInitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        roleName = DBUtil.getConfigValue("role_name");
        hasInitial = DBUtil.getConfigValue("initial");
        if (roleName.equals(Constant.RoleName.CONSTRUCTION)||roleName.equals(Constant.RoleName.LUZHENG_CONFIRM)){
            showAddEventBtn = false;
        }
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView)findViewById(R.id.navigation_view);
        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.content_container);
        mToolbar = (Toolbar)findViewById(R.id.tool_bar);
        mNavigationIcon = (ImageView)findViewById(R.id.icon_menu);
        mTitle = (TextView)findViewById(R.id.title);
        mFragmentTabHost = (FragmentTabHost)findViewById(R.id.tab_host);
        mRealContent = (FrameLayout)findViewById(R.id.realcontent);
        mAddEvent = (ImageView)findViewById(R.id.icon_menu_right);

        mNavigationIcon.setOnClickListener(this);
        mAddEvent.setOnClickListener(this);

        mToolbar.setTitle("");
        if (mTabIndex==0){
            mTitle.setText("智能巡查");
        }
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                isDrawerOpen = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                isDrawerOpen = false;
            }
        };
        mDrawerLayout.setDrawerListener(mToggle);
        mNavigationView.setNavigationItemSelectedListener(this);

        mFragmentTabHost.setup(this, getSupportFragmentManager(), R.id.realcontent);
        for (int i=0; i<labels.length; i++){
            TabHost.TabSpec mTabSpec = mFragmentTabHost.newTabSpec(labels[i]);
            mTabSpec.setIndicator(getTabItemView(i));
            mFragmentTabHost.addTab(mTabSpec, fragments[i], null);
            mFragmentTabHost.getTabWidget().setDividerDrawable(null);
        }

        mFragmentTabHost.setOnTabChangedListener(this);
        mFragmentTabHost.setCurrentTab(0);

        if (showAddEventBtn){
            mAddEvent.setVisibility(View.VISIBLE);
        }else {
            mAddEvent.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFragmentTabHost = null;
    }

    private View getTabItemView(int index){
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(icons[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(labels[index]);
        if (index==0){
            textView.setTextColor(getResources().getColor(R.color.color_tab_active));
        }
        mTextViews.add(textView);

        return view;
    }

    @Override
    public void onFruitActivityClick(View view) {
        super.onFruitActivityClick(view);
        switch (view.getId()){
            case R.id.icon_menu:
                if (isDrawerOpen){
                    isDrawerOpen = false;
                    mDrawerLayout.closeDrawer(mNavigationView);
                }else {
                    isDrawerOpen = true;
                    mDrawerLayout.openDrawer(mNavigationView);
                }
                break;
            case R.id.icon_menu_right:
                if (DBUtil.getConfigValue("initial").equals("1")){
                    Intent intent = new Intent(HomeActivity.this, LocationActivity.class);
                    intent.putExtra("type", Constant.AddType.TYPE_EVENT);
                    startActivity(intent);
                }else {
                    ToastUtil.showShort(this, "请先初始化数据");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        switch (tabId){
            case "首页":
                mTitle.setText("智能巡查");
                if (showAddEventBtn){
                    mAddEvent.setVisibility(View.VISIBLE);
                }
                for (int i=0; i<mTextViews.size(); i++){
                    if (i!=0){
                        mTextViews.get(i).setTextColor(getResources().getColor(R.color.color_tab_normal));
                    }else {
                        mTextViews.get(i).setTextColor(getResources().getColor(R.color.color_tab_active));
                    }
                }
                break;
            case "事件":
                mTitle.setText("我的事件");
                if (showAddEventBtn){
                    mAddEvent.setVisibility(View.VISIBLE);
                }

                for (int i=0; i<mTextViews.size(); i++){
                    if (i!=1){
                        mTextViews.get(i).setTextColor(getResources().getColor(R.color.color_tab_normal));
                    }else {
                        mTextViews.get(i).setTextColor(getResources().getColor(R.color.color_tab_active));
                    }
                }
                break;
            case "巡查":
                mTitle.setText("巡查");
                if (showAddEventBtn){
                    mAddEvent.setVisibility(View.VISIBLE);
                }

                for (int i=0; i<mTextViews.size(); i++){
                    if (i!=2){
                        mTextViews.get(i).setTextColor(getResources().getColor(R.color.color_tab_normal));
                    }else {
                        mTextViews.get(i).setTextColor(getResources().getColor(R.color.color_tab_active));
                    }
                }
                break;
            case "监控":
                mTitle.setText("监控");
                if (showAddEventBtn){
                    mAddEvent.setVisibility(View.VISIBLE);
                }

                for (int i=0; i<mTextViews.size(); i++){
                    if (i!=3){
                        mTextViews.get(i).setTextColor(getResources().getColor(R.color.color_tab_normal));
                    }else {
                        mTextViews.get(i).setTextColor(getResources().getColor(R.color.color_tab_active));
                    }
                }
                break;
            case "个人":
                mTitle.setText("个人中心");
                mAddEvent.setVisibility(View.GONE);
                for (int i=0; i<mTextViews.size(); i++){
                    if (i!=4){
                        mTextViews.get(i).setTextColor(getResources().getColor(R.color.color_tab_normal));
                    }else {
                        mTextViews.get(i).setTextColor(getResources().getColor(R.color.color_tab_active));
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_pile:
                if (DBUtil.getConfigValue("initial").equals("1")){
                    Intent intent = new Intent(HomeActivity.this, LocationActivity.class);
                    intent.putExtra("type", Constant.AddType.TYPE_PILE);
                    startActivity(intent);
                    closeDrawerLater();
                }else {
                    ToastUtil.showShort(this, "请先初始化数据");
                }
                break;
            case R.id.add_mark:
                if (DBUtil.getConfigValue("initial").equals("1")){
                    Intent intent1 = new Intent(HomeActivity.this, LocationActivity.class);
                    intent1.putExtra("type", Constant.AddType.TYPE_MARK);
                    startActivity(intent1);
                    closeDrawerLater();
                }else {
                    ToastUtil.showShort(this, "请先初始化数据");
                }
                break;
            case R.id.add_fence:
                if (DBUtil.getConfigValue("initial").equals("1")){
                    Intent intent2 = new Intent(HomeActivity.this, LocationActivity.class);
                    intent2.putExtra("type", Constant.AddType.TYPE_FENCE);
                    startActivity(intent2);
                    closeDrawerLater();
                }else {
                    ToastUtil.showShort(this, "请先初始化数据");
                }
                break;
            case R.id.add_other:
                if (DBUtil.getConfigValue("initial").equals("1")){
                    Intent intent3 = new Intent(HomeActivity.this, LocationActivity.class);
                    intent3.putExtra("type", Constant.AddType.TYPE_OTHER);
                    startActivity(intent3);
                    closeDrawerLater();
                }else {
                    ToastUtil.showShort(this, "请先初始化数据");
                }
                break;
            case R.id.initial_system:
                //每加载一次数据，tag的数字标识加一
                showDialog("初始化数据...");
                curTag = label+(++num);
                loadData(curTag);
                break;
            case R.id.clear_cache:
                clearCache();
                break;
            case R.id.check_update:
                showDialog("正在检查版本更新...");
                checkVersion();
                break;
            default:
                break;
        }
        return true;
    }

    private String getVersionName(Context context){
        PackageManager packageManager=context.getPackageManager();
        PackageInfo packageInfo;
        String versionName="";
        try {
            packageInfo=packageManager.getPackageInfo(context.getPackageName(),0);
            versionName=packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    private void checkVersion(){
        Map<String, String> params = new HashMap<>();
        params.put("type", "VersionUpdate");
        params.put("version", getVersionName(HomeActivity.this));
        String s = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", s, params, Urls.ROOT+Urls.PERSONAL_CENTER,
                Object.class, this, TASK_VERSION_UPDATE);
    }

    private void clearCache() {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator;
        File file = new File(filePath, "params.txt");
        if (file.exists()){
            file.delete();
            ToastUtil.showShort(this, "成功清除缓存");
        }else {
            ToastUtil.showShort(this, "没有缓存");
        }
        DBUtil.setConfigValue("initial", "0");
    }

    private void loadData(String tag){
        queryParam(tag, params[0], QueryParamsResponse.class, TASK_ROUTE);
        queryParam(tag, params[1], QueryParamsResponse.class, TASK_FACILITY_NAME);
        queryParam(tag, params[2], QueryParamsResponse.class, TASK_LOCATION);
        queryParam(tag, params[3], QueryParamsResponse.class, TASK_SUPPORT_METHOD);
        queryParam(tag, params[4], QueryParamsResponse.class, TASK_OTHER_FACILITY);
        queryParam(tag, params[5], QueryParamsResponse.class, TASK_FENCE);
        queryParam(tag, params[6], QueryParamsResponse.class, TASK_FENCE_TYPE);
        queryParam(tag, params[7], QueryParamsResponse.class, TASK_UNIT);
        queryParam(tag, params[8], QueryParamsResponse.class, TASK_DEAL_STATUS);
        queryParam(tag, params[9], QueryParamsResponse.class, TASK_DEAL_TYPE);
        queryParam(tag, params[10], QueryParamsResponse.class, TASK_CAR_NO);
        queryParam(tag, params[11], QueryParamsResponse.class, TASK_HIGHWAY_PROJECT);
        queryParam(tag, params[12], QueryParamsResponse.class, TASK_ROAD_NET_PROJECT);
        queryParam(tag, params[13], QueryParamsResponse.class, TASK_CONSERVATION_PROJECT);
        queryParam(tag, params[14], QueryParamsResponse.class, TASK_CONSERVATION_UNIT);
    }

    /**
     * 查询参数
     * @param type 参数类型
     * @param clz 返回json数据的实体类
     * @param taskid 任务编号
     */
    private void queryParam(String tag, String type, Class clz, int taskid) {
        String mStr = Constant.url + queryUrl + "?type="+ type;
        VolleyManager.newInstance(this).JsonGetRequest(tag, mStr, clz, this, taskid);
    }

    @Override
    public void dealFailureResult(VolleyError returnObject, int taskid) {
        super.dealFailureResult(returnObject, taskid);
        hideProgressDialog();
        switch (taskid){
            case TASK_VERSION_UPDATE:
                hideProgressDialog();
                ToastUtil.showShort(this, "检查版本更新失败");
                break;
            default:
                mParams.clear();
                VolleyManager.newInstance(HomeActivity.this).cancel(curTag);
                ToastUtil.showShort(this, "初始化数据失败,网络错误");
                break;
        }
    }

    @Override
    public void dealSuccessResult(Object returnObject, int taskid) {
        super.dealSuccessResult(returnObject, taskid);
        if (taskid==TASK_VERSION_UPDATE){
            JSONObject jsonObject = (JSONObject)returnObject;
            String flag = jsonObject.getString("flag");
            hideProgressDialog();
            if (flag.equals("0000")){
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                JSONArray jsonArray = jsonObject1.getJSONArray("result");
                JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                String url = jsonObject2.getString("Url");
                Intent intent = new Intent(HomeActivity.this, UpdateService.class);
                intent.putExtra("download_url", url);
                startService(intent);
            }else if (flag.equals("0001")){
                ToastUtil.showShort(this, "已经是最新版本");
            }else {
                ToastUtil.showShort(this, "检查版本更新失败");
            }
        }else {
            QueryParamsResponse qpr = (QueryParamsResponse)returnObject;
            if (qpr.getFlag().equals(Constant.QueryRouteStatus.STATUS_SUCCESS)){
                switch (taskid){
                    case TASK_ROUTE:
                        dealResponseData((QueryParamsResponse)returnObject, params[0]);
                        break;
                    case TASK_FACILITY_NAME:
                        dealResponseData((QueryParamsResponse)returnObject, params[1]);
                        break;
                    case TASK_LOCATION:
                        dealResponseData((QueryParamsResponse)returnObject, params[2]);
                        break;
                    case TASK_SUPPORT_METHOD:
                        dealResponseData((QueryParamsResponse)returnObject, params[3]);
                        break;
                    case TASK_OTHER_FACILITY:
                        dealResponseData((QueryParamsResponse)returnObject, params[4]);
                        break;
                    case TASK_FENCE:
                        dealResponseData((QueryParamsResponse)returnObject, params[5]);
                        break;
                    case TASK_FENCE_TYPE:
                        dealResponseData((QueryParamsResponse)returnObject, params[6]);
                        break;
                    case TASK_UNIT:
                        dealResponseData((QueryParamsResponse)returnObject, params[7]);
                        break;
                    case TASK_DEAL_STATUS:
                        dealResponseData((QueryParamsResponse)returnObject, params[8]);
                        break;
                    case TASK_DEAL_TYPE:
                        dealResponseData((QueryParamsResponse)returnObject, params[9]);
                        break;
                    case TASK_CAR_NO:
                        dealResponseData((QueryParamsResponse)returnObject, params[10]);
                        break;
                    case TASK_HIGHWAY_PROJECT:
                        dealResponseData((QueryParamsResponse)returnObject, params[11]);
                        break;
                    case TASK_ROAD_NET_PROJECT:
                        dealResponseData((QueryParamsResponse)returnObject, params[12]);
                        break;
                    case TASK_CONSERVATION_PROJECT:
                        dealResponseData((QueryParamsResponse)returnObject, params[13]);
                        break;
                    case TASK_CONSERVATION_UNIT:
                        dealResponseData((QueryParamsResponse)returnObject, params[14]);
                        break;
                    default:
                        break;
                }
            }else {
                //如果有任务失败，则取消当前所有未完成的任务,参数集合clear
                mParams.clear();
                VolleyManager.newInstance(HomeActivity.this).cancel(curTag);
                ToastUtil.showShort(this, "初始化数据失败，点击重试");
            }
        }
    }

    private void  closeDrawerLater(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.closeDrawer(mNavigationView);
            }
        }, 500);
    }

    private void dealResponseData(QueryParamsResponse data, String paramName)
    {
        if (data.getFlag().equals(Constant.QueryRouteStatus.STATUS_SUCCESS))
        {
            ArrayList<Param> mList = data.getData().getList();
            mParams.put(paramName, mList);
            if (mParams.size()==params.length){
                String jsonString = JSONObject.toJSONString(mParams);
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator;
                File mFile = new File(filePath, "params.txt");
                if (!mFile.exists()){
                    FileUtil.writeStringToFile(jsonString, filePath, "params.txt");
                }else {
                    mFile.delete();
                    FileUtil.writeStringToFile(jsonString, filePath, "params.txt");
                }
                hideProgressDialog();
                ToastUtil.showShort(this, "初始化数据成功");
                DBUtil.setConfigValue("initial", "1");
            }
        }
    }
}
