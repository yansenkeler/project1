package com.fruit.client.object;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2016/4/29.
 */
public class ImageItem implements Parcelable{
    private Bitmap mBitmap;
    private int uploadStatus;
    private String imgUrl;
    private String filePk;
    private boolean isLast = false;
    private boolean isShown = true;

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }

    public ImageItem() {
    }

    public ImageItem(Bitmap mBitmap, int uploadStatus, String imgUrl, String filePk) {
        this.mBitmap = mBitmap;
        this.uploadStatus = uploadStatus;
        this.imgUrl = imgUrl;
        this.filePk = filePk;
    }

    protected ImageItem(Parcel in) {
        mBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        uploadStatus = in.readInt();
        imgUrl = in.readString();
        filePk = in.readString();
        isLast = in.readByte() != 0;
    }

    public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
        @Override
        public ImageItem createFromParcel(Parcel in) {
            return new ImageItem(in);
        }

        @Override
        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mBitmap, flags);
        dest.writeInt(uploadStatus);
        dest.writeString(imgUrl);
        dest.writeString(filePk);
        dest.writeByte((byte) (isLast ? 1 : 0));
    }
}
