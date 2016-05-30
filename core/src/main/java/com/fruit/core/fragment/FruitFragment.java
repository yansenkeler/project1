package com.fruit.core.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.volley.VolleyError;
import com.fruit.common.ui.ToastUtil;
import com.fruit.core.R;
import com.fruit.core.api.VolleyResponse;
import com.fruit.widget.progressDialog.SimpleProgressDialog;

import org.simple.eventbus.EventBus;

/**
 * http请求 fragment基础类
 * Created by liangchen on 15/7/7.
 */
public abstract class FruitFragment extends Fragment implements View.OnClickListener, VolleyResponse{

    @Override
    public void onClick(View v) {
        onFruitClick(v.getId());
    }

    @Override
    public void onResponse(Object response, int taskid) {
        dealSuccessResult(response, taskid);
    }

    @Override
    public void onErrorResponse(VolleyError error, int taskid) {
        dealFailResult(error, taskid);
    }

    public abstract void onFruitClick(int id);

    public String getUrl(){
        Activity activity = getActivity();
        Resources r = activity.getResources();
        return r.getString(R.string.Fruit_URL);
    }

    /**
     * 请求成功返回数据处理
     * @param data
     * @param taskid
     */
    public void dealSuccessResult(Object data, int taskid){

    }

    public void dealFailResult(VolleyError returnString, int taskid){
        if(returnString!=null && returnString.networkResponse.statusCode!=200){
            ToastUtil.showShort(getActivity(), "网络错误");
        }
    }

    //悬浮加载进度条相关处理
    SimpleProgressDialog dialog;
    boolean dialogIsShow;

    public void setDialog(String dialogmeg, Context mContext){
        if (dialog==null){
            dialog = SimpleProgressDialog.createLoadingDialog(mContext);
        }
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        dialog.setMsg(dialogmeg);
    }

    public void showDialog(String dialogmeg, Context mContext){
        if (!dialogIsShow) {
            setDialog(dialogmeg, mContext);
            dialog.show();
            dialogIsShow = true;
        }
    }

    public void hideProgressDialog(){
        if (dialogIsShow && dialog!=null) {
            dialog.dismiss();
            dialogIsShow = false;
            dialog = null;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
