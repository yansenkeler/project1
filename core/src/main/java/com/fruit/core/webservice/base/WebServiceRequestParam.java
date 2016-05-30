package com.fruit.core.webservice.base;

import java.util.Map;

/**
 * Created by liangchen on 15/7/3.
 */
public class WebServiceRequestParam {
    private String methodName;
    private String serviceUrl;
    private String serviceNameSpace;

    private Map<String,String> requestProperty;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getServiceNameSpace() {
        return serviceNameSpace;
    }

    public void setServiceNameSpace(String serviceNameSpace) {
        this.serviceNameSpace = serviceNameSpace;
    }

    public Map<String, String> getRequestProperty() {
        return requestProperty;
    }

    public void setRequestProperty(Map<String, String> requestProperty) {
        this.requestProperty = requestProperty;
    }
}
