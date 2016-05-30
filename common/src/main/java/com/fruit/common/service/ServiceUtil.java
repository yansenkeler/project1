package com.fruit.common.service;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by user on 2015/12/7.
 */
public class ServiceUtil {

    /**
     * 应用是否在前台运行
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isApplicationRunning(Context context, String packageName){
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfos = am.getRunningTasks(10);
        for (ActivityManager.RunningTaskInfo info: taskInfos){
            if(info.topActivity.getPackageName().equals(packageName) && info.baseActivity.getPackageName().equals(packageName)){
                return true;
            }
        }
        return false;
    }
}
