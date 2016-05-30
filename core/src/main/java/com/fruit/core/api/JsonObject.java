package com.fruit.core.api;

/**
 * Created by user on 2015/12/17.
 */
public class JsonObject {
    public int ret ;
    public Object data = null;
    public Object msg = null;

    public JsonObject() {
    }

    public JsonObject(int ret, Object data, Object msg) {
        this.ret = ret;
        this.data = data;
        this.msg = msg;
    }
}
