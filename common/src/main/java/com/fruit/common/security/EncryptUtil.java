package com.fruit.common.security;

import android.util.Base64;

import java.util.Calendar;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author liyc
 * @time 2014-8-6 上午9:26:56
 * @annotation 
 */
public class EncryptUtil {

	public static String getToken(String appKey,String appSecret)
	{
		try{
			String encodeFormat = "UTF-8";
			String algorithmName = "HmacSHA1";
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, 2);
			String p1 = String.valueOf(cal.getTime().getTime()).substring(0, 10);
			String p2 = Base64.encodeToString(p1.getBytes(encodeFormat), Base64.DEFAULT).trim();
			SecretKeySpec signingKey = new SecretKeySpec(appSecret.getBytes(encodeFormat), algorithmName);
			Mac mac = Mac.getInstance(algorithmName);
			mac.init(signingKey);
			mac.update(p2.getBytes(encodeFormat));
			byte[] b = mac.doFinal();
			String p4 = Base64.encodeToString(b, Base64.DEFAULT).trim();
			String token = appKey+"@"+p4+"@"+p2;
			return token;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
