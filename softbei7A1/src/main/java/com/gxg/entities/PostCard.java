package com.gxg.entities;

import org.json.JSONObject;

/**
 * Created by 郭欣光 on 2018/6/4.
 */

public class PostCard {

    private String pcId;
    private String cityId;
    private String userId;
    private String image;
    private String sendWord;

    public String getPcId() {
        return pcId;
    }

    public void setPcId(String pcId) {
        this.pcId = pcId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSendWord() {
        return sendWord;
    }

    public void setSendWord(String sendWord) {
        this.sendWord = sendWord;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("pc_id", pcId);
        jsonObject.accumulate("city_id", cityId);
        jsonObject.accumulate("user_id", userId);
        jsonObject.accumulate("image", image);
        jsonObject.accumulate("send_word", sendWord);
        return jsonObject;
    }
}
