package com.fruit.core.fragment.basic;

import android.content.DialogInterface;
import android.os.Bundle;

import com.fruit.common.ui.ToastUtil;
import com.fruit.core.fragment.FruitWebServiceFragment;
import com.fruit.core.webservice.WerServiceAysTask;
import com.fruit.widget.progressDialog.SimpleProgressDialog;

/**
 * Created by liangchen on 15/7/7.
 */
public class BaseProgressWebServiceFragment extends FruitWebServiceFragment{


    /*加载进度条处理*/
    SimpleProgressDialog dialog;
    boolean dialogIsShow;

    public void setDialog(String dialogmeg){

        if (dialog==null){
            dialog = SimpleProgressDialog.createLoadingDialog(getActivity());
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancleTask();
                    dialog.dismiss();
                }
            });
        }
        dialog.setMsg(dialogmeg);
    }

    public void showDialog(String dialogmeg){
        setDialog(dialogmeg);
        if (!dialogIsShow) {

            //在弹出窗口之前用Activity的isFinishing判断一下Activity是否还存在:
            if (!getActivity().isFinishing()) {
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void RequestOnStart(int taskid) {
        showDialog("加载中...");
    }

    @Override
    public void DealFailureResult(Object returnObject, int taskid) {
        hideProgressDialog();
        ToastUtil.showShort(getActivity(), "网络异常");
    }

    @Override
    public void DealSuccessResult(Object returnObject, int taskid) {

    }

}
