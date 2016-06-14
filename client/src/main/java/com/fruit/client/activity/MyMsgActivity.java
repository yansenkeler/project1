package com.fruit.client.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fruit.client.R;
import com.fruit.client.adapter.MyMsgAdapter;
import com.fruit.client.object.event.PushMessage;
import com.fruit.client.util.Constant;
import com.fruit.client.util.Urls;
import com.fruit.common.network.NetWorkUtil;
import com.fruit.common.ui.ToastUtil;
import com.fruit.core.activity.templet.NaviActivity;
import com.fruit.core.db.DBUtil;
import com.fruit.core.db.models.gen.msg;
import com.fruit.core.http.VolleyManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qianyx on 16-5-8.
 */
public class MyMsgActivity extends NaviActivity {

    private static final int TASK_GET_MSG = 0;
    private static final int TASK_DEL_MSG = 1;
    private ListView listView;
    private MyMsgAdapter adapter;

    private ArrayList<PushMessage> msgs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTitle("我的消息");
        setTopbarBackground(getResources().getColor(R.color.colorPrimary));
        listView = (ListView)findViewById(R.id.listview);

        adapter = new MyMsgAdapter(msgs, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PushMessage msg = msgs.get(position);
                Intent intent = new Intent(MyMsgActivity.this, EventDetailEditActivity.class);
                intent.putExtra("bill_no", msg.getBillNo());
                intent.putExtra("msg_pk", msg.getMsgPk());
                startActivity(intent);
//                DBUtil.setIsRead(msg.getBillno(), true);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,  int position, long id) {
                final String msgpk =  msgs.get(position).getMsgPk();
                new AlertDialog.Builder(MyMsgActivity.this)
                        .setTitle("提醒")
                        .setMessage("确认要删除这条消息?")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showDialog("正在删除消息");
                                delMsg(msgpk);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMsgs();
    }

    private void delMsg(String msgpk){
        Map<String, String> params = new HashMap<>();
        params.put("type", "delete");
        params.put("msgpk", msgpk);
        String s = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", s, params, Urls.ROOT+Urls.USER_MAG, Object.class,
                this, TASK_DEL_MSG);
    }

    private void getMsgs(){
        Map<String, String> params = new HashMap<>();
        params.put("type", "query");
        params.put("userno", DBUtil.getConfigValue("user_name"));
        String s = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", s, params, Urls.ROOT+Urls.USER_MAG, Object.class,
                this, TASK_GET_MSG);
    }


    @Override
    public void dealFailureResult(VolleyError returnObject, int taskid) {
        super.dealFailureResult(returnObject, taskid);
        switch (taskid){
            case TASK_GET_MSG:
                ToastUtil.showShort(this, "获取消息失败");
                break;
            case TASK_DEL_MSG:
                hideProgressDialog();
                ToastUtil.showShort(this, "删除消息失败");
                break;
            default:
                break;
        }
    }

    @Override
    public void dealSuccessResult(Object returnObject, int taskid) {
        super.dealSuccessResult(returnObject, taskid);
        switch (taskid){
            case TASK_GET_MSG:
                if (returnObject!=null){
                    JSONObject jsonObject = (JSONObject)returnObject;
                    String flag = jsonObject.getString("flag");
                    if (flag.equals("0000")){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObject1.getJSONArray("list");
                        msgs.clear();
                        for (int i=0; i<jsonArray.size(); i++){
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            PushMessage pushMessage = new PushMessage();
                            pushMessage.setBillNo(jsonObject2.getString("billNo"));
                            pushMessage.setDateInfo(jsonObject2.getString("dateInfo"));
                            pushMessage.setIsDel(jsonObject2.getString("IsDel"));
                            pushMessage.setIsRead(jsonObject2.getString("IsRead"));
                            pushMessage.setMsgCenter(jsonObject2.getString("MsgCenter"));
                            pushMessage.setMsgPk(jsonObject2.getString("msgPk"));
                            pushMessage.setTitle(jsonObject2.getString("title"));
                            pushMessage.setUserNo(jsonObject2.getString("UserNo"));
                            msgs.add(pushMessage);
                        }
                        adapter.notifyDataSetChanged();
                    }else if(flag.equals("0001")){
                        ToastUtil.showShort(this, "没有消息");
                    }else {
                        ToastUtil.showShort(this, "获取消息失败");
                    }
                    hideProgressDialog();
                }
                break;
            case TASK_DEL_MSG:
                if (returnObject!=null){
                    JSONObject jsonObject = (JSONObject)returnObject;
                    String flag = jsonObject.getString("flag");
                    if (flag.equals("0000")){
                        ToastUtil.showShort(this, "删除消息成功");
                        getMsgs();
                    }else {
                        hideProgressDialog();
                        ToastUtil.showShort(this, "删除消息失败");
                    }

                }
                break;
            default:
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_msg;
    }
}
