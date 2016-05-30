package com.fruit.common.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fruit.common.res.ResUtils;


/**
 * @author liyc
 * @time 2014年5月12日 下午5:39:05
 * @annotation 
 */
public class UIUtils {
	
	/**
	 * 显示输入对话框
	 * @param con
	 * @param title
	 * @param posButton
	 * @param l
	 */
	public static void showInputDialog(Activity con,String title,String posButton,final IEpointInputDialog l)
	{
		LayoutInflater factory = LayoutInflater.from(con);
		final View textEntryView = factory.inflate(ResUtils.getInstance().getLayoutInt("ead_common_inputdialog"), null);
		final EditText etInput = (EditText) textEntryView.findViewById(ResUtils.getInstance().getIdInt("etInput"));
		AlertDialog dlg  = new AlertDialog.Builder(con)
		.setTitle(title)
		.setView(textEntryView).setPositiveButton(posButton, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				l.submit(etInput.getText().toString());
			}
		})
		.setNegativeButton("取消", null)
		.create();
		dlg.setCanceledOnTouchOutside(false);
		dlg.show();
		con.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		InputMethodManager imm = (InputMethodManager)
				con.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(etInput, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}
	
	/*
	 * 得到自定义的progressDialog
	 * @param context
	 * @param msg
	 * @return
	 */
	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(ResUtils.getInstance().getLayoutInt("loading_dialog"), null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(ResUtils.getInstance().getIdInt("dialog_view"));// 加载布局
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(ResUtils.getInstance().getIdInt("img"));
		TextView tipTextView = (TextView) v.findViewById(ResUtils.getInstance().getIdInt("tipTextView"));// 提示文字
		if(msg==null||"".equals(msg))
		{
			tipTextView.setVisibility(View.GONE);
		}
		else
		{
			tipTextView.setText(msg);// 设置加载信息
		}
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
				context, ResUtils.getInstance().getAnimInt("loading_animation"));
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		Dialog loadingDialog = new Dialog(context, ResUtils.getInstance().getStyleInt("loading_dialog"));// 创建自定义样式dialog
		loadingDialog.setCanceledOnTouchOutside(false);
		loadingDialog.setCancelable(true);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
		return loadingDialog;
	}
	
	public interface IEpointInputDialog {
		
		public void submit(String text);
		
	}

    /*获取频幕宽度*/
    public static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    // 方法一
    public static float getRawSize(Context context, int unit, float value) {
        Resources res = context.getResources();
        return TypedValue.applyDimension(unit, value, res.getDisplayMetrics());
    }

    // 方法2，需先在values中dimens的进行设置
    public static int getIntFromDimens(Context context ,int index) {
        int result = context.getResources().getDimensionPixelSize(index);
        return result;
    }

}
