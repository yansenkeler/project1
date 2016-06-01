package com.fruit.client.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.android.volley.VolleyError;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNRouteGuideManager;
import com.baidu.navisdk.adapter.BNRouteGuideManager.CustomizedLayerItem;
import com.baidu.navisdk.adapter.BNRouteGuideManager.OnNavigationListener;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviBaseCallbackModel;
import com.baidu.navisdk.adapter.BaiduNaviCommonModule;
import com.baidu.navisdk.adapter.NaviModuleFactory;
import com.baidu.navisdk.adapter.NaviModuleImpl;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.fruit.client.util.Constant;
import com.fruit.client.util.Urls;
import com.fruit.common.network.NetWorkUtil;
import com.fruit.common.ui.ToastUtil;
import com.fruit.client.MyApplication;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import com.fruit.client.R;
import com.fruit.client.fragment.InspectFragment;
import com.fruit.client.object.event.Event;
import com.fruit.client.util.Locationor;
import com.fruit.client.util.LonLatUtil;
import com.fruit.core.api.VolleyResponse;
import com.fruit.core.db.DBUtil;
import com.fruit.core.http.VolleyManager;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

/**
 * 诱导界面
 *
 * @author sunhao04
 *
 */
public class InspectNavigationActivity extends Activity implements MyApplication.OnReceiveData, VolleyResponse<Object> {

    private static final int TASK_GPS_INFO = 0;
    private final String TAG = InspectNavigationActivity.class.getName();
    private BNRoutePlanNode mBNRoutePlanNode = null;
    private BaiduNaviCommonModule mBaiduNaviCommonModule = null;
    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<LatLng> latLngs = new ArrayList<>();
    private MyApplication myApplication;

    private Timer mTimer;
    private LocationTimerTask mTask;

    private MediaPlayer mediaPlayer = null;

    private boolean isLastPlayVoice = false;

    @Override
    public void onResponse(Object response, int taskid) {

    }

    @Override
    public void onErrorResponse(VolleyError error, int taskid) {

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
            myApplication.setReceiveDataListener(InspectNavigationActivity.this);
            Locationor.getInstance(getApplication()).startLocation();
            Log.d(TAG, "start locate automatically after 2s");
        }
    };

    /*
     * 对于导航模块有两种方式来实现发起导航。 1：使用通用接口来实现 2：使用传统接口来实现
     */
    // 是否使用通用接口
    private boolean useCommonInterface = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InspectFragment.activityList.add(this);
        createHandler();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        }
        View view = null;

        if (useCommonInterface) {
            //使用通用接口
            mBaiduNaviCommonModule = NaviModuleFactory.getNaviModuleManager().getNaviCommonModule(
                    NaviModuleImpl.BNaviCommonModuleConstants.ROUTE_GUIDE_MODULE, this,
                    BNaviBaseCallbackModel.BNaviBaseCallbackConstants.CALLBACK_ROUTEGUIDE_TYPE, mOnNavigationListener);
            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onCreate();
                view = mBaiduNaviCommonModule.getView();
            }

        } else {
            //使用传统接口
            view = BNRouteGuideManager.getInstance().onCreate(this,mOnNavigationListener);
        }

//        new AlertDialog.Builder(this)
//                .setTitle("是否开始导航？")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        InspectNavigationActivity.this.finish();
//                    }
//                })
//                .create()
//                .show();
        if (view != null) {
            setContentView(view);
        }

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mBNRoutePlanNode = (BNRoutePlanNode) bundle.getSerializable(InspectFragment.ROUTE_PLAN_NODE);
                ArrayList<Event> events1 = bundle.getParcelableArrayList("event_list");
                events.addAll(events1);
                for (int i=0; i<events.size(); i++){
                    Event event = events.get(i);
                    double lat = Double.parseDouble(event.getLat());
                    double lon = Double.parseDouble(event.getLon());
                    LatLng latLng = new LatLng(lat, lon);
                    latLngs.add(latLng);
                }
            }
        }
        mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor fileDescriptor = getResources().getAssets().openFd("voice.mp3");
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Application application = getApplication();
        if (application instanceof MyApplication){
            myApplication = (MyApplication)application;
        }

        if (myApplication!=null){
            mTimer = new Timer();
            mTask = new LocationTimerTask();
            mTimer.schedule(mTask, 0, 3000);
        }else {
            ToastUtil.showShort(this, "定位初始化失败，无法为您播报事件点");
        }

        SpeechSynthesizer speechSynthesizer = SpeechSynthesizer.getInstance();
        speechSynthesizer.speak("现在开始为您导航");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(useCommonInterface) {
            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onResume();
            }
        } else {
            BNRouteGuideManager.getInstance().onResume();
        }
        if (hd != null) {
            hd.sendEmptyMessageAtTime(MSG_SHOW, 2000);
        }

