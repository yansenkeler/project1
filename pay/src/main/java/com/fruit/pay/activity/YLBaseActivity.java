package com.fruit.pay.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.fruit.core.activity.templet.NaviProgressWebServiceActivity;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

public abstract class YLBaseActivity extends NaviProgressWebServiceActivity {
    public static final String LOG_TAG = "YLBasePay";

    /*****************************************************************
     * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
     *****************************************************************/
    public final String mMode = "00";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*************************************************
         * 步骤3：处理银联手机支付控件返回的支付结果
         ************************************************/
        if (data == null) {
            return;
        }

        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        final String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        // builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (str.equalsIgnoreCase("success")) {
                    paySuccess();
                } else if (str.equalsIgnoreCase("fail")) {
                    payFail();
                } else if (str.equalsIgnoreCase("cancel")) {
                    payCancle();
                }
            }
        });
        builder.create().show();
    }

    public abstract void paySuccess();

    public abstract void payFail();

    public abstract void payCancle();

    public void startPay(String tn){
        doStartUnionPayPlugin(this,tn,mMode);
    }

    public void doStartUnionPayPlugin(Activity activity, String tn, String mode) {
        UPPayAssistEx.startPayByJAR(activity, PayActivity.class, null, null,
                tn, mode);
    }

}
