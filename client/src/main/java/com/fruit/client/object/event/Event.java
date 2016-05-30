package com.fruit.client.object.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2016/4/28.
 */
public class Event implements Parcelable{
    private String BillPk;
    private String BillNo;
    private String BillDate;
    private String Grade;
    private String State;
    private String RouteNo;
    private String PileNo;
    private String Direction;
    private String Lon;
    private String Lat;
    private String CarNo;
    private String userName;
    private String userPhone;
    private String Type1;
    private String Type2;
    private String Type3;
    private String Type;
    private String Number;
    private String unit;
    private String ProcessType;
    private String IsProcess;
    private String ProcessDept;
    private String RequEndTime;
    private String FacilityPk;
    private String Memo;
    private String RealName;

    public Event(String billPk, String billNo, String billDate, String grade, String state, String routeNo, String pileNo, String direction, String lon, String lat, String carNo, String userName, String userPhone, String type1, String type2, String type3, String type, String number, String unit, String processType, String isProcess, String processDept, String requEndTime, String facilityPk, String memo, String realName) {
        BillPk = billPk;
        BillNo = billNo;
        BillDate = billDate;
        Grade = grade;
        State = state;
        RouteNo = routeNo;
        PileNo = pileNo;
        Direction = direction;
        Lon = lon;
        Lat = lat;
        CarNo = carNo;
        this.userName = userName;
        this.userPhone = userPhone;
        Type1 = type1;
        Type2 = type2;
        Type3 = type3;
        Type = type;
        Number = number;
        this.unit = unit;
        ProcessType = processType;
        IsProcess = isProcess;
        ProcessDept = processDept;
        RequEndTime = requEndTime;
        FacilityPk = facilityPk;
        Memo = memo;
        RealName = realName;
    }

    public Event() {
    }

    protected Event(Parcel in) {
        BillPk = in.readString();
        BillNo = in.readString();
        BillDate = in.readString();
        Grade = in.readString();
        State = in.readString();
        RouteNo = in.readString();
        PileNo = in.readString();
        Direction = in.readString();
        Lon = in.readString();
        Lat = in.readString();
        CarNo = in.readString();
        userName = in.readString();
        userPhone = in.readString();
        Type1 = in.readString();
        Type2 = in.readString();
        Type3 = in.readString();
        Type = in.readString();
        Number = in.readString();
        unit = in.readString();
        ProcessType = in.readString();
        IsProcess = in.readString();
        ProcessDept = in.readString();
        RequEndTime = in.readString();
        FacilityPk = in.readString();
        Memo = in.readString();
        RealName = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getRealName() {

        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getBillPk() {
        return BillPk;
    }

    public void setBillPk(String billPk) {
        BillPk = billPk;
    }

    public String getBillNo() {
        return BillNo;
    }

    public void setBillNo(String billNo) {
        BillNo = billNo;
    }

    public String getBillDate() {
        return BillDate;
    }

    public void setBillDate(String billDate) {
        BillDate = billDate;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getRouteNo() {
        return RouteNo;
    }

    public void setRouteNo(String routeNo) {
        RouteNo = routeNo;
    }

    public String getPileNo() {
        return PileNo;
    }

    public void setPileNo(String pileNo) {
        PileNo = pileNo;
    }

    public String getDirection() {
        return Direction;
    }

    public void setDirection(String direction) {
        Direction = direction;
    }

    public String getLon() {
        return Lon;
    }

    public void setLon(String lon) {
        Lon = lon;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getCarNo() {
        return CarNo;
    }

    public void setCarNo(String carNo) {
        CarNo = carNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getType1() {
        return Type1;
    }

    public void setType1(String type1) {
        Type1 = type1;
    }

    public String getType2() {
        return Type2;
    }

    public void setType2(String type2) {
        Type2 = type2;
    }

    public String getType3() {
        return Type3;
    }

    public void setType3(String type3) {
        Type3 = type3;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getProcessType() {
        return ProcessType;
    }

    public void setProcessType(String processType) {
        ProcessType = processType;
    }

    public String getIsProcess() {
        return IsProcess;
    }

    public void setIsProcess(String isProcess) {
        IsProcess = isProcess;
    }

    public String getProcessDept() {
        return ProcessDept;
    }

    public void setProcessDept(String processDept) {
        ProcessDept = processDept;
    }

    public String getRequEndTime() {
        return RequEndTime;
    }

    public void setRequEndTime(String requEndTime) {
        RequEndTime = requEndTime;
    }

    public String getFacilityPk() {
        return FacilityPk;
    }

    public void setFacilityPk(String facilityPk) {
        FacilityPk = facilityPk;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(BillPk);
        dest.writeString(BillNo);
        dest.writeString(BillDate);
        dest.writeString(Grade);
        dest.writeString(State);
        dest.writeString(RouteNo);
        dest.writeString(PileNo);
        dest.writeString(Direction);
        dest.writeString(Lon);
        dest.writeString(Lat);
        dest.writeString(CarNo);
        dest.writeString(userName);
        dest.writeString(userPhone);
        dest.writeString(Type1);
        dest.writeString(Type2);
        dest.writeString(Type3);
        dest.writeString(Type);
        dest.writeString(Number);
        dest.writeString(unit);
        dest.writeString(ProcessType);
        dest.writeString(IsProcess);
        dest.writeString(ProcessDept);
        dest.writeString(RequEndTime);
        dest.writeString(FacilityPk);
        dest.writeString(Memo);
        dest.writeString(RealName);
    }
}
