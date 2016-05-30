package com.fruit.client;

import android.util.Log;

import com.baidu.tts.answer.auth.AuthInfo;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.TtsMode;
import com.fruit.client.util.Locationor;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.fruit.core.application.FruitAndroidApplication;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by user on 2016/2/25.
 */
public class MyApplication extends FruitAndroidApplication implements BDLocationListener{
    private static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication instance;
    private OnReceiveData mReceiveDataListener;

    @Override
    public void onCreate() {
        super.onCreate();

        Locationor.getInstance(this).setLocation(this);
        CrashReport.UserStrategy mUserStrategy = new CrashReport.UserStrategy(getApplicationContext());
        CrashReport.initCrashReport(getApplicationContext(), "900020927", false, mUserStrategy);
        SDKInitializer.initialize(getApplicationContext());
        initTTS();
    }

    private void initTTS() {
        SpeechSynthesizer speechSynthesizer = SpeechSynthesizer.getInstance();
        speechSynthesizer.setContext(getApplicationContext());
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, "");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, "");
        speechSynthesizer.setAppId("7922874");
        speechSynthesizer.setApiKey("wWfQryRWHf8ZhQHeGTq2mBKBWHwPnXSO", "FAzEVykiXZV8Yp3TGXxmXxBBCTBF62ZE");
        AuthInfo authInfo = speechSynthesizer.auth(TtsMode.ONLINE);
        speechSynthesizer.initTts(TtsMode.ONLINE);
    }

    public void setReceiveDataListener(OnReceiveData mReceiveDataListener) {
        this.mReceiveDataListener = mReceiveDataListener;
    }

    @Override
    public void onReceiveLocation(BDLocation mBDLocation) {
        Log.d("location", "receive location data");
        mReceiveDataListener.onReceive(mBDLocation.getLongitude(), mBDLocation.getLatitude());
    }

    public interface OnReceiveData{
        void onReceive(double longitude, double latitude);
    }
}
