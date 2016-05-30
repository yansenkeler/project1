package com.fruit.client.object.facilitymanager;

import android.graphics.Bitmap;

/**
 * Created by user on 2016/3/1.
 */
public class ImageInfo {
    private Bitmap mBitmap;
    private int progress;
    private boolean isImage;

    public ImageInfo() {
    }

    public ImageInfo(Bitmap mBitmap, int mProgress, boolean mIsImage) {
        this.mBitmap = mBitmap;
        progress = mProgress;
        isImage = mIsImage;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int mProgress) {
        progress = mProgress;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setIsImage(boolean mIsImage) {
        isImage = mIsImage;
    }
}
