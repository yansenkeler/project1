package com.fruit.core.application;

import android.app.Application;

import com.fruit.common.application.ApplicationUtils;
import com.fruit.common.application.FrameApplication;
import com.fruit.common.img.UniversalImageLoader;
import com.fruit.common.res.ResUtils;
import com.fruit.core.db.DatabaseManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by liangchen on 15/3/14.
 */
public class FruitAndroidApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("APPLICATION启动...");
        FrameApplication.initializeInstance(this);
        DatabaseManager.initializeInstance();
        ResUtils.initializeInstance(this);
        Thread.setDefaultUncaughtExceptionHandler(handler);

        //图片异步加载初始化
        ImageLoaderConfiguration imageLoaderConfiguration = UniversalImageLoader.getDefaultImageLoaderConfiguration(getApplicationContext());
        ImageLoader.getInstance().init(imageLoaderConfiguration);


    }

    Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ApplicationUtils.saveErrorLog(ex);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    };


}
