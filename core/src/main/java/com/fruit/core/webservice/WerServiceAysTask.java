package com.fruit.core.webservice;


import android.os.AsyncTask;

import com.fruit.core.webservice.base.WebServiceRequestParam;
import com.fruit.core.webservice.base.WebServiceResponseHandlerInterface;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by liangchen on 15/7/3.
 */
public class WerServiceAysTask extends AsyncTask<WebServiceRequestParam, String, SoapObject> {

    private WebServiceResponseHandlerInterface responseHandlerInterface;
    private int taskId;

    public WerServiceAysTask(int taskId,WebServiceResponseHandlerInterface responseHandlerInterface ) {
        this.responseHandlerInterface = responseHandlerInterface;
        this.taskId = taskId;
    }

    @Override
    protected void onPreExecute() {
        responseHandlerInterface.onStart();
    }

    @Override
    protected SoapObject doInBackground(WebServiceRequestParam... params) {
        // TODO Auto-generated method stub
        WebServiceRequestParam param = params[0];

        // 调用的方法
        String methodName = param.getMethodName();
        // 创建httptransportSE传输对象
        HttpTransportSE ht = new HttpTransportSE(param.getServiceUrl());
        ht.debug = true;
        // 实例化SoapObject对象
        SoapObject soapObject = new SoapObject(param.getServiceNameSpace(), methodName);

        // 添加请求参数
        Map<String,String> propertyMap = param.getRequestProperty();
        if (propertyMap!=null){
            for (String key:propertyMap.keySet()){
                soapObject.addProperty(key,propertyMap.get(key));
            }
        }

        // 使用soap1.1协议创建envelop对象
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.bodyOut = soapObject;
        // 设置与.NET提供的webservice保持较好的兼容性
        envelope.dotNet = true;

        // 调用webservice
        try {
            ht.call(param.getServiceNameSpace() + methodName, envelope);
            if (envelope.getResponse() != null) {
                // 获取服务器响应返回的SOAP消息
//                androidHttpTransport.call(nameSpace + method, envelope);
//                result = envelope.getResponse().toString();
                SoapObject result = (SoapObject) envelope.bodyIn;
                SoapObject detail = (SoapObject) result.getProperty(methodName
                        + "Result");
                String response = envelope.getResponse().toString();
                System.out.print(response);

                // 解析服务器响应的SOAP消息
                return detail;
            }
        } catch (SoapFault e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(SoapObject result) {
        // TODO Auto-generated method stub
        if (result!=null){
            responseHandlerInterface.onHandleResult(200, result);
        }else{
            responseHandlerInterface.onHandleResult(-1,"网络请求错误");
        }

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

}
