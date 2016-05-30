package com.fruit.core.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.fruit.common.ui.ToastUtil;
import com.fruit.core.R;
import com.fruit.core.api.UiDealInterface;
import com.fruit.core.api.WebServiceUiDealInterface;
import com.fruit.core.webservice.WebServiceTaskFactory;
import com.fruit.core.webservice.WerServiceAysTask;
import com.fruit.core.webservice.base.WebServiceRequestParam;
import com.fruit.widget.progressDialog.SimpleProgressDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangchen on 15/7/7.
 */
public abstract class FruitWebServiceActivity extends FragmentActivity implements WebServiceUiDealInterface , View.OnClickListener{

    private SharedPreferences prefs;

    public String getUrl(){
        return this.getString(R.string.Fruit_URL);
    }

    public static SharedPreferences preferences(Context paramContext)
    {
        return paramContext.getSharedPreferences(paramContext.getPackageName(), 3);
    }

    public boolean getBooleanParam(String paramString)
    {
        return getBooleanParam(paramString, false);
    }

    public boolean getBooleanParam(String paramString, boolean paramBoolean)
    {
        Intent localIntent = getIntent();
        try
        {
            Uri localUri = localIntent.getData();
            if (localUri != null)
            {
                String str = localUri.getQueryParameter(paramString);
                if (!TextUtils.isEmpty(str))
                {
                    boolean bool = Boolean.parseBoolean(str);
                    return bool;
                }
            }
        }
        catch (Exception localException)
        {
        }
        return localIntent.getBooleanExtra(paramString, paramBoolean);
    }

    public byte getByteParam(String paramString)
    {
        return getByteParam(paramString, (byte) 0);
    }

    public byte getByteParam(String paramString, byte paramByte)
    {
        Intent localIntent = getIntent();
        try
        {
            Uri localUri = localIntent.getData();
            if (localUri != null)
            {
                byte b = Byte.parseByte(localUri.getQueryParameter(paramString));
                return b;
            }
        }
        catch (Exception localException)
        {
        }
        return localIntent.getByteExtra(paramString, paramByte);
    }

    public char getCharParam(String paramString)
    {
        return getCharParam(paramString, '\000');
    }

    public char getCharParam(String paramString, char paramChar)
    {
        Intent localIntent = getIntent();
        try
        {
            Uri localUri = localIntent.getData();
            if (localUri != null)
            {
                char c = localUri.getQueryParameter(paramString).charAt(0);
                return c;
            }
        }
        catch (Exception localException)
        {
        }
        return localIntent.getCharExtra(paramString, paramChar);
    }

    public double getDoubleParam(String paramString)
    {
        return getDoubleParam(paramString, 0.0D);
    }

    public double getDoubleParam(String paramString, double paramDouble)
    {
        Intent localIntent = getIntent();
        try
        {
            Uri localUri = localIntent.getData();
            if (localUri != null)
            {
                double d = Double.parseDouble(localUri.getQueryParameter(paramString));
                return d;
            }
        }
        catch (Exception localException)
        {
        }
        return localIntent.getDoubleExtra(paramString, paramDouble);
    }

    public float getFloatParam(String paramString)
    {
        return getFloatParam(paramString, 0.0F);
    }

    public float getFloatParam(String paramString, float paramFloat)
    {
        Intent localIntent = getIntent();
        try
        {
            Uri localUri = localIntent.getData();
            if (localUri != null)
            {
                float f = Float.parseFloat(localUri.getQueryParameter(paramString));
                return f;
            }
        }
        catch (Exception localException)
        {
        }
        return localIntent.getFloatExtra(paramString, paramFloat);
    }

    public int getIntParam(String paramString)
    {
        return getIntParam(paramString, 0);
    }

    public int getIntParam(String paramString, int paramInt)
    {
        Intent localIntent = getIntent();
        try
        {
            Uri localUri = localIntent.getData();
            if (localUri != null)
            {
                int i = Integer.parseInt(localUri.getQueryParameter(paramString));
                return i;
            }
        }
        catch (Exception localException)
        {
        }
        return localIntent.getIntExtra(paramString, paramInt);
    }

