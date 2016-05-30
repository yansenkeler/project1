package com.fruit.core.http;

import android.content.Context;

import com.fruit.common.cache.CacheHelp;
import com.fruit.common.network.BasicNetworkUtils;
import com.fruit.common.network.HttpUtilsAsync;
import com.fruit.core.api.UiDealInterface;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by liangchen on 15/5/4.
 */
public class CommonHttp {

    public final String UN_KNOW_EXCEPTION = "1000";//未知错误
    public final String MISS_PARAM = "1001";//缺失参数
    public final String BAD_PARAM = "1002";//错误的参数
    public final String NOT_ALLOWED = "1003";//无操作权限
    public final String UN_KNOW_WORKFLOW = "2000";//未知的工作流程
    public final String WORKFLOW_STOPED = "2001";//该流程已经结束
    public final String WORKFLOW_NO_NEXT_HANDLER = "2002";//下一步处理人为空
    public final String WORKFLOW_NO_NEXT_ACTIVITY = "2003";//下一步处理节点为空
    public final String RESOURCE_NOT_FOUND = "3001";//资源不存在
    public final String FILE_IMPORT_ERROR = "4001";//文件导入失败
    public final String EMAIL_USER_ERROR = "5001";//邮件收件人错误
    public final String LOGIN_ERROR = "1004";//登录失败

    /*普通get请求
    * 请求时针对不同的状态所有处理方式都由uiDealInterface接口来处理
    * 只要实现uiDealInterface即可进行相应的处理
    * */
    public static void get(Context context,String url,final UiDealInterface uiDealInterface,final int taskid){
        if (BasicNetworkUtils.checkNetwork(context)){//判断网络是否连接
            HttpUtilsAsync.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    // called before request is started
                    uiDealInterface.UIFresh(UiDealInterface.RESPONSEHANDLER_ONSTART, null, null, taskid);
                }


                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    // called when response HTTP status is "200 OK"
                    if (statusCode == 200) {//请求到数据时返回
                        String responseString = new String(responseBody);
                        uiDealInterface.UIFresh(UiDealInterface.RESPONSEHANDLER_ONSUCCESS, null, responseString, taskid);
                    } else {//未请求到数据
                        uiDealInterface.UIFresh(UiDealInterface.RESPONSEHANDLER_ONFAILURE, null, null, taskid);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    uiDealInterface.UIFresh(UiDealInterface.RESPONSEHANDLER_ONFAILURE, null, null, taskid);
                }


                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                    uiDealInterface.UIFresh(UiDealInterface.RESPONSEHANDLER_ONRETRY, null, null, taskid);
                }
            });
        }else{
            //网络未连接
            uiDealInterface.UIFresh(UiDealInterface.RESPONSEHANDLER_ONFAILURE,null,null,taskid);
        }


    }

    /*加入缓存的请求*/
    public static void getNeedCache(final Context context, final String url,final UiDealInterface uiDealInterface,final int taskid){
        if (BasicNetworkUtils.checkNetwork(context)){//判断网络是否连接
            HttpUtilsAsync.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    // called before request is started
                    uiDealInterface.UIFresh(UiDealInterface.RESPONSEHANDLER_ONSTART, null, null, taskid);
                }


                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    // called when response HTTP status is "200 OK"
                    if (statusCode == 200) {//请求到数据时返回
                        String responseString = new String(responseBody);
                        CacheHelp.putJsonCache(context, url, responseString);
                        uiDealInterface.UIFresh(UiDealInterface.RESPONSEHANDLER_ONSUCCESS, null, responseString, taskid);
                    } else {//未请求到数据
                        uiDealInterface.UIFresh(UiDealInterface.RESPONSEHANDLER_ONFAILURE, null, null, taskid);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    uiDealInterface.UIFresh(UiDealInterface.RESPONSEHANDLER_ONFAILURE, null, null, taskid);
                }


                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                    uiDealInterface.UIFresh(UiDealInterface.RESPONSEHANDLER_ONRETRY, null, null, taskid);
                }
            });
        }else{
           String returnstring =   CacheHelp.getJsonCache(context, url);
            if (returnstring!=null){
                uiDealInterface.UIFresh(UiDealInterface.RESPONSEHANDLER_ONFAILURE,null,returnstring,taskid);
            }else{
                uiDealInterface.UIFresh(UiDealInterface.RESPONSEHANDLER_ONFAILURE,null,null,taskid);
            }
        }
    }

    /*普通post请求
    * 请求时针对不同的状态所有处理方式都由uiDealInterface接口来处理
    * 只要实现uiDealInterface即可进行相应的处理
    * */
    public static void post(Context context,String url,RequestParams params,final UiDealInterface uiDealInterface,final int taskid){
        if (BasicNetworkUtils.checkNetwork(context)){//判断网络是否连接

            HttpUtilsAsync.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    // called before request is started
                    uiDealInterface.UIFresh(UiDealInterface.RESPONSEHANDLER_ONSTART, null, null, taskid);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    // called when response HTTP status is "200 OK"
                    if (statusCode == 200) {//请求到数据时返回
                        String responseString = new String(responseBody);
                        uiDealInterface.UIFresh(UiDealInterface.RESPONSEHANDLER_ONSUCCESS, null, responseString, taskid);
                    } else {//未请求到数据
                        uiDealInterface.UIFresh(UiDealInterface.RESPONSEHANDLER_ONFAILURE, null, null, taskid);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    uiDealInterface.UIFresh(UiDealInterface.RESPONSEHANDLER_ONFAILURE, null, null, taskid);
                }


                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                    uiDealInterface.UIFresh(UiDealInterface.RESPONSEHANDLER_ONRETRY, null, null, taskid);
                }
            });
        }else{
            //网络未连接
            uiDealInterface.UIFresh(UiDealInterface.RESPONSEHANDLER_ONFAILURE, null, null, taskid);
        }
    }

}
