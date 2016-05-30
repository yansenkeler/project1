package com.fruit.common.application;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;


import com.fruit.common.date.DateUtil;
import com.fruit.common.io.IOHelp;
import com.fruit.common.res.ResUtils;
import com.fruit.common.security.EncryptUtil;

import java.util.Date;
import java.util.HashMap;

/**
 * @author liangchen
 * @time 2014-4-29 下午4:42:23
 * @annotation 
 */
public class ApplicationUtils {

	private static final String TAG = ApplicationUtils.class.getSimpleName();

	/**
	 * 获取应用上线文
	 * @return
	 */
	public static Context getAppContext()
	{
		return FrameApplication.getInstance();
	}

	/**
	 * 判断当前应用版本是否手机版
	 * @return
	 */
	public static boolean isPhone()
	{
		if(getAppContext().getString(ResUtils.getInstance().getStringInt("deviceType")).equals("1"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static void appToast(String msg)
	{
		Toast.makeText(getAppContext(), msg, Toast.LENGTH_LONG).show();
	}

	public static HashMap<String, Object> getPassMap(Activity act)
	{
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("MobileManagerEnable",getResString("MobileManagerEnable"));
		params.put("Epoint_MobileManager_URL",getResString("Epoint_MobileManager_URL"));
		params.put("Context", act);
//		params.put(ConfigKey.userguid, DBFrameUtil.getConfigValue(ConfigKey.userguid));
		params.put("validata", getToken());
		params.put("namespace", getResString("Epoint_DNet_WS_NameSpace"));
//		params.put(ConfigKey.userguid,DBFrameUtil.getConfigValue(ConfigKey.userguid));
//		params.put(ConfigKey.Epoint_MobileOAWebService_URL,DBFrameUtil.getConfigValue(ConfigKey.Epoint_MobileOAWebService_URL));
		return params;
	}

	/**
	 * 获取中间平台URL
	 * @return
	 */
	public static String getMPUrl()
	{
		return ApplicationUtils.getAppContext().getString(ResUtils.getInstance().getStringInt("Epoint_MobileManager_URL"));
	}

	/**
	 * 获取中间平台PlatformService URL
	 * @return
	 */
	public static String getPlatformServiceUrl()
	{
		return ApplicationUtils.getAppContext().getString(ResUtils.getInstance().getStringInt("Epoint_MobileManager_URL")).replace("MobileOaManage", "PlatformService");
	}


	public static String getAppPrivateName(String subName)
	{
		return getAppContext().getPackageName()+"."+subName;
	}

	public static String getCloseAppBroadcastReceiverName()
	{
		return getAppPrivateName("EpointCloseAppBroadcastReceiver");
	}

	public static String getStopMqttServiceReceiver()
	{
		return getAppPrivateName("EpointStopMqttServiceReceiver");
	}

	public static String getMqttLostBroadcastReceiverName()
	{
		return getAppPrivateName("EpointMqttLostBroadcastReceiver");
	}

	public static String getStartMqttInitReceiverName()
	{
		return getAppPrivateName("EpointStartMqttInitReceiver");
	}

	/**
	 * 获取应用附件存储路径
	 * @return
	 */
	public static String getAttachStoragePath()
	{
		return IOHelp.getAttachStoragePath();
	}

	public static void saveErrorLog(Throwable ex)
	{
		String info = IOHelp.throwException2String(ex);
		Log.e("FRAME_ERROR", ex.getMessage(), ex);
		String key = "ERROR"+ DateUtil.convertDate(new Date(), "yyyyMMddHHmm");
//		DBFrameUtil.setFrameCache(key, info);
	}

	public static String getToken()
	{
		return EncryptUtil.getToken(getResString("appKey"), getResString("appSecret")).replace("+", "-").replace("/", "_");
	}

	public static String getResString(String name)
	{
		return getAppContext().getString(ResUtils.getInstance().getStringInt(name));
	}


    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verName;
	}

}
