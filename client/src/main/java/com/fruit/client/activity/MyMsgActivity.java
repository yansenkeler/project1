package com.fruit.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fruit.client.R;
import com.fruit.client.adapter.MyMsgAdapter;
import com.fruit.core.activity.templet.NaviActivity;
import com.fruit.core.db.DBUtil;
import com.fruit.core.db.models.gen.msg;

import java.util.ArrayList;

/**
 * Created by qianyx on 16-5-8.
 */
public class MyMsgActivity extends NaviActivity {

    private ListView listView;
    private MyMsgAdapter adapter;

    private ArrayList<msg> msgs = new ArrayList<>();

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
                msg msg = msgs.get(position);
                Intent intent = new Intent(MyMsgActivity.this, EventDetailEditActivity.class);
                intent.putExtra("bill_no", msg.getBillno());
                startActivity(intent);
                DBUtil.setIsRead(msg.getBillno(), true);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        msgs.clear();
        msgs.addAll(DBUtil.getMsgs());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_msg;
    }
}
