package com.fruit.client.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by user on 2016/4/29.
 */
public class UpdateService extends Service {
    private DownloadManager mDownloadManager;
    private DownloadComplateReceiver mReceiver;
    private String downloadUrl = "http://music.163.keler/api/android/download/latest2";

    private void initialDownloadManager(){
        mDownloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        mReceiver = new DownloadComplateReceiver();
        DownloadManager.Request down = new DownloadManager.Request(Uri.parse(downloadUrl));
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE |
                DownloadManager.Request.NETWORK_WIFI);
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        down.setVisibleInDownloadsUi(true);
        down.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "智能巡查.apk");
        mDownloadManager.enqueue(down);
        registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!=null && intent.hasExtra("download_url")){
            downloadUrl = intent.getStringExtra("download_url");
        }
        initialDownloadManager();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (mReceiver!=null){
            unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }

    class DownloadComplateReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                installApk(mDownloadManager.getUriForDownloadedFile(downId));
                UpdateService.this.stopSelf();
            }
        }
    }

    private void installApk(Uri apk){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("application/vnd.android.package-archive");
        intent.setData(apk);
        intent.setDataAndType(apk, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        android.os.Process.killProcess(android.os.Process.myPid());
        startActivity(intent);
    }
}
