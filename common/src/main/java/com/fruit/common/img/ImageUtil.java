package com.fruit.common.img;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by JOJO on 2015/11/15.
 */
public class ImageUtil {

    /**
     * 将图片Uri转换成Bitmap
     * @param mContext
     * @param uri
     * @return
     */
    public static Bitmap getBitmapFromUri(Context mContext, Uri uri)
    {
        try
        {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            return bitmap;
        }
        catch (Exception e)
        {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }
}
