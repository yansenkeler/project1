package com.fruit.client.object.event;

/**
 * Created by John on 6/13/2016.
 */
public class PushMessage {
    private String msgPk;
    private String UserNo;
    private String dateInfo;
    private String title;
    private String MsgCenter;
    private String IsRead;
    private String billNo;
    private String IsDel;

    public String getMsgPk() {
        return msgPk;
    }

    public void setMsgPk(String msgPk) {
        this.msgPk = msgPk;
    }

    public String getUserNo() {
        return UserNo;
    }

    public void setUserNo(String userNo) {
        UserNo = userNo;
    }

    public String getDateInfo() {
        return dateInfo;
    }

    public void setDateInfo(String dateInfo) {
        this.dateInfo = dateInfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsgCenter() {
        return MsgCenter;
    }

    public void setMsgCenter(String msgCenter) {
        MsgCenter = msgCenter;
    }

    public String getIsRead() {
        return IsRead;
    }

    public void setIsRead(String isRead) {
        IsRead = isRead;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getIsDel() {
        return IsDel;
    }

    public void setIsDel(String isDel) {
        IsDel = isDel;
    }
}
