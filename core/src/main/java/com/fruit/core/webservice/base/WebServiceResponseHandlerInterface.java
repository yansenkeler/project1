package com.fruit.core.webservice.base;

/**
 * Created by liangchen on 15/7/3.
 */
public interface WebServiceResponseHandlerInterface {

    void onStart();

    void onHandleResult(int statusCode, Object response);

}
