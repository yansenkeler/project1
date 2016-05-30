package com.fruit.core.webservice;

import android.content.Context;

import com.fruit.common.network.BasicNetworkUtils;
import com.fruit.core.api.UiDealInterface;
import com.fruit.core.api.WebServiceUiDealInterface;
import com.fruit.core.webservice.base.WebServiceRequestParam;
import com.fruit.core.webservice.base.WebServiceResponseHandlerInterface;

/**
 * Created by liangchen on 15/7/7.
 */
public class WebServiceTaskFactory {

    public static WerServiceAysTask createWebServiceTaskAndExcute(Context context,final int taskid,WebServiceRequestParam webServiceRequestParam, final WebServiceUiDealInterface webServiceUiDealInterface){
        WerServiceAysTask webserviceTask = null;
        if (BasicNetworkUtils.checkNetwork(context)) {//判断网络是否连接
            webserviceTask = new WerServiceAysTask(taskid, new WebServiceResponseHandlerInterface() {
                @Override
                public void onStart() {
                    webServiceUiDealInterface.WebServiceUIFresh(WebServiceUiDealInterface.RESPONSEHANDLER_ONSTART,null, null, taskid);
                }

                @Override
                public void onHandleResult(int statusCode, Object response) {
                    if (statusCode == 200) {//请求到数据时返回
                        webServiceUiDealInterface.WebServiceUIFresh(UiDealInterface.RESPONSEHANDLER_ONSUCCESS, null, response, taskid);
                    } else {//未请求到数据
                        webServiceUiDealInterface.WebServiceUIFresh(UiDealInterface.RESPONSEHANDLER_ONFAILURE, null, response, taskid);
                    }
                }
            });
            webserviceTask.execute(webServiceRequestParam);
        } else {
            //网络未连接
            webServiceUiDealInterface.WebServiceUIFresh(UiDealInterface.RESPONSEHANDLER_ONFAILURE,null,null,taskid);
        }
        return webserviceTask;
    }
}
