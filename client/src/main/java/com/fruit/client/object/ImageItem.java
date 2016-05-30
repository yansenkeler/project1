package com.fruit.client.object;

import android.graphics.Bitmap;

/**
 * Created by user on 2016/4/29.
 */
public class ImageItem {
    private Bitmap mBitmap;
    private int uploadStatus;
    private String imgUrl;
    private String filePk;

    public ImageItem() {
    }

    public ImageItem(Bitmap mBitmap, int uploadStatus, String imgUrl, String filePk) {

        this.mBitmap = mBitmap;
        this.uploadStatus = uploadStatus;
        this.imgUrl = imgUrl;
        this.filePk = filePk;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getFilePk() {
        return filePk;
    }

    public void setFilePk(String filePk) {
        this.filePk = filePk;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public int getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(int mUploadStatus) {
        uploadStatus = mUploadStatus;
    }
}
