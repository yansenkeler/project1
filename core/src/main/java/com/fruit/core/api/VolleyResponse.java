package com.fruit.core.api;

import com.android.volley.VolleyError;

/**
 * Volley http请求返回处理接口
 * Created by user on 2016/2/24.
 */
public interface VolleyResponse<T> {
    void onResponse(T response, int taskid);
    void onErrorResponse(VolleyError error, int taskid);
}
