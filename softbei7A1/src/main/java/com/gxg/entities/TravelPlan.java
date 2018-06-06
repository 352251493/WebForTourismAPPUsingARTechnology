package com.gxg.entities;

import org.json.JSONObject;

/**
 * Created by 郭欣光 on 2018/6/3.
 */


public class TravelPlan {
    private String cityId;
    private String vehicle;
    private String beginDate;
    private String endDate;
    private String travelId;
    private String userId;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTravelId() {
        return travelId;
    }

    public void setTravelId(String travelId) {
        this.travelId = travelId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("city_id", cityId);
        jsonObject.accumulate("vehicle", vehicle);
        jsonObject.accumulate("begin_date", beginDate);
        jsonObject.accumulate("end_date", endDate);
        jsonObject.accumulate("travel_id", travelId);
        jsonObject.accumulate("user_id", userId);
        return jsonObject;
    }
}
