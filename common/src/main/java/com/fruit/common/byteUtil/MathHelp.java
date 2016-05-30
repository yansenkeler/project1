package com.fruit.common.byteUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by user on 2015/12/22.
 */
public class MathHelp {

    /*
     *把double转换成字符串，舍掉小数点后面的位数
     */
    public static String decodeDoubleToString(double ddd){
        BigDecimal bigDecimal = new BigDecimal(ddd);
        String s = bigDecimal.toPlainString();
        if (s.length()>0){
            if (s.contains(".")){
                return s.split("\\.")[0];
            }else {
                return s;
            }
//            if (!s.contains(".")){
//                return s;
//            }else if(!s.endsWith("0") && !s.contains("E")){
//                return new DecimalFormat("0").format(ddd);
//            }else if (s.contains("E")){
//                BigDecimal bd = new BigDecimal(ddd);
//                String string = bd.toPlainString();
//                if (string.contains(".")){
//                    return string.split("\\.")[0];
//                }else {
//                    return string;
//                }
//            } else {
//                String[] ss = s.split("\\.");
//                String temp = ss[1].replace("0", " ");
//                temp = temp.trim();
//                temp = temp.replace(" ", "0");
//                if (temp.length()==0){
//                    return ss[0];
//                }else {
//                    return ss[0]+"."+temp;
//                }
//            }
        }else {
            return s;
        }

    }
}
