package com.fruit.client.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fruit.client.activity.EventDetailEditActivity;
import com.fruit.common.push.FruitPushMessageReceiver;
import com.fruit.common.ui.ToastUtil;
import com.fruit.core.db.DBUtil;

import java.util.List;

/**
 * Created by user on 2015/11/9.
 */
public class BdPushReceiver  extends FruitPushMessageReceiver{
    private static final String D = BdPushReceiver.class.getSimpleName();

    @Override
    public void onBind(Context context, int errorCode, String appid, String userId, String channelId, String requestId) {
        super.onBind(context, errorCode, appid, userId, channelId, requestId);
        Log.d(D, "errorCode-" + errorCode + ";appid-" + appid + ";userId-" + userId + ";channelId-" + channelId + ";requestId-" + requestId);
        if (errorCode==0){
            DBUtil.setConfigValue("channelId", channelId);
            Log.d("channelid", "bind success channelid: "+DBUtil.getConfigValue("channelId"));
        }
    }

    @Override
    public void onUnbind(Context context, int i, String s) {
        super.onUnbind(context, i, s);
    }

    @Override
    public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {
        super.onSetTags(context, i, list, list1, s);
    }

    @Override
    public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {
        super.onDelTags(context, i, list, list1, s);
    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {
        super.onListTags(context, i, list, s);
    }

    @Override
    public void onMessage(final Context context, final String message, String customContentString) {
        super.onMessage(context, message, customContentString);
        ToastUtil.showShort(context.getApplicationContext(), "接收到透传消息:"+message+" "+customContentString);
    }

    @Override
    public void onNotificationClicked(Context context, String title, String content, String jsonString) {
        super.onNotificationClicked(context, title, content, jsonString);
        Log.d(D + "onNotificationClicked: ", title + ";" + content + ";" + jsonString);
        if (jsonString!=null){
            JSONObject jsonObject = JSON.parseObject(jsonString);
            String billNoValue = jsonObject.getString("billno");
            String msgPkValue = jsonObject.getString("msgpk");
            Intent intent = new Intent();
            intent.setClass(context.getApplicationContext(), EventDetailEditActivity.class);
            intent.putExtra("bill_no", billNoValue);
            intent.putExtra("msg_pk", msgPkValue);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startActivity(intent);
//            DBUtil.setIsRead(billNoValue, true);
        }
    }

    @Override
    public void onNotificationArrived(Context context, String title,  String content, String jsonString) {
        super.onNotificationArrived(context, title, content, jsonString);
        Log.d(D + "onNotificationArrived", title + ";" + content + ";" + jsonString);
        if (jsonString!=null){
            JSONObject jsonObject = JSON.parseObject(jsonString);
            String billNoValue = jsonObject.getString("billno");
            String msgPkValue = jsonObject.getString("msgpk");
//            DBUtil.setMsg(title, content, billNoValue);
        }
    }
}
