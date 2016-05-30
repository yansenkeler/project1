package com.fruit.client.activity;

import com.fruit.client.R;
import android.os.Bundle;

import com.fruit.core.activity.templet.NaviActivity;

/**
 * Created by qianyx on 16-5-8.
 */
public class UnfinishEventActivity extends NaviActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTitle("未完成事件");
        setTopbarBackground(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_unfinish_event;
    }
}
