package com.fruit.client.object;

/**
 * Created by John on 2016/5/13.
 */
public class RoutePoint {
    private String id;
    private String planPk;
    private String lon;
    private String lat;

    public RoutePoint() {

    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlanPk() {
        return planPk;
    }

    public void setPlanPk(String planPk) {
        this.planPk = planPk;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    private String orderId;
    private String routeCode;
}
