package com.fruit.client.object.pilemanager;

/**
 * Created by user on 2016/2/26.
 */
public class AddPileResponse {
    private String flag;
    private String msg;

    public Piles getData() {
        return data;
    }

    public void setData(Piles mData) {
        data = mData;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String mFlag) {
        flag = mFlag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String mMsg) {
        msg = mMsg;
    }

    private Piles data;
}
