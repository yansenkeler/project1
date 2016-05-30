package com.fruit.core.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.VolleyError;
import com.fruit.common.ui.ToastUtil;
import com.fruit.core.R;
import com.fruit.core.api.VolleyResponse;
import com.fruit.widget.progressDialog.SimpleProgressDialog;

/**
 * Created by liangchen on 15/3/26.
 * 最高级的框架activity，继承此activity可自定义自己的二级activity。或者子activity
 * */
public class FruitActivity extends AppCompatActivity implements View.OnClickListener, VolleyResponse
{

    /*异步加载get方法的相关回调处理start
    * 使用CommonHttp请求结束后回调UiDealInterface的UIFresh，处理后分发到相应的方法，
    * 重新相关状态的方法即可，重写onstart方法之类的
    * */
//    @Override
//    public void UIFresh(int status, String errorcode,String returnString, int taskid) {
//        if (status== UiDealInterface.RESPONSEHANDLER_ONSTART){
//            RequestOnStart(returnString,taskid);
//        }else if(status== UiDealInterface.RESPONSEHANDLER_ONSUCCESS){
//            JsonObject jsonObject;
//            jsonObject = JSON.parseObject(returnString, JsonObject.class);
//                if (jsonObject.ret == 200) {
//                    DealSuccessResult(jsonObject.data, taskid);
//                }else if(jsonObject.ret == 202){
//                    switchToLogin();
//                } else {
//                    DealFailureResult(jsonObject.msg, taskid);
//                }
//        }else if(status== UiDealInterface.RESPONSEHANDLER_ONFAILURE){
//            DealFailureResult(returnString,taskid);
//        }else if(status== UiDealInterface.RESPONSEHANDLER_ONRETRY){
//
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private SharedPreferences prefs;

    public String getUrl(){
       return this.getString(R.string.Fruit_URL);
    }

    public static SharedPreferences preferences(Context paramContext)
    {
        return paramContext.getSharedPreferences(paramContext.getPackageName(), 3);
    }

    /**
     * 在onCreate方法里面获得不同类型的参数
     * @param paramString
     * @return
     */
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

    /**
     * 需要处理click事件的时候使用
     * @param view
     */
    public void onFruitActivityClick(View view){

    }

    /**
     * 悬浮加载进度条的处理
     */
    SimpleProgressDialog dialog;
    boolean dialogIsShow;

    public void setDialog(String dialogmeg){
        if (dialog==null){
            dialog = SimpleProgressDialog.createLoadingDialog(FruitActivity.this);
        }
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        dialog.setMsg(dialogmeg);
    }

    public void showDialog(String dialogmeg){
        if (!dialogIsShow) {
            setDialog(dialogmeg);
            dialog.show();
            dialogIsShow = true;
        }
    }

    public void hideProgressDialog(){
        if (dialogIsShow) {
            dialog.dismiss();
            dialogIsShow = false;
            dialog = null;
        }
    }

    /**
     * 请求失败
     * @param returnObject
     * @param taskid
     */
    public  void dealFailureResult(VolleyError returnObject, int taskid){
        if (returnObject==null){
            ToastUtil.showShort(this, "网络异常");
        }
    }

    /**
     * 请求成功返回数据
     * @param returnObject
     * @param taskid
     */
    public  void dealSuccessResult(Object returnObject, int taskid){

    }

    /**
     * Volley请求相关回调方法
     * @param response
     * @param taskid
     */
    @Override
    public void onResponse(Object response, int taskid) {
        dealSuccessResult(response, taskid);
    }

    @Override
    public void onErrorResponse(VolleyError error, int taskid) {
        dealFailureResult(error, taskid);
    }
}
