package com.gxg.entities;

import org.json.JSONObject;

/**
 * Created by 郭欣光 on 2018/6/2.
 */


public class ScenicArea {

    private String saId;
    private String cityId;
    private String saImg;
    private String saAr;
    private String saIntro;

    public String getSaId() {
        return saId;
    }

    public void setSaId(String saId) {
        this.saId = saId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getSaImg() {
        return saImg;
    }

    public void setSaImg(String saImg) {
        this.saImg = saImg;
    }

    public String getSaAr() {
        return saAr;
    }

    public void setSaAr(String saAr) {
        this.saAr = saAr;
    }

    public String getSaIntro() {
        return saIntro;
    }

    public void setSaIntro(String saIntro) {
        this.saIntro = saIntro;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("sa_id", saId);
        jsonObject.accumulate("city_id", cityId);
        jsonObject.accumulate("sa_img", saImg);
        jsonObject.accumulate("sa_ar", saAr);
        jsonObject.accumulate("sa_intro", saIntro);
        return jsonObject;
    }
}
