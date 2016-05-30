package com.fruit.core.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;

/**
 * http图片上传工具类
 * Created by user on 2016/2/29.
 */
public class HttpUploadManager {
    public static final int UPLOAD_SUCCESS_CODE = 1;
    public static final int UPLOAD_SERVER_ERROR_CODE = 2;
    public static final int UPLOAD_INIT = 3;
    public static final int UPLOAD_PROCESS = 4;

    private static HttpUploadManager mHttpManager = null;
    private OnUploadProcessListener mListener = null;

    public HttpUploadManager(OnUploadProcessListener mListener) {
        this.mListener = mListener;
    }


    /**
     * 上传文件到指定地址
     * @param httpUrl 上传地址
     * @param uploadFile 上传文件file
     * @param fileKey 文件名
     */
    public void uploadFile(final String httpUrl, final File uploadFile, final String fileKey)
    {
        new Thread(){
            @Override
            public void run() {
                super.run();
                String end ="\r\n";
                String twoHyphens ="--";
                String boundary ="*****";

                String BOUNDARY = UUID.randomUUID().toString(); //边界标识 随机生成
                String PREFIX = "--" , LINE_END = "\r\n";
                String CONTENT_TYPE = "multipart/form-data"; //内容类型
                int TIME_OUT = 10*10000000; //超时时间
                String CHARSET = "utf-8"; //设置编码
                try
                {
                    URL url =new URL(httpUrl);
                    HttpURLConnection con=(HttpURLConnection)url.openConnection();
                    /* 允许Input、Output，不使用Cache */
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    /* 设置传送的method=POST */
                    con.setRequestMethod("POST");
                    /* setRequestProperty */
                    con.setRequestProperty("Connection", "Keep-Alive");
                    con.setRequestProperty("Charset", "UTF-8");
                    con.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary="+boundary);
                    /* 设置DataOutputStream */
                    DataOutputStream ds = new DataOutputStream(con.getOutputStream());
                    /**
                     * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                     * filename是文件的名字，包含后缀名的 比如:abc.png
                     */
                    ds.writeBytes(twoHyphens + boundary + end);
                    ds.writeBytes("Content-Disposition: form-data; "+
                            "name=\""+fileKey+"\";filename=\""+
                            uploadFile.getName() +"\""+ end);
                    ds.writeBytes(end);

                    //开始上传文件
                    /* 取得文件的FileInputStream */
                    FileInputStream fStream = new FileInputStream(uploadFile);

                    Message msg = new Message();
                    msg.what = UPLOAD_INIT;
                    msg.arg1 = (int) uploadFile.length();
                    uploadImageHandler.sendMessage(msg);
                    /* 设置每次写入1024bytes */
                    int bufferSize =1024;
                    byte[] buffer =new byte[bufferSize];
                    int length =-1;
                    int curLen = 0;
                    /* 从文件读取数据至缓冲区 */
                    length = fStream.read(buffer);
                    while(length !=-1)
                    {
                        /* 将资料写入DataOutputStream中 */
                        curLen += length;
                        ds.write(buffer, 0, length);
                        Message msg1 = new Message();
                        msg1.what = UPLOAD_PROCESS;
                        msg1.arg1 = curLen;
                        uploadImageHandler.sendMessage(msg1);
                        length = fStream.read(buffer);
                    }
                    ds.writeBytes(end);
                    ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
                    /* close streams */
                    fStream.close();
                    ds.flush();
                    /* 取得Response内容 */
                    InputStream is = con.getInputStream();
                    int statusCode = con.getResponseCode();
                    int ch;
                    StringBuffer b =new StringBuffer();
                    BufferedReader mBufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    while( ( ch = mBufferedReader.read() ) !=-1 )
                    {
                        b.append( (char)ch );
                    }
                    if (statusCode==200){
                        Message msg2 = new Message();
                        msg2.what = UPLOAD_SUCCESS_CODE;
                        msg2.obj = b.toString();
                        uploadImageHandler.sendMessage(msg2);
                    }else {
                        Message msg3 = new Message();
                        msg3.what = UPLOAD_SERVER_ERROR_CODE;
                        msg3.obj = b.toString();
                        msg3.arg1 = statusCode;
                        uploadImageHandler.sendMessage(msg3);
                    }
                    ds.close();
                }
                catch(Exception e)
                {
                    Message msg = new Message();
                    msg.what = UPLOAD_SERVER_ERROR_CODE;
                    msg.obj = e.getMessage();
                    uploadImageHandler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }


    /**
     * 上传文件到指定地址
     * @param httpUrl 上传地址
     * @param uploadFile 上传文件file
     * @param fileKey 文件名
     */
    public void uploadFileWithParams(final String httpUrl, final File uploadFile, final String fileKey , final HashMap<String,String> params)
    {
        new Thread(){
            @Override
            public void run() {
                super.run();
                String end ="\r\n";
                String twoHyphens ="--";
                String boundary = Long.toHexString(System.currentTimeMillis());

                String CONTENT_TYPE = "multipart/form-data"; //内容类型
                int TIME_OUT = 10*10000000; //超时时间
                String CHARSET = "utf-8"; //设置编码
                try
                {
                    URL url =new URL(httpUrl);
                    HttpURLConnection con=(HttpURLConnection)url.openConnection();
                    /* 允许Input、Output，不使用Cache */
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    /* 设置传送的method=POST */
                    con.setRequestMethod("POST");
                    /* setRequestProperty */
                    con.setRequestProperty("Connection", "Keep-Alive");
                    con.setRequestProperty("Charset", CHARSET);
                    con.setRequestProperty("Content-Type",
                            CONTENT_TYPE+";boundary="+boundary);
                    /* 设置DataOutputStream */
                    DataOutputStream ds = new DataOutputStream(con.getOutputStream());

                    StringBuffer paramString =  new StringBuffer();

                    for (String key : params.keySet()){
                        paramString.append( "Content-Type: text/plain" + end
                                + "Content-Disposition: form-data; name=\""+key+"\"" + end + end
                                + params.get(key) + end
                                + "--" + boundary + "--");
                    }
                    /**
                     * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                     * filename是文件的名字，包含后缀名的 比如:abc.png
                     */
                    ds.writeBytes("Content-Disposition: form-data; "+
                            "name=\""+fileKey+"\";filename=\""+
                            uploadFile.getName() +"\""+ end);
                    ds.writeBytes(twoHyphens + boundary + end);
                    ds.writeBytes(paramString.toString());

                    ds.writeBytes(end);

                    //开始上传文件
                    /* 取得文件的FileInputStream */
                    FileInputStream fStream = new FileInputStream(uploadFile);

                    Message msg = new Message();
                    msg.what = UPLOAD_INIT;
                    msg.arg1 = (int) uploadFile.length();
                    uploadImageHandler.sendMessage(msg);
                    /* 设置每次写入1024bytes */
                    int bufferSize =1024;
                    byte[] buffer =new byte[bufferSize];
                    int length =-1;
                    int curLen = 0;
                    /* 从文件读取数据至缓冲区 */
                    length = fStream.read(buffer);
                    while(length !=-1)
                    {
                        /* 将资料写入DataOutputStream中 */
                        curLen += length;
                        ds.write(buffer, 0, length);
                        Message msg1 = new Message();
                        msg1.what = UPLOAD_PROCESS;
                        msg1.arg1 = curLen;
                        uploadImageHandler.sendMessage(msg1);
                        length = fStream.read(buffer);
                    }
                    ds.writeBytes(end);
                    ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
                    /* close streams */
                    fStream.close();
                    ds.flush();
                    /* 取得Response内容 */
                    InputStream is = con.getInputStream();
                    int statusCode = con.getResponseCode();
                    int ch;
                    StringBuffer b =new StringBuffer();
                    BufferedReader mBufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    while( ( ch = mBufferedReader.read() ) !=-1 )
                    {
                        b.append( (char)ch );
                    }
                    if (statusCode==200){
                        Message msg2 = new Message();
                        msg2.what = UPLOAD_SUCCESS_CODE;
                        msg2.obj = b.toString();
                        uploadImageHandler.sendMessage(msg2);
                    }else {
                        Message msg3 = new Message();
                        msg3.what = UPLOAD_SERVER_ERROR_CODE;
                        msg3.obj = b.toString();
                        msg3.arg1 = statusCode;
                        uploadImageHandler.sendMessage(msg3);
                    }
                    ds.close();
                }
                catch(Exception e)
                {
                    Message msg = new Message();
                    msg.what = UPLOAD_SERVER_ERROR_CODE;
                    msg.obj = e.getMessage();
                    uploadImageHandler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler uploadImageHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPLOAD_SUCCESS_CODE:
                    mListener.onUploadDone(UPLOAD_SUCCESS_CODE, (String) msg.obj);
                    break;
                case UPLOAD_SERVER_ERROR_CODE:
                    if (msg.arg1>0){
                        mListener.onUploadDone(UPLOAD_SERVER_ERROR_CODE, (String) msg.obj+";statusCode:"+msg.arg1);
                    }else {
                        mListener.onUploadDone(UPLOAD_SERVER_ERROR_CODE, (String) msg.obj);
                    }
                    break;
                case UPLOAD_INIT:
                    mListener.initUpload(msg.arg1);
                    break;
                case UPLOAD_PROCESS:
                    mListener.onUploadProcess(msg.arg1);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 图片上传回调接口
     */
    public static interface OnUploadProcessListener {
        /**
         * 上传响应
         * @param responseCode
         * @param message
         */
        void onUploadDone(int responseCode, String message);
        /**
         * 上传中
         * @param uploadSize
         */
        void onUploadProcess(int uploadSize);
        /**
         * 准备上传
         * @param fileSize
         */
        void initUpload(int fileSize);
    }
}
