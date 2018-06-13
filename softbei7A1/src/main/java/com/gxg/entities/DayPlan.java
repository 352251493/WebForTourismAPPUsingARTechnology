package com.gxg.entities;

import org.json.JSONObject;


/**
 * Created by 郭欣光 on 2018/6/3.
 */


public class DayPlan {

    private String dayId;
    private String travelId;
    private String scenicAreaId;
    private String vehicle;
    private String beginTime;
    private String endTime;

    public String getDayId() {
        return dayId;
    }

    public void setDayId(String dayId) {
        this.dayId = dayId;
    }

    public String getScenicAreaId() {
        return scenicAreaId;
    }

    public void setScenicAreaId(String scenicAreaId) {
        this.scenicAreaId = scenicAreaId;
    }

    public String getTravelId() {
        return travelId;
    }

    public void setTravelId(String travelId) {
        this.travelId = travelId;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("day_id", dayId);
        jsonObject.accumulate("travel_id", travelId);
        jsonObject.accumulate("scenic_area_id", scenicAreaId);
        jsonObject.accumulate("vehicle", vehicle);
        jsonObject.accumulate("begin_time", beginTime);
        jsonObject.accumulate("end_time", endTime);
        return jsonObject;
    }
}
