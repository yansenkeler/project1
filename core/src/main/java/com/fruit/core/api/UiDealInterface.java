package com.fruit.core.api;

/**
 * Created by liangchen on 15/3/13.
 */
public interface UiDealInterface {

    public static final int RESPONSEHANDLER_ONSTART=10;
    public static final int RESPONSEHANDLER_ONSUCCESS=11;
    public static final int RESPONSEHANDLER_ONFAILURE=12;
    public static final int RESPONSEHANDLER_ONRETRY=13;

    /*webservice请求的时候*/
    void UIFresh(int status, String errorcode, String returnString, int taskid);

}
