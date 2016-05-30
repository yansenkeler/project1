package com.fruit.core.fragment;

import android.support.v4.app.Fragment;

import com.fruit.core.R;
import com.fruit.core.api.UiDealInterface;
import com.fruit.core.api.WebServiceUiDealInterface;
import com.fruit.core.webservice.WebServiceTaskFactory;
import com.fruit.core.webservice.WerServiceAysTask;
import com.fruit.core.webservice.base.WebServiceRequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangchen on 15/7/7.
 */
public abstract class FruitWebServiceFragment extends Fragment implements WebServiceUiDealInterface{


    /*任务模式工具类start*/
    private Map<Integer,WerServiceAysTask> taskMap;
    private int currentTaskId;

    public void excuteTask(int taskId,String method,Map<String,String> mapParams){
        WebServiceRequestParam webServiceRequestParam = new WebServiceRequestParam();
        webServiceRequestParam.setMethodName(method);
        webServiceRequestParam.setServiceUrl(getResources().getString(R.string.webserviceUrl));
        webServiceRequestParam.setServiceNameSpace(getResources().getString(R.string.webserviceNameSpace));

        webServiceRequestParam.setRequestProperty(mapParams);

        WerServiceAysTask temptask =  WebServiceTaskFactory.createWebServiceTaskAndExcute(this.getActivity(), taskId, webServiceRequestParam, this);

        //以下操作供取消的时候使用，设置了当前task就可以找到当前的task，就可以取消了
        if (taskMap==null){
            taskMap = new HashMap<Integer,WerServiceAysTask>();
        }
        taskMap.put(taskId,temptask);
        currentTaskId = taskId;
    }
    /*任务模式工具类end*/

    @Override
    public void WebServiceUIFresh(int status, String errorcode, Object returnObj, int taskid) {
        if (status== UiDealInterface.RESPONSEHANDLER_ONSTART){
            RequestOnStart(taskid);
        }else if(status== UiDealInterface.RESPONSEHANDLER_ONSUCCESS){
            DealSuccessResult(returnObj, taskid);
        }else if(status== UiDealInterface.RESPONSEHANDLER_ONFAILURE){
            DealFailureResult(errorcode,taskid);
        }else if(status== UiDealInterface.RESPONSEHANDLER_ONRETRY){

        }
    }

    /*请求开始*/
    public abstract void RequestOnStart(int taskid);

    /*请求失败*/
    public abstract void DealFailureResult(Object returnObject, int taskid);

    /*请求成功*/
    public abstract void DealSuccessResult(Object returnObject, int taskid);


    /*取消当前异步加载任务*/
    public void cancleTask(){
        if (taskMap!=null){
            WerServiceAysTask temptask = taskMap.get(currentTaskId);
            if (temptask!=null){
                temptask.cancel(true);
                taskMap.remove(currentTaskId);
            }
        }
    }

}
