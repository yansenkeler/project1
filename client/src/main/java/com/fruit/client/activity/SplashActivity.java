package com.fruit.client.activity;

import android.content.Intent;
import android.graphics.Color;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.fruit.client.R;
import com.fruit.client.object.query.Param;
import com.fruit.client.object.query.QueryParamsResponse;
import com.fruit.client.util.Constant;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fruit.common.file.FileUtil;
import com.fruit.common.ui.ToastUtil;
import com.fruit.core.activity.FruitActivity;
import com.fruit.core.http.VolleyManager;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2016/3/1.
 */
public class SplashActivity extends FruitActivity {
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

    private String[] params = new String[]{"路线","标志","位置","支持方式", "其它", "护栏", "护栏类型",
            "计量单位", "处理情况", "处理类别", "车牌号", "路政项目", "路网项目", "养护项目", "养护单位"};
    private ProgressWheel mProgressBar;
    private TextView mRetry;
    private Map<String, ArrayList<Param>> mParams = new HashMap<>();
    private String curTag;
    private int num = 0;
    private String label = "loaddata";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
        super.onCreate(savedInstanceState);
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, "nGRGZEweVdliIAQuKDIBTZS5");
        setContentView(R.layout.activity_splash);
        mProgressBar = (ProgressWheel)findViewById(R.id.progress);
        mRetry = (TextView)findViewById(R.id.retry);
        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curTag = label + (++num);
                loadData(curTag);
                mRetry.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });
        //每加载一次数据，tag的数字标识加一
        curTag = label+(++num);
//        loadData(curTag);
        mProgressBar.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
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

    @Override
    public void dealFailureResult(VolleyError returnObject, int taskid) {
        super.dealFailureResult(returnObject, taskid);
        mProgressBar.setVisibility(View.GONE);
        mRetry.setVisibility(View.VISIBLE);
        /**
         * 如果有任务失败，则取消当前所有未完成的任务,参数集合clear
         */
        mParams.clear();
        VolleyManager.newInstance(SplashActivity.this).cancel(curTag);
        ToastUtil.showShort(this, "初始化数据失败--网络错误");
    }

    @Override
    public void dealSuccessResult(Object returnObject, int taskid) {
        super.dealSuccessResult(returnObject, taskid);
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
            mProgressBar.setVisibility(View.GONE);
            mRetry.setVisibility(View.VISIBLE);
            //如果有任务失败，则取消当前所有未完成的任务,参数集合clear
            mParams.clear();
            VolleyManager.newInstance(SplashActivity.this).cancel(curTag);
            ToastUtil.showShort(this, "初始化数据失败--查询失败");
        }

    }

    /**
     * 查询参数
     * @param type 参数类型
     * @param clz 返回json数据的实体类
     * @param taskid 任务编号
     */
    private void queryParam(String tag, String type, Class clz, int taskid)
    {
        String mStr = Constant.url + queryUrl + "?type="+ type;
        VolleyManager.newInstance(this).JsonGetRequest(tag, mStr, clz, this, taskid);
    }

    private void dealResponseData(QueryParamsResponse data, String paramName)
    {
        if (data.getFlag().equals(Constant.QueryRouteStatus.STATUS_SUCCESS))
        {
            ArrayList<Param> mList = data.getData().getList();
            mParams.put(paramName, mList);
            if (mParams.size()==params.length){
                String jsonString = JSONObject.toJSONString(mParams);
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator;
                File mFile = new File(filePath, "params.txt");
                if (!mFile.exists()){
                    FileUtil.writeStringToFile(jsonString, filePath, "params.txt");
                }else {
                    mFile.delete();
                    FileUtil.writeStringToFile(jsonString, filePath, "params.txt");
                }
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