    public long getLongParam(String paramString)
    {
        return getLongParam(paramString, 0L);
    }

    public long getLongParam(String paramString, long paramLong)
    {
        Intent localIntent = getIntent();
        try
        {
            Uri localUri = localIntent.getData();
            if (localUri != null)
            {
                long l = Long.parseLong(localUri.getQueryParameter(paramString));
                return l;
            }
        }
        catch (Exception localException)
        {
        }
        return localIntent.getLongExtra(paramString, paramLong);
    }

    public short getShortParam(String paramString, short paramShort)
    {
        Intent localIntent = getIntent();
        try
        {
            Uri localUri = localIntent.getData();
            if (localUri != null)
            {
                short s = Short.parseShort(localUri.getQueryParameter(paramString));
                return s;
            }
        }
        catch (Exception localException)
        {
        }
        return localIntent.getShortExtra(paramString, paramShort);
    }

    public String getStringParam(String paramString)
    {
        Intent localIntent = getIntent();
        try
        {
            Uri localUri = localIntent.getData();
            if (localUri != null)
            {
                String str = localUri.getQueryParameter(paramString);
                if (str != null)
                    return str;
            }
        }
        catch (Exception localException)
        {
        }
        return localIntent.getStringExtra(paramString);
    }

    public SharedPreferences preferences()
    {
        if (this.prefs == null)
            this.prefs = preferences(this);
        return this.prefs;
    }

    @Override
    public void onClick(View view) {
        onFruitActivityClick(view);
    }

    /*需要处理click事件的时候使用*/
    public void onFruitActivityClick(View view){
    }

    /*异步加载get方法的相关回调处理start
   * 使用CommonHttp请求结束后回调UiDealInterface的UIFresh，处理后分发到相应的方法，
   * 重新相关状态的方法即可，重写onstart方法之类的
   * */
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

    /*加载进度条处理*/
    SimpleProgressDialog dialog;
    boolean dialogIsShow;

    public void setDialog(String dialogmeg){

        if (dialog==null){
            dialog = SimpleProgressDialog.createLoadingDialog(FruitWebServiceActivity.this);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                    cancleTask();
                }
            });
        }
        dialog.setMsg(dialogmeg);
    }

    public void showDialog(String dialogmeg){
        setDialog(dialogmeg);
        if (!dialogIsShow) {
            //在弹出窗口之前用Activity的isFinishing判断一下Activity是否还存在:
            if (!isFinishing()) {
                dialog.show();
                dialogIsShow = true;
            }
        }
    }

    public void hideProgressDialog(){
        if (dialogIsShow) {
            dialog.dismiss();
            dialogIsShow = false;
        }
    }

    /*请求开始*/
    public void RequestOnStart(int taskid){
        showDialog("加载中...");
    }

    /*请求失败*/
    public void DealFailureResult(Object returnObject, int taskid){
        hideProgressDialog();
        ToastUtil.showShort(this, "网络异常");
    }

    /*请求成功*/
    public abstract void DealSuccessResult(Object returnObject, int taskid);

    /*任务模式工具类start*/
    private Map<Integer,WerServiceAysTask> taskMap;
    private int currentTaskId;

    public void excuteTask(int taskId,String method,Map<String,String> mapParams){
        WebServiceRequestParam webServiceRequestParam = new WebServiceRequestParam();
        webServiceRequestParam.setMethodName(method);
        webServiceRequestParam.setServiceUrl(getResources().getString(R.string.webserviceUrl));
        webServiceRequestParam.setServiceNameSpace(getResources().getString(R.string.webserviceNameSpace));

        webServiceRequestParam.setRequestProperty(mapParams);

        WerServiceAysTask temptask =  WebServiceTaskFactory.createWebServiceTaskAndExcute(this, taskId, webServiceRequestParam, this);

        //以下操作供取消的时候使用，设置了当前task就可以找到当前的task，就可以取消了
        if (taskMap==null){
            taskMap = new HashMap<Integer,WerServiceAysTask>();
        }
        taskMap.put(taskId,temptask);
        currentTaskId = taskId;
    }
    /*任务模式工具类end*/

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
