package com.fruit.client.object.query;

/**
 * Created by user on 2016/2/26.
 */
public class QueryParamsResponse {
    private String flag;
    private Params data;
    private String msg;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String mFlag) {
        flag = mFlag;
    }

    public Params getData() {
        return data;
    }

    public void setData(Params mData) {
        data = mData;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String mMsg) {
        msg = mMsg;
    }
}
