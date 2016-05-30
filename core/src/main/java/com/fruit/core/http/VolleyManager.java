package com.fruit.core.http;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fruit.common.application.ApplicationUtils;
import com.fruit.core.api.FastJsonRequest;
import com.fruit.core.api.VolleyResponse;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * Created by Keler Qian on 2016/02/25.
 */
public class VolleyManager {
    private static VolleyManager mVolleyManager = null;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    /**
     * @param context
     */
    public VolleyManager(Context context) {

        mRequestQueue = Volley.newRequestQueue(context, new OkHttp3Stack(new OkHttpClient()));

        mImageLoader = new ImageLoader(mRequestQueue,
                new LruBitmapCache(context));
    }

    /**
     * @return VolleyManager instance
     */
    public static synchronized VolleyManager newInstance(Context context) {
        if (mVolleyManager == null) {
            mVolleyManager = new VolleyManager(context);
        }
        return mVolleyManager;
    }

    private <T> Request<T> add(Request<T> request) {
        return mRequestQueue.add(request);//添加请求到队列
    }

    /**
     * @param tag
     * @param url
     * @param volleyResponse
     * @return
     */
    public StringRequest StrRequest(Object tag, String url, final VolleyResponse<String> volleyResponse, final int taskid) {
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                volleyResponse.onResponse(response, taskid);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponse.onErrorResponse(error, taskid);
            }
        });
        request.setTag(tag);
        add(request);
        return request;
    }

    /**
     *
     * @param tag
     * @param method
     * @param params
     * @param url
     * @param volleyResponse
     * @param taskid
     * @return
     */
    public StringRequest StrRequest(Object tag, int method, final HashMap<String, String> params, String url, final VolleyResponse<String> volleyResponse, final int taskid) {
        StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                volleyResponse.onResponse(response, taskid);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponse.onErrorResponse(error, taskid);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        request.setTag(tag);
        add(request);
        return request;
    }

    /**
     * ImageRequest
     *
     * @param tag
     * @param url
     * @param volleyResponse
     * @param maxWidth
     * @param maxHeight
     * @param scaleType
     * @param decodeConfig
     * @return
     */
    public ImageRequest ImageRequest(Object tag, String url, final VolleyResponse<Bitmap> volleyResponse,
                                     int maxWidth, int maxHeight, ImageView.ScaleType scaleType,
                                     Bitmap.Config decodeConfig, final int taskid) {
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                volleyResponse.onResponse(response, taskid);
            }
        }, maxWidth, maxHeight, scaleType,
                decodeConfig, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponse.onErrorResponse(error, taskid);
            }
        });
        request.setTag(tag);
        add(request);
        return request;
    }

    /**
     * ImageLoader
     *
     * @param imageView
     * @param imgViewUrl
     * @param defaultImageResId
     * @param errorImageResId
     */
    public void ImageLoaderRequest(ImageView imageView, String imgViewUrl, int defaultImageResId,
                                   int errorImageResId) {
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, defaultImageResId,
                errorImageResId);
        mImageLoader.get(imgViewUrl, listener);
    }


    /**
     * ImageLoader指定图片大小
     * @param imageView
     * @param imgViewUrl
     * @param defaultImageResId
     * @param errorImageResId
     * @param maxWidth
     * @param maxHeight
     */
    public void ImageLoaderRequest(ImageView imageView, String imgViewUrl, int defaultImageResId,
                                   int errorImageResId, int maxWidth, int maxHeight) {
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, defaultImageResId,
                errorImageResId);
        mImageLoader.get(imgViewUrl, listener, maxWidth, maxHeight);
    }

    /**
     * Get
     * @param tag
     * @param url
     * @param clazz
     * @param volleyResponse
     * @param <T>
     * @return
     */
    public <T> FastJsonRequest<T> JsonGetRequest(Object tag, String url, Class<T> clazz, final VolleyResponse<T> volleyResponse, final int taskid) {
        FastJsonRequest<T> request = new FastJsonRequest<T>(url, clazz, new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                volleyResponse.onResponse(response, taskid);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponse.onErrorResponse(error, taskid);
            }
        });
        request.setTag(tag);
        add(request);
        return request;
    }

    /**
     * Post
     * @param tag
     * @param params
     * @param url
     * @param clazz
     * @param volleyResponse
     * @param <T>
     * @return
     */
    public <T> FastJsonRequest<T> JsonPostRequest(Object tag, final String charset, final String mRequestBody, Map<String, String> params, String url, Class<T> clazz, final VolleyResponse<T> volleyResponse, final int taskid) {
        FastJsonRequest<T> request = new FastJsonRequest<T>(Request.Method.POST, params, url, clazz, new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                volleyResponse.onResponse(response, taskid);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponse.onErrorResponse(error, taskid);
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
            }

            @Override
            public byte[] getBody() {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes(charset);
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            mRequestBody, charset);
                    return null;
                }
            }
        };
        request.setTag(tag);
        add(request);
        return request;
    }

    /**
     * 取消请求
     * @param tag
     */
    public void cancel(Object tag) {
        mRequestQueue.cancelAll(tag);
    }
}