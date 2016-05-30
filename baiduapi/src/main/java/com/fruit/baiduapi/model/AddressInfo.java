package com.fruit.baiduapi.model;

import java.io.Serializable;

/**
 * Created by liangchen on 15/7/12.
 */
public class AddressInfo implements Serializable {
        private static final long serialVersionUID = -758459502806858414L;
        /**
         * 经度
         */
        private double latitude;
        /**
         * 纬度
         */
        private double longitude;
        /**
         * 图片ID，真实项目中可能是图片路径
         */
        private int imgId;
        /**
         * 地址对应的名字名称
         */
        private String name;
        /**
         * 距离
         */
        private String distance;


        public AddressInfo()
        {
        }

        public AddressInfo(double latitude, double longitude, int imgId, String name,
            String distance)
        {
            super();
            this.latitude = latitude;
            this.longitude = longitude;
            this.imgId = imgId;
            this.name = name;
            this.distance = distance;
        }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public String getName()
    {
        return name;
    }

    public int getImgId()
    {
        return imgId;
    }

    public void setImgId(int imgId)
    {
        this.imgId = imgId;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDistance()
    {
        return distance;
    }

    public void setDistance(String distance)
    {
        this.distance = distance;
    }

}
