package com.fruit.client.util;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by John on 2016/5/12.
 */
public class LonLatUtil {
    private static double lat = 31.22997;
    private static double lon = 121.640756;
    public static double x_pi = lat * lon / 180.0;

    public static double getDistanceFromXtoY(double lonX, double latX, double lonY, double latY){
        double pk = 180 / 3.14169;
        double a1 = latX / pk;
        double a2 = lonX / pk;
        double b1 = latY / pk;
        double b2 = lonY / pk;
        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);
        return 6366000 * tt;
    }

    //解密成为国测局坐标
    public static LatLng bd_decrypt(double bd_lat, double bd_lon)
    {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new LatLng(gg_lat, gg_lon);
    }
    //加密成为百度坐标
    public static LatLng bd_encrypt(double gg_lat, double gg_lon)
    {
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new LatLng(bd_lat, bd_lon);
    }
}
