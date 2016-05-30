package com.fruit.common.webservice;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by liangchen on 15/7/17.
 */
public class SoapPrase {

    public static String getSoapString(SoapObject soapObject,String id){
//        String custId =  soapObject.getPropertyAsString(id);
        String returnString =  soapObject.getPropertyAsString(id);

        if (returnString.equals("anyType{}")){
            return "";
        }else{
            return returnString;
        }
    }
}
