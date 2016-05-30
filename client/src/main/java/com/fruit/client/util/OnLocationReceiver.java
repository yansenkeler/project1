package com.fruit.client.util;

import com.baidu.location.BDLocation;

/**
 * Created by user on 2016/3/11.
 */
public interface OnLocationReceiver {
    void onLatAndLonReceiver(BDLocation mBDLocation);
    void onErrorReceiver();
}
