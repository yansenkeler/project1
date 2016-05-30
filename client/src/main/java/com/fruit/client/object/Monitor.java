package com.fruit.client.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by John on 2016/5/19.
 */
public class Monitor implements Parcelable{
    private String FacilityPK;
    private String RouteCode;
    private String FacilityName;
    private String PileNo;
    private String Longitude;
    private String Latitude;
    private String Description;
    private String SetDate;
    private String CreateTime;
    private String Valid;
    private String ImageName;
    private String Location;
    private String SupportType;
    private String Size;

    public Monitor() {
    }

    protected Monitor(Parcel in) {
        FacilityPK = in.readString();
        RouteCode = in.readString();
        FacilityName = in.readString();
        PileNo = in.readString();
        Longitude = in.readString();
        Latitude = in.readString();
        Description = in.readString();
        SetDate = in.readString();
        CreateTime = in.readString();
        Valid = in.readString();
        ImageName = in.readString();
        Location = in.readString();
        SupportType = in.readString();
        Size = in.readString();
    }

    public static final Creator<Monitor> CREATOR = new Creator<Monitor>() {
        @Override
        public Monitor createFromParcel(Parcel in) {
            return new Monitor(in);
        }

        @Override
        public Monitor[] newArray(int size) {
            return new Monitor[size];
        }
    };

    public String getFacilityPK() {

        return FacilityPK;
    }

    public void setFacilityPK(String facilityPK) {
        FacilityPK = facilityPK;
    }

    public String getRouteCode() {
        return RouteCode;
    }

    public void setRouteCode(String routeCode) {
        RouteCode = routeCode;
    }

    public String getFacilityName() {
        return FacilityName;
    }

    public void setFacilityName(String facilityName) {
        FacilityName = facilityName;
    }

    public String getPileNo() {
        return PileNo;
    }

    public void setPileNo(String pileNo) {
        PileNo = pileNo;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getSetDate() {
        return SetDate;
    }

    public void setSetDate(String setDate) {
        SetDate = setDate;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getValid() {
        return Valid;
    }

    public void setValid(String valid) {
        Valid = valid;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getSupportType() {
        return SupportType;
    }

    public void setSupportType(String supportType) {
        SupportType = supportType;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(FacilityPK);
        dest.writeString(RouteCode);
        dest.writeString(FacilityName);
        dest.writeString(PileNo);
        dest.writeString(Longitude);
        dest.writeString(Latitude);
        dest.writeString(Description);
        dest.writeString(SetDate);
        dest.writeString(CreateTime);
        dest.writeString(Valid);
        dest.writeString(ImageName);
        dest.writeString(Location);
        dest.writeString(SupportType);
        dest.writeString(Size);
    }
}
