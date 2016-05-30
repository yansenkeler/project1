package com.fruit.widget.progressDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fruit.widget.R;

/**
 * Created by chenliang on 15/7/23.
 */
public class SimpleProgressDialog extends AppCompatDialog{

    private TextView tipTextView;

    public SimpleProgressDialog(Context context) {
        super(context,R.style.loading_dialog);
    }

    public SimpleProgressDialog(Context context, int theme) {
        super(context, theme);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_progress, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);

        this.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

    }

    protected SimpleProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setMsg(String msg){
        tipTextView.setText(msg);
    }

    /**
     * 得到自定义的progressDialog
     * @param context
     * @return
     */
    public static SimpleProgressDialog createLoadingDialog(Context context) {
        SimpleProgressDialog loadingDialog = new SimpleProgressDialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        return loadingDialog;

    }
}
