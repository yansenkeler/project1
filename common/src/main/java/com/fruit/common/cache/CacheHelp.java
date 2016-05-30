package com.fruit.common.cache;

import android.content.Context;
import android.os.Environment;

import com.fruit.common.application.ApplicationUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by liangchen on 15/5/12.
 */
public class CacheHelp {
    public static final int RAM_MAX_SIZE = 200;
    public static final String CACHE_NAME = "fruitcache";
    public static final int TEST_APP_VERSION = 1;

    public static SimpleDiskCache mDiskLruCache = null;

    public static void getJsonSimpleDisCache(Context context){
        try {
            if (mDiskLruCache==null){
                mDiskLruCache = SimpleDiskCache.open(getDiskCacheDir(context, "json"), ApplicationUtils.getAppVersion(context), 10 * 1024 * 1024);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*添加缓存*/
    public static void putJsonCache(Context context,String url ,String jsonstring){

        try {
            String key = hashKeyForDisk(url);
            getJsonSimpleDisCache(context);
            mDiskLruCache.put(key,jsonstring);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getJsonCache(Context context,String url){
        String key = hashKeyForDisk(url);
        SimpleDiskCache.StringEntry returnstring = null;
        try {
            getJsonSimpleDisCache(context);
            returnstring =  mDiskLruCache.getString(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnstring.getString();
    }

    public static void clearCache(Context context){
        try {
            if (mDiskLruCache!=null){
                mDiskLruCache.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 读取流
     *
     * @param inStream
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }


    /*打开普通图片缓存的处理文件夹*/
    public static DiskLruCache openImageCache(Context context){
        DiskLruCache mDiskLruCache = null;
        try {
            File cacheDir = getDiskCacheDir(context, "bitmap");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, ApplicationUtils.getAppVersion(context), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mDiskLruCache;
    }

    /*打开普通json缓存的处理文件夹*/
    public static DiskLruCache openJsonStringCache(Context context){
        DiskLruCache mDiskLruCache = null;
        try {
            File cacheDir = getDiskCacheDir(context, "jsonobject");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, ApplicationUtils.getAppVersion(context), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mDiskLruCache;
    }

    /*获取缓存根目录，uniqueName用于区分图片，还是文字存储*/
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }


    //缓存时对key进行加密
    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }


}
