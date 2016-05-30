package com.fruit.common.string;

import com.fruit.common.nfc.util.Converter;

import java.io.UnsupportedEncodingException;

/**
 * Created by liangchen on 15/7/20.
 */
public class StringEncodeing {

    public static String encodeToGB(String encoding) {

        byte[] b1 = new byte[0];

        String returnString="";
        try {
            b1 = encoding.getBytes("gb2312");
            returnString = new String(b1, "gb2312"); //编码解码不同，乱码
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return returnString;
    }
}
