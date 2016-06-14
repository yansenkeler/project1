package com.fruit.client.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by John on 6/13/2016.
 */
public class ImageInfo implements Parcelable{
    private String filePK;
    private String BillNO;
    private String type;
    private String fileName;
    private String fileSize;
    private String Url;
    private String upUser;
    private String upTime;

    public ImageInfo() {
    }

    public String getFilePK() {
        return filePK;
    }

    public void setFilePK(String filePK) {
        this.filePK = filePK;
    }

    public String getBillNO() {
        return BillNO;
    }

    public void setBillNO(String billNO) {
        BillNO = billNO;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getUpUser() {
        return upUser;
    }

    public void setUpUser(String upUser) {
        this.upUser = upUser;
    }

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    protected ImageInfo(Parcel in) {
        filePK = in.readString();
        BillNO = in.readString();
        type = in.readString();
        fileName = in.readString();
        fileSize = in.readString();
        Url = in.readString();
        upUser = in.readString();
        upTime = in.readString();
    }

    public static final Creator<ImageInfo> CREATOR = new Creator<ImageInfo>() {
        @Override
        public ImageInfo createFromParcel(Parcel in) {
            return new ImageInfo(in);
        }

        @Override
        public ImageInfo[] newArray(int size) {
            return new ImageInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filePK);
        dest.writeString(BillNO);
        dest.writeString(type);
        dest.writeString(fileName);
        dest.writeString(fileSize);
        dest.writeString(Url);
        dest.writeString(upUser);
        dest.writeString(upTime);
    }
}
