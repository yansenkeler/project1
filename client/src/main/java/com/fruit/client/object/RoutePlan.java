package com.fruit.client.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by John on 2016/5/12.
 */
public class RoutePlan implements Parcelable{
    private String label;
    private String taskNo;
    private String planPk;
    private String deptPk;
    private String deptName;
    private String dateInfo;
    private String state;
    private String userNo;
    private String userName;
    private String endTime;
    private String planName;
    private String planDescr;

    public RoutePlan(String label, String planPk, String deptName, String dateInfo, String state, String endTime, String taskNo, String planName) {
        this.label = label;
        this.planPk = planPk;
        this.deptName = deptName;
        this.dateInfo = dateInfo;
        this.state = state;
        this.endTime = endTime;
        this.taskNo = taskNo;
        this.planName = planName;
    }

    protected RoutePlan(Parcel in) {
        label = in.readString();
        taskNo = in.readString();
        planPk = in.readString();
        deptPk = in.readString();
        deptName = in.readString();
        dateInfo = in.readString();
        state = in.readString();
        userNo = in.readString();
        userName = in.readString();
        endTime = in.readString();
        planName = in.readString();
        planDescr = in.readString();
    }

    public String getPlanDescr() {
        return planDescr;
    }

    public void setPlanDescr(String planDescr) {
        this.planDescr = planDescr;
    }

    public static final Creator<RoutePlan> CREATOR = new Creator<RoutePlan>() {
        @Override
        public RoutePlan createFromParcel(Parcel in) {
            return new RoutePlan(in);
        }

        @Override
        public RoutePlan[] newArray(int size) {
            return new RoutePlan[size];
        }
    };

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getPlanPk() {
        return planPk;
    }

    public void setPlanPk(String planPk) {
        this.planPk = planPk;
    }

    public String getDeptPk() {
        return deptPk;
    }

    public void setDeptPk(String deptPk) {
        this.deptPk = deptPk;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDateInfo() {
        return dateInfo;
    }

    public void setDateInfo(String dateInfo) {
        this.dateInfo = dateInfo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public RoutePlan() {
    }

    public RoutePlan(String label) {

        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(taskNo);
        dest.writeString(planPk);
        dest.writeString(deptPk);
        dest.writeString(deptName);
        dest.writeString(dateInfo);
        dest.writeString(state);
        dest.writeString(userNo);
        dest.writeString(userName);
        dest.writeString(endTime);
        dest.writeString(planName);
        dest.writeString(planDescr);
    }
}