//        addCustomizedLayerItems();
    }

    protected void onPause() {
        super.onPause();

        if(useCommonInterface) {
            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onPause();
            }
        } else {
            BNRouteGuideManager.getInstance().onPause();
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(useCommonInterface) {
            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onDestroy();
            }
        } else {
            BNRouteGuideManager.getInstance().onDestroy();
        }
        InspectFragment.activityList.remove(this);
        mTimer.cancel();
        mTask.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(useCommonInterface) {
            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onStop();
            }
        } else {
            BNRouteGuideManager.getInstance().onStop();
        }

    }

    @Override
    public void onBackPressed() {
        if(useCommonInterface) {
            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onBackPressed(false);
            }
        } else {
            BNRouteGuideManager.getInstance().onBackPressed(false);
        }
    }

    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(useCommonInterface) {
            if(mBaiduNaviCommonModule != null) {
                mBaiduNaviCommonModule.onConfigurationChanged(newConfig);
            }
        } else {
            BNRouteGuideManager.getInstance().onConfigurationChanged(newConfig);
        }

    };

    private void addCustomizedLayerItems() {
        List<CustomizedLayerItem> items = new ArrayList<CustomizedLayerItem>();
        CustomizedLayerItem item1 = null;
        for (int i=0; i<events.size(); i++){
            Event event = events.get(i);
            double lat = Double.parseDouble(event.getLat());
            double lon = Double.parseDouble(event.getLon());
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
//                LatLng gcjLatLng = LonLatUtil.bd_decrypt(lat, lon);
//                Log.d("event_lat_lon", "百度坐标："+lon+" "+lat+" ;国测局坐标："+gcjLatLng.longitude+" "+gcjLatLng.latitude);
                CustomizedLayerItem item = new CustomizedLayerItem(lon, lat, CoordinateType.GCJ02,
                        getResources().getDrawable(R.drawable.blue_ic_icon_track_map_bar), CustomizedLayerItem.ALIGN_CENTER);
                items.add(item);
            }else {
                LatLng gcjLatLng = LonLatUtil.bd_decrypt(lat, lon);
                Log.d("event_lat_lon", "百度坐标："+lon+" "+lat+" ;国测局坐标："+gcjLatLng.longitude+" "+gcjLatLng.latitude);
                CustomizedLayerItem item = new CustomizedLayerItem(gcjLatLng.longitude, gcjLatLng.latitude, CoordinateType.GCJ02,
                        getResources().getDrawable(R.drawable.blue_ic_icon_track_map_bar), CustomizedLayerItem.ALIGN_CENTER);
                items.add(item);
            }

        }
        BNRouteGuideManager.getInstance().setCustomizedLayerItems(items);
        BNRouteGuideManager.getInstance().showCustomizedLayer(true);
    }

    private static final int MSG_SHOW = 1;
    private static final int MSG_HIDE = 2;
    private static final int MSG_RESET_NODE = 3;
    private Handler hd = null;

    private void createHandler() {
        if (hd == null) {
            hd = new Handler(getMainLooper()) {
                public void handleMessage(android.os.Message msg) {
                    if (msg.what == MSG_SHOW) {
                        addCustomizedLayerItems();
                    } else if (msg.what == MSG_HIDE) {
                        BNRouteGuideManager.getInstance().showCustomizedLayer(false);
                    } else if (msg.what == MSG_RESET_NODE) {
                        BNRouteGuideManager.getInstance().resetEndNodeInNavi(
                                new BNRoutePlanNode(116.21142, 40.85087, "百度大厦11", null, CoordinateType.GCJ02));
                    }
                };
            };
        }
    }

    private OnNavigationListener mOnNavigationListener = new OnNavigationListener() {

        @Override
        public void onNaviGuideEnd() {
            finish();
        }

        @Override
        public void notifyOtherAction(int actionType, int arg1, int arg2, Object obj) {

            if (actionType == 0) {
                Log.i(TAG, "notifyOtherAction actionType = " + actionType + ",导航到达目的地！");
                setResult(RESULT_OK);
                finish();
            }

            Log.i(TAG, "actionType:" + actionType + "arg1:" + arg1 + "arg2:" + arg2 + "obj:" + obj.toString());
        }
    };

    @Override
    public void onReceive(double longitude, double latitude) {
        uploadLocation(longitude, latitude);
        LatLng ll = LonLatUtil.bd_encrypt(latitude, longitude);
        dealEventPassBy(ll.longitude, ll.latitude);
    }

    private void uploadLocation(double longitude, double latitude){
        Map<String, String> params = new HashMap<>();
        params.put("type", "add");
        String userno = DBUtil.getConfigValue("user_name");
        params.put("userno", userno);
        params.put("longitude", longitude+"");
        params.put("latitude", latitude+"");
        String s = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", s, params, Urls.ROOT+Urls.GPS_INFO, Object.class, this, TASK_GPS_INFO);
    }

    private void dealEventPassBy(double longitude, double latitude) {
        Log.d("distance", "我的位置:"+longitude+" "+latitude);
        for (int i=0; i<latLngs.size(); i++){
            LatLng latLng = latLngs.get(i);
            double distance = LonLatUtil.getDistanceFromXtoY(longitude, latitude, latLng.longitude, latLng.latitude);
            Log.d("distance", "第"+(i+1)+"个事件的经纬度："+latLng.latitude+" "+latLng.longitude+" ;距离我的位置："+distance);
            if (distance<200){
                if (!isLastPlayVoice){
                    playNotifyMedia();
                }
                isLastPlayVoice = true;
                break;
            }
        }
    }

    private void playNotifyMedia() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                voiceHandler.sendEmptyMessage(0);
            }
        });
    }

    Handler voiceHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    SpeechSynthesizer speechSynthesizer = SpeechSynthesizer.getInstance();
                    speechSynthesizer.speak("前方200米有事件");
                    break;
                default:
                    break;
            }
        }
    };
}
