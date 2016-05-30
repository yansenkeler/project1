package com.fruit.common.phone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 通用方法
 * @author liyc
 */
public class Service {

	/**
	 * 网络信号的检测
	 * @param context
	 * @return
	 */

	public static boolean checkNet(Context context)
	{// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理） 
		try { 
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) { 
				// 获取网络连接管理的对象 
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null&& info.isConnected()) { 
					// 判断当前网络是否已经连接 
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true; 
					}       
				}       
			} 
		} catch (Exception e){
		} 
		return false; 
	}


	public static void AlertWithLayout(Context con,int layout,int icon){
		LayoutInflater inflater = LayoutInflater.from(con);
		View view = inflater.inflate(layout, null);
		AlertDialog ad = new AlertDialog.Builder(con)
		.setTitle("欢迎登录")
		.setView(view)
		.setPositiveButton("确定", null)
		.setNegativeButton("取消", null)
		.create();
		ad.show();
	}

	/**
	 * 电话拨打
	 * @param con
	 * @param phonenum
	 */
	public static void Call(Context con,String phonenum){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.CALL");
		intent.setData(Uri.parse("tel:" + phonenum));
		con.startActivity(intent);
	}

	/**
	 * 发送短信
	 * @param con
	 * @param phonenum
	 * @param shortmsg
	 */
	public static void SendMsg(Context con,String phonenum,
			String shortmsg) {
		SmsManager smsMag = SmsManager.getDefault();
		List<String> texts = smsMag.divideMessage(shortmsg);
		for(String text:texts){
			smsMag.sendTextMessage(phonenum, null, text, null, null);
		}
	}

	/**
	 * android获取一个用于打开HTML文件的intent
	 * @param param
	 * @return
	 */
	public static Intent getHtmlFileIntent(String param)
	{
		Uri uri = Uri.parse(param).buildUpon().encodedAuthority("keler.android.htmlfileprovider").scheme("content").encodedPath(param ).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri, "text/html");
		return intent;
	}

	/**
	 * android获取一个用于打开图片文件的intent
	 * @param param
	 * @return
	 */
	public static Intent getImageFileIntent(String param)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "image/*");
		return intent;
	}

	/**
	 * android获取一个用于打开PDF文件的intent
	 * @param param
	 * @return
	 */
	public static Intent getPdfFileIntent(String param)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/pdf");
		return intent;
	}

	/**
	 * android获取一个用于打开文本文件的intent
	 * @param param
	 * @param paramBoolean
	 * @return
	 */
	public static Intent getTextFileIntent(String param, boolean paramBoolean)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (paramBoolean)
		{
			Uri uri1 = Uri.parse(param);
			intent.setDataAndType(uri1, "text/plain");
		}
		else
		{
			Uri uri2 = Uri.fromFile(new File(param));
			intent.setDataAndType(uri2, "text/plain");
		}

		return intent;
	}

	/**
	 * android获取一个用于打开音频文件的intent
	 * @param param
	 * @return
	 */
	public static Intent getAudioFileIntent(String param)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "audio/*");
		return intent;
	}

	/**
	 * android获取一个用于打开视频文件的intent
	 * @param param
	 * @return
	 */
	public static Intent getVideoFileIntent(String param)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "video/*");
		return intent;
	}

	//android获取一个用于打开CHM文件的intent
	public static Intent getChmFileIntent(String param)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/x-chm");
		return intent;
	}

	/**
	 * android获取一个用于打开Word文件的intent
	 * @param param
	 * @return
	 */
	public static Intent getWordFileIntent(String param)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/msword");
		return intent;
	}

	/**
	 * android获取一个用于打开Excel文件的intent
	 * @param param
	 * @return
	 */
	public static Intent getExcelFileIntent(String param,Context con)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.ms-excel");
		return intent;
	}

	/**
	 * android获取一个用于打开PPT文件的intent
	 * @param param
	 * @return
	 */
	public static Intent getPptFileIntent(String param)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		return intent;
	}

	public static Intent getRARFileIntent(String param){
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/x-rar-compressed");
				intent.setAction("keler.asrazpaid");
		System.out.println("uncompass RAR");
		return intent;
	}

	/**寻找适合的程序打开文件*/
	@SuppressWarnings("unused")
	private String tag = "FileDialog";
	public final static boolean D = false;
	public static void doOpenFile2(Context con,String attachfilepath){
		String suffix = attachfilepath.substring(attachfilepath.lastIndexOf("."),attachfilepath.length()).toLowerCase();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		String param = "file://" + attachfilepath;
		Uri uri = Uri.parse(param);
		String type = null;
		type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
				MimeTypeMap.getFileExtensionFromUrl(attachfilepath));
		System.out.println("filetype:"+type);
		if (type == null) {
			String tmp = attachfilepath.toLowerCase();
			if (tmp.endsWith("mp3") || tmp.endsWith("wav") || tmp.endsWith("wma")) {
				type = "audio/*";
			} else if (tmp.endsWith("apk")) {
				type = "application/vnd.android.package-archive";
			}
		}
		if(type != null&&!suffix.equals(".rar")){
			intent.setDataAndType(uri, type);

			try {
				((Activity) con).startActivityForResult(intent, 1);
			} catch (ActivityNotFoundException e){
				Toast.makeText(con,
						"文件不能打开",
						Toast.LENGTH_SHORT).show();
			}
		} 
		else {
			try{
				Intent intent1 = null;

				if(suffix.equals(".jpg")||suffix.equals(".png")||suffix.equals(".gif")||suffix.equals(".bmp")){
					intent1 = Service.getImageFileIntent(attachfilepath);
				}
				else if(suffix.equals(".pdf")){
					intent1  = Service.getPdfFileIntent(attachfilepath);
				}
				else if(suffix.equals(".doc")||suffix.equals(".docx")){
					intent1  = Service.getWordFileIntent(attachfilepath);
				}
				else if(suffix.equals(".xls")||suffix.equals(".xlsx")){
					intent1  = Service.getExcelFileIntent(attachfilepath,con);
				}
				else if(suffix.equals(".mp3")||suffix.equals(".wma")||suffix.equals(".mp4")||suffix.equals(".wav")){
					intent1  = Service.getAudioFileIntent(attachfilepath);
				}
				else if(suffix.equals(".txt")){
					intent1  = Service.getTextFileIntent(attachfilepath, false);
				}
				else if(suffix.equals(".html")){
					intent1  = Service.getHtmlFileIntent(attachfilepath);
				}
				else if(suffix.equals(".ppt")||suffix.equals(".pptx")){
					intent1  = Service.getPptFileIntent(attachfilepath);
				}
				else if(suffix.equals(".rar")||suffix.equals(".zip")){
					intent1  = Service.getRARFileIntent(attachfilepath);
				}
				con.startActivity(intent1);
			}catch(Exception e){
				Toast.makeText(con, "没有适合的程序打开此文件", 1000).show();
				System.out.println((e.getMessage()==null?"":"\n"+e.getMessage()));
			}
		}
	}



	public static void playMusic(Context con,String filename) throws IOException {
		MediaPlayer m = new MediaPlayer();
		AssetFileDescriptor descriptor = con.getAssets().openFd(
				filename);
		m.setDataSource(descriptor.getFileDescriptor(),
				descriptor.getStartOffset(), descriptor.getLength());
		descriptor.close();
		m.prepare();
		m.start();
	}

	public static void deleteFile(File file){
		if(file.exists()){                    //判断文件是否存在
			if(file.isFile()){                    //判断是否是文件
				file.delete();                       //delete()方法 你应该知道 是删除的意思;
			}else if(file.isDirectory()){              //否则如果它是一个目录
				File files[] = file.listFiles();               //声明目录下所有的文件 files[];
				for(int i=0;i<files.length;i++){            //遍历目录下所有的文件
					deleteFile(files[i]);             //把每个文件 用这个方法进行迭代
				} 
			} 
			file.delete(); 
		}else{ 
			System.out.println("所删除的文件不存在！"+'\n');
		} 
	}

	/**
	 * 跳转到另一个Activity
	 */
	public static void intentView(Activity con,Class<?> disClass)
	{
		Intent intent = new Intent(con,disClass);
		con.startActivity(intent);
	}
	
	/**
	 * 跳转到另一个Activity
	 */
	public static void intentViewFinish(Activity con,Class<?> disClass)
	{
		Intent intent = new Intent(con,disClass);
		con.startActivity(intent);
		con.finish();
	}
	
	/**
	 * 跳转到另一个Activity
	 */
	public static void intentViewWithTitle(Activity con,Class<?> disClass,String title)
	{
		Intent intent = new Intent(con,disClass);
		intent.putExtra("viewtitle", title);
		con.startActivity(intent);
	}
}
