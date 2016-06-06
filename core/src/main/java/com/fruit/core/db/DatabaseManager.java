package com.fruit.core.db;

import android.database.sqlite.SQLiteDatabase;
import com.fruit.common.application.ApplicationUtils;
import com.fruit.core.db.models.gen.DaoMaster;
import com.fruit.core.db.models.gen.DaoSession;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liangchen
 * @time 2014-4-29 下午3:39:55
 * @annotation SQLITE操作工具类单例管理类
 */
public class DatabaseManager {

    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static DatabaseManager instance;
    private SQLiteDatabase mDatabase;
    public static final String DB_NAME = "fruit.db";

    public static synchronized void initializeInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    "没有实例化，请先调用initializeInstance方法");
        }
        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if(mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            mDatabase = new DaoMaster.DevOpenHelper(ApplicationUtils.getAppContext(),DB_NAME,null).getWritableDatabase();
        }
        return mDatabase;
    }


    public synchronized DaoSession getDaoSession(){
        mDatabase =  openDatabase();
        DaoMaster daoMaster = new DaoMaster(mDatabase);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }


    public synchronized void closeDatabase() {
        if(mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            mDatabase.close();
        }
    }
}