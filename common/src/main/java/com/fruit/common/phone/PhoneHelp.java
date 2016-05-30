package com.fruit.common.phone;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

/**
 * @author liyc
 * @time 2012-3-21 上午8:12:32
 * @annotation
 */
public class PhoneHelp {

	/**
	 * 电话拨打
	 */
	public static void Call(Context con, String phonenum) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + phonenum));
		con.startActivity(intent);
	}

	/*
	 * 电话状态： 1.tm.CALL_STATE_IDLE=0 无活动 2.tm.CALL_STATE_RINGING=1 响铃
	 * 3.tm.CALL_STATE_OFFHOOK=2 摘机
	 */
	public static int getCallState(Context con) {
		TelephonyManager tm = (TelephonyManager) con
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getCallState();
	}

	/*
	 * 电话方位：
	 */
	public static CellLocation getCellLocation(Context con) {
		TelephonyManager tm = (TelephonyManager) con
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getCellLocation();
	}

	/*
	 * 唯一的设备ID： GSM手机的 IMEI 和 CDMA手机的 MEID. Return null if device ID is not
	 * available.
	 */
	public static String getDeviceId(Context con) {
		TelephonyManager tm = (TelephonyManager) con
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (null == tm.getDeviceId() || "".equals(tm.getDeviceId())) {
			return getMacAddress(con);
		} else {
			return tm.getDeviceId();
		}

	}

	/**
	 * 获取系统版本号
	 */
	public static String getVersionName(Context con) {
		PackageManager packageManager = con.getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(con.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		String version = packInfo.versionName;
		return version;
	}

	public static String getPackageName(Context con) {
		return getPackageInfo(con).packageName;
	}

	public static PackageInfo getPackageInfo(Context con) {
		PackageManager packageManager = con.getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(con.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return packInfo;
	}

	public static void sendMsgUsePhoneSelf(Context con, String phonenum,
			String body) {
		Uri smsToUri = Uri.parse("smsto:" + phonenum);
		Intent mIntent = new Intent(Intent.ACTION_SENDTO,
				smsToUri);
		mIntent.putExtra("sms_body", body);
		con.startActivity(mIntent);
	}

	// 调用系统浏览器
	public static void invokeSystemBrowser(Context con, String url) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(url);
		intent.setData(content_url);
		con.startActivity(intent);

		/*
		 * Uri u = Uri.parse(url);
		 * 
		 * Intent it = new Intent();
		 * 
		 * it.setData(u);
		 * 
		 * it.setAction( Intent.ACTION_VIEW);
		 * 
		 * it.setClassName("keler.android.browser",
		 * "keler.android.browser.BrowserActivity");
		 * 
		 * startActivity(it);
		 */
	}

	// 获取屏幕宽度
	public static int getPhoneWidth(Context con) {
		WindowManager wm = (WindowManager) con
				.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
	}

	// 获取屏幕高度
	public static int getPhoneHeight(Context con) {
		WindowManager wm = (WindowManager) con
				.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getHeight();
	}

	/**
	 * 获取mac 地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getMacAddress(Context context) {
		String result = "";
		try {
			WifiManager wifiManager = (WifiManager) context
					.getSystemService(context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			result = wifiInfo.getMacAddress();
			return result;
		} catch (Exception e) {
		}
		return "";
	}
	
	/**
	 * 判断SD卡是否存在
	 * @return
	 */
	public static boolean ExistSDCard() {  
		if (android.os.Environment.getExternalStorageState().equals(  
				android.os.Environment.MEDIA_MOUNTED)) {  
			return true;  
		} else  
			return false;  
	} 
	
	/**
	 * 判断某个应用在系统是否有安装
	 * @param con
	 * @param packagename
	 * @return
	 */
	public static boolean ExistAppInSystem(Context con,String packagename)
	{
		PackageInfo packageInfo;
        try {
            packageInfo = con.getPackageManager().getPackageInfo(packagename, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo==null?false:true;
	}
}
