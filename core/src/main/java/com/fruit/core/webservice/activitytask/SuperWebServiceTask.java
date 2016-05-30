package com.fruit.core.webservice.activitytask;

import android.content.Context;

import com.fruit.core.R;
import com.fruit.core.api.WebServiceUiDealInterface;
import com.fruit.core.webservice.WebServiceTaskFactory;
import com.fruit.core.webservice.WerServiceAysTask;
import com.fruit.core.webservice.base.WebServiceRequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangchen on 15/7/7.
 */
public abstract class SuperWebServiceTask {

    public WerServiceAysTask AysTask;
    public Context context;
    public int taskId;
    public WebServiceUiDealInterface webServiceUiDealInterface;
    public Map<String,String> mapParams ;
    public WebServiceRequestParam webServiceRequestParam;

    public SuperWebServiceTask(Context context, int taskId, Map<String,String> mapParams, WebServiceUiDealInterface webServiceUiDealInterface) {
        this.context = context;
        this.taskId = taskId;
        this.mapParams = mapParams;
        this.webServiceUiDealInterface = webServiceUiDealInterface;
    }

    /*执行task*/
    public void excute(){

        webServiceRequestParam = new WebServiceRequestParam();
        webServiceRequestParam.setMethodName(getMethod());
        webServiceRequestParam.setServiceUrl(context.getResources().getString(R.string.webserviceUrl));
        webServiceRequestParam.setServiceNameSpace(context.getResources().getString(R.string.webserviceNameSpace));

        webServiceRequestParam.setRequestProperty(mapParams);

        WerServiceAysTask temptask =  WebServiceTaskFactory.createWebServiceTaskAndExcute(context, taskId, webServiceRequestParam, webServiceUiDealInterface);
        setAysTask(temptask);
    }

    public WerServiceAysTask getAysTask() {
        return AysTask;
    }

    public void setAysTask(WerServiceAysTask aysTask) {
        AysTask = aysTask;
    }

    /*取消task*/
    public void cancle(){
        getAysTask().cancel(true);
    }

    public abstract String getMethod();
}
